package shadersmod.client;

import org.lwjgl.opengl.ARBShaderObjects;

public abstract class ShaderUniformBase
{
    private String name;
    private int program = -1;
    private int location = -1;

    public ShaderUniformBase(String name)
    {
        this.name = name;
    }

    public void setProgram(int program)
    {
        if (this.program != program)
        {
            this.program = program;
            location = ARBShaderObjects.glGetUniformLocationARB(program, name);
            onProgramChanged();
        }
    }

    protected abstract void onProgramChanged();

    public String getName()
    {
        return name;
    }

    public int getProgram()
    {
        return program;
    }

    public int getLocation()
    {
        return location;
    }

    public boolean isDefined()
    {
        return location >= 0;
    }
}
