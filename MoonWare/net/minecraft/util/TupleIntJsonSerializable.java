package net.minecraft.util;

public class TupleIntJsonSerializable
{
    private int integerValue;
    private IJsonSerializable jsonSerializableValue;

    /**
     * Gets the integer value stored in this tuple.
     */
    public int getIntegerValue()
    {
        return integerValue;
    }

    /**
     * Sets this tuple's integer value to the given value.
     */
    public void setIntegerValue(int integerValueIn)
    {
        integerValue = integerValueIn;
    }

    public <T extends IJsonSerializable> T getJsonSerializableValue()
    {
        return (T) jsonSerializableValue;
    }

    /**
     * Sets this tuple's JsonSerializable value to the given value.
     */
    public void setJsonSerializableValue(IJsonSerializable jsonSerializableValueIn)
    {
        jsonSerializableValue = jsonSerializableValueIn;
    }
}
