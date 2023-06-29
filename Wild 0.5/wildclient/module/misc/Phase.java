//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class Phase extends Module
{
    public Phase() {
        super("Phase", "Allow to to phase", Category.MISC);
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        Phase.mc.player.noClip = true;
        Phase.mc.player.fallDistance = 0.0f;
        Phase.mc.player.onGround = false;
        Phase.mc.player.capabilities.isFlying = false;
        Phase.mc.player.motionX = 0.0;
        Phase.mc.player.motionY = 0.0;
        Phase.mc.player.motionZ = 0.0;
        final float speed = 1.0f;
        Phase.mc.player.jumpMovementFactor = speed;
        if (Phase.mc.gameSettings.keyBindJump.isPressed()) {
            final EntityPlayerSP player = Phase.mc.player;
            player.motionY += speed;
        }
        if (Phase.mc.gameSettings.keyBindSneak.isPressed()) {
            final EntityPlayerSP player2 = Phase.mc.player;
            player2.motionY -= speed;
        }
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
}
