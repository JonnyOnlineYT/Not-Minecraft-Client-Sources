package org.moonware.client.event.events.callables;

import net.minecraft.item.ItemStack;

public class EventRenderToolTip
        extends EventCancellable {
    private final ItemStack stack;
    private final int x;
    private final int y;

    public EventRenderToolTip(ItemStack stack, int x, int y) {
        this.stack = stack;
        this.x = x;
        this.y = y;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
