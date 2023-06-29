//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import java.util.Random;
import black.nigger.wildclient.module.Module;

public class Derp extends Module
{
    private final Random e;
    
    public Derp() {
        super("Derp", "rotating like bitch", Category.PLAYER);
        this.e = new Random();
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        final float f = this.e.nextFloat() * 360.0f;
        final float cfr_ignored_0 = this.e.nextFloat() * 180.0f - 90.0f;
        Derp.mc.player.rotationYawHead = f;
        Derp.mc.player.renderYawOffset = f;
    }
}
