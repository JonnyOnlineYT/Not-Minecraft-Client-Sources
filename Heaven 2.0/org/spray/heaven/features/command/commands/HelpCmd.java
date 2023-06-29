package org.spray.heaven.features.command.commands;

import org.spray.heaven.features.command.Command;
import org.spray.heaven.features.command.CommandInfo;
import org.spray.heaven.main.Wrapper;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.util.text.TextComponentKeybind;

@CommandInfo(name = "help", desc = "See commands")
public class HelpCmd extends Command {

	@Override
	public void command(String[] args, String msg) {
		mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentKeybind(" "));
		Wrapper.getCommand().getCommands()
				.forEach(command -> send(command.getName() + " - " + ChatFormatting.GRAY + command.getDesc()));
	}

	@Override
	public void error() {
		mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentKeybind(" "));
		send("Use: " + ChatFormatting.GREEN + PREFIX + "help");

	}

}
