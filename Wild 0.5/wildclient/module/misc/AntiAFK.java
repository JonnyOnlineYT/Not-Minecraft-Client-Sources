//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class AntiAFK extends Module
{
    public AntiAFK() {
        super("AntiAFK", "jump delay = 0", Category.MISC);
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        if (this.isToggled() && AntiAFK.mc.player.onGround) {
            AntiAFK.mc.player.jump();
            KeyBinding.setKeyBindState(AntiAFK.mc.gameSettings.keyBindSneak.getKeyCode(), true);
            KeyBinding.setKeyBindState(AntiAFK.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
    }
}
