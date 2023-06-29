package shadersmod.client;

import optifine.Config;

public class PropertyDefaultFastFancyOff extends Property
{
    public static final String[] PROPERTY_VALUES = {"default", "fast", "fancy", "off"};
    public static final String[] USER_VALUES = {"Default", "Fast", "Fancy", "OFF"};

    public PropertyDefaultFastFancyOff(String propertyName, String userName, int defaultValue)
    {
        super(propertyName, PROPERTY_VALUES, userName, USER_VALUES, defaultValue);
    }

    public boolean isDefault()
    {
        return getValue() == 0;
    }

    public boolean isFast()
    {
        return getValue() == 1;
    }

    public boolean isFancy()
    {
        return getValue() == 2;
    }

    public boolean isOff()
    {
        return getValue() == 3;
    }

    public boolean setPropertyValue(String propVal)
    {
        if (Config.equals(propVal, "none"))
        {
            propVal = "off";
        }

        return super.setPropertyValue(propVal);
    }
}
