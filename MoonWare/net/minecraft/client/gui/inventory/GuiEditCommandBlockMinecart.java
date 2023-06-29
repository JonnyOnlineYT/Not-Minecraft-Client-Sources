package net.minecraft.client.gui.inventory;

import io.netty.buffer.Unpooled;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

public class GuiEditCommandBlockMinecart extends Screen implements ITabCompleter
{
    private GuiTextField commandField;
    private GuiTextField previousEdit;
    private final CommandBlockBaseLogic commandBlockLogic;
    private ButtonWidget doneButton;
    private ButtonWidget cancelButton;
    private ButtonWidget outputButton;
    private boolean trackOutput;
    private TabCompleter tabCompleter;

    public GuiEditCommandBlockMinecart(CommandBlockBaseLogic p_i46595_1_)
    {
        commandBlockLogic = p_i46595_1_;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        commandField.update();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        Keyboard.enableRepeatEvents(true);
        widgets.clear();
        doneButton = addButton(new ButtonWidget(0, width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20, I18n.format("gui.done")));
        cancelButton = addButton(new ButtonWidget(1, width / 2 + 4, height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel")));
        outputButton = addButton(new ButtonWidget(4, width / 2 + 150 - 20, 150, 20, 20, "O"));
        commandField = new GuiTextField(2, font, width / 2 - 150, 50, 300, 20);
        commandField.setMaxStringLength(32500);
        commandField.setFocused(true);
        commandField.setText(commandBlockLogic.getCommand());
        previousEdit = new GuiTextField(3, font, width / 2 - 150, 150, 276, 20);
        previousEdit.setMaxStringLength(32500);
        previousEdit.setEnabled(false);
        previousEdit.setText("-");
        trackOutput = commandBlockLogic.shouldTrackOutput();
        updateCommandOutput();
        doneButton.enabled = !commandField.getText().trim().isEmpty();
        tabCompleter = new TabCompleter(commandField, true)
        {
            @Nullable
            public BlockPos getTargetBlockPos()
            {
                return commandBlockLogic.getPosition();
            }
        };
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
                commandBlockLogic.setTrackOutput(trackOutput);
                Minecraft.openScreen(null);
            }
            else if (button.id == 0)
            {
                PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                packetbuffer.writeByte(commandBlockLogic.getCommandBlockType());
                commandBlockLogic.fillInInfo(packetbuffer);
                packetbuffer.writeString(commandField.getText());
                packetbuffer.writeBoolean(commandBlockLogic.shouldTrackOutput());
                minecraft.getConnection().sendPacket(new CPacketCustomPayload("MC|AdvCmd", packetbuffer));

                if (!commandBlockLogic.shouldTrackOutput())
                {
                    commandBlockLogic.setLastOutput(null);
                }

                Minecraft.openScreen(null);
            }
            else if (button.id == 4)
            {
                commandBlockLogic.setTrackOutput(!commandBlockLogic.shouldTrackOutput());
                updateCommandOutput();
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        tabCompleter.resetRequested();

        if (key == 15)
        {
            tabCompleter.complete();
        }
        else
        {
            tabCompleter.resetDidComplete();
        }

        commandField.textboxKeyTyped(c, key);
        previousEdit.textboxKeyTyped(c, key);
        doneButton.enabled = !commandField.getText().trim().isEmpty();

        if (key != 28 && key != 156)
        {
            if (key == 1)
            {
                actionPerformed(cancelButton);
            }
        }
        else
        {
            actionPerformed(doneButton);
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);
        commandField.mouseClicked(mouseX, mouseY, button);
        previousEdit.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        drawCenteredString(font, I18n.format("advMode.setCommand"), width / 2, 20, 16777215);
        drawString(font, I18n.format("advMode.command"), width / 2 - 150, 40, 10526880);
        commandField.drawTextBox();
        int i = 75;
        int j = 0;
        drawString(font, I18n.format("advMode.nearestPlayer"), width / 2 - 140, i + j++ * font.height, 10526880);
        drawString(font, I18n.format("advMode.randomPlayer"), width / 2 - 140, i + j++ * font.height, 10526880);
        drawString(font, I18n.format("advMode.allPlayers"), width / 2 - 140, i + j++ * font.height, 10526880);
        drawString(font, I18n.format("advMode.allEntities"), width / 2 - 140, i + j++ * font.height, 10526880);
        drawString(font, I18n.format("advMode.self"), width / 2 - 140, i + j++ * font.height, 10526880);

        if (!previousEdit.getText().isEmpty())
        {
            i = i + j * font.height + 20;
            drawString(font, I18n.format("advMode.previousOutput"), width / 2 - 150, i, 10526880);
            previousEdit.drawTextBox();
        }

        super.draw(mouseX, mouseY, partialTick);
    }

    private void updateCommandOutput()
    {
        if (commandBlockLogic.shouldTrackOutput())
        {
            outputButton.text = "O";

            if (commandBlockLogic.getLastOutput() != null)
            {
                previousEdit.setText(commandBlockLogic.getLastOutput().asString());
            }
        }
        else
        {
            outputButton.text = "X";
            previousEdit.setText("-");
        }
    }

    /**
     * Sets the list of tab completions, as long as they were previously requested.
     */
    public void setCompletions(String... newCompletions)
    {
        tabCompleter.setCompletions(newCompletions);
    }
}
