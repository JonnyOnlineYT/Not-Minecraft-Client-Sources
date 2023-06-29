package shadersmod.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import optifine.StrUtils;

public class ShaderPackZip implements IShaderPack
{
    protected File packFile;
    protected ZipFile packZipFile;

    public ShaderPackZip(String name, File file)
    {
        packFile = file;
        packZipFile = null;
    }

    public void close()
    {
        if (packZipFile != null)
        {
            try
            {
                packZipFile.close();
            }
            catch (Exception var2)
            {
            }

            packZipFile = null;
        }
    }

    public InputStream getResourceAsStream(String resName)
    {
        try
        {
            if (packZipFile == null)
            {
                packZipFile = new ZipFile(packFile);
            }

            String s = StrUtils.removePrefix(resName, "/");
            ZipEntry zipentry = packZipFile.getEntry(s);
            return zipentry == null ? null : packZipFile.getInputStream(zipentry);
        }
        catch (Exception var4)
        {
            return null;
        }
    }

    public boolean hasDirectory(String resName)
    {
        try
        {
            if (packZipFile == null)
            {
                packZipFile = new ZipFile(packFile);
            }

            String s = StrUtils.removePrefix(resName, "/");
            ZipEntry zipentry = packZipFile.getEntry(s);
            return zipentry != null;
        }
        catch (IOException var4)
        {
            return false;
        }
    }

    public String getName()
    {
        return packFile.getName();
    }
}
