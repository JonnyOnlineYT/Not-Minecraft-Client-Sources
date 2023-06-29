package ru.rockstar.api.command.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.server.management.PlayerList;
import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;

public class TpCommand extends CommandAbstract {

    Minecraft mc = Minecraft.getMinecraft();

    public TpCommand() {
        super("tp", "tp", "§6.tp x y z", "tp");
    }

    @Override
    public void execute(String... args) {
        if (args.length > 1) {
            float endX = (float) Double.parseDouble(args[1]);
            float endY = (float) Double.parseDouble(args[2]);
            float endZ = (float) Double.parseDouble(args[3]);
            Main.msg("Пытаюсь телепортироваться на " + endX + " " + endY + " " + endZ, true);
            if (mc.player.posX != endX && mc.player.posZ != endZ) {
            	mc.player.motionY = 1;
                if (args.length == 4) {
                	for (int i = 0; i < 10; i++) {
                		mc.player.motionY = 10;
                        mc.player.onGround = true;
               		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                         mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                         mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true));
                         
                	}
                }
                if (args.length == 5) {
                	for (int i = 0; i < Double.parseDouble(args[4]); i++) {
                		mc.player.motionY = 0.02;
                        mc.player.onGround = true;
               		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY, endZ - 0.5, false));
                         mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                         mc.player.connection.sendPacket(new CPacketPlayer.Position(endX - 0.5, endY, endZ - 0.5, true));
                         
                	}
                }
            }
            if(mc.player.posX == endX && mc.player.posZ == endZ) {
            Main.msg("§aУспешное телепортирование!", true);
            }
            
        } else {
            Main.msg(getUsage(),true);
        }
    }
}
