package org.spray.infinity.features.command.commands;

import org.spray.infinity.features.command.Command;
import org.spray.infinity.features.command.CommandInfo;
import org.spray.infinity.utils.MoveUtil;

import net.minecraft.util.Formatting;

@CommandInfo(name = "hclip", desc = "horizontal clipping")
public class HClipCommand extends Command {

	@Override
	public void command(String[] args, String msg) {
		MoveUtil.hClip(Double.parseDouble(args[0]));
		send(Formatting.GRAY + "You clipping to " + Formatting.AQUA + args[0] + Formatting.WHITE + " blocks");
	}

	@Override
	public void error() {
		send(Formatting.GRAY + "Please use" + Formatting.WHITE + ":");
		send(Formatting.WHITE + prefix + "hclip " + Formatting.GRAY + "<" + Formatting.AQUA + "value" + Formatting.GRAY
				+ ">");

	}

}
