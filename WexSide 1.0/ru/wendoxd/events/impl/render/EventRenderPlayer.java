package ru.wendoxd.events.impl.render;

import net.minecraft.entity.player.EntityPlayer;
import ru.wendoxd.events.Event;

public class EventRenderPlayer extends Event {
	private boolean canceled;
	private EntityPlayer player;
	private Runnable callback;

	public EventRenderPlayer(EntityPlayer player, Runnable callback) {
		this.player = player;
		this.callback = callback;
	}

	public EntityPlayer getPlayer() {
		return this.player;
	}

	public void draw() {
		callback.run();
	}

	public boolean isCanceled() {
		return this.canceled;
	}

	public void setCanceled() {

		this.canceled = true;
	}
}
