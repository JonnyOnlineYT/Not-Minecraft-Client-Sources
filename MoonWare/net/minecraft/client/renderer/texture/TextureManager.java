package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Namespaced;
import optifine.Config;
import optifine.CustomGuis;
import optifine.RandomMobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class TextureManager implements ITickable, IResourceManagerReloadListener
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Namespaced field_194008_a = new Namespaced("");
    private final Map<Namespaced, ITextureObject> mapTextureObjects = Maps.newHashMap();
    private final List<ITickable> listTickables = Lists.newArrayList();
    private final Map<String, Integer> mapTextureCounters = Maps.newHashMap();
    private final IResourceManager theResourceManager;

    public TextureManager(IResourceManager resourceManager)
    {
        theResourceManager = resourceManager;
    }

    public void bindTexture(Namespaced resource)
    {
        if (Config.isRandomMobs())
        {
            resource = RandomMobs.getTextureLocation(resource);
        }

        if (Config.isCustomGuis())
        {
            resource = CustomGuis.getTextureLocation(resource);
        }

        ITextureObject itextureobject = mapTextureObjects.get(resource);

        if (itextureobject == null)
        {
            itextureobject = new SimpleTexture(resource);
            loadTexture(resource, itextureobject);
        }

        if (Config.isShaders())
        {
            ShadersTex.bindTexture(itextureobject);
        }
        else
        {
            TextureUtil.bindTexture(itextureobject.getGlTextureId());
        }
    }

    public boolean loadTickableTexture(Namespaced textureLocation, ITickableTextureObject textureObj)
    {
        if (loadTexture(textureLocation, textureObj))
        {
            listTickables.add(textureObj);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean loadTexture(Namespaced textureLocation, ITextureObject textureObj)
    {
        boolean flag = true;

        try
        {
            textureObj.loadTexture(theResourceManager);
        }
        catch (IOException ioexception)
        {
            if (textureLocation != field_194008_a)
            {
                LOGGER.warn("Failed to load texture: {}", textureLocation, ioexception);
            }

            textureObj = TextureUtil.MISSING_TEXTURE;
            mapTextureObjects.put(textureLocation, textureObj);
            flag = false;
        }
        catch (Throwable throwable)
        {
            ITextureObject textureObjf = textureObj;
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            crashreportcategory.setDetail("Texture object class", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return textureObjf.getClass().getName();
                }
            });
            throw new ReportedException(crashreport);
        }

        mapTextureObjects.put(textureLocation, textureObj);
        return flag;
    }

    public ITextureObject getTexture(Namespaced textureLocation)
    {
        return mapTextureObjects.get(textureLocation);
    }

    public Namespaced getDynamicTextureLocation(String name, DynamicTexture texture)
    {
        if (name.equals("logo"))
        {
            texture = Config.getMojangLogoTexture(texture);
        }

        Integer integer = mapTextureCounters.get(name);

        if (integer == null)
        {
            integer = Integer.valueOf(1);
        }
        else
        {
            integer = integer.intValue() + 1;
        }

        mapTextureCounters.put(name, integer);
        Namespaced resourcelocation = new Namespaced(String.format("dynamic/%s_%d", name, integer));
        loadTexture(resourcelocation, texture);
        return resourcelocation;
    }

    public void tick()
    {
        for (ITickable itickable : listTickables)
        {
            itickable.tick();
        }
    }

    public void deleteTexture(Namespaced textureLocation)
    {
        ITextureObject itextureobject = getTexture(textureLocation);

        if (itextureobject != null)
        {
            mapTextureObjects.remove(textureLocation);
            TextureUtil.deleteTexture(itextureobject.getGlTextureId());
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());
        Iterator iterator = mapTextureObjects.keySet().iterator();

        while (iterator.hasNext())
        {
            Namespaced resourcelocation = (Namespaced)iterator.next();
            String s = resourcelocation.getPath();

            if (s.startsWith("mcpatcher/") || s.startsWith("optifine/"))
            {
                ITextureObject itextureobject = mapTextureObjects.get(resourcelocation);

                if (itextureobject instanceof AbstractTexture)
                {
                    AbstractTexture abstracttexture = (AbstractTexture)itextureobject;
                    abstracttexture.deleteGlTexture();
                }

                iterator.remove();
            }
        }

        Iterator<Map.Entry<Namespaced, ITextureObject>> iterator1 = mapTextureObjects.entrySet().iterator();

        while (iterator1.hasNext())
        {
            Map.Entry<Namespaced, ITextureObject> entry = iterator1.next();
            ITextureObject itextureobject1 = entry.getValue();

            if (itextureobject1 == TextureUtil.MISSING_TEXTURE)
            {
                iterator1.remove();
            }
            else
            {
                loadTexture(entry.getKey(), itextureobject1);
            }
        }
    }

    public void reloadBannerTextures()
    {
        for (Map.Entry<Namespaced, ITextureObject> entry : mapTextureObjects.entrySet())
        {
            Namespaced resourcelocation = entry.getKey();
            ITextureObject itextureobject = entry.getValue();

            if (itextureobject instanceof LayeredColorMaskTexture)
            {
                loadTexture(resourcelocation, itextureobject);
            }
        }
    }
}
