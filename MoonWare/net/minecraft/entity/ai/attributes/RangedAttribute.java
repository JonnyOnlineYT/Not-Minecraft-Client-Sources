package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;
import net.minecraft.util.math.MathHelper;

public class RangedAttribute extends BaseAttribute
{
    private final double minimumValue;
    private final double maximumValue;
    private String description;

    public RangedAttribute(@Nullable IAttribute parentIn, String unlocalizedNameIn, double defaultValue, double minimumValueIn, double maximumValueIn)
    {
        super(parentIn, unlocalizedNameIn, defaultValue);
        minimumValue = minimumValueIn;
        maximumValue = maximumValueIn;

        if (minimumValueIn > maximumValueIn)
        {
            throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
        }
        else if (defaultValue < minimumValueIn)
        {
            throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
        }
        else if (defaultValue > maximumValueIn)
        {
            throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
        }
    }

    public RangedAttribute setDescription(String descriptionIn)
    {
        description = descriptionIn;
        return this;
    }

    public String getDescription()
    {
        return description;
    }

    public double clampValue(double value)
    {
        value = MathHelper.clamp(value, minimumValue, maximumValue);
        return value;
    }
}
