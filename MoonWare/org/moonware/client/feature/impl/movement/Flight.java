package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.movement.Move.PacketUtil;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class Flight extends Feature {

    public static ListSetting flyMode;
    public static NumberSetting speed;
    private int boostTick;
    TimerHelper timerUtils = new TimerHelper();

    public Flight() {
        super("Flight", "Позволяет вам летать без креатив режима", Type.Movement);
        flyMode = new ListSetting("Flight Mode", "Vanilla", () -> true, "Vanilla","ReallyworldUps","Glide","MatrixSP", "Sunrise Blocks", "Matrix Glide", "Guardian");
        speed = new NumberSetting("Flight Speed", 5F, 0.1F, 15F, 0.1F, () -> flyMode.currentMode.equals("Vanilla") || flyMode.currentMode.equals("WellMore"));
        //   TPAmount = new NumberSetting("TPAmount", 25, 5, 100, 1, () -> flyMode.currentMode.equals("ReallyWorld"));
        addSettings(flyMode, speed);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.player.speedInAir = 0.02f;
        Minecraft.timer.timerSpeed = 1.0f;
        Minecraft.player.capabilities.isFlying = false;
        if (flyMode.getOptions().equalsIgnoreCase("WellMore")) {
            Minecraft.player.motionY = 0;
            Minecraft.player.motionX = 0;
            Minecraft.player.motionZ = 0;
        }
    }


    @EventTarget
    public void onPreMotionn(EventPreMotion event) {
        String mode = flyMode.getCurrentMode();
        if (mode.equalsIgnoreCase("MatrixSP")) {
            //final Integer item = PlayerUtil.findItem(null);
            //if (item == null) return;

            //PacketUtil.sendPacketWithoutEvent(new CPacketHeldItemChange(item));
            EntityPlayerSP thePlayer = Minecraft.player;
            Minecraft.timer.timerSpeed = 1.6F;

            if (thePlayer.ticksExisted % 3 == 0) {
                Minecraft.timer.timerSpeed = 0.6F;
                thePlayer.motionY = 0.42F;
            }

            if (thePlayer.onGround) {
                thePlayer.jump();
            }

            if (thePlayer.fallDistance > 1) {
                PacketUtil.sendPacket(new CPacketPlayerTryUseItemOnBlock());
                thePlayer.motionY = -((thePlayer.posY) - Math.floor(thePlayer.posY));
            }

            if (thePlayer.ticksExisted % 4 == 0) {
                if (thePlayer.ticksExisted % 24 == 0) {
                    PacketUtil.sendPacket(new CPacketPlayer.PositionRotation(thePlayer.posX, thePlayer.posY, thePlayer.posZ, thePlayer.rotationYaw, thePlayer.rotationPitch, true));
                }
                PacketUtil.sendPacket(new CPacketPlayerTryUseItemOnBlock());
            }

            if (thePlayer.motionY >= 0) {
                thePlayer.motionY = 0.42f;
                thePlayer.fallDistance = 0;
            }
        }
        if (mode.equalsIgnoreCase("Sunrise Blocks")) {
            if (Minecraft.playerController.getCurrentGameType() == GameType.ADVENTURE) {
            } else {
                int block = -1;
                for (int i = 0; i < 9; i++) {
                    ItemStack s = Minecraft.player.inventory.getStackInSlot(i);
                    if (s.getItem() instanceof ItemBlock) {
                        block = i;
                        break;
                    }
                }
                if (block == -1 && timerUtils.hasReached(1000)) {
                    MWUtils.sendChat("Вам нужен любой блок в хотбаре для этого Флая.");
                    timerUtils.reset();
                    return;
                }
                if (timerUtils.hasReached(1.1 * 55)) {
                    try {
                        if (block != -1 && Minecraft.objectMouseOver != null && Minecraft.objectMouseOver.hitVec != null
                                && Minecraft.objectMouseOver.getBlockPos() != null
                                && Minecraft.objectMouseOver.sideHit != null) {
                            Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(block));
                            float prevPitch = Minecraft.player.rotationPitch;
                            Minecraft.player.rotationPitch = -60;
                            Minecraft.gameRenderer.getMouseOver(1);
                            Vec3d facing = Minecraft.objectMouseOver.hitVec;
                            BlockPos stack = Minecraft.objectMouseOver.getBlockPos();
                            float f = (float) (facing.xCoord - (double) stack.getX());
                            float f1 = (float) (facing.yCoord - (double) stack.getY());
                            float f2 = (float) (facing.zCoord - (double) stack.getZ());
                            Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player,
                                    CPacketEntityAction.Action.START_SNEAKING));
                            if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player).add(0, 2, 0))
                                    .getBlock() == Blocks.AIR) {
                                Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(stack,
                                        Minecraft.objectMouseOver.sideHit, EnumHand.MAIN_HAND, f, f1, f2));
                            } else {
                                Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(
                                        CPacketPlayerDigging.Action.START_DESTROY_BLOCK, stack, Minecraft.objectMouseOver.sideHit));
                                Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(
                                        CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, stack, Minecraft.objectMouseOver.sideHit));
                            }
                            Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player,
                                    CPacketEntityAction.Action.STOP_SNEAKING));
                            Minecraft.player.rotationPitch = prevPitch;
                            Minecraft.gameRenderer.getMouseOver(1);
                            Minecraft.player.connection
                                    .sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
                            event.setOnGround(true);
                            Minecraft.player.onGround = true;
                            Minecraft.player.isCollidedVertically = true;
                            Minecraft.player.isCollidedHorizontally = true;
                            Minecraft.player.isAirBorne = true;
                            Minecraft.player.jump();
                            timerUtils.reset();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String mode = flyMode.getCurrentMode();
        if (mode.equalsIgnoreCase("Matrix Glide")) {
            if (!Minecraft.player.onGround && Minecraft.player.fallDistance >= 1) {
                Minecraft.timer.timerSpeed = Minecraft.player.ticksExisted % 4 == 0 ? 0.08f : 0.5F;
                Minecraft.player.motionY *= 0.007;
                Minecraft.player.fallDistance = 0F;
            } else {
                Minecraft.timer.timerSpeed = 1;
            }
        } else if (mode.equalsIgnoreCase("Guardian")) {
            float speed = (float) 0.25;

            Minecraft.player.noClip = true;
            Minecraft.player.fallDistance = 0;
            Minecraft.player.onGround = false;

            Minecraft.player.capabilities.isFlying = false;

            Minecraft.player.motionX = 0;
            Minecraft.player.motionY = 0;
            Minecraft.player.motionZ = 0;

            Minecraft.player.jumpMovementFactor = 0.35f;

            if (Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                if (Minecraft.player.ticksExisted % 5 == 0) {
                    Minecraft.player.motionY += speed;
                }
            }
            if (Minecraft.gameSettings.keyBindSneak.isKeyDown()) {
                if (Minecraft.player.ticksExisted % 5 == 0) {
                    Minecraft.player.motionY -= speed;
                }
            }
        }else if (mode.equalsIgnoreCase("ASPYRIA")) {
            if (MovementHelper.isMoving()) {
                if (Minecraft.player.onGround) {
                    boostTick = 1;
                    Minecraft.player.jump();
                } else if (boostTick < 1) {
                    boostTick++;
                } else {
                    if (Helper.timerHelper.hasReached(460F) && Minecraft.player.fallDistance >= 5.0f) {
                        Minecraft.player.motionX *= 1 + 0.14;
                        Minecraft.player.motionZ *= 1 + 0.14;
                        Helper.timerHelper.reset();
                    }
                    boostTick = 0;
                }
            }
        }
        if (mode.equalsIgnoreCase("ReallyworldUps")) {
            if (Minecraft.player.fallDistance < 0.9f && Helper.timerHelper.hasReached(10.0F)) {
                Minecraft.player.onGround = true;
                Minecraft.player.Matrixjumphelper();
                //mc.player.speedInAir = 0.4516574f;
                Minecraft.player.onGround = Minecraft.player.ticksExisted % 1.5 != 0;
                //mc.player.motionY = 0.34146434072f;
                Minecraft.player.Matrixjump();
                Minecraft.player.isJumping = false;
                Helper.timerHelper.reset();
            }else {
                Minecraft.player.speedInAir = 0.2f;

            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = flyMode.getCurrentMode();

        setSuffix(flyMode.getCurrentMode());

        if (mode.equalsIgnoreCase("Vanilla")) {
            Minecraft.player.capabilities.isFlying = true;
            MovementHelper.setSpeed(speed.getNumberValue());
            if (Minecraft.gameSettings.keyBindSneak.isKeyDown()) {
                Minecraft.player.motionY -= speed.getNumberValue();
            } else if (Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.player.motionY += speed.getNumberValue();
            }

        }
    }
}