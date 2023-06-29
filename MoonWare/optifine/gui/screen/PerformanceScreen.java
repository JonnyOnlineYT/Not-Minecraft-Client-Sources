package optifine.gui.screen;

import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.gui.OFOptionButton;
import optifine.gui.OFOptionSlider;

@AllArgsConstructor
public class PerformanceScreen extends Screen {
    private static GameSettings.Options[] SETTINGS = {GameSettings.Options.SMOOTH_FPS, GameSettings.Options.SMOOTH_WORLD, GameSettings.Options.FAST_RENDER, GameSettings.Options.CHUNK_UPDATES, GameSettings.Options.CHUNK_UPDATES_DYNAMIC, GameSettings.Options.LAZY_CHUNK_LOADING};
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
        drawCenteredString(font, I18n.format("of.options.performanceTitle"), width / 2, 15, 16777215);
        super.draw(mouseX, mouseY, partialTick);
    }
}
