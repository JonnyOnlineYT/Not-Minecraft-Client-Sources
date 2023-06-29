package org.spray.heaven.features.module.modules.movement;

import java.util.Arrays;

import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.BlockUtil;
import org.spray.heaven.util.EntityUtil;
import org.spray.heaven.util.MovementUtil;
import org.spray.heaven.util.Timer;

@ModuleInfo(name = "Fly", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class Fly extends Module {

    private final Setting mode = register(new Setting("Mode", "Motion",
            Arrays.asList("CubeCraft", "MatrixPearl", "ReallyWorld", "Wellmore", "WellMore 2", "Motion", "SunRise", "NCPspigot")));
    private final Setting delay = register(new Setting("Jump Delay", 3, 1, 10)
            .setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Old Sunrise Drop")));

    private final Setting throwPearl = register(new Setting("Auto Throw Pearl", true)
            .setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("MatrixPearl")));

    private final Timer timer = new Timer();

    @Override
    public void onEnable() {
        if (mode.getCurrentMode().equalsIgnoreCase("MatrixPearl") && throwPearl.isToggle())
            EntityUtil.throwPearl();
    }

    @Override
    public void onDisable() {
        mc.getTimer().reset();
        mc.player.capabilities.setFlySpeed(0.05F);
        if (mc.player.capabilities.isFlying)
            mc.player.capabilities.isFlying = false;
    }

    @Override
    public void onUpdate() {
        setSuffix(mode.getCurrentMode());
        if (mode.getCurrentMode().equalsIgnoreCase("CubeCraft")) {
            mc.getTimer().setSpeed(0.6f);
            if (timer.hasReached(2)) {
                MovementUtil.forward(MovementUtil.getDirection(), 2.4);
                timer.reset();
            } else
                MovementUtil.forward(MovementUtil.getDirection(), 0.2);
        } else if (mc.getTimer().getSpeed() != 1)
            mc.getTimer().reset();

        if (mode.getCurrentMode().equalsIgnoreCase("MatrixPearl")) {
            if (mc.player.hurtTime > 0) {
                if (mc.player.onGround) {
                    mc.player.jump();
                } else {
                    mc.player.motionY += .3;
                    mc.player.jumpMovementFactor = .9f;
                }
            }
        }

        if (mode.getCurrentMode().equalsIgnoreCase("ReallyWorld")) {
            mc.player.jumpTicks = 0;
            if (mc.player.movementInput.jump)
                mc.player.jump();
        }

        if (mode.getCurrentMode().equalsIgnoreCase("Old Sunrise Drop")) {

            for (int i = 9; i < 45; i++) {
                if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                    if (mc.player.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock
                            && mc.player.ticksExisted % (int) delay.getValue() == 0) {
                        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.THROW,
                                mc.player);
                        mc.player.jump();
                        break;
                    }
                }
            }
        }
        if (mode.getCurrentMode().equalsIgnoreCase("Wellmore")) {
            if (!mc.player.onGround) {
                mc.player.capabilities.isFlying = true;
                mc.player.capabilities.setFlySpeed(1.3F);
                mc.player.motionX = 0.0;
                mc.player.motionY = -0.02;
                mc.player.motionZ = 0.0;

                if (timer.hasReached(150)) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                    timer.reset();

                }
            }
        }

        if (mode.getCurrentMode().equalsIgnoreCase("Damage Push")) {
            mc.player.capabilities.isFlying = false;

            mc.player.motionY = -0.01;

            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
            MovementUtil.strafe(2);

            if (timer.hasReached(150) && mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.getConnection().sendPacket(
                        new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 5, mc.player.posZ, false));
                mc.getConnection().sendPacket(new CPacketPlayer.Position(0.5, -1000.0, 0.5, false));
                float yaw = (float) Math.toRadians(mc.player.rotationYaw);
                float x = (float) (-Math.sin(yaw) * 0.4f);
                float z = (float) (Math.cos(yaw) * 0.4f);
                mc.player.setPosition(mc.player.posX + x, mc.player.posY, mc.player.posZ + z);
                timer.reset();
            }
        }
        if (mode.getCurrentMode().equalsIgnoreCase("NCPspigot")) {
            float yaw = (float) Math.toRadians(mc.player.rotationYaw);
            double x = -MathHelper.sin(yaw) * 0.25;
            double z = MathHelper.cos(yaw) * 0.25;
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY, mc.player.posZ + z, false));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY - 490, mc.player.posZ + z, true));
            mc.player.motionY = 0;
        }

        if (mode.getCurrentMode().equalsIgnoreCase("SunRise")) {
            MovementUtil.strafe(MovementUtil.calcMoveYaw(mc.player.rotationYaw), 0.1);
            mc.player.motionY = 0;

            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.player.motionY = 0.3;

            if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.player.motionY = -0.3;
            for (int i = 9; i < 45; i++) {
                if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                    if (mc.player.inventoryContainer.getSlot(i).getStack().getItem() == Items.ENDER_EYE
                            && mc.player.ticksExisted % (int) 8 == 0) {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.inventoryContainer.getSlot(i).getStack().useItemRightClick(mc.world, mc.player, EnumHand.MAIN_HAND);
                        break;
                    }
                }
            }
        }
        if (mode.getCurrentMode().equalsIgnoreCase("Motion")) {
            MovementUtil.strafe(MovementUtil.calcMoveYaw(mc.player.rotationYaw), 2.8);
            mc.player.motionY = 0;

            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.player.motionY = 0.8;

            if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.player.motionY = -0.8;
        }

        if (mode.getCurrentMode().equalsIgnoreCase("Wellmore 2")) {
            Vec3d vec = Vec3d.ZERO;
            double speed = 3;

            if (mc.player.ticksExisted % 20 == 0 && BlockUtil.getBlock(
                    new BlockPos(new BlockPos(mc.player.getPosition().add(0, -0.069, 0)))) instanceof BlockAir) {
                vec = vec.addVector(0, -0.069, 0);
            }

            mc.player.motionX = vec.xCoord;
            mc.player.motionY = vec.yCoord;
            mc.player.motionZ = vec.zCoord;

            mc.player.motionY = 0.069;

            Vec3d forward = new Vec3d(0, 0, speed).rotateYaw(-(float) Math.toRadians(mc.player.rotationYaw));
            Vec3d strafe = forward.rotateYaw((float) Math.toRadians(90));

            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.player.motionY += speed;
            if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.player.motionY -= speed;
            if (mc.gameSettings.keyBindBack.isKeyDown()) {
                mc.player.motionX += -forward.xCoord;
                mc.player.motionZ += -forward.zCoord;
            }
            if (mc.gameSettings.keyBindForward.isKeyDown()) {
                mc.player.motionX += forward.xCoord;
                mc.player.motionZ += forward.zCoord;
            }
            if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                mc.player.motionX += strafe.xCoord;
                mc.player.motionZ += strafe.zCoord;
            }
            if (mc.gameSettings.keyBindRight.isKeyDown()) {
                mc.player.motionX += -strafe.xCoord;
                mc.player.motionZ += -strafe.zCoord;
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (!event.getType().equals(EventType.SEND))
            return;

        if (mode.getCurrentMode().equalsIgnoreCase("Wellmore")
                || mode.getCurrentMode().equalsIgnoreCase("Wellmore 2")) {
            if (event.getPacket() instanceof CPacketKeepAlive) {
                //		event.cancel();
            }
            if (event.getPacket() instanceof CPacketConfirmTeleport) {
                event.cancel();
            }
        }
    }
}
