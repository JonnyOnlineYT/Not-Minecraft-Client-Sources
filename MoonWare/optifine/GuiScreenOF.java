package optifine;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.GuiVideoSettings;

public class GuiScreenOF extends Screen
{
    protected void actionPerformedRightClick(ButtonWidget p_actionPerformedRightClick_1_)
    {
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);

        if (button == 1)
        {
            ButtonWidget guibutton = getSelectedButton(widgets, mouseX, mouseY);

            if (guibutton != null && guibutton.enabled)
            {
                guibutton.playPressSound(Minecraft.getSoundHandler());
                actionPerformedRightClick(guibutton);
            }
        }
    }

    public static ButtonWidget getSelectedButton(List<ButtonWidget> p_getSelectedButton_0_, int p_getSelectedButton_1_, int p_getSelectedButton_2_)
    {
        for (int i = 0; i < p_getSelectedButton_0_.size(); ++i)
        {
            ButtonWidget guibutton = p_getSelectedButton_0_.get(i);

            if (guibutton.visible)
            {
                int j = GuiVideoSettings.getButtonWidth(guibutton);
                int k = GuiVideoSettings.getButtonHeight(guibutton);

                if (p_getSelectedButton_1_ >= guibutton.x && p_getSelectedButton_2_ >= guibutton.y && p_getSelectedButton_1_ < guibutton.x + j && p_getSelectedButton_2_ < guibutton.y + k)
                {
                    return guibutton;
                }
            }
        }

        return null;
    }
}
