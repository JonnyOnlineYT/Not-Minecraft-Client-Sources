package ru.wendoxd.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

import org.apache.commons.lang3.math.NumberUtils;
import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;

@CommandInfo(name = "vclip", desc = "Позволяет телепортироваться по вертикали")
public class VClipCommand extends Command {

	@Override
	public void execute(String[] args) throws Exception {
		float y = 0;
		if (args[1].equals("down")) {
			for (int i = 0; i < 255; i++) {
				if (mc.world.getBlockState(new BlockPos(mc.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
					y = -i - 1;
					break;
				}
				if (mc.world.getBlockState(new BlockPos(mc.player).add(0, -i, 0)) == Blocks.BEDROCK.getDefaultState()) {
					sendMessage(ChatFormatting.RED + "Тут можно телепортироваться только под бедрок.");
					return;
				}
			}
		}
		if (args[1].equals("up")) {
			for (int i = 3; i < 255; i++) {
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
		for (int i = 0; i < Math.max(y / 1000, 3); i++) {
			mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
		}
		mc.player.connection
				.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + y, mc.player.posZ, false));
		mc.player.setPosition(mc.player.posX, mc.player.posY + y, mc.player.posZ);
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
		sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix + "vclip " + ChatFormatting.GRAY + "<"
				+ ChatFormatting.RED + "value" + ChatFormatting.GRAY + ">");
	}
}
