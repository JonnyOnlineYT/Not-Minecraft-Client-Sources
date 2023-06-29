package me.wintware.client.clickgui.panel.component.impl;

import java.awt.Color;
import me.wintware.client.clickgui.panel.Panel;
import me.wintware.client.clickgui.panel.component.Component;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.utils.animation.AnimationUtil;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;

public final class BoolOptionComponent extends Component {
   private int opacity = 120;
   private int animation = 20;
   float textHoverAnimate = 0.0F;
   float leftRectAnimation = 0.0F;
   double rightRectAnimation = 0.0D;

   public BoolOptionComponent(Setting option, Panel panel, int x, int y, int width, int height) {
      super(panel, x, y, width, height);
      this.option = option;
   }

   public void onDraw(int mouseX, int mouseY) {
      Panel parent = this.getPanel();
      int x = parent.getX() + this.getX();
      int y = parent.getY() + this.getY();
      boolean hovered = this.isMouseOver(mouseX, mouseY);
      if (hovered) {
         if (this.opacity < 200) {
            this.opacity += 5;
         }
      } else if (this.opacity > 120) {
         this.opacity -= 5;
      }

      if (this.option.getValue()) {
         if (this.animation < 30) {
            ++this.animation;
         }
      } else if (this.animation > 20) {
         --this.animation;
      }

      String lol = this.option.getValue() ? "wintware/enabled.png" : "wintware/disabled.png";
      RenderUtil.drawRect((double)((float)x), (double)((float)y), (double)((float)(x + this.getWidth())), (double)((float)(y + this.getHeight())), parent.dragging ? 150994944 : ColorUtils.getColorWithOpacity(BACKGROUND, 255 - this.opacity).getRGB());
      int color = this.option.getValue() ? -1 : (new Color(this.opacity, this.opacity, this.opacity)).getRGB();
      this.textHoverAnimate = AnimationUtil.moveUD(this.textHoverAnimate, hovered ? 2.3F : 2.0F, 1.0E-13F);
      this.leftRectAnimation = AnimationUtil.moveUD(this.leftRectAnimation, this.option.getValue() ? 10.0F : 17.0F, 1.0E-13F);
      this.rightRectAnimation = (double)AnimationUtil.moveUD((float)this.rightRectAnimation, (float)(this.option.getValue() ? 3 : 10), 1.0E-13F);
      RenderUtil.drawSmoothRect((float)((double)x + this.width - 18.0D), (float)(y + 2), (float)((double)x + this.width - 2.0D), (float)((double)y + this.height - 3.0D), (new Color(14, 14, 14)).getRGB());
      RenderUtil.drawSmoothRect((float)((double)x + this.width - (double)this.leftRectAnimation), (float)(y + 3), (float)((double)x + this.width - this.rightRectAnimation), (float)(y + this.getHeight() - 4), this.option.getValue() ? parent.category.getColor() : (new Color(50, 50, 50)).getRGB());
      Minecraft.getMinecraft().smallfontRenderer.drawStringWithShadow(this.option.getName(), (float)x + 4.0F, (float)y + (float)this.getHeight() / this.textHoverAnimate - 3.0F, color);
   }

   public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
      if (this.isMouseOver(mouseX, mouseY)) {
         this.option.setValue(!this.option.getValue());
      }

   }
}
