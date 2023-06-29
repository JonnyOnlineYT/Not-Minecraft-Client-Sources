//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.Wild;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class FlyBypass extends Module
{
    public FlyBypass() {
        super("FlyBypass", "Fly", Category.MISC);
        Wild.instance.settingsManager.rSetting(new Setting("Speed", this, 1.4, 0.01, 5.0, false));
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        if (this.isToggled()) {
            MinecraftForge.EVENT_BUS.register((Object)this);
            FlyBypass.mc.player.capabilities.isFlying = true;
            FlyBypass.mc.player.capabilities.allowFlying = true;
        }
    }
    
    @Override
    public void onDisable() {
        FlyBypass.mc.player.capabilities.isFlying = false;
        FlyBypass.mc.player.capabilities.allowFlying = false;
    }
}
