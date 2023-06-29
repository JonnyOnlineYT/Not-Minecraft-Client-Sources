package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;

public abstract class BaseAttribute implements IAttribute
{
    private final IAttribute parent;
    private final String unlocalizedName;
    private final double defaultValue;
    private boolean shouldWatch;

    protected BaseAttribute(@Nullable IAttribute parentIn, String unlocalizedNameIn, double defaultValueIn)
    {
        parent = parentIn;
        unlocalizedName = unlocalizedNameIn;
        defaultValue = defaultValueIn;

        if (unlocalizedNameIn == null)
        {
            throw new IllegalArgumentException("Name cannot be null!");
        }
    }

    public String getAttributeUnlocalizedName()
    {
        return unlocalizedName;
    }

    public double getDefaultValue()
    {
        return defaultValue;
    }

    public boolean getShouldWatch()
    {
        return shouldWatch;
    }

    public BaseAttribute setShouldWatch(boolean shouldWatchIn)
    {
        shouldWatch = shouldWatchIn;
        return this;
    }

    @Nullable
    public IAttribute getParent()
    {
        return parent;
    }

    public int hashCode()
    {
        return unlocalizedName.hashCode();
    }

    public boolean equals(Object p_equals_1_)
    {
        return p_equals_1_ instanceof IAttribute && unlocalizedName.equals(((IAttribute)p_equals_1_).getAttributeUnlocalizedName());
    }
}
