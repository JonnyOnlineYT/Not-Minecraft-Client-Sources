package Celestial.cmd.impl;

import Celestial.cmd.CommandAbstract;
import Celestial.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;

public class TpCommand
        extends CommandAbstract {
   Minecraft mc;

    public TpCommand() {
        super("tp", "tp", "\u00a76.tp x y z", "tp");
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public /* synthetic */ void execute(String ... args) {
        if (args.length > 1) {
            float endX = (float)Double.parseDouble(args[1]);
            float endY = (float)Double.parseDouble(args[2]);
            float endZ = (float)Double.parseDouble(args[3]);
            if (this.mc.player.ticksExisted % 1 == 0) {
                ChatUtils.addChatMessage(" Мобилизирую на заданные мне координаты " + endX + " " + endY + " " + endZ);
                mc.player.motionY = 0.05f;
                mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY, endZ - 0.5, false));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY + 109, endZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY, endZ - 0.5, true));
            }
        }
    }
}