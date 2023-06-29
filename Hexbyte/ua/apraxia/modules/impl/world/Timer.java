package ua.apraxia.modules.impl.world;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.packet.EventSendPacket;
import ua.apraxia.eventapi.events.impl.player.EventPreMotion;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.other.MoveUtility;

public class Timer extends Module {
    public BooleanSetting smart = new BooleanSetting("Smart", true);
    public SliderSetting timerAmount = new SliderSetting("Speed",  2, 1, 10, 1);
    public static float ticks = 0;
    public boolean active;

    public float animWidth;

    public Timer() {
        super("Timer", Categories.World);
        addSetting(timerAmount, smart);
    }

    @EventTarget
    public void onSend(EventSendPacket eventSendPacket) {
        if (eventSendPacket.getPacket() instanceof CPacketPlayer.Position || eventSendPacket.getPacket() instanceof CPacketPlayer.PositionRotation) {
            if (ticks <= 25 && !active) {
                if (MoveUtility.isMoving())
                    ticks++;
            }
        }
    }



    @EventTarget
    public void onPreUpdate(EventPreMotion eventPreMotion) {
        if (smart.value) {
            if (ticks <= 25 && !active) {
                if (MoveUtility.isMoving())
                    mc.timer.timerSpeed = timerAmount.value;
                else {
                    mc.timer.timerSpeed = 1;
                }
            }
            if (ticks >= 25) {
                active = true;
            }
            if (active) {
                mc.timer.timerSpeed = 1;
                if (!MoveUtility.isMoving())
                    ticks -= 1;
            }
            if (ticks <= 0) {
                active = false;
            }
        } else {
            mc.timer.timerSpeed = timerAmount.value;
        }
        ticks = MathHelper.clamp(ticks, 0, 100);
    }

  /*  @EventTarget
    public void onRender(EventRender2D event2D) {
        if (smart.value) {
        }
    } */

    public void onDisable() {
        super.onDisable();
        active = true;
        ticks = 0;
        this.mc.timer.timerSpeed = 1.0f;
    }

}