package me.wintware.client.ui.configgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import me.wintware.client.Main;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class DrawConfigManager extends GuiScreen {
   public Color guiColor4;
   String currentScreen = "";
   ScaledResolution sr;
   ArrayList<Config> configs = new ArrayList();
   int x = 250;
   int y;
   int width;
   int center;

   public DrawConfigManager() {
      this.currentScreen = "";
      this.configs = new ArrayList();
      this.x = 250;
      this.configs.addAll(Main.instance.configManager.getConfigs());
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.sr = new ScaledResolution(Minecraft.getMinecraft());
      this.y = 60;
      this.width = this.sr.getScaledWidth() - this.x;
      this.center = this.sr.getScaledWidth() / 2;
      int difference = 16;
      new Color(255 - difference * 2, 255 - difference * 2, 255 - difference * 2, 255);
      this.guiColor4 = new Color(26, 26, 26, 255);
      RenderUtil.relativeRect(0.0F, 0.0F, (float)this.sr.getScaledWidth(), (float)this.sr.getScaledHeight(), (new Color(0, 0, 0, 125)).getRGB());
      RenderUtil.drawSmoothRect((float)this.x, 50.0F, (float)this.width, (float)(this.sr.getScaledHeight() - 60), this.guiColor4.getRGB());

      for(Iterator var6 = this.configs.iterator(); var6.hasNext(); this.y += 20) {
         Config config = (Config)var6.next();
         DrawConfigs drawConfigs = new DrawConfigs(config, this.x, this.y, this.width);
         drawConfigs.drawScreen(mouseX, mouseY, partialTicks, this.guiColor4, new Color(172, 161, 161));
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public boolean isHoveredClient(float mouseX, float mouseY) {
      return mouseX > (float)this.x && mouseX < (float)(this.x / 2 + this.width / 2) && mouseY > 30.0F && mouseY < 50.0F;
   }

   public void initGui() {
      for(Iterator var1 = this.configs.iterator(); var1.hasNext(); this.y += 20) {
         Config config = (Config)var1.next();
         DrawConfigs drawConfigs = new DrawConfigs(config, this.x, this.y, this.width);
         drawConfigs.initGui();
      }

      super.initGui();
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      int y = 60;
      if (mouseButton == 0 && this.isHoveredClient((float)mouseX, (float)mouseY)) {
         this.currentScreen = "Client Configs";
      }

      for(Iterator var5 = this.configs.iterator(); var5.hasNext(); y += 20) {
         Config config = (Config)var5.next();
         DrawConfigs drawConfigs = new DrawConfigs(config, this.x, y, this.width);
         drawConfigs.mouseClicked(mouseX, mouseY, mouseButton);
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }
}
