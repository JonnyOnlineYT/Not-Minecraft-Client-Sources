//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class NoHurtCum extends Module
{
    public NoHurtCum() {
        super("NoHurtCum", "disables hurt effect", Category.RENDER);
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        NoHurtCum.mc.player.hurtTime = 0;
        NoHurtCum.mc.player.hurtResistantTime = 0;
        NoHurtCum.mc.player.maxHurtResistantTime = 0;
        NoHurtCum.mc.player.maxHurtTime = 0;
    }
}
