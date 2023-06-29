package splash.api.event.events.network;

import me.hippo.systems.lwjeb.event.Cancelable;
import net.minecraft.network.Packet;

/**
 * Author: Ice
 * Created: 17:27, 30-May-20
 * Project: Client
 */
public class PacketReceiveEvent extends Cancelable {

    private Packet receivedPacket;

    public PacketReceiveEvent(Packet sentPacket) {
        this.receivedPacket = sentPacket;
    }

    public Packet getReceivedPacket() {
        return receivedPacket;
    }

    public void setReceivedPacket(Packet receivedPacket) {
        this.receivedPacket = receivedPacket;
    }
}
