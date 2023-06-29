//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import black.nigger.wildclient.module.ReflectionFields;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import black.nigger.wildclient.module.EventPacketRecieve;
import black.nigger.wildclient.module.EventManager;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class AutoFish extends Module
{
    int delay;
    
    public AutoFish() {
        super("AutoFish", "AutoFish", Category.MISC);
        this.delay = 0;
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
    
    @SubscribeEvent
    public void onPacketReceive(final EventPacketRecieve event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (packet.getSound().equals(SoundEvents.ENTITY_BOBBER_SPLASH)) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            ReflectionFields.rightClickMouse();
                            Thread.sleep(300L);
                            ReflectionFields.rightClickMouse();
                        }
                        catch (Exception ex) {}
                    }
                }.start();
            }
        }
    }
}
