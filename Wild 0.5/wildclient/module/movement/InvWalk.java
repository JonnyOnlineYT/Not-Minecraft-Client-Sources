//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.movement;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class InvWalk extends Module
{
    public InvWalk() {
        super("InvWalk", "InvWalk", Category.MOVEMENT);
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!(InvWalk.mc.currentScreen instanceof GuiContainer)) {
            return;
        }
        if (InvWalk.mc.player.isInWater()) {
            return;
        }
        double speed = 0.05;
        if (!InvWalk.mc.player.onGround) {
            speed /= 4.0;
        }
        this.handleJump();
        this.handleForward(speed);
        if (!InvWalk.mc.player.onGround) {
            speed /= 2.0;
        }
        this.handleBack(speed);
        this.handleLeft(speed);
        this.handleRight(speed);
    }
    
    void moveForward(final double speed) {
        final float direction = this.getDirection();
        final EntityPlayerSP player = InvWalk.mc.player;
        player.motionX -= MathHelper.sin(direction) * speed;
        final EntityPlayerSP player2 = InvWalk.mc.player;
        player2.motionZ += MathHelper.cos(direction) * speed;
    }
    
    void moveBack(final double speed) {
        final float direction = this.getDirection();
        final EntityPlayerSP player = InvWalk.mc.player;
        player.motionX += MathHelper.sin(direction) * speed;
        final EntityPlayerSP player2 = InvWalk.mc.player;
        player2.motionZ -= MathHelper.cos(direction) * speed;
    }
    
    void moveLeft(final double speed) {
        final float direction = this.getDirection();
        final EntityPlayerSP player = InvWalk.mc.player;
        player.motionZ += MathHelper.sin(direction) * speed;
        final EntityPlayerSP player2 = InvWalk.mc.player;
        player2.motionX += MathHelper.cos(direction) * speed;
    }
    
    void moveRight(final double speed) {
        final float direction = this.getDirection();
        final EntityPlayerSP player = InvWalk.mc.player;
        player.motionZ -= MathHelper.sin(direction) * speed;
        final EntityPlayerSP player2 = InvWalk.mc.player;
        player2.motionX -= MathHelper.cos(direction) * speed;
    }
    
    void handleForward(final double speed) {
        InvWalk.mc.player.setSprinting(true);
        if (!Keyboard.isKeyDown(InvWalk.mc.gameSettings.keyBindForward.getKeyCode())) {
            return;
        }
        this.moveForward(speed);
    }
    
    void handleBack(final double speed) {
        if (!Keyboard.isKeyDown(InvWalk.mc.gameSettings.keyBindBack.getKeyCode())) {
            return;
        }
        this.moveBack(speed);
    }
    
    void handleLeft(final double speed) {
        InvWalk.mc.player.setSprinting(true);
        if (!Keyboard.isKeyDown(InvWalk.mc.gameSettings.keyBindLeft.getKeyCode())) {
            return;
        }
        this.moveLeft(speed * 0.8);
    }
    
    void handleRight(final double speed) {
        InvWalk.mc.player.setSprinting(true);
        if (!Keyboard.isKeyDown(InvWalk.mc.gameSettings.keyBindRight.getKeyCode())) {
            return;
        }
        this.moveRight(speed * 0.8);
    }
    
    void handleJump() {
        if (InvWalk.mc.player.onGround && Keyboard.isKeyDown(InvWalk.mc.gameSettings.keyBindJump.getKeyCode())) {
            InvWalk.mc.player.jump();
        }
    }
    
    public float getDirection() {
        float var1 = InvWalk.mc.player.rotationYaw;
        if (InvWalk.mc.player.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (InvWalk.mc.player.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (InvWalk.mc.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (InvWalk.mc.player.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (InvWalk.mc.player.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        var1 *= 0.017453292f;
        return var1;
    }
}
