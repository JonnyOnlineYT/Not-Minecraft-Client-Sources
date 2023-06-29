package org.spray.heaven.features.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.UpdateEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.util.MovementUtil;

@ModuleInfo(name = "Phase", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class Phase extends Module {
    @EventTarget
    public void onUpdate(UpdateEvent eventUpdate) {
            if (mc.player.isCollidedHorizontally) {
                MovementUtil.setMotion(0);

                float yaw = MovementUtil.getDirection();
                double x = mc.player.posX + -MathHelper.sin(yaw) * 0.00000001;
                double z = mc.player.posZ + MathHelper.cos(yaw) * 0.00000001;

                double x2 = mc.player.posX + -MathHelper.sin(yaw) * 0.85;
                double z2 = mc.player.posZ + MathHelper.cos(yaw) * 0.85;
                double y = mc.player.posY;
                mc.player.connection.sendPacket(new CPacketConfirmTeleport((int) MathHelper.getRandomFloat(4, 1)));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(x2, y, z2, true));
            }
        }
    }


