//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class NoJumpDelay extends Module
{
    public NoJumpDelay() {
        super("NoJumpDelay", "jump delay = 0", Category.MISC);
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        if (this.isToggled() && NoJumpDelay.mc.gameSettings.keyBindJump.isPressed() && NoJumpDelay.mc.player.onGround) {
            NoJumpDelay.mc.player.jump();
        }
    }
}
