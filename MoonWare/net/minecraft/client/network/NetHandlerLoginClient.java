package net.minecraft.client.network;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.math.BigInteger;
import java.security.PublicKey;
import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DisconnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.util.CryptManager;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerLoginClient implements INetHandlerLoginClient
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft mc;
    @Nullable
    private final Screen previousScreen;
    private final NetworkManager networkManager;
    private GameProfile gameProfile;

    public NetHandlerLoginClient(NetworkManager networkManagerIn, Minecraft mcIn, @Nullable Screen previousScreenIn)
    {
        networkManager = networkManagerIn;
        mc = mcIn;
        previousScreen = previousScreenIn;
    }

    public void handleEncryptionRequest(SPacketEncryptionRequest packetIn)
    {
        SecretKey secretkey = CryptManager.createNewSharedKey();
        String s = packetIn.getServerId();
        PublicKey publickey = packetIn.getPublicKey();
        String s1 = (new BigInteger(CryptManager.getServerIdHash(s, publickey, secretkey))).toString(16);
        {
            try
            {
                getSessionService().joinServer(Minecraft.getSession().getProfile(), Minecraft.getSession().getToken(), s1);
            }
            catch (AuthenticationUnavailableException var7)
            {
                networkManager.closeChannel(new TranslatableComponent("disconnect.loginFailedInfo", new TranslatableComponent("disconnect.loginFailedInfo.serversUnavailable")));
                return;
            }
            catch (InvalidCredentialsException var8)
            {
                networkManager.closeChannel(new TranslatableComponent("disconnect.loginFailedInfo", new TranslatableComponent("disconnect.loginFailedInfo.invalidSession")));
                return;
            }
            catch (AuthenticationException authenticationexception)
            {
                networkManager.closeChannel(new TranslatableComponent("disconnect.loginFailedInfo", authenticationexception.getMessage()));
                return;
            }
        }

        networkManager.sendPacket(new CPacketEncryptionResponse(secretkey, publickey, packetIn.getVerifyToken()), new GenericFutureListener < Future <? super Void >> ()
        {
            public void operationComplete(Future <? super Void > p_operationComplete_1_) throws Exception
            {
                networkManager.enableEncryption(secretkey);
            }
        });
    }

    private MinecraftSessionService getSessionService()
    {
        return Minecraft.getSessionService();
    }

    public void handleLoginSuccess(SPacketLoginSuccess packetIn)
    {
        gameProfile = packetIn.getProfile();
        networkManager.setConnectionState(EnumConnectionState.PLAY);
        networkManager.setNetHandler(new NetHandlerPlayClient(mc, previousScreen, networkManager, gameProfile));
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    public void onDisconnect(Component reason)
    {
        Minecraft.openScreen(new DisconnectScreen(previousScreen, new TranslatableComponent("connect.failed"), reason));
    }

    public void handleDisconnect(SPacketDisconnect packetIn)
    {
        networkManager.closeChannel(packetIn.getReason());
    }

    public void handleEnableCompression(SPacketEnableCompression packetIn)
    {
        if (!networkManager.isLocalChannel())
        {
            networkManager.setCompressionThreshold(packetIn.getCompressionThreshold());
        }
    }
}
