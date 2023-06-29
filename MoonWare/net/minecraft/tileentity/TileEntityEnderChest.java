package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityEnderChest extends TileEntity implements ITickable
{
    public float lidAngle;

    /** The angle of the ender chest lid last tick */
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        if (++ticksSinceSync % 20 * 4 == 0)
        {
            world.addBlockEvent(pos, Blocks.ENDER_CHEST, 1, numPlayersUsing);
        }

        prevLidAngle = lidAngle;
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        float f = 0.1F;

        if (numPlayersUsing > 0 && lidAngle == 0.0F)
        {
            double d0 = (double)i + 0.5D;
            double d1 = (double)k + 0.5D;
            world.playSound(null, d0, (double)j + 0.5D, d1, SoundEvents.BLOCK_ENDERCHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F)
        {
            float f2 = lidAngle;

            if (numPlayersUsing > 0)
            {
                lidAngle += 0.1F;
            }
            else
            {
                lidAngle -= 0.1F;
            }

            if (lidAngle > 1.0F)
            {
                lidAngle = 1.0F;
            }

            float f1 = 0.5F;

            if (lidAngle < 0.5F && f2 >= 0.5F)
            {
                double d3 = (double)i + 0.5D;
                double d2 = (double)k + 0.5D;
                world.playSound(null, d3, (double)j + 0.5D, d2, SoundEvents.BLOCK_ENDERCHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (lidAngle < 0.0F)
            {
                lidAngle = 0.0F;
            }
        }
    }

    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate()
    {
        updateContainingBlockInfo();
        super.invalidate();
    }

    public void openChest()
    {
        ++numPlayersUsing;
        world.addBlockEvent(pos, Blocks.ENDER_CHEST, 1, numPlayersUsing);
    }

    public void closeChest()
    {
        --numPlayersUsing;
        world.addBlockEvent(pos, Blocks.ENDER_CHEST, 1, numPlayersUsing);
    }

    public boolean canBeUsed(EntityPlayer player)
    {
        if (world.getTileEntity(pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
        }
    }
}
