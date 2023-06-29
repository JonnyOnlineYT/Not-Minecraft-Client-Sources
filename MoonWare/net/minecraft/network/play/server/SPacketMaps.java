package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Collection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class SPacketMaps implements Packet<INetHandlerPlayClient>
{
    private int mapId;
    private byte mapScale;
    private boolean trackingPosition;
    private MapDecoration[] icons;
    private int minX;
    private int minZ;
    private int columns;
    private int rows;
    private byte[] mapDataBytes;

    public SPacketMaps()
    {
    }

    public SPacketMaps(int mapIdIn, byte mapScaleIn, boolean trackingPositionIn, Collection<MapDecoration> iconsIn, byte[] p_i46937_5_, int minXIn, int minZIn, int columnsIn, int rowsIn)
    {
        mapId = mapIdIn;
        mapScale = mapScaleIn;
        trackingPosition = trackingPositionIn;
        icons = iconsIn.toArray(new MapDecoration[iconsIn.size()]);
        minX = minXIn;
        minZ = minZIn;
        columns = columnsIn;
        rows = rowsIn;
        mapDataBytes = new byte[columnsIn * rowsIn];

        for (int i = 0; i < columnsIn; ++i)
        {
            for (int j = 0; j < rowsIn; ++j)
            {
                mapDataBytes[i + j * columnsIn] = p_i46937_5_[minXIn + i + (minZIn + j) * 128];
            }
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        mapId = buf.readVarIntFromBuffer();
        mapScale = buf.readByte();
        trackingPosition = buf.readBoolean();
        icons = new MapDecoration[buf.readVarIntFromBuffer()];

        for (int i = 0; i < icons.length; ++i)
        {
            short short1 = buf.readByte();
            icons[i] = new MapDecoration(MapDecoration.Type.func_191159_a((byte)(short1 >> 4 & 15)), buf.readByte(), buf.readByte(), (byte)(short1 & 15));
        }

        columns = buf.readUnsignedByte();

        if (columns > 0)
        {
            rows = buf.readUnsignedByte();
            minX = buf.readUnsignedByte();
            minZ = buf.readUnsignedByte();
            mapDataBytes = buf.readByteArray();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(mapId);
        buf.writeByte(mapScale);
        buf.writeBoolean(trackingPosition);
        buf.writeVarIntToBuffer(icons.length);

        for (MapDecoration mapdecoration : icons)
        {
            buf.writeByte((mapdecoration.getType() & 15) << 4 | mapdecoration.getRotation() & 15);
            buf.writeByte(mapdecoration.getX());
            buf.writeByte(mapdecoration.getY());
        }

        buf.writeByte(columns);

        if (columns > 0)
        {
            buf.writeByte(rows);
            buf.writeByte(minX);
            buf.writeByte(minZ);
            buf.writeByteArray(mapDataBytes);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleMaps(this);
    }

    public int getMapId()
    {
        return mapId;
    }

    /**
     * Sets new MapData from the packet to given MapData param
     */
    public void setMapdataTo(MapData mapdataIn)
    {
        mapdataIn.scale = mapScale;
        mapdataIn.trackingPosition = trackingPosition;
        mapdataIn.mapDecorations.clear();

        for (int i = 0; i < icons.length; ++i)
        {
            MapDecoration mapdecoration = icons[i];
            mapdataIn.mapDecorations.put("icon-" + i, mapdecoration);
        }

        for (int j = 0; j < columns; ++j)
        {
            for (int k = 0; k < rows; ++k)
            {
                mapdataIn.colors[minX + j + (minZ + k) * 128] = mapDataBytes[j + k * columns];
            }
        }
    }
}
