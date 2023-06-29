package net.minecraft.world.storage;

import javax.annotation.Nullable;

public class SaveDataMemoryStorage extends MapStorage {
   public SaveDataMemoryStorage() {
      super((ISaveHandler)null);
   }

   @Nullable
   public WorldSavedData getOrLoadData(Class<? extends WorldSavedData> clazz, String dataIdentifier) {
      return (WorldSavedData)this.loadedDataMap.get(dataIdentifier);
   }

   public void setData(String dataIdentifier, WorldSavedData data) {
      this.loadedDataMap.put(dataIdentifier, data);
   }

   public void saveAllData() {
   }

   public int getUniqueDataId(String key) {
      return 0;
   }
}
