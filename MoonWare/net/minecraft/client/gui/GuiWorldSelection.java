package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClientButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.utils.MWFont;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.ui.titlescreen.TitleLikeScreen;

import javax.annotation.Nullable;
import java.awt.*;

public class GuiWorldSelection extends TitleLikeScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private String worldVersTooltip;
    private ButtonWidget deleteButton;
    private ButtonWidget selectButton;
    private ButtonWidget renameButton;
    private ButtonWidget copyButton;
    private GuiListWorldSelection selectionList;

    public GuiWorldSelection() {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init() {
        super.init();
        selectionList = new GuiListWorldSelection(this, minecraft, width, height, 32, height - 64, 36);
        ScaledResolution sr = new ScaledResolution(minecraft);
        int one = height - height + 75 - 27;
        int two = height - height + 75;
        int three = height - height + 102;
        int four = height - height + 129;
        int five = height - height + 129 + 27;
        selectButton = addButton(new ClientButtonWidget(1, sr.getScaledWidth() - 142 + 7, two, 128, 20, I18n.format("selectWorld.select")));
        addButton(new ClientButtonWidget(3, sr.getScaledWidth() - 142 + 7, one, 128, 20, I18n.format("selectWorld.create")));
        renameButton = addButton(new ClientButtonWidget(4, sr.getScaledWidth() - 142 + 7, four, 128, 20, I18n.format("selectWorld.edit")));
        deleteButton = addButton(new ClientButtonWidget(2, sr.getScaledWidth() - 142 + 7, five, 128, 20, I18n.format("selectWorld.delete")));
        copyButton = addButton(new ClientButtonWidget(5, sr.getScaledWidth() - 142 + 7, three, 128, 20, I18n.format("selectWorld.recreate")));
        selectButton.enabled = false;
        deleteButton.enabled = false;
        renameButton.enabled = false;
        copyButton.enabled = false;
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() {
        super.handleMouseInput();
        selectionList.handleMouseInput();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button) {
        if (button.enabled) {
            GuiListWorldSelectionEntry guilistworldselectionentry = selectionList.getSelectedWorld();

            if (button.id == 2) {
                if (guilistworldselectionentry != null) {
                    guilistworldselectionentry.deleteWorld();
                }
            } else if (button.id == 1) {
                if (guilistworldselectionentry != null) {
                    guilistworldselectionentry.joinWorld();
                }
            } else if (button.id == 3) {
                Minecraft.openScreen(new GuiCreateWorld(this));
            } else if (button.id == 4) {
                if (guilistworldselectionentry != null) {
                    guilistworldselectionentry.editWorld();
                }
            } else if (button.id == 5 && guilistworldselectionentry != null) {
                guilistworldselectionentry.recreateWorld();
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        drawTitle();
        worldVersTooltip = null;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        selectionList.drawScreenSinglePlayer(mouseX, mouseY, partialTick);
        RenderUtils2.drawBlur(15,() -> RoundedUtil.drawRound(sr.getScaledWidth() - 142, 24, 162, Minecraft.getScaledRoundedHeight(), 4, true, new Color(0, 0, 0, 90)));
        RenderUtils2.drawShadow(4,8,() -> RoundedUtil.drawRound(sr.getScaledWidth() - 142, 24, 162, Minecraft.getScaledRoundedHeight(), 4, true, new Color(0, 0, 0, 90)));

        RoundedUtil.drawRound(sr.getScaledWidth() - 142, 24, 162, Minecraft.getScaledRoundedHeight(), 4, true, new Color(0, 0, 0, 50));
        GL11.glPushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
        MWFont.MONTSERRAT_BOLD.get(30).drawCenterShadow("SinglePlayer", sr.getScaledWidth() - 78 + 9, 28f, -1);
        super.draw(mouseX, mouseY, partialTick);
        if (worldVersTooltip != null) {
            drawTooltip(Lists.newArrayList(Splitter.on("\n").split(worldVersTooltip)), mouseX, mouseY);
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button) {
        super.mousePressed(mouseX, mouseY, button);
        selectionList.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Called when a mouse button is released.
     */
    public void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        selectionList.mouseReleased(mouseX, mouseY, button);
    }

    /**
     * Called back by selectionList when we call its drawScreen method, from ours.
     */
    public void setVersionTooltip(String p_184861_1_) {
        worldVersTooltip = p_184861_1_;
    }

    public void selectWorld(@Nullable GuiListWorldSelectionEntry entry) {
        boolean flag = entry != null;
        selectButton.enabled = flag;
        deleteButton.enabled = flag;
        renameButton.enabled = flag;
        copyButton.enabled = flag;
    }
}
