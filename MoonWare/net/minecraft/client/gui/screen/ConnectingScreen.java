package net.minecraft.client.gui.screen;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moonware.client.qol.QOLConfig;
import org.moonware.client.qol.via.MWVia;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectingScreen extends Screen {
    private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
    private static final Logger LOGGER = LogManager.getLogger();
    private NetworkManager manager;
    private String state = "Инициализация...";
    private boolean cancel;
    private final Screen previousScreen;

    public ConnectingScreen(Screen parent, Minecraft mcIn, ServerData serverDataIn) {
        previousScreen = parent;
        ServerAddress serveraddress = ServerAddress.fromString(serverDataIn.ip);
        mcIn.loadWorld(null);
        mcIn.setServerData(serverDataIn);
        connect(serveraddress.getIP(), serveraddress.getPort());
    }

    public ConnectingScreen(Screen parent, Minecraft mcIn, String hostName, int port) {
        previousScreen = parent;
        mcIn.loadWorld(null);
        connect(hostName, port);
    }

    private void connect(String ip, int port) {
        LOGGER.info("Connecting to {}, {}", ip, Integer.valueOf(port));
        new Thread(() -> {
            InetAddress inetaddress = null;
            try {
                if (cancel) return;
                state = "Получение IP...";
                inetaddress = InetAddress.getByName(ip);
                if (cancel) return;
                state = "Обработка ViaVersion...";
                if (QOLConfig.via == null) {
                    MWVia.version = ProtocolVersion.unknown;
                } else if (QOLConfig.via.isKnown()) {
                    MWVia.version = QOLConfig.via;
                } else {
                    MWVia.version = MWVia.getProtocol(inetaddress, port);
                }
                state = "Подключение...";
                manager = NetworkManager.remote(inetaddress, port);
                manager.setNetHandler(new NetHandlerLoginClient(manager, minecraft, previousScreen));
                manager.sendPacket(new C00Handshake(ip, port, EnumConnectionState.LOGIN));
                manager.sendPacket(new CPacketLoginStart(Minecraft.getSession().getProfile()));
                state = "Вход...";
            } catch (UnknownHostException unknownhostexception) {
                if (cancel) {
                    return;
                }

                LOGGER.error("Couldn't connect to server", unknownhostexception);
                Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.openScreen(new DisconnectScreen(previousScreen, new TranslatableComponent("connect.failed"), new TranslatableComponent("disconnect.genericReason", "Unknown host"))));
            } catch (Exception exception) {
                if (cancel) {
                    return;
                }

                LOGGER.error("Couldn't connect to server", exception);
                String s1 = inetaddress + ":" + port;
                String s = exception.toString().replaceAll(s1, "");
                Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.openScreen(new DisconnectScreen(previousScreen, new TranslatableComponent("connect.failed"), new TranslatableComponent("disconnect.genericReason", s))));
            }
        }, "Server Connector #" + CONNECTION_ID.incrementAndGet()).start();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update() {
        if (manager != null) {
            if (manager.isChannelOpen()) {
                manager.processReceivedPackets();
            } else {
                manager.checkDisconnected();
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c) {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init() {
        widgets.clear();
        widgets.add(new ButtonWidget(0, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel")));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button) {
        if (button.id == 0) {
            cancel = true;

            if (manager != null) {
                manager.closeChannel(new TextComponent("Aborted"));
            }

            Minecraft.openScreen(previousScreen);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        drawCenteredString(font, state, width / 2, height / 2 - 50, 16777215);
        super.draw(mouseX, mouseY, partialTick);
    }
}