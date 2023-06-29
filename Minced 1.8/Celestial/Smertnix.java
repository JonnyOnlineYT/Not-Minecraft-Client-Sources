package Celestial;


import Celestial.cmd.CommandManager;
import Celestial.cmd.macro.MacroManager;
import Celestial.drag.DragModern;
import Celestial.event.EventManager;
import Celestial.event.EventTarget;
import Celestial.event.events.impl.input.EventInputKey;
import Celestial.files.FileManager;
import Celestial.files.impl.FriendConfig;
import Celestial.files.impl.HudConfig;
import Celestial.files.impl.MacroConfig;
import Celestial.friend.FriendManager;
import Celestial.module.Module;
import Celestial.module.ModuleManager;
import Celestial.utils.math.RotationHelper;
import Celestial.utils.math.ShaderShell;
import Celestial.utils.math.TPSUtils;
import Celestial.utils.otherutils.gayutil.ScaleUtils;
//import viamcp.ViaMCP;
import Celestial.ui.celestun4ik.guiscreencomponent;
import Celestial.ui.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;

public class Smertnix {
    public Long time;
    public static ScaleUtils scale = new ScaleUtils(2);
    public ModuleManager featureManager;
    public FileManager fileManager;
    public static long playTimeStart = 0;
    public DragModern draggableHUD;

    public MacroManager macroManager;
    public ConfigManager configManager;
    public EventManager eventManager;
    public CommandManager commandManager;

    public FriendManager friendManager;
    public guiscreencomponent clickGui;
    public static Smertnix instance = new Smertnix();
    public RotationHelper.Rotation rotation;

    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }

    public String name = "Celestial", type = "Recode", version = "2.1";

    public void init() {
        Display.setTitle(name + " " + type + " 2.1");
        ShaderShell.init();
        time = System.currentTimeMillis();
        (fileManager = new FileManager()).loadFiles();
        friendManager = new FriendManager();
        featureManager = new ModuleManager();
        macroManager = new MacroManager();
        eventManager = new EventManager();
        configManager = new ConfigManager();
        draggableHUD = new DragModern();
        commandManager = new CommandManager();
        clickGui = new guiscreencomponent();
        rotation = new RotationHelper.Rotation();


        for (RenderPlayer render : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
        }
        TPSUtils tpsUtils = new TPSUtils();
        try {
            //  ViaMCP.getInstance().start();
           // ViaMCP.getInstance().initAsyncSlider();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(FriendConfig.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(MacroConfig.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(HudConfig.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        EventManager.register(this);
        EventManager.register(this.rotation);

    }

    public final Color getClientColor() {
        return new Color(236, 133, 209);
    }

    public final Color getAlternateClientColor() {
        return new Color(28, 167, 222);
    }

    public void stop() {
        Smertnix.instance.configManager.saveConfig("default");
        (fileManager = new FileManager()).saveFiles();
        EventManager.unregister(this);
    }

    @EventTarget
    public void onInputKey(EventInputKey event) {
        featureManager.getAllFeatures().stream().filter(module -> module.getBind() == event.getKey()).forEach(Module::toggle);
        macroManager.getMacros().stream().filter(macros -> macros.getKey() == event.getKey()).forEach(macros -> Minecraft.getMinecraft().player.sendChatMessage(macros.getValue()));
    }
}
