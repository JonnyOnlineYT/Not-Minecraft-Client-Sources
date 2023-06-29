package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

public class GuiCommandBlock extends Screen implements ITabCompleter
{
    /** Text field containing the command block's command. */
    private GuiTextField commandTextField;
    private GuiTextField previousOutputTextField;
    private final TileEntityCommandBlock commandBlock;

    /** "Done" button for the GUI. */
    private ButtonWidget doneBtn;
    private ButtonWidget cancelBtn;
    private ButtonWidget outputBtn;
    private ButtonWidget modeBtn;
    private ButtonWidget conditionalBtn;
    private ButtonWidget autoExecBtn;
    private boolean trackOutput;
    private TileEntityCommandBlock.Mode commandBlockMode = TileEntityCommandBlock.Mode.REDSTONE;
    private TabCompleter tabCompleter;
    private boolean conditional;
    private boolean automatic;

    public GuiCommandBlock(TileEntityCommandBlock commandBlockIn)
    {
        commandBlock = commandBlockIn;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        commandTextField.update();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        CommandBlockBaseLogic commandblockbaselogic = commandBlock.getCommandBlockLogic();
        Keyboard.enableRepeatEvents(true);
        widgets.clear();
        doneBtn = addButton(new ButtonWidget(0, width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20, I18n.format("gui.done")));
        cancelBtn = addButton(new ButtonWidget(1, width / 2 + 4, height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel")));
        outputBtn = addButton(new ButtonWidget(4, width / 2 + 150 - 20, 135, 20, 20, "O"));
        modeBtn = addButton(new ButtonWidget(5, width / 2 - 50 - 100 - 4, 165, 100, 20, I18n.format("advMode.mode.sequence")));
        conditionalBtn = addButton(new ButtonWidget(6, width / 2 - 50, 165, 100, 20, I18n.format("advMode.mode.unconditional")));
        autoExecBtn = addButton(new ButtonWidget(7, width / 2 + 50 + 4, 165, 100, 20, I18n.format("advMode.mode.redstoneTriggered")));
        commandTextField = new GuiTextField(2, font, width / 2 - 150, 50, 300, 20);
        commandTextField.setMaxStringLength(32500);
        commandTextField.setFocused(true);
        previousOutputTextField = new GuiTextField(3, font, width / 2 - 150, 135, 276, 20);
        previousOutputTextField.setMaxStringLength(32500);
        previousOutputTextField.setEnabled(false);
        previousOutputTextField.setText("-");
        doneBtn.enabled = false;
        outputBtn.enabled = false;
        modeBtn.enabled = false;
        conditionalBtn.enabled = false;
        autoExecBtn.enabled = false;
        tabCompleter = new TabCompleter(commandTextField, true)
        {
            @Nullable
            public BlockPos getTargetBlockPos()
            {
                return commandblockbaselogic.getPosition();
            }
        };
    }

    public void updateGui()
    {
        CommandBlockBaseLogic commandblockbaselogic = commandBlock.getCommandBlockLogic();
        commandTextField.setText(commandblockbaselogic.getCommand());
        trackOutput = commandblockbaselogic.shouldTrackOutput();
        commandBlockMode = commandBlock.getMode();
        conditional = commandBlock.isConditional();
        automatic = commandBlock.isAuto();
        updateCmdOutput();
        updateMode();
        updateConditional();
        updateAutoExec();
        doneBtn.enabled = true;
        outputBtn.enabled = true;
        modeBtn.enabled = true;
        conditionalBtn.enabled = true;
        autoExecBtn.enabled = true;
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
            CommandBlockBaseLogic commandblockbaselogic = commandBlock.getCommandBlockLogic();

            if (button.id == 1)
            {
                commandblockbaselogic.setTrackOutput(trackOutput);
                Minecraft.openScreen(null);
            }
            else if (button.id == 0)
            {
                PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                commandblockbaselogic.fillInInfo(packetbuffer);
                packetbuffer.writeString(commandTextField.getText());
                packetbuffer.writeBoolean(commandblockbaselogic.shouldTrackOutput());
                packetbuffer.writeString(commandBlockMode.name());
                packetbuffer.writeBoolean(conditional);
                packetbuffer.writeBoolean(automatic);
                minecraft.getConnection().sendPacket(new CPacketCustomPayload("MC|AutoCmd", packetbuffer));

                if (!commandblockbaselogic.shouldTrackOutput())
                {
                    commandblockbaselogic.setLastOutput(null);
                }

                Minecraft.openScreen(null);
            }
            else if (button.id == 4)
            {
                commandblockbaselogic.setTrackOutput(!commandblockbaselogic.shouldTrackOutput());
                updateCmdOutput();
            }
            else if (button.id == 5)
            {
                nextMode();
                updateMode();
            }
            else if (button.id == 6)
            {
                conditional = !conditional;
                updateConditional();
            }
            else if (button.id == 7)
            {
                automatic = !automatic;
                updateAutoExec();
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

        commandTextField.textboxKeyTyped(c, key);
        previousOutputTextField.textboxKeyTyped(c, key);

        if (key != 28 && key != 156)
        {
            if (key == 1)
            {
                actionPerformed(cancelBtn);
            }
        }
        else
        {
            actionPerformed(doneBtn);
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);
        commandTextField.mouseClicked(mouseX, mouseY, button);
        previousOutputTextField.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        drawCenteredString(font, I18n.format("advMode.setCommand"), width / 2, 20, 16777215);
        drawString(font, I18n.format("advMode.command"), width / 2 - 150, 40, 10526880);
        commandTextField.drawTextBox();
        int i = 75;
        int j = 0;
        drawString(font, I18n.format("advMode.nearestPlayer"), width / 2 - 140, i + j++ * font.height, 10526880);
        drawString(font, I18n.format("advMode.randomPlayer"), width / 2 - 140, i + j++ * font.height, 10526880);
        drawString(font, I18n.format("advMode.allPlayers"), width / 2 - 140, i + j++ * font.height, 10526880);
        drawString(font, I18n.format("advMode.allEntities"), width / 2 - 140, i + j++ * font.height, 10526880);
        drawString(font, I18n.format("advMode.self"), width / 2 - 140, i + j++ * font.height, 10526880);

        if (!previousOutputTextField.getText().isEmpty())
        {
            i = i + j * font.height + 1;
            drawString(font, I18n.format("advMode.previousOutput"), width / 2 - 150, i + 4, 10526880);
            previousOutputTextField.drawTextBox();
        }

        super.draw(mouseX, mouseY, partialTick);
    }

    private void updateCmdOutput()
    {
        CommandBlockBaseLogic commandblockbaselogic = commandBlock.getCommandBlockLogic();

        if (commandblockbaselogic.shouldTrackOutput())
        {
            outputBtn.text = "O";

            if (commandblockbaselogic.getLastOutput() != null)
            {
                previousOutputTextField.setText(commandblockbaselogic.getLastOutput().asString());
            }
        }
        else
        {
            outputBtn.text = "X";
            previousOutputTextField.setText("-");
        }
    }

    private void updateMode()
    {
        switch (commandBlockMode)
        {
            case SEQUENCE:
                modeBtn.text = I18n.format("advMode.mode.sequence");
                break;

            case AUTO:
                modeBtn.text = I18n.format("advMode.mode.auto");
                break;

            case REDSTONE:
                modeBtn.text = I18n.format("advMode.mode.redstone");
        }
    }

    private void nextMode()
    {
        switch (commandBlockMode)
        {
            case SEQUENCE:
                commandBlockMode = TileEntityCommandBlock.Mode.AUTO;
                break;

            case AUTO:
                commandBlockMode = TileEntityCommandBlock.Mode.REDSTONE;
                break;

            case REDSTONE:
                commandBlockMode = TileEntityCommandBlock.Mode.SEQUENCE;
        }
    }

    private void updateConditional()
    {
        if (conditional)
        {
            conditionalBtn.text = I18n.format("advMode.mode.conditional");
        }
        else
        {
            conditionalBtn.text = I18n.format("advMode.mode.unconditional");
        }
    }

    private void updateAutoExec()
    {
        if (automatic)
        {
            autoExecBtn.text = I18n.format("advMode.mode.autoexec.bat");
        }
        else
        {
            autoExecBtn.text = I18n.format("advMode.mode.redstoneTriggered");
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
