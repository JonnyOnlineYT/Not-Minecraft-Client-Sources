package ru.wendoxd.utils.misc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class BlockUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isBlockPosAir(BlockPos blockPos) {
        return getBlock(blockPos) != Blocks.AIR && !(getBlock(blockPos) instanceof BlockLiquid);
    }

    public static Block getBlock(BlockPos blockPos) {
        return mc.world.getBlockState(blockPos).getBlock();
    }
}
