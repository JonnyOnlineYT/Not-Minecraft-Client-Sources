package Celestial.event.events.impl.player;

import Celestial.event.events.Event;
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