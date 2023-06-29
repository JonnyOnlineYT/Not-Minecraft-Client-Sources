package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage extends GuiScreen {
   protected GuiScreen parentScreen;
   private GuiLanguage.List list;
   private final GameSettings game_settings_3;
   private final LanguageManager languageManager;
   private GuiOptionButton forceUnicodeFontBtn;
   private GuiOptionButton confirmSettingsBtn;

   public GuiLanguage(GuiScreen screen, GameSettings gameSettingsObj, LanguageManager manager) {
      this.parentScreen = screen;
      this.game_settings_3 = gameSettingsObj;
      this.languageManager = manager;
   }

   public void initGui() {
      this.forceUnicodeFontBtn = (GuiOptionButton)this.addButton(new GuiOptionButton(100, width / 2 - 155, height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
      this.confirmSettingsBtn = (GuiOptionButton)this.addButton(new GuiOptionButton(6, width / 2 - 155 + 160, height - 38, I18n.format("gui.done")));
      this.list = new GuiLanguage.List(this.mc);
      this.list.registerScrollButtons(7, 8);
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.list.handleMouseInput();
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.enabled) {
         switch(button.id) {
         case 5:
            break;
         case 6:
            this.mc.displayGuiScreen(this.parentScreen);
            break;
         case 100:
            if (button instanceof GuiOptionButton) {
               this.game_settings_3.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
               button.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
               ScaledResolution scaledresolution = new ScaledResolution(this.mc);
               int i = scaledresolution.getScaledWidth();
               int j = scaledresolution.getScaledHeight();
               this.setWorldAndResolution(this.mc, i, j);
            }
            break;
         default:
            this.list.actionPerformed(button);
         }
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.list.drawScreen(mouseX, mouseY, partialTicks);
      drawCenteredString(this.fontRendererObj, I18n.format("options.language"), width / 2, 16, 16777215);
      drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning") + ")", width / 2, height - 56, 8421504);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   class List extends GuiSlot {
      private final java.util.List<String> langCodeList = Lists.newArrayList();
      private final Map<String, Language> languageMap = Maps.newHashMap();

      public List(Minecraft mcIn) {
         super(mcIn, GuiLanguage.width, GuiLanguage.height, 32, GuiLanguage.height - 65 + 4, 18);
         Iterator var3 = GuiLanguage.this.languageManager.getLanguages().iterator();

         while(var3.hasNext()) {
            Language language = (Language)var3.next();
            this.languageMap.put(language.getLanguageCode(), language);
            this.langCodeList.add(language.getLanguageCode());
         }

      }

      protected int getSize() {
         return this.langCodeList.size();
      }

      protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
         Language language = (Language)this.languageMap.get(this.langCodeList.get(slotIndex));
         GuiLanguage.this.languageManager.setCurrentLanguage(language);
         GuiLanguage.this.game_settings_3.language = language.getLanguageCode();
         this.mc.refreshResources();
         GuiLanguage.this.fontRendererObj.setUnicodeFlag(GuiLanguage.this.languageManager.isCurrentLocaleUnicode() || GuiLanguage.this.game_settings_3.forceUnicodeFont);
         GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.isCurrentLanguageBidirectional());
         GuiLanguage.this.confirmSettingsBtn.displayString = I18n.format("gui.done");
         GuiLanguage.this.forceUnicodeFontBtn.displayString = GuiLanguage.this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
         GuiLanguage.this.game_settings_3.saveOptions();
      }

      protected boolean isSelected(int slotIndex) {
         return ((String)this.langCodeList.get(slotIndex)).equals(GuiLanguage.this.languageManager.getCurrentLanguage().getLanguageCode());
      }

      protected int getContentHeight() {
         return this.getSize() * 18;
      }

      protected void drawBackground() {
         GuiLanguage.this.drawDefaultBackground();
      }

      protected void drawSlot(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
         GuiLanguage.this.fontRendererObj.setBidiFlag(true);
         GuiLanguage var10000 = GuiLanguage.this;
         GuiLanguage.drawCenteredString(GuiLanguage.this.fontRendererObj, ((Language)this.languageMap.get(this.langCodeList.get(p_192637_1_))).toString(), this.width / 2, p_192637_3_ + 1, 16777215);
         GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.getCurrentLanguage().isBidirectional());
      }
   }
}
