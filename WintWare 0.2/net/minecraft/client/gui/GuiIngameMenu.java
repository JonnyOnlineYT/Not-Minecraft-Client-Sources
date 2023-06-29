package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;

public class GuiIngameMenu extends GuiScreen {
   private int saveStep;
   private int visibleTime;

   public void initGui() {
      this.saveStep = 0;
      this.buttonList.clear();
      int i = true;
      int j = true;
      this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + -16, I18n.format("menu.returnToMenu")));
      if (!this.mc.isIntegratedServerRunning()) {
         ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
      }

      this.buttonList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 + -16, I18n.format("menu.returnToGame")));
      this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + -16, 98, 20, I18n.format("menu.options")));
      GuiButton guibutton = this.addButton(new GuiButton(7, width / 2 + 2, height / 4 + 96 + -16, 98, 20, I18n.format("menu.shareToLan")));
      guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
      this.buttonList.add(new GuiButton(5, width / 2 - 100, height / 4 + 48 + -16, 98, 20, I18n.format("gui.advancements")));
      this.buttonList.add(new GuiButton(6, width / 2 + 2, height / 4 + 48 + -16, 98, 20, I18n.format("gui.stats")));
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
      case 0:
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
         break;
      case 1:
         boolean flag = this.mc.isIntegratedServerRunning();
         boolean flag1 = this.mc.isConnectedToRealms();
         button.enabled = false;
         this.mc.world.sendQuittingDisconnectingPacket();
         this.mc.loadWorld((WorldClient)null);
         if (flag) {
            this.mc.displayGuiScreen(new GuiMainMenu());
         } else if (flag1) {
            RealmsBridge realmsbridge = new RealmsBridge();
            realmsbridge.switchToRealms(new GuiMainMenu());
         } else {
            this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
         }
      case 2:
      case 3:
      default:
         break;
      case 4:
         this.mc.displayGuiScreen((GuiScreen)null);
         this.mc.setIngameFocus();
         break;
      case 5:
         this.mc.displayGuiScreen(new GuiScreenAdvancements(this.mc.player.connection.func_191982_f()));
         break;
      case 6:
         this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
         break;
      case 7:
         this.mc.displayGuiScreen(new GuiShareToLan(this));
      }

   }

   public void updateScreen() {
      super.updateScreen();
      ++this.visibleTime;
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      drawCenteredString(this.fontRendererObj, I18n.format("menu.game"), width / 2, 40, 16777215);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
