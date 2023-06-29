//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class LongJump extends Module
{
    private double lastDist;
    public static int stage;
    private double moveSpeed;
    
    public LongJump() {
        super("LongJump", "long jump okay", Category.MOVEMENT);
        this.lastDist = 0.0;
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        if (Speed.isMoving()) {
            if (LongJump.mc.player.moveForward != 0.0f || LongJump.mc.player.moveStrafing != 0.0f) {
                if (LongJump.stage == 0) {
                    this.moveSpeed = 1.0 + Speed.getBaseMoveSpeed() - 0.05;
                }
                else if (LongJump.stage == 1) {
                    LongJump.mc.player.motionY = 0.42;
                    this.moveSpeed *= 2.13;
                }
                else if (LongJump.stage == 2) {
                    final double d = 0.66 * (this.lastDist - Speed.getBaseMoveSpeed());
                    this.moveSpeed = this.lastDist - d;
                }
                else {
                    this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                }
                setMoveSpeed(this.moveSpeed = Math.max(Speed.getBaseMoveSpeed(), this.moveSpeed));
                ++LongJump.stage;
            }
            else if (LongJump.stage > 1) {
                LongJump.stage = 0;
            }
        }
    }
    
    public static void setMoveSpeed(final double d) {
        double d2 = LongJump.mc.player.movementInput.moveForward;
        double d3 = LongJump.mc.player.movementInput.moveStrafe;
        float f = LongJump.mc.player.rotationYaw;
        if (d2 == 0.0 && d3 == 0.0) {
            LongJump.mc.player.motionX = 0.0;
            LongJump.mc.player.motionZ = 0.0;
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
            LongJump.mc.player.motionX = d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 90.0f));
            LongJump.mc.player.motionZ = d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.0f));
        }
    }
}
