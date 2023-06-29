package shadersmod.client;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderUniformFloat4 extends ShaderUniformBase
{
    private float[] values = new float[4];

    public ShaderUniformFloat4(String name)
    {
        super(name);
    }

    protected void onProgramChanged()
    {
        values[0] = 0.0F;
        values[1] = 0.0F;
        values[2] = 0.0F;
        values[3] = 0.0F;
    }

    public void setValue(float f0, float f1, float f2, float f3)
    {
        if (getLocation() >= 0)
        {
            if (values[0] != f0 || values[1] != f1 || values[2] != f2 || values[3] != f3)
            {
                ARBShaderObjects.glUniform4fARB(getLocation(), f0, f1, f2, f3);
                Shaders.checkGLError(getName());
                values[0] = f0;
                values[1] = f1;
                values[2] = f2;
                values[3] = f3;
            }
        }
    }

    public float[] getValues()
    {
        return values;
    }
}
