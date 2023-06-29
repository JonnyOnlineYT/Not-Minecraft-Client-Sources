package ru.wendoxd.events.impl.player;

import net.minecraft.item.Item;
import ru.wendoxd.events.Event;

public class EventCalculateCooldown extends Event {
	private Item stack;
	private float cooldown;

	public EventCalculateCooldown(Item stack) {
		this.stack = stack;
	}

	public float getCooldown() {
		return this.cooldown;
	}

	public void setCooldown(float f) {
		this.cooldown = f;
	}

	public Item getStack() {
		return this.stack;
	}
}
