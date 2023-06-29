package Celestial.module.impl.Movement;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventSendPacket;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.inventory.InventoryUtil;
import Celestial.utils.math.TimerHelper;
import Celestial.utils.movement.MovementUtils;
import Celestial.ui.settings.impl.ListSetting;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("all")
public class Speed extends Module {
    public static float ticks = 0;
    public TimerHelper timerHelper = new TimerHelper();
    public static ListSetting speedMode = new ListSetting("Speed Mode", "Matrix New", () -> true, "ReallyWorld2","ReallyWorld","Matrix New","Sunrise Damage");
    private float jumps = 0;

    public Speed() {
        super("Speed", "Быстрая скорость бега", ModuleCategory.Movement);
        addSettings(speedMode);

    }

    @EventTarget
    public void on(EventSendPacket event) {
        if (speedMode.currentMode.equals("Matrix Elytra")) {
            if (Helper.mc.player.isOnLadder() || Helper.mc.player.isInLiquid() || InventoryUtil.getSlotWithElytra() == -1) {
                return;
            }
            if (event.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer cPacketPlayer = (CPacketPlayer) event.getPacket();
                cPacketPlayer.onGround = false;
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Elytra")) {
            if (Helper.mc.player.isInWeb || Helper.mc.player.isOnLadder() || Helper.mc.player.isInLiquid() || Helper.mc.gameSettings.keyBindJump.isKeyDown() || InventoryUtil.getSlotWithElytra() == -1) {
                return;
            }
            if (Helper.mc.player.ticksExisted % 6 == 0) {
                InventoryUtil.disabler(InventoryUtil.getSlotWithElytra());
            }
            if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY - 0.9D, Helper.mc.player.posZ)).getBlock() != Blocks.AIR) {
                Helper.mc.player.motionX *= 1.8f;
                Helper.mc.player.motionZ *= 1.8f;
                MovementUtils.strafe();
            }

        } else if (mode.equalsIgnoreCase("NCPHop")) {
            if (MovementUtils.isMoving()) {
                if (Helper.mc.player.onGround) {
                    Helper.mc.player.jump();
                    Helper.mc.player.speedInAir = 0.0223f;
                }
                MovementUtils.strafe();
            } else {
                Helper.mc.player.motionX = 0.0;
                Helper.mc.player.motionZ = 0.0;
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix")) {
            if (Helper.mc.player.isInWeb || Helper.mc.player.isOnLadder() || Helper.mc.player.isInLiquid()) {
                r
            }
                mc.timer.timerSpeed = 2F;
            }
        } else if (mode.equalsIgnoreCase("Sunrise Damage")) {
            if (MovementUtils.isMoving()) {
                if (Helper.mc.player.onGround) {
                    Helper.mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 9.5D /20, 0, Math.cos(MovementUtils.getAllDirection()) * 9.5D / 20);
                    MovementUtils.strafe();
                } else if (Helper.mc.player.isInWater()) {
    }

    @Override
    public void onEnable() {
        if (speedMode.currentMode.equalsIgnoreCase("NCPHop")) {
            Helper.mc.timer.timerSpeed = 1.0865f;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Helper.mc.player.speedInAir = 0.02f;
        Helper.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}