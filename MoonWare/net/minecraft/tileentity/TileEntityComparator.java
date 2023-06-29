package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityComparator extends TileEntity
{
    private int outputSignal;

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("OutputSignal", outputSignal);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        outputSignal = compound.getInteger("OutputSignal");
    }

    public int getOutputSignal()
    {
        return outputSignal;
    }

    public void setOutputSignal(int outputSignalIn)
    {
        outputSignal = outputSignalIn;
    }
}
