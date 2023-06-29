package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketDestroyEntities implements Packet<INetHandlerPlayClient> {
   private int[] entityIDs;

   public SPacketDestroyEntities() {
   }

   public SPacketDestroyEntities(int... entityIdsIn) {
      this.entityIDs = entityIdsIn;
   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.entityIDs = new int[buf.readVarIntFromBuffer()];

      for(int i = 0; i < this.entityIDs.length; ++i) {
         this.entityIDs[i] = buf.readVarIntFromBuffer();
      }

   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarIntToBuffer(this.entityIDs.length);
      int[] var2 = this.entityIDs;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int i = var2[var4];
         buf.writeVarIntToBuffer(i);
      }

   }

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handleDestroyEntities(this);
   }

   public int[] getEntityIDs() {
      return this.entityIDs;
   }
}
