package net.minecraft.client.gui;

import com.google.common.base.Predicate;

import java.net.IDN;
import javax.annotation.Nullable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;

public class GuiScreenAddServer extends Screen
{
    private final MultiplayerScreen parentScreen;
    private final ServerData serverData;
    private GuiTextField serverIPField;
    private GuiTextField serverNameField;
    private ButtonWidget serverResourcePacks;
    private final Predicate<String> addressFilter = new Predicate<String>()
    {
        public boolean apply(@Nullable String p_apply_1_)
        {
            if (StringUtils.isNullOrEmpty(p_apply_1_))
            {
                return true;
            }
            else
            {
                String[] astring = p_apply_1_.split(":");

                if (astring.length == 0)
                {
                    return true;
                }
                else
                {
                    try
                    {
                        String s = IDN.toASCII(astring[0]);
                        return true;
                    }
                    catch (IllegalArgumentException var4)
                    {
                        return false;
                    }
                }
            }
        }
    };

    public GuiScreenAddServer(MultiplayerScreen p_i1033_1_, ServerData p_i1033_2_)
    {
        parentScreen = p_i1033_1_;
        serverData = p_i1033_2_;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        serverNameField.update();
        serverIPField.update();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        Keyboard.enableRepeatEvents(true);
        widgets.clear();
        widgets.add(new ButtonWidget(0, width / 2 - 100, height / 4 + 96 + 18, I18n.format("addServer.add")));
        widgets.add(new ButtonWidget(1, width / 2 - 100, height / 4 + 120 + 18, I18n.format("gui.cancel")));
        serverResourcePacks = addButton(new ButtonWidget(2, width / 2 - 100, height / 4 + 72, I18n.format("addServer.resourcePack") + ": " + serverData.getResourceMode().getName().asFormattedString()));
        serverNameField = new GuiTextField(0, font, width / 2 - 100, 66, 200, 20);
        serverNameField.setFocused(true);
        serverNameField.setText(serverData.name);
        serverIPField = new GuiTextField(1, font, width / 2 - 100, 106, 200, 20);
        serverIPField.setMaxStringLength(128);
        serverIPField.setText(serverData.ip);
        serverIPField.setValidator(addressFilter);
        (widgets.get(0)).enabled = !serverIPField.getText().isEmpty() && serverIPField.getText().split(":").length > 0 && !serverNameField.getText().isEmpty();
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
            if (button.id == 2)
            {
                serverData.setResourceMode(ServerData.ServerResourceMode.values()[(serverData.getResourceMode().ordinal() + 1) % ServerData.ServerResourceMode.values().length]);
                serverResourcePacks.text = I18n.format("addServer.resourcePack") + ": " + serverData.getResourceMode().getName().asFormattedString();
            }
            else if (button.id == 1)
            {
                parentScreen.confirmClicked(false, 0);
            }
            else if (button.id == 0)
            {
                serverData.name = serverNameField.getText();
                serverData.ip = serverIPField.getText();
                parentScreen.confirmClicked(true, 0);
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        serverNameField.textboxKeyTyped(c, key);
        serverIPField.textboxKeyTyped(c, key);

        if (key == 15)
        {
            serverNameField.setFocused(!serverNameField.isFocused());
            serverIPField.setFocused(!serverIPField.isFocused());
        }

        if (key == 28 || key == 156)
        {
            actionPerformed(widgets.get(0));
        }

        (widgets.get(0)).enabled = !serverIPField.getText().isEmpty() && serverIPField.getText().split(":").length > 0 && !serverNameField.getText().isEmpty();
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);
        serverIPField.mouseClicked(mouseX, mouseY, button);
        serverNameField.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        drawCenteredString(font, I18n.format("addServer.title"), width / 2, 17, 16777215);
        drawString(font, I18n.format("addServer.enterName"), width / 2 - 100, 53, 10526880);
        drawString(font, I18n.format("addServer.enterIp"), width / 2 - 100, 94, 10526880);
        serverNameField.drawTextBox();
        serverIPField.drawTextBox();
        super.draw(mouseX, mouseY, partialTick);
    }
}
