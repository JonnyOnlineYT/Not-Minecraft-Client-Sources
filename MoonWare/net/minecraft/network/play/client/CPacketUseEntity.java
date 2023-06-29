package net.minecraft.network.play.client;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CPacketUseEntity implements Packet<INetHandlerPlayServer>
{
    private int entityId;
    private CPacketUseEntity.Action action;
    private Vec3d hitVec;
    private EnumHand hand;

    public CPacketUseEntity()
    {
    }

    public CPacketUseEntity(Entity entityIn)
    {
        entityId = entityIn.getEntityId();
        action = CPacketUseEntity.Action.ATTACK;
    }

    public CPacketUseEntity(Entity entityIn, EnumHand handIn)
    {
        entityId = entityIn.getEntityId();
        action = CPacketUseEntity.Action.INTERACT;
        hand = handIn;
    }

    public CPacketUseEntity(Entity entityIn, EnumHand handIn, Vec3d hitVecIn)
    {
        entityId = entityIn.getEntityId();
        action = CPacketUseEntity.Action.INTERACT_AT;
        hand = handIn;
        hitVec = hitVecIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityId = buf.readVarIntFromBuffer();
        action = buf.readEnumValue(Action.class);

        if (action == CPacketUseEntity.Action.INTERACT_AT)
        {
            hitVec = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        if (action == CPacketUseEntity.Action.INTERACT || action == CPacketUseEntity.Action.INTERACT_AT)
        {
            hand = buf.readEnumValue(EnumHand.class);
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityId);
        buf.writeEnumValue(action);

        if (action == CPacketUseEntity.Action.INTERACT_AT)
        {
            buf.writeFloat((float) hitVec.xCoord);
            buf.writeFloat((float) hitVec.yCoord);
            buf.writeFloat((float) hitVec.zCoord);
        }

        if (action == CPacketUseEntity.Action.INTERACT || action == CPacketUseEntity.Action.INTERACT_AT)
        {
            buf.writeEnumValue(hand);
        }
    }

    public int getEntityId() {
        return entityId;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processUseEntity(this);
    }

    @Nullable
    public Entity getEntityFromWorld(World worldIn)
    {
        return worldIn.getEntityByID(entityId);
    }

    public CPacketUseEntity.Action getAction()
    {
        return action;
    }

    public EnumHand getHand()
    {
        return hand;
    }

    public Vec3d getHitVec()
    {
        return hitVec;
    }

    public enum Action
    {
        INTERACT,
        ATTACK,
        INTERACT_AT
    }
}
