//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import black.nigger.wildclient.module.EventManager;
import black.nigger.wildclient.module.EventTarget;
import black.nigger.wildclient.module.Wrapper;
import black.nigger.wildclient.module.EventPreMotionUpdates;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class NoPush extends Module
{
    public NoPush() {
        super("NoPush", "NoPush", Category.COMBAT);
    }
    
    @EventTarget
    public void onEvent(final EventPreMotionUpdates event) {
        if (!this.isToggled()) {
            return;
        }
        Wrapper.getPlayer().entityCollisionReduction = 1.0f;
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
