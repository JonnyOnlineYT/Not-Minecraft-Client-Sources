package net.minecraft.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface ITileEntityProvider
{
    @Nullable

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    TileEntity createNewTileEntity(World worldIn, int meta);
}
