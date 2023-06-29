package me.wintware.client.ui.configgui;

import java.awt.Color;
import java.io.IOException;
import me.wintware.client.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class DrawConfigs {
   ScaledResolution sr;
   Config config;
   int x;
   int y;
   int width;
   int height;
   int center;

   public DrawConfigs(Config config, int x, int y, int width) {
      this.config = config;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = 20;
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks, Color elementColor, Color textColor) {
      this.sr = new ScaledResolution(Minecraft.getMinecraft());
      this.center = this.sr.getScaledWidth() / 2;
      Gui.drawRect((double)this.x, (double)this.y, (double)this.width, (double)(this.y + this.height), elementColor.getRGB());
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.config.getName(), (float)(this.center - Minecraft.getMinecraft().fontRenderer.getStringWidth(this.config.getName()) / 2), (float)(this.y + Minecraft.getMinecraft().fontRenderer.getStringHeight(this.config.getName())), textColor.getRGB());
   }

   public void initGui() {
   }

   public boolean isHovered(float mouseX, float mouseY) {
      return mouseX > (float)this.x && mouseX < (float)this.width && mouseY > (float)this.y && mouseY < (float)(this.y + this.height);
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (mouseButton == 0 && this.isHovered((float)mouseX, (float)mouseY)) {
         this.config.loadConfig();
         ChatUtils.addChatMessage(this.config.getName() + " config loaded");
      }

   }
}
