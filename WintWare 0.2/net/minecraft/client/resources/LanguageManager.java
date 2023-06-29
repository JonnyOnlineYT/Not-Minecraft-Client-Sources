package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.text.translation.LanguageMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanguageManager implements IResourceManagerReloadListener {
   private static final Logger LOGGER = LogManager.getLogger();
   private final MetadataSerializer theMetadataSerializer;
   private String currentLanguage;
   protected static final Locale CURRENT_LOCALE = new Locale();
   private final Map<String, Language> languageMap = Maps.newHashMap();

   public LanguageManager(MetadataSerializer theMetadataSerializerIn, String currentLanguageIn) {
      this.theMetadataSerializer = theMetadataSerializerIn;
      this.currentLanguage = currentLanguageIn;
      I18n.setLocale(CURRENT_LOCALE);
   }

   public void parseLanguageMetadata(List<IResourcePack> resourcesPacks) {
      this.languageMap.clear();
      Iterator var2 = resourcesPacks.iterator();

      while(var2.hasNext()) {
         IResourcePack iresourcepack = (IResourcePack)var2.next();

         try {
            LanguageMetadataSection languagemetadatasection = (LanguageMetadataSection)iresourcepack.getPackMetadata(this.theMetadataSerializer, "language");
            if (languagemetadatasection != null) {
               Iterator var5 = languagemetadatasection.getLanguages().iterator();

               while(var5.hasNext()) {
                  Language language = (Language)var5.next();
                  if (!this.languageMap.containsKey(language.getLanguageCode())) {
                     this.languageMap.put(language.getLanguageCode(), language);
                  }
               }
            }
         } catch (RuntimeException var7) {
            LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", iresourcepack.getPackName(), var7);
         } catch (IOException var8) {
            LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", iresourcepack.getPackName(), var8);
         }
      }

   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      List<String> list = Lists.newArrayList(new String[]{"en_us"});
      if (!"en_us".equals(this.currentLanguage)) {
         list.add(this.currentLanguage);
      }

      CURRENT_LOCALE.loadLocaleDataFiles(resourceManager, list);
      LanguageMap.replaceWith(CURRENT_LOCALE.properties);
   }

   public boolean isCurrentLocaleUnicode() {
      return CURRENT_LOCALE.isUnicode();
   }

   public boolean isCurrentLanguageBidirectional() {
      return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
   }

   public void setCurrentLanguage(Language currentLanguageIn) {
      this.currentLanguage = currentLanguageIn.getLanguageCode();
   }

   public Language getCurrentLanguage() {
      String s = this.languageMap.containsKey(this.currentLanguage) ? this.currentLanguage : "en_us";
      return (Language)this.languageMap.get(s);
   }

   public SortedSet<Language> getLanguages() {
      return Sets.newTreeSet(this.languageMap.values());
   }

   public Language func_191960_a(String p_191960_1_) {
      return (Language)this.languageMap.get(p_191960_1_);
   }
}
