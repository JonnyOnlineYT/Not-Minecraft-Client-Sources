package net.minecraft.stats;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

public class StatisticsManagerServer extends StatisticsManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final MinecraftServer mcServer;
    private final File statsFile;
    private final Set<StatBase> dirty = Sets.newHashSet();
    private int lastStatRequest = -300;

    public StatisticsManagerServer(MinecraftServer serverIn, File statsFileIn)
    {
        mcServer = serverIn;
        statsFile = statsFileIn;
    }

    public void readStatFile()
    {
        if (statsFile.isFile())
        {
            try
            {
                statsData.clear();
                statsData.putAll(parseJson(FileUtils.readFileToString(statsFile)));
            }
            catch (IOException ioexception)
            {
                LOGGER.error("Couldn't read statistics file {}", statsFile, ioexception);
            }
            catch (JsonParseException jsonparseexception)
            {
                LOGGER.error("Couldn't parse statistics file {}", statsFile, jsonparseexception);
            }
        }
    }

    public void saveStatFile()
    {
        try
        {
            FileUtils.writeStringToFile(statsFile, dumpJson(statsData));
        }
        catch (IOException ioexception)
        {
            LOGGER.error("Couldn't save stats", ioexception);
        }
    }

    /**
     * Triggers the logging of an achievement and attempts to announce to server
     */
    public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_)
    {
        super.unlockAchievement(playerIn, statIn, p_150873_3_);
        dirty.add(statIn);
    }

    private Set<StatBase> getDirty()
    {
        Set<StatBase> set = Sets.newHashSet(dirty);
        dirty.clear();
        return set;
    }

    public Map<StatBase, TupleIntJsonSerializable> parseJson(String p_150881_1_)
    {
        JsonElement jsonelement = (new JsonParser()).parse(p_150881_1_);

        if (!jsonelement.isJsonObject())
        {
            return Maps.newHashMap();
        }
        else
        {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            Map<StatBase, TupleIntJsonSerializable> map = Maps.newHashMap();

            for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet())
            {
                StatBase statbase = StatList.getOneShotStat(entry.getKey());

                if (statbase != null)
                {
                    TupleIntJsonSerializable tupleintjsonserializable = new TupleIntJsonSerializable();

                    if (entry.getValue().isJsonPrimitive() && entry.getValue().getAsJsonPrimitive().isNumber())
                    {
                        tupleintjsonserializable.setIntegerValue(entry.getValue().getAsInt());
                    }
                    else if (entry.getValue().isJsonObject())
                    {
                        JsonObject jsonobject1 = entry.getValue().getAsJsonObject();

                        if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber())
                        {
                            tupleintjsonserializable.setIntegerValue(jsonobject1.getAsJsonPrimitive("value").getAsInt());
                        }

                        if (jsonobject1.has("progress") && statbase.getSerializableClazz() != null)
                        {
                            try
                            {
                                Constructor <? extends IJsonSerializable > constructor = statbase.getSerializableClazz().getConstructor();
                                IJsonSerializable ijsonserializable = constructor.newInstance();
                                ijsonserializable.fromJson(jsonobject1.get("progress"));
                                tupleintjsonserializable.setJsonSerializableValue(ijsonserializable);
                            }
                            catch (Throwable throwable)
                            {
                                LOGGER.warn("Invalid statistic progress in {}", statsFile, throwable);
                            }
                        }
                    }

                    map.put(statbase, tupleintjsonserializable);
                }
                else
                {
                    LOGGER.warn("Invalid statistic in {}: Don't know what {} is", statsFile, entry.getKey());
                }
            }

            return map;
        }
    }

    public static String dumpJson(Map<StatBase, TupleIntJsonSerializable> p_150880_0_)
    {
        JsonObject jsonobject = new JsonObject();

        for (Map.Entry<StatBase, TupleIntJsonSerializable> entry : p_150880_0_.entrySet())
        {
            if (entry.getValue().getJsonSerializableValue() != null)
            {
                JsonObject jsonobject1 = new JsonObject();
                jsonobject1.addProperty("value", Integer.valueOf(entry.getValue().getIntegerValue()));

                try
                {
                    jsonobject1.add("progress", entry.getValue().getJsonSerializableValue().getSerializableElement());
                }
                catch (Throwable throwable)
                {
                    LOGGER.warn("Couldn't save statistic {}: error serializing progress", entry.getKey().getStatName(), throwable);
                }

                jsonobject.add((entry.getKey()).statId, jsonobject1);
            }
            else
            {
                jsonobject.addProperty((entry.getKey()).statId, Integer.valueOf(entry.getValue().getIntegerValue()));
            }
        }

        return jsonobject.toString();
    }

    public void markAllDirty()
    {
        dirty.addAll(statsData.keySet());
    }

    public void sendStats(EntityPlayerMP player)
    {
        int i = mcServer.getTickCounter();
        Map<StatBase, Integer> map = Maps.newHashMap();

        if (i - lastStatRequest > 300)
        {
            lastStatRequest = i;

            for (StatBase statbase : getDirty())
            {
                map.put(statbase, Integer.valueOf(readStat(statbase)));
            }
        }

        player.connection.sendPacket(new SPacketStatistics(map));
    }
}
