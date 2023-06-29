package net.minecraft.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum DimensionType {
   OVERWORLD(0, "overworld", "", WorldProviderSurface.class),
   NETHER(-1, "the_nether", "_nether", WorldProviderHell.class),
   THE_END(1, "the_end", "_end", WorldProviderEnd.class);

   private final int id;
   private final String name;
   private final String suffix;
   private final Class<? extends WorldProvider> clazz;

   private DimensionType(int idIn, String nameIn, String suffixIn, Class<? extends WorldProvider> clazzIn) {
      this.id = idIn;
      this.name = nameIn;
      this.suffix = suffixIn;
      this.clazz = clazzIn;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getSuffix() {
      return this.suffix;
   }

   public WorldProvider createDimension() {
      try {
         Constructor<? extends WorldProvider> constructor = this.clazz.getConstructor();
         return (WorldProvider)constructor.newInstance();
      } catch (NoSuchMethodException var2) {
         throw new Error("Could not create new dimension", var2);
      } catch (InvocationTargetException var3) {
         throw new Error("Could not create new dimension", var3);
      } catch (InstantiationException var4) {
         throw new Error("Could not create new dimension", var4);
      } catch (IllegalAccessException var5) {
         throw new Error("Could not create new dimension", var5);
      }
   }

   public static DimensionType getById(int id) {
      DimensionType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         DimensionType dimensiontype = var1[var3];
         if (dimensiontype.getId() == id) {
            return dimensiontype;
         }
      }

      throw new IllegalArgumentException("Invalid dimension id " + id);
   }

   public static DimensionType func_193417_a(String p_193417_0_) {
      DimensionType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         DimensionType dimensiontype = var1[var3];
         if (dimensiontype.getName().equals(p_193417_0_)) {
            return dimensiontype;
         }
      }

      throw new IllegalArgumentException("Invalid dimension " + p_193417_0_);
   }
}
