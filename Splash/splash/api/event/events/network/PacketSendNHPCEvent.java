package splash.api.event.events.network;

import me.hippo.systems.lwjeb.event.Cancelable;
import net.minecraft.network.Packet;

/**
 * Author: Ice
 * Created: 17:27, 30-May-20
 * Project: Client
 */
public class PacketSendNHPCEvent extends Cancelable {

    private Packet sentPacket;

    public PacketSendNHPCEvent(Packet sentPacket) {
        this.sentPacket = sentPacket;
    }

    public Packet getSentPacket() {
        return sentPacket;
    }

    public void setSentPacket(Packet sentPacket) {
        this.sentPacket = sentPacket;
    }
}
