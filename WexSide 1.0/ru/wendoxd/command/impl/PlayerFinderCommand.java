package ru.wendoxd.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;
import ru.wendoxd.modules.impl.player.PlayerFinder;
import ru.wendoxd.modules.impl.player.PlayerFinder.Task;

@CommandInfo(name = "nocom", desc = "Поиск игроков")
public class PlayerFinderCommand extends Command {

	@Override
	public void execute(String[] args) throws Exception {
		if (args[1].equals("stop") || args[1].equals("off")) {
			sendMessage(ChatFormatting.RED + "Canceling...");
			PlayerFinder.setTask(null);
			return;
		}
		int distance = Math.min(Integer.parseInt(args[1]), 15000);
		int stepRange = Math.min(Math.max(Integer.parseInt(args[2]), 1), 10);
		int perTick = Math.min(Math.max(Integer.parseInt(args[3]), 1), 4);
		sendMessage(ChatFormatting.RED + "Дистанция сканирования [ " + distance + " ]");
		sendMessage(ChatFormatting.RED + "Дистанция между чанками [ " + (stepRange * 16) + " ]");
		sendMessage(ChatFormatting.RED + "Количество пакетов в секунду [ " + (perTick * 20) + " ]");
		PlayerFinder.setTask(new Task(mc.player.getPositionVector(), distance, stepRange, perTick));
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "nocom " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "distance" + ChatFormatting.GRAY + "> " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "stepRange" + ChatFormatting.GRAY + "> " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "perTick" + ChatFormatting.GRAY + ">");
	}
}
