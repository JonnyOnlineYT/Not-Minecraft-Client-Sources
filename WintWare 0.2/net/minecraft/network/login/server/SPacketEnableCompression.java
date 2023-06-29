package net.minecraft.network.login.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class SPacketEnableCompression implements Packet<INetHandlerLoginClient> {
   private int compressionThreshold;

   public SPacketEnableCompression() {
   }

   public SPacketEnableCompression(int thresholdIn) {
      this.compressionThreshold = thresholdIn;
   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.compressionThreshold = buf.readVarIntFromBuffer();
   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarIntToBuffer(this.compressionThreshold);
   }

   public void processPacket(INetHandlerLoginClient handler) {
      handler.handleEnableCompression(this);
   }

   public int getCompressionThreshold() {
      return this.compressionThreshold;
   }
}
