package org.moonware.client.helpers.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.moonware.client.helpers.Helper;

import java.util.ArrayList;
import java.util.List;

public class BlockHelper implements Helper {

    public static Block getBlock(BlockPos pos) {
        return Minecraft.world.getBlockState(pos).getBlock();
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(Minecraft.player.posX), Math.floor(Minecraft.player.posY), Math.floor(Minecraft.player.posZ));
    }

    public static boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = Minecraft.world.getBlockState(pos);
        if (state.getBlock() instanceof BlockDirt || state.getBlock() instanceof BlockGrass && !((state).getBlock() instanceof BlockFarmland))
            return Minecraft.world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
        return false;
    }

    public static List<BlockPos> getSphere(BlockPos blockPos, float offset, int range, boolean hollow, boolean sphere) {
        ArrayList<BlockPos> blockPosList = new ArrayList<>();
        int blockPosX = blockPos.getX();
        int blockPosY = blockPos.getY();
        int blockPosZ = blockPos.getZ();
        float x = blockPosX - offset;
        while (x <= blockPosX + offset) {
            float z = blockPosZ - offset;
            while (z <= blockPosZ + offset) {
                float y = sphere ? blockPosY - offset : blockPosY;
                do {
                    float f = sphere ? blockPosY + offset : blockPosY + range;
                    if (!(y < f))
                        break;

                    float dist = (blockPosX - x) * (blockPosX - x) + (blockPosZ - z) * (blockPosZ - z) + (sphere ? (blockPosY - y) * (blockPosY - y) : 0);
                    if (!(!(dist < offset * offset) || hollow && dist < (offset - 1 * (offset - 1)))) {
                        BlockPos pos = new BlockPos(x, y, z);
                        blockPosList.add(pos);
                    }
                    ++y;
                } while (true);
                ++z;
            }
            ++x;
        }
        return blockPosList;
    }

    public static ArrayList<BlockPos> getBlocks(int x, int y, int z) {
        BlockPos min = new BlockPos(Minecraft.player.posX - x, Minecraft.player.posY - y, Minecraft.player.posZ - z);
        BlockPos max = new BlockPos(Minecraft.player.posX + x, Minecraft.player.posY + y, Minecraft.player.posZ + z);
        return getAllInBox(min, max);
    }

    public static ArrayList<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
        ArrayList<BlockPos> blocks = new ArrayList<>();

        BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    blocks.add(new BlockPos(x, y, z));
                }
            }
        }
        return blocks;
    }
}
