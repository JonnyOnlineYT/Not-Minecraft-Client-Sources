package org.spray.heaven.features.command.commands;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.spray.heaven.features.command.Command;
import org.spray.heaven.features.command.CommandInfo;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.file.FileManager;
import org.spray.heaven.util.file.config.Config;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.util.text.TextComponentKeybind;

@CommandInfo(name = "config", desc = "Configuration managing")
public class ConfigCmd extends Command {

	@Override
	public void command(String[] args, String msg) {
		if (args[0] == null || args[0].isEmpty())
			error();

		switch (args[0]) {
		case "add":
			if (args[1] != null) {
				Config config = new Config(args[1], "",
						new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime()));
				config.save();
				Wrapper.getConfig().add(config);
			} else
				send("Specify a name for the config");
			break;
		case "del":
			if (args[1] != null) {
				Config config = Wrapper.getConfig().fromName(args[1]);
				if (config != null)
					Wrapper.getConfig().delete(config);
				else
					send("Config not found");
			} else
				send("Specify a name for the config");
			break;
		case "save":
			if (args[1] != null) {
				Config config = Wrapper.getConfig().fromName(args[1]);
				if (config != null)
					config.save();
				else
					send("Config not found");
			} else
				send("Specify a name for the config");
			break;
		case "load":
			if (args[1] != null) {
				Config config = Wrapper.getConfig().fromName(args[1]);
				if (config != null)
					config.load();
				else
					send("Config not found");
			} else
				send("Specify a name for the config");
			break;

		case "folder":
			try {
				Runtime.getRuntime().exec("explorer " + FileManager.configDir.getAbsolutePath());
			} catch (IOException e) {
				send(e.getMessage());
			}
			break;

		case "list":
			Wrapper.getConfig().getConfigList().forEach(config -> {
				String date = config.getDate() != null && !config.getDate().isEmpty() ? ChatFormatting.RESET + " ["
						+ ChatFormatting.GRAY + config.getDate() + ChatFormatting.RESET + "]" : "";
				send(ChatFormatting.BLUE + config.getName() + date);
			});

			break;
		}
	}

	@Override
	public void error() {
		mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentKeybind(" "));
		send("Using:");
		send(Command.PREFIX + "config add " + ChatFormatting.GREEN + "name");
		send(Command.PREFIX + "config del " + ChatFormatting.GREEN + "name");
		send(Command.PREFIX + "config load " + ChatFormatting.GREEN + "name");
		send(Command.PREFIX + "config save " + ChatFormatting.GREEN + "name");
		send(Command.PREFIX + "config folder");
		send(Command.PREFIX + "config list");
	}

}
