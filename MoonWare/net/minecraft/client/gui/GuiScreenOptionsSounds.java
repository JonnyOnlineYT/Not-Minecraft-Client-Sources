package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class GuiScreenOptionsSounds extends Screen
{
    private final Screen parent;

    /** Reference to the GameSettings object. */
    private final GameSettings game_settings_4;
    protected String title = "Options";
    private String offDisplayString;

    public GuiScreenOptionsSounds(Screen parentIn, GameSettings settingsIn)
    {
        parent = parentIn;
        game_settings_4 = settingsIn;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        title = I18n.format("options.sounds.title");
        offDisplayString = I18n.format("options.off");
        int i = 0;
        widgets.add(new GuiScreenOptionsSounds.Button(SoundCategory.MASTER.ordinal(), width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), SoundCategory.MASTER, true));
        i = i + 2;

        for (SoundCategory soundcategory : SoundCategory.values())
        {
            if (soundcategory != SoundCategory.MASTER)
            {
                widgets.add(new GuiScreenOptionsSounds.Button(soundcategory.ordinal(), width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), soundcategory, false));
                ++i;
            }
        }

        int j = width / 2 - 75;
        int k = height / 6 - 12;
        ++i;
        widgets.add(new GuiOptionButton(201, j, k + 24 * (i >> 1), GameSettings.Options.SHOW_SUBTITLES, game_settings_4.getKeyBinding(GameSettings.Options.SHOW_SUBTITLES)));
        widgets.add(new ButtonWidget(200, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
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
                Minecraft.openScreen(parent);
            }
            else if (button.id == 201)
            {
                Minecraft.gameSettings.setOptionValue(GameSettings.Options.SHOW_SUBTITLES, 1);
                button.text = Minecraft.gameSettings.getKeyBinding(GameSettings.Options.SHOW_SUBTITLES);
                Minecraft.gameSettings.saveOptions();
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        drawCenteredString(font, title, width / 2, 15, 16777215);
        super.draw(mouseX, mouseY, partialTick);
    }

    protected String getDisplayString(SoundCategory category)
    {
        float f = game_settings_4.getSoundLevel(category);
        return f == 0.0F ? offDisplayString : (int)(f * 100.0F) + "%";
    }

    class Button extends ButtonWidget
    {
        private final SoundCategory category;
        private final String categoryName;
        public float volume = 1.0F;
        public boolean pressed;

        public Button(int p_i46744_2_, int x, int y, SoundCategory categoryIn, boolean master)
        {
            super(p_i46744_2_, x, y, master ? 310 : 150, 20, "");
            category = categoryIn;
            categoryName = I18n.format("soundCategory." + categoryIn.getName());
            text = categoryName + ": " + getDisplayString(categoryIn);
            volume = game_settings_4.getSoundLevel(categoryIn);
        }

        protected int getHoverState(boolean mouseOver)
        {
            return 0;
        }

        protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
        {
            if (visible)
            {
                if (pressed)
                {
                    volume = (float)(mouseX - (x + 4)) / (float)(width - 8);
                    volume = MathHelper.clamp(volume, 0.0F, 1.0F);
                    Minecraft.gameSettings.setSoundLevel(category, volume);
                    Minecraft.gameSettings.saveOptions();
                    text = categoryName + ": " + getDisplayString(category);
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                drawTexturedModalRect(x + (int)(volume * (float)(width - 8)), y, 0, 66, 4, 20);
                drawTexturedModalRect(x + (int)(volume * (float)(width - 8)) + 4, y, 196, 66, 4, 20);
            }
        }

        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
        {
            if (super.mousePressed(mc, mouseX, mouseY))
            {
                volume = (float)(mouseX - (x + 4)) / (float)(width - 8);
                volume = MathHelper.clamp(volume, 0.0F, 1.0F);
                Minecraft.gameSettings.setSoundLevel(category, volume);
                Minecraft.gameSettings.saveOptions();
                text = categoryName + ": " + getDisplayString(category);
                pressed = true;
                return true;
            }
            else
            {
                return false;
            }
        }

        public void playPressSound(SoundHandler soundHandlerIn)
        {
        }

        public void mouseReleased(int mouseX, int mouseY)
        {
            if (pressed)
            {
                Minecraft.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }

            pressed = false;
        }
    }
}
