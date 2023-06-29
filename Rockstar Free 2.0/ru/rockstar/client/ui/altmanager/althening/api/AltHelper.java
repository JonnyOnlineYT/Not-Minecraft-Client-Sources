package ru.rockstar.client.ui.altmanager.althening.api;

import java.lang.reflect.Field;

public class AltHelper {
   private String className;
   private Class<?> clazz;

   public AltHelper(String v1) {
      try {
         this.clazz = Class.forName(v1);
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
      }

   }

   public void setStaticField(String a2, Object v1) throws NoSuchFieldException, IllegalAccessException {
      Field v2 = this.clazz.getDeclaredField(a2);
      v2.setAccessible(true);
      Field v3 = Field.class.getDeclaredField("modifiers");
      v3.setAccessible(true);
      v3.setInt(v2, v2.getModifiers() & -17);
      v2.set((Object)null, v1);
   }
}
