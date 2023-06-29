package org.spray.heaven.features.command.commands;

import org.spray.heaven.features.command.Command;
import org.spray.heaven.features.command.CommandInfo;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.util.math.MathHelper;

@CommandInfo(name = "hclip", desc = "Horizontal clipping")
public class HClipCmd extends Command {

	@Override
	public void command(String[] args, String msg) {
        float f = mc.player.rotationYaw * 0.017453292F;
        double speed = Double.valueOf(args[1]);
        double x = -(MathHelper.sin(f) * speed);
        double z = MathHelper.cos(f) * speed;
        mc.player.setPosition(mc.player.posX + x, mc.player.posY, mc.player.posZ + z);
		send(ChatFormatting.GRAY + "You hclipped to " + ChatFormatting.GREEN + args[0] + ChatFormatting.WHITE
				+ " blocks");
	}

	@Override
	public void error() {
		send("Syntax error");
		send(PREFIX + getName() + ChatFormatting.GREEN + " range");
	}

}
