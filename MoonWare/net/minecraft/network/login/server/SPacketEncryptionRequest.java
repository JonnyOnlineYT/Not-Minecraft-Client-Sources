package net.minecraft.network.login.server;

import java.io.IOException;
import java.security.PublicKey;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.CryptManager;

public class SPacketEncryptionRequest implements Packet<INetHandlerLoginClient>
{
    private String hashedServerId;
    private PublicKey publicKey;
    private byte[] verifyToken;

    public SPacketEncryptionRequest()
    {
    }

    public SPacketEncryptionRequest(String serverIdIn, PublicKey publicKeyIn, byte[] verifyTokenIn)
    {
        hashedServerId = serverIdIn;
        publicKey = publicKeyIn;
        verifyToken = verifyTokenIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        hashedServerId = buf.readStringFromBuffer(20);
        publicKey = CryptManager.decodePublicKey(buf.readByteArray());
        verifyToken = buf.readByteArray();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(hashedServerId);
        buf.writeByteArray(publicKey.getEncoded());
        buf.writeByteArray(verifyToken);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerLoginClient handler)
    {
        handler.handleEncryptionRequest(this);
    }

    public String getServerId()
    {
        return hashedServerId;
    }

    public PublicKey getPublicKey()
    {
        return publicKey;
    }

    public byte[] getVerifyToken()
    {
        return verifyToken;
    }
}
