package net.minecraft.client.player.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.Component;
import net.minecraft.world.IInteractionObject;

public class LocalBlockIntercommunication implements IInteractionObject
{
    private final String guiID;
    private final Component displayName;

    public LocalBlockIntercommunication(String guiIdIn, Component displayNameIn)
    {
        guiID = guiIdIn;
        displayName = displayNameIn;
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return displayName.asString();
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return true;
    }

    public String getGuiID()
    {
        return guiID;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return displayName;
    }
}
