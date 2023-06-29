package ru.wendoxd.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import ru.wendoxd.WexSide;
import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;
import ru.wendoxd.macro.Macro;

@CommandInfo(name = "macro", desc = "Позволяет отправить команду по нажатию кнопки")
public class MacroCommand extends Command {

	@Override
	public void execute(String[] args) throws Exception {
		if (args.length > 1) {
			switch (args[1]) {
			case "add":
				StringBuilder sb = new StringBuilder();
				for (int i = 3; i < args.length; i++) {
					sb.append(args[i]).append(" ");
				}
				WexSide.macroManager.addMacros(new Macro(sb.toString(), Keyboard.getKeyIndex(args[2].toUpperCase())));
				sendMessage(ChatFormatting.GREEN + "Добавлен макрос для кнопки" + ChatFormatting.RED + " \""
						+ args[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "с командой "
						+ ChatFormatting.RED + sb);
				break;
			case "clear":
				if (WexSide.macroManager.getMacros().isEmpty()) {
					sendMessage(ChatFormatting.RED + "Список макросов пуст");
				} else {
					sendMessage(ChatFormatting.GREEN + "Список Макросов " + ChatFormatting.WHITE + "успешно очищен");
					WexSide.macroManager.getMacros().clear();
					WexSide.macroManager.updateFile();
				}
				break;
			case "remove":
				WexSide.macroManager.deleteMacro(Keyboard.getKeyIndex(args[2].toUpperCase()));
				sendMessage(ChatFormatting.GREEN + "Макрос " + ChatFormatting.WHITE + "был удален с кнопки "
						+ ChatFormatting.RED + "\"" + args[2] + "\"");
				break;
			case "list":
				if (WexSide.macroManager.getMacros().isEmpty()) {
					sendMessage("Список макросов пуст");
				} else {
					sendMessage(ChatFormatting.GREEN + "Список макросов: ");
					WexSide.macroManager.getMacros()
							.forEach(macro -> sendMessage(ChatFormatting.WHITE + "Команда: " + ChatFormatting.RED
									+ macro.getMessage() + ChatFormatting.WHITE + ", Кнопка: " + ChatFormatting.RED
									+ Keyboard.getKeyName(macro.getKey())));
				}
				break;
			}
		} else {
			error();
		}
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "macro add " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "key" + ChatFormatting.GRAY + ">" + ChatFormatting.GRAY + " message");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "macro remove " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "key" + ChatFormatting.GRAY + ">");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "macro list");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "macro clear");
	}
}
