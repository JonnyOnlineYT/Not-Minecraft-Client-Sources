package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public abstract class TileEntityLockable extends TileEntity implements ILockableContainer
{
    private LockCode code = LockCode.EMPTY_CODE;

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        code = LockCode.fromNBT(compound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (code != null)
        {
            code.toNBT(compound);
        }

        return compound;
    }

    public boolean isLocked()
    {
        return code != null && !code.isEmpty();
    }

    public LockCode getLockCode()
    {
        return code;
    }

    public void setLockCode(LockCode code)
    {
        this.code = code;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return hasCustomName() ? new TextComponent(getName()) : new TranslatableComponent(getName());
    }
}
