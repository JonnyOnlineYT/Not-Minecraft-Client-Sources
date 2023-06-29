package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeTaiga extends Biome
{
    private static final WorldGenTaiga1 PINE_GENERATOR = new WorldGenTaiga1();
    private static final WorldGenTaiga2 SPRUCE_GENERATOR = new WorldGenTaiga2(false);
    private static final WorldGenMegaPineTree MEGA_PINE_GENERATOR = new WorldGenMegaPineTree(false, false);
    private static final WorldGenMegaPineTree MEGA_SPRUCE_GENERATOR = new WorldGenMegaPineTree(false, true);
    private static final WorldGenBlockBlob FOREST_ROCK_GENERATOR = new WorldGenBlockBlob(Blocks.MOSSY_COBBLESTONE, 0);
    private final BiomeTaiga.Type type;

    public BiomeTaiga(BiomeTaiga.Type typeIn, Biome.BiomeProperties properties)
    {
        super(properties);
        type = typeIn;
        spawnableCreatureList.add(new Biome.SpawnListEntry(EntityWolf.class, 8, 4, 4));
        spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        theBiomeDecorator.treesPerChunk = 10;

        if (typeIn != BiomeTaiga.Type.MEGA && typeIn != BiomeTaiga.Type.MEGA_SPRUCE)
        {
            theBiomeDecorator.grassPerChunk = 1;
            theBiomeDecorator.mushroomsPerChunk = 1;
        }
        else
        {
            theBiomeDecorator.grassPerChunk = 7;
            theBiomeDecorator.deadBushPerChunk = 1;
            theBiomeDecorator.mushroomsPerChunk = 3;
        }
    }

    public WorldGenAbstractTree genBigTreeChance(Random rand)
    {
        if ((type == BiomeTaiga.Type.MEGA || type == BiomeTaiga.Type.MEGA_SPRUCE) && rand.nextInt(3) == 0)
        {
            return type != BiomeTaiga.Type.MEGA_SPRUCE && rand.nextInt(13) != 0 ? MEGA_PINE_GENERATOR : MEGA_SPRUCE_GENERATOR;
        }
        else
        {
            return rand.nextInt(3) == 0 ? PINE_GENERATOR : SPRUCE_GENERATOR;
        }
    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random rand)
    {
        return rand.nextInt(5) > 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        if (type == BiomeTaiga.Type.MEGA || type == BiomeTaiga.Type.MEGA_SPRUCE)
        {
            int i = rand.nextInt(3);

            for (int j = 0; j < i; ++j)
            {
                int k = rand.nextInt(16) + 8;
                int l = rand.nextInt(16) + 8;
                BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
                FOREST_ROCK_GENERATOR.generate(worldIn, rand, blockpos);
            }
        }

        Biome.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);

        for (int i1 = 0; i1 < 7; ++i1)
        {
            int j1 = rand.nextInt(16) + 8;
            int k1 = rand.nextInt(16) + 8;
            int l1 = rand.nextInt(worldIn.getHeight(pos.add(j1, 0, k1)).getY() + 32);
            Biome.DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j1, l1, k1));
        }

        super.decorate(worldIn, rand, pos);
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        if (type == BiomeTaiga.Type.MEGA || type == BiomeTaiga.Type.MEGA_SPRUCE)
        {
            topBlock = Blocks.GRASS.getDefaultState();
            fillerBlock = Blocks.DIRT.getDefaultState();

            if (noiseVal > 1.75D)
            {
                topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
            }
            else if (noiseVal > -0.95D)
            {
                topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
            }
        }

        generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    public enum Type
    {
        NORMAL,
        MEGA,
        MEGA_SPRUCE
    }
}
