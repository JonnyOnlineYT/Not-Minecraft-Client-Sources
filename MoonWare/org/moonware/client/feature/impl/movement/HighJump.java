package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.Event;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.combat.particle.EventMotion;
import org.moonware.client.feature.impl.movement.Move.MoveUtils;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class HighJump extends Feature {

    public ListSetting mode;
    public NumberSetting motionBoost;
    public BooleanSetting motionYBoost = new BooleanSetting("MotionY boost", false, () -> true);
    public boolean jump;
    public TimerHelper helper = new TimerHelper();

    public boolean flag;
    public int delay, requestFlags;
    private int ticks;
    private boolean timerEnable;

    public HighJump() {
        super("High Jump", "Вы подлетаете на большую высоту", Type.Movement);
    }

    @EventTarget
    public void EventMove(EventMotion event) {
        if (getState()) {
            if (this.getState()) {
                if (MoveUtils.isMoving() && MoveUtils.getSpeed() < 0.2177f) {
                    MoveUtils.strafe();
                    if (Math.abs(Minecraft.player.movementInput.moveStrafe) < 0.1 && Strafe.mc.gameSettings.keyBindForward.pressed) {
                        MoveUtils.strafe();
                    }
                    if (Minecraft.player.onGround) {
                        MoveUtils.strafe();
                    }
                }
            }
        }else{
            requestFlags = 0;
        }
    }
    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        update2(event);
    }
    public void update(Event event) {

    }
    public void update2(Event event) {
        EventReceivePacket erp = (EventReceivePacket) event;
        Packet packet = erp.getPacket();
        if (packet instanceof SPacketPlayerPosLook) {
            if (requestFlags == 0) {
                //return;
            }
            requestFlags--;
            flag = true;
        }
    }


}
