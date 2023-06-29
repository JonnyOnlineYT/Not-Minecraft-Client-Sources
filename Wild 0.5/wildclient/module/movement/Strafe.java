//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class Strafe extends Module
{
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static void strafe(final float f) {
        if (!isMoving()) {
            return;
        }
        final double d = getDirection();
        Strafe.mc.player.motionX = -Math.sin(d) * f;
        Strafe.mc.player.motionZ = Math.cos(d) * f;
    }
    
    public Strafe() {
        super("Strafe", "strafing", Category.MOVEMENT);
    }
    
    public static float getSpeed() {
        return (float)Math.sqrt(Strafe.mc.player.motionX * Strafe.mc.player.motionX + Strafe.mc.player.motionZ * Strafe.mc.player.motionZ);
    }
    
    public static double getDirection() {
        float f = Strafe.mc.player.rotationYaw;
        if (Strafe.mc.player.moveForward < 0.0f) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        if (Strafe.mc.player.moveForward < 0.0f) {
            f2 = -0.5f;
        }
        else if (Strafe.mc.player.moveForward > 0.0f) {
            f2 = 0.5f;
        }
        if (Strafe.mc.player.moveStrafing > 0.0f) {
            f -= 90.0f * f2;
        }
        if (Strafe.mc.player.moveStrafing < 0.0f) {
            f += 90.0f * f2;
        }
        return Math.toRadians(f);
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        strafe();
    }
    
    public static boolean isMoving() {
        return Strafe.mc.player != null && (Strafe.mc.player.movementInput.moveForward != 0.0f || Strafe.mc.player.movementInput.moveStrafe != 0.0f);
    }
}
