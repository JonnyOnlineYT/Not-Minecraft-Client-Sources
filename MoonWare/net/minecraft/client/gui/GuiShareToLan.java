package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.GameType;

public class GuiShareToLan extends Screen
{
    private final Screen lastScreen;
    private ButtonWidget allowCheatsButton;
    private ButtonWidget gameModeButton;
    private String gameMode = "survival";
    private boolean allowCheats;

    public GuiShareToLan(Screen p_i1055_1_)
    {
        lastScreen = p_i1055_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        widgets.clear();
        widgets.add(new ButtonWidget(101, width / 2 - 155, height - 28, 150, 20, I18n.format("lanServer.start")));
        widgets.add(new ButtonWidget(102, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
        gameModeButton = addButton(new ButtonWidget(104, width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.gameMode")));
        allowCheatsButton = addButton(new ButtonWidget(103, width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.allowCommands")));
        updateDisplayNames();
    }

    private void updateDisplayNames()
    {
        gameModeButton.text = I18n.format("selectWorld.gameMode") + ": " + I18n.format("selectWorld.gameMode." + gameMode);
        allowCheatsButton.text = I18n.format("selectWorld.allowCommands") + " ";

        if (allowCheats)
        {
            allowCheatsButton.text = allowCheatsButton.text + I18n.format("options.on");
        }
        else
        {
            allowCheatsButton.text = allowCheatsButton.text + I18n.format("options.off");
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.id == 102)
        {
            Minecraft.openScreen(lastScreen);
        }
        else if (button.id == 104)
        {
            if ("spectator".equals(gameMode))
            {
                gameMode = "creative";
            }
            else if ("creative".equals(gameMode))
            {
                gameMode = "adventure";
            }
            else if ("adventure".equals(gameMode))
            {
                gameMode = "survival";
            }
            else
            {
                gameMode = "spectator";
            }

            updateDisplayNames();
        }
        else if (button.id == 103)
        {
            allowCheats = !allowCheats;
            updateDisplayNames();
        }
        else if (button.id == 101)
        {
            Minecraft.openScreen(null);
            String s = Minecraft.getIntegratedServer().shareToLAN(GameType.getByName(gameMode), allowCheats);
            Component itextcomponent;

            if (s != null)
            {
                itextcomponent = new TranslatableComponent("commands.publish.started", s);
            }
            else
            {
                itextcomponent = new TextComponent("commands.publish.failed");
            }

            Minecraft.ingameGUI.getChatGUI().printChatMessage(itextcomponent);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        drawCenteredString(font, I18n.format("lanServer.title"), width / 2, 50, 16777215);
        drawCenteredString(font, I18n.format("lanServer.otherPlayers"), width / 2, 82, 16777215);
        super.draw(mouseX, mouseY, partialTick);
    }
}
