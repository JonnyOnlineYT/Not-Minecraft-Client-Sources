//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.render;

import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class FullBright extends Module
{
    public float oldGamma;
    
    public FullBright() {
        super("FullBright", "all bright", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.oldGamma = FullBright.mc.gameSettings.gammaSetting;
        FullBright.mc.gameSettings.gammaSetting = 10.0f;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        FullBright.mc.gameSettings.gammaSetting = this.oldGamma;
    }
}
