package ru.rockstar;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.api.changelogs.ChangeManager;
import ru.rockstar.api.command.CommandManager;
import ru.rockstar.api.command.macro.Macro;
import ru.rockstar.api.command.macro.MacroManager;
import ru.rockstar.api.event.EventManager;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventKey;
import ru.rockstar.api.utils.friend.FriendManager;
import ru.rockstar.api.utils.shader.ShaderShell;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.FeatureDirector;
import ru.rockstar.client.ui.HudConfig;
import ru.rockstar.client.ui.altmanager.alt.AltConfig;
import ru.rockstar.client.ui.clickgui.ClickGuiScreen;
import ru.rockstar.client.ui.draggable.DraggableManager;
import ru.rockstar.client.ui.settings.FileManager;
import ru.rockstar.client.ui.settings.config.ConfigManager;
import ru.rockstar.client.ui.settings.impls.FriendConfig;
import ru.rockstar.client.ui.settings.impls.MacroConfig;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Main {
    //Название Голосового Помощника
    public static String holo = "Holo";

    public static Main instance = new Main();
    public static String name = "Rockstar Free";

	public static String version = "2.0";
	public static String v = version;
    public EventManager eventManager;
    public static FeatureDirector featureDirector;
    public ConfigManager configManager;
    public FriendManager friendManager;
    public FileManager fileManager;
    public ChangeManager changeManager;
    public DraggableManager draggableManager;
    public ru.rockstar.client.ui.espgble.DraggableManager espgbleManager;
    public MacroManager macroManager;
    public CommandManager commandManager;

    public ClickGuiScreen clickGui;
    //public Window windowGui;
    public ru.rockstar.client.ui.csgui.ClickGuiScreen csgui;
    //public ru.rockstar.client.ui.newclickgui.ClickGuiScreen newClickGui;
    //public ru.rockstar.client.ui.sigmagui.ClickGuiScreen sigmaGui;

    
    public void startClient() {

        //Shader
        ShaderShell.init();
        //Manager
        eventManager = new EventManager();
        featureDirector = new FeatureDirector();
        configManager = new ConfigManager();
        commandManager = new CommandManager();
        this.draggableManager = new DraggableManager();
        this.espgbleManager = new ru.rockstar.client.ui.espgble.DraggableManager();
        macroManager = new MacroManager();
        changeManager = new ChangeManager();
        clickGui = new ClickGuiScreen();
       // windowGui = new Window();
       // newClickGui = new ru.rockstar.client.ui.newclickgui.ClickGuiScreen();
        //sigmaGui = new ru.rockstar.client.ui.sigmagui.ClickGuiScreen();
        csgui = new ru.rockstar.client.ui.csgui.ClickGuiScreen();
        friendManager = new FriendManager();
        // Minecraft name.
        Display.setTitle(name);
        
        
        
        //config
        try {
        	this.fileManager.getFile(FriendConfig.class).loadOnStart();
            this.fileManager.getFile(MacroConfig.class).loadOnStart();
            this.fileManager.getFile(HudConfig.class).loadOnStart();
			this.fileManager.getFile(AltConfig.class).loadOnStart();
        }
        catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //Event
        EventManager.register(this);
    }

    public void stopClient() {
        EventManager.unregister(instance);
        (fileManager).saveFiles();
    }
    
    

    public static void msg(String s, boolean prefix) {
        s = (prefix ? TextFormatting.GRAY + "[" + TextFormatting.RED + "Rockstar" + TextFormatting.GRAY + "]" + ": " : "") + s;
        Minecraft.getMinecraft().player.addChatMessage(new TextComponentTranslation(s.replace("&", "??")));
    }

    @EventTarget
    public void onKey(EventKey event) {
        for (Feature feature : featureDirector.getFeatureList()) {
            if (feature.getKey() == event.getKey()) {
                feature.toggle();
            }
            for (Macro macro : macroManager.getMacros()) {
                if (macro.getKey() == Keyboard.getEventKey()) {
                    if (Minecraft.getMinecraft().player != null) {
                        Minecraft.getMinecraft().player.sendChatMessage(macro.getValue());
                        return;
                    }
                }
            }
        }
    }

    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }
}