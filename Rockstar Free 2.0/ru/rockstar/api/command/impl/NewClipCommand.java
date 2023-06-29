package ru.rockstar.api.command.impl;

import net.minecraft.client.Minecraft;
import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;

public class NewClipCommand extends CommandAbstract {

    Minecraft mc = Minecraft.getMinecraft();

    public NewClipCommand() {
        super("newclip", "newclip | newhclip", "§6.newclip | (newhclip) + | - §3<value> | down", "newclip", "newhclip");
    }

    @Override
    public void execute(String... args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("newclip")) {
                try {
                    if (args[1].equals("down")) {
                    	 mc.player.setPositionAndUpdate(mc.player.posX, -2, mc.player.posZ);
                    	 if (timerHelper.hasReached(50))
                    	 mc.player.setPositionAndUpdate(mc.player.posX, -2, mc.player.posZ);
                    }
                    if (args[1].equals("+")) {
                    	 mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + Double.parseDouble(args[2]), mc.player.posZ);
                    	 if (timerHelper.hasReached(50))
                    	 mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + Double.parseDouble(args[2]), mc.player.posZ);
                    }
                    if (args[1].equals("-")) {
                    	 mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY - Double.parseDouble(args[2]), mc.player.posZ);
                    	 if (timerHelper.hasReached(50))
                    	 mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY - Double.parseDouble(args[2]), mc.player.posZ);
                    }
                } catch (Exception ignored) {
                }
            }
            if (args[0].equalsIgnoreCase("newhclip")) {
                double x = mc.player.posX;
                double y = mc.player.posY;
                double z = mc.player.posZ;
                double yaw = mc.player.rotationYaw * 0.017453292;
                try {
                    if (args[1].equals("+")) {
                        mc.player.setPositionAndUpdate(x - Math.sin(yaw) * Double.parseDouble(args[2]), y, z + Math.cos(yaw) * Double.parseDouble(args[2]));
                        if (timerHelper.hasReached(50))
                        mc.player.setPositionAndUpdate(x - Math.sin(yaw) * Double.parseDouble(args[2]), y, z + Math.cos(yaw) * Double.parseDouble(args[2]));
                    }
                    if (args[1].equals("-")) {
                        mc.player.setPositionAndUpdate(x + Math.sin(yaw) * Double.parseDouble(args[2]), y, z - Math.cos(yaw) * Double.parseDouble(args[2]));
                        if (timerHelper.hasReached(50))
                        mc.player.setPositionAndUpdate(x + Math.sin(yaw) * Double.parseDouble(args[2]), y, z - Math.cos(yaw) * Double.parseDouble(args[2]));
                    }
                } catch (Exception ignored) {
                }
            }
        } else {
            Main.msg(getUsage(),true);
        }
    }
}
