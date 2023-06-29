package me.wintware.client.utils.font;

import java.awt.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public final class FontRendererHook extends net.minecraft.client.gui.FontRenderer {
   private final FontRenderer fontRenderer;

   public FontRendererHook(Font font, boolean antiAliasing, boolean fractionalMetrics) {
      super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
      this.fontRenderer = new FontRenderer(font, antiAliasing, fractionalMetrics);
   }

   protected int renderString(String text, float x, float y, int color, boolean dropShadow) {
      if (text == null) {
         return 0;
      } else {
         if (this.bidiFlag) {
            text = this.bidiReorder(text);
         }

         if ((color & -67108864) == 0) {
            color |= -16777216;
         }

         if (dropShadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
         }

         this.red = (float)(color >> 16 & 255) / 255.0F;
         this.blue = (float)(color >> 8 & 255) / 255.0F;
         this.green = (float)(color & 255) / 255.0F;
         this.alpha = (float)(color >> 24 & 255) / 255.0F;
         GlStateManager.color(this.red, this.blue, this.green, this.alpha);
         this.posX = x;
         this.posY = y;
         this.fontRenderer.drawString(text, (double)x, (double)y, color, dropShadow);
         return (int)this.posX;
      }
   }

   public int getStringWidth(String text) {
      return this.fontRenderer.getStringWidth(text);
   }
}
