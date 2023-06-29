package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.util.text.Formatting;

public abstract class GuiResourcePackList extends GuiListExtended
{
    protected final Minecraft mc;
    protected final List<ResourcePackListEntry> resourcePackEntries;

    public GuiResourcePackList(Minecraft mcIn, int p_i45055_2_, int p_i45055_3_, List<ResourcePackListEntry> p_i45055_4_)
    {
        super(mcIn, p_i45055_2_, p_i45055_3_, 32, p_i45055_3_ - 55 + 4, 36);
        mc = mcIn;
        resourcePackEntries = p_i45055_4_;
        centerListVertically = false;
        setHasListHeader(true, (int)((float) Minecraft.font.height * 1.5F));
    }

    /**
     * Handles drawing a list's header row.
     */
    protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellatorIn)
    {
        String s = Formatting.UNDERLINE + "" + Formatting.BOLD + getListHeader();
        Minecraft.font.drawString(s, insideLeft + width / 2 - Minecraft.font.getStringWidth(s) / 2, Math.min(top + 3, insideTop), 16777215);
    }

    protected abstract String getListHeader();

    public List<ResourcePackListEntry> getList()
    {
        return resourcePackEntries;
    }

    protected int getSize()
    {
        return getList().size();
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public ResourcePackListEntry getListEntry(int index)
    {
        return getList().get(index);
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return width;
    }

    protected int getScrollBarX()
    {
        return right - 6;
    }
}
