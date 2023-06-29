package ua.apraxia;

import ua.apraxia.cmd.CommandManager;
import ua.apraxia.cmd.macro.MacroManager;
import ua.apraxia.draggable.Draggable;
import ua.apraxia.eventapi.EventManager;
import ua.apraxia.files.FileManager;
import ua.apraxia.gui.WindowGUI;
import net.minecraft.client.Minecraft;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.ModuleManagment;
import ua.apraxia.modules.settings.config.ConfigManager;
import ua.apraxia.utility.other.DiscordUtility;

public class Hexbyte {
    public ModuleManagment moduleManagment;
    private static Hexbyte instance;
    public ConfigManager configManager;
    public Draggable draggable;
    public MacroManager macroManager;
    public FileManager fileManager;
    public WindowGUI clickUI;

    public void startClient() {
        new DiscordUtility().init();
        macroManager = new MacroManager();
        configManager = new ConfigManager();
        (fileManager = new FileManager()).loadFiles();
        CommandManager commandManager = new CommandManager();
        moduleManagment = new ModuleManagment();
        draggable = new Draggable();
        instance = this;
        clickUI = new WindowGUI();
    }

    public void stop() {
        (fileManager = new FileManager()).saveFiles();
        EventManager.unregister(this);
    }

    public static Hexbyte getInstance() {
        return instance;
    }

    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? 1.0 / (double) Minecraft.getDebugFPS() : 1.0;
    }

    public void keyPress(int key) {
        for (Module module : moduleManagment.getModules()) {
            if (module.moduleKey == key) {
                module.toggle();
            }
        }
    }

}
