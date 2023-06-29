package org.spray.heaven.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.command.Command;
import org.spray.heaven.features.command.CommandInfo;
import org.spray.heaven.main.Wrapper;

@CommandInfo(name = "macros", desc = "Binding message for key to chat")
public class MacrosCmd extends Command {

	@Override
	public void command(String[] args, String msg) {
		String macroMsg = msg.replace(getName() + " " + args[0] + " " + args[1] + " ", "");
		switch (args[0]) {
		case "add":
			Wrapper.getMacros().add(macroMsg, Keyboard.getKeyIndex(args[1].toUpperCase()));
			send(ChatFormatting.GRAY + "Macro added to key " + ChatFormatting.WHITE + args[1]);
			Wrapper.getMacros().save();
			break;
		case "del":
			Wrapper.getMacros().del(Keyboard.getKeyIndex(args[1].toUpperCase()));
			send(ChatFormatting.GRAY + "Macro deleted from key " + ChatFormatting.WHITE + args[1]);
			Wrapper.getMacros().save();
			break;
		}
	}

	@Override
	public void error() {
		send("Error, use:");
		send(Command.PREFIX + "macros add " + ChatFormatting.GRAY + "key " + ChatFormatting.GREEN + "message");
		send(Command.PREFIX + "macros del " + ChatFormatting.GRAY + "key");
	}

}
