package splash.client.events.network;

import me.hippo.systems.lwjeb.event.Cancelable;
import net.minecraft.network.Packet;

/**
 * Author: Ice
 * Created: 17:27, 30-May-20
 * Project: Client
 */
public class EventPacketSend extends Cancelable {

    private Packet sentPacket;

    public EventPacketSend(Packet sentPacket) {
        this.sentPacket = sentPacket;
    }

    public Packet getSentPacket() {
        return sentPacket;
    }

    public void setSentPacket(Packet sentPacket) {
        this.sentPacket = sentPacket;
    }
}
