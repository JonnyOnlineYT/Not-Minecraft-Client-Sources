package me.wintware.client.module.visual;

import java.util.Iterator;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class ArmorHUD extends Module {
   private static RenderItem kappita;
   private static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

   public ArmorHUD() {
      super("ArmorHUD", Category.Visuals);
   }

   @EventTarget
   public void onRender2D(Event2D event) {
      GlStateManager.enableTexture2D();
      ScaledResolution resolution = new ScaledResolution(mc);
      int i = resolution.getScaledWidth() / 2;
      int iteration = 0;
      int y = resolution.getScaledHeight() - 65 - (mc.player.isInWater() ? 10 : 0);
      Iterator var6 = mc.player.inventory.armorInventory.iterator();

      while(var6.hasNext()) {
         ItemStack is = (ItemStack)var6.next();
         ++iteration;
         if (!is.func_190926_b()) {
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            itemRender.zLevel = 200.0F;
            itemRender.renderItemAndEffectIntoGUI(is, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, is, x, y, "");
            itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.func_190916_E() > 1 ? is.func_190916_E() + "" : "";
            mc.fontRenderer.drawStringWithShadow(s, (float)(x + 19 - 2 - mc.fontRenderer.getStringWidth(s)), (float)(y + 20), 16777215);
            int green = Math.abs(is.getMaxDamage() - is.getItemDamage());
            mc.fontRenderer.drawStringWithShadow(green + "", (float)(x + 8 - mc.fontRenderer.getStringWidth(green + "") / 2), (float)(y - -18), -1);
         }
      }

      GlStateManager.enableDepth();
      GlStateManager.disableLighting();
   }
}
