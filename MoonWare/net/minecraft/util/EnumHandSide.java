package net.minecraft.util;

import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;

public enum EnumHandSide
{
    LEFT(new TranslatableComponent("options.mainHand.left")),
    RIGHT(new TranslatableComponent("options.mainHand.right"));

    private final Component handName;

    EnumHandSide(Component nameIn)
    {
        handName = nameIn;
    }

    public EnumHandSide opposite()
    {
        return this == LEFT ? RIGHT : LEFT;
    }

    public String toString()
    {
        return handName.asString();
    }
}
