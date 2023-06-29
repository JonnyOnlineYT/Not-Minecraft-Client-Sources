package net.minecraft.client.shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

public class ShaderUniform
{
    private static final Logger LOGGER = LogManager.getLogger();
    private int uniformLocation;
    private final int uniformCount;
    private final int uniformType;
    private final IntBuffer uniformIntBuffer;
    private final FloatBuffer uniformFloatBuffer;
    private final String shaderName;
    private boolean dirty;
    private final ShaderManager shaderManager;

    public ShaderUniform(String name, int type, int count, ShaderManager manager)
    {
        shaderName = name;
        uniformCount = count;
        uniformType = type;
        shaderManager = manager;

        if (type <= 3)
        {
            uniformIntBuffer = BufferUtils.createIntBuffer(count);
            uniformFloatBuffer = null;
        }
        else
        {
            uniformIntBuffer = null;
            uniformFloatBuffer = BufferUtils.createFloatBuffer(count);
        }

        uniformLocation = -1;
        markDirty();
    }

    private void markDirty()
    {
        dirty = true;

        if (shaderManager != null)
        {
            shaderManager.markDirty();
        }
    }

    public static int parseType(String typeName)
    {
        int i = -1;

        if ("int".equals(typeName))
        {
            i = 0;
        }
        else if ("float".equals(typeName))
        {
            i = 4;
        }
        else if (typeName.startsWith("matrix"))
        {
            if (typeName.endsWith("2x2"))
            {
                i = 8;
            }
            else if (typeName.endsWith("3x3"))
            {
                i = 9;
            }
            else if (typeName.endsWith("4x4"))
            {
                i = 10;
            }
        }

        return i;
    }

    public void setUniformLocation(int uniformLocationIn)
    {
        uniformLocation = uniformLocationIn;
    }

    public String getShaderName()
    {
        return shaderName;
    }

    public void set(float p_148090_1_)
    {
        uniformFloatBuffer.position(0);
        uniformFloatBuffer.put(0, p_148090_1_);
        markDirty();
    }

    public void set(float p_148087_1_, float p_148087_2_)
    {
        uniformFloatBuffer.position(0);
        uniformFloatBuffer.put(0, p_148087_1_);
        uniformFloatBuffer.put(1, p_148087_2_);
        markDirty();
    }

    public void set(float p_148095_1_, float p_148095_2_, float p_148095_3_)
    {
        uniformFloatBuffer.position(0);
        uniformFloatBuffer.put(0, p_148095_1_);
        uniformFloatBuffer.put(1, p_148095_2_);
        uniformFloatBuffer.put(2, p_148095_3_);
        markDirty();
    }

    public void set(float p_148081_1_, float p_148081_2_, float p_148081_3_, float p_148081_4_)
    {
        uniformFloatBuffer.position(0);
        uniformFloatBuffer.put(p_148081_1_);
        uniformFloatBuffer.put(p_148081_2_);
        uniformFloatBuffer.put(p_148081_3_);
        uniformFloatBuffer.put(p_148081_4_);
        uniformFloatBuffer.flip();
        markDirty();
    }

    public void setSafe(float p_148092_1_, float p_148092_2_, float p_148092_3_, float p_148092_4_)
    {
        uniformFloatBuffer.position(0);

        if (uniformType >= 4)
        {
            uniformFloatBuffer.put(0, p_148092_1_);
        }

        if (uniformType >= 5)
        {
            uniformFloatBuffer.put(1, p_148092_2_);
        }

        if (uniformType >= 6)
        {
            uniformFloatBuffer.put(2, p_148092_3_);
        }

        if (uniformType >= 7)
        {
            uniformFloatBuffer.put(3, p_148092_4_);
        }

        markDirty();
    }

    public void set(int p_148083_1_, int p_148083_2_, int p_148083_3_, int p_148083_4_)
    {
        uniformIntBuffer.position(0);

        if (uniformType >= 0)
        {
            uniformIntBuffer.put(0, p_148083_1_);
        }

        if (uniformType >= 1)
        {
            uniformIntBuffer.put(1, p_148083_2_);
        }

        if (uniformType >= 2)
        {
            uniformIntBuffer.put(2, p_148083_3_);
        }

        if (uniformType >= 3)
        {
            uniformIntBuffer.put(3, p_148083_4_);
        }

        markDirty();
    }

