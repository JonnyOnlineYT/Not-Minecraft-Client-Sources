package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.network.play.server.SPacketPlayerListItem;

public class EventPlayerList implements Event {

    private final SPacketPlayerListItem.Action action;
    private final SPacketPlayerListItem.AddPlayerData data;

    public EventPlayerList(SPacketPlayerListItem.Action action, SPacketPlayerListItem.AddPlayerData data) {
        this.action = action;
        this.data = data;
    }

    public SPacketPlayerListItem.Action getAction() {
        return action;
    }

    public SPacketPlayerListItem.AddPlayerData getData() {
        return data;
    }
}