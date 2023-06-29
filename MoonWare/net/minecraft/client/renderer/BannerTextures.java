package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.Namespaced;

public class BannerTextures
{
    /** An array of all the banner patterns that are being currently rendered */
    public static final BannerTextures.Cache BANNER_DESIGNS = new BannerTextures.Cache("B", new Namespaced("textures/entity/banner_base.png"), "textures/entity/banner/");

    /** An array of all the shield patterns that are being currently rendered */
    public static final BannerTextures.Cache SHIELD_DESIGNS = new BannerTextures.Cache("S", new Namespaced("textures/entity/shield_base.png"), "textures/entity/shield/");
    public static final Namespaced SHIELD_BASE_TEXTURE = new Namespaced("textures/entity/shield_base_nopattern.png");
    public static final Namespaced BANNER_BASE_TEXTURE = new Namespaced("textures/entity/banner/base.png");

    public static class Cache
    {
        private final Map<String, BannerTextures.CacheEntry> cacheMap = Maps.newLinkedHashMap();
        private final Namespaced cacheNamespaced;
        private final String cacheResourceBase;
        private final String cacheId;

        public Cache(String id, Namespaced baseResource, String resourcePath)
        {
            cacheId = id;
            cacheNamespaced = baseResource;
            cacheResourceBase = resourcePath;
        }

        @Nullable
        public Namespaced getResourceLocation(String id, List<BannerPattern> patternList, List<EnumDyeColor> colorList)
        {
            if (id.isEmpty())
            {
                return null;
            }
            else
            {
                id = cacheId + id;
                BannerTextures.CacheEntry bannertextures$cacheentry = cacheMap.get(id);

                if (bannertextures$cacheentry == null)
                {
                    if (cacheMap.size() >= 256 && !freeCacheSlot())
                    {
                        return BANNER_BASE_TEXTURE;
                    }

                    List<String> list = Lists.newArrayList();

                    for (BannerPattern bannerpattern : patternList)
                    {
                        list.add(cacheResourceBase + bannerpattern.func_190997_a() + ".png");
                    }

                    bannertextures$cacheentry = new BannerTextures.CacheEntry();
                    bannertextures$cacheentry.textureLocation = new Namespaced(id);
                    Minecraft.getTextureManager().loadTexture(bannertextures$cacheentry.textureLocation, new LayeredColorMaskTexture(cacheNamespaced, list, colorList));
                    cacheMap.put(id, bannertextures$cacheentry);
                }

                bannertextures$cacheentry.lastUseMillis = System.currentTimeMillis();
                return bannertextures$cacheentry.textureLocation;
            }
        }

        private boolean freeCacheSlot()
        {
            long i = System.currentTimeMillis();
            Iterator<String> iterator = cacheMap.keySet().iterator();

            while (iterator.hasNext())
            {
                String s = iterator.next();
                BannerTextures.CacheEntry bannertextures$cacheentry = cacheMap.get(s);

                if (i - bannertextures$cacheentry.lastUseMillis > 5000L)
                {
                    Minecraft.getTextureManager().deleteTexture(bannertextures$cacheentry.textureLocation);
                    iterator.remove();
                    return true;
                }
            }

            return cacheMap.size() < 256;
        }
    }

    static class CacheEntry
    {
        public long lastUseMillis;
        public Namespaced textureLocation;

        private CacheEntry()
        {
        }
    }
}
