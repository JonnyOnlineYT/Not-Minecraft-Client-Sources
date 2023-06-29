package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeSavannaMutated extends BiomeSavanna
{
    public BiomeSavannaMutated(Biome.BiomeProperties properties)
    {
        super(properties);
        theBiomeDecorator.treesPerChunk = 2;
        theBiomeDecorator.flowersPerChunk = 2;
        theBiomeDecorator.grassPerChunk = 5;
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        topBlock = Blocks.GRASS.getDefaultState();
        fillerBlock = Blocks.DIRT.getDefaultState();

        if (noiseVal > 1.75D)
        {
            topBlock = Blocks.STONE.getDefaultState();
            fillerBlock = Blocks.STONE.getDefaultState();
        }
        else if (noiseVal > -0.5D)
        {
            topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
        }

        generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        theBiomeDecorator.decorate(worldIn, rand, this, pos);
    }
}
