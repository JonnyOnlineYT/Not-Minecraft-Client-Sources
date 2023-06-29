package ru.rockstar.client.features.impl.combat;

import java.util.Objects;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventReceivePacket;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;



public class Velocity extends Feature
{
    public static BooleanSetting cancelOtherDamage;
    public static ListSetting velocityMode;
    public static NumberSetting verticalPer;
    public static NumberSetting horizontalPer;
    
    public Velocity() {
        super("Velocity", "\u0423\u043c\u0435\u043d\u044c\u0448\u0430\u0435\u0442 \u043a\u043d\u043e\u043a\u0431\u044d\u043a \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435", 0, Category.COMBAT);
        Velocity.velocityMode = new ListSetting("Velocity Mode", "Packet", () -> true, new String[] { "Packet", "Custom Motion", "Matrix", "AAC", "Strafe Cancel" });
        Velocity.cancelOtherDamage = new BooleanSetting("Cancel Other Damage", true, () -> true);
        Velocity.verticalPer = new NumberSetting("Vertical Percentage", 0.0f, 0.0f, 100.0f, 1.0f, () -> Velocity.velocityMode.currentMode.equals("Custom Motion"));
        Velocity.horizontalPer = new NumberSetting("Horizontal Percentage", 0.0f, 0.0f, 100.0f, 1.0f, () -> Velocity.velocityMode.currentMode.equals("Custom Motion"));
        this.addSettings(Velocity.velocityMode, Velocity.cancelOtherDamage, Velocity.verticalPer, Velocity.horizontalPer);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (Velocity.velocityMode.currentMode.equals("Custom Motion")) {
            final double hori = Velocity.horizontalPer.getNumberValue();
            final double vert = Velocity.verticalPer.getNumberValue();
            this.setSuffix("H: " + hori + "% V: " + vert + "%", true);
        }
        else {
            this.setSuffix(Velocity.velocityMode.currentMode, true);
        }
        if (Velocity.velocityMode.currentMode.equalsIgnoreCase("AAC")) {
            if (Velocity.mc.player.hurtTime > 1) {
            	mc.gameSettings.keyBindSneak.pressed = true;
            } else if (Velocity.mc.player.hurtTime == 1){
            	mc.gameSettings.keyBindSneak.pressed = false;
            }
        }
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        final String mode = Velocity.velocityMode.getOptions();
        if (Velocity.cancelOtherDamage.getBoolValue() && Velocity.mc.player.hurtTime > 0 && event.getPacket() instanceof SPacketEntityVelocity && !Velocity.mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && (Velocity.mc.player.isPotionActive(MobEffects.POISON) || Velocity.mc.player.isPotionActive(MobEffects.WITHER) || Velocity.mc.player.isBurning())) {
            event.setCancelled(true);
        }
        if (mode.equalsIgnoreCase("Packet")) {
            if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) {
                final SPacketEntityVelocity velocity = (SPacketEntityVelocity)event.getPacket();
                if (velocity.getEntityID() == Velocity.mc.player.getEntityId()) {
                    event.setCancelled(true);
                }
            }
        }
        else if (mode.equalsIgnoreCase("Strafe Cancel")) {
            if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) {
                if (((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                    event.setCancelled(true);
                }
                if (Velocity.mc.player.hurtTime > 0) {
                    MovementHelper.strafe(0.20f);
                }
            }
        }
        else if (mode.equalsIgnoreCase("Custom Motion")) {
            final double hori = Velocity.horizontalPer.getNumberValue();
            final double vert = Velocity.verticalPer.getNumberValue();
            if (((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                if (event.getPacket() instanceof SPacketEntityVelocity) {
                    final Entity entity = Objects.requireNonNull(Velocity.mc.getConnection()).clientWorldController.getEntityByID(((SPacketEntityVelocity)event.getPacket()).getEntityID());
                    if (entity instanceof EntityPlayerSP) {
                        if (hori == 0.0 && vert == 0.0) {
                            event.setCancelled(true);
                            return;
                        }
                        if (hori != 100.0) {
                            ((SPacketEntityVelocity)event.getPacket()).motionX = (int)(((SPacketEntityVelocity)event.getPacket()).motionX / 100 * hori);
                            ((SPacketEntityVelocity)event.getPacket()).motionZ = (int)(((SPacketEntityVelocity)event.getPacket()).motionZ / 100 * hori);
                        }
                        if (vert != 100.0) {
                            ((SPacketEntityVelocity)event.getPacket()).motionY = (int)(((SPacketEntityVelocity)event.getPacket()).motionY / 100 * vert);
                        }
                    }
                }
                if (event.getPacket() instanceof SPacketExplosion) {
                    if (hori == 0.0 && vert == 0.0) {
                        event.setCancelled(true);
                        return;
                    }
                    if (hori != 100.0) {
                        ((SPacketExplosion)event.getPacket()).motionX = (float)(int)(((SPacketExplosion)event.getPacket()).motionX / 100.0f * hori);
                        ((SPacketExplosion)event.getPacket()).motionZ = (float)(int)(((SPacketExplosion)event.getPacket()).motionZ / 100.0f * hori);
                    }
                    if (vert != 100.0) {
                        ((SPacketExplosion)event.getPacket()).motionY = (float)(int)(((SPacketExplosion)event.getPacket()).motionY / 100.0f * vert);
                    }
                }
            }
        }
    }
}