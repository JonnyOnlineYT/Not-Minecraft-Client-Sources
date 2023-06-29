package net.minecraft.world.gen.structure.template;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Namespaced;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Map;

public class TemplateManager
{
    private final Map<String, Template> templates = Maps.newHashMap();

    /**
     * the folder in the assets folder where the structure templates are found.
     */
    private final String baseFolder;
    private final DataFixer field_191154_c;

    public TemplateManager(String p_i47239_1_, DataFixer p_i47239_2_)
    {
        baseFolder = p_i47239_1_;
        field_191154_c = p_i47239_2_;
    }

    public Template getTemplate(@Nullable MinecraftServer server, Namespaced id)
    {
        Template template = get(server, id);

        if (template == null)
        {
            template = new Template();
            templates.put(id.getPath(), template);
        }

        return template;
    }

    @Nullable
    public Template get(@Nullable MinecraftServer p_189942_1_, Namespaced p_189942_2_)
    {
        String s = p_189942_2_.getPath();

        if (templates.containsKey(s))
        {
            return templates.get(s);
        }
        else
        {
            if (p_189942_1_ == null)
            {
                readTemplateFromJar(p_189942_2_);
            }
            else
            {
                readTemplate(p_189942_2_);
            }

            return templates.containsKey(s) ? templates.get(s) : null;
        }
    }

    /**
     * This reads a structure template from the given location and stores it.
     * This first attempts get the template from an external folder.
     * If it isn't there then it attempts to take it from the minecraft jar.
     */
    public boolean readTemplate(Namespaced server)
    {
        String s = server.getPath();
        File file1 = new File(baseFolder, s + ".nbt");

        if (!file1.exists())
        {
            return readTemplateFromJar(server);
        }
        else
        {
            InputStream inputstream = null;
            boolean flag;

            try
            {
                inputstream = new FileInputStream(file1);
                readTemplateFromStream(s, inputstream);
                return true;
            }
            catch (Throwable var10)
            {
                flag = false;
            }
            finally
            {
                IOUtils.closeQuietly(inputstream);
            }

            return flag;
        }
    }

    /**
     * reads a template from the minecraft jar
     */
    private boolean readTemplateFromJar(Namespaced id)
    {
        String s = id.getNamespace();
        String s1 = id.getPath();
        InputStream inputstream = null;
        boolean flag;

        try
        {
            inputstream = MinecraftServer.class.getResourceAsStream("/assets/" + s + "/structures/" + s1 + ".nbt");
            readTemplateFromStream(s1, inputstream);
            return true;
        }
        catch (Throwable var10)
        {
            flag = false;
        }
        finally
        {
            IOUtils.closeQuietly(inputstream);
        }

        return flag;
    }

    /**
     * reads a template from an inputstream
     */
    private void readTemplateFromStream(String id, InputStream stream) throws IOException
    {
        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(stream);

        if (!nbttagcompound.hasKey("DataVersion", 99))
        {
            nbttagcompound.setInteger("DataVersion", 500);
        }

        Template template = new Template();
        template.read(field_191154_c.process(FixTypes.STRUCTURE, nbttagcompound));
        templates.put(id, template);
    }

    /**
     * writes the template to an external folder
     */
    public boolean writeTemplate(@Nullable MinecraftServer server, Namespaced id)
    {
        String s = id.getPath();

        if (server != null && templates.containsKey(s))
        {
            File file1 = new File(baseFolder);

            if (!file1.exists())
            {
                if (!file1.mkdirs())
                {
                    return false;
                }
            }
            else if (!file1.isDirectory())
            {
                return false;
            }

            File file2 = new File(file1, s + ".nbt");
            Template template = templates.get(s);
            OutputStream outputstream = null;
            boolean flag;

            try
            {
                NBTTagCompound nbttagcompound = template.writeToNBT(new NBTTagCompound());
                outputstream = new FileOutputStream(file2);
                CompressedStreamTools.writeCompressed(nbttagcompound, outputstream);
                return true;
            }
            catch (Throwable var13)
            {
                flag = false;
            }
            finally
            {
                IOUtils.closeQuietly(outputstream);
            }

            return flag;
        }
        else
        {
            return false;
        }
    }

    public void remove(Namespaced p_189941_1_)
    {
        templates.remove(p_189941_1_.getPath());
    }
}
