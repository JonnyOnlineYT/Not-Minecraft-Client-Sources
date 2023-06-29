package org.spray.heaven.features.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.util.MovementUtil;

@ModuleInfo(name = "DamageFly", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class DamageFly extends Module {

    private boolean damaged;
    private int ticks;
    private double packetY;

    @Override
    public void onDisable() {
        mc.player.jumpMovementFactor = 0.02f;
        mc.player.speedInAir = 0.02f;
        ticks = 0;
        damaged = false;
        mc.getTimer().reset();
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getType() == EventType.RECIEVE) {
            if (event.getPacket() instanceof SPacketEntityVelocity) {
                SPacketEntityVelocity sp = (SPacketEntityVelocity)event.getPacket();
                if (sp.getEntityID() == mc.player.getEntityId())
                    packetY = sp.getMotionY() / 8000D;
            }

            if (event.getPacket() instanceof SPacketEntityStatus) {
                SPacketEntityStatus sp = (SPacketEntityStatus) event.getPacket();
                if (sp.getOpCode() == 2 && sp.getEntity(mc.world) == mc.player) {
                    damaged = true;
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        if (damaged) {
            mc.gameSettings.keyBindJump.setPressed(false);
            ticks++;
            mc.player.motionY = packetY;
            mc.player.jumpMovementFactor = 0.4f;
            MovementUtil.setMotion(MovementUtil.getSqrtSpeed());
        }

        if (ticks >= 34) {
            mc.getTimer().setSpeed(1.1766f);
        }

        // event.y = mc.player.posY += 0.42; mc.player.onGround = true;
        if (ticks >= 13) {
            mc.player.jumpMovementFactor = 0.3544f;
            mc.player.speedInAir = 666f / 1.1454f;
            mc.getTimer().setSpeed(1.2f);
        }
//
//        if (ticks > 6) {
//            mc.player.motionY += 0.08;
//        }

        if (ticks >= 55) {
            disable();
        }
    }

}
