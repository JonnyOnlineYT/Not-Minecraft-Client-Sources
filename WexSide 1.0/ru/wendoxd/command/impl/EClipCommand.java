package ru.wendoxd.command.impl;

import net.minecraft.network.play.client.CPacketPlayer;

import org.apache.commons.lang3.math.NumberUtils;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.math.BlockPos;
import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;
import ru.wendoxd.modules.impl.movement.GlideFly;

@CommandInfo(name = "eclip", desc = "Клип с места(нужны элитры в инвентаре)")
public class EClipCommand extends Command {
	@Override
	public void execute(String[] args) throws Exception {
		float y = 0;
		if (args[1].equals("bd")) {
			y = -(float) mc.player.posY - 3;
		}
		if (args[1].equals("down")) {
			for (int i = 1; i < 255; i++) {
				if (mc.world.getBlockState(new BlockPos(mc.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
					y = -i - 1;
					break;
				}
				if (mc.world.getBlockState(new BlockPos(mc.player).add(0, -i, 0)) == Blocks.BEDROCK.getDefaultState()) {
					sendMessage(ChatFormatting.RED + "Тут можно телепортироваться только под бедрок.");
					sendMessage(ChatFormatting.RED + PrefixCommand.prefix + "eclip bd");
					return;
				}
			}
		}
		if (args[1].equals("up")) {
			for (int i = 4; i < 255; i++) {
				if (mc.world.getBlockState(new BlockPos(mc.player).add(0, i, 0)) == Blocks.AIR.getDefaultState()) {
					y = i + 1;
					break;
				}
			}
		}
		if (y == 0) {
			if (NumberUtils.isNumber(args[1])) {
				y = Float.parseFloat(args[1]);
			} else {
				sendMessage(ChatFormatting.RED + args[1] + ChatFormatting.GRAY + " не является числом!");
				return;
			}
		}
		int elytra = GlideFly.getSlotIDFromItem(Items.ELYTRA);
		if (elytra == -1) {
			sendMessage(ChatFormatting.RED + "Вам нужны элитры в инвентаре");
			return;
		}
		if (elytra != -2) {
			mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
			mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
		}
		mc.getConnection()
				.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
		mc.getConnection()
				.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
		mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, Action.START_FALL_FLYING));
		mc.getConnection()
				.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + y, mc.player.posZ, false));
		mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, Action.START_FALL_FLYING));
		if (elytra != -2) {
			mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
			mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
		}
		mc.player.setPosition(mc.player.posX, mc.player.posY + y, mc.player.posZ);
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "eclip " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "value" + ChatFormatting.GRAY + ">/up/down/bd");
	}
}
