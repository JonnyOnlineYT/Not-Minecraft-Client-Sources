package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagShort;

public class MapStorage
{
    private final ISaveHandler saveHandler;
    protected Map<String, WorldSavedData> loadedDataMap = Maps.newHashMap();
    private final List<WorldSavedData> loadedDataList = Lists.newArrayList();
    private final Map<String, Short> idCounts = Maps.newHashMap();

    public MapStorage(ISaveHandler saveHandlerIn)
    {
        saveHandler = saveHandlerIn;
        loadIdCounts();
    }

    @Nullable

    /**
     * Loads an existing MapDataBase corresponding to the given id from disk, instantiating the given Class, or returns
     * null if none such file exists.
     */
    public WorldSavedData getOrLoadData(Class <? extends WorldSavedData > clazz, String dataIdentifier)
    {
        WorldSavedData worldsaveddata = loadedDataMap.get(dataIdentifier);

        if (worldsaveddata != null)
        {
            return worldsaveddata;
        }
        else
        {
            if (saveHandler != null)
            {
                try
                {
                    File file1 = saveHandler.getMapFileFromName(dataIdentifier);

                    if (file1 != null && file1.exists())
                    {
                        try
                        {
                            worldsaveddata = clazz.getConstructor(String.class).newInstance(dataIdentifier);
                        }
                        catch (Exception exception)
                        {
                            throw new RuntimeException("Failed to instantiate " + clazz, exception);
                        }

                        FileInputStream fileinputstream = new FileInputStream(file1);
                        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
                        fileinputstream.close();
                        worldsaveddata.readFromNBT(nbttagcompound.getCompoundTag("data"));
                    }
                }
                catch (Exception exception1)
                {
                    exception1.printStackTrace();
                }
            }

            if (worldsaveddata != null)
            {
                loadedDataMap.put(dataIdentifier, worldsaveddata);
                loadedDataList.add(worldsaveddata);
            }

            return worldsaveddata;
        }
    }

    /**
     * Assigns the given String id to the given MapDataBase, removing any existing ones of the same id.
     */
    public void setData(String dataIdentifier, WorldSavedData data)
    {
        if (loadedDataMap.containsKey(dataIdentifier))
        {
            loadedDataList.remove(loadedDataMap.remove(dataIdentifier));
        }

        loadedDataMap.put(dataIdentifier, data);
        loadedDataList.add(data);
    }

    /**
     * Saves all dirty loaded MapDataBases to disk.
     */
    public void saveAllData()
    {
        for (int i = 0; i < loadedDataList.size(); ++i)
        {
            WorldSavedData worldsaveddata = loadedDataList.get(i);

            if (worldsaveddata.isDirty())
            {
                saveData(worldsaveddata);
                worldsaveddata.setDirty(false);
            }
        }
    }

    /**
     * Saves the given MapDataBase to disk.
     */
    private void saveData(WorldSavedData data)
    {
        if (saveHandler != null)
        {
            try
            {
                File file1 = saveHandler.getMapFileFromName(data.mapName);

                if (file1 != null)
                {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setTag("data", data.writeToNBT(new NBTTagCompound()));
                    FileOutputStream fileoutputstream = new FileOutputStream(file1);
                    CompressedStreamTools.writeCompressed(nbttagcompound, fileoutputstream);
                    fileoutputstream.close();
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Loads the idCounts Map from the 'idcounts' file.
     */
    private void loadIdCounts()
    {
        try
        {
            idCounts.clear();

            if (saveHandler == null)
            {
                return;
            }

            File file1 = saveHandler.getMapFileFromName("idcounts");

            if (file1 != null && file1.exists())
            {
                DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));
                NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
                datainputstream.close();

                for (String s : nbttagcompound.getKeySet())
                {
                    NBTBase nbtbase = nbttagcompound.getTag(s);

                    if (nbtbase instanceof NBTTagShort)
                    {
                        NBTTagShort nbttagshort = (NBTTagShort)nbtbase;
                        short short1 = nbttagshort.getShort();
                        idCounts.put(s, Short.valueOf(short1));
                    }
                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Returns an unique new data id for the given prefix and saves the idCounts map to the 'idcounts' file.
     */
    public int getUniqueDataId(String key)
    {
        Short oshort = idCounts.get(key);

        if (oshort == null)
        {
            oshort = 0;
        }
        else
        {
            oshort = (short)(oshort.shortValue() + 1);
        }

        idCounts.put(key, oshort);

        if (saveHandler == null)
        {
            return oshort.shortValue();
        }
        else
        {
            try
            {
                File file1 = saveHandler.getMapFileFromName("idcounts");

                if (file1 != null)
                {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();

                    for (String s : idCounts.keySet())
                    {
                        nbttagcompound.setShort(s, idCounts.get(s).shortValue());
                    }

                    DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));
                    CompressedStreamTools.write(nbttagcompound, dataoutputstream);
                    dataoutputstream.close();
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

            return oshort.shortValue();
        }
    }
}
