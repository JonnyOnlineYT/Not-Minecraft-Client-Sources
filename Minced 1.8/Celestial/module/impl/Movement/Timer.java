package Celestial.module.impl.Movement;

import Celestial.drag.comp.impl.DragTimer;
import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventSendPacket;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.event.events.impl.render.EventRender2D;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.module.impl.Render.ModuleList;
import Celestial.utils.math.MathematicHelper;
import Celestial.utils.movement.MovementUtils;
import Celestial.utils.render.RoundedUtil;
import Celestial.Smertnix;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class Timer extends Module {
    public static BooleanSetting smart;
    private NumberSetting timerAmount = new NumberSetting("Timer Amount", 2, 1, 10, 1);
    public static float ticks = 0;
    public boolean active;
    public float animWidth;


    public Timer() {
        super("Timer", "Ускоряет вас", ModuleCategory.Movement);
        smart = new BooleanSetting("Smart", false);
        addSettings(timerAmount,smart);
    }

    @EventTarget
    public void onSend(EventSendPacket eventSendPacket) {
        if (eventSendPacket.getPacket() instanceof CPacketPlayer.Position || eventSendPacket.getPacket() instanceof CPacketPlayer.PositionRotation) {
            if (ticks <= 25 && !active) {
                if (MovementUtils.isMoving())
                    ticks++;
            }
        }
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion eventPreMotion) {
        if (smart.getCurrentValue()) {
            if (ticks <= 25 && !active) {
                if (MovementUtils.isMoving())
                    mc.timer.timerSpeed = timerAmount.getCurrentValue();
                else {
                    mc.timer.timerSpeed = 1;
                }
            }
            if (ticks >= 25) {
                active = true;
            }
            if (active) {
                mc.timer.timerSpeed = 1;
                if (!MovementUtils.isMoving())
                    ticks -= 1;
            }
            if (ticks <= 0) {
                active = false;
            }
        } else {

            mc.timer.timerSpeed = timerAmount.getCurrentValue();
        }
        ticks = MathHelper.clamp(ticks, 0, 100);
    }
    @EventTarget
    public void onRender(EventRender2D event2D) {
        if(smart.getCurrentValue()) {
            DragTimer dt = (DragTimer) Smertnix.instance.draggableHUD.getDraggableComponentByClass(DragTimer.class);

            dt.setWidth(125);
            dt.setHeight(25);
            Color onecolor = new Color(ModuleList.oneColor.getColorValue());
            Color twocolor = new Color(ModuleList.twoColor.getColorValue());

            RoundedUtil.drawGradientRound((dt.getX()), dt.getY(), (100.0F - Timer.ticks * 2.0F), 10.0f, 4, onecolor, onecolor, twocolor, twocolor);
            mc.sfui18.drawCenteredString("" + MathematicHelper.round(100.0F - Timer.ticks * 2.0F, 1) + "%", dt.getX() + 50, (dt.getY() + 2), -1);
            //RenderUtils.drawRect2(dt.getX()-26, (dt.getY() + 14),52,11,Color.BLACK.getRGB());
            mc.sfui18.drawCenteredString("Smart Timer", dt.getX() + 50, (dt.getY() + 16), -1);
            float y = dt.getY();
            float x = dt.getX();
        }

    }

    public void onDisable() {
        super.onDisable();
        active = true;
        ticks = 0;
        this.mc.timer.timerSpeed = 1.0f;
    }

}