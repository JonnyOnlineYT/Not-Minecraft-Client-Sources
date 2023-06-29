//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import black.nigger.wildclient.module.Category;
import java.util.Iterator;
import net.minecraft.client.gui.GuiScreen;
import black.nigger.wildclient.Wild;
import black.nigger.wildclient.module.ModuleManager;
import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.module.render.HUD;
import black.nigger.wildclient.module.Module;

public class Panic extends Module
{
    public static HUD hud;
    public static Setting setting;
    public ModuleManager moduleManager;
    
    @Override
    public void onEnable() {
        for (final Module module : Wild.instance.moduleManager.modules) {
            final Module modulew = module;
            if (module.isToggled()) {
                module.toggle();
            }
            module.setKey(0);
        }
        Panic.mc.player.jump();
        Panic.mc.displayGuiScreen((GuiScreen)null);
        super.onEnable();
    }
    
    public Panic() {
        super("SelfDestruct", "disable all active modules.(you will can't enable them after using this before restart)", Category.MISC);
    }
}
