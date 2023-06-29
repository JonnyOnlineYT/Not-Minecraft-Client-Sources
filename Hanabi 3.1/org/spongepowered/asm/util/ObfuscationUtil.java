package org.spongepowered.asm.util;

public abstract class ObfuscationUtil {
   private ObfuscationUtil() {
   }

   public static String mapDescriptor(String desc, IClassRemapper remapper) {
      return remapDescriptor(desc, remapper, false);
   }

   public static String unmapDescriptor(String desc, IClassRemapper remapper) {
      return remapDescriptor(desc, remapper, true);
   }

   private static String remapDescriptor(String desc, IClassRemapper remapper, boolean unmap) {
      StringBuilder sb = new StringBuilder();
      StringBuilder token = null;

      for(int pos = 0; pos < desc.length(); ++pos) {
         char c = desc.charAt(pos);
         if (token != null) {
            if (c == ';') {
               sb.append('L').append(remap(token.toString(), remapper, unmap)).append(';');
               token = null;
            } else {
               token.append(c);
            }
         } else if (c == 'L') {
            token = new StringBuilder();
         } else {
            sb.append(c);
         }
      }

      if (token != null) {
         throw new IllegalArgumentException("Invalid descriptor '" + desc + "', missing ';'");
      } else {
         return sb.toString();
      }
   }

   private static Object remap(String typeName, IClassRemapper remapper, boolean unmap) {
      String result = unmap ? remapper.unmap(typeName) : remapper.map(typeName);
      return result != null ? result : typeName;
   }

   public interface IClassRemapper {
      String map(String var1);

      String unmap(String var1);
   }
}
