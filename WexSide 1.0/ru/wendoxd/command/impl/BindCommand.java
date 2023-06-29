package ru.wendoxd.command.impl;

import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.PrimaryButton;
import ru.wendoxd.ui.menu.utils.MenuAPI;

@CommandInfo(name = "bind", desc = "Взаимодействие с биндами")
public class BindCommand extends Command {
	public static long time;

	@Override
	public void execute(String[] args) throws Exception {
		if (args[1].equalsIgnoreCase("list")) {
			ArrayList<CheckBox> boxes = new ArrayList();
			for (ru.wendoxd.ui.menu.elements.Tab tab : MenuAPI.tabs) {
				for (Frame frame : tab.getFrames()) {
					for (PrimaryButton button : frame.getButtons()) {
						float f;
						if (button instanceof CheckBox && ((CheckBox) button).getKey() != 0) {
							boxes.add((CheckBox) button);
						}
					}
				}
			}
			boxes.sort(Comparator.comparing(CheckBox::getName));
			sendMessage(ChatFormatting.GRAY + (boxes.isEmpty() ? "Пусто" : "Бинды :"));
			for (CheckBox box : boxes) {
				sendMessage(ChatFormatting.GRAY + box.getName() + " [ " + box.getPath().replace(":", "_").toLowerCase()
						+ " ]" + ChatFormatting.WHITE + " [ " + ChatFormatting.GRAY + Keyboard.getKeyName(box.getKey())
						+ ChatFormatting.WHITE + " ]");
			}
		} else {
			if (args[1].equalsIgnoreCase("unbindAll")) {
				int i = 0;
				for (ru.wendoxd.ui.menu.elements.Tab tab : MenuAPI.tabs) {
					for (Frame frame : tab.getFrames()) {
						for (PrimaryButton button : frame.getButtons()) {
							float f;
							if (button instanceof CheckBox && ((CheckBox) button).getKey() != 0) {
								((CheckBox) button).setKeybind(0);
								i++;
							}
						}
					}
				}
				sendMessage(ChatFormatting.GRAY + "Убрано " + i + " бинд(а/ов).");
			} else {
				throw new IllegalArgumentException("command");
			}
		}
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "bind list");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "bind unbindAll");
	}
}
