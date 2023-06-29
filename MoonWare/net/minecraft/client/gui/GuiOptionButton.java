package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionButton extends ButtonWidget {
    private final GameSettings.Options setting;

    public GuiOptionButton(int x, int y, GameSettings.Options setting, String text) {
        super(x, y, 150, 20, text, btn -> {
            Minecraft.gameSettings.setOptionValue(setting, 1);
        });
        this.setting = setting;
    }

    @Deprecated
    public GuiOptionButton(int id, int x, int y, GameSettings.Options setting, String text) {
        super(id, x, y, 150, 20, text);
        this.setting = setting;
    }

    public GameSettings.Options getSetting() {
        return setting;
    }
}
