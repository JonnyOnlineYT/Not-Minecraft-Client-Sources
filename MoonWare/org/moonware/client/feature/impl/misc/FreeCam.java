/* 3eLeHyy#0089 */

package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.event.events.impl.player.EventFullCube;
import org.moonware.client.event.events.impl.player.EventPush;
import org.moonware.client.event.events.impl.player.EventUpdateLiving;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class FreeCam extends Feature {

    public NumberSetting speed;
    public BooleanSetting AntiAction = new BooleanSetting("AntiAction", false, () -> true);
    public BooleanSetting autoDamageDisable = new BooleanSetting("Auto Damage Disable", false, () -> true);
    private float old;
    private double oldX;
    private double oldY;
    private double oldZ;
    public static boolean enable;

    public FreeCam() {
        super("FreeCam", "Позволяет летать в свободной камере", Type.Other);
        speed = new NumberSetting("Flying Speed", 0.5F, 0.1F, 1F, 0.1F, () -> true);
        addSettings(speed, autoDamageDisable, AntiAction);
    }

    @Override
    public void onDisable() {
        enable = false;
        Minecraft.player.capabilities.isFlying = false;
        Minecraft.player.capabilities.setFlySpeed(old);
        Minecraft.player.noClip = false;
        Minecraft.renderGlobal.loadRenderers();
        Minecraft.player.noClip = false;
        Minecraft.player.setPositionAndRotation(oldX, oldY, oldZ, Minecraft.player.rotationYaw, Minecraft.player.rotationPitch);
        Minecraft.world.removeEntityFromWorld(-69);
        Minecraft.player.motionZ = 0;
        Minecraft.player.motionX = 0;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        old = 0.05F;
        enable = true;
        oldX = Minecraft.player.posX;
        oldY = Minecraft.player.posY;
        oldZ = Minecraft.player.posZ;
        Minecraft.player.noClip = true;
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(Minecraft.world, Minecraft.player.getGameProfile());
        fakePlayer.copyLocationAndAnglesFrom(Minecraft.player);
        fakePlayer.posY -= 0;
        fakePlayer.rotationYawHead = Minecraft.player.rotationYawHead;
        Minecraft.world.addEntityToWorld(-69, fakePlayer);
        super.onEnable();
    }

    @EventTarget
    public void onFullCube(EventFullCube event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onPush(EventPush event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onUpdate(EventUpdateLiving event) {

        if (autoDamageDisable.getBoolValue() && Minecraft.player.hurtTime > 0 && MoonWare.featureManager.getFeatureByClass(FreeCam.class).getState()) {
            MoonWare.featureManager.getFeatureByClass(FreeCam.class).toggle();
        }
        Minecraft.player.noClip = true;
        Minecraft.player.onGround = false;
        Minecraft.player.capabilities.setFlySpeed(speed.getNumberValue() / 5);
        Minecraft.player.capabilities.isFlying = true;
    }

    @EventTarget
    public void onPacket(EventSendPacket event) {
        Minecraft.player.setSprinting(false);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (AntiAction.getBoolValue()) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (AntiAction.getBoolValue()) {
            if (event.getPacket() instanceof CPacketPlayer) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof CPacketPlayer.Position) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof CPacketEntityAction) {
                event.setCancelled(true);
            }
        }
    }
}
