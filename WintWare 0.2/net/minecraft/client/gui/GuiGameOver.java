package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class GuiGameOver extends GuiScreen {
   private int enableButtonsTimer;
   private final ITextComponent causeOfDeath;

   public GuiGameOver(@Nullable ITextComponent p_i46598_1_) {
      this.causeOfDeath = p_i46598_1_;
   }

   public void initGui() {
      this.buttonList.clear();
      this.enableButtonsTimer = 0;
      if (this.mc.world.getWorldInfo().isHardcoreModeEnabled()) {
         this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 72, I18n.format("deathScreen.spectate")));
         this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96, I18n.format("deathScreen." + (this.mc.isIntegratedServerRunning() ? "deleteWorld" : "leaveServer"))));
      } else {
         this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 72, I18n.format("deathScreen.respawn")));
         this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96, I18n.format("deathScreen.titleScreen")));
         if (this.mc.getSession() == null) {
            ((GuiButton)this.buttonList.get(1)).enabled = false;
         }
      }

      GuiButton guibutton;
      for(Iterator var1 = this.buttonList.iterator(); var1.hasNext(); guibutton.enabled = false) {
         guibutton = (GuiButton)var1.next();
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
      case 0:
         this.mc.player.respawnPlayer();
         this.mc.displayGuiScreen((GuiScreen)null);
         break;
      case 1:
         if (this.mc.world.getWorldInfo().isHardcoreModeEnabled()) {
            this.mc.displayGuiScreen(new GuiMainMenu());
         } else {
            GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm"), "", I18n.format("deathScreen.titleScreen"), I18n.format("deathScreen.respawn"), 0);
            this.mc.displayGuiScreen(guiyesno);
            guiyesno.setButtonDelay(20);
         }
      }

   }

   public void confirmClicked(boolean result, int id) {
      if (result) {
         if (this.mc.world != null) {
            this.mc.world.sendQuittingDisconnectingPacket();
         }

         this.mc.loadWorld((WorldClient)null);
         this.mc.displayGuiScreen(new GuiMainMenu());
      } else {
         this.mc.player.respawnPlayer();
         this.mc.displayGuiScreen((GuiScreen)null);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      boolean flag = this.mc.world.getWorldInfo().isHardcoreModeEnabled();
      this.drawGradientRect(0, 0, width, height, 1615855616, -1602211792);
      GlStateManager.pushMatrix();
      GlStateManager.scale(2.0F, 2.0F, 2.0F);
      drawCenteredString(this.fontRendererObj, I18n.format(flag ? "deathScreen.title.hardcore" : "deathScreen.title"), width / 2 / 2, 30, 16777215);
      GlStateManager.popMatrix();
      if (this.causeOfDeath != null) {
         drawCenteredString(this.fontRendererObj, this.causeOfDeath.getFormattedText(), width / 2, 85, 16777215);
      }

      drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score") + ": " + TextFormatting.YELLOW + this.mc.player.getScore(), width / 2, 100, 16777215);
      if (this.causeOfDeath != null && mouseY > 85 && mouseY < 85 + this.fontRendererObj.FONT_HEIGHT) {
         ITextComponent itextcomponent = this.getClickedComponentAt(mouseX);
         if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null) {
            this.handleComponentHover(itextcomponent, mouseX, mouseY);
         }
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   @Nullable
   public ITextComponent getClickedComponentAt(int p_184870_1_) {
      if (this.causeOfDeath == null) {
         return null;
      } else {
         int i = this.mc.fontRendererObj.getStringWidth(this.causeOfDeath.getFormattedText());
         int j = width / 2 - i / 2;
         int k = width / 2 + i / 2;
         int l = j;
         if (p_184870_1_ >= j && p_184870_1_ <= k) {
            Iterator var6 = this.causeOfDeath.iterator();

            ITextComponent itextcomponent;
            do {
               if (!var6.hasNext()) {
                  return null;
               }

               itextcomponent = (ITextComponent)var6.next();
               l += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(itextcomponent.getUnformattedComponentText(), false));
            } while(l <= p_184870_1_);

            return itextcomponent;
         } else {
            return null;
         }
      }
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void updateScreen() {
      super.updateScreen();
      ++this.enableButtonsTimer;
      GuiButton guibutton;
      if (this.enableButtonsTimer == 20) {
         for(Iterator var1 = this.buttonList.iterator(); var1.hasNext(); guibutton.enabled = true) {
            guibutton = (GuiButton)var1.next();
         }
      }

   }
}
