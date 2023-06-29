package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHandSide;

public class CPacketClientSettings implements Packet<INetHandlerPlayServer>
{
    private String lang;
    private int view;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private boolean enableColors;
    private int modelPartFlags;
    private EnumHandSide mainHand;

    public CPacketClientSettings()
    {
    }

    public CPacketClientSettings(String langIn, int renderDistanceIn, EntityPlayer.EnumChatVisibility chatVisibilityIn, boolean chatColorsIn, int modelPartsIn, EnumHandSide mainHandIn)
    {
        lang = langIn;
        view = renderDistanceIn;
        chatVisibility = chatVisibilityIn;
        enableColors = chatColorsIn;
        modelPartFlags = modelPartsIn;
        mainHand = mainHandIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        lang = buf.readStringFromBuffer(16);
        view = buf.readByte();
        chatVisibility = buf.readEnumValue(EntityPlayer.EnumChatVisibility.class);
        enableColors = buf.readBoolean();
        modelPartFlags = buf.readUnsignedByte();
        mainHand = buf.readEnumValue(EnumHandSide.class);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(lang);
        buf.writeByte(view);
        buf.writeEnumValue(chatVisibility);
        buf.writeBoolean(enableColors);
        buf.writeByte(modelPartFlags);
        buf.writeEnumValue(mainHand);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processClientSettings(this);
    }

    public String getLang()
    {
        return lang;
    }

    public EntityPlayer.EnumChatVisibility getChatVisibility()
    {
        return chatVisibility;
    }

    public boolean isColorsEnabled()
    {
        return enableColors;
    }

    public int getModelPartFlags()
    {
        return modelPartFlags;
    }

    public EnumHandSide getMainHand()
    {
        return mainHand;
    }
}
