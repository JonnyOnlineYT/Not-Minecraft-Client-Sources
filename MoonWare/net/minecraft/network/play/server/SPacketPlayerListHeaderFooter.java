package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.Component;

public class SPacketPlayerListHeaderFooter implements Packet<INetHandlerPlayClient>
{
    private Component header;
    private Component footer;

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        header = buf.readTextComponent();
        footer = buf.readTextComponent();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeTextComponent(header);
        buf.writeTextComponent(footer);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handlePlayerListHeaderFooter(this);
    }

    public Component getHeader()
    {
        return header;
    }

    public Component getFooter()
    {
        return footer;
    }
}
