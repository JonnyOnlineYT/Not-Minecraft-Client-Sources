package net.minecraft.network.play.server;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

public class SPacketStatistics implements Packet<INetHandlerPlayClient> {
   private Map<StatBase, Integer> statisticMap;

   public SPacketStatistics() {
   }

   public SPacketStatistics(Map<StatBase, Integer> statisticMapIn) {
      this.statisticMap = statisticMapIn;
   }

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handleStatistics(this);
   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      int i = buf.readVarIntFromBuffer();
      this.statisticMap = Maps.newHashMap();

      for(int j = 0; j < i; ++j) {
         StatBase statbase = StatList.getOneShotStat(buf.readStringFromBuffer(32767));
         int k = buf.readVarIntFromBuffer();
         if (statbase != null) {
            this.statisticMap.put(statbase, k);
         }
      }

   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarIntToBuffer(this.statisticMap.size());
      Iterator var2 = this.statisticMap.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<StatBase, Integer> entry = (Entry)var2.next();
         buf.writeString(((StatBase)entry.getKey()).statId);
         buf.writeVarIntToBuffer((Integer)entry.getValue());
      }

   }

   public Map<StatBase, Integer> getStatisticMap() {
      return this.statisticMap;
   }
}
