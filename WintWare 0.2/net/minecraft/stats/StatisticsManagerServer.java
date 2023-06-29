package net.minecraft.stats;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatisticsManagerServer extends StatisticsManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private final MinecraftServer mcServer;
   private final File statsFile;
   private final Set<StatBase> dirty = Sets.newHashSet();
   private int lastStatRequest = -300;

   public StatisticsManagerServer(MinecraftServer serverIn, File statsFileIn) {
      this.mcServer = serverIn;
      this.statsFile = statsFileIn;
   }

   public void readStatFile() {
      if (this.statsFile.isFile()) {
         try {
            this.statsData.clear();
            this.statsData.putAll(this.parseJson(FileUtils.readFileToString(this.statsFile)));
         } catch (IOException var2) {
            LOGGER.error("Couldn't read statistics file {}", this.statsFile, var2);
         } catch (JsonParseException var3) {
            LOGGER.error("Couldn't parse statistics file {}", this.statsFile, var3);
         }
      }

   }

   public void saveStatFile() {
      try {
         FileUtils.writeStringToFile(this.statsFile, dumpJson(this.statsData));
      } catch (IOException var2) {
         LOGGER.error("Couldn't save stats", var2);
      }

   }

   public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_) {
      super.unlockAchievement(playerIn, statIn, p_150873_3_);
      this.dirty.add(statIn);
   }

   private Set<StatBase> getDirty() {
      Set<StatBase> set = Sets.newHashSet(this.dirty);
      this.dirty.clear();
      return set;
   }

   public Map<StatBase, TupleIntJsonSerializable> parseJson(String p_150881_1_) {
      JsonElement jsonelement = (new JsonParser()).parse(p_150881_1_);
      if (!jsonelement.isJsonObject()) {
         return Maps.newHashMap();
      } else {
         JsonObject jsonobject = jsonelement.getAsJsonObject();
         Map<StatBase, TupleIntJsonSerializable> map = Maps.newHashMap();
         Iterator var5 = jsonobject.entrySet().iterator();

         while(true) {
            while(var5.hasNext()) {
               Entry<String, JsonElement> entry = (Entry)var5.next();
               StatBase statbase = StatList.getOneShotStat((String)entry.getKey());
               if (statbase != null) {
                  TupleIntJsonSerializable tupleintjsonserializable = new TupleIntJsonSerializable();
                  if (((JsonElement)entry.getValue()).isJsonPrimitive() && ((JsonElement)entry.getValue()).getAsJsonPrimitive().isNumber()) {
                     tupleintjsonserializable.setIntegerValue(((JsonElement)entry.getValue()).getAsInt());
                  } else if (((JsonElement)entry.getValue()).isJsonObject()) {
                     JsonObject jsonobject1 = ((JsonElement)entry.getValue()).getAsJsonObject();
                     if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber()) {
                        tupleintjsonserializable.setIntegerValue(jsonobject1.getAsJsonPrimitive("value").getAsInt());
                     }

                     if (jsonobject1.has("progress") && statbase.getSerializableClazz() != null) {
                        try {
                           Constructor<? extends IJsonSerializable> constructor = statbase.getSerializableClazz().getConstructor();
                           IJsonSerializable ijsonserializable = (IJsonSerializable)constructor.newInstance();
                           ijsonserializable.fromJson(jsonobject1.get("progress"));
                           tupleintjsonserializable.setJsonSerializableValue(ijsonserializable);
                        } catch (Throwable var12) {
                           LOGGER.warn("Invalid statistic progress in {}", this.statsFile, var12);
                        }
                     }
                  }

                  map.put(statbase, tupleintjsonserializable);
               } else {
                  LOGGER.warn("Invalid statistic in {}: Don't know what {} is", this.statsFile, entry.getKey());
               }
            }

            return map;
         }
      }
   }

   public static String dumpJson(Map<StatBase, TupleIntJsonSerializable> p_150880_0_) {
      JsonObject jsonobject = new JsonObject();
      Iterator var2 = p_150880_0_.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<StatBase, TupleIntJsonSerializable> entry = (Entry)var2.next();
         if (((TupleIntJsonSerializable)entry.getValue()).getJsonSerializableValue() != null) {
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("value", ((TupleIntJsonSerializable)entry.getValue()).getIntegerValue());

            try {
               jsonobject1.add("progress", ((TupleIntJsonSerializable)entry.getValue()).getJsonSerializableValue().getSerializableElement());
            } catch (Throwable var6) {
               LOGGER.warn("Couldn't save statistic {}: error serializing progress", ((StatBase)entry.getKey()).getStatName(), var6);
            }

            jsonobject.add(((StatBase)entry.getKey()).statId, jsonobject1);
         } else {
            jsonobject.addProperty(((StatBase)entry.getKey()).statId, ((TupleIntJsonSerializable)entry.getValue()).getIntegerValue());
         }
      }

      return jsonobject.toString();
   }

   public void markAllDirty() {
      this.dirty.addAll(this.statsData.keySet());
   }

   public void sendStats(EntityPlayerMP player) {
      int i = this.mcServer.getTickCounter();
      Map<StatBase, Integer> map = Maps.newHashMap();
      if (i - this.lastStatRequest > 300) {
         this.lastStatRequest = i;
         Iterator var4 = this.getDirty().iterator();

         while(var4.hasNext()) {
            StatBase statbase = (StatBase)var4.next();
            map.put(statbase, this.readStat(statbase));
         }
      }

      player.connection.sendPacket(new SPacketStatistics(map));
   }
}
