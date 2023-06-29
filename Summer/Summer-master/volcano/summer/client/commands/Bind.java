package volcano.summer.client.commands;

import org.lwjgl.input.Keyboard;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;
import volcano.summer.base.manager.module.Module;
import volcano.summer.base.manager.module.ModuleManager;

public class Bind extends Command {
	public Bind() {
		super("Bind", "<Module> <Key>");
	}

	@Override
	public void run(final String message) {
		final ModuleManager moduleM = Summer.moduleManager;
		final Module m = Summer.moduleManager.getModuleByString(message.split(" ")[1]);
		if (message.split(" ").length == 3) {
			if (message.split(" ")[1].equalsIgnoreCase(m.name)) {
				if (Keyboard.getKeyIndex(message.split(" ")[2].toUpperCase()) == 0) {
					m.setBind(0);
					Summer.tellPlayer("The Bind for " + m.name + " has been set to " + Keyboard.getKeyName(m.bind));
				} else {
					m.setBind(Keyboard.getKeyIndex(message.split(" ")[2].toUpperCase()));
					Summer.tellPlayer("The Bind for " + m.name + " has been set to " + Keyboard.getKeyName(m.bind));
				}
				Summer.fileManager.saveFiles();
			}
		} else {
			Summer.tellPlayer("Incorrect Syntax! bind <Module> <Bind>");
		}
	}
}
