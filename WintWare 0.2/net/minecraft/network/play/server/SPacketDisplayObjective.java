package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketDisplayObjective implements Packet<INetHandlerPlayClient> {
   private int position;
   private String scoreName;

   public SPacketDisplayObjective() {
   }

   public SPacketDisplayObjective(int positionIn, ScoreObjective objective) {
      this.position = positionIn;
      if (objective == null) {
         this.scoreName = "";
      } else {
         this.scoreName = objective.getName();
      }

   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.position = buf.readByte();
      this.scoreName = buf.readStringFromBuffer(16);
   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeByte(this.position);
      buf.writeString(this.scoreName);
   }

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handleDisplayObjective(this);
   }

   public int getPosition() {
      return this.position;
   }

   public String getName() {
      return this.scoreName;
   }
}
