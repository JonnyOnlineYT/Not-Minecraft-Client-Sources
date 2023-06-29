package ru.wendoxd.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import ru.wendoxd.WexSide;
import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;

@CommandInfo(name = "friend", desc = "Позволяет добавлять игроков в друзья")
public class FriendCommand extends Command {

	@Override
	public void execute(String[] args) throws Exception {
		switch (args[1]) {
		case "add":
			if (args[2].equalsIgnoreCase(mc.session.getUsername())) {
				sendMessage(ChatFormatting.GRAY + "Ты не можешь добавить себя в друзья");
			} else {
				if (WexSide.friendManager.getFriends().contains(args[2])) {
					sendMessage(ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " уже есть в списке друзей");
				} else {
					WexSide.friendManager.addFriend(args[2]);
					sendMessage(ChatFormatting.GRAY + "Успешно добавил " + ChatFormatting.RED + args[2]
							+ ChatFormatting.GRAY + " в друзья");
				}
			}
			break;
		case "remove":
			if (WexSide.friendManager.isFriend(args[2])) {
				WexSide.friendManager.removeFriend(args[2]);
				sendMessage(ChatFormatting.RED + args[2] + " " + ChatFormatting.GRAY + " был удален из списка друзей");
			} else {
				sendMessage(ChatFormatting.RED + args[2] + " " + ChatFormatting.GRAY + " нету в списке друзей");
			}
			break;
		case "clear":
			if (WexSide.friendManager.getFriends().isEmpty()) {
				sendMessage("Список друзей пуст");
			} else {
				WexSide.friendManager.clearFriend();
				sendMessage("Список друзей успешно очищен");
			}
			break;
		case "list":
			if (WexSide.friendManager.getFriends().isEmpty()) {
				sendMessage("Список друзей пуст");
			} else {
				sendMessage(ChatFormatting.GREEN + "Список друзей: ");
				WexSide.friendManager.getFriends().forEach(friends -> sendMessage(ChatFormatting.GRAY + friends));
			}
			break;
		}
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "friend add " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "name" + ChatFormatting.GRAY + ">");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "friend remove " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "name" + ChatFormatting.GRAY + ">");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "friend list");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "friend clear");
	}
}