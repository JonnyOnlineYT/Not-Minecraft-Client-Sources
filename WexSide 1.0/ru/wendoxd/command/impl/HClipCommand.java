package ru.wendoxd.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.apache.commons.lang3.math.NumberUtils;
import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;

@CommandInfo(name = "hclip", desc = "Позволяет телепортироваться по горизонтали")
public class HClipCommand extends Command {

	@Override
	public void execute(String[] args) throws Exception {
		double yaw = Math.toRadians(mc.player.rotationYaw);
		switch (args[1]) {
		case "+":
			if (NumberUtils.isNumber(args[2])) {
				mc.player.setPositionAndUpdate(mc.player.posX - Math.sin(yaw) * Double.parseDouble(args[2]),
						mc.player.posY, mc.player.posZ + Math.cos(yaw) * Double.parseDouble(args[2]));
			} else {
				sendMessage(ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " не является числом!");
			}
			break;
		case "-":
			if (NumberUtils.isNumber(args[2])) {
				mc.player.setPositionAndUpdate(mc.player.posX + Math.sin(yaw) * Double.parseDouble(args[2]),
						mc.player.posY, mc.player.posZ - Math.cos(yaw) * Double.parseDouble(args[2]));
			} else {
				sendMessage(ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " не является числом!");
			}
			break;
		}
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "hclip + " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "value" + ChatFormatting.GRAY + ">");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "hclip - " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "value" + ChatFormatting.GRAY + ">");
	}
}
