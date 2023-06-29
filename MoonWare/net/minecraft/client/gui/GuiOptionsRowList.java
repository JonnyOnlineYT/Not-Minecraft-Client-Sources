package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionsRowList extends GuiListExtended
{
    private final List<GuiOptionsRowList.Row> options = Lists.newArrayList();

    public GuiOptionsRowList(Minecraft mcIn, int p_i45015_2_, int p_i45015_3_, int p_i45015_4_, int p_i45015_5_, int p_i45015_6_, GameSettings.Options... p_i45015_7_)
    {
        super(mcIn, p_i45015_2_, p_i45015_3_, p_i45015_4_, p_i45015_5_, p_i45015_6_);
        centerListVertically = false;

        for (int i = 0; i < p_i45015_7_.length; i += 2)
        {
            GameSettings.Options gamesettings$options = p_i45015_7_[i];
            GameSettings.Options gamesettings$options1 = i < p_i45015_7_.length - 1 ? p_i45015_7_[i + 1] : null;
            ButtonWidget guibutton = createButton(mcIn, p_i45015_2_ / 2 - 155, 0, gamesettings$options);
            ButtonWidget guibutton1 = createButton(mcIn, p_i45015_2_ / 2 - 155 + 160, 0, gamesettings$options1);
            options.add(new GuiOptionsRowList.Row(guibutton, guibutton1));
        }
    }

    private ButtonWidget createButton(Minecraft mcIn, int p_148182_2_, int p_148182_3_, GameSettings.Options options)
    {
        if (options == null)
        {
            return null;
        }
        else
        {
            int i = options.returnEnumOrdinal();
            return options.getEnumFloat() ? new GuiOptionSlider(i, p_148182_2_, p_148182_3_, options) : new GuiOptionButton(i, p_148182_2_, p_148182_3_, options, Minecraft.gameSettings.getKeyBinding(options));
        }
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public GuiOptionsRowList.Row getListEntry(int index)
    {
        return options.get(index);
    }

    protected int getSize()
    {
        return options.size();
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return 400;
    }

    protected int getScrollBarX()
    {
        return super.getScrollBarX() + 32;
    }

    public static class Row implements GuiListExtended.IGuiListEntry
    {
        private final Minecraft client = Minecraft.getMinecraft();
        private final ButtonWidget buttonA;
        private final ButtonWidget buttonB;

        public Row(ButtonWidget buttonAIn, ButtonWidget buttonBIn)
        {
            buttonA = buttonAIn;
            buttonB = buttonBIn;
        }

        public void draw(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_)
        {
            if (buttonA != null)
            {
                buttonA.y = p_192634_3_;
                buttonA.draw(client, p_192634_6_, p_192634_7_, p_192634_9_);
            }

            if (buttonB != null)
            {
                buttonB.y = p_192634_3_;
                buttonB.draw(client, p_192634_6_, p_192634_7_, p_192634_9_);
            }
        }

        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
        {
            if (buttonA.mousePressed(client, mouseX, mouseY))
            {
                if (buttonA instanceof GuiOptionButton)
                {
                    Minecraft.gameSettings.setOptionValue(((GuiOptionButton) buttonA).getSetting(), 1);
                    buttonA.text = Minecraft.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(buttonA.id));
                }

                return true;
            }
            else if (buttonB != null && buttonB.mousePressed(client, mouseX, mouseY))
            {
                if (buttonB instanceof GuiOptionButton)
                {
                    Minecraft.gameSettings.setOptionValue(((GuiOptionButton) buttonB).getSetting(), 1);
                    buttonB.text = Minecraft.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(buttonB.id));
                }

                return true;
            }
            else
            {
                return false;
            }
        }

        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (buttonA != null)
            {
                buttonA.mouseReleased(x, y);
            }

            if (buttonB != null)
            {
                buttonB.mouseReleased(x, y);
            }
        }

        public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_)
        {
        }
    }
}
