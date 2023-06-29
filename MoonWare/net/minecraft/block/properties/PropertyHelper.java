package net.minecraft.block.properties;

import com.google.common.base.MoreObjects;

public abstract class PropertyHelper<T extends Comparable<T>> implements IProperty<T>
{
    private final Class<T> valueClass;
    private final String name;

    protected PropertyHelper(String name, Class<T> valueClass)
    {
        this.valueClass = valueClass;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Class<T> getValueClass()
    {
        return valueClass;
    }

    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("name", name).add("clazz", valueClass).add("values", getAllowedValues()).toString();
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof PropertyHelper))
        {
            return false;
        }
        else
        {
            PropertyHelper<?> propertyhelper = (PropertyHelper)p_equals_1_;
            return valueClass.equals(propertyhelper.valueClass) && name.equals(propertyhelper.name);
        }
    }

    public int hashCode()
    {
        return 31 * valueClass.hashCode() + name.hashCode();
    }
}
