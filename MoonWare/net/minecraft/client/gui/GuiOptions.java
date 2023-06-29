package net.minecraft.client.gui;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.LanguageScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.EnumDifficulty;
import org.moonware.client.ui.titlescreen.TitleLikeScreen;

@RequiredArgsConstructor
public class GuiOptions extends TitleLikeScreen {
    private static final GameSettings.Options[] SCREEN_OPTIONS = {GameSettings.Options.FOV};
    private final Screen lastScreen;
    private final GameSettings settings = Minecraft.gameSettings;
    private ButtonWidget difficultyButton;
    private GuiLockIconButton lockButton;
    public GuiOptions() {
        this(null);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init() {
        if (Minecraft.world == null) super.init();
        int i = 0;

        for (GameSettings.Options gamesettings$options : SCREEN_OPTIONS) {
            if (gamesettings$options.getEnumFloat()) {
                widgets.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), gamesettings$options));
            } else {
                GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), gamesettings$options, settings.getKeyBinding(gamesettings$options));
                widgets.add(guioptionbutton);
            }

            ++i;
        }

        if (Minecraft.world != null) {
            EnumDifficulty enumdifficulty = Minecraft.world.getDifficulty();
            difficultyButton = new ButtonWidget(108, width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), 150, 20, getDifficultyText(enumdifficulty));
            widgets.add(difficultyButton);

            if (minecraft.isSingleplayer() && !Minecraft.world.getWorldInfo().isHardcoreModeEnabled()) {
                difficultyButton.width = difficultyButton.width - 20;
                lockButton = new GuiLockIconButton(109, difficultyButton.x + difficultyButton.width, difficultyButton.y);
                widgets.add(lockButton);
                lockButton.setLocked(Minecraft.world.getWorldInfo().isDifficultyLocked());
                lockButton.enabled = !lockButton.isLocked();
                difficultyButton.enabled = !lockButton.isLocked();
            } else {
                difficultyButton.enabled = false;
            }
        }

        widgets.add(new ButtonWidget(110, width / 2 - 155, height / 6 + 48 - 6, 150, 20, I18n.format("options.skinCustomisation")));
        widgets.add(new ButtonWidget(106, width / 2 + 5, height / 6 + 48 - 6, 150, 20, I18n.format("options.sounds")));
        widgets.add(new ButtonWidget(101, width / 2 - 155, height / 6 + 72 - 6, 150, 20, I18n.format("options.video")));
        widgets.add(new ButtonWidget(100, width / 2 + 5, height / 6 + 72 - 6, 150, 20, I18n.format("options.controls")));
        widgets.add(new ButtonWidget(102, width / 2 - 155, height / 6 + 96 - 6, 150, 20, I18n.format("options.language")));
        widgets.add(new ButtonWidget(103, width / 2 + 5, height / 6 + 96 - 6, 150, 20, I18n.format("options.chat.title")));
        widgets.add(new ButtonWidget(105, width / 2 - 155, height / 6 + 120 - 6, 150, 20, I18n.format("options.resourcepack")));
        //widgets.add(new ButtonWidget(width / 2 + 5, height / 6 + 120 - 6, 150, 20, "QOL", btn -> Minecraft.openScreen(new QOLScreen(this))));
        widgets.add(new ButtonWidget(200, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
    }

    public String getDifficultyText(EnumDifficulty p_175355_1_) {
        Component itextcomponent = new TextComponent("");
        itextcomponent.append(new TranslatableComponent("options.difficulty"));
        itextcomponent.append(": ");
        itextcomponent.append(new TranslatableComponent(p_175355_1_.getDifficultyResourceKey()));
        return itextcomponent.asFormattedString();
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c) {
        if (key == 1) {
            Minecraft.gameSettings.saveOptions();
        }

        super.keyPressed(key, c);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button) {
        if (button.enabled) {
            if (button.id < 100 && button instanceof GuiOptionButton) {
                GameSettings.Options gamesettings$options = ((GuiOptionButton) button).getSetting();
                settings.setOptionValue(gamesettings$options, 1);
                button.text = settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }

            if (button.id == 108) {
                Minecraft.world.getWorldInfo().setDifficulty(EnumDifficulty.getDifficultyEnum(Minecraft.world.getDifficulty().getDifficultyId() + 1));
                difficultyButton.text = getDifficultyText(Minecraft.world.getDifficulty());
            }

            if (button.id == 109) {
                Minecraft.openScreen(new ConfirmScreen(b -> {
                    Minecraft.openScreen(this);
                    if (b && Minecraft.world != null) {
                        Minecraft.world.getWorldInfo().setDifficultyLocked(true);
                        lockButton.setLocked(true);
                        lockButton.enabled = false;
                        difficultyButton.enabled = false;
                    }
                }, (new TranslatableComponent("difficulty.lock.title")).asFormattedString(), (new TranslatableComponent("difficulty.lock.question", new TranslatableComponent(Minecraft.world.getWorldInfo().getDifficulty().getDifficultyResourceKey()))).asFormattedString()));
            }

            if (button.id == 110) {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(new GuiCustomizeSkin(this));
            }

            if (button.id == 101) {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(new GuiVideoSettings(this, settings));
            }

            if (button.id == 100) {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(new GuiControls(this, settings));
            }

            if (button.id == 102) {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(new LanguageScreen(this, settings, Minecraft.getLanguageManager()));
            }

            if (button.id == 103) {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(new ScreenChatOptions(this, settings));
            }

            if (button.id == 200) {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(lastScreen);
            }

            if (button.id == 105) {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(new GuiScreenResourcePacks(this));
            }

            if (button.id == 106) {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(new GuiScreenOptionsSounds(this, settings));
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        if (Minecraft.world == null) drawTitle();
        super.draw(mouseX, mouseY, partialTick);
    }
}
