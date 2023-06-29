package ru.wendoxd.events.impl.packet;

import net.minecraft.network.Packet;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventSendPacket extends Event implements IEventCancelable {

    public Packet<?> packet;
    private boolean canceled;

    public EventSendPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    @Override
    public void setCanceled() {
        canceled = true;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }
}
