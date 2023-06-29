package ru.wendoxd.events.impl.player;

import net.minecraft.network.play.server.SPacketPlayerListItem.Action;
import net.minecraft.network.play.server.SPacketPlayerListItem.AddPlayerData;
import ru.wendoxd.events.Event;

public class EventPacketPlayerList extends Event {
	private AddPlayerData addPlayerData;
	private Action action;

	public EventPacketPlayerList(AddPlayerData addPlayerData, Action action) {
		this.addPlayerData = addPlayerData;
		this.action = action;
	}

	public AddPlayerData getPlayerData() {
		return this.addPlayerData;
	}

	public Action getAction() {
		return this.action;
	}
}
