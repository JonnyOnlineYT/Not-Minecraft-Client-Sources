package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketKeepAlive implements Packet<INetHandlerPlayServer> {
   private long key;

   public CPacketKeepAlive() {
   }

   public CPacketKeepAlive(long idIn) {
      this.key = idIn;
   }

   public void processPacket(INetHandlerPlayServer handler) {
      handler.processKeepAlive(this);
   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.key = buf.readLong();
   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeLong(this.key);
   }

   public long getKey() {
      return this.key;
   }
}
