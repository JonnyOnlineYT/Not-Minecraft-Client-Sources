package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;

public class GuiLockIconButton extends ButtonWidget
{
    private boolean locked;

    public GuiLockIconButton(int p_i45538_1_, int p_i45538_2_, int p_i45538_3_)
    {
        super(p_i45538_1_, p_i45538_2_, p_i45538_3_, 20, 20, "");
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(boolean lockedIn)
    {
        locked = lockedIn;
    }

    public void draw(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float partialTick)
    {
        if (visible)
        {
            Minecraft.getTextureManager().bindTexture(ButtonWidget.BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = p_191745_2_ >= x && p_191745_3_ >= y && p_191745_2_ < x + width && p_191745_3_ < y + height;
            GuiLockIconButton.Icon guilockiconbutton$icon;

            if (locked)
            {
                if (!enabled)
                {
                    guilockiconbutton$icon = GuiLockIconButton.Icon.LOCKED_DISABLED;
                }
                else if (flag)
                {
                    guilockiconbutton$icon = GuiLockIconButton.Icon.LOCKED_HOVER;
                }
                else
                {
                    guilockiconbutton$icon = GuiLockIconButton.Icon.LOCKED;
                }
            }
            else if (!enabled)
            {
                guilockiconbutton$icon = GuiLockIconButton.Icon.UNLOCKED_DISABLED;
            }
            else if (flag)
            {
                guilockiconbutton$icon = GuiLockIconButton.Icon.UNLOCKED_HOVER;
            }
            else
            {
                guilockiconbutton$icon = GuiLockIconButton.Icon.UNLOCKED;
            }

            drawTexturedModalRect(x, y, guilockiconbutton$icon.getX(), guilockiconbutton$icon.getY(), width, height);
        }
    }

    enum Icon
    {
        LOCKED(0, 146),
        LOCKED_HOVER(0, 166),
        LOCKED_DISABLED(0, 186),
        UNLOCKED(20, 146),
        UNLOCKED_HOVER(20, 166),
        UNLOCKED_DISABLED(20, 186);

        private final int x;
        private final int y;

        Icon(int p_i45537_3_, int p_i45537_4_)
        {
            x = p_i45537_3_;
            y = p_i45537_4_;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }
    }
}