    public void set(float[] p_148097_1_)
    {
        if (p_148097_1_.length < uniformCount)
        {
            LOGGER.warn("Uniform.set called with a too-small value array (expected {}, got {}). Ignoring.", Integer.valueOf(uniformCount), Integer.valueOf(p_148097_1_.length));
        }
        else
        {
            uniformFloatBuffer.position(0);
            uniformFloatBuffer.put(p_148097_1_);
            uniformFloatBuffer.position(0);
            markDirty();
        }
    }

    public void set(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33)
    {
        uniformFloatBuffer.position(0);
        uniformFloatBuffer.put(0, m00);
        uniformFloatBuffer.put(1, m01);
        uniformFloatBuffer.put(2, m02);
        uniformFloatBuffer.put(3, m03);
        uniformFloatBuffer.put(4, m10);
        uniformFloatBuffer.put(5, m11);
        uniformFloatBuffer.put(6, m12);
        uniformFloatBuffer.put(7, m13);
        uniformFloatBuffer.put(8, m20);
        uniformFloatBuffer.put(9, m21);
        uniformFloatBuffer.put(10, m22);
        uniformFloatBuffer.put(11, m23);
        uniformFloatBuffer.put(12, m30);
        uniformFloatBuffer.put(13, m31);
        uniformFloatBuffer.put(14, m32);
        uniformFloatBuffer.put(15, m33);
        markDirty();
    }

    public void set(Matrix4f matrix)
    {
        set(matrix.m00, matrix.m01, matrix.m02, matrix.m03, matrix.m10, matrix.m11, matrix.m12, matrix.m13, matrix.m20, matrix.m21, matrix.m22, matrix.m23, matrix.m30, matrix.m31, matrix.m32, matrix.m33);
    }

    public void upload()
    {
        if (!dirty)
        {
        }

        dirty = false;

        if (uniformType <= 3)
        {
            uploadInt();
        }
        else if (uniformType <= 7)
        {
            uploadFloat();
        }
        else
        {
            if (uniformType > 10)
            {
                LOGGER.warn("Uniform.upload called, but type value ({}) is not a valid type. Ignoring.", uniformType);
                return;
            }

            uploadFloatMatrix();
        }
    }

    private void uploadInt()
    {
        switch (uniformType)
        {
            case 0:
                OpenGlHelper.glUniform1(uniformLocation, uniformIntBuffer);
                break;

            case 1:
                OpenGlHelper.glUniform2(uniformLocation, uniformIntBuffer);
                break;

            case 2:
                OpenGlHelper.glUniform3(uniformLocation, uniformIntBuffer);
                break;

            case 3:
                OpenGlHelper.glUniform4(uniformLocation, uniformIntBuffer);
                break;

            default:
                LOGGER.warn("Uniform.upload called, but count value ({}) is  not in the range of 1 to 4. Ignoring.", uniformCount);
        }
    }

    private void uploadFloat()
    {
        switch (uniformType)
        {
            case 4:
                OpenGlHelper.glUniform1(uniformLocation, uniformFloatBuffer);
                break;

            case 5:
                OpenGlHelper.glUniform2(uniformLocation, uniformFloatBuffer);
                break;

            case 6:
                OpenGlHelper.glUniform3(uniformLocation, uniformFloatBuffer);
                break;

            case 7:
                OpenGlHelper.glUniform4(uniformLocation, uniformFloatBuffer);
                break;

            default:
                LOGGER.warn("Uniform.upload called, but count value ({}) is not in the range of 1 to 4. Ignoring.", uniformCount);
        }
    }

    private void uploadFloatMatrix()
    {
        switch (uniformType)
        {
            case 8:
                OpenGlHelper.glUniformMatrix2(uniformLocation, true, uniformFloatBuffer);
                break;

            case 9:
                OpenGlHelper.glUniformMatrix3(uniformLocation, true, uniformFloatBuffer);
                break;

            case 10:
                OpenGlHelper.glUniformMatrix4(uniformLocation, true, uniformFloatBuffer);
        }
    }
}
