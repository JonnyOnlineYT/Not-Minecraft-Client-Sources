package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;

public class GuiDisconnected extends GuiScreen {
   private final String reason;
   private final ITextComponent message;
   private List<String> multilineMessage;
   private final GuiScreen parentScreen;
   private int textHeight;

   public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, ITextComponent chatComp) {
      this.parentScreen = screen;
      this.reason = I18n.format(reasonLocalizationKey);
      this.message = chatComp;
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
   }

   public void initGui() {
      this.buttonList.clear();
      this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), width - 50);
      this.textHeight = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
      this.buttonList.add(new GuiButton(0, width / 2 - 100, Math.min(height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT, height - 30), I18n.format("gui.toMenu")));
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 0) {
         this.mc.displayGuiScreen(this.parentScreen);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      drawCenteredString(this.fontRendererObj, this.reason, width / 2, height / 2 - this.textHeight / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
      int i = height / 2 - this.textHeight / 2;
      if (this.multilineMessage != null) {
         for(Iterator var5 = this.multilineMessage.iterator(); var5.hasNext(); i += this.fontRendererObj.FONT_HEIGHT) {
            String s = (String)var5.next();
            drawCenteredString(this.fontRendererObj, s, width / 2, i, 16777215);
         }
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
