package shadersmod.client;

public class ShaderLine
{
    private int type;
    private String name;
    private String value;
    private String line;
    public static final int TYPE_UNIFORM = 1;
    public static final int TYPE_COMMENT = 2;
    public static final int TYPE_CONST_INT = 3;
    public static final int TYPE_CONST_FLOAT = 4;
    public static final int TYPE_CONST_BOOL = 5;

    public ShaderLine(int type, String name, String value, String line)
    {
        this.type = type;
        this.name = name;
        this.value = value;
        this.line = line;
    }

    public int getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }

    public boolean isUniform()
    {
        return type == 1;
    }

    public boolean isUniform(String name)
    {
        return isUniform() && name.equals(this.name);
    }

    public boolean isComment()
    {
        return type == 2;
    }

    public boolean isConstInt()
    {
        return type == 3;
    }

    public boolean isConstFloat()
    {
        return type == 4;
    }

    public boolean isConstBool()
    {
        return type == 5;
    }

    public boolean isComment(String name)
    {
        return isComment() && name.equals(this.name);
    }

    public boolean isComment(String name, String value)
    {
        return isComment(name) && value.equals(this.value);
    }

    public boolean isConstInt(String name)
    {
        return isConstInt() && name.equals(this.name);
    }

    public boolean isConstIntSuffix(String suffix)
    {
        return isConstInt() && name.endsWith(suffix);
    }

    public boolean isConstFloat(String name)
    {
        return isConstFloat() && name.equals(this.name);
    }

    public boolean isConstBool(String name)
    {
        return isConstBool() && name.equals(this.name);
    }

    public boolean isConstBoolSuffix(String suffix)
    {
        return isConstBool() && name.endsWith(suffix);
    }

    public boolean isConstBoolSuffix(String suffix, boolean val)
    {
        return isConstBoolSuffix(suffix) && getValueBool() == val;
    }

    public boolean isConstBool(String name1, String name2)
    {
        return isConstBool(name1) || isConstBool(name2);
    }

    public boolean isConstBool(String name1, String name2, String name3)
    {
        return isConstBool(name1) || isConstBool(name2) || isConstBool(name3);
    }

    public boolean isConstBool(String name, boolean val)
    {
        return isConstBool(name) && getValueBool() == val;
    }

    public boolean isConstBool(String name1, String name2, boolean val)
    {
        return isConstBool(name1, name2) && getValueBool() == val;
    }

    public boolean isConstBool(String name1, String name2, String name3, boolean val)
    {
        return isConstBool(name1, name2, name3) && getValueBool() == val;
    }

    public int getValueInt()
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException var2)
        {
            throw new NumberFormatException("Invalid integer: " + value + ", line: " + line);
        }
    }

    public float getValueFloat()
    {
        try
        {
            return Float.parseFloat(value);
        }
        catch (NumberFormatException var2)
        {
            throw new NumberFormatException("Invalid float: " + value + ", line: " + line);
        }
    }

    public boolean getValueBool()
    {
        String s = value.toLowerCase();

        if (!s.equals("true") && !s.equals("false"))
        {
            throw new RuntimeException("Invalid boolean: " + value + ", line: " + line);
        }
        else
        {
            return Boolean.valueOf(value).booleanValue();
        }
    }
}
