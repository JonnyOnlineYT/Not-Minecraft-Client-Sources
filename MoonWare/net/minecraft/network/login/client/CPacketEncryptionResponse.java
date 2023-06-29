package net.minecraft.network.login.client;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.util.CryptManager;

public class CPacketEncryptionResponse implements Packet<INetHandlerLoginServer>
{
    private byte[] secretKeyEncrypted = new byte[0];
    private byte[] verifyTokenEncrypted = new byte[0];

    public CPacketEncryptionResponse()
    {
    }

    public CPacketEncryptionResponse(SecretKey secret, PublicKey key, byte[] verifyToken)
    {
        secretKeyEncrypted = CryptManager.encryptData(key, secret.getEncoded());
        verifyTokenEncrypted = CryptManager.encryptData(key, verifyToken);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        secretKeyEncrypted = buf.readByteArray();
        verifyTokenEncrypted = buf.readByteArray();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByteArray(secretKeyEncrypted);
        buf.writeByteArray(verifyTokenEncrypted);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerLoginServer handler)
    {
        handler.processEncryptionResponse(this);
    }

    public SecretKey getSecretKey(PrivateKey key)
    {
        return CryptManager.decryptSharedKey(key, secretKeyEncrypted);
    }

    public byte[] getVerifyToken(PrivateKey key)
    {
        return key == null ? verifyTokenEncrypted : CryptManager.decryptData(key, verifyTokenEncrypted);
    }
}
