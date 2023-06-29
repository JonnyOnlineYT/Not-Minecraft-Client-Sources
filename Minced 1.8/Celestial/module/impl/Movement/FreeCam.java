package Celestial.module.impl.Movement;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventReceivePacket;
import Celestial.event.events.impl.packet.EventSendPacket;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.event.events.impl.player.EventUpdateLiving;
import Celestial.event.events.impl.render.EventRender2D;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.movement.MovementUtils;
import Celestial.utils.other.ChatUtils;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

import java.awt.*;

public class FreeCam
        extends Module {
    public NumberSetting speed = new NumberSetting("Flying Speed", 1.0f, 0.1f, 5.0f, 0.1f, () -> true);
    public BooleanSetting disableOnDamage = new BooleanSetting("Disable on damage", false, () -> true);
    public BooleanSetting reallyWorld = new BooleanSetting("ReallyWorld", true, () -> true);
    private double oldX;
    private double oldY;
    private double oldZ;
    private int oldPosY;

    public FreeCam() {
        super("FreeCam", "Позволяет осматривать территорию вокруг", ModuleCategory.Movement);
        addSettings(speed, reallyWorld, disableOnDamage);
    }

    @Override
    public void onEnable() {
        oldX = Helper.mc.player.posX;
        oldY = Helper.mc.player.posY;
        oldZ = Helper.mc.player.posZ;
        oldPosY = (int) Helper.mc.player.posY;
        Helper.mc.player.noClip = true;
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(Helper.mc.world, Helper.mc.player.getGameProfile());
        fakePlayer.copyLocationAndAnglesFrom(Helper.mc.player);
        fakePlayer.posY -= 0.0;
        fakePlayer.rotationYawHead = Helper.mc.player.rotationYawHead;
        Helper.mc.world.addEntityToWorld(-69, fakePlayer);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Helper.mc.player.capabilities.isFlying = false;
        Helper.mc.world.removeEntityFromWorld(-69);
        Helper.mc.player.motionZ = 0.0;
        Helper.mc.player.motionX = 0.0;
        Helper.mc.player.noClip = true;
        Helper.mc.player.setPositionAndRotation(oldX, oldY, oldZ, Helper.mc.player.rotationYaw, Helper.mc.player.rotationPitch);
        super.onDisable();
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (!reallyWorld.getCurrentValue()) {
            return;
        }
        if (Helper.mc.player == null || Helper.mc.world == null || Helper.mc.player.ticksExisted < 1) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (Helper.mc.player == null || Helper.mc.world == null || Helper.mc.player.ticksExisted < 1) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketEntityAction) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
            ScaledResolution sr = new ScaledResolution(Helper.mc);
            String pos = "" + (int)(-((double)oldPosY - Helper.mc.player.posY));
            String plusOrMinus = "";
            String clipValue = ".vclip " + plusOrMinus + pos;
            Helper.mc.mntsb_16.drawBlurredStringWithShadow(clipValue, (float)sr.getScaledWidth() / 2.0f + 15.0f, (float)sr.getScaledHeight() / 2.0f,15,new Color(17,17,17,255), -1);
    }

    @EventTarget
    public void onUpdate(EventUpdateLiving event) {
        if (Helper.mc.player == null || Helper.mc.world == null) {
            return;
        }
        if (disableOnDamage.getCurrentValue()) {
            if (Helper.mc.player.hurtTime <= 8) {
                Helper.mc.player.noClip = true;
                Helper.mc.player.capabilities.isFlying = true;
                if (Helper.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Helper.mc.player.motionY = speed.getCurrentValue() / 1.5f;
                }
                if (Helper.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Helper.mc.player.motionY = -speed.getCurrentValue() / 1.5f;
                }
                MovementUtils.setSpeed(speed.getCurrentValue());
            } else if (!MovementUtils.isUnderBedrock()) {
                Helper.mc.player.capabilities.isFlying = false;
                Helper.mc.renderGlobal.loadRenderers();
                Helper.mc.player.noClip = false;
                Helper.mc.player.setPositionAndRotation(oldX, oldY, oldZ, Helper.mc.player.rotationYaw, Helper.mc.player.rotationPitch);
                Helper.mc.world.removeEntityFromWorld(-69);
                Helper.mc.player.motionZ = 0.0;
                Helper.mc.player.motionX = 0.0;
                toggle();
                ChatUtils.addChatMessage("FreeCam был включен из за телепорта под бедрок!");
            }
        } else {
            Helper.mc.player.noClip = true;
            Helper.mc.player.onGround = false;
            if (Helper.mc.gameSettings.keyBindJump.isKeyDown()) {
                Helper.mc.player.motionY = speed.getCurrentValue() / 1.5f;
            }
            if (Helper.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Helper.mc.player.motionY = -speed.getCurrentValue() / 1.5f;
            }
            MovementUtils.setSpeed(speed.getCurrentValue());
            Helper.mc.player.capabilities.isFlying = true;
        }
    }
}
