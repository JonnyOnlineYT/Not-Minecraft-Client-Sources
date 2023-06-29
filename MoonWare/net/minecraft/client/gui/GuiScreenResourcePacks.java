package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.*;
import net.minecraft.util.OS;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class GuiScreenResourcePacks extends Screen
{
    private final Screen parentScreen;
    private List<ResourcePackListEntry> availableResourcePacks;
    private List<ResourcePackListEntry> selectedResourcePacks;

    /** List component that contains the available resource packs */
    private GuiResourcePackAvailable availableResourcePacksList;

    /** List component that contains the selected resource packs */
    private GuiResourcePackSelected selectedResourcePacksList;
    private boolean changed;

    public GuiScreenResourcePacks(Screen parentScreenIn)
    {
        parentScreen = parentScreenIn;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        widgets.add(new ButtonWidget(2, width / 2 - 154, height - 48, 150, 20, I18n.format("resourcePack.openFolder")));
        widgets.add(new ButtonWidget(1, width / 2 + 4, height - 48, 150, 2, I18n.format("gui.done")));

        if (!changed)
        {
            availableResourcePacks = Lists.newArrayList();
            selectedResourcePacks = Lists.newArrayList();
            ResourcePackRepository resourcepackrepository = Minecraft.getResourcePackRepository();
            resourcepackrepository.updateRepositoryEntriesAll();
            List<ResourcePackRepository.Entry> list = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
            list.removeAll(resourcepackrepository.getRepositoryEntries());

            for (ResourcePackRepository.Entry resourcepackrepository$entry : list)
            {
                availableResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry));
            }

            ResourcePackRepository.Entry resourcepackrepository$entry2 = resourcepackrepository.getResourcePackEntry();

            if (resourcepackrepository$entry2 != null)
            {
                selectedResourcePacks.add(new ResourcePackListEntryServer(this, resourcepackrepository.getResourcePackInstance()));
            }

            for (ResourcePackRepository.Entry resourcepackrepository$entry1 : Lists.reverse(resourcepackrepository.getRepositoryEntries()))
            {
                selectedResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
            }

            selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
        }

        availableResourcePacksList = new GuiResourcePackAvailable(minecraft, 200, height, availableResourcePacks);
        availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
        availableResourcePacksList.registerScrollButtons(7, 8);
        selectedResourcePacksList = new GuiResourcePackSelected(minecraft, 200, height, selectedResourcePacks);
        selectedResourcePacksList.setSlotXBoundsFromLeft(width / 2 + 4);
        selectedResourcePacksList.registerScrollButtons(7, 8);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        selectedResourcePacksList.handleMouseInput();
        availableResourcePacksList.handleMouseInput();
    }

    public boolean hasResourcePackEntry(ResourcePackListEntry p_146961_1_)
    {
        return selectedResourcePacks.contains(p_146961_1_);
    }

    public List<ResourcePackListEntry> getListContaining(ResourcePackListEntry p_146962_1_)
    {
        return hasResourcePackEntry(p_146962_1_) ? selectedResourcePacks : availableResourcePacks;
    }

    public List<ResourcePackListEntry> getAvailableResourcePacks()
    {
        return availableResourcePacks;
    }

    public List<ResourcePackListEntry> getSelectedResourcePacks()
    {
        return selectedResourcePacks;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button)
    {
        if (button.enabled)
        {
            if (button.id == 2)
            {
                File file1 = Minecraft.getResourcePackRepository().getDirResourcepacks();
                OS.openFile(file1);
            }
            else if (button.id == 1)
            {
                if (changed)
                {
                    List<ResourcePackRepository.Entry> list = Lists.newArrayList();

                    for (ResourcePackListEntry resourcepacklistentry : selectedResourcePacks)
                    {
                        if (resourcepacklistentry instanceof ResourcePackListEntryFound)
                        {
                            list.add(((ResourcePackListEntryFound)resourcepacklistentry).getResourcePackEntry());
                        }
                    }

                    Collections.reverse(list);
                    Minecraft.getResourcePackRepository().setRepositories(list);
                    Minecraft.gameSettings.resourcePacks.clear();
                    Minecraft.gameSettings.incompatibleResourcePacks.clear();

                    for (ResourcePackRepository.Entry resourcepackrepository$entry : list)
                    {
                        Minecraft.gameSettings.resourcePacks.add(resourcepackrepository$entry.getResourcePackName());

                        if (resourcepackrepository$entry.getPackFormat() != 3)
                        {
                            Minecraft.gameSettings.incompatibleResourcePacks.add(resourcepackrepository$entry.getResourcePackName());
                        }
                    }

                    Minecraft.gameSettings.saveOptions();
                    Minecraft.refreshResources();
                }

                Minecraft.openScreen(parentScreen);
            }
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        super.mousePressed(mouseX, mouseY, button);
        availableResourcePacksList.mouseClicked(mouseX, mouseY, button);
        selectedResourcePacksList.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Called when a mouse button is released.
     */
    public void mouseReleased(int mouseX, int mouseY, int button)
    {
        super.mouseReleased(mouseX, mouseY, button);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        drawDefaultBackground();
        availableResourcePacksList.drawScreen(mouseX, mouseY, partialTick);
        selectedResourcePacksList.drawScreenB(mouseX, mouseY, partialTick);
        drawCenteredString(font, I18n.format("resourcePack.title"), width / 2, 16, 16777215);
        drawCenteredString(font, I18n.format("resourcePack.folderInfo"), width / 2 - 77, height - 26, 8421504);
        super.draw(mouseX, mouseY, partialTick);
    }

    /**
     * Marks the selected resource packs list as changed to trigger a resource reload when the screen is closed
     */
    public void markChanged()
    {
        changed = true;
    }
}
