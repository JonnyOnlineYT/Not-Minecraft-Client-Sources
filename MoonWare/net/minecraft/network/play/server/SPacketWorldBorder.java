package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.border.WorldBorder;

public class SPacketWorldBorder implements Packet<INetHandlerPlayClient>
{
    private SPacketWorldBorder.Action action;
    private int size;
    private double centerX;
    private double centerZ;
    private double targetSize;
    private double diameter;
    private long timeUntilTarget;
    private int warningTime;
    private int warningDistance;

    public SPacketWorldBorder()
    {
    }

    public SPacketWorldBorder(WorldBorder border, SPacketWorldBorder.Action actionIn)
    {
        action = actionIn;
        centerX = border.getCenterX();
        centerZ = border.getCenterZ();
        diameter = border.getDiameter();
        targetSize = border.getTargetSize();
        timeUntilTarget = border.getTimeUntilTarget();
        size = border.getSize();
        warningDistance = border.getWarningDistance();
        warningTime = border.getWarningTime();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        action = buf.readEnumValue(Action.class);

        switch (action)
        {
            case SET_SIZE:
                targetSize = buf.readDouble();
                break;

            case LERP_SIZE:
                diameter = buf.readDouble();
                targetSize = buf.readDouble();
                timeUntilTarget = buf.readVarLong();
                break;

            case SET_CENTER:
                centerX = buf.readDouble();
                centerZ = buf.readDouble();
                break;

            case SET_WARNING_BLOCKS:
                warningDistance = buf.readVarIntFromBuffer();
                break;

            case SET_WARNING_TIME:
                warningTime = buf.readVarIntFromBuffer();
                break;

            case INITIALIZE:
                centerX = buf.readDouble();
                centerZ = buf.readDouble();
                diameter = buf.readDouble();
                targetSize = buf.readDouble();
                timeUntilTarget = buf.readVarLong();
                size = buf.readVarIntFromBuffer();
                warningDistance = buf.readVarIntFromBuffer();
                warningTime = buf.readVarIntFromBuffer();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(action);

        switch (action)
        {
            case SET_SIZE:
                buf.writeDouble(targetSize);
                break;

            case LERP_SIZE:
                buf.writeDouble(diameter);
                buf.writeDouble(targetSize);
                buf.writeVarLong(timeUntilTarget);
                break;

            case SET_CENTER:
                buf.writeDouble(centerX);
                buf.writeDouble(centerZ);
                break;

            case SET_WARNING_BLOCKS:
                buf.writeVarIntToBuffer(warningDistance);
                break;

            case SET_WARNING_TIME:
                buf.writeVarIntToBuffer(warningTime);
                break;

            case INITIALIZE:
                buf.writeDouble(centerX);
                buf.writeDouble(centerZ);
                buf.writeDouble(diameter);
                buf.writeDouble(targetSize);
                buf.writeVarLong(timeUntilTarget);
                buf.writeVarIntToBuffer(size);
                buf.writeVarIntToBuffer(warningDistance);
                buf.writeVarIntToBuffer(warningTime);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleWorldBorder(this);
    }

    public void apply(WorldBorder border)
    {
        switch (action)
        {
            case SET_SIZE:
                border.setTransition(targetSize);
                break;

            case LERP_SIZE:
                border.setTransition(diameter, targetSize, timeUntilTarget);
                break;

            case SET_CENTER:
                border.setCenter(centerX, centerZ);
                break;

            case SET_WARNING_BLOCKS:
                border.setWarningDistance(warningDistance);
                break;

            case SET_WARNING_TIME:
                border.setWarningTime(warningTime);
                break;

            case INITIALIZE:
                border.setCenter(centerX, centerZ);

                if (timeUntilTarget > 0L)
                {
                    border.setTransition(diameter, targetSize, timeUntilTarget);
                }
                else
                {
                    border.setTransition(targetSize);
                }

                border.setSize(size);
                border.setWarningDistance(warningDistance);
                border.setWarningTime(warningTime);
        }
    }

    public enum Action
    {
        SET_SIZE,
        LERP_SIZE,
        SET_CENTER,
        INITIALIZE,
        SET_WARNING_TIME,
        SET_WARNING_BLOCKS
    }
}
