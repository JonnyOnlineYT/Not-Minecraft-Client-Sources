package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeDecorator {
   protected boolean decorating;
   protected BlockPos chunkPos;
   protected ChunkGeneratorSettings chunkProviderSettings;
   protected WorldGenerator clayGen = new WorldGenClay(4);
   protected WorldGenerator sandGen;
   protected WorldGenerator gravelAsSandGen;
   protected WorldGenerator dirtGen;
   protected WorldGenerator gravelGen;
   protected WorldGenerator graniteGen;
   protected WorldGenerator dioriteGen;
   protected WorldGenerator andesiteGen;
   protected WorldGenerator coalGen;
   protected WorldGenerator ironGen;
   protected WorldGenerator goldGen;
   protected WorldGenerator redstoneGen;
   protected WorldGenerator diamondGen;
   protected WorldGenerator lapisGen;
   protected WorldGenFlowers yellowFlowerGen;
   protected WorldGenerator mushroomBrownGen;
   protected WorldGenerator mushroomRedGen;
   protected WorldGenerator bigMushroomGen;
   protected WorldGenerator reedGen;
   protected WorldGenerator cactusGen;
   protected WorldGenerator waterlilyGen;
   protected int waterlilyPerChunk;
   protected int treesPerChunk;
   protected float extraTreeChance;
   protected int flowersPerChunk;
   protected int grassPerChunk;
   protected int deadBushPerChunk;
   protected int mushroomsPerChunk;
   protected int reedsPerChunk;
   protected int cactiPerChunk;
   protected int sandPerChunk;
   protected int sandPerChunk2;
   protected int clayPerChunk;
   protected int bigMushroomsPerChunk;
   public boolean generateLakes;

   public BiomeDecorator() {
      this.sandGen = new WorldGenSand(Blocks.SAND, 7);
      this.gravelAsSandGen = new WorldGenSand(Blocks.GRAVEL, 6);
      this.yellowFlowerGen = new WorldGenFlowers(Blocks.YELLOW_FLOWER, BlockFlower.EnumFlowerType.DANDELION);
      this.mushroomBrownGen = new WorldGenBush(Blocks.BROWN_MUSHROOM);
      this.mushroomRedGen = new WorldGenBush(Blocks.RED_MUSHROOM);
      this.bigMushroomGen = new WorldGenBigMushroom();
      this.reedGen = new WorldGenReed();
      this.cactusGen = new WorldGenCactus();
      this.waterlilyGen = new WorldGenWaterlily();
      this.extraTreeChance = 0.1F;
      this.flowersPerChunk = 2;
      this.grassPerChunk = 1;
      this.sandPerChunk = 1;
      this.sandPerChunk2 = 3;
      this.clayPerChunk = 1;
      this.generateLakes = true;
   }

   public void decorate(World worldIn, Random random, Biome biome, BlockPos pos) {
      if (this.decorating) {
         throw new RuntimeException("Already decorating");
      } else {
         this.chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(worldIn.getWorldInfo().getGeneratorOptions()).build();
         this.chunkPos = pos;
         this.dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), this.chunkProviderSettings.dirtSize);
         this.gravelGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), this.chunkProviderSettings.gravelSize);
         this.graniteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
         this.dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
         this.andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
         this.coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), this.chunkProviderSettings.coalSize);
         this.ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), this.chunkProviderSettings.ironSize);
         this.goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), this.chunkProviderSettings.goldSize);
         this.redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), this.chunkProviderSettings.redstoneSize);
         this.diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), this.chunkProviderSettings.diamondSize);
         this.lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), this.chunkProviderSettings.lapisSize);
         this.genDecorations(biome, worldIn, random);
         this.decorating = false;
      }
   }

   protected void genDecorations(Biome biomeIn, World worldIn, Random random) {
      this.generateOres(worldIn, random);

      int k1;
      int l5;
      int j10;
      for(k1 = 0; k1 < this.sandPerChunk2; ++k1) {
         l5 = random.nextInt(16) + 8;
         j10 = random.nextInt(16) + 8;
         this.sandGen.generate(worldIn, random, worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(l5, 0, j10)));
      }

      for(k1 = 0; k1 < this.clayPerChunk; ++k1) {
         l5 = random.nextInt(16) + 8;
         j10 = random.nextInt(16) + 8;
         this.clayGen.generate(worldIn, random, worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(l5, 0, j10)));
      }

      for(k1 = 0; k1 < this.sandPerChunk; ++k1) {
         l5 = random.nextInt(16) + 8;
         j10 = random.nextInt(16) + 8;
         this.gravelAsSandGen.generate(worldIn, random, worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(l5, 0, j10)));
      }

      k1 = this.treesPerChunk;
      if (random.nextFloat() < this.extraTreeChance) {
         ++k1;
      }

      int i14;
      BlockPos blockpos3;
      for(l5 = 0; l5 < k1; ++l5) {
         j10 = random.nextInt(16) + 8;
         i14 = random.nextInt(16) + 8;
         WorldGenAbstractTree worldgenabstracttree = biomeIn.genBigTreeChance(random);
         worldgenabstracttree.setDecorationDefaults();
         blockpos3 = worldIn.getHeight(this.chunkPos.add(j10, 0, i14));
         if (worldgenabstracttree.generate(worldIn, random, blockpos3)) {
            worldgenabstracttree.generateSaplings(worldIn, random, blockpos3);
         }
      }

      for(l5 = 0; l5 < this.bigMushroomsPerChunk; ++l5) {
         j10 = random.nextInt(16) + 8;
         i14 = random.nextInt(16) + 8;
         this.bigMushroomGen.generate(worldIn, random, worldIn.getHeight(this.chunkPos.add(j10, 0, i14)));
      }

      BlockPos blockpos6;
      int j17;
      int k19;
      for(l5 = 0; l5 < this.flowersPerChunk; ++l5) {
         j10 = random.nextInt(16) + 8;
         i14 = random.nextInt(16) + 8;
         j17 = worldIn.getHeight(this.chunkPos.add(j10, 0, i14)).getY() + 32;
         if (j17 > 0) {
            k19 = random.nextInt(j17);
            blockpos6 = this.chunkPos.add(j10, k19, i14);
            BlockFlower.EnumFlowerType blockflower$enumflowertype = biomeIn.pickRandomFlower(random, blockpos6);
            BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();
            if (blockflower.getDefaultState().getMaterial() != Material.AIR) {
               this.yellowFlowerGen.setGeneratedBlock(blockflower, blockflower$enumflowertype);
               this.yellowFlowerGen.generate(worldIn, random, blockpos6);
            }
         }
      }

      for(l5 = 0; l5 < this.grassPerChunk; ++l5) {
         j10 = random.nextInt(16) + 8;
         i14 = random.nextInt(16) + 8;
         j17 = worldIn.getHeight(this.chunkPos.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = random.nextInt(j17);
            biomeIn.getRandomWorldGenForGrass(random).generate(worldIn, random, this.chunkPos.add(j10, k19, i14));
         }
      }

      for(l5 = 0; l5 < this.deadBushPerChunk; ++l5) {
         j10 = random.nextInt(16) + 8;
         i14 = random.nextInt(16) + 8;
         j17 = worldIn.getHeight(this.chunkPos.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = random.nextInt(j17);
            (new WorldGenDeadBush()).generate(worldIn, random, this.chunkPos.add(j10, k19, i14));
         }
      }

      for(l5 = 0; l5 < this.waterlilyPerChunk; ++l5) {
         j10 = random.nextInt(16) + 8;
         i14 = random.nextInt(16) + 8;
         j17 = worldIn.getHeight(this.chunkPos.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = random.nextInt(j17);

            BlockPos blockpos7;
            for(blockpos6 = this.chunkPos.add(j10, k19, i14); blockpos6.getY() > 0; blockpos6 = blockpos7) {
               blockpos7 = blockpos6.down();
               if (!worldIn.isAirBlock(blockpos7)) {
                  break;
               }
            }

            this.waterlilyGen.generate(worldIn, random, blockpos6);
         }
      }

      for(l5 = 0; l5 < this.mushroomsPerChunk; ++l5) {
         if (random.nextInt(4) == 0) {
            j10 = random.nextInt(16) + 8;
            i14 = random.nextInt(16) + 8;
            BlockPos blockpos2 = worldIn.getHeight(this.chunkPos.add(j10, 0, i14));
            this.mushroomBrownGen.generate(worldIn, random, blockpos2);
         }

         if (random.nextInt(8) == 0) {
            j10 = random.nextInt(16) + 8;
            i14 = random.nextInt(16) + 8;
            j17 = worldIn.getHeight(this.chunkPos.add(j10, 0, i14)).getY() * 2;
            if (j17 > 0) {
               k19 = random.nextInt(j17);
               blockpos6 = this.chunkPos.add(j10, k19, i14);
               this.mushroomRedGen.generate(worldIn, random, blockpos6);
            }
         }
      }

      if (random.nextInt(4) == 0) {
         l5 = random.nextInt(16) + 8;
         j10 = random.nextInt(16) + 8;
         i14 = worldIn.getHeight(this.chunkPos.add(l5, 0, j10)).getY() * 2;
         if (i14 > 0) {
            j17 = random.nextInt(i14);
            this.mushroomBrownGen.generate(worldIn, random, this.chunkPos.add(l5, j17, j10));
         }
      }

      if (random.nextInt(8) == 0) {
         l5 = random.nextInt(16) + 8;
         j10 = random.nextInt(16) + 8;
         i14 = worldIn.getHeight(this.chunkPos.add(l5, 0, j10)).getY() * 2;
         if (i14 > 0) {
            j17 = random.nextInt(i14);
            this.mushroomRedGen.generate(worldIn, random, this.chunkPos.add(l5, j17, j10));
         }
      }

      for(l5 = 0; l5 < this.reedsPerChunk; ++l5) {
         j10 = random.nextInt(16) + 8;
         i14 = random.nextInt(16) + 8;
         j17 = worldIn.getHeight(this.chunkPos.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = random.nextInt(j17);
            this.reedGen.generate(worldIn, random, this.chunkPos.add(j10, k19, i14));
         }
      }

      for(l5 = 0; l5 < 10; ++l5) {
         j10 = random.nextInt(16) + 8;
         i14 = random.nextInt(16) + 8;
         j17 = worldIn.getHeight(this.chunkPos.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = random.nextInt(j17);
            this.reedGen.generate(worldIn, random, this.chunkPos.add(j10, k19, i14));
         }
      }

      if (random.nextInt(32) == 0) {
         l5 = random.nextInt(16) + 8;
         j10 = random.nextInt(16) + 8;
         i14 = worldIn.getHeight(this.chunkPos.add(l5, 0, j10)).getY() * 2;
         if (i14 > 0) {
            j17 = random.nextInt(i14);
            (new WorldGenPumpkin()).generate(worldIn, random, this.chunkPos.add(l5, j17, j10));
         }
      }

      for(l5 = 0; l5 < this.cactiPerChunk; ++l5) {
         j10 = random.nextInt(16) + 8;
         i14 = random.nextInt(16) + 8;
         j17 = worldIn.getHeight(this.chunkPos.add(j10, 0, i14)).getY() * 2;
         if (j17 > 0) {
            k19 = random.nextInt(j17);
            this.cactusGen.generate(worldIn, random, this.chunkPos.add(j10, k19, i14));
         }
      }

      if (this.generateLakes) {
         for(l5 = 0; l5 < 50; ++l5) {
            j10 = random.nextInt(16) + 8;
            i14 = random.nextInt(16) + 8;
            j17 = random.nextInt(248) + 8;
            if (j17 > 0) {
               k19 = random.nextInt(j17);
               blockpos6 = this.chunkPos.add(j10, k19, i14);
               (new WorldGenLiquids(Blocks.FLOWING_WATER)).generate(worldIn, random, blockpos6);
            }
         }

         for(l5 = 0; l5 < 20; ++l5) {
            j10 = random.nextInt(16) + 8;
            i14 = random.nextInt(16) + 8;
            j17 = random.nextInt(random.nextInt(random.nextInt(240) + 8) + 8);
            blockpos3 = this.chunkPos.add(j10, j17, i14);
            (new WorldGenLiquids(Blocks.FLOWING_LAVA)).generate(worldIn, random, blockpos3);
         }
      }

   }

   protected void generateOres(World worldIn, Random random) {
      this.genStandardOre1(worldIn, random, this.chunkProviderSettings.dirtCount, this.dirtGen, this.chunkProviderSettings.dirtMinHeight, this.chunkProviderSettings.dirtMaxHeight);
      this.genStandardOre1(worldIn, random, this.chunkProviderSettings.gravelCount, this.gravelGen, this.chunkProviderSettings.gravelMinHeight, this.chunkProviderSettings.gravelMaxHeight);
      this.genStandardOre1(worldIn, random, this.chunkProviderSettings.dioriteCount, this.dioriteGen, this.chunkProviderSettings.dioriteMinHeight, this.chunkProviderSettings.dioriteMaxHeight);
      this.genStandardOre1(worldIn, random, this.chunkProviderSettings.graniteCount, this.graniteGen, this.chunkProviderSettings.graniteMinHeight, this.chunkProviderSettings.graniteMaxHeight);
      this.genStandardOre1(worldIn, random, this.chunkProviderSettings.andesiteCount, this.andesiteGen, this.chunkProviderSettings.andesiteMinHeight, this.chunkProviderSettings.andesiteMaxHeight);
      this.genStandardOre1(worldIn, random, this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, this.chunkProviderSettings.coalMaxHeight);
      this.genStandardOre1(worldIn, random, this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, this.chunkProviderSettings.ironMaxHeight);
      this.genStandardOre1(worldIn, random, this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, this.chunkProviderSettings.goldMaxHeight);
      this.genStandardOre1(worldIn, random, this.chunkProviderSettings.redstoneCount, this.redstoneGen, this.chunkProviderSettings.redstoneMinHeight, this.chunkProviderSettings.redstoneMaxHeight);
      this.genStandardOre1(worldIn, random, this.chunkProviderSettings.diamondCount, this.diamondGen, this.chunkProviderSettings.diamondMinHeight, this.chunkProviderSettings.diamondMaxHeight);
      this.genStandardOre2(worldIn, random, this.chunkProviderSettings.lapisCount, this.lapisGen, this.chunkProviderSettings.lapisCenterHeight, this.chunkProviderSettings.lapisSpread);
   }

   protected void genStandardOre1(World worldIn, Random random, int blockCount, WorldGenerator generator, int minHeight, int maxHeight) {
      int j;
      if (maxHeight < minHeight) {
         j = minHeight;
         minHeight = maxHeight;
         maxHeight = j;
      } else if (maxHeight == minHeight) {
         if (minHeight < 255) {
            ++maxHeight;
         } else {
            --minHeight;
         }
      }

      for(j = 0; j < blockCount; ++j) {
         BlockPos blockpos = this.chunkPos.add(random.nextInt(16), random.nextInt(maxHeight - minHeight) + minHeight, random.nextInt(16));
         generator.generate(worldIn, random, blockpos);
      }

   }

   protected void genStandardOre2(World worldIn, Random random, int blockCount, WorldGenerator generator, int centerHeight, int spread) {
      for(int i = 0; i < blockCount; ++i) {
         BlockPos blockpos = this.chunkPos.add(random.nextInt(16), random.nextInt(spread) + random.nextInt(spread) + centerHeight - spread, random.nextInt(16));
         generator.generate(worldIn, random, blockpos);
      }

   }
}
