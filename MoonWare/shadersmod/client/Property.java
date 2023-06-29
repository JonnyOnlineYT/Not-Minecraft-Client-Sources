package shadersmod.client;

import java.util.Arrays;
import java.util.Properties;

import optifine.Config;

public class Property
{
    private int[] values;
    private int defaultValue;
    private String propertyName;
    private String[] propertyValues;
    private String userName;
    private String[] userValues;
    private int value;

    public Property(String propertyName, String[] propertyValues, String userName, String[] userValues, int defaultValue)
    {
        this.propertyName = propertyName;
        this.propertyValues = propertyValues;
        this.userName = userName;
        this.userValues = userValues;
        this.defaultValue = defaultValue;

        if (propertyValues.length != userValues.length)
        {
            throw new IllegalArgumentException("Property and user values have different lengths: " + propertyValues.length + " != " + userValues.length);
        }
        else if (defaultValue >= 0 && defaultValue < propertyValues.length)
        {
            value = defaultValue;
        }
        else
        {
            throw new IllegalArgumentException("Invalid default value: " + defaultValue);
        }
    }

    public boolean setPropertyValue(String propVal)
    {
        if (propVal == null)
        {
            value = defaultValue;
            return false;
        }
        else
        {
            value = Arrays.asList(propertyValues).indexOf(propVal);

            if (value >= 0 && value < propertyValues.length)
            {
                return true;
            }
            else
            {
                value = defaultValue;
                return false;
            }
        }
    }

    public void nextValue()
    {
        ++value;

        if (value < 0 || value >= propertyValues.length)
        {
            value = 0;
        }
    }

    public void setValue(int val)
    {
        value = val;

        if (value < 0 || value >= propertyValues.length)
        {
            value = defaultValue;
        }
    }

    public int getValue()
    {
        return value;
    }

    public String getUserValue()
    {
        return userValues[value];
    }

    public String getPropertyValue()
    {
        return propertyValues[value];
    }

    public String getUserName()
    {
        return userName;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public void resetValue()
    {
        value = defaultValue;
    }

    public boolean loadFrom(Properties props)
    {
        resetValue();

        if (props == null)
        {
            return false;
        }
        else
        {
            String s = props.getProperty(propertyName);
            return s != null && setPropertyValue(s);
        }
    }

    public void saveTo(Properties props)
    {
        if (props != null)
        {
            props.setProperty(getPropertyName(), getPropertyValue());
        }
    }

    public String toString()
    {
        return "" + propertyName + "=" + getPropertyValue() + " [" + Config.arrayToString(propertyValues) + "], value: " + value;
    }
}
