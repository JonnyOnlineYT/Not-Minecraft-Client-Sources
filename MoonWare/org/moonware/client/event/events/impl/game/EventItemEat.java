package org.moonware.client.event.events.impl.game;

import net.minecraft.item.ItemStack;
import org.moonware.client.event.events.callables.EventCancellable;

public class EventItemEat extends EventCancellable {
    public EventItemEat(ItemStack stack) {itemStack = stack;}
    public ItemStack itemStack = null;
}
