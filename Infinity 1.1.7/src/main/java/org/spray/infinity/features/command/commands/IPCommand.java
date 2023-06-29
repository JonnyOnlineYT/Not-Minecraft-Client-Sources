package org.spray.infinity.features.command.commands;

import org.spray.infinity.features.command.Command;
import org.spray.infinity.features.command.CommandInfo;
import org.spray.infinity.utils.Helper;

import net.minecraft.util.Formatting;

@CommandInfo(name = "ip", desc = "See server ip address")
public class IPCommand extends Command {

	@Override
	public void command(String[] args, String msg) {
		if (Helper.MC.isInSingleplayer()) {
			send(Formatting.GRAY + "You playing " + Formatting.WHITE + "SinglePlayer");
		} else
		send(Formatting.GRAY + "Server IP addres: "
				+ Helper.getPlayer().networkHandler.getConnection().getAddress().toString());

	}

	@Override
	public void error() {
		send("Please use: " + Formatting.AQUA + prefix + "ip");

	}

}
