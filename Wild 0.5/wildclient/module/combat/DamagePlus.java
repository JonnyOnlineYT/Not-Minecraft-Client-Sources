//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import black.nigger.wildclient.module.EventManager;
import black.nigger.wildclient.module.EventTarget;
import black.nigger.wildclient.module.EventPreAttack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class DamagePlus extends Module
{
    public DamagePlus() {
        super("DamagePlus", "\u0420±\u0420µ\u0421\u201a\u0420°", Category.COMBAT);
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent e) {
        if (this.isToggled() && DamagePlus.mc.player.fallDistance >= 0.3 && DamagePlus.mc.player.fallDistance < 0.6) {
            DamagePlus.mc.player.motionY = 0.0;
        }
    }
    
    @EventTarget
    public void event(final EventPreAttack event) {
        if (!this.isToggled()) {
            return;
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
