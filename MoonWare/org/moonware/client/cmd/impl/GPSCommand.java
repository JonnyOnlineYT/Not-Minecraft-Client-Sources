package org.moonware.client.cmd.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import org.apache.commons.lang3.math.NumberUtils;
import org.moonware.client.cmd.CommandAbstract;

import static org.moonware.client.utils.MWUtils.sendChat;

public class GPSCommand extends CommandAbstract {

    public GPSCommand() {
        super("tp", "tp", "§6.tp  <x> <y> <z>", "tp");
    }

    @Override
    public void execute(String... args) {
        if (args.length > 1) {
            if (!NumberUtils.isNumber(args[1]) || !NumberUtils.isNumber(args[2]) || !NumberUtils.isNumber(args[3])) {
                return;
            }
           if (args[0].equalsIgnoreCase("tp")) {
               //Minecraft.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Double.parseDouble(args[1]) ,Double.parseDouble(args[2]),Double.parseDouble(args[3]),1,1,false));
               Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX ,Minecraft.player.posY + 0.1f,Minecraft.player.posZ,true));

               Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Double.parseDouble(args[1]) ,Double.parseDouble(args[2]),Double.parseDouble(args[3]),false));
           }
        } else {
            sendChat(getUsage());
        }
    }

}
