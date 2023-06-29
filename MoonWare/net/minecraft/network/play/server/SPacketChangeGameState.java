package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketChangeGameState implements Packet<INetHandlerPlayClient>
{
    public static final String[] MESSAGE_NAMES = {"tile.bed.notValid"};
    private int state;
    private float value;

    public SPacketChangeGameState()
    {
    }

    public SPacketChangeGameState(int stateIn, float valueIn)
    {
        state = stateIn;
        value = valueIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        state = buf.readUnsignedByte();
        value = buf.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(state);
        buf.writeFloat(value);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleChangeGameState(this);
    }

    public int getGameState()
    {
        return state;
    }

    public float getValue()
    {
        return value;
    }
}
