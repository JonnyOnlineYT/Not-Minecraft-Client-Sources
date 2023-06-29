// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import net.minecraftforge.common.MinecraftForge;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class Timer extends Module
{
    public Timer() {
        super("timer", "changes player timer", Category.MISC);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
}
