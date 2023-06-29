package org.spray.heaven.features.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import java.util.ArrayList;
import java.util.Arrays;

@ModuleInfo(name = "AntiVoid", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
    public class AntiVoid extends Module {
    public float fall = 0;
    public boolean toggleFeature = false;
    private Setting fallDist = register(new Setting("fallDist", 15, 0, 20));
    private Setting antiVoidMode = register(new Setting("Mode", "High-Motion",
            new ArrayList<>(Arrays.asList("Flag","Invalid Pitch", "Invalid Position", "Spoof", "Packet", "High-Motion"))));
    @EventTarget
    public void onUpdate(MotionEvent event) {
        String mode = String.valueOf(antiVoidMode.getValue());
        this.setSuffix(mode);

        if (!mc.player.onGround && !mc.player.isCollidedVertically) {
            if (mc.player.fallDistance > fallDist.getValue()) {
                if (mc.world.getBlockState(new BlockPos(0, -fall, 0)).getBlock() == Blocks.AIR) {
                    if (mode.equalsIgnoreCase("High-Motion")) {
                        mc.player.motionY += 3f;
                    } else if (mode.equals("Packet")) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 12, mc.player.posZ, true));
                    } else if (mode.equals("Spoof")) {
                        mc.player.connection.sendPacket(new CPacketPlayer(true));
                    } else if (mode.equals("Flag")) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + 1, mc.player.posY + 1, mc.player.posZ + 1, true));
                    } else if (mode.equals("Invalid Pitch")) {
                        event.setOnGround(true);
                        event.setPitch(-91);
                        mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
                    } else if (mode.equals("Invalid Position")) {
                        if (mc.player.onGround) {
                            toggleFeature = true;
                        }
                    }
                }
            }
        }
    }
}

