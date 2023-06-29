package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagShort;

public class MapStorage {
   private final ISaveHandler saveHandler;
   protected Map<String, WorldSavedData> loadedDataMap = Maps.newHashMap();
   private final List<WorldSavedData> loadedDataList = Lists.newArrayList();
   private final Map<String, Short> idCounts = Maps.newHashMap();

   public MapStorage(ISaveHandler saveHandlerIn) {
      this.saveHandler = saveHandlerIn;
      this.loadIdCounts();
   }

   @Nullable
   public WorldSavedData getOrLoadData(Class<? extends WorldSavedData> clazz, String dataIdentifier) {
      WorldSavedData worldsaveddata = (WorldSavedData)this.loadedDataMap.get(dataIdentifier);
      if (worldsaveddata != null) {
         return worldsaveddata;
      } else {
         if (this.saveHandler != null) {
            try {
               File file1 = this.saveHandler.getMapFileFromName(dataIdentifier);
               if (file1 != null && file1.exists()) {
                  try {
                     worldsaveddata = (WorldSavedData)clazz.getConstructor(String.class).newInstance(dataIdentifier);
                  } catch (Exception var7) {
                     throw new RuntimeException("Failed to instantiate " + clazz, var7);
                  }

                  FileInputStream fileinputstream = new FileInputStream(file1);
                  NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
                  fileinputstream.close();
                  worldsaveddata.readFromNBT(nbttagcompound.getCompoundTag("data"));
               }
            } catch (Exception var8) {
               var8.printStackTrace();
            }
         }

         if (worldsaveddata != null) {
            this.loadedDataMap.put(dataIdentifier, worldsaveddata);
            this.loadedDataList.add(worldsaveddata);
         }

         return worldsaveddata;
      }
   }

   public void setData(String dataIdentifier, WorldSavedData data) {
      if (this.loadedDataMap.containsKey(dataIdentifier)) {
         this.loadedDataList.remove(this.loadedDataMap.remove(dataIdentifier));
      }

      this.loadedDataMap.put(dataIdentifier, data);
      this.loadedDataList.add(data);
   }

   public void saveAllData() {
      for(int i = 0; i < this.loadedDataList.size(); ++i) {
         WorldSavedData worldsaveddata = (WorldSavedData)this.loadedDataList.get(i);
         if (worldsaveddata.isDirty()) {
            this.saveData(worldsaveddata);
            worldsaveddata.setDirty(false);
         }
      }

   }

   private void saveData(WorldSavedData data) {
      if (this.saveHandler != null) {
         try {
            File file1 = this.saveHandler.getMapFileFromName(data.mapName);
            if (file1 != null) {
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               nbttagcompound.setTag("data", data.writeToNBT(new NBTTagCompound()));
               FileOutputStream fileoutputstream = new FileOutputStream(file1);
               CompressedStreamTools.writeCompressed(nbttagcompound, fileoutputstream);
               fileoutputstream.close();
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   private void loadIdCounts() {
      try {
         this.idCounts.clear();
         if (this.saveHandler == null) {
            return;
         }

         File file1 = this.saveHandler.getMapFileFromName("idcounts");
         if (file1 != null && file1.exists()) {
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));
            NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
            datainputstream.close();
            Iterator var4 = nbttagcompound.getKeySet().iterator();

            while(var4.hasNext()) {
               String s = (String)var4.next();
               NBTBase nbtbase = nbttagcompound.getTag(s);
               if (nbtbase instanceof NBTTagShort) {
                  NBTTagShort nbttagshort = (NBTTagShort)nbtbase;
                  short short1 = nbttagshort.getShort();
                  this.idCounts.put(s, short1);
               }
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public int getUniqueDataId(String key) {
      Short oshort = (Short)this.idCounts.get(key);
      if (oshort == null) {
         oshort = Short.valueOf((short)0);
      } else {
         oshort = (short)(oshort + 1);
      }

      this.idCounts.put(key, oshort);
      if (this.saveHandler == null) {
         return oshort;
      } else {
         try {
            File file1 = this.saveHandler.getMapFileFromName("idcounts");
            if (file1 != null) {
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               Iterator var5 = this.idCounts.keySet().iterator();

               while(var5.hasNext()) {
                  String s = (String)var5.next();
                  nbttagcompound.setShort(s, (Short)this.idCounts.get(s));
               }

               DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));
               CompressedStreamTools.write(nbttagcompound, (DataOutput)dataoutputstream);
               dataoutputstream.close();
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         return oshort;
      }
   }
}
