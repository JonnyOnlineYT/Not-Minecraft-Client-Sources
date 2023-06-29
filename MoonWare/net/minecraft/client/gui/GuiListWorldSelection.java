package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.AlertScreen;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiListWorldSelection extends GuiListExtended
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final GuiWorldSelection worldSelectionObj;
    private final List<GuiListWorldSelectionEntry> entries = Lists.newArrayList();

    /** Index to the currently selected world */
    private int selectedIdx = -1;

    public GuiListWorldSelection(GuiWorldSelection p_i46590_1_, Minecraft clientIn, int p_i46590_3_, int p_i46590_4_, int p_i46590_5_, int p_i46590_6_, int p_i46590_7_)
    {
        super(clientIn, p_i46590_3_, p_i46590_4_, p_i46590_5_, p_i46590_6_, p_i46590_7_);
        worldSelectionObj = p_i46590_1_;
        refreshList();
    }

    public void refreshList()
    {
        ISaveFormat isaveformat = mc.getSaveLoader();
        List<WorldSummary> list;

        try
        {
            list = isaveformat.getSaveList();
        }
        catch (AnvilConverterException anvilconverterexception)
        {
            LOGGER.error("Couldn't load level list", anvilconverterexception);
            Minecraft.openScreen(new AlertScreen(new TranslatableComponent("selectWorld.unable_to_load"), new TextComponent(anvilconverterexception.getMessage()), new TranslatableComponent("gui.done"), () -> Minecraft.openScreen(null)));
            return;
        }

        Collections.sort(list);

        for (WorldSummary worldsummary : list)
        {
            entries.add(new GuiListWorldSelectionEntry(this, worldsummary, mc.getSaveLoader()));
        }
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public GuiListWorldSelectionEntry getListEntry(int index)
    {
        return entries.get(index);
    }

    protected int getSize()
    {
        return entries.size();
    }

    protected int getScrollBarX()
    {
        return super.getScrollBarX() + 20;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return super.getListWidth() + 50;
    }

    public void selectWorld(int idx)
    {
        selectedIdx = idx;
        worldSelectionObj.selectWorld(getSelectedWorld());
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int slotIndex)
    {
        return slotIndex == selectedIdx;
    }

    @Nullable
    public GuiListWorldSelectionEntry getSelectedWorld()
    {
        return selectedIdx >= 0 && selectedIdx < getSize() ? getListEntry(selectedIdx) : null;
    }

    public GuiWorldSelection getGuiWorldSelection()
    {
        return worldSelectionObj;
    }
}
