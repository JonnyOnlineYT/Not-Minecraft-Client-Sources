package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry {
   private final GuiMultiplayer screen;
   protected final Minecraft mc;
   protected final LanServerInfo serverData;
   private long lastClickTime;

   protected ServerListEntryLanDetected(GuiMultiplayer p_i47141_1_, LanServerInfo p_i47141_2_) {
      this.screen = p_i47141_1_;
      this.serverData = p_i47141_2_;
      this.mc = Minecraft.getMinecraft();
   }

   public void func_192634_a(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_) {
      this.mc.fontRendererObj.drawString(I18n.format("lanServer.title"), (float)(p_192634_2_ + 32 + 3), (float)(p_192634_3_ + 1), 16777215);
      this.mc.fontRendererObj.drawString(this.serverData.getServerMotd(), (float)(p_192634_2_ + 32 + 3), (float)(p_192634_3_ + 12), 8421504);
      if (this.mc.gameSettings.hideServerAddress) {
         this.mc.fontRendererObj.drawString(I18n.format("selectServer.hiddenAddress"), (float)(p_192634_2_ + 32 + 3), (float)(p_192634_3_ + 12 + 11), 3158064);
      } else {
         this.mc.fontRendererObj.drawString(this.serverData.getServerIpPort(), (float)(p_192634_2_ + 32 + 3), (float)(p_192634_3_ + 12 + 11), 3158064);
      }

   }

   public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
      this.screen.selectServer(slotIndex);
      if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
         this.screen.connectToSelected();
      }

      this.lastClickTime = Minecraft.getSystemTime();
      return false;
   }

   public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_) {
   }

   public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
   }

   public LanServerInfo getServerData() {
      return this.serverData;
   }
}
