package summer.cheat.cheats.misc;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.ChatUtils;
import summer.cheat.cheats.movement.Flight;
import summer.cheat.cheats.movement.Speed;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventSendPacket;
import summer.cheat.eventsystem.events.player.EventUpdate;

public class AntiBan extends Cheats {

    public AntiBan() {
        super("AntiBan", "", Selection.PLAYER);
        // TODO Auto-generated constructor stub
    }


    @EventTarget
    public void onUpdate(EventUpdate e) {
        PlayerCapabilities pc = new PlayerCapabilities();
        if (CheatManager.getInstance(Flight.class).isToggled() || CheatManager.getInstance(Speed.class).isToggled()) {
            pc.allowFlying = true;
            pc.isFlying = true;
            pc.isCreativeMode = true;
            pc.setFlySpeed((float) randomNumber(1.0, 5.0));
            pc.setPlayerWalkSpeed((float) randomNumber(1.0, 5.0));
           // mc.thePlayer.isAirBorne = true;
            //mc.thePlayer.sendQueue.addToSendQueueSilent(new C13PacketPlayerAbilities(pc));
        }
    }

    public static double randomNumber(final double max, final double min) {
        return Math.random() * (max - min) + min;
    }

    @EventTarget
    public void onPacket(EventSendPacket event) {
    	if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook)event.getPacket();
            pac.yaw = mc.thePlayer.rotationYaw;
            pac.pitch = mc.thePlayer.rotationPitch;
            ChatUtils.sendMessage("FLY");
    	}
        if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
            final C0FPacketConfirmTransaction packetConfirmTransaction = (C0FPacketConfirmTransaction) event.getPacket();
            if (packetConfirmTransaction.getUid() < 0 && mc.thePlayer.ticksExisted % 32 != 0) {
                event.setCancelled(true);
            }
        }

        if (event.getPacket() instanceof C00PacketKeepAlive) {
            event.setCancelled(true);
        }
        if (CheatManager.getInstance(Flight.class).isToggled() || CheatManager.getInstance(Speed.class).isToggled()) {				
			if (event.getPacket() instanceof C13PacketPlayerAbilities) {
            final C13PacketPlayerAbilities C4 = (C13PacketPlayerAbilities)event.getPacket();
            C4.setAllowFlying(true);
            C4.setCreativeMode(true);
            C4.setFlying(true);
            C4.setInvulnerable(true);
            C4.isInvulnerable();
            event.setCancelled(true);
        }
        }
    }
}
