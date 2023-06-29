package me.wintware.client.clickgui.panel.component.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.wintware.client.clickgui.panel.Panel;
import me.wintware.client.clickgui.panel.component.Component;
import me.wintware.client.module.Module;
import me.wintware.client.utils.animation.AnimationUtil;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;

public class VisibleComponent extends Component {
   private int opacity = 120;
   private Module mod;
   float hoveredAnimation = 0.0F;

   public VisibleComponent(Module mod, Panel panel, int x, int y, int width, int height) {
      super(panel, x, y, width, height);
      this.mod = mod;
   }

   public void onDraw(int mouseX, int mouseY) {
      Panel parent = this.getPanel();
      int x = parent.getX() + this.getX();
      int y = parent.getY() + this.getY();
      boolean hovered = this.isMouseOver(mouseX, mouseY);
      int height = this.getHeight();
      int width = this.getWidth();
      if (hovered) {
         if (this.opacity < 200) {
            this.opacity += 5;
         }
      } else if (this.opacity > 120) {
         this.opacity -= 5;
      }

      this.hoveredAnimation = AnimationUtil.moveUD(this.hoveredAnimation, hovered ? 2.3F : 2.0F, 0.01F);
      RenderUtil.drawRect((double)((float)x), (double)((float)y), (double)((float)(x + width)), (double)((float)(y + height)), parent.dragging ? 150994944 : ColorUtils.getColorWithOpacity(BACKGROUND, 255 - this.opacity).getRGB());
      Minecraft.getMinecraft().smallfontRenderer.drawStringWithShadow("Visible: " + ChatFormatting.GRAY + this.mod.visible, (float)x + 2.0F, (float)y + (float)this.getHeight() / this.hoveredAnimation - 2.0F, -1);
      super.onDraw(mouseX, mouseY);
   }

   public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
      if (this.isMouseOver(mouseX, mouseY) && mouseButton == 0) {
         this.mod.visible = !this.mod.visible;
      }

   }
}
