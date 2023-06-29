package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class GuiCustomizeSkin extends GuiScreen {
   private final GuiScreen parentScreen;
   private String title;

   public GuiCustomizeSkin(GuiScreen parentScreenIn) {
      this.parentScreen = parentScreenIn;
   }

   public void initGui() {
      int i = 0;
      this.title = I18n.format("options.skinCustomisation.title");
      EnumPlayerModelParts[] var2 = EnumPlayerModelParts.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumPlayerModelParts enumplayermodelparts = var2[var4];
         this.buttonList.add(new GuiCustomizeSkin.ButtonPart(enumplayermodelparts.getPartId(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), 150, 20, enumplayermodelparts));
         ++i;
      }

      this.buttonList.add(new GuiOptionButton(199, width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), GameSettings.Options.MAIN_HAND, this.mc.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND)));
      ++i;
      if (i % 2 == 1) {
         ++i;
      }

      this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 24 * (i >> 1), I18n.format("gui.done")));
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (keyCode == 1) {
         this.mc.gameSettings.saveOptions();
      }

      super.keyTyped(typedChar, keyCode);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.enabled) {
         if (button.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (button.id == 199) {
            this.mc.gameSettings.setOptionValue(GameSettings.Options.MAIN_HAND, 1);
            button.displayString = this.mc.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND);
            this.mc.gameSettings.sendSettingsToServer();
         } else if (button instanceof GuiCustomizeSkin.ButtonPart) {
            EnumPlayerModelParts enumplayermodelparts = ((GuiCustomizeSkin.ButtonPart)button).playerModelParts;
            this.mc.gameSettings.switchModelPartEnabled(enumplayermodelparts);
            button.displayString = this.getMessage(enumplayermodelparts);
         }
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      drawCenteredString(this.fontRendererObj, this.title, width / 2, 20, 16777215);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   private String getMessage(EnumPlayerModelParts playerModelParts) {
      String s;
      if (this.mc.gameSettings.getModelParts().contains(playerModelParts)) {
         s = I18n.format("options.on");
      } else {
         s = I18n.format("options.off");
      }

      return playerModelParts.getName().getFormattedText() + ": " + s;
   }

   class ButtonPart extends GuiButton {
      private final EnumPlayerModelParts playerModelParts;

      private ButtonPart(int p_i45514_2_, int p_i45514_3_, int p_i45514_4_, int p_i45514_5_, int p_i45514_6_, EnumPlayerModelParts playerModelParts) {
         super(p_i45514_2_, p_i45514_3_, p_i45514_4_, p_i45514_5_, p_i45514_6_, GuiCustomizeSkin.this.getMessage(playerModelParts));
         this.playerModelParts = playerModelParts;
      }

      // $FF: synthetic method
      ButtonPart(int x1, int x2, int x3, int x4, int x5, EnumPlayerModelParts x6, Object x7) {
         this(x1, x2, x3, x4, x5, x6);
      }
   }
}
