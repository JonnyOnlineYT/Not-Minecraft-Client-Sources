package Celestial.module.impl.Movement;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventReceivePacket;
import Celestial.event.events.impl.packet.EventSendPacket;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.math.TimerHelper;
import Celestial.utils.movement.MovementUtils;
import Celestial.Smertnix;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.ListSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class NoClip extends Module {
    public static final ListSetting noClipMode = new ListSetting("NoClip Mode", "Vanilla", "Vanilla", "NCP");
    public NumberSetting customSpeedXZ;
    public BooleanSetting customSpeed;
    TimerHelper timerHelper = new TimerHelper();

    public NoClip() {
        super("NoClip", "Позволяет ходить в блоках", ModuleCategory.Movement);
        customSpeed = new BooleanSetting("Custom Speed", false, () -> !noClipMode.currentMode.equals("NCP"));
        customSpeedXZ = new NumberSetting("XZ Speed", 1.0f, 0.01f, 1.5f, 0.01f, () -> !noClipMode.currentMode.equals("NCP") && customSpeed.getCurrentValue());
        addSettings(noClipMode,customSpeed,customSpeedXZ);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (noClipMode.currentMode.equals("NCP") && event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            Helper.mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
            Helper.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), Helper.mc.player.posY, packet.getZ(), packet.getYaw(), packet.getPitch(), false));
            Helper.mc.player.setPosition(packet.getX(), Helper.mc.player.posY, packet.getZ());
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (noClipMode.currentMode.equals("NCP") && event.getPacket() instanceof CPacketPlayer && !Helper.mc.player.onGround) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            packet.y = Helper.mc.player.posY + 50.0;
        }
    }

    @EventTarget
    public void onPreMotionUpdate2(EventPreMotion event) {
        if (noClipMode.currentMode.equals("NCP")) {
            Helper.mc.player.connection.sendPacket(new CPacketEntityAction(Helper.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            if (Helper.mc.player.onGround) {
                Helper.mc.player.jump();
            }
        }
    }

    @EventTarget
    public void onPreMotionUpdate(EventPreMotion event) {
        if (noClipMode.currentMode.equals("NCP")) {
            Helper.mc.player.setVelocity(0.0, 0.0, 0.0);
            event.setCancelled(true);
            float speedY = 0.0f;
            if (Helper.mc.player.movementInput.jump) {
                if (!timerHelper.hasReached(3000.0)) {
                    speedY = Helper.mc.player.ticksExisted % 20 == 0 ? -0.04f : 0.031f;
                } else {
                    timerHelper.reset();
                    speedY = -0.08f;
                }
            } else if (Helper.mc.player.movementInput.sneak) {
                speedY = -0.0031f;
            }
            double[] dir = MovementUtils.forward(0.02f);
            Helper.mc.player.motionX = dir[0];
            Helper.mc.player.motionY = speedY;
            Helper.mc.player.motionZ = dir[1];
            Helper.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Helper.mc.player.posX + Helper.mc.player.motionX, Helper.mc.player.posY + Helper.mc.player.motionY, Helper.mc.player.posZ + Helper.mc.player.motionZ, Helper.mc.player.rotationYaw, Helper.mc.player.rotationPitch, Helper.mc.player.onGround));
            double x = Helper.mc.player.posX + Helper.mc.player.motionX;
            double y = Helper.mc.player.posY + Helper.mc.player.motionY;
            double z = Helper.mc.player.posZ + Helper.mc.player.motionZ;
            Helper.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y -= -1337.0, z, Helper.mc.player.onGround));
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (noClipMode.currentMode.equals("Vanilla")) {
            if (Helper.mc.player != null) {
                Helper.mc.player.noClip = true;
                Helper.mc.player.motionY = 0.00001;
                if(customSpeed.getCurrentValue()) {
                    Helper.mc.player.motionX *= customSpeedXZ.getCurrentValue();
                    Helper.mc.player.motionZ *= customSpeedXZ.getCurrentValue();
                }
                if (Helper.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Helper.mc.player.motionY = 0.4;
                }
                if (Helper.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Helper.mc.player.motionY = -0.4;
                }
            }
        }
    }


    public static boolean isNoClip(Entity entity) {
        if (Smertnix.instance.featureManager.getFeature(NoClip.class).isEnabled() && Helper.mc.player != null && (Helper.mc.player.ridingEntity == null || entity == Helper.mc.player.ridingEntity))
            return true;
        return entity.noClip;

    }

    public void onDisable() {
        Helper.mc.player.noClip = false;
        super.onDisable();
    }
}
