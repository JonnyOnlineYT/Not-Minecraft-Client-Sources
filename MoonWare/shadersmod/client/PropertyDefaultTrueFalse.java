package shadersmod.client;

import optifine.Lang;

public class PropertyDefaultTrueFalse extends Property
{
    public static final String[] PROPERTY_VALUES = {"default", "true", "false"};
    public static final String[] USER_VALUES = {"Default", "ON", "OFF"};

    public PropertyDefaultTrueFalse(String propertyName, String userName, int defaultValue)
    {
        super(propertyName, PROPERTY_VALUES, userName, USER_VALUES, defaultValue);
    }

    public String getUserValue()
    {
        if (isDefault())
        {
            return Lang.getDefault();
        }
        else if (isTrue())
        {
            return Lang.getOn();
        }
        else
        {
            return isFalse() ? Lang.getOff() : super.getUserValue();
        }
    }

    public boolean isDefault()
    {
        return getValue() == 0;
    }

    public boolean isTrue()
    {
        return getValue() == 1;
    }

    public boolean isFalse()
    {
        return getValue() == 2;
    }
}
