package me.wintware.client.clickgui.panel.component.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.wintware.client.Main;
import me.wintware.client.clickgui.panel.AnimationState;
import me.wintware.client.clickgui.panel.Panel;
import me.wintware.client.clickgui.panel.component.Component;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.module.Module;
import me.wintware.client.utils.animation.AnimationUtil;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public final class ModuleComponent extends Component {
   private static Color BACKGROUND_COLOR = new Color(23, 23, 23);
   public final List components = new ArrayList();
   private final Module module;
   private final ArrayList<Component> children = new ArrayList();
   private int opacity = 120;
   private int childrenHeight;
   private double scissorBoxHeight;
   private AnimationState state;
   private boolean binding;
   private float activeRectAnimate = 0.0F;
   public float animation = 0.0F;
   int onlySettingsY = 0;

   public ModuleComponent(Module module, Panel parent, int x, int y, int width, int height) {
      super(parent, x, y, width, height);
      this.state = AnimationState.STATIC;
      this.module = module;
      int y2 = height;
      int i = false;
      if (Main.instance.setmgr.getSettingsByMod(module) != null) {
         Iterator var9 = Main.instance.setmgr.getSettingsByMod(module).iterator();

         while(var9.hasNext()) {
            Setting s = (Setting)var9.next();
            if (s.isCombo()) {
               this.children.add(new EnumOptionComponent(s, this.getPanel(), x, y + y2, width, height));
               y2 += height + 20;
            }

            if (s.isSlider()) {
               this.children.add(new NumberOptionComponent(s, this.getPanel(), x, y, width, 16));
               y2 += height + 20;
               y2 += height + 20;
            }

            if (s.isCheck()) {
               this.children.add(new BoolOptionComponent(s, this.getPanel(), x, y + y2, width, height));
               y2 += height + 20;
            }
         }
      }

      this.children.add(new VisibleComponent(module, this.getPanel(), x, y, width, height));
      this.calculateChildrenHeight();
   }

   public double getOffset() {
      return this.scissorBoxHeight;
   }

   private void drawChildren(int mouseX, int mouseY) {
      int childY = 15;
      List children = this.children;
      int i = 0;

      for(int componentListSize = children.size(); i < componentListSize; ++i) {
         Component child = (Component)children.get(i);
         if (!child.isHidden()) {
            child.setY(this.getY() + childY);
            child.onDraw(mouseX, mouseY);
            childY += 15;
         }
      }

   }

   private int calculateChildrenHeight() {
      int height = 0;
      List children = this.children;
      int i = 0;

      for(int childrenSize = children.size(); i < childrenSize; ++i) {
         Component component = (Component)children.get(i);
         if (!component.isHidden()) {
            height = (int)((double)height + (double)component.getHeight() + component.getOffset());
         }
      }

      return height;
   }

   public void onDraw(int mouseX, int mouseY) {
      Panel parent = this.getPanel();
      int x = parent.getX() + this.getX();
      int y = parent.getY() + this.getY();
      int height = this.getHeight();
      int width = this.getWidth();
      boolean hovered = this.isMouseOver(mouseX, mouseY);
      this.handleScissorBox();
      this.childrenHeight = this.calculateChildrenHeight();
      if (hovered) {
         if (this.opacity < 200) {
            this.opacity += 5;
         }
      } else if (this.opacity > 120) {
         this.opacity -= 5;
      }

      this.activeRectAnimate = AnimationUtil.moveUD(this.activeRectAnimate, hovered ? 4.0F : 2.0F, 0.001F);
      int opacity = this.opacity;
      RenderUtil.drawRect((double)((float)x), (double)((float)y), (double)((float)(x + width)), (double)((float)((double)((float)(y + height)) + this.getOffset())), ColorUtils.getColorWithOpacity(BACKGROUND_COLOR, 255 - opacity).getRGB());
      int color = this.module.getState() ? parent.category.getColor() : (new Color(opacity, opacity, opacity)).getRGB();
      Minecraft.getMinecraft().fontRenderer.drawCenteredStringWithShadow(this.binding ? "Binding... Key:" + Keyboard.getKeyName(this.module.getKey()) : this.module.getName(), (float)x + 48.0F + this.activeRectAnimate, (float)y + (float)height / 1.5F - 4.0F, color);
      if (this.scissorBoxHeight > 0.0D) {
         if (parent.state != AnimationState.RETRACTING) {
            RenderUtil.prepareScissorBox((float)x, (float)y, (float)(x + width), (float)((double)y + Math.min(this.scissorBoxHeight, parent.scissorBoxHeight) + (double)height));
         }

         this.drawChildren(mouseX, mouseY);
      }

   }

   public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
      if (this.scissorBoxHeight > 0.0D) {
         List componentList = this.children;
         int i = 0;

         for(int componentListSize = componentList.size(); i < componentListSize; ++i) {
            ((Component)componentList.get(i)).onMouseClick(mouseX, mouseY, mouseButton);
         }
      }

      if (this.isMouseOver(mouseX, mouseY) && mouseButton == 2) {
         this.binding = !this.binding;
      }

      if (this.isMouseOver(mouseX, mouseY)) {
         if (mouseButton == 0) {
            this.module.toggle();
         } else if (mouseButton == 1 && !this.children.isEmpty()) {
            if (!(this.scissorBoxHeight > 0.0D) || this.state != AnimationState.EXPANDING && this.state != AnimationState.STATIC) {
               if (this.scissorBoxHeight < (double)this.childrenHeight && (this.state == AnimationState.EXPANDING || this.state == AnimationState.STATIC)) {
                  this.state = AnimationState.EXPANDING;
               }
            } else {
               this.state = AnimationState.RETRACTING;
            }
         }
      }

   }

   public void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
      if (this.scissorBoxHeight > 0.0D) {
         List componentList = this.children;
         int i = 0;

         for(int componentListSize = componentList.size(); i < componentListSize; ++i) {
            ((Component)componentList.get(i)).onMouseRelease(mouseX, mouseY, mouseButton);
         }
      }

   }

   public void onKeyPress(int typedChar, int keyCode) {
      if (this.binding) {
         this.module.setKey(keyCode);
         this.binding = false;
         if (keyCode == 211) {
            this.module.setKey(0);
         } else if (keyCode == 1) {
            this.setBinding(false);
         }
      }

      if (this.scissorBoxHeight > 0.0D) {
         int i = 0;
         List componentList = this.children;

         for(int componentListSize = componentList.size(); i < componentListSize; ++i) {
            ((Component)componentList.get(i)).onKeyPress(typedChar, keyCode);
         }
      }

   }

   public void setBinding(boolean binding) {
      this.binding = binding;
   }

   private void handleScissorBox() {
      int childrenHeight = this.childrenHeight;
      switch(this.state) {
      case EXPANDING:
         if (this.scissorBoxHeight < (double)childrenHeight) {
            this.scissorBoxHeight = AnimationUtil.animate((double)childrenHeight, this.scissorBoxHeight, 0.06D);
         } else if (this.scissorBoxHeight >= (double)childrenHeight) {
            this.state = AnimationState.STATIC;
         }

         this.scissorBoxHeight = this.clamp(this.scissorBoxHeight, (double)childrenHeight);
         break;
      case RETRACTING:
         if (this.scissorBoxHeight > 0.0D) {
            this.scissorBoxHeight = AnimationUtil.animate(0.0D, this.scissorBoxHeight, 0.06D);
         } else if (this.scissorBoxHeight <= 0.0D) {
            this.state = AnimationState.STATIC;
         }

         this.scissorBoxHeight = this.clamp(this.scissorBoxHeight, (double)childrenHeight);
         break;
      case STATIC:
         if (this.scissorBoxHeight > 0.0D && this.scissorBoxHeight != (double)childrenHeight) {
            this.scissorBoxHeight = AnimationUtil.animate((double)childrenHeight, this.scissorBoxHeight, 0.06D);
         }

         this.scissorBoxHeight = this.clamp(this.scissorBoxHeight, (double)childrenHeight);
      }

   }

   private double clamp(double a, double max) {
      return a < 0.0D ? 0.0D : Math.min(a, max);
   }
}
