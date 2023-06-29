//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.movement;

import net.minecraft.potion.Potion;
import java.lang.reflect.Field;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.Wild;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class Speed extends Module
{
    public static double getDirection() {
        float f = Speed.mc.player.rotationYaw;
        if (Speed.mc.player.moveForward < 0.0f) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        if (Speed.mc.player.moveForward < 0.0f) {
            f2 = -0.5f;
        }
        else if (Speed.mc.player.moveForward > 0.0f) {
            f2 = 0.5f;
        }
        if (Speed.mc.player.moveStrafing > 0.0f) {
            f -= 90.0f * f2;
        }
        if (Speed.mc.player.moveStrafing < 0.0f) {
            f += 90.0f * f2;
        }
        return Math.toRadians(f);
    }
    
    public Speed() {
        super("Speed", "faster moving", Category.MOVEMENT);
        Wild.instance.settingsManager.rSetting(new Setting("Speed", this, 1.2, 0.1, 10.0, false));
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        final float movespeed = (float)Wild.instance.settingsManager.getSettingByName(this, "Speed").getValDouble();
        if (Speed.mc.player.onGround && Speed.mc.player.moveForward != 0.0f) {
            Speed.mc.player.connection.sendPacket((Packet)new CPacketPlayer(true));
            Speed.mc.player.jump();
        }
        final float f = Speed.mc.player.rotationYaw;
        final Vec3d vec3d = Vec3d.fromPitchYaw(0.0f, f);
        final Vec3d vec3d2 = Vec3d.fromPitchYaw(0.0f, f + 90.0f);
        double d = 0.0;
        double d2 = 0.0;
        boolean bl = false;
        if (Speed.mc.player.movementInput.forwardKeyDown) {
            d += vec3d.x / 20.0 * movespeed;
            d2 += vec3d.z / 20.0 * movespeed;
            bl = true;
        }
        if (Speed.mc.player.movementInput.backKeyDown) {
            d -= vec3d.x / 20.0 * movespeed;
            d2 -= vec3d.z / 20.0 * movespeed;
            bl = true;
        }
        if (Speed.mc.player.movementInput.rightKeyDown) {
            d += vec3d2.x / 20.0 * movespeed;
            d2 += vec3d2.z / 20.0 * movespeed;
        }
        if (Speed.mc.player.movementInput.leftKeyDown) {
            d -= vec3d2.x / 20.0 * movespeed;
            d2 -= vec3d2.z / 20.0 * movespeed;
        }
        Speed.mc.player.motionX = d;
        Speed.mc.player.motionZ = d2;
    }
    
    public static float getSpeed() {
        return (float)Math.sqrt(Speed.mc.player.motionX * Speed.mc.player.motionX + Speed.mc.player.motionZ * Speed.mc.player.motionZ);
    }
    
    public static void setMoveSpeed(final double d) {
        double d2 = Speed.mc.player.movementInput.moveForward;
        double d3 = Speed.mc.player.movementInput.moveStrafe;
        float f = Speed.mc.player.rotationYaw;
        if (d2 == 0.0 && d3 == 0.0) {
            Speed.mc.player.motionX = 0.0;
            Speed.mc.player.motionZ = 0.0;
        }
        else {
            if (d2 != 0.0) {
                if (d3 > 0.0) {
                    f += ((d2 > 0.0) ? -45 : 45);
                }
                else if (d3 < 0.0) {
                    f += ((d2 > 0.0) ? 45 : -45);
                }
                d3 = 0.0;
                if (d2 > 0.0) {
                    d2 = 1.0;
                }
                else if (d2 < 0.0) {
                    d2 = -1.0;
                }
            }
            Speed.mc.player.motionX = d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 90.0f));
            Speed.mc.player.motionZ = d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.0f));
        }
    }
    
    public static double[] forward(final double d) {
        float f = Minecraft.getMinecraft().player.movementInput.moveForward;
        float f2 = Minecraft.getMinecraft().player.movementInput.moveStrafe;
        float f3 = Minecraft.getMinecraft().player.prevRotationYaw + (Minecraft.getMinecraft().player.rotationYaw - Minecraft.getMinecraft().player.prevRotationYaw) * Minecraft.getMinecraft().getRenderPartialTicks();
        if (f != 0.0f) {
            if (f2 > 0.0f) {
                f3 += ((f > 0.0f) ? -45 : 45);
            }
            else if (f2 < 0.0f) {
                f3 += ((f > 0.0f) ? 45 : -45);
            }
            f2 = 0.0f;
            if (f > 0.0f) {
                f = 1.0f;
            }
            else if (f < 0.0f) {
                f = -1.0f;
            }
        }
        final double d2 = Math.sin(Math.toRadians(f3 + 90.0f));
        final double d3 = Math.cos(Math.toRadians(f3 + 90.0f));
        final double d4 = f * d * d3 + f2 * d * d2;
        final double d5 = f * d * d2 - f2 * d * d3;
        return new double[] { d4, d5 };
    }
    
    public static void setSpeed(final EntityLivingBase entityLivingBase, final double d) {
        final double[] arrd = forward(d);
        entityLivingBase.motionX = arrd[0];
        entityLivingBase.motionZ = arrd[1];
    }
    
    public static boolean isMoving() {
        return Speed.mc.player != null && (Speed.mc.player.movementInput.moveForward != 0.0f || Speed.mc.player.movementInput.moveStrafe != 0.0f);
    }
    
    public static void setTimerSpeed(final float f) {
        final Class<Minecraft> class_ = Minecraft.class;
        try {
            class_.getDeclaredField("timer");
        }
        catch (NoSuchFieldException ex) {}
        Minecraft.getMinecraft();
        final Object object = null;
        final Class<?> class_2 = object.getClass();
        try {
            final Field field2 = class_2.getDeclaredField("timerSpeed");
        }
        catch (NoSuchFieldException ex2) {}
    }
    
    public static double getBaseMoveSpeed() {
        double d = 0.2873;
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.isPotionActive(Potion.getPotionById(1))) {
            final int n = Minecraft.getMinecraft().player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            d *= 1.0 + 0.2 * (n + 1);
        }
        return d;
    }
}
