//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class NoSlowdown extends Module
{
    public NoSlowdown() {
        super("noSlow", "slow? no.", Category.MOVEMENT);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        MinecraftForge.EVENT_BUS.register((Object)this);
        Blocks.DIRT.setLightOpacity(10);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        if (NoSlowdown.mc.player.isHandActive() && !NoSlowdown.mc.player.isRiding()) {
            final EntityPlayerSP player = NoSlowdown.mc.player;
            player.moveStrafing *= 5.0f;
            final EntityPlayerSP player2 = NoSlowdown.mc.player;
            player2.moveForward *= 5.0f;
        }
    }
}
