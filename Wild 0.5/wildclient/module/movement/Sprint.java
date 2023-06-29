//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class Sprint extends Module
{
    public Sprint() {
        super("Sprint", "i like sprinting", Category.MOVEMENT);
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent e) {
        KeyBinding.setKeyBindState(Sprint.mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        KeyBinding.setKeyBindState(Sprint.mc.gameSettings.keyBindSprint.getKeyCode(), false);
    }
}
