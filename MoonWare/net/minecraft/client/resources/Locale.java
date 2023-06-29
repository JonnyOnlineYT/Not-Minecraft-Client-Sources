package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.util.Namespaced;
import org.apache.commons.io.IOUtils;

public class Locale
{
    /** Splits on "=" */
    private static final Splitter SPLITTER = Splitter.on('=').limit(2);
    private static final Pattern PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    Map<String, String> properties = Maps.newHashMap();
    private boolean unicode;

    /**
     * For each domain $D and language $L, attempts to load the resource $D:lang/$L.lang
     */
    public synchronized void loadLocaleDataFiles(IResourceManager resourceManager, List<String> languageList)
    {
        properties.clear();

        for (String s : languageList)
        {
            String s1 = String.format("lang/%s.lang", s);

            for (String s2 : resourceManager.getResourceDomains())
            {
                try
                {
                    loadLocaleData(resourceManager.getAllResources(new Namespaced(s2, s1)));
                }
                catch (IOException var9)
                {
                }
            }
        }

        checkUnicode();
    }

    public boolean isUnicode()
    {
        return unicode;
    }

    private void checkUnicode()
    {
        unicode = false;
        int i = 0;
        int j = 0;

        for (String s : properties.values())
        {
            int k = s.length();
            j += k;

            for (int l = 0; l < k; ++l)
            {
                if (s.charAt(l) >= 256)
                {
                    ++i;
                }
            }
        }

        float f = (float)i / (float)j;
        unicode = (double)f > 0.1D;
    }

    /**
     * Loads the locale data for the list of resources.
     */
    private void loadLocaleData(List<IResource> resourcesList) throws IOException
    {
        for (IResource iresource : resourcesList)
        {
            InputStream inputstream = iresource.getInputStream();

            try
            {
                loadLocaleData(inputstream);
            }
            finally
            {
                IOUtils.closeQuietly(inputstream);
            }
        }
    }

    private void loadLocaleData(InputStream inputStreamIn) throws IOException
    {
        for (String s : IOUtils.readLines(inputStreamIn, StandardCharsets.UTF_8))
        {
            if (!s.isEmpty() && s.charAt(0) != '#')
            {
                String[] astring = Iterables.toArray(SPLITTER.split(s), String.class);

                if (astring != null && astring.length == 2)
                {
                    String s1 = astring[0];
                    String s2 = PATTERN.matcher(astring[1]).replaceAll("%$1s");
                    properties.put(s1, s2);
                }
            }
        }
    }

    /**
     * Returns the translation, or the key itself if the key could not be translated.
     */
    private String translateKeyPrivate(String translateKey)
    {
        String s = properties.get(translateKey);
        return s == null ? translateKey : s;
    }

    /**
     * Calls String.format(translateKey(key), params)
     */
    public String formatMessage(String translateKey, Object[] parameters)
    {
        String s = translateKeyPrivate(translateKey);

        try
        {
            return String.format(s, parameters);
        }
        catch (IllegalFormatException var5)
        {
            return "Format error: " + s;
        }
    }

    public boolean hasKey(String key)
    {
        return properties.containsKey(key);
    }
}
