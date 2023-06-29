//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.render;

import net.minecraft.client.gui.GuiScreen;
import black.nigger.wildclient.Wild;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class ClickGUI extends Module
{
    public ClickGUI() {
        super("ClickGUI", "menu.skeet", Category.RENDER);
        this.setKey(54);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        ClickGUI.mc.displayGuiScreen((GuiScreen)Wild.instance.clickGui);
        this.setToggled(false);
    }
}
