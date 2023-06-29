package ru.wendoxd.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;
import ru.wendoxd.modules.impl.visuals.Crosshair;

@CommandInfo(name = "gps", desc = "Помогает дойти до какой либо точки на карте.")
public class GPSCommand extends Command {

	@Override
	public void execute(String[] args) throws Exception {
		if (args[1].equals("off")) {
			Crosshair.setEnabled(false);
		} else {
			double x = Double.parseDouble(args[1]);
			double z = Double.parseDouble(args[2]);
			Crosshair.setGPS(x, z);
		}
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "gps " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "x, z" + ChatFormatting.GRAY + ">");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "gps " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "off" + ChatFormatting.GRAY + ">");
	}
}
