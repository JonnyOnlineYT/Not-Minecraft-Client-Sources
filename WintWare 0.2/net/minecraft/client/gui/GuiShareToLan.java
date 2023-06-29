package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class GuiShareToLan extends GuiScreen {
   private final GuiScreen lastScreen;
   private GuiButton allowCheatsButton;
   private GuiButton gameModeButton;
   private String gameMode = "survival";
   private boolean allowCheats;

   public GuiShareToLan(GuiScreen p_i1055_1_) {
      this.lastScreen = p_i1055_1_;
   }

   public void initGui() {
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(101, width / 2 - 155, height - 28, 150, 20, I18n.format("lanServer.start")));
      this.buttonList.add(new GuiButton(102, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
      this.gameModeButton = this.addButton(new GuiButton(104, width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.gameMode")));
      this.allowCheatsButton = this.addButton(new GuiButton(103, width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.allowCommands")));
      this.updateDisplayNames();
   }

   private void updateDisplayNames() {
      this.gameModeButton.displayString = I18n.format("selectWorld.gameMode") + ": " + I18n.format("selectWorld.gameMode." + this.gameMode);
      this.allowCheatsButton.displayString = I18n.format("selectWorld.allowCommands") + " ";
      if (this.allowCheats) {
         this.allowCheatsButton.displayString = this.allowCheatsButton.displayString + I18n.format("options.on");
      } else {
         this.allowCheatsButton.displayString = this.allowCheatsButton.displayString + I18n.format("options.off");
      }

   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 102) {
         this.mc.displayGuiScreen(this.lastScreen);
      } else if (button.id == 104) {
         if ("spectator".equals(this.gameMode)) {
            this.gameMode = "creative";
         } else if ("creative".equals(this.gameMode)) {
            this.gameMode = "adventure";
         } else if ("adventure".equals(this.gameMode)) {
            this.gameMode = "survival";
         } else {
            this.gameMode = "spectator";
         }

         this.updateDisplayNames();
      } else if (button.id == 103) {
         this.allowCheats = !this.allowCheats;
         this.updateDisplayNames();
      } else if (button.id == 101) {
         this.mc.displayGuiScreen((GuiScreen)null);
         String s = this.mc.getIntegratedServer().shareToLAN(GameType.getByName(this.gameMode), this.allowCheats);
         Object itextcomponent;
         if (s != null) {
            itextcomponent = new TextComponentTranslation("commands.publish.started", new Object[]{s});
         } else {
            itextcomponent = new TextComponentString("commands.publish.failed");
         }

         this.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)itextcomponent);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      drawCenteredString(this.fontRendererObj, I18n.format("lanServer.title"), width / 2, 50, 16777215);
      drawCenteredString(this.fontRendererObj, I18n.format("lanServer.otherPlayers"), width / 2, 82, 16777215);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
