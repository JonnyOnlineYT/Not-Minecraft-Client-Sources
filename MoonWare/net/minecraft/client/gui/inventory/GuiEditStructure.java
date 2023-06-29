package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiEditStructure extends Screen
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final int[] LEGAL_KEY_CODES = {203, 205, 14, 211, 199, 207};
    private final TileEntityStructure tileStructure;
    private Mirror mirror = Mirror.NONE;
    private Rotation rotation = Rotation.NONE;
    private TileEntityStructure.Mode mode = TileEntityStructure.Mode.DATA;
    private boolean ignoreEntities;
    private boolean showAir;
    private boolean showBoundingBox;
    private GuiTextField nameEdit;
    private GuiTextField posXEdit;
    private GuiTextField posYEdit;
    private GuiTextField posZEdit;
    private GuiTextField sizeXEdit;
    private GuiTextField sizeYEdit;
    private GuiTextField sizeZEdit;
    private GuiTextField integrityEdit;
    private GuiTextField seedEdit;
    private GuiTextField dataEdit;
    private ButtonWidget doneButton;
    private ButtonWidget cancelButton;
    private ButtonWidget saveButton;
    private ButtonWidget loadButton;
    private ButtonWidget rotateZeroDegreesButton;
    private ButtonWidget rotateNinetyDegreesButton;
    private ButtonWidget rotate180DegreesButton;
    private ButtonWidget rotate270DegressButton;
    private ButtonWidget modeButton;
    private ButtonWidget detectSizeButton;
    private ButtonWidget showEntitiesButton;
    private ButtonWidget mirrorButton;
    private ButtonWidget showAirButton;
    private ButtonWidget showBoundingBoxButton;
    private final List<GuiTextField> tabOrder = Lists.newArrayList();
    private final DecimalFormat decimalFormat = new DecimalFormat("0.0###");

    public GuiEditStructure(TileEntityStructure p_i47142_1_)
    {
        tileStructure = p_i47142_1_;
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        nameEdit.update();
        posXEdit.update();
        posYEdit.update();
        posZEdit.update();
        sizeXEdit.update();
        sizeYEdit.update();
        sizeZEdit.update();
        integrityEdit.update();
        seedEdit.update();
        dataEdit.update();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        Keyboard.enableRepeatEvents(true);
        widgets.clear();
        doneButton = addButton(new ButtonWidget(0, width / 2 - 4 - 150, 210, 150, 20, I18n.format("gui.done")));
        cancelButton = addButton(new ButtonWidget(1, width / 2 + 4, 210, 150, 20, I18n.format("gui.cancel")));
        saveButton = addButton(new ButtonWidget(9, width / 2 + 4 + 100, 185, 50, 20, I18n.format("structure_block.button.save")));
        loadButton = addButton(new ButtonWidget(10, width / 2 + 4 + 100, 185, 50, 20, I18n.format("structure_block.button.load")));
        modeButton = addButton(new ButtonWidget(18, width / 2 - 4 - 150, 185, 50, 20, "MODE"));
        detectSizeButton = addButton(new ButtonWidget(19, width / 2 + 4 + 100, 120, 50, 20, I18n.format("structure_block.button.detect_size")));
        showEntitiesButton = addButton(new ButtonWidget(20, width / 2 + 4 + 100, 160, 50, 20, "ENTITIES"));
        mirrorButton = addButton(new ButtonWidget(21, width / 2 - 20, 185, 40, 20, "MIRROR"));
        showAirButton = addButton(new ButtonWidget(22, width / 2 + 4 + 100, 80, 50, 20, "SHOWAIR"));
        showBoundingBoxButton = addButton(new ButtonWidget(23, width / 2 + 4 + 100, 80, 50, 20, "SHOWBB"));
        rotateZeroDegreesButton = addButton(new ButtonWidget(11, width / 2 - 1 - 40 - 1 - 40 - 20, 185, 40, 20, "0"));
        rotateNinetyDegreesButton = addButton(new ButtonWidget(12, width / 2 - 1 - 40 - 20, 185, 40, 20, "90"));
        rotate180DegreesButton = addButton(new ButtonWidget(13, width / 2 + 1 + 20, 185, 40, 20, "180"));
        rotate270DegressButton = addButton(new ButtonWidget(14, width / 2 + 1 + 40 + 1 + 20, 185, 40, 20, "270"));
        nameEdit = new GuiTextField(2, font, width / 2 - 152, 40, 300, 20);
        nameEdit.setMaxStringLength(64);
        nameEdit.setText(tileStructure.getName());
        tabOrder.add(nameEdit);
        BlockPos blockpos = tileStructure.getPosition();
        posXEdit = new GuiTextField(3, font, width / 2 - 152, 80, 80, 20);
        posXEdit.setMaxStringLength(15);
        posXEdit.setText(Integer.toString(blockpos.getX()));
        tabOrder.add(posXEdit);
        posYEdit = new GuiTextField(4, font, width / 2 - 72, 80, 80, 20);
        posYEdit.setMaxStringLength(15);
        posYEdit.setText(Integer.toString(blockpos.getY()));
        tabOrder.add(posYEdit);
        posZEdit = new GuiTextField(5, font, width / 2 + 8, 80, 80, 20);
        posZEdit.setMaxStringLength(15);
        posZEdit.setText(Integer.toString(blockpos.getZ()));
        tabOrder.add(posZEdit);
        BlockPos blockpos1 = tileStructure.getStructureSize();
        sizeXEdit = new GuiTextField(6, font, width / 2 - 152, 120, 80, 20);
        sizeXEdit.setMaxStringLength(15);
        sizeXEdit.setText(Integer.toString(blockpos1.getX()));
        tabOrder.add(sizeXEdit);
        sizeYEdit = new GuiTextField(7, font, width / 2 - 72, 120, 80, 20);
        sizeYEdit.setMaxStringLength(15);
        sizeYEdit.setText(Integer.toString(blockpos1.getY()));
        tabOrder.add(sizeYEdit);
        sizeZEdit = new GuiTextField(8, font, width / 2 + 8, 120, 80, 20);
        sizeZEdit.setMaxStringLength(15);
        sizeZEdit.setText(Integer.toString(blockpos1.getZ()));
        tabOrder.add(sizeZEdit);
        integrityEdit = new GuiTextField(15, font, width / 2 - 152, 120, 80, 20);
        integrityEdit.setMaxStringLength(15);
        integrityEdit.setText(decimalFormat.format(tileStructure.getIntegrity()));
        tabOrder.add(integrityEdit);
        seedEdit = new GuiTextField(16, font, width / 2 - 72, 120, 80, 20);
        seedEdit.setMaxStringLength(31);
        seedEdit.setText(Long.toString(tileStructure.getSeed()));
        tabOrder.add(seedEdit);
        dataEdit = new GuiTextField(17, font, width / 2 - 152, 120, 240, 20);
        dataEdit.setMaxStringLength(128);
        dataEdit.setText(tileStructure.getMetadata());
        tabOrder.add(dataEdit);
        mirror = tileStructure.getMirror();
        updateMirrorButton();
        rotation = tileStructure.getRotation();
        updateDirectionButtons();
        mode = tileStructure.getMode();
        updateMode();
        ignoreEntities = tileStructure.ignoresEntities();
        updateEntitiesButton();
        showAir = tileStructure.showsAir();
        updateToggleAirButton();
        showBoundingBox = tileStructure.showsBoundingBox();
        updateToggleBoundingBox();
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
                tileStructure.setMirror(mirror);
                tileStructure.setRotation(rotation);
                tileStructure.setMode(mode);
                tileStructure.setIgnoresEntities(ignoreEntities);
                tileStructure.setShowAir(showAir);
                tileStructure.setShowBoundingBox(showBoundingBox);
                Minecraft.openScreen(null);
            }
            else if (button.id == 0)
            {
                if (sendToServer(1))
                {
                    Minecraft.openScreen(null);
                }
            }
            else if (button.id == 9)
            {
                if (tileStructure.getMode() == TileEntityStructure.Mode.SAVE)
                {
                    sendToServer(2);
                    Minecraft.openScreen(null);
                }
            }
            else if (button.id == 10)
            {
                if (tileStructure.getMode() == TileEntityStructure.Mode.LOAD)
                {
                    sendToServer(3);
                    Minecraft.openScreen(null);
                }
            }
            else if (button.id == 11)
            {
                tileStructure.setRotation(Rotation.NONE);
                updateDirectionButtons();
            }
            else if (button.id == 12)
            {
                tileStructure.setRotation(Rotation.CLOCKWISE_90);
                updateDirectionButtons();
            }
            else if (button.id == 13)
            {
                tileStructure.setRotation(Rotation.CLOCKWISE_180);
                updateDirectionButtons();
            }
            else if (button.id == 14)
            {
                tileStructure.setRotation(Rotation.COUNTERCLOCKWISE_90);
                updateDirectionButtons();
            }
            else if (button.id == 18)
            {
                tileStructure.nextMode();
                updateMode();
            }
            else if (button.id == 19)
            {
                if (tileStructure.getMode() == TileEntityStructure.Mode.SAVE)
                {
                    sendToServer(4);
                    Minecraft.openScreen(null);
                }
            }
            else if (button.id == 20)
            {
                tileStructure.setIgnoresEntities(!tileStructure.ignoresEntities());
                updateEntitiesButton();
            }
            else if (button.id == 22)
            {
                tileStructure.setShowAir(!tileStructure.showsAir());
                updateToggleAirButton();
            }
            else if (button.id == 23)
            {
                tileStructure.setShowBoundingBox(!tileStructure.showsBoundingBox());
                updateToggleBoundingBox();
            }
            else if (button.id == 21)
            {
                switch (tileStructure.getMirror())
                {
                    case NONE:
                        tileStructure.setMirror(Mirror.LEFT_RIGHT);
                        break;

                    case LEFT_RIGHT:
                        tileStructure.setMirror(Mirror.FRONT_BACK);
                        break;

                    case FRONT_BACK:
                        tileStructure.setMirror(Mirror.NONE);
                }

                updateMirrorButton();
            }
        }
    }

    private void updateEntitiesButton()
    {
        boolean flag = !tileStructure.ignoresEntities();

        if (flag)
        {
            showEntitiesButton.text = I18n.format("options.on");
        }
        else
        {
            showEntitiesButton.text = I18n.format("options.off");
        }
    }

    private void updateToggleAirButton()
    {
        boolean flag = tileStructure.showsAir();

        if (flag)
        {
            showAirButton.text = I18n.format("options.on");
        }
        else
        {
            showAirButton.text = I18n.format("options.off");
        }
    }

    private void updateToggleBoundingBox()
    {
        boolean flag = tileStructure.showsBoundingBox();

        if (flag)
        {
            showBoundingBoxButton.text = I18n.format("options.on");
        }
        else
        {
            showBoundingBoxButton.text = I18n.format("options.off");
        }
    }

    private void updateMirrorButton()
    {
        Mirror mirror = tileStructure.getMirror();

        switch (mirror)
        {
            case NONE:
                mirrorButton.text = "|";
                break;

            case LEFT_RIGHT:
                mirrorButton.text = "< >";
                break;

            case FRONT_BACK:
                mirrorButton.text = "^ v";
        }
    }

    private void updateDirectionButtons()
    {
        rotateZeroDegreesButton.enabled = true;
        rotateNinetyDegreesButton.enabled = true;
        rotate180DegreesButton.enabled = true;
        rotate270DegressButton.enabled = true;

        switch (tileStructure.getRotation())
        {
            case NONE:
                rotateZeroDegreesButton.enabled = false;
                break;

            case CLOCKWISE_180:
                rotate180DegreesButton.enabled = false;
                break;

            case COUNTERCLOCKWISE_90:
                rotate270DegressButton.enabled = false;
                break;

            case CLOCKWISE_90:
                rotateNinetyDegreesButton.enabled = false;
        }
    }

    private void updateMode()
    {
        nameEdit.setFocused(false);
        posXEdit.setFocused(false);
        posYEdit.setFocused(false);
        posZEdit.setFocused(false);
        sizeXEdit.setFocused(false);
        sizeYEdit.setFocused(false);
        sizeZEdit.setFocused(false);
        integrityEdit.setFocused(false);
        seedEdit.setFocused(false);
        dataEdit.setFocused(false);
        nameEdit.setVisible(false);
        nameEdit.setFocused(false);
        posXEdit.setVisible(false);
        posYEdit.setVisible(false);
        posZEdit.setVisible(false);
        sizeXEdit.setVisible(false);
        sizeYEdit.setVisible(false);
        sizeZEdit.setVisible(false);
        integrityEdit.setVisible(false);
        seedEdit.setVisible(false);
        dataEdit.setVisible(false);
        saveButton.visible = false;
        loadButton.visible = false;
        detectSizeButton.visible = false;
        showEntitiesButton.visible = false;
        mirrorButton.visible = false;
        rotateZeroDegreesButton.visible = false;
        rotateNinetyDegreesButton.visible = false;
        rotate180DegreesButton.visible = false;
        rotate270DegressButton.visible = false;
        showAirButton.visible = false;
        showBoundingBoxButton.visible = false;

        switch (tileStructure.getMode())
        {
            case SAVE:
                nameEdit.setVisible(true);
                nameEdit.setFocused(true);
                posXEdit.setVisible(true);
                posYEdit.setVisible(true);
                posZEdit.setVisible(true);
                sizeXEdit.setVisible(true);
                sizeYEdit.setVisible(true);
                sizeZEdit.setVisible(true);
                saveButton.visible = true;
                detectSizeButton.visible = true;
                showEntitiesButton.visible = true;
                showAirButton.visible = true;
                break;

            case LOAD:
                nameEdit.setVisible(true);
                nameEdit.setFocused(true);
                posXEdit.setVisible(true);
                posYEdit.setVisible(true);
                posZEdit.setVisible(true);
                integrityEdit.setVisible(true);
                seedEdit.setVisible(true);
                loadButton.visible = true;
                showEntitiesButton.visible = true;
                mirrorButton.visible = true;
                rotateZeroDegreesButton.visible = true;
                rotateNinetyDegreesButton.visible = true;
                rotate180DegreesButton.visible = true;
                rotate270DegressButton.visible = true;
                showBoundingBoxButton.visible = true;
                updateDirectionButtons();
                break;

            case CORNER:
                nameEdit.setVisible(true);
                nameEdit.setFocused(true);
                break;

            case DATA:
                dataEdit.setVisible(true);
                dataEdit.setFocused(true);
        }

        modeButton.text = I18n.format("structure_block.mode." + tileStructure.getMode().getName());
    }

    private boolean sendToServer(int p_189820_1_)
    {
        try
        {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            tileStructure.writeCoordinates(packetbuffer);
            packetbuffer.writeByte(p_189820_1_);
            packetbuffer.writeString(tileStructure.getMode().toString());
            packetbuffer.writeString(nameEdit.getText());
            packetbuffer.writeInt(parseCoordinate(posXEdit.getText()));
            packetbuffer.writeInt(parseCoordinate(posYEdit.getText()));
            packetbuffer.writeInt(parseCoordinate(posZEdit.getText()));
            packetbuffer.writeInt(parseCoordinate(sizeXEdit.getText()));
            packetbuffer.writeInt(parseCoordinate(sizeYEdit.getText()));
            packetbuffer.writeInt(parseCoordinate(sizeZEdit.getText()));
            packetbuffer.writeString(tileStructure.getMirror().toString());
            packetbuffer.writeString(tileStructure.getRotation().toString());
            packetbuffer.writeString(dataEdit.getText());
            packetbuffer.writeBoolean(tileStructure.ignoresEntities());
            packetbuffer.writeBoolean(tileStructure.showsAir());
            packetbuffer.writeBoolean(tileStructure.showsBoundingBox());
            packetbuffer.writeFloat(parseIntegrity(integrityEdit.getText()));
            packetbuffer.writeVarLong(parseSeed(seedEdit.getText()));
            minecraft.getConnection().sendPacket(new CPacketCustomPayload("MC|Struct", packetbuffer));
            return true;
        }
        catch (Exception exception)
        {
            LOGGER.warn("Could not send structure block info", exception);
            return false;
        }
    }

    private long parseSeed(String p_189821_1_)
    {
        try
        {
            return Long.valueOf(p_189821_1_).longValue();
        }
        catch (NumberFormatException var3)
        {
            return 0L;
        }
    }

    private float parseIntegrity(String p_189819_1_)
    {
        try
        {
            return Float.valueOf(p_189819_1_).floatValue();
        }
        catch (NumberFormatException var3)
        {
            return 1.0F;
        }
    }

    private int parseCoordinate(String p_189817_1_)
    {
        try
        {
            return Integer.parseInt(p_189817_1_);
        }
        catch (NumberFormatException var3)
        {
            return 0;
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (nameEdit.getVisible() && isValidCharacterForName(c, key))
        {
            nameEdit.textboxKeyTyped(c, key);
        }

        if (posXEdit.getVisible())
        {
            posXEdit.textboxKeyTyped(c, key);
        }

        if (posYEdit.getVisible())
        {
            posYEdit.textboxKeyTyped(c, key);
        }

        if (posZEdit.getVisible())
        {
            posZEdit.textboxKeyTyped(c, key);
        }

        if (sizeXEdit.getVisible())
        {
            sizeXEdit.textboxKeyTyped(c, key);
        }

        if (sizeYEdit.getVisible())
        {
            sizeYEdit.textboxKeyTyped(c, key);
        }

        if (sizeZEdit.getVisible())
        {
            sizeZEdit.textboxKeyTyped(c, key);
        }

        if (integrityEdit.getVisible())
        {
            integrityEdit.textboxKeyTyped(c, key);
        }

        if (seedEdit.getVisible())
        {
            seedEdit.textboxKeyTyped(c, key);
        }

        if (dataEdit.getVisible())
        {
            dataEdit.textboxKeyTyped(c, key);
        }

        if (key == 15)
        {
            GuiTextField guitextfield = null;
            GuiTextField guitextfield1 = null;

            for (GuiTextField guitextfield2 : tabOrder)
            {
                if (guitextfield != null && guitextfield2.getVisible())
                {
                    guitextfield1 = guitextfield2;
                    break;
                }

                if (guitextfield2.isFocused() && guitextfield2.getVisible())
                {
                    guitextfield = guitextfield2;
                }
            }

            if (guitextfield != null && guitextfield1 == null)
            {
                for (GuiTextField guitextfield3 : tabOrder)
                {
                    if (guitextfield3.getVisible() && guitextfield3 != guitextfield)
                    {
                        guitextfield1 = guitextfield3;
                        break;
                    }
                }
            }

            if (guitextfield1 != null && guitextfield1 != guitextfield)
            {
                guitextfield.setFocused(false);
                guitextfield1.setFocused(true);
            }
        }

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

    private static boolean isValidCharacterForName(char p_190301_0_, int p_190301_1_)
    {
        boolean flag = true;

        for (int i : LEGAL_KEY_CODES)
        {
            if (i == p_190301_1_)
            {
                return true;
            }
        }

        for (char c0 : ChatAllowedCharacters.ILLEGAL_STRUCTURE_CHARACTERS)
        {
            if (c0 == p_190301_0_)
            {
                flag = false;
                break;
            }
        }

        return flag;
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);

        if (nameEdit.getVisible())
        {
            nameEdit.mouseClicked(mouseX, mouseY, button);
        }

        if (posXEdit.getVisible())
        {
            posXEdit.mouseClicked(mouseX, mouseY, button);
        }

        if (posYEdit.getVisible())
        {
            posYEdit.mouseClicked(mouseX, mouseY, button);
        }

        if (posZEdit.getVisible())
        {
            posZEdit.mouseClicked(mouseX, mouseY, button);
        }

        if (sizeXEdit.getVisible())
        {
            sizeXEdit.mouseClicked(mouseX, mouseY, button);
        }

        if (sizeYEdit.getVisible())
        {
            sizeYEdit.mouseClicked(mouseX, mouseY, button);
        }

        if (sizeZEdit.getVisible())
        {
            sizeZEdit.mouseClicked(mouseX, mouseY, button);
        }

        if (integrityEdit.getVisible())
        {
            integrityEdit.mouseClicked(mouseX, mouseY, button);
        }

        if (seedEdit.getVisible())
        {
            seedEdit.mouseClicked(mouseX, mouseY, button);
        }

        if (dataEdit.getVisible())
        {
            dataEdit.mouseClicked(mouseX, mouseY, button);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        TileEntityStructure.Mode tileentitystructure$mode = tileStructure.getMode();
        drawCenteredString(font, I18n.format("tile.structureBlock.name"), width / 2, 10, 16777215);

        if (tileentitystructure$mode != TileEntityStructure.Mode.DATA)
        {
            drawString(font, I18n.format("structure_block.structure_name"), width / 2 - 153, 30, 10526880);
            nameEdit.drawTextBox();
        }

        if (tileentitystructure$mode == TileEntityStructure.Mode.LOAD || tileentitystructure$mode == TileEntityStructure.Mode.SAVE)
        {
            drawString(font, I18n.format("structure_block.position"), width / 2 - 153, 70, 10526880);
            posXEdit.drawTextBox();
            posYEdit.drawTextBox();
            posZEdit.drawTextBox();
            String s = I18n.format("structure_block.include_entities");
            int i = font.getStringWidth(s);
            drawString(font, s, width / 2 + 154 - i, 150, 10526880);
        }

        if (tileentitystructure$mode == TileEntityStructure.Mode.SAVE)
        {
            drawString(font, I18n.format("structure_block.size"), width / 2 - 153, 110, 10526880);
            sizeXEdit.drawTextBox();
            sizeYEdit.drawTextBox();
            sizeZEdit.drawTextBox();
            String s2 = I18n.format("structure_block.detect_size");
            int k = font.getStringWidth(s2);
            drawString(font, s2, width / 2 + 154 - k, 110, 10526880);
            String s1 = I18n.format("structure_block.show_air");
            int j = font.getStringWidth(s1);
            drawString(font, s1, width / 2 + 154 - j, 70, 10526880);
        }

        if (tileentitystructure$mode == TileEntityStructure.Mode.LOAD)
        {
            drawString(font, I18n.format("structure_block.integrity"), width / 2 - 153, 110, 10526880);
            integrityEdit.drawTextBox();
            seedEdit.drawTextBox();
            String s3 = I18n.format("structure_block.show_boundingbox");
            int l = font.getStringWidth(s3);
            drawString(font, s3, width / 2 + 154 - l, 70, 10526880);
        }

        if (tileentitystructure$mode == TileEntityStructure.Mode.DATA)
        {
            drawString(font, I18n.format("structure_block.custom_data"), width / 2 - 153, 110, 10526880);
            dataEdit.drawTextBox();
        }

        String s4 = "structure_block.mode_info." + tileentitystructure$mode.getName();
        drawString(font, I18n.format(s4), width / 2 - 153, 174, 10526880);
        super.draw(mouseX, mouseY, partialTick);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean pauses()
    {
        return false;
    }
}
