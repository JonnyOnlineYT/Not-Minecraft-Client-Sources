package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenNetherBridge;

public class ChunkGeneratorHell implements IChunkGenerator {
   protected static final IBlockState AIR;
   protected static final IBlockState NETHERRACK;
   protected static final IBlockState BEDROCK;
   protected static final IBlockState LAVA;
   protected static final IBlockState GRAVEL;
   protected static final IBlockState SOUL_SAND;
   private final World world;
   private final boolean generateStructures;
   private final Random rand;
   private double[] slowsandNoise = new double[256];
   private double[] gravelNoise = new double[256];
   private double[] depthBuffer = new double[256];
   private double[] buffer;
   private final NoiseGeneratorOctaves lperlinNoise1;
   private final NoiseGeneratorOctaves lperlinNoise2;
   private final NoiseGeneratorOctaves perlinNoise1;
   private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
   private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
   public final NoiseGeneratorOctaves scaleNoise;
   public final NoiseGeneratorOctaves depthNoise;
   private final WorldGenFire fireFeature = new WorldGenFire();
   private final WorldGenGlowStone1 lightGemGen = new WorldGenGlowStone1();
   private final WorldGenGlowStone2 hellPortalGen = new WorldGenGlowStone2();
   private final WorldGenerator quartzGen;
   private final WorldGenerator magmaGen;
   private final WorldGenHellLava lavaTrapGen;
   private final WorldGenHellLava hellSpringGen;
   private final WorldGenBush brownMushroomFeature;
   private final WorldGenBush redMushroomFeature;
   private final MapGenNetherBridge genNetherBridge;
   private final MapGenBase genNetherCaves;
   double[] pnr;
   double[] ar;
   double[] br;
   double[] noiseData4;
   double[] dr;

