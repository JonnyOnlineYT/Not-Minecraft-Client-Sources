package shadersmod.client;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderUniformInt extends ShaderUniformBase
{
    private int value = -1;

    public ShaderUniformInt(String name)
    {
        super(name);
    }

    protected void onProgramChanged()
    {
        value = -1;
    }

    public void setValue(int value)
    {
        if (getLocation() >= 0)
        {
            if (this.value != value)
            {
                ARBShaderObjects.glUniform1iARB(getLocation(), value);
                Shaders.checkGLError(getName());
                this.value = value;
            }
        }
    }

    public int getValue()
    {
        return value;
    }
}
