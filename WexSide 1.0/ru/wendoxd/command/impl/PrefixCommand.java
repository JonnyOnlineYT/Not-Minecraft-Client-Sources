package ru.wendoxd.command.impl;

import java.io.File;
import java.nio.file.Files;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;

@CommandInfo(name = "prefix", desc = "Позволяет установить префиксы к командам чита")
public class PrefixCommand extends Command {
	public static String prefix = ".";

	@Override
	public void execute(String[] args) throws Exception {
		File p = new File("prefix");
		Files.write(p.toPath(), args[1].getBytes());
		prefix = args[1];
		sendMessage(ChatFormatting.WHITE + "Префикс " + prefix + " установлен");
		new Exception().printStackTrace();
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
		sendMessage(ChatFormatting.WHITE + prefix + "prefix " + ChatFormatting.GRAY + "<prefix>");
	}
}
