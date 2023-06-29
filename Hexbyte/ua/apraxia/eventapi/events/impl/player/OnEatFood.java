package ua.apraxia.eventapi.events.impl.player;

import ua.apraxia.eventapi.events.Event;
import net.minecraft.item.ItemStack;

public class OnEatFood implements Event {
    private ItemStack item;

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}