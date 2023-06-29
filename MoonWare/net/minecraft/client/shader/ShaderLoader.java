package net.minecraft.client.shader;

import com.google.common.collect.Maps;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.Namespaced;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;

public class ShaderLoader
{
    private final ShaderLoader.ShaderType shaderType;
    private final String shaderFilename;
    private final int shader;
    private int shaderAttachCount;

    private ShaderLoader(ShaderLoader.ShaderType type, int shaderId, String filename)
    {
        shaderType = type;
        shader = shaderId;
        shaderFilename = filename;
    }

    public void attachShader(ShaderManager manager)
    {
        ++shaderAttachCount;
        OpenGlHelper.glAttachShader(manager.getProgram(), shader);
    }

    public void deleteShader(ShaderManager manager)
    {
        --shaderAttachCount;

        if (shaderAttachCount <= 0)
        {
            OpenGlHelper.glDeleteShader(shader);
            shaderType.getLoadedShaders().remove(shaderFilename);
        }
    }

    public String getShaderFilename()
    {
        return shaderFilename;
    }

    public static ShaderLoader loadShader(IResourceManager resourceManager, ShaderLoader.ShaderType type, String filename) throws IOException
    {
        ShaderLoader shaderloader = type.getLoadedShaders().get(filename);

        if (shaderloader == null)
        {
            Namespaced resourcelocation = new Namespaced("shaders/program/" + filename + type.getShaderExtension());
            IResource iresource = resourceManager.getResource(resourcelocation);

            try
            {
                byte[] abyte = IOUtils.toByteArray(new BufferedInputStream(iresource.getInputStream()));
                ByteBuffer bytebuffer = BufferUtils.createByteBuffer(abyte.length);
                bytebuffer.put(abyte);
                bytebuffer.position(0);
                int i = OpenGlHelper.glCreateShader(type.getShaderMode());
                OpenGlHelper.glShaderSource(i, bytebuffer);
                OpenGlHelper.glCompileShader(i);

                if (OpenGlHelper.glGetShaderi(i, OpenGlHelper.GL_COMPILE_STATUS) == 0)
                {
                    String s = OpenGlHelper.glGetShaderInfoLog(i, 32768).trim();
                    JsonException jsonexception = new JsonException("Couldn't compile " + type.getShaderName() + " program: " + s);
                    jsonexception.setFilenameAndFlush(resourcelocation.getPath());
                    throw jsonexception;
                }

                shaderloader = new ShaderLoader(type, i, filename);
                type.getLoadedShaders().put(filename, shaderloader);
            }
            finally
            {
                IOUtils.closeQuietly(iresource);
            }
        }

        return shaderloader;
    }

    public enum ShaderType
    {
        VERTEX("vertex", ".vsh", OpenGlHelper.GL_VERTEX_SHADER),
        FRAGMENT("fragment", ".fsh", OpenGlHelper.GL_FRAGMENT_SHADER);

        private final String shaderName;
        private final String shaderExtension;
        private final int shaderMode;
        private final Map<String, ShaderLoader> loadedShaders = Maps.newHashMap();

        ShaderType(String shaderNameIn, String shaderExtensionIn, int shaderModeIn)
        {
            shaderName = shaderNameIn;
            shaderExtension = shaderExtensionIn;
            shaderMode = shaderModeIn;
        }

        public String getShaderName()
        {
            return shaderName;
        }

        private String getShaderExtension()
        {
            return shaderExtension;
        }

        private int getShaderMode()
        {
            return shaderMode;
        }

        private Map<String, ShaderLoader> getLoadedShaders()
        {
            return loadedShaders;
        }
    }
}
