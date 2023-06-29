package Celestial.cmd.impl;

import Celestial.cmd.CommandAbstract;
import Celestial.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.text.TextFormatting;

public class TPaCommand extends CommandAbstract {

    Minecraft mc = Minecraft.getMinecraft();

    public TPaCommand() {
        super("tpa", "tpa", "§6.tpa entity", "tpa");
    }

    @Override
    public void execute(String... args) {
        if (args.length > 1) {
        for (EntityPlayer e : Minecraft.getMinecraft().world.playerEntities) {
            if (args[1].equalsIgnoreCase(e.getName())) {
                int x = (int) e.posX;
                int y = (int) e.posY;
                int z = (int) e.posZ;
                Minecraft.getMinecraft().player.setSprinting(false);
                if (Minecraft.getMinecraft().player.onGround) {
                    Minecraft.getMinecraft().player.jumpTicks = 4;
                    Minecraft.getMinecraft().player.jump();
                }
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketEntityAction(Minecraft.getMinecraft().player, CPacketEntityAction.Action.START_SNEAKING));
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, false));
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer(false));
                ChatUtils.addChatMessage(TextFormatting.GREEN + "Телепортация к " + x + " " + y + " " + z + "..");
                return;
            }
            
        }
        }
    }
}