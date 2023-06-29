package net.minecraft.server.network;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.CryptManager;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerLoginServer implements INetHandlerLoginServer, ITickable
{
    private static final AtomicInteger AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    private final byte[] verifyToken = new byte[4];
    private final MinecraftServer server;
    public final NetworkManager networkManager;
    private NetHandlerLoginServer.LoginState currentLoginState = NetHandlerLoginServer.LoginState.HELLO;

    /** How long has player been trying to login into the server. */
    private int connectionTimer;
    private GameProfile loginGameProfile;
    private final String serverId = "";
    private SecretKey secretKey;
    private EntityPlayerMP player;

    public NetHandlerLoginServer(MinecraftServer serverIn, NetworkManager networkManagerIn)
    {
        server = serverIn;
        networkManager = networkManagerIn;
        RANDOM.nextBytes(verifyToken);
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        if (currentLoginState == NetHandlerLoginServer.LoginState.READY_TO_ACCEPT)
        {
            tryAcceptPlayer();
        }
        else if (currentLoginState == NetHandlerLoginServer.LoginState.DELAY_ACCEPT)
        {
            EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUUID(loginGameProfile.getId());

            if (entityplayermp == null)
            {
                currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                server.getPlayerList().initializeConnectionToPlayer(networkManager, player);
                player = null;
            }
        }

        if (connectionTimer++ == 600)
        {
            func_194026_b(new TranslatableComponent("multiplayer.disconnect.slow_login"));
        }
    }

    public void func_194026_b(Component p_194026_1_)
    {
        try
        {
            LOGGER.info("Disconnecting {}: {}", getConnectionInfo(), p_194026_1_.asString());
            networkManager.sendPacket(new SPacketDisconnect(p_194026_1_));
            networkManager.closeChannel(p_194026_1_);
        }
        catch (Exception exception)
        {
            LOGGER.error("Error whilst disconnecting player", exception);
        }
    }

    public void tryAcceptPlayer()
    {
        if (!loginGameProfile.isComplete())
        {
            loginGameProfile = getOfflineProfile(loginGameProfile);
        }

        String s = server.getPlayerList().allowUserToConnect(networkManager.getRemoteAddress(), loginGameProfile);

        if (s != null)
        {
            func_194026_b(new TranslatableComponent(s));
        }
        else
        {
            currentLoginState = NetHandlerLoginServer.LoginState.ACCEPTED;

            if (server.getNetworkCompressionThreshold() >= 0 && !networkManager.isLocalChannel())
            {
                networkManager.sendPacket(new SPacketEnableCompression(server.getNetworkCompressionThreshold()), new ChannelFutureListener()
                {
                    public void operationComplete(ChannelFuture p_operationComplete_1_) throws Exception
                    {
                        networkManager.setCompressionThreshold(server.getNetworkCompressionThreshold());
                    }
                });
            }

            networkManager.sendPacket(new SPacketLoginSuccess(loginGameProfile));
            EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUUID(loginGameProfile.getId());

            if (entityplayermp != null)
            {
                currentLoginState = NetHandlerLoginServer.LoginState.DELAY_ACCEPT;
                player = server.getPlayerList().createPlayerForUser(loginGameProfile);
            }
            else
            {
                server.getPlayerList().initializeConnectionToPlayer(networkManager, server.getPlayerList().createPlayerForUser(loginGameProfile));
            }
        }
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    public void onDisconnect(Component reason)
    {
        LOGGER.info("{} lost connection: {}", getConnectionInfo(), reason.asString());
    }

    public String getConnectionInfo()
    {
        return loginGameProfile != null ? loginGameProfile + " (" + networkManager.getRemoteAddress() + ")" : String.valueOf(networkManager.getRemoteAddress());
    }

    public void processLoginStart(CPacketLoginStart packetIn)
    {
        Preconditions.checkState(currentLoginState == NetHandlerLoginServer.LoginState.HELLO, "Unexpected hello packet");
        loginGameProfile = packetIn.getProfile();

        if (server.isServerInOnlineMode() && !networkManager.isLocalChannel())
        {
            currentLoginState = NetHandlerLoginServer.LoginState.KEY;
            networkManager.sendPacket(new SPacketEncryptionRequest("", server.getKeyPair().getPublic(), verifyToken));
        }
        else
        {
            currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
        }
    }

    public void processEncryptionResponse(CPacketEncryptionResponse packetIn)
    {
        Preconditions.checkState(currentLoginState == NetHandlerLoginServer.LoginState.KEY, "Unexpected key packet");
        PrivateKey privatekey = server.getKeyPair().getPrivate();

        if (!Arrays.equals(verifyToken, packetIn.getVerifyToken(privatekey)))
        {
            throw new IllegalStateException("Invalid nonce!");
        }
        else
        {
            secretKey = packetIn.getSecretKey(privatekey);
            currentLoginState = NetHandlerLoginServer.LoginState.AUTHENTICATING;
            networkManager.enableEncryption(secretKey);
            (new Thread("User Authenticator #" + AUTHENTICATOR_THREAD_ID.incrementAndGet())
            {
                public void run()
                {
                    GameProfile gameprofile = loginGameProfile;

                    try
                    {
                        String s = (new BigInteger(CryptManager.getServerIdHash("", server.getKeyPair().getPublic(), secretKey))).toString(16);
                        loginGameProfile = server.getInstanceSessionService().hasJoinedServer(new GameProfile(null, gameprofile.getName()), s, func_191235_a());

                        if (loginGameProfile != null)
                        {
                            LOGGER.info("UUID of player {} is {}", loginGameProfile.getName(), loginGameProfile.getId());
                            currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                        }
                        else if (server.isSinglePlayer())
                        {
                            LOGGER.warn("Failed to verify username but will let them in anyway!");
                            loginGameProfile = getOfflineProfile(gameprofile);
                            currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                        }
                        else
                        {
                            func_194026_b(new TranslatableComponent("multiplayer.disconnect.unverified_username"));
                            LOGGER.error("Username '{}' tried to join with an invalid session", gameprofile.getName());
                        }
                    }
                    catch (AuthenticationUnavailableException var3)
                    {
                        if (server.isSinglePlayer())
                        {
                            LOGGER.warn("Authentication servers are down but will let them in anyway!");
                            loginGameProfile = getOfflineProfile(gameprofile);
                            currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                        }
                        else
                        {
                            func_194026_b(new TranslatableComponent("multiplayer.disconnect.authservers_down"));
                            LOGGER.error("Couldn't verify username because servers are unavailable");
                        }
                    }
                }
                @Nullable
                private InetAddress func_191235_a()
                {
                    SocketAddress socketaddress = networkManager.getRemoteAddress();
                    return server.func_190518_ac() && socketaddress instanceof InetSocketAddress ? ((InetSocketAddress)socketaddress).getAddress() : null;
                }
            }).start();
        }
    }

    protected GameProfile getOfflineProfile(GameProfile original)
    {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(StandardCharsets.UTF_8));
        return new GameProfile(uuid, original.getName());
    }

    enum LoginState
    {
        HELLO,
        KEY,
        AUTHENTICATING,
        READY_TO_ACCEPT,
        DELAY_ACCEPT,
        ACCEPTED
    }
}
