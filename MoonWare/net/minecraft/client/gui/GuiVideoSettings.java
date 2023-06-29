package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.AlertScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.TranslatableComponent;
import optifine.Config;
import optifine.GuiScreenOF;
import optifine.Lang;
import optifine.gui.OFOptionButton;
import optifine.gui.OFOptionSlider;
import optifine.gui.screen.*;
import shadersmod.client.GuiShaders;

public class GuiVideoSettings extends Screen {
    private Screen parentScreen;
    protected String screenTitle = "Video Settings";
    private GameSettings guiGameSettings;
    private static GameSettings.Options[] videoOptions = {GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.ATTACK_INDICATOR, GameSettings.Options.DYNAMIC_LIGHTS, GameSettings.Options.DYNAMIC_FOV};
    private static final String __OBFID = "CL_00000718";

    public GuiVideoSettings(Screen parentScreenIn, GameSettings gameSettingsIn) {
        parentScreen = parentScreenIn;
        guiGameSettings = gameSettingsIn;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init() {
        screenTitle = I18n.format("options.videoTitle");
        widgets.clear();

        for (int i = 0; i < videoOptions.length; ++i) {
            GameSettings.Options gamesettings$options = videoOptions[i];

            if (gamesettings$options != null) {
                int j = width / 2 - 155 + i % 2 * 160;
                int k = height / 6 + 21 * (i / 2) - 12;

                if (gamesettings$options.getEnumFloat()) {
                    widgets.add(new OFOptionSlider(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
                } else {
                    widgets.add(new OFOptionButton(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, guiGameSettings.getKeyBinding(gamesettings$options)));
                }
            }
        }

        int l = height / 6 + 21 * (videoOptions.length / 2) - 12;
        int i1 = 0;
        i1 = width / 2 - 155;
        widgets.add(new ButtonWidget(231, i1, l, 150, 20, Lang.get("of.options.shaders")));
        i1 = width / 2 - 155 + 160;
        widgets.add(new ButtonWidget(202, i1, l, 150, 20, Lang.get("of.options.quality")));
        l = l + 21;
        i1 = width / 2 - 155;
        widgets.add(new ButtonWidget(201, i1, l, 150, 20, Lang.get("of.options.details")));
        i1 = width / 2 - 155 + 160;
        widgets.add(new ButtonWidget(212, i1, l, 150, 20, Lang.get("of.options.performance")));
        l = l + 21;
        i1 = width / 2 - 155;
        widgets.add(new ButtonWidget(211, i1, l, 150, 20, Lang.get("of.options.animations")));
        i1 = width / 2 - 155 + 160;
        widgets.add(new ButtonWidget(222, i1, l, 150, 20, Lang.get("of.options.other")));
        l = l + 21;
        widgets.add(new ButtonWidget(200, width / 2 - 100, height / 6 + 168 + 11, I18n.format("gui.done")));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button) {
        actionPerformed(button, 1);
    }

    private void actionPerformed(ButtonWidget p_actionPerformed_1_, int p_actionPerformed_2_) {
        if (p_actionPerformed_1_.enabled) {
            int i = guiGameSettings.scale;

            if (p_actionPerformed_1_.id < 200 && p_actionPerformed_1_ instanceof GuiOptionButton) {
                guiGameSettings.setOptionValue(((GuiOptionButton) p_actionPerformed_1_).getSetting(), p_actionPerformed_2_);
                p_actionPerformed_1_.text = guiGameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(p_actionPerformed_1_.id));
            }

            if (p_actionPerformed_1_.id == 200) {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(parentScreen);
            }

            if (guiGameSettings.scale != i) {
                Minecraft.updateScaled();
                init(Minecraft.getScaledRoundedWidth(), Minecraft.getScaledRoundedHeight());
            }

            if (p_actionPerformed_1_.id == 201) {
                Minecraft.gameSettings.saveOptions();
                DetailsScreen guidetailsettingsof = new DetailsScreen(this, guiGameSettings);
                Minecraft.openScreen(guidetailsettingsof);
            }

            if (p_actionPerformed_1_.id == 202) {
                Minecraft.gameSettings.saveOptions();
                QualityScreen guiqualitysettingsof = new QualityScreen(this, guiGameSettings);
                Minecraft.openScreen(guiqualitysettingsof);
            }

            if (p_actionPerformed_1_.id == 211) {
                Minecraft.gameSettings.saveOptions();
                AnimationsScreen guianimationsettingsof = new AnimationsScreen(this, guiGameSettings);
                Minecraft.openScreen(guianimationsettingsof);
            }

            if (p_actionPerformed_1_.id == 212) {
                Minecraft.gameSettings.saveOptions();
                PerformanceScreen guiperformancesettingsof = new PerformanceScreen(this, guiGameSettings);
                Minecraft.openScreen(guiperformancesettingsof);
            }

            if (p_actionPerformed_1_.id == 222) {
                Minecraft.gameSettings.saveOptions();
                OtherScreen guiothersettingsof = new OtherScreen(this, guiGameSettings);
                Minecraft.openScreen(guiothersettingsof);
            }

            if (p_actionPerformed_1_.id == 231) {
                if (Config.isAntialiasing() || Config.isAntialiasingConfigured()) {
                    Minecraft.openScreen(new AlertScreen(new TranslatableComponent("of.message.shaders.aa1"), new TranslatableComponent("of.message.shaders.aa2"), new TranslatableComponent("gui.cancel"), () -> Minecraft.openScreen(this)));
                    return;
                }

                if (Config.isAnisotropicFiltering()) {
                    Minecraft.openScreen(new AlertScreen(new TranslatableComponent("of.message.shaders.af1"), new TranslatableComponent("of.message.shaders.af2"), new TranslatableComponent("gui.cancel"), () -> Minecraft.openScreen(this)));
                    return;
                }

                if (Config.isFastRender()) {
                    Minecraft.openScreen(new AlertScreen(new TranslatableComponent("of.message.shaders.fr1"), new TranslatableComponent("of.message.shaders.fr2"), new TranslatableComponent("gui.cancel"), () -> Minecraft.openScreen(this)));
                    return;
                }

                if (Config.getGameSettings().anaglyph) {
                    Minecraft.openScreen(new AlertScreen(new TranslatableComponent("of.message.shaders.an1"), new TranslatableComponent("of.message.shaders.an2"), new TranslatableComponent("gui.cancel"), () -> Minecraft.openScreen(this)));
                    return;
                }

                Minecraft.gameSettings.saveOptions();
                GuiShaders guishaders = new GuiShaders(this, guiGameSettings);
                Minecraft.openScreen(guishaders);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        drawCenteredString(font, screenTitle, width / 2, 15, 16777215);
        super.draw(mouseX, mouseY, partialTick);
    }

    public static int getButtonWidth(ButtonWidget p_getButtonWidth_0_) {
        return p_getButtonWidth_0_.width;
    }

    public static int getButtonHeight(ButtonWidget p_getButtonHeight_0_) {
        return p_getButtonHeight_0_.height;
    }

    public static void drawGradientRect(Screen p_drawGradientRect_0_, int p_drawGradientRect_1_, int p_drawGradientRect_2_, int p_drawGradientRect_3_, int p_drawGradientRect_4_, int p_drawGradientRect_5_, int p_drawGradientRect_6_) {
        p_drawGradientRect_0_.drawGradientRect(p_drawGradientRect_1_, p_drawGradientRect_2_, p_drawGradientRect_3_, p_drawGradientRect_4_, p_drawGradientRect_5_, p_drawGradientRect_6_);
    }
}
