package net.minecraft.util.text.translation;

/** @deprecated */
@Deprecated
public class I18n {
   private static final LanguageMap localizedName = LanguageMap.getInstance();
   private static final LanguageMap fallbackTranslator = new LanguageMap();

   /** @deprecated */
   @Deprecated
   public static String translateToLocal(String key) {
      return localizedName.translateKey(key);
   }

   /** @deprecated */
   @Deprecated
   public static String translateToLocalFormatted(String key, Object... format) {
      return localizedName.translateKeyFormat(key, format);
   }

   /** @deprecated */
   @Deprecated
   public static String translateToFallback(String key) {
      return fallbackTranslator.translateKey(key);
   }

   /** @deprecated */
   @Deprecated
   public static boolean canTranslate(String key) {
      return localizedName.isKeyTranslated(key);
   }

   public static long getLastTranslationUpdateTimeInMilliseconds() {
      return localizedName.getLastUpdateTimeInMilliseconds();
   }
}
