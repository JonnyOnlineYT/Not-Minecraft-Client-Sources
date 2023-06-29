package org.moonware.client;

import baritone.api.BaritoneAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.moonware.client.cmd.CommandManager;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.game.EventShutdownClient;
import org.moonware.client.event.events.impl.input.EventInputKey;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.FeatureManager;
import org.moonware.client.feature.impl.combat.AntiBot;
import org.moonware.client.files.FileManager;
import org.moonware.client.files.impl.FriendConfig;
import org.moonware.client.files.impl.MacroConfig;
import org.moonware.client.files.impl.XrayConfig;
import org.moonware.client.friend.FriendManager;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.math.RotationHelper;
import org.moonware.client.helpers.render.BlurUtil;
import org.moonware.client.helpers.render.cosmetic.CosmeticRender;
import org.moonware.client.helpers.render.cosmetic.impl.DragonWing;
import org.moonware.client.macro.Macro;
import org.moonware.client.macro.MacroManager;
import org.moonware.client.qol.via.MWVia;
import org.moonware.client.settings.config.ConfigManager;
import org.moonware.client.ui.celestialgui.ClickGuiScreen;
import org.moonware.client.ui.govnogui.MainScreen;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.sqgui.GuiScreen;
import org.moonware.client.ui.windowClickGui.WildGui;
import org.moonware.client.utils.MWDiscordRPC;

import java.io.IOException;
import java.util.Objects;

public class MoonWare implements Helper {
    public static final String BUILD = Objects.toString(MoonWare.class.getPackage().getImplementationVersion(), "UNKNOWN");
    public static final String VERSION = Objects.toString(MoonWare.class.getPackage().getSpecificationVersion(), "DEV");
    public static final String UUID = System.getProperty("moonware.uuid", "000");
    public static final String LICENSE = System.getProperty("moonware.license", "dev");
    public static final Logger LOGGER = LogManager.getLogger("MoonWare");

    public static FeatureManager featureManager = new FeatureManager();
    public static org.moonware.client.ui.guiWithScroll.ClickGuiScreen clickGui;
    public static GuiScreen guiNew;
    public static ClickGuiScreen celestialGui;
    public static WildGui windowGui;
    public static MainScreen govnogui;

    public static CommandManager commandManager;
    public static ConfigManager configManager;
    public static MacroManager macroManager;
    public static FileManager fileManager;
    public static FriendManager friendManager;
    public static AntiBot antiBot;
    public static RotationHelper.Rotation rotation;
    public static BlurUtil blurUtil;
    public static NotificationManager notificationManager;
    public static double createAnimation(double value) {
        return Math.sqrt(1 - Math.pow(value - 1, 2));
    }
    public static void load() throws IOException {
        Display.setTitle("MoonWare 1.12.2 (" + VERSION + "/" + BUILD + ")");
        (fileManager = new FileManager()).loadFiles();
        LOGGER.info("FileManager Loaded");
        featureManager = new FeatureManager();
        LOGGER.info("FeatureManager Loaded");
        clickGui = new org.moonware.client.ui.guiWithScroll.ClickGuiScreen();
        LOGGER.info("org.moonware.client.ui.clickgui.GuiScreen Loaded");
        celestialGui = new ClickGuiScreen();
        guiNew = new GuiScreen();
        windowGui = new WildGui();
        govnogui = new MainScreen();
        commandManager = new CommandManager();
        LOGGER.info("CommandManager Loaded");
        configManager = new ConfigManager();
        LOGGER.info("ConfigManager Loaded");
        notificationManager = new NotificationManager();
        LOGGER.info("NotificationManager Loaded");
        macroManager = new MacroManager();
        LOGGER.info("MacroManager Loaded");
        friendManager = new FriendManager();
        LOGGER.info("FriendManager Loaded");
        antiBot = new AntiBot();
        LOGGER.info("AntiBot Loaded");
        rotation = new RotationHelper.Rotation();
        LOGGER.info("RotationHelper.Rotation Loaded");
        blurUtil = new BlurUtil();
        LOGGER.info("BlurUtil Loaded");
        BaritoneAPI.getProvider().getPrimaryBaritone();
        LOGGER.info("BaritoneAPI Loaded");
        MWDiscordRPC.start();
        MWVia.load();

        try {
            fileManager.getFile(FriendConfig.class).loadFile();
        } catch (Exception e) {
        }

        try {
            fileManager.getFile(MacroConfig.class).loadFile();
        } catch (Exception e) {

        }
        try {
            fileManager.getFile(XrayConfig.class).loadFile();
        } catch (Exception e) {
        }
        try {
            new DragonWing();
            for (RenderPlayer render : Minecraft.getRenderManager().getSkinMap().values()) {
                render.addLayer(new CosmeticRender(render));
            }
        } catch (Exception ignored) {}
        EventManager.register(rotation);
        EventManager.register(new MoonWare());
    }

    @EventTarget
    public void shutDown(EventShutdownClient event) {
        EventManager.unregister(this);
        (fileManager = new FileManager()).saveFiles();
        MWDiscordRPC.stop();
    }

    @EventTarget
    public void onInputKey(EventInputKey event) {
        for (Feature feature : featureManager.getFeatureList()) {
            if (feature.getBind() == event.getKey()) {
                feature.toggle();
            }
        }
        for (Macro macro : macroManager.getMacros()) {
            if (macro.getKey() == Keyboard.getEventKey()) {
                if (Minecraft.player.getHealth() > 0 && Minecraft.player != null) {
                    Minecraft.player.sendChatMessage(macro.getValue());
                }
            }
        }
    }
}
