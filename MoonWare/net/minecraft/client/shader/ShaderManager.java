package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonBlendingMode;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Namespaced;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ShaderManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ShaderDefault DEFAULT_SHADER_UNIFORM = new ShaderDefault();
    private static ShaderManager staticShaderManager;
    private static int currentProgram = -1;
    private static boolean lastCull = true;
    private final Map<String, Object> shaderSamplers = Maps.newHashMap();
    private final List<String> samplerNames = Lists.newArrayList();
    private final List<Integer> shaderSamplerLocations = Lists.newArrayList();
    private final List<ShaderUniform> shaderUniforms = Lists.newArrayList();
    private final List<Integer> shaderUniformLocations = Lists.newArrayList();
    private final Map<String, ShaderUniform> mappedShaderUniforms = Maps.newHashMap();
    private final int program;
    private final String programFilename;
    private final boolean useFaceCulling;
    public String programFilenamee;
    private boolean isDirty;
    private final JsonBlendingMode blendingMode;
    private final List<Integer> attribLocations;
    private final List<String> attributes;
    private final ShaderLoader vertexShaderLoader;
    private final ShaderLoader fragmentShaderLoader;

    public ShaderManager(IResourceManager resourceManager, String programName) throws IOException
    {
        JsonParser jsonparser = new JsonParser();
        Namespaced resourcelocation = new Namespaced("shaders/program/" + programName + ".json");
        programFilename = programName;
        programFilenamee = programFilename;
        IResource iresource = null;

        try
        {
            iresource = resourceManager.getResource(resourcelocation);
            JsonObject jsonobject = jsonparser.parse(IOUtils.toString(iresource.getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
            String s = JsonUtils.getString(jsonobject, "vertex");
            String s1 = JsonUtils.getString(jsonobject, "fragment");
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "samplers", null);

            if (jsonarray != null)
            {
                int i = 0;

                for (JsonElement jsonelement : jsonarray)
                {
                    try
                    {
                        parseSampler(jsonelement);
                    }
                    catch (Exception exception2)
                    {
                        JsonException jsonexception1 = JsonException.forException(exception2);
                        jsonexception1.prependJsonKey("samplers[" + i + "]");
                        throw jsonexception1;
                    }

                    ++i;
                }
            }

            JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonobject, "attributes", null);

            if (jsonarray1 != null)
            {
                int j = 0;
                attribLocations = Lists.newArrayListWithCapacity(jsonarray1.size());
                attributes = Lists.newArrayListWithCapacity(jsonarray1.size());

                for (JsonElement jsonelement1 : jsonarray1)
                {
                    try
                    {
                        attributes.add(JsonUtils.getString(jsonelement1, "attribute"));
                    }
                    catch (Exception exception1)
                    {
                        JsonException jsonexception2 = JsonException.forException(exception1);
                        jsonexception2.prependJsonKey("attributes[" + j + "]");
                        throw jsonexception2;
                    }

                    ++j;
                }
            }
            else
            {
                attribLocations = null;
                attributes = null;
            }

            JsonArray jsonarray2 = JsonUtils.getJsonArray(jsonobject, "uniforms", null);

            if (jsonarray2 != null)
            {
                int k = 0;

                for (JsonElement jsonelement2 : jsonarray2)
                {
                    try
                    {
                        parseUniform(jsonelement2);
                    }
                    catch (Exception exception)
                    {
                        JsonException jsonexception3 = JsonException.forException(exception);
                        jsonexception3.prependJsonKey("uniforms[" + k + "]");
                        throw jsonexception3;
                    }

                    ++k;
                }
            }

            blendingMode = JsonBlendingMode.parseBlendNode(JsonUtils.getJsonObject(jsonobject, "blend", null));
            useFaceCulling = JsonUtils.getBoolean(jsonobject, "cull", true);
            vertexShaderLoader = ShaderLoader.loadShader(resourceManager, ShaderLoader.ShaderType.VERTEX, s);
            fragmentShaderLoader = ShaderLoader.loadShader(resourceManager, ShaderLoader.ShaderType.FRAGMENT, s1);
            program = ShaderLinkHelper.getStaticShaderLinkHelper().createProgram();
            ShaderLinkHelper.getStaticShaderLinkHelper().linkProgram(this);
            setupUniforms();

            if (attributes != null)
            {
                for (String s2 : attributes)
                {
                    int l = OpenGlHelper.glGetAttribLocation(program, s2);
                    attribLocations.add(Integer.valueOf(l));
                }
            }
        }
        catch (Exception exception3)
        {
            JsonException jsonexception = JsonException.forException(exception3);
            jsonexception.setFilenameAndFlush(resourcelocation.getPath());
            throw jsonexception;
        }
        finally
        {
            IOUtils.closeQuietly(iresource);
        }

        markDirty();
    }

    public void deleteShader()
    {
        ShaderLinkHelper.getStaticShaderLinkHelper().deleteShader(this);
    }

    public static void getProgramFileName() {

    }
    public void endShader()
    {
        OpenGlHelper.glUseProgram(0);
        currentProgram = -1;
        staticShaderManager = null;
        lastCull = true;

        for (int i = 0; i < shaderSamplerLocations.size(); ++i)
        {
            if (shaderSamplers.get(samplerNames.get(i)) != null)
            {
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + i);
                GlStateManager.bindTexture(0);
            }
        }
    }

    public void useShader()
    {
        isDirty = false;
        staticShaderManager = this;
        blendingMode.apply();

        if (program != currentProgram)
        {
            OpenGlHelper.glUseProgram(program);
            currentProgram = program;
        }

        if (useFaceCulling)
        {
            GlStateManager.enableCull();
        }
        else
        {
            GlStateManager.disableCull();
        }

        for (int i = 0; i < shaderSamplerLocations.size(); ++i)
        {
            if (shaderSamplers.get(samplerNames.get(i)) != null)
            {
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + i);
                GlStateManager.enableTexture2D();
                Object object = shaderSamplers.get(samplerNames.get(i));
                int j = -1;

                if (object instanceof Framebuffer)
                {
                    j = ((Framebuffer)object).framebufferTexture;
                }
                else if (object instanceof ITextureObject)
                {
                    j = ((ITextureObject)object).getGlTextureId();
                }
                else if (object instanceof Integer)
                {
                    j = ((Integer)object).intValue();
                }

                if (j != -1)
                {
                    GlStateManager.bindTexture(j);
                    OpenGlHelper.glUniform1i(OpenGlHelper.glGetUniformLocation(program, samplerNames.get(i)), i);
                }
            }
        }

        for (ShaderUniform shaderuniform : shaderUniforms)
        {
            shaderuniform.upload();
        }
    }

    public void markDirty()
    {
        isDirty = true;
    }

    @Nullable

    /**
     * gets a shader uniform for the name given. null if not found.
     */
    public ShaderUniform getShaderUniform(String name)
    {
        return mappedShaderUniforms.get(name);
    }

    /**
     * gets a shader uniform for the name given. if not found, returns a default not-null value
     */
    public ShaderUniform getShaderUniformOrDefault(String name)
    {
        ShaderUniform shaderuniform = getShaderUniform(name);
        return shaderuniform == null ? DEFAULT_SHADER_UNIFORM : shaderuniform;
    }

    /**
     * goes through the parsed uniforms and samplers and connects them to their GL counterparts.
     */
    private void setupUniforms()
    {
        int i = 0;

        for (int j = 0; i < samplerNames.size(); ++j)
        {
            String s = samplerNames.get(i);
            int k = OpenGlHelper.glGetUniformLocation(program, s);

            if (k == -1)
            {
                LOGGER.warn("Shader {}could not find sampler named {} in the specified shader program.", programFilename, s);
                shaderSamplers.remove(s);
                samplerNames.remove(j);
                --j;
            }
            else
            {
                shaderSamplerLocations.add(Integer.valueOf(k));
            }

            ++i;
        }

        for (ShaderUniform shaderuniform : shaderUniforms)
        {
            String s1 = shaderuniform.getShaderName();
            int l = OpenGlHelper.glGetUniformLocation(program, s1);

            if (l == -1)
            {
                LOGGER.warn("Could not find uniform named {} in the specified shader program.", s1);
            }
            else
            {
                shaderUniformLocations.add(Integer.valueOf(l));
                shaderuniform.setUniformLocation(l);
                mappedShaderUniforms.put(s1, shaderuniform);
            }
        }
    }

    private void parseSampler(JsonElement element) throws JsonException
    {
        JsonObject jsonobject = JsonUtils.getJsonObject(element, "sampler");
        String s = JsonUtils.getString(jsonobject, "name");

        if (!JsonUtils.isString(jsonobject, "file"))
        {
            shaderSamplers.put(s, null);
            samplerNames.add(s);
        }
        else
        {
            samplerNames.add(s);
        }
    }

    /**
     * adds a shader sampler texture. if it already exists, replaces it.
     */
    public void addSamplerTexture(String name, Object samplerTexture)
    {
        shaderSamplers.remove(name);

        shaderSamplers.put(name, samplerTexture);
        markDirty();
    }

    private void parseUniform(JsonElement element) throws JsonException
    {
        JsonObject jsonobject = JsonUtils.getJsonObject(element, "uniform");
        String s = JsonUtils.getString(jsonobject, "name");
        int i = ShaderUniform.parseType(JsonUtils.getString(jsonobject, "type"));
        int j = JsonUtils.getInt(jsonobject, "count");
        float[] afloat = new float[Math.max(j, 16)];
        JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "values");

        if (jsonarray.size() != j && jsonarray.size() > 1)
        {
            throw new JsonException("Invalid amount of values specified (expected " + j + ", found " + jsonarray.size() + ")");
        }
        else
        {
            int k = 0;

            for (JsonElement jsonelement : jsonarray)
            {
                try
                {
                    afloat[k] = JsonUtils.getFloat(jsonelement, "value");
                }
                catch (Exception exception)
                {
                    JsonException jsonexception = JsonException.forException(exception);
                    jsonexception.prependJsonKey("values[" + k + "]");
                    throw jsonexception;
                }

                ++k;
            }

            if (j > 1 && jsonarray.size() == 1)
            {
                while (k < j)
                {
                    afloat[k] = afloat[0];
                    ++k;
                }
            }

            int l = j > 1 && j <= 4 && i < 8 ? j - 1 : 0;
            ShaderUniform shaderuniform = new ShaderUniform(s, i + l, j, this);

            if (i <= 3)
            {
                shaderuniform.set((int)afloat[0], (int)afloat[1], (int)afloat[2], (int)afloat[3]);
            }
            else if (i <= 7)
            {
                shaderuniform.setSafe(afloat[0], afloat[1], afloat[2], afloat[3]);
            }
            else
            {
                shaderuniform.set(afloat);
            }

            shaderUniforms.add(shaderuniform);
        }
    }

    public ShaderLoader getVertexShaderLoader()
    {
        return vertexShaderLoader;
    }

    public ShaderLoader getFragmentShaderLoader()
    {
        return fragmentShaderLoader;
    }

    public int getProgram()
    {
        return program;
    }
}
