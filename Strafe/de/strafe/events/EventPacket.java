package de.strafe.events;

import com.eventapi.events.callables.EventCancellable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

/**
 * @author XButtonn
 * @created 23.02.2022 - 12:40
 */

@AllArgsConstructor
public class EventPacket extends EventCancellable {
    private final Action action;

    @Getter
    @Setter
    private Packet packet;

    public boolean isSend() {
        return this.action == Action.SEND;
    }

    public boolean isReceive() {
        return this.action == Action.RECEIVE;
    }

    public enum Action {
        SEND, RECEIVE
    }
}
