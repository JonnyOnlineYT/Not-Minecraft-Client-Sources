package shadersmod.client;

import java.util.ArrayList;
import net.minecraft.client.gui.GuiSlot;
import optifine.Lang;

class GuiSlotShaders extends GuiSlot
{
    private ArrayList shaderslist;
    private int selectedIndex;
    private long lastClickedCached;
    final GuiShaders shadersGui;

    public GuiSlotShaders(GuiShaders par1GuiShaders, int width, int height, int top, int bottom, int slotHeight)
    {
        super(par1GuiShaders.getMc(), width, height, top, bottom, slotHeight);
        shadersGui = par1GuiShaders;
        updateList();
        amountScrolled = 0.0F;
        int i = selectedIndex * slotHeight;
        int j = (bottom - top) / 2;

        if (i > j)
        {
            scrollBy(i - j);
        }
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return width - 20;
    }

    public void updateList()
    {
        shaderslist = Shaders.listOfShaders();
        selectedIndex = 0;
        int i = 0;

        for (int j = shaderslist.size(); i < j; ++i)
        {
            if (shaderslist.get(i).equals(Shaders.currentshadername))
            {
                selectedIndex = i;
                break;
            }
        }
    }

    protected int getSize()
    {
        return shaderslist.size();
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected void elementClicked(int i, boolean doubleClick, int mouseX, int mouseY)
    {
        if (i != selectedIndex || lastClicked != lastClickedCached)
        {
            selectedIndex = i;
            lastClickedCached = lastClicked;
            Shaders.setShaderPack((String) shaderslist.get(i));
            Shaders.uninit();
            shadersGui.updateButtons();
        }
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int index)
    {
        return index == selectedIndex;
    }

    protected int getScrollBarX()
    {
        return width - 6;
    }

    /**
     * Return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return getSize() * 18;
    }

    protected void drawBackground()
    {
    }

    protected void func_192637_a(int index, int posX, int posY, int contentY, int mouseX, int mouseY, float partialTicks)
    {
        String s = (String) shaderslist.get(index);

        if (s.equals(Shaders.packNameNone))
        {
            s = Lang.get("of.options.shaders.packNone");
        }
        else if (s.equals(Shaders.packNameDefault))
        {
            s = Lang.get("of.options.shaders.packDefault");
        }

        shadersGui.drawCenteredString(s, width / 2, posY + 1, 16777215);
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }
}
