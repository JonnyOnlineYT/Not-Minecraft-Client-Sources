package net.minecraft.client.gui.screen;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import optifine.Lang;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class LanguageScreen extends Screen {
    private final Screen parent;
    private final GameSettings settings;
    private final LanguageManager languageManager;
    private LanguageList list;

    @Override
    public void init() {
        widgets.add(new GuiOptionButton(width / 2 - 155, height - 38, GameSettings.Options.FORCE_UNICODE_FONT, settings.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
        widgets.add(new ButtonWidget(width / 2 - 155 + 160, height - 38, I18n.format("gui.done"), btn -> close()));
        list = new LanguageList(minecraft);
        list.registerScrollButtons(7, 8);
    }

    @Override
    public void actionPerformed(ButtonWidget button) {
        list.actionPerformed(button);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        list.handleMouseInput();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        list.drawScreen(mouseX, mouseY, partialTick);
        drawCenteredString(font, I18n.format("options.language"), width / 2, 16, -1);
        drawCenteredString(font, "(" + I18n.format("options.languageWarning") + ")", width / 2, height - 56, 8421504);
        super.draw(mouseX, mouseY, partialTick);
    }

    @Override
    public void close() {
        Minecraft.openScreen(parent);
        Language lang = list.languages.get(list.selected);
        if (lang.equals(languageManager.getCurrentLanguage())) return;
        languageManager.setCurrentLanguage(lang);
        settings.language = lang.getCode();
        Minecraft.getLanguageManager().onResourceManagerReload(Minecraft.getResourceManager());
        Lang.resourcesReloaded();
        Minecraft.font.setUnicodeFlag(languageManager.isCurrentLocaleUnicode() || settings.forceUnicodeFont);
        Minecraft.font.setBidiFlag(languageManager.isCurrentLanguageBidirectional());
        settings.saveOptions();
    }

    class LanguageList extends GuiSlot {
        private final List<Language> languages;
        private int selected;

        public LanguageList(Minecraft minecraft) {
            super(minecraft, LanguageScreen.this.width, LanguageScreen.this.height, 32, LanguageScreen.this.height - 65 + 4, 18);
            languages = new ArrayList<>(languageManager.getLanguages());
            selected = languages.indexOf(languageManager.getCurrentLanguage());
        }

        @Override
        public int getSize() {
            return languages.size();
        }

        protected void elementClicked(int i, boolean doubleClick, int mouseX, int mouseY) {
            selected = i;
        }

        protected boolean isSelected(int i) {
            return i == selected;
        }

        protected int getContentHeight() {
            return getSize() * 18;
        }

        protected void drawBackground() {
            drawDefaultBackground();
        }

        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int y, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
            font.setBidiFlag(true);
            drawCenteredString(font, languages.get(p_192637_1_).toString(), width / 2, y + 1, 16777215);
            font.setBidiFlag(languageManager.getCurrentLanguage().isBidirectional());
        }
    }
}
