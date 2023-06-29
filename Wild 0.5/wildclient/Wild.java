//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import black.nigger.wildclient.module.Module;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import black.nigger.wildclient.clickgui.ClickGui;
import black.nigger.wildclient.settings.SettingsManager;
import black.nigger.wildclient.module.ModuleManager;

public class Wild
{
    public static Wild instance;
    public ModuleManager moduleManager;
    public SettingsManager settingsManager;
    public ClickGui clickGui;
    
    public void init() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.settingsManager = new SettingsManager();
        this.moduleManager = new ModuleManager();
        this.clickGui = new ClickGui();
    }
    
    @SubscribeEvent
    public void key(final InputEvent.KeyInputEvent e) {
        if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null) {
            return;
        }
        try {
            if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {
                final int keyCode = Keyboard.getEventKey();
                if (keyCode <= 0) {
                    return;
                }
                for (final Module m : this.moduleManager.modules) {
                    if (m.getKey() == keyCode && keyCode > 0) {
                        m.toggle();
                    }
                }
            }
        }
        catch (Exception q) {
            q.printStackTrace();
        }
    }
}
