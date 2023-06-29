package net.minecraft.network.login.client;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;

public class CPacketLoginStart implements Packet<INetHandlerLoginServer>
{
    private GameProfile profile;

    public CPacketLoginStart()
    {
    }

    public CPacketLoginStart(GameProfile profileIn)
    {
        profile = profileIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        profile = new GameProfile(null, buf.readStringFromBuffer(16));
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(profile.getName());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerLoginServer handler)
    {
        handler.processLoginStart(this);
    }

    public GameProfile getProfile()
    {
        return profile;
    }
}
