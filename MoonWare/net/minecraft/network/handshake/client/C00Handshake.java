package net.minecraft.network.handshake.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class C00Handshake implements Packet<INetHandlerHandshakeServer> {
    private int protocol;
    private String ip;
    private int port;
    private EnumConnectionState state;

    public C00Handshake(String ip, int port, EnumConnectionState state) {
        this(340, ip, port, state);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        protocol = buf.readVarIntFromBuffer();
        ip = buf.readStringFromBuffer(255);
        port = buf.readUnsignedShort();
        state = EnumConnectionState.getById(buf.readVarIntFromBuffer());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(protocol);
        buf.writeString(ip);
        buf.writeShort(port);
        buf.writeVarIntToBuffer(state.getId());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerHandshakeServer handler) {
        handler.processHandshake(this);
    }

    public EnumConnectionState getState() {
        return state;
    }

    public int getProtocol() {
        return protocol;
    }
}
