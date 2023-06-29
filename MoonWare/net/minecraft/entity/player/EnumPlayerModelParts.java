package net.minecraft.entity.player;

import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;

public enum EnumPlayerModelParts
{
    CAPE(0, "cape"),
    JACKET(1, "jacket"),
    LEFT_SLEEVE(2, "left_sleeve"),
    RIGHT_SLEEVE(3, "right_sleeve"),
    LEFT_PANTS_LEG(4, "left_pants_leg"),
    RIGHT_PANTS_LEG(5, "right_pants_leg"),
    HAT(6, "hat");

    private final int partId;
    private final int partMask;
    private final String partName;
    private final Component name;

    EnumPlayerModelParts(int partIdIn, String partNameIn)
    {
        partId = partIdIn;
        partMask = 1 << partIdIn;
        partName = partNameIn;
        name = new TranslatableComponent("options.modelPart." + partNameIn);
    }

    public int getPartMask()
    {
        return partMask;
    }

    public int getPartId()
    {
        return partId;
    }

    public String getPartName()
    {
        return partName;
    }

    public Component getName()
    {
        return name;
    }
}
