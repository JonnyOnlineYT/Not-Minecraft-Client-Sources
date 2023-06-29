package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SPacketExplosion implements Packet<INetHandlerPlayClient>
{
    private double posX;
    private double posY;
    private double posZ;
    private float strength;
    private List<BlockPos> affectedBlockPositions;
    public float motionX;
    public float motionY;
    public float motionZ;

    public SPacketExplosion()
    {
    }

    public SPacketExplosion(double xIn, double yIn, double zIn, float strengthIn, List<BlockPos> affectedBlockPositionsIn, Vec3d motion)
    {
        posX = xIn;
        posY = yIn;
        posZ = zIn;
        strength = strengthIn;
        affectedBlockPositions = Lists.newArrayList(affectedBlockPositionsIn);

        if (motion != null)
        {
            motionX = (float)motion.xCoord;
            motionY = (float)motion.yCoord;
            motionZ = (float)motion.zCoord;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        posX = buf.readFloat();
        posY = buf.readFloat();
        posZ = buf.readFloat();
        strength = buf.readFloat();
        int i = buf.readInt();
        affectedBlockPositions = Lists.newArrayListWithCapacity(i);
        int j = (int) posX;
        int k = (int) posY;
        int l = (int) posZ;

        for (int i1 = 0; i1 < i; ++i1)
        {
            int j1 = buf.readByte() + j;
            int k1 = buf.readByte() + k;
            int l1 = buf.readByte() + l;
            affectedBlockPositions.add(new BlockPos(j1, k1, l1));
        }

        motionX = buf.readFloat();
        motionY = buf.readFloat();
        motionZ = buf.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeFloat((float) posX);
        buf.writeFloat((float) posY);
        buf.writeFloat((float) posZ);
        buf.writeFloat(strength);
        buf.writeInt(affectedBlockPositions.size());
        int i = (int) posX;
        int j = (int) posY;
        int k = (int) posZ;

        for (BlockPos blockpos : affectedBlockPositions)
        {
            int l = blockpos.getX() - i;
            int i1 = blockpos.getY() - j;
            int j1 = blockpos.getZ() - k;
            buf.writeByte(l);
            buf.writeByte(i1);
            buf.writeByte(j1);
        }

        buf.writeFloat(motionX);
        buf.writeFloat(motionY);
        buf.writeFloat(motionZ);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleExplosion(this);
    }

    public float getMotionX()
    {
        return motionX;
    }

    public float getMotionY()
    {
        return motionY;
    }

    public float getMotionZ()
    {
        return motionZ;
    }

    public double getX()
    {
        return posX;
    }

    public double getY()
    {
        return posY;
    }

    public double getZ()
    {
        return posZ;
    }

    public float getStrength()
    {
        return strength;
    }

    public List<BlockPos> getAffectedBlockPositions()
    {
        return affectedBlockPositions;
    }
}
