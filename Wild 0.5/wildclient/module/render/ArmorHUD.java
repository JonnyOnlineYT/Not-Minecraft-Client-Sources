//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.render;

import net.minecraft.client.Minecraft;
import black.nigger.wildclient.module.EventManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import black.nigger.wildclient.module.ColourHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.ScaledResolution;
import black.nigger.wildclient.module.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import black.nigger.wildclient.module.Category;
import net.minecraft.client.renderer.RenderItem;
import black.nigger.wildclient.module.Module;

public class ArmorHUD extends Module
{
    private static RenderItem itemRender;
    
    public ArmorHUD() {
        super("ArmorHUD", "ArmorHUD", Category.RENDER);
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Text event) {
        GlStateManager.enableTexture2D();
        final ScaledResolution resolution = new ScaledResolution(Wrapper.mc);
        final int i = resolution.getScaledWidth() / 2;
        int iteration = 0;
        final int y = resolution.getScaledHeight() - 55 - (Wrapper.mc.player.isInWater() ? 10 : 0);
        for (final ItemStack is : Wrapper.mc.player.inventory.armorInventory) {
            ++iteration;
            if (is.isEmpty()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            ArmorHUD.itemRender.zLevel = 200.0f;
            ArmorHUD.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            ArmorHUD.itemRender.renderItemOverlayIntoGUI(Wrapper.mc.fontRenderer, is, x, y, "");
            ArmorHUD.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
            Wrapper.mc.fontRenderer.drawStringWithShadow(s, (float)(x + 19 - 2 - Wrapper.mc.fontRenderer.getStringWidth(s)), (float)(y + 9), 16777215);
            final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            Wrapper.mc.fontRenderer.drawStringWithShadow(dmg + "", (float)(x + 8 - Wrapper.mc.fontRenderer.getStringWidth(dmg + "") / 2), (float)(y - 11), ColourHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
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
    
    static {
        ArmorHUD.itemRender = Minecraft.getMinecraft().getRenderItem();
    }
}
