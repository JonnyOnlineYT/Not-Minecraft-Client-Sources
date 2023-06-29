package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.OS;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

public class GuiWorldEdit extends Screen
{
    private final Screen lastScreen;
    private GuiTextField nameEdit;
    private final String worldId;

    public GuiWorldEdit(Screen p_i46593_1_, String p_i46593_2_)
    {
        lastScreen = p_i46593_1_;
        worldId = p_i46593_2_;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        nameEdit.update();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        Keyboard.enableRepeatEvents(true);
        widgets.clear();
        ButtonWidget guibutton = addButton(new ButtonWidget(3, width / 2 - 100, height / 4 + 24 + 12, I18n.format("selectWorld.edit.resetIcon")));
        widgets.add(new ButtonWidget(4, width / 2 - 100, height / 4 + 48 + 12, I18n.format("selectWorld.edit.openFolder")));
        widgets.add(new ButtonWidget(0, width / 2 - 100, height / 4 + 96 + 12, I18n.format("selectWorld.edit.save")));
        widgets.add(new ButtonWidget(1, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel")));
        guibutton.enabled = minecraft.getSaveLoader().getFile(worldId, "icon.png").isFile();
        ISaveFormat isaveformat = minecraft.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo(worldId);
        String s = worldinfo == null ? "" : worldinfo.getWorldName();
        nameEdit = new GuiTextField(2, font, width / 2 - 100, 60, 200, 20);
        nameEdit.setFocused(true);
        nameEdit.setText(s);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        Keyboard.enableRepeatEvents(false);
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
                Minecraft.openScreen(lastScreen);
            }
            else if (button.id == 0)
            {
                ISaveFormat isaveformat = minecraft.getSaveLoader();
                isaveformat.renameWorld(worldId, nameEdit.getText().trim());
                Minecraft.openScreen(lastScreen);
            }
            else if (button.id == 3)
            {
                ISaveFormat isaveformat1 = minecraft.getSaveLoader();
                FileUtils.deleteQuietly(isaveformat1.getFile(worldId, "icon.png"));
                button.enabled = false;
            }
            else if (button.id == 4)
            {
                ISaveFormat isaveformat2 = minecraft.getSaveLoader();
                OS.openFile(isaveformat2.getFile(worldId, "icon.png").getParentFile());
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        nameEdit.textboxKeyTyped(c, key);
        (widgets.get(2)).enabled = !nameEdit.getText().trim().isEmpty();

        if (key == 28 || key == 156)
        {
            actionPerformed(widgets.get(2));
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);
        nameEdit.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        drawCenteredString(font, I18n.format("selectWorld.edit.title"), width / 2, 20, 16777215);
        drawString(font, I18n.format("selectWorld.enterName"), width / 2 - 100, 47, 10526880);
        nameEdit.drawTextBox();
        super.draw(mouseX, mouseY, partialTick);
    }
}
