package org.spray.heaven.features.module.modules.movement;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.LiquidSolidEvent;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.events.UpdateEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.EntityUtil;
import org.spray.heaven.util.MovementUtil;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name = "Jesus", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class Jesus extends Module {

    private final Setting mode = register(
            new Setting("Mode", "MatrixPixel", new ArrayList<>(Arrays.asList("Matrix 6.3", "MatrixPixel", "NCP", "ReallyWorld"))));
    private final Setting pixelSpeed = register(new Setting("Pixel Speed", 4, 3, 10))
            .setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("MatrixPixel"));

    @Override
    public void onDisable() {
        mc.getTimer().reset();
        mc.player.jumpMovementFactor = 0.02f;
        mc.player.speedInAir = 0.02F;
    }


    @EventTarget
    public void onLiquidBB(LiquidSolidEvent event) {
        if (mode.getCurrentMode().equalsIgnoreCase("ReallyWorld")) {
//            event.cancel();
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        setSuffix(mode.getCurrentMode());
        switch (mode.getCurrentMode()) {
            case "Matrix 6.3":
                if (mc.player.isInWater())
                    if (mc.player.isCollidedHorizontally || mc.gameSettings.keyBindJump.isPressed()) {
                        mc.player.motionY = 0.09;
                        return;
                    }
                if (EntityUtil.isFluid(0.3)) {
                    mc.player.motionY = 0.1;

                } else if (EntityUtil.isFluid(0.2)) {
                    mc.getTimer().reset();
                    mc.player.motionY = 0.2;
                    MovementUtil.strafe(0.6);
                } else if (EntityUtil.isFluid(0)) {
                    mc.getTimer().setSpeed(0.8f);
                    MovementUtil.hClip(1.2);
                    mc.player.motionX = 0;
                    mc.player.motionZ = 0;
                }
                break;




            case "NCP":

                double x = mc.player.posX;
                double y = mc.player.posY;
                double z = mc.player.posZ;
                mc.getTimer().updateTimer();
                if (
                        mc.world.getBlockState(new BlockPos(x,y,z)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x,y,z)).getBlock() == Blocks.LAVA
                ) {
                    IceSpeed.go = false;
                }
                if (
                        mc.world.getBlockState(new BlockPos(x,y,z)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x,y,z+0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x,y,z-0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z+0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z-0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z+0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z-0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x,y,z)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x,y,z+0.3)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x,y,z-0.3)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z+0.3)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z-0.3)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z+0.3)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z-0.3)).getBlock() == Blocks.LAVA
                ) {
                    if (mc.player.movementInput.jump || mc.player.isCollidedHorizontally) {
                        if (mc.player.isCollidedHorizontally) {
                            mc.player.setPosition(x, y + 0.2, z);
                        } mc.player.onGround = true;if (mc.player.fallDistance < 0.2){  mc.player.motionY += 0.021f; }}
                    mc.player.motionX = 0; mc.player.motionZ = 0; mc.player.motionY = 0.04;
                    if (!(mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.LAVA)) {
                        if (mc.player.fallDistance != 0 && mc.player.motionX == 0 && mc.player.motionZ == 0) {
                            mc.player.setPosition(x, y - 0.0400005, z);
                            if(mc.player.fallDistance < 0.08) {
                                mc.player.setPosition(x, y + 0.2, z);
                            }}}
                    if(!Wrapper.getPlayer().isPotionActive(Potion.getPotionById(1))) {
                        mc.player.jumpMovementFactor = 0.2865f;}
                    if(Wrapper.getPlayer().isPotionActive(Potion.getPotionById(1))) {
                        mc.player.jumpMovementFactor = 0.4005f;}
                }
                MovementUtil.setMotion(MovementUtil.getSpeed());
                if (!mc.gameSettings.keyBindJump.isKeyDown() && (mc.player.isInWater() || mc.player.isInLava())) {
                    mc.player.motionY = 0.12;
                    mc.getTimer().setSpeed(1.1F);
                    if (mc.player.isInWater() && mc.world.getBlockState(new BlockPos(x, y + 0.9, z)).getBlock() == Blocks.WATER &&
                            mc.world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.AIR &&
                            !(mc.world.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.WATER)) {
                        mc.player.posY += 0.1;
                        mc.player.motionY = 0.42;
                    }
                }

                break;
        }
    }

    @EventTarget
    public void onMotion(UpdateEvent event) {
        if (mode.getCurrentMode().equalsIgnoreCase("MatrixPixel")) {
            mc.getTimer().setSpeed(1f);
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() ==
                    Blocks.WATER ||
                    mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() ==
                            Blocks.LAVA) {
                mc.player.motionX = 0;
                mc.player.motionY = 0.0181f;
                mc.player.motionZ = 0;
                mc.player.speedInAir = (float) pixelSpeed.getValue() / 1.345f;
                mc.player.jumpMovementFactor = 0;
                if (mc.player.fallDistance >= 3.4609) {
                    mc.player.motionY = -0.15f;
                }
                if (mc.player.isCollidedHorizontally) {
                    mc.player.speedInAir = 0.02f;
                    mc.player.jumpMovementFactor = 0.3f;
                    mc.player.motionY += 0.09f;
                }
            } else {
                if (mc.player.speedInAir == (float) pixelSpeed.getValue() / 1.345f) {
                    mc.player.speedInAir = 0.02f;
                }
            }
            if (mc.player.isInWater() || mc.player.isInLava()) {
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1.5f, mc.player.posZ))
                        .getBlock() == Blocks.WATER ||
                        mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1.5f, mc.player.posZ))
                                .getBlock() == Blocks.LAVA) {
                    mc.player.motionY += 0.099;
                } else {
                    mc.player.motionY += 0.19;
                }
            }
        } else if (mode.getCurrentMode().equalsIgnoreCase("ReallyWorld")) {
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.2, mc.player.posZ)).getBlock() == Blocks.WATER) {
                mc.player.jumpMovementFactor = 0;
            }
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.008, mc.player.posZ)).getBlock() == Blocks.WATER
                    && !mc.player.onGround) {
                boolean isUp = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.03, mc.player.posZ)).getBlock() == Blocks.WATER;
                mc.player.jumpMovementFactor = 0;
                float yport = MovementUtil.getSpeed() > 0.1 ? 0.02f : 0.032f;
                mc.player.setVelocity(mc.player.motionX,mc.player.fallDistance < 3.5 ? (mc.player.isCollidedHorizontally && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) ? 0.2105f : (isUp ? yport : -yport) : -0.5, mc.player.motionZ);
                if (mc.player.posY > (int)mc.player.posY + 0.89 && mc.player.posY <= (int)mc.player.posY + 1 || mc.player.fallDistance > 3.5) {
                    mc.player.posY = (int)mc.player.posY + 1 + 1E-45;

                    if (!mc.player.isInWater()) {
                            MovementUtil.setMotion(1);
                    }

                }
            }
            if ((mc.player.isInWater() || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.15, mc.player.posZ)).getBlock() == Blocks.WATER)) {
                mc.player.motionY = 0.16;
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)).getBlock() == Blocks.AIR) {
                    mc.player.motionY = 0.12;
                }
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() == Blocks.AIR) {
                    mc.player.motionY = 0.18;
                }
            }
        }
    }


    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(EventType.SEND)) {
            if (event.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer cp = (CPacketPlayer) event.getPacket();
                if (EntityUtil.isFluid(0.3))
                    cp.setOnGround(false);
            }
        }
    }

}