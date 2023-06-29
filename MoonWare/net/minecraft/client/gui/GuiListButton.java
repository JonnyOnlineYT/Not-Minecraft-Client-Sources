package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;

public class GuiListButton extends ButtonWidget
{
    private boolean value;

    /** The localization string used by this control. */
    private final String localizationStr;

    /** The GuiResponder Object reference. */
    private final GuiPageButtonList.GuiResponder guiResponder;

    public GuiListButton(GuiPageButtonList.GuiResponder responder, int p_i45539_2_, int p_i45539_3_, int p_i45539_4_, String p_i45539_5_, boolean p_i45539_6_)
    {
        super(p_i45539_2_, p_i45539_3_, p_i45539_4_, 150, 20, "");
        localizationStr = p_i45539_5_;
        value = p_i45539_6_;
        text = buildDisplayString();
        guiResponder = responder;
    }

    /**
     * Builds the localized display string for this GuiListButton
     */
    private String buildDisplayString()
    {
        return I18n.format(localizationStr) + ": " + I18n.format(value ? "gui.yes" : "gui.no");
    }

    public void setValue(boolean p_175212_1_)
    {
        value = p_175212_1_;
        text = buildDisplayString();
        guiResponder.setEntryValue(id, p_175212_1_);
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            value = !value;
            text = buildDisplayString();
            guiResponder.setEntryValue(id, value);
            return true;
        }
        else
        {
            return false;
        }
    }
}
