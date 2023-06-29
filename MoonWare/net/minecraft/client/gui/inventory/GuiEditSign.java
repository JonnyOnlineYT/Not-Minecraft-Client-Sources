package net.minecraft.client.gui.inventory;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponent;
import org.lwjgl.input.Keyboard;

public class GuiEditSign extends Screen
{
    /** Reference to the sign object. */
    private final TileEntitySign tileSign;

    /** Counts the number of screen updates. */
    private int updateCounter;

    /** The index of the line that is being edited. */
    private int editLine;

    /** "Done" button for the GUI. */
    private ButtonWidget doneBtn;

    public GuiEditSign(TileEntitySign teSign)
    {
        tileSign = teSign;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        widgets.clear();
        Keyboard.enableRepeatEvents(true);
        doneBtn = addButton(new ButtonWidget(0, width / 2 - 100, height / 4 + 120, I18n.format("gui.done")));
        tileSign.setEditable(false);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        Keyboard.enableRepeatEvents(false);
        NetHandlerPlayClient nethandlerplayclient = minecraft.getConnection();

        if (nethandlerplayclient != null)
        {
            nethandlerplayclient.sendPacket(new CPacketUpdateSign(tileSign.getPos(), tileSign.signText));
        }

        tileSign.setEditable(true);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update()
    {
        ++updateCounter;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                tileSign.markDirty();
                Minecraft.openScreen(null);
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (key == 200)
        {
            editLine = editLine - 1 & 3;
        }

        if (key == 208 || key == 28 || key == 156)
        {
            editLine = editLine + 1 & 3;
        }

        String s = tileSign.signText[editLine].asString();

        if (key == 14 && !s.isEmpty())
        {
            s = s.substring(0, s.length() - 1);
        }

        if (ChatAllowedCharacters.isAllowedCharacter(c) && font.getStringWidth(s + c) <= 90)
        {
            s = s + c;
        }

        tileSign.signText[editLine] = new TextComponent(s);

        if (key == 1)
        {
            actionPerformed(doneBtn);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        drawCenteredString(font, I18n.format("sign.edit"), width / 2, 40, 16777215);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(width / 2), 0.0F, 50.0F);
        float f = 93.75F;
        GlStateManager.scale(-93.75F, -93.75F, -93.75F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        Block block = tileSign.getBlockType();

        if (block == Blocks.STANDING_SIGN)
        {
            float f1 = (float)(tileSign.getBlockMetadata() * 360) / 16.0F;
            GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -1.0625F, 0.0F);
        }
        else
        {
            int i = tileSign.getBlockMetadata();
            float f2 = 0.0F;

            if (i == 2)
            {
                f2 = 180.0F;
            }

            if (i == 4)
            {
                f2 = 90.0F;
            }

            if (i == 5)
            {
                f2 = -90.0F;
            }

            GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -1.0625F, 0.0F);
        }

        if (updateCounter / 6 % 2 == 0)
        {
            tileSign.lineBeingEdited = editLine;
        }

        TileEntityRendererDispatcher.instance.renderTileEntityAt(tileSign, -0.5D, -0.75D, -0.5D, 0.0F);
        tileSign.lineBeingEdited = -1;
        GlStateManager.popMatrix();
        super.draw(mouseX, mouseY, partialTick);
    }
}
