package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;

public class GuiDownloadTerrain extends GuiScreen {
   public void initGui() {
      this.buttonList.clear();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawBackground(0);
      drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingTerrain"), width / 2, height / 2 - 50, 16777215);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
