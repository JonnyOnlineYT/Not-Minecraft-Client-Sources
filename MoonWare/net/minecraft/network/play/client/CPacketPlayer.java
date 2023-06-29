package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketPlayer implements Packet<INetHandlerPlayServer>
{
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean onGround;
    public boolean moving;
    public boolean rotating;

    public CPacketPlayer()
    {
    }

    public CPacketPlayer(boolean onGroundIn)
    {
        onGround = onGroundIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayer(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        onGround = buf.readUnsignedByte() != 0;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(onGround ? 1 : 0);
    }

    public double getX(double defaultValue)
    {
        return moving ? x : defaultValue;
    }

    public double getY(double defaultValue)
    {
        return moving ? y : defaultValue;
    }

    public double getZ(double defaultValue)
    {
        return moving ? z : defaultValue;
    }

    public float getYaw(float defaultValue)
    {
        return rotating ? yaw : defaultValue;
    }

    public float getPitch(float defaultValue)
    {
        return rotating ? pitch : defaultValue;
    }

    public boolean isOnGround()
    {
        return onGround;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isRotating() {
        return rotating;
    }

    public void setRotating(boolean rotating) {
        this.rotating = rotating;
    }

    public static class Position extends CPacketPlayer
    {
        public Position()
        {
            moving = true;
        }

        public Position(double xIn, double yIn, double zIn, boolean onGroundIn)
        {
            x = xIn;
            y = yIn;
            z = zIn;
            onGround = onGroundIn;
            moving = true;
        }

        public void readPacketData(PacketBuffer buf) throws IOException
        {
            x = buf.readDouble();
            y = buf.readDouble();
            z = buf.readDouble();
            super.readPacketData(buf);
        }

        public void writePacketData(PacketBuffer buf) throws IOException
        {
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            super.writePacketData(buf);
        }
    }

    public static class PositionRotation extends CPacketPlayer
    {
        public PositionRotation()
        {
            moving = true;
            rotating = true;
        }

        public PositionRotation(double xIn, double yIn, double zIn, float yawIn, float pitchIn, boolean onGroundIn)
        {
            x = xIn;
            y = yIn;
            z = zIn;
            yaw = yawIn;
            pitch = pitchIn;
            onGround = onGroundIn;
            rotating = true;
            moving = true;
        }

        public void readPacketData(PacketBuffer buf) throws IOException
        {
            x = buf.readDouble();
            y = buf.readDouble();
            z = buf.readDouble();
            yaw = buf.readFloat();
            pitch = buf.readFloat();
            super.readPacketData(buf);
        }

        public void writePacketData(PacketBuffer buf) throws IOException
        {
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeFloat(yaw);
            buf.writeFloat(pitch);
            super.writePacketData(buf);
        }
    }

    public static class Rotation extends CPacketPlayer
    {
        public Rotation()
        {
            rotating = true;
        }

        public Rotation(float yawIn, float pitchIn, boolean onGroundIn)
        {
            yaw = yawIn;
            pitch = pitchIn;
            onGround = onGroundIn;
            rotating = true;
        }

        public void readPacketData(PacketBuffer buf) throws IOException
        {
            yaw = buf.readFloat();
            pitch = buf.readFloat();
            super.readPacketData(buf);
        }

        public void writePacketData(PacketBuffer buf) throws IOException
        {
            buf.writeFloat(yaw);
            buf.writeFloat(pitch);
            super.writePacketData(buf);
        }
    }
}
