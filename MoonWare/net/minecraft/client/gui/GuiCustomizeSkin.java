package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class GuiCustomizeSkin extends Screen
{
    /** The parent GUI for this GUI */
    private final Screen parentScreen;

    /** The title of the GUI. */
    private String title;

    public GuiCustomizeSkin(Screen parentScreenIn)
    {
        parentScreen = parentScreenIn;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        int i = 0;
        title = I18n.format("options.skinCustomisation.title");

        for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values())
        {
            widgets.add(new GuiCustomizeSkin.ButtonPart(enumplayermodelparts.getPartId(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), 150, 20, enumplayermodelparts));
            ++i;
        }

        widgets.add(new GuiOptionButton(199, width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), GameSettings.Options.MAIN_HAND, Minecraft.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND)));
        ++i;

        if (i % 2 == 1)
        {
            ++i;
        }

        widgets.add(new ButtonWidget(200, width / 2 - 100, height / 6 + 24 * (i >> 1), I18n.format("gui.done")));
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
            if (button.id == 200)
            {
                Minecraft.gameSettings.saveOptions();
                Minecraft.openScreen(parentScreen);
            }
            else if (button.id == 199)
            {
                Minecraft.gameSettings.setOptionValue(GameSettings.Options.MAIN_HAND, 1);
                button.text = Minecraft.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND);
                Minecraft.gameSettings.sendSettingsToServer();
            }
            else if (button instanceof GuiCustomizeSkin.ButtonPart)
            {
                EnumPlayerModelParts enumplayermodelparts = ((GuiCustomizeSkin.ButtonPart)button).playerModelParts;
                Minecraft.gameSettings.switchModelPartEnabled(enumplayermodelparts);
                button.text = getMessage(enumplayermodelparts);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        drawCenteredString(font, title, width / 2, 20, 16777215);
        super.draw(mouseX, mouseY, partialTick);
    }

    private String getMessage(EnumPlayerModelParts playerModelParts)
    {
        String s;

        if (Minecraft.gameSettings.getModelParts().contains(playerModelParts))
        {
            s = I18n.format("options.on");
        }
        else
        {
            s = I18n.format("options.off");
        }

        return playerModelParts.getName().asFormattedString() + ": " + s;
    }

    class ButtonPart extends ButtonWidget
    {
        private final EnumPlayerModelParts playerModelParts;

        private ButtonPart(int p_i45514_2_, int p_i45514_3_, int p_i45514_4_, int p_i45514_5_, int p_i45514_6_, EnumPlayerModelParts playerModelParts)
        {
            super(p_i45514_2_, p_i45514_3_, p_i45514_4_, p_i45514_5_, p_i45514_6_, getMessage(playerModelParts));
            this.playerModelParts = playerModelParts;
        }
    }
}
