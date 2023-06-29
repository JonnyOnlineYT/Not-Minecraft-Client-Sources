package shadersmod.client;

import java.util.Arrays;
import java.util.List;

import optifine.Config;
import optifine.StrUtils;

public abstract class ShaderOption
{
    private String name;
    private String description;
    private String value;
    private String[] values;
    private String valueDefault;
    private String[] paths;
    private boolean enabled = true;
    private boolean visible = true;
    public static final String COLOR_GREEN = "\u00a7a";
    public static final String COLOR_RED = "\u00a7c";
    public static final String COLOR_BLUE = "\u00a79";

    public ShaderOption(String name, String description, String value, String[] values, String valueDefault, String path)
    {
        this.name = name;
        this.description = description;
        this.value = value;
        this.values = values;
        this.valueDefault = valueDefault;

        if (path != null)
        {
            paths = new String[] {path};
        }
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getDescriptionText()
    {
        String s = Config.normalize(description);
        s = StrUtils.removePrefix(s, "//");
        s = Shaders.translate("option." + getName() + ".comment", s);
        return s;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getValue()
    {
        return value;
    }

    public boolean setValue(String value)
    {
        int i = getIndex(value, values);

        if (i < 0)
        {
            return false;
        }
        else
        {
            this.value = value;
            return true;
        }
    }

    public String getValueDefault()
    {
        return valueDefault;
    }

    public void resetValue()
    {
        value = valueDefault;
    }

    public void nextValue()
    {
        int i = getIndex(value, values);

        if (i >= 0)
        {
            i = (i + 1) % values.length;
            value = values[i];
        }
    }

    public void prevValue()
    {
        int i = getIndex(value, values);

        if (i >= 0)
        {
            i = (i - 1 + values.length) % values.length;
            value = values[i];
        }
    }

    private static int getIndex(String str, String[] strs)
    {
        for (int i = 0; i < strs.length; ++i)
        {
            String s = strs[i];

            if (s.equals(str))
            {
                return i;
            }
        }

        return -1;
    }

    public String[] getPaths()
    {
        return paths;
    }

    public void addPaths(String[] newPaths)
    {
        List<String> list = Arrays.asList(paths);

        for (int i = 0; i < newPaths.length; ++i)
        {
            String s = newPaths[i];

            if (!list.contains(s))
            {
                paths = (String[])Config.addObjectToArray(paths, s);
            }
        }
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isChanged()
    {
        return !Config.equals(value, valueDefault);
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public boolean isValidValue(String val)
    {
        return getIndex(val, values) >= 0;
    }

    public String getNameText()
    {
        return Shaders.translate("option." + name, name);
    }

    public String getValueText(String val)
    {
        return Shaders.translate("value." + name + "." + val, val);
    }

    public String getValueColor(String val)
    {
        return "";
    }

    public boolean matchesLine(String line)
    {
        return false;
    }

    public boolean checkUsed()
    {
        return false;
    }

    public boolean isUsedInLine(String line)
    {
        return false;
    }

    public String getSourceLine()
    {
        return null;
    }

    public String[] getValues()
    {
        return values.clone();
    }

    public float getIndexNormalized()
    {
        if (values.length <= 1)
        {
            return 0.0F;
        }
        else
        {
            int i = getIndex(value, values);

            if (i < 0)
            {
                return 0.0F;
            }
            else
            {
                float f = 1.0F * (float)i / ((float) values.length - 1.0F);
                return f;
            }
        }
    }

    public void setIndexNormalized(float f)
    {
        if (values.length > 1)
        {
            f = Config.limit(f, 0.0F, 1.0F);
            int i = Math.round(f * (float)(values.length - 1));
            value = values[i];
        }
    }

    public String toString()
    {
        return "" + name + ", value: " + value + ", valueDefault: " + valueDefault + ", paths: " + Config.arrayToString(paths);
    }
}
