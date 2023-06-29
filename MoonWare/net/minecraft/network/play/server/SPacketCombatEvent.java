package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;

public class SPacketCombatEvent implements Packet<INetHandlerPlayClient>
{
    public SPacketCombatEvent.Event eventType;
    public int playerId;
    public int entityId;
    public int duration;
    public Component deathMessage;

    public SPacketCombatEvent()
    {
    }

    public SPacketCombatEvent(CombatTracker tracker, SPacketCombatEvent.Event eventIn)
    {
        this(tracker, eventIn, true);
    }

    @SuppressWarnings("incomplete-switch")
    public SPacketCombatEvent(CombatTracker tracker, SPacketCombatEvent.Event eventIn, boolean p_i46932_3_)
    {
        eventType = eventIn;
        EntityLivingBase entitylivingbase = tracker.getBestAttacker();

        switch (eventIn)
        {
            case END_COMBAT:
                duration = tracker.getCombatDuration();
                entityId = entitylivingbase == null ? -1 : entitylivingbase.getEntityId();
                break;

            case ENTITY_DIED:
                playerId = tracker.getFighter().getEntityId();
                entityId = entitylivingbase == null ? -1 : entitylivingbase.getEntityId();

                if (p_i46932_3_)
                {
                    deathMessage = tracker.getDeathMessage();
                }
                else
                {
                    deathMessage = new TextComponent("");
                }
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        eventType = buf.readEnumValue(Event.class);

        if (eventType == SPacketCombatEvent.Event.END_COMBAT)
        {
            duration = buf.readVarIntFromBuffer();
            entityId = buf.readInt();
        }
        else if (eventType == SPacketCombatEvent.Event.ENTITY_DIED)
        {
            playerId = buf.readVarIntFromBuffer();
            entityId = buf.readInt();
            deathMessage = buf.readTextComponent();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(eventType);

        if (eventType == SPacketCombatEvent.Event.END_COMBAT)
        {
            buf.writeVarIntToBuffer(duration);
            buf.writeInt(entityId);
        }
        else if (eventType == SPacketCombatEvent.Event.ENTITY_DIED)
        {
            buf.writeVarIntToBuffer(playerId);
            buf.writeInt(entityId);
            buf.writeTextComponent(deathMessage);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCombatEvent(this);
    }

    public enum Event
    {
        ENTER_COMBAT,
        END_COMBAT,
        ENTITY_DIED
    }
}
