package optifine.gui.screen;

import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.gui.OFOptionButton;
import optifine.gui.OFOptionSlider;
import optifine.Lang;

@AllArgsConstructor
public class AnimationsScreen extends Screen {
    private static final GameSettings.Options[] SETTINGS = {GameSettings.Options.ANIMATED_WATER, GameSettings.Options.ANIMATED_LAVA, GameSettings.Options.ANIMATED_FIRE, GameSettings.Options.ANIMATED_PORTAL, GameSettings.Options.ANIMATED_REDSTONE, GameSettings.Options.ANIMATED_EXPLOSION, GameSettings.Options.ANIMATED_FLAME, GameSettings.Options.ANIMATED_SMOKE, GameSettings.Options.VOID_PARTICLES, GameSettings.Options.WATER_PARTICLES, GameSettings.Options.RAIN_SPLASH, GameSettings.Options.PORTAL_PARTICLES, GameSettings.Options.POTION_PARTICLES, GameSettings.Options.DRIPPING_WATER_LAVA, GameSettings.Options.ANIMATED_TERRAIN, GameSettings.Options.ANIMATED_TEXTURES, GameSettings.Options.FIREWORK_PARTICLES, GameSettings.Options.PARTICLES};
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
        widgets.add(new ButtonWidget(210, width / 2 - 155, height / 6 + 168 + 11, 70, 20, Lang.get("of.options.animation.allOn")));
        widgets.add(new ButtonWidget(211, width / 2 - 155 + 80, height / 6 + 168 + 11, 70, 20, Lang.get("of.options.animation.allOff")));
        widgets.add(new ButtonWidget(200, width / 2 + 5, height / 6 + 168 + 11, 150, 20, I18n.format("gui.done")));
    }

    @Override
    public void actionPerformed(ButtonWidget button) {
        if (button.id < 200 && button instanceof GuiOptionButton) {
            settings.setOptionValue(((GuiOptionButton) button).getSetting(), 1);
            button.text = settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
        }

        if (button.id == 200) {
            Minecraft.gameSettings.saveOptions();
            Minecraft.openScreen(parent);
        }

        if (button.id == 210) {
            Minecraft.gameSettings.setAllAnimations(true);
        }

        if (button.id == 211) {
            Minecraft.gameSettings.setAllAnimations(false);
        }

        ScaledResolution scaledresolution = new ScaledResolution(minecraft);
        init(scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        drawCenteredString(font, I18n.format("of.options.animationsTitle"), width / 2, 15, -1);
        super.draw(mouseX, mouseY, partialTick);
    }
}
