package optifine.gui.screen;

import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.gui.OFOptionButton;
import optifine.gui.OFOptionSlider;

@AllArgsConstructor
public class OtherScreen extends Screen {
    private static final GameSettings.Options[] SETTINGS = {GameSettings.Options.LAGOMETER, GameSettings.Options.PROFILER, GameSettings.Options.SHOW_FPS, GameSettings.Options.ADVANCED_TOOLTIPS, GameSettings.Options.WEATHER, GameSettings.Options.TIME, GameSettings.Options.USE_FULLSCREEN, GameSettings.Options.FULLSCREEN_MODE, GameSettings.Options.ANAGLYPH, GameSettings.Options.AUTOSAVE_TICKS, GameSettings.Options.SCREENSHOT_SIZE};
    private final Screen parent;
    private final GameSettings settings;

    @Override
    public void init() {
        for (int i = 0; i < SETTINGS.length; ++i) {
            GameSettings.Options setting = SETTINGS[i];
            int j = width / 2 - 155 + i % 2 * 160;
            int k = height / 6 + 21 * (i / 2) - 12;

            if (!setting.getEnumFloat()) {
                widgets.add(new OFOptionButton(setting.returnEnumOrdinal(), j, k, setting, settings.getKeyBinding(setting)));
            } else {
                widgets.add(new OFOptionSlider(setting.returnEnumOrdinal(), j, k, setting));
            }
        }
        widgets.add(new ButtonWidget(width / 2 - 100, height / 6 + 168 + 11 - 44, I18n.format("of.options.other.reset"), btn -> {
            Minecraft.gameSettings.saveOptions();
            Minecraft.openScreen(new ConfirmScreen(b -> {
                if (b) Minecraft.gameSettings.resetSettings();
                Minecraft.openScreen(this);
            }, I18n.format("of.message.other.reset"), ""));
        }));
        widgets.add(new ButtonWidget(200, width / 2 - 100, height / 6 + 168 + 11, I18n.format("gui.done")));
    }

    @Override
    public void actionPerformed(ButtonWidget button) {
        if (button.enabled) {
            if (button.id < 200 && button instanceof GuiOptionButton) {
                settings.setOptionValue(((GuiOptionButton) button).getSetting(), 1);
                button.text = settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }

            if (button.id == 200) {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(parent);
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        drawCenteredString(font, I18n.format("of.options.otherTitle"), width / 2, 15, 16777215);
        super.draw(mouseX, mouseY, partialTick);
    }
}
