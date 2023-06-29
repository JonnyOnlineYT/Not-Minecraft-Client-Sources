package ru.rockstar.client.features.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.*;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import org.lwjgl.Sys;

public class FreeCam extends Feature {

    public NumberSetting speed;
    public BooleanSetting AntiAction = new BooleanSetting("ReallyWorld", false, () -> true);
    public BooleanSetting clipOnDisable = new BooleanSetting("Clip On Disable", false, () -> true);
    public BooleanSetting autoDamageDisable = new BooleanSetting("Auto Damage Disable", false, () -> true);

    double x, y, z;
    double yaw;

    public FreeCam() {
        super("FreeCam", "Позволяет летать в свободной камере", 0, Category.MISC);
        speed = new NumberSetting("Flying Speed", 0.5F, 0.1F, 1F, 0.1F, () -> true);
        addSettings(speed, autoDamageDisable, AntiAction, clipOnDisable);
    }


    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (AntiAction.getBoolValue()) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                event.setCancelled(true);
            }
        }
    }

    public void onEnable() {
        super.onEnable();
        x = mc.player.posX;
        y = mc.player.posY;
        z = mc.player.posZ;
        EntityOtherPlayerMP ent = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        ent.inventory = mc.player.inventory;
        ent.inventoryContainer = mc.player.inventoryContainer;
        ent.setHealth(mc.player.getHealth());
        ent.setPositionAndRotation(this.x, mc.player.getEntityBoundingBox().minY, this.z, mc.player.rotationYaw, mc.player.rotationPitch);
        ent.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-1, ent);
        yaw = ent.rotationYaw * 0.017453292;
    }
    
    @EventTarget
    public void ebatkopat(Event2D render) {
    	ScaledResolution sr = new ScaledResolution(mc);
    	Minecraft.getMinecraft().neverlose500_18.drawStringWithShadow("Y: " + (int) (mc.player.posY - y), sr.getScaledWidth() / 2 - 7, sr.getScaledHeight() / 2 + 7, -1);
    }

    @EventTarget
    public void onPreMotion(EventUpdate e) {
        if (autoDamageDisable.getBoolValue() && mc.player.hurtTime > 0 && Main.instance.featureDirector.getFeatureByClass(FreeCam.class).isToggled()) {
            Main.instance.featureDirector.getFeatureByClass(FreeCam.class).toggle();
        }
        mc.player.motionY = 0;
        if (mc.gameSettings.keyBindJump.pressed) {
            mc.player.motionY = speed.getNumberValue();
        }
        if (mc.gameSettings.keyBindSneak.pressed) {
            mc.player.motionY = -speed.getNumberValue();
        }
        mc.player.noClip = true;
        MovementHelper.setSpeed(speed.getNumberValue());
    }
    @Override
    public void onDisable() {
        super.onDisable();
        if (clipOnDisable.getBoolValue()) {
        	yaw = 0;
        	mc.getConnection().sendPacket((Packet)new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        	mc.player.setPositionAndUpdate(mc.player.posX + Math.sin(yaw), mc.player.posY , mc.player.posZ - Math.cos(yaw));
        } else {
        	mc.player.setPosition(x, y, z);
            mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.01, mc.player.posZ, mc.player.onGround));
        }
        mc.player.capabilities.isFlying = false;
        mc.player.noClip = false;
        mc.world.removeEntityFromWorld(-1);
    }
}