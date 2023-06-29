package org.spray.heaven.features.command.commands;

import org.spray.heaven.features.command.Command;
import org.spray.heaven.features.command.CommandInfo;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.BlockUtil;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;

@CommandInfo(name = "vclip", desc = "Vertical clipping")
public class VClipCmd extends Command {

	@Override
	public void command(String[] args, String msg) {
		if (args[0].equalsIgnoreCase("up")) {
			double scan = getScanUp();
			if (scan == 0 || scan == 1) {
				send(ChatFormatting.RED + "a suitable territory was not found");
				return;
			}
			clip(scan);
		} else if (args[0].equalsIgnoreCase("down")) {
			double scan = -getScanDown();
			if (scan == 0 || scan == -1) {
				send(ChatFormatting.RED + "a suitable territory was not found");
				return;
			}
			clip(scan);
		} else {
			double range = Double.parseDouble(args[0]);
			clip(range);
		}
	}

	private void clip(double range) {
		for (int i = 0; i < 10; i++) {
			mc.player.connection
					.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));

		}
		for (int i = 0; i < 10; i++) {
			mc.player.connection.sendPacket(
					new CPacketPlayer.Position(mc.player.posX, mc.player.posY + range, mc.player.posZ, false));
		}
		mc.player.setPosition(mc.player.posX, mc.player.posY + range, mc.player.posZ);
		Wrapper.getModule().get("FakePlayer").enable();
		send(ChatFormatting.GRAY + "You vclipped to " + ChatFormatting.GREEN + (int) range + ChatFormatting.WHITE
				+ " blocks");
	}

	private double getScanUp() {
		for (int i = 0; i < 80; i++) {
			if (BlockUtil.getBlock(mc.player.getPosition().up(i)) != Blocks.AIR
					&& BlockUtil.getBlock(mc.player.getPosition().up(i + 1)) == Blocks.AIR
					&& BlockUtil.getBlock(mc.player.getPosition().up(i + 2)) == Blocks.AIR) {
				return i + 1;
			}
		}
		return 0;
	}
	
	private double getScanDown() {
		for (int i = 2; i < 80; i++) {
			if (BlockUtil.getBlock(mc.player.getPosition().down(i)) != Blocks.AIR
					&& BlockUtil.getBlock(mc.player.getPosition().down(i - 1)) == Blocks.AIR
					&& BlockUtil.getBlock(mc.player.getPosition().down(i - 2)) == Blocks.AIR) {
				return i - 1;
			}
		}
		return 0;
	}

	@Override
	public void error() {
		send("Syntax error");
		send(PREFIX + getName() + ChatFormatting.GREEN + " range");
	}

}
