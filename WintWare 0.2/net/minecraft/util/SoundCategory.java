package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;

public enum SoundCategory {
   MASTER("master"),
   MUSIC("music"),
   RECORDS("record"),
   WEATHER("weather"),
   BLOCKS("block"),
   HOSTILE("hostile"),
   NEUTRAL("neutral"),
   PLAYERS("player"),
   AMBIENT("ambient"),
   VOICE("voice");

   private static final Map<String, SoundCategory> SOUND_CATEGORIES = Maps.newHashMap();
   private final String name;

   private SoundCategory(String nameIn) {
      this.name = nameIn;
   }

   public String getName() {
      return this.name;
   }

   public static SoundCategory getByName(String categoryName) {
      return (SoundCategory)SOUND_CATEGORIES.get(categoryName);
   }

   public static Set<String> getSoundCategoryNames() {
      return SOUND_CATEGORIES.keySet();
   }

   static {
      SoundCategory[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         SoundCategory soundcategory = var0[var2];
         if (SOUND_CATEGORIES.containsKey(soundcategory.getName())) {
            throw new Error("Clash in Sound Category name pools! Cannot insert " + soundcategory);
         }

         SOUND_CATEGORIES.put(soundcategory.getName(), soundcategory);
      }

   }
}
