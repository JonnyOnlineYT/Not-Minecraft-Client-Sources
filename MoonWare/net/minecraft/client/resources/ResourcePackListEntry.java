package net.minecraft.client.resources;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;

public abstract class ResourcePackListEntry implements GuiListExtended.IGuiListEntry
{
    private static final Namespaced RESOURCE_PACKS_TEXTURE = new Namespaced("textures/gui/resource_packs.png");
    private static final Component INCOMPATIBLE = new TranslatableComponent("resourcePack.incompatible");
    private static final Component INCOMPATIBLE_OLD = new TranslatableComponent("resourcePack.incompatible.old");
    private static final Component INCOMPATIBLE_NEW = new TranslatableComponent("resourcePack.incompatible.new");
    protected final Minecraft mc;
    protected final GuiScreenResourcePacks resourcePacksGUI;

    public ResourcePackListEntry(GuiScreenResourcePacks resourcePacksGUIIn)
    {
        resourcePacksGUI = resourcePacksGUIIn;
        mc = Minecraft.getMinecraft();
    }

    public void draw(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_)
    {
        int i = getResourcePackFormat();

        if (i != 3)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawRect(p_192634_2_ - 1, p_192634_3_ - 1, p_192634_2_ + p_192634_4_ - 9, p_192634_3_ + p_192634_5_ + 1, -8978432);
        }

        bindResourcePackIcon();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        String s = getResourcePackName();
        String s1 = getResourcePackDescription();

        if (showHoverOverlay() && p_192634_8_)
        {
            Minecraft.getTextureManager().bindTexture(RESOURCE_PACKS_TEXTURE);
            Gui.drawRect(p_192634_2_, p_192634_3_, p_192634_2_ + 32, p_192634_3_ + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int j = p_192634_6_ - p_192634_2_;
            int k = p_192634_7_ - p_192634_3_;

            if (i < 3)
            {
                s = INCOMPATIBLE.asFormattedString();
                s1 = INCOMPATIBLE_OLD.asFormattedString();
            }
            else if (i > 3)
            {
                s = INCOMPATIBLE.asFormattedString();
                s1 = INCOMPATIBLE_NEW.asFormattedString();
            }

            if (canMoveRight())
            {
                if (j < 32)
                {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
            else
            {
                if (canMoveLeft())
                {
                    if (j < 16)
                    {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 32.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    }
                    else
                    {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 32.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (canMoveUp())
                {
                    if (j < 32 && j > 16 && k < 16)
                    {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    }
                    else
                    {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (canMoveDown())
                {
                    if (j < 32 && j > 16 && k > 16)
                    {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    }
                    else
                    {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }
            }
        }

        int i1 = Minecraft.font.getStringWidth(s);

        if (i1 > 157)
        {
            s = Minecraft.font.trimStringToWidth(s, 157 - Minecraft.font.getStringWidth("...")) + "...";
        }

        Minecraft.font.drawStringWithShadow(s, (float)(p_192634_2_ + 32 + 2), (float)(p_192634_3_ + 1), 16777215);
        List<String> list = Minecraft.font.split(s1, 157);

        for (int l = 0; l < 2 && l < list.size(); ++l)
        {
            Minecraft.font.drawStringWithShadow(list.get(l), (float)(p_192634_2_ + 32 + 2), (float)(p_192634_3_ + 12 + 10 * l), 8421504);
        }
    }

    protected abstract int getResourcePackFormat();

    protected abstract String getResourcePackDescription();

    protected abstract String getResourcePackName();

    protected abstract void bindResourcePackIcon();

    protected boolean showHoverOverlay()
    {
        return true;
    }

    protected boolean canMoveRight()
    {
        return !resourcePacksGUI.hasResourcePackEntry(this);
    }

    protected boolean canMoveLeft()
    {
        return resourcePacksGUI.hasResourcePackEntry(this);
    }

    protected boolean canMoveUp()
    {
        List<ResourcePackListEntry> list = resourcePacksGUI.getListContaining(this);
        int i = list.indexOf(this);
        return i > 0 && list.get(i - 1).showHoverOverlay();
    }

    protected boolean canMoveDown()
    {
        List<ResourcePackListEntry> list = resourcePacksGUI.getListContaining(this);
        int i = list.indexOf(this);
        return i >= 0 && i < list.size() - 1 && list.get(i + 1).showHoverOverlay();
    }

    /**
     * Called when the mouse is clicked within this entry. Returning true means that something within this entry was
     * clicked and the list should not be dragged.
     */
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
    {
        if (showHoverOverlay() && relativeX <= 32)
        {
            if (canMoveRight())
            {
                resourcePacksGUI.markChanged();
                int j = resourcePacksGUI.getSelectedResourcePacks().get(0).isServerPack() ? 1 : 0;
                int l = getResourcePackFormat();

                if (l == 3)
                {
                    resourcePacksGUI.getListContaining(this).remove(this);
                    resourcePacksGUI.getSelectedResourcePacks().add(j, this);
                }
                else
                {
                    String s = I18n.format("resourcePack.incompatible.confirm.title");
                    String s1 = I18n.format("resourcePack.incompatible.confirm." + (l > 3 ? "new" : "old"));
                    Minecraft.openScreen(new ConfirmScreen(b -> {
                        List<ResourcePackListEntry> list2 = resourcePacksGUI.getListContaining(this);
                        Minecraft.openScreen(resourcePacksGUI);
                        if (b)
                        {
                            list2.remove(this);
                            resourcePacksGUI.getSelectedResourcePacks().add(j, this);
                        }
                    }, s, s1));
                }

                return true;
            }

            if (relativeX < 16 && canMoveLeft())
            {
                resourcePacksGUI.getListContaining(this).remove(this);
                resourcePacksGUI.getAvailableResourcePacks().add(0, this);
                resourcePacksGUI.markChanged();
                return true;
            }

            if (relativeX > 16 && relativeY < 16 && canMoveUp())
            {
                List<ResourcePackListEntry> list1 = resourcePacksGUI.getListContaining(this);
                int k = list1.indexOf(this);
                list1.remove(this);
                list1.add(k - 1, this);
                resourcePacksGUI.markChanged();
                return true;
            }

            if (relativeX > 16 && relativeY > 16 && canMoveDown())
            {
                List<ResourcePackListEntry> list = resourcePacksGUI.getListContaining(this);
                int i = list.indexOf(this);
                list.remove(this);
                list.add(i + 1, this);
                resourcePacksGUI.markChanged();
                return true;
            }
        }

        return false;
    }

    public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_)
    {
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }

    public boolean isServerPack()
    {
        return false;
    }
}