   public ChunkGeneratorHell(World worldIn, boolean p_i45637_2_, long seed) {
      this.quartzGen = new WorldGenMinable(Blocks.QUARTZ_ORE.getDefaultState(), 14, BlockMatcher.forBlock(Blocks.NETHERRACK));
      this.magmaGen = new WorldGenMinable(Blocks.MAGMA.getDefaultState(), 33, BlockMatcher.forBlock(Blocks.NETHERRACK));
      this.lavaTrapGen = new WorldGenHellLava(Blocks.FLOWING_LAVA, true);
      this.hellSpringGen = new WorldGenHellLava(Blocks.FLOWING_LAVA, false);
      this.brownMushroomFeature = new WorldGenBush(Blocks.BROWN_MUSHROOM);
      this.redMushroomFeature = new WorldGenBush(Blocks.RED_MUSHROOM);
      this.genNetherBridge = new MapGenNetherBridge();
      this.genNetherCaves = new MapGenCavesHell();
      this.world = worldIn;
      this.generateStructures = p_i45637_2_;
      this.rand = new Random(seed);
      this.lperlinNoise1 = new NoiseGeneratorOctaves(this.rand, 16);
      this.lperlinNoise2 = new NoiseGeneratorOctaves(this.rand, 16);
      this.perlinNoise1 = new NoiseGeneratorOctaves(this.rand, 8);
      this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.rand, 4);
      this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.rand, 4);
      this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
      this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
      worldIn.setSeaLevel(63);
   }

   public void prepareHeights(int p_185936_1_, int p_185936_2_, ChunkPrimer primer) {
      int i = true;
      int j = this.world.getSeaLevel() / 2 + 1;
      int k = true;
      int l = true;
      int i1 = true;
      this.buffer = this.getHeights(this.buffer, p_185936_1_ * 4, 0, p_185936_2_ * 4, 5, 17, 5);

      for(int j1 = 0; j1 < 4; ++j1) {
         for(int k1 = 0; k1 < 4; ++k1) {
            for(int l1 = 0; l1 < 16; ++l1) {
               double d0 = 0.125D;
               double d1 = this.buffer[((j1 + 0) * 5 + k1 + 0) * 17 + l1 + 0];
               double d2 = this.buffer[((j1 + 0) * 5 + k1 + 1) * 17 + l1 + 0];
               double d3 = this.buffer[((j1 + 1) * 5 + k1 + 0) * 17 + l1 + 0];
               double d4 = this.buffer[((j1 + 1) * 5 + k1 + 1) * 17 + l1 + 0];
               double d5 = (this.buffer[((j1 + 0) * 5 + k1 + 0) * 17 + l1 + 1] - d1) * 0.125D;
               double d6 = (this.buffer[((j1 + 0) * 5 + k1 + 1) * 17 + l1 + 1] - d2) * 0.125D;
               double d7 = (this.buffer[((j1 + 1) * 5 + k1 + 0) * 17 + l1 + 1] - d3) * 0.125D;
               double d8 = (this.buffer[((j1 + 1) * 5 + k1 + 1) * 17 + l1 + 1] - d4) * 0.125D;

               for(int i2 = 0; i2 < 8; ++i2) {
                  double d9 = 0.25D;
                  double d10 = d1;
                  double d11 = d2;
                  double d12 = (d3 - d1) * 0.25D;
                  double d13 = (d4 - d2) * 0.25D;

                  for(int j2 = 0; j2 < 4; ++j2) {
                     double d14 = 0.25D;
                     double d15 = d10;
                     double d16 = (d11 - d10) * 0.25D;

                     for(int k2 = 0; k2 < 4; ++k2) {
                        IBlockState iblockstate = null;
                        if (l1 * 8 + i2 < j) {
                           iblockstate = LAVA;
                        }

                        if (d15 > 0.0D) {
                           iblockstate = NETHERRACK;
                        }

                        int l2 = j2 + j1 * 4;
                        int i3 = i2 + l1 * 8;
                        int j3 = k2 + k1 * 4;
                        primer.setBlockState(l2, i3, j3, iblockstate);
                        d15 += d16;
                     }

                     d10 += d12;
                     d11 += d13;
                  }

                  d1 += d5;
                  d2 += d6;
                  d3 += d7;
                  d4 += d8;
               }
            }
         }
      }

   }

   public void buildSurfaces(int p_185937_1_, int p_185937_2_, ChunkPrimer primer) {
      int i = this.world.getSeaLevel() + 1;
      double d0 = 0.03125D;
      this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, p_185937_1_ * 16, p_185937_2_ * 16, 0, 16, 16, 1, 0.03125D, 0.03125D, 1.0D);
      this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, p_185937_1_ * 16, 109, p_185937_2_ * 16, 16, 1, 16, 0.03125D, 1.0D, 0.03125D);
      this.depthBuffer = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.depthBuffer, p_185937_1_ * 16, p_185937_2_ * 16, 0, 16, 16, 1, 0.0625D, 0.0625D, 0.0625D);

      for(int j = 0; j < 16; ++j) {
         for(int k = 0; k < 16; ++k) {
            boolean flag = this.slowsandNoise[j + k * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
            boolean flag1 = this.gravelNoise[j + k * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
            int l = (int)(this.depthBuffer[j + k * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
            int i1 = -1;
            IBlockState iblockstate = NETHERRACK;
            IBlockState iblockstate1 = NETHERRACK;

            for(int j1 = 127; j1 >= 0; --j1) {
               if (j1 < 127 - this.rand.nextInt(5) && j1 > this.rand.nextInt(5)) {
                  IBlockState iblockstate2 = primer.getBlockState(k, j1, j);
                  if (iblockstate2.getBlock() != null && iblockstate2.getMaterial() != Material.AIR) {
                     if (iblockstate2.getBlock() == Blocks.NETHERRACK) {
                        if (i1 == -1) {
                           if (l <= 0) {
                              iblockstate = AIR;
                              iblockstate1 = NETHERRACK;
                           } else if (j1 >= i - 4 && j1 <= i + 1) {
                              iblockstate = NETHERRACK;
                              iblockstate1 = NETHERRACK;
                              if (flag1) {
                                 iblockstate = GRAVEL;
                                 iblockstate1 = NETHERRACK;
                              }

                              if (flag) {
                                 iblockstate = SOUL_SAND;
                                 iblockstate1 = SOUL_SAND;
                              }
                           }

                           if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                              iblockstate = LAVA;
                           }

                           i1 = l;
                           if (j1 >= i - 1) {
                              primer.setBlockState(k, j1, j, iblockstate);
                           } else {
                              primer.setBlockState(k, j1, j, iblockstate1);
                           }
                        } else if (i1 > 0) {
                           --i1;
                           primer.setBlockState(k, j1, j, iblockstate1);
                        }
                     }
                  } else {
                     i1 = -1;
                  }
               } else {
                  primer.setBlockState(k, j1, j, BEDROCK);
               }
            }
         }
      }

   }

   public Chunk provideChunk(int x, int z) {
      this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
      ChunkPrimer chunkprimer = new ChunkPrimer();
      this.prepareHeights(x, z, chunkprimer);
      this.buildSurfaces(x, z, chunkprimer);
      this.genNetherCaves.generate(this.world, x, z, chunkprimer);
      if (this.generateStructures) {
         this.genNetherBridge.generate(this.world, x, z, chunkprimer);
      }

      Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
      Biome[] abiome = this.world.getBiomeProvider().getBiomes((Biome[])null, x * 16, z * 16, 16, 16);
      byte[] abyte = chunk.getBiomeArray();

      for(int i = 0; i < abyte.length; ++i) {
         abyte[i] = (byte)Biome.getIdForBiome(abiome[i]);
      }

      chunk.resetRelightChecks();
      return chunk;
   }

   private double[] getHeights(double[] p_185938_1_, int p_185938_2_, int p_185938_3_, int p_185938_4_, int p_185938_5_, int p_185938_6_, int p_185938_7_) {
      if (p_185938_1_ == null) {
         p_185938_1_ = new double[p_185938_5_ * p_185938_6_ * p_185938_7_];
      }

      double d0 = 684.412D;
      double d1 = 2053.236D;
      this.noiseData4 = this.scaleNoise.generateNoiseOctaves(this.noiseData4, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, 1, p_185938_7_, 1.0D, 0.0D, 1.0D);
      this.dr = this.depthNoise.generateNoiseOctaves(this.dr, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, 1, p_185938_7_, 100.0D, 0.0D, 100.0D);
      this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, p_185938_6_, p_185938_7_, 8.555150000000001D, 34.2206D, 8.555150000000001D);
      this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, p_185938_6_, p_185938_7_, 684.412D, 2053.236D, 684.412D);
      this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, p_185938_6_, p_185938_7_, 684.412D, 2053.236D, 684.412D);
      int i = 0;
      double[] adouble = new double[p_185938_6_];

      int j;
      for(j = 0; j < p_185938_6_; ++j) {
         adouble[j] = Math.cos((double)j * 3.141592653589793D * 6.0D / (double)p_185938_6_) * 2.0D;
         double d2 = (double)j;
         if (j > p_185938_6_ / 2) {
            d2 = (double)(p_185938_6_ - 1 - j);
         }

         if (d2 < 4.0D) {
            d2 = 4.0D - d2;
            adouble[j] -= d2 * d2 * d2 * 10.0D;
         }
      }

      for(j = 0; j < p_185938_5_; ++j) {
         for(int i1 = 0; i1 < p_185938_7_; ++i1) {
            double d3 = 0.0D;

            for(int k = 0; k < p_185938_6_; ++k) {
               double d4 = adouble[k];
               double d5 = this.ar[i] / 512.0D;
               double d6 = this.br[i] / 512.0D;
               double d7 = (this.pnr[i] / 10.0D + 1.0D) / 2.0D;
               double d8;
               if (d7 < 0.0D) {
                  d8 = d5;
               } else if (d7 > 1.0D) {
                  d8 = d6;
               } else {
                  d8 = d5 + (d6 - d5) * d7;
               }

               d8 -= d4;
               double d10;
               if (k > p_185938_6_ - 4) {
                  d10 = (double)((float)(k - (p_185938_6_ - 4)) / 3.0F);
                  d8 = d8 * (1.0D - d10) + -10.0D * d10;
               }

               if ((double)k < 0.0D) {
                  d10 = (0.0D - (double)k) / 4.0D;
                  d10 = MathHelper.clamp(d10, 0.0D, 1.0D);
                  d8 = d8 * (1.0D - d10) + -10.0D * d10;
               }

               p_185938_1_[i] = d8;
               ++i;
            }
         }
      }

      return p_185938_1_;
   }

   public void populate(int x, int z) {
      BlockFalling.fallInstantly = true;
      int i = x * 16;
      int j = z * 16;
      BlockPos blockpos = new BlockPos(i, 0, j);
      Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
      ChunkPos chunkpos = new ChunkPos(x, z);
      this.genNetherBridge.generateStructure(this.world, this.rand, chunkpos);

      int i2;
      for(i2 = 0; i2 < 8; ++i2) {
         this.hellSpringGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
      }

      for(i2 = 0; i2 < this.rand.nextInt(this.rand.nextInt(10) + 1) + 1; ++i2) {
         this.fireFeature.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
      }

      for(i2 = 0; i2 < this.rand.nextInt(this.rand.nextInt(10) + 1); ++i2) {
         this.lightGemGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
      }

      for(i2 = 0; i2 < 10; ++i2) {
         this.hellPortalGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
      }

      if (this.rand.nextBoolean()) {
         this.brownMushroomFeature.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
      }

      if (this.rand.nextBoolean()) {
         this.redMushroomFeature.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
      }

      for(i2 = 0; i2 < 16; ++i2) {
         this.quartzGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16), this.rand.nextInt(108) + 10, this.rand.nextInt(16)));
      }

      i2 = this.world.getSeaLevel() / 2 + 1;

      int j2;
      for(j2 = 0; j2 < 4; ++j2) {
         this.magmaGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16), i2 - 5 + this.rand.nextInt(10), this.rand.nextInt(16)));
      }

      for(j2 = 0; j2 < 16; ++j2) {
         this.lavaTrapGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16), this.rand.nextInt(108) + 10, this.rand.nextInt(16)));
      }

      biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
      BlockFalling.fallInstantly = false;
   }

   public boolean generateStructures(Chunk chunkIn, int x, int z) {
      return false;
   }

   public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
      if (creatureType == EnumCreatureType.MONSTER) {
         if (this.genNetherBridge.isInsideStructure(pos)) {
            return this.genNetherBridge.getSpawnList();
         }

         if (this.genNetherBridge.isPositionInStructure(this.world, pos) && this.world.getBlockState(pos.down()).getBlock() == Blocks.NETHER_BRICK) {
            return this.genNetherBridge.getSpawnList();
         }
      }

      Biome biome = this.world.getBiome(pos);
      return biome.getSpawnableList(creatureType);
   }

   @Nullable
   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_) {
      return "Fortress".equals(structureName) && this.genNetherBridge != null ? this.genNetherBridge.getClosestStrongholdPos(worldIn, position, p_180513_4_) : null;
   }

   public boolean func_193414_a(World p_193414_1_, String p_193414_2_, BlockPos p_193414_3_) {
      return "Fortress".equals(p_193414_2_) && this.genNetherBridge != null ? this.genNetherBridge.isInsideStructure(p_193414_3_) : false;
   }

   public void recreateStructures(Chunk chunkIn, int x, int z) {
      this.genNetherBridge.generate(this.world, x, z, (ChunkPrimer)null);
   }

   static {
      AIR = Blocks.AIR.getDefaultState();
      NETHERRACK = Blocks.NETHERRACK.getDefaultState();
      BEDROCK = Blocks.BEDROCK.getDefaultState();
      LAVA = Blocks.LAVA.getDefaultState();
      GRAVEL = Blocks.GRAVEL.getDefaultState();
      SOUL_SAND = Blocks.SOUL_SAND.getDefaultState();
   }
}
