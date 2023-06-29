package me.wintware.client.clickgui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import me.wintware.client.clickgui.panel.Panel;
import me.wintware.client.module.Category;
import me.wintware.client.utils.animation.Translate;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public final class ClickGuiScreen extends GuiScreen {
   private static ClickGuiScreen INSTANCE;
   private final List panels = Lists.newArrayList();
   public Translate translate;
   public static double scaling;
   private int heightY = 0;

   public ClickGuiScreen() {
      Category[] categories = Category.values();
      scaling = 0.0D;

      for(int i = categories.length - 1; i >= 0; --i) {
         this.panels.add(new Panel(categories[i], 5 + 110 * i, 10));
         this.translate = new Translate(0.0F, 0.0F);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      int i = 0;
      new ScaledResolution(this.mc);
      this.drawDefaultBackground();

      for(int panelsSize = this.panels.size(); i < panelsSize; ++i) {
         ((Panel)this.panels.get(i)).onDraw(mouseX, mouseY);
         this.updateMouseWheel();
      }

   }

   public void updateMouseWheel() {
      int i = 0;
      int scrollWheel = Mouse.getDWheel();

      for(int panelsSize = this.panels.size(); i < panelsSize; ++i) {
         if (scrollWheel < 0) {
            ((Panel)this.panels.get(i)).setY(((Panel)this.panels.get(i)).getY() - 15);
         } else if (scrollWheel > 0) {
            ((Panel)this.panels.get(i)).setY(((Panel)this.panels.get(i)).getY() + 15);
         }
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      int i = 0;
      new ScaledResolution(this.mc);

      for(int panelsSize = this.panels.size(); i < panelsSize; ++i) {
         ((Panel)this.panels.get(i)).onMouseClick(mouseX, mouseY, mouseButton);
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      int i = 0;

      for(int panelsSize = this.panels.size(); i < panelsSize; ++i) {
         ((Panel)this.panels.get(i)).onMouseRelease(mouseX, mouseY, state);
      }

   }

   public void onGuiClosed() {
      if (this.mc.entityRenderer.isShaderActive()) {
         this.mc.entityRenderer.theShaderGroup = null;
      }

   }

   public void initGui() {
      if (!this.mc.gameSettings.ofFastRender && !this.mc.entityRenderer.isShaderActive()) {
         this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      int i = 0;

      for(int panelsSize = this.panels.size(); i < panelsSize; ++i) {
         ((Panel)this.panels.get(i)).onKeyPress(typedChar, keyCode);
      }

      super.keyTyped(typedChar, keyCode);
   }

   public static ClickGuiScreen getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new ClickGuiScreen();
      }

      return INSTANCE;
   }
}
