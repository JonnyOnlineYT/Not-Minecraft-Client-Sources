//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.render;

import java.awt.Color;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import black.nigger.wildclient.Wild;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.Minecraft;
import black.nigger.wildclient.module.Category;
import net.minecraft.client.gui.FontRenderer;
import black.nigger.wildclient.module.Module;

public class HUD extends Module
{
    public FontRenderer fr;
    
    public HUD() {
        super("HUD", "modules on screen", Category.RENDER);
        this.fr = Minecraft.getMinecraft().fontRenderer;
    }
    
    public void onStart() {
        this.isToggled();
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent event) {
        final int[] counter = { 0 };
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int y = 1;
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            GL11.glPushMatrix();
            GL11.glTranslated(1.0, 1.0, 1.0);
            GL11.glScaled(2.0, 2.0, 2.0);
            this.fr.drawStringWithShadow("WILD", 2.0f, 1.0f, rainbow(counter[0] * 700));
            this.fr.drawStringWithShadow("B5", 27.0f, 1.0f, rainbow(counter[0] * 700));
            GL11.glPopMatrix();
            for (final Module mod : Wild.instance.moduleManager.getModuleList()) {
                if (!mod.getName().equalsIgnoreCase("HUD") && mod.isToggled() && mod.visible) {
                    this.fr.drawStringWithShadow(mod.getName(), (float)(sr.getScaledWidth() - this.fr.getStringWidth(mod.getName()) - 1), (float)y, rainbow(counter[0] * 700));
                    y += this.fr.FONT_HEIGHT;
                }
            }
        }
    }
    
    public static int rainbow(final int delay) {
        double rainbowState = Math.ceil((double)((System.currentTimeMillis() + delay) / 20L));
        rainbowState %= 360.0;
        return Color.getHSBColor((float)(rainbowState / 360.0), 1.0f, 1.0f).getRGB();
    }
}
