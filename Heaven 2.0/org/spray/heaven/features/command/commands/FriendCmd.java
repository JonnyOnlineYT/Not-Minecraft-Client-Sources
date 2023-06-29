package org.spray.heaven.features.command.commands;

import org.spray.heaven.features.command.Command;
import org.spray.heaven.features.command.CommandInfo;
import org.spray.heaven.main.Wrapper;

import com.mojang.realmsclient.gui.ChatFormatting;

@CommandInfo(name = "friend", desc = "Adding / Removing friend")
public class FriendCmd extends Command {

	@Override
	public void command(String[] args, String msg) {
		switch (args[0]) {
		case "add":
			Wrapper.getFriend().add(args[1]);
			break;
		case "del":
			Wrapper.getFriend().remove(args[1]);
			break;
		}
	}

	@Override
	public void error() {
		send("Error, use:");
		send(Command.PREFIX + "friend add " + ChatFormatting.GREEN + "name");
		send(Command.PREFIX + "friend del " + ChatFormatting.GREEN + "name");
	}

}
