//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import black.nigger.wildclient.module.EventManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiScreen;
import black.nigger.wildclient.module.Wrapper;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class AutoRespawn extends Module
{
    public AutoRespawn() {
        super("AutoRespawn", "AutoRespawn", Category.MISC);
    }
    
    @SubscribeEvent
    public void onPlayerTickEvent(final TickEvent.PlayerTickEvent playerTickEvent) {
        if (!this.isToggled()) {
            return;
        }
        if (Wrapper.mc.currentScreen instanceof GuiGameOver) {
            Wrapper.mc.player.respawnPlayer();
            Wrapper.mc.displayGuiScreen((GuiScreen)null);
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        super.onDisable();
    }
}
