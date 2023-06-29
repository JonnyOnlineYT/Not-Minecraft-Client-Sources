package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class ScreenChatOptions extends Screen
{
    private static final GameSettings.Options[] CHAT_OPTIONS = {GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS, GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE, GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED, GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO};
    private final Screen parentScreen;
    private final GameSettings game_settings;
    private String chatTitle;
    private GuiOptionButton field_193025_i;

    public ScreenChatOptions(Screen parentScreenIn, GameSettings gameSettingsIn)
    {
        parentScreen = parentScreenIn;
        game_settings = gameSettingsIn;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        chatTitle = I18n.format("options.chat.title");
        int i = 0;

        for (GameSettings.Options gamesettings$options : CHAT_OPTIONS)
        {
            if (gamesettings$options.getEnumFloat())
            {
                widgets.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), gamesettings$options));
            }
            else
            {
                GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), gamesettings$options, game_settings.getKeyBinding(gamesettings$options));
                widgets.add(guioptionbutton);
            }

            ++i;
        }

        widgets.add(new ButtonWidget(200, width / 2 - 100, height / 6 + 144, I18n.format("gui.done")));
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (key == 1)
        {
            Minecraft.gameSettings.saveOptions();
        }

        super.keyPressed(key, c);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.enabled)
        {
            if (button.id < 100 && button instanceof GuiOptionButton)
            {
                game_settings.setOptionValue(((GuiOptionButton)button).getSetting(), 1);
                button.text = game_settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }

            if (button.id == 200)
            {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(parentScreen);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        drawCenteredString(font, chatTitle, width / 2, 20, 16777215);
        super.draw(mouseX, mouseY, partialTick);
    }

    public void func_193024_a()
    {
        field_193025_i.text = game_settings.getKeyBinding(GameSettings.Options.getEnumOptions(field_193025_i.id));
    }
}
