package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Namespaced;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.MWFont;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.render.rect.DrawHelper;

import java.awt.*;

public class GuiControls extends Screen
{
    private static final GameSettings.Options[] OPTIONS_ARR = {GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN, GameSettings.Options.AUTO_JUMP};

    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    private final Screen parentScreen;
    protected String screenTitle = "Controls";

    /** Reference to the GameSettings object. */
    private final GameSettings options;

    /** The ID of the button that has been pressed. */
    public KeyBinding buttonId;
    public long time;
    private GuiKeyBindingList keyBindingList;
    private ButtonWidget buttonReset;

    public GuiControls(Screen screen, GameSettings settings)
    {
        parentScreen = screen;
        options = settings;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init() {
        keyBindingList = new GuiKeyBindingList(this, minecraft);
        widgets.add(new ButtonWidget(200, width / 2 - 155 + 160, height - 29, 150, 20, I18n.format("gui.done")));
        buttonReset = addButton(new ButtonWidget(201, width / 2 - 155, height - 29, 150, 20, I18n.format("controls.resetAll")));
        screenTitle = I18n.format("controls.title");
        int i = 0;

        for (GameSettings.Options gamesettings$options : OPTIONS_ARR) {
            if (gamesettings$options.getEnumFloat()) {
                widgets.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160 + 4, 18 + 24 * (i >> 1), gamesettings$options));
            } else {
                widgets.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160 + 4, 18 + 24 * (i >> 1), gamesettings$options, options.getKeyBinding(gamesettings$options)));
            }

            ++i;
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        keyBindingList.handleMouseInput();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.id == 200)
        {
            Minecraft.openScreen(parentScreen);
        }
        else if (button.id == 201)
        {
            for (KeyBinding keybinding : Minecraft.gameSettings.keyBindings)
            {
                keybinding.setKeyCode(keybinding.getKeyCodeDefault());
            }

            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else if (button.id < 100 && button instanceof GuiOptionButton)
        {
            options.setOptionValue(((GuiOptionButton)button).getSetting(), 1);
            button.text = options.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        if (buttonId != null)
        {
            options.setOptionKeyBinding(buttonId, -100 + button);
            buttonId = null;
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else if (button != 0 || !keyBindingList.mouseClicked(mouseX, mouseY, button))
        {
            super.mousePressed(mouseX, mouseY, button);
        }
    }

    /**
     * Called when a mouse button is released.
     */
    public void mouseReleased(int mouseX, int mouseY, int button)
    {
        if (button != 0 || !keyBindingList.mouseReleased(mouseX, mouseY, button))
        {
            super.mouseReleased(mouseX, mouseY, button);
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (buttonId != null)
        {
            if (key == 1)
            {
                options.setOptionKeyBinding(buttonId, 0);
            }
            else if (key != 0)
            {
                options.setOptionKeyBinding(buttonId, key);
            }
            else if (c > 0)
            {
                options.setOptionKeyBinding(buttonId, c + 256);
            }

            buttonId = null;
            time = Minecraft.getSystemTime();
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else
        {
            super.keyPressed(key, c);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        keyBindingList.drawScreen(mouseX, mouseY, partialTick);
        drawCenteredString(font, screenTitle, width / 2, 8, 16777215);

        boolean flag = false;

        for (KeyBinding keybinding : options.keyBindings)
        {
            if (keybinding.getKeyCode() != keybinding.getKeyCodeDefault())
            {
                flag = true;
                break;
            }
        }

        buttonReset.enabled = flag;

        if (Minecraft.world == null) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            //DrawHelper.drawRect(300, -4, sr.getScaledWidth() - 300, sr.getScaledHeight(), new Color(1, 1, 1, 50).getRGB());
            RoundedUtil.drawRound(9, 18, 142, 220, 4, true, new Color(0, 0, 0, 90));
            RoundedUtil.drawRound(14, sr.getScaledHeight() - 38, 132, 24, 4, true, new Color(0, 0, 0, 90));
            GL11.glPushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            DrawHelper.startSmooth();
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            Minecraft.getTextureManager().bindTexture(new Namespaced("moonware/rqbad.png"));
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            RoundedUtil.drawRoundTextured(17, sr.getScaledHeight() - 35, 18, 18, 5, 255);
            DrawHelper.endSmooth();
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GL11.glPopMatrix();
            MWFont.MONTSERRAT_BOLD.get(12).drawShadow("Hello", 40, sr.getScaledHeight() - 30, new Color(145, 145, 145).getRGB());
            MWFont.MONTSERRAT_BOLD.get(16).drawShadow("RedBool", 40, sr.getScaledHeight() - 24, -1);
            MWFont.ELEGANT_ICONS.get(30).drawShadow("E", 130, sr.getScaledHeight() - 30.5f, -1);
            RoundedUtil.drawGradientRound(9, 18, 142, 30, 4, DrawHelper.fade(new Color(102, 0, 255), 10, 100), DrawHelper.fade(new Color(102, 0, 255), 30, 100), DrawHelper.fade(new Color(255, 0, 128), 60, 100), DrawHelper.fade(new Color(47, 0, 255), 90, 100));
            MWFont.NEBORA_BLACK.get(38).drawCenterShadow("MWARE", 78, 28f, -1);
        }
        super.draw(mouseX, mouseY, partialTick);
    }
}
