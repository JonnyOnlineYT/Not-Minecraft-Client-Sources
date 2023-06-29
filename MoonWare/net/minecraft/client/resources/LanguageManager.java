package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.text.translation.LanguageMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class LanguageManager implements IResourceManagerReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private final MetadataSerializer theMetadataSerializer;
    private String currentLanguage;
    protected static final Locale CURRENT_LOCALE = new Locale();
    private final Map<String, Language> languageMap = Maps.newHashMap();

    public LanguageManager(MetadataSerializer theMetadataSerializerIn, String currentLanguageIn) {
        theMetadataSerializer = theMetadataSerializerIn;
        currentLanguage = currentLanguageIn;
        I18n.setLocale(CURRENT_LOCALE);
    }

    public void parseLanguageMetadata(List<IResourcePack> resourcesPacks) {
        languageMap.clear();

        for (IResourcePack iresourcepack : resourcesPacks) {
            try {
                LanguageMetadataSection languagemetadatasection = iresourcepack.getPackMetadata(theMetadataSerializer, "language");

                if (languagemetadatasection != null) {
                    for (Language language : languagemetadatasection.getLanguages()) {
                        if (!languageMap.containsKey(language.getCode())) {
                            languageMap.put(language.getCode(), language);
                        }
                    }
                }
            } catch (RuntimeException runtimeexception) {
                LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", iresourcepack.getPackName(), runtimeexception);
            } catch (IOException ioexception) {
                LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", iresourcepack.getPackName(), ioexception);
            }
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        List<String> list = Lists.newArrayList("en_us");

        if (!"en_us".equals(currentLanguage)) {
            list.add(currentLanguage);
        }

        CURRENT_LOCALE.loadLocaleDataFiles(resourceManager, list);
        LanguageMap.replaceWith(CURRENT_LOCALE.properties);
    }

    public boolean isCurrentLocaleUnicode() {
        return CURRENT_LOCALE.isUnicode();
    }

    public boolean isCurrentLanguageBidirectional() {
        return getCurrentLanguage() != null && getCurrentLanguage().isBidirectional();
    }

    public void setCurrentLanguage(Language currentLanguageIn) {
        currentLanguage = currentLanguageIn.getCode();
    }

    public Language getCurrentLanguage() {
        String s = languageMap.containsKey(currentLanguage) ? currentLanguage : "en_us";
        return languageMap.get(s);
    }

    public SortedSet<Language> getLanguages() {
        return Sets.newTreeSet(languageMap.values());
    }

    public Language func_191960_a(String p_191960_1_) {
        return languageMap.get(p_191960_1_);
    }
}
