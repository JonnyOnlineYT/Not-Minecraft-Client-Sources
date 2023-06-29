package ru.wendoxd.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.player.EntityPlayer;

import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;
import ru.wendoxd.modules.impl.player.RodLeave;

@CommandInfo(name = "tp", desc = "Позволяет телепортироваться к игроку")
public class TPCommand extends Command {

	@Override
	public void execute(String[] args) throws Exception {
		String name = args[1];
		int entityId = -1;
		for (EntityPlayer player : mc.world.playerEntities) {
			if (player.getName().equalsIgnoreCase(name)) {
				entityId = player.getEntityId();
			}
		}
		if (entityId == -1) {
			sendMessage(ChatFormatting.GRAY + "Игрок не найден");
		} else {
			if (RodLeave.getHand() == null) {
				sendMessage(
						ChatFormatting.GRAY + "Вам нужно подцепить себя удочкой что бы использовать этот телепорт.");
				return;
			}
			RodLeave.entityID = entityId;
			sendMessage(ChatFormatting.GRAY + "Подойдите к игроку ближе чем на 8 блоков по X/Z.");
		}
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "tp " + ChatFormatting.GRAY + "<" + ChatFormatting.RED
				+ "nick" + ChatFormatting.GRAY + ">");
	}
}
