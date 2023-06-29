package de.strafe;

import de.strafe.command.CommandManager;
import de.strafe.gui.clickgui.ClickGUI;
import de.strafe.modules.ModuleManager;
import de.strafe.utils.DiscordRP;
import de.strafe.utils.FileUtil;
import joptsimple.internal.Strings;
import org.lwjgl.opengl.Display;
import wtf.auxy.scripting.ScriptManager;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;


public class Strafe {
    public static Strafe INSTANCE;
    public final String NAME = "Strafe Client", VERSION = " v1.0", BACKGROUND = "background.jpg";
    public final String[] AUTHORS = {"Strafe"};
    public final DiscordRP discordRP;
    public final ClickGUI clickGUI;
	public final CommandManager commandManager;
    public final ModuleManager moduleManager;
    public final ScriptManager scriptManager;
    public final File scriptingdir;

    public Strafe() throws ScriptException, IOException, NoSuchMethodException {
		INSTANCE = this;
        System.out.println("Starting " + NAME + VERSION);
        Display.setTitle(NAME + VERSION);

        this.scriptingdir = new File("Strafe Client/Scripts");
        if (!this.scriptingdir.exists())
            this.scriptingdir.mkdir();
		commandManager = new CommandManager();
        discordRP = new DiscordRP();
        moduleManager = new ModuleManager();
        clickGUI = new ClickGUI();
        scriptManager = new ScriptManager();
        scriptManager.loadScripts();
        scriptManager.onStart();
        FileUtil.load();
    }

    public void shutdown() {
        discordRP.shutdown();
        FileUtil.save();
    }
}
