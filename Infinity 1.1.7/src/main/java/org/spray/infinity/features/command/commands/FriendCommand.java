package org.spray.infinity.features.command.commands;

import org.spray.infinity.features.command.Command;
import org.spray.infinity.features.command.CommandInfo;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;

import net.minecraft.util.Formatting;

@CommandInfo(name = "friend", desc = "Adding / Removing friend")
public class FriendCommand extends Command {

	@Override
	public void command(String[] args, String msg) {
		if (args[0].equalsIgnoreCase("add")) {
			Infinity.getFriend().add(args[1]);
		} else if (args[0].equalsIgnoreCase("del")) {
			Infinity.getFriend().remove(args[1]);
		} else if (args[0].equalsIgnoreCase("clear")) {
			Infinity.getFriend().getFriendList().clear();
			Infinity.getFriend().save();
			Helper.message(Formatting.GRAY + "Friend list successfully" + Formatting.WHITE + "cleared");
		} else if (args[0].equalsIgnoreCase("list")) {
			Infinity.getFriend().getFriendList().forEach(friends -> {
				Helper.message(Formatting.GRAY + "[Friend] " + Formatting.WHITE + friends);
			});
		}
		
	}

	@Override
	public void error() {
		send(Formatting.GRAY + "Please use" + Formatting.WHITE + ":");
		send(Formatting.WHITE + prefix + "friend add " + Formatting.AQUA + "name");
		send(Formatting.WHITE + prefix + "bind del " + Formatting.AQUA + "name");
		send(Formatting.WHITE + prefix + "bind list");
		send(Formatting.WHITE + prefix + "bind clear");
		
	}

}
