package Celestial.event.events.impl.render;

import Celestial.event.events.callables.EventCancellable;
import net.minecraft.item.ItemStack;

public class EventRenderToolTip extends EventCancellable
{
    private final ItemStack stack;
    private final int x;
    private final int y;

    public EventRenderToolTip(ItemStack stack, int x, int y)
    {
        this.stack = stack;
        this.x = x;
        this.y = y;
    }

    public ItemStack getStack()
    {
        return this.stack;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }
}
