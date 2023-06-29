package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiScreenServerList extends Screen
{
    private final MultiplayerScreen lastScreen;
    private final ServerData serverData;
    private GuiTextField ipEdit;

    public GuiScreenServerList(MultiplayerScreen p_i1031_1_, ServerData p_i1031_2_)
    {
        lastScreen = p_i1031_1_;
        serverData = p_i1031_2_;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        ipEdit.update();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        Keyboard.enableRepeatEvents(true);
        widgets.clear();
        widgets.add(new ButtonWidget(0, width / 2 - 100, height / 4 + 96 + 12, I18n.format("selectServer.select")));
        widgets.add(new ButtonWidget(1, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel")));
        ipEdit = new GuiTextField(2, font, width / 2 - 100, 116, 200, 20);
        ipEdit.setMaxStringLength(128);
        ipEdit.setFocused(true);
        ipEdit.setText(Minecraft.gameSettings.lastServer);
        (widgets.get(0)).enabled = !ipEdit.getText().isEmpty() && ipEdit.getText().split(":").length > 0;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        Keyboard.enableRepeatEvents(false);
        Minecraft.gameSettings.lastServer = ipEdit.getText();
        Minecraft.gameSettings.saveOptions();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                lastScreen.confirmClicked(false, 0);
            }
            else if (button.id == 0)
            {
                serverData.ip = ipEdit.getText();
                lastScreen.confirmClicked(true, 0);
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (ipEdit.textboxKeyTyped(c, key))
        {
            (widgets.get(0)).enabled = !ipEdit.getText().isEmpty() && ipEdit.getText().split(":").length > 0;
        }
        else if (key == 28 || key == 156)
        {
            actionPerformed(widgets.get(0));
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);
        ipEdit.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        drawCenteredString(font, I18n.format("selectServer.direct"), width / 2, 20, 16777215);
        drawString(font, I18n.format("addServer.enterIp"), width / 2 - 100, 100, 10526880);
        ipEdit.drawTextBox();
        super.draw(mouseX, mouseY, partialTick);
    }
}
