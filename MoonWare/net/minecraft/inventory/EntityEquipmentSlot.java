package net.minecraft.inventory;

public enum EntityEquipmentSlot
{
    MAINHAND(EntityEquipmentSlot.Type.HAND, 0, 0, "mainhand"),
    OFFHAND(EntityEquipmentSlot.Type.HAND, 1, 5, "offhand"),
    FEET(EntityEquipmentSlot.Type.ARMOR, 0, 1, "feet"),
    LEGS(EntityEquipmentSlot.Type.ARMOR, 1, 2, "legs"),
    CHEST(EntityEquipmentSlot.Type.ARMOR, 2, 3, "chest"),
    HEAD(EntityEquipmentSlot.Type.ARMOR, 3, 4, "head");

    private final EntityEquipmentSlot.Type slotType;
    private final int index;
    private final int slotIndex;
    private final String name;

    EntityEquipmentSlot(EntityEquipmentSlot.Type slotTypeIn, int indexIn, int slotIndexIn, String nameIn)
    {
        slotType = slotTypeIn;
        index = indexIn;
        slotIndex = slotIndexIn;
        name = nameIn;
    }

    public EntityEquipmentSlot.Type getSlotType()
    {
        return slotType;
    }

    public int getIndex()
    {
        return index;
    }

    /**
     * Gets the actual slot index.
     */
    public int getSlotIndex()
    {
        return slotIndex;
    }

    public String getName()
    {
        return name;
    }

    public static EntityEquipmentSlot fromString(String targetName)
    {
        for (EntityEquipmentSlot entityequipmentslot : values())
        {
            if (entityequipmentslot.getName().equals(targetName))
            {
                return entityequipmentslot;
            }
        }

        throw new IllegalArgumentException("Invalid slot '" + targetName + "'");
    }

    public enum Type {
        HAND,
        ARMOR
    }
}
