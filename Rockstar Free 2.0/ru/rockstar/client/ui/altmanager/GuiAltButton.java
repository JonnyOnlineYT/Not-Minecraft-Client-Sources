package ru.rockstar.client.ui.altmanager;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import ru.rockstar.api.utils.render.DrawHelper;

public class GuiAltButton extends GuiButton {
   private int opacity;

   public GuiAltButton(int buttonId, int x, int y, String buttonText) {
      this(buttonId, x, y, 200, 20, buttonText);
   }

   public GuiAltButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
      super(buttonId, x, y, widthIn, heightIn, buttonText);
      this.opacity = 40;
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
      if (this.visible) {
         mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
         if (this.hovered) {
            if (this.opacity < 40) {
               ++this.opacity;
            }
         } else if (this.opacity > 22) {
            --this.opacity;
         }

         boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
         Color color = new Color(0, 0, 0, 73);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.blendFunc(770, 771);
         if (flag) {
            DrawHelper.drawNewRect((double)this.xPosition, (double)this.yPosition, (double)(this.xPosition + this.width), (double)(this.yPosition + this.height), (new Color(this.opacity, this.opacity, this.opacity, 150)).getRGB());
            mc.sfui18.drawCenteredString(this.displayString, (float)(this.xPosition + this.width / 2), (float)(this.yPosition + (this.height - 2) / 3), -1);
         } else {
            DrawHelper.drawOutlineRect((float)this.xPosition, (float)this.yPosition, (float)this.width, (float)this.height, color, new Color(255, 255, 255, 10));
            mc.sfui18.drawCenteredString(this.displayString, (float)(this.xPosition + this.width / 2), (float)(this.yPosition + (this.height - 2) / 3), -1);
         }

         this.mouseDragged(mc, mouseX, mouseY);
      }

   }
}
