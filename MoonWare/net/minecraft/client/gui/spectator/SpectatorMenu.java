package net.minecraft.client.gui.spectator;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;

public class SpectatorMenu
{
    private static final ISpectatorMenuObject CLOSE_ITEM = new SpectatorMenu.EndSpectatorObject();
    private static final ISpectatorMenuObject SCROLL_LEFT = new SpectatorMenu.MoveMenuObject(-1, true);
    private static final ISpectatorMenuObject SCROLL_RIGHT_ENABLED = new SpectatorMenu.MoveMenuObject(1, true);
    private static final ISpectatorMenuObject SCROLL_RIGHT_DISABLED = new SpectatorMenu.MoveMenuObject(1, false);
    public static final ISpectatorMenuObject EMPTY_SLOT = new ISpectatorMenuObject()
    {
        public void selectItem(SpectatorMenu menu)
        {
        }
        public Component getSpectatorName()
        {
            return new TextComponent("");
        }
        public void renderIcon(float p_178663_1_, int alpha)
        {
        }
        public boolean isEnabled()
        {
            return false;
        }
    };
    private final ISpectatorMenuRecipient listener;
    private final List<SpectatorDetails> previousCategories = Lists.newArrayList();
    private ISpectatorMenuView category = new BaseSpectatorGroup();
    private int selectedSlot = -1;
    private int page;

    public SpectatorMenu(ISpectatorMenuRecipient p_i45497_1_)
    {
        listener = p_i45497_1_;
    }

    public ISpectatorMenuObject getItem(int p_178643_1_)
    {
        int i = p_178643_1_ + page * 6;

        if (page > 0 && p_178643_1_ == 0)
        {
            return SCROLL_LEFT;
        }
        else if (p_178643_1_ == 7)
        {
            return i < category.getItems().size() ? SCROLL_RIGHT_ENABLED : SCROLL_RIGHT_DISABLED;
        }
        else if (p_178643_1_ == 8)
        {
            return CLOSE_ITEM;
        }
        else
        {
            return i >= 0 && i < category.getItems().size() ? MoreObjects.firstNonNull(category.getItems().get(i), EMPTY_SLOT) : EMPTY_SLOT;
        }
    }

    public List<ISpectatorMenuObject> getItems()
    {
        List<ISpectatorMenuObject> list = Lists.newArrayList();

        for (int i = 0; i <= 8; ++i)
        {
            list.add(getItem(i));
        }

        return list;
    }

    public ISpectatorMenuObject getSelectedItem()
    {
        return getItem(selectedSlot);
    }

    public ISpectatorMenuView getSelectedCategory()
    {
        return category;
    }

    public void selectSlot(int slotIn)
    {
        ISpectatorMenuObject ispectatormenuobject = getItem(slotIn);

        if (ispectatormenuobject != EMPTY_SLOT)
        {
            if (selectedSlot == slotIn && ispectatormenuobject.isEnabled())
            {
                ispectatormenuobject.selectItem(this);
            }
            else
            {
                selectedSlot = slotIn;
            }
        }
    }

    public void exit()
    {
        listener.onSpectatorMenuClosed(this);
    }

    public int getSelectedSlot()
    {
        return selectedSlot;
    }

    public void selectCategory(ISpectatorMenuView menuView)
    {
        previousCategories.add(getCurrentPage());
        category = menuView;
        selectedSlot = -1;
        page = 0;
    }

    public SpectatorDetails getCurrentPage()
    {
        return new SpectatorDetails(category, getItems(), selectedSlot);
    }

    static class EndSpectatorObject implements ISpectatorMenuObject
    {
        private EndSpectatorObject()
        {
        }

        public void selectItem(SpectatorMenu menu)
        {
            menu.exit();
        }

        public Component getSpectatorName()
        {
            return new TranslatableComponent("spectatorMenu.close");
        }

        public void renderIcon(float p_178663_1_, int alpha)
        {
            Minecraft.getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 128.0F, 0.0F, 16, 16, 256.0F, 256.0F);
        }

        public boolean isEnabled()
        {
            return true;
        }
    }

    static class MoveMenuObject implements ISpectatorMenuObject
    {
        private final int direction;
        private final boolean enabled;

        public MoveMenuObject(int p_i45495_1_, boolean p_i45495_2_)
        {
            direction = p_i45495_1_;
            enabled = p_i45495_2_;
        }

        public void selectItem(SpectatorMenu menu)
        {
            menu.page = menu.page + direction;
        }

        public Component getSpectatorName()
        {
            return direction < 0 ? new TranslatableComponent("spectatorMenu.previous_page") : new TranslatableComponent("spectatorMenu.next_page");
        }

        public void renderIcon(float p_178663_1_, int alpha)
        {
            Minecraft.getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);

            if (direction < 0)
            {
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 144.0F, 0.0F, 16, 16, 256.0F, 256.0F);
            }
            else
            {
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 160.0F, 0.0F, 16, 16, 256.0F, 256.0F);
            }
        }

        public boolean isEnabled()
        {
            return enabled;
        }
    }
}
