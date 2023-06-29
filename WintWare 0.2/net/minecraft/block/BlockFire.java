package net.minecraft.block;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class BlockFire extends Block {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyBool UPPER = PropertyBool.create("up");
   private final Map<Block, Integer> encouragements = Maps.newIdentityHashMap();
   private final Map<Block, Integer> flammabilities = Maps.newIdentityHashMap();

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      return !worldIn.getBlockState(pos.down()).isFullyOpaque() && !Blocks.FIRE.canCatchFire(worldIn, pos.down()) ? state.withProperty(NORTH, this.canCatchFire(worldIn, pos.north())).withProperty(EAST, this.canCatchFire(worldIn, pos.east())).withProperty(SOUTH, this.canCatchFire(worldIn, pos.south())).withProperty(WEST, this.canCatchFire(worldIn, pos.west())).withProperty(UPPER, this.canCatchFire(worldIn, pos.up())) : this.getDefaultState();
   }

   protected BlockFire() {
      super(Material.FIRE);
      this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UPPER, false));
      this.setTickRandomly(true);
   }

   public static void init() {
      Blocks.FIRE.setFireInfo(Blocks.PLANKS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.DOUBLE_WOODEN_SLAB, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.WOODEN_SLAB, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.OAK_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.SPRUCE_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.BIRCH_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.JUNGLE_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.ACACIA_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.OAK_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.SPRUCE_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.BIRCH_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.JUNGLE_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.ACACIA_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.OAK_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.BIRCH_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.SPRUCE_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.JUNGLE_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.ACACIA_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.LOG, 5, 5);
      Blocks.FIRE.setFireInfo(Blocks.LOG2, 5, 5);
      Blocks.FIRE.setFireInfo(Blocks.LEAVES, 30, 60);
      Blocks.FIRE.setFireInfo(Blocks.LEAVES2, 30, 60);
      Blocks.FIRE.setFireInfo(Blocks.BOOKSHELF, 30, 20);
      Blocks.FIRE.setFireInfo(Blocks.TNT, 15, 100);
      Blocks.FIRE.setFireInfo(Blocks.TALLGRASS, 60, 100);
      Blocks.FIRE.setFireInfo(Blocks.DOUBLE_PLANT, 60, 100);
      Blocks.FIRE.setFireInfo(Blocks.YELLOW_FLOWER, 60, 100);
      Blocks.FIRE.setFireInfo(Blocks.RED_FLOWER, 60, 100);
      Blocks.FIRE.setFireInfo(Blocks.DEADBUSH, 60, 100);
      Blocks.FIRE.setFireInfo(Blocks.WOOL, 30, 60);
      Blocks.FIRE.setFireInfo(Blocks.VINE, 15, 100);
      Blocks.FIRE.setFireInfo(Blocks.COAL_BLOCK, 5, 5);
      Blocks.FIRE.setFireInfo(Blocks.HAY_BLOCK, 60, 20);
      Blocks.FIRE.setFireInfo(Blocks.CARPET, 60, 20);
   }

   public void setFireInfo(Block blockIn, int encouragement, int flammability) {
      this.encouragements.put(blockIn, encouragement);
      this.flammabilities.put(blockIn, flammability);
   }

   @Nullable
   public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return NULL_AABB;
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public int quantityDropped(Random random) {
      return 0;
   }

   public int tickRate(World worldIn) {
      return 30;
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (worldIn.getGameRules().getBoolean("doFireTick")) {
         if (!this.canPlaceBlockAt(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
         }

         Block block = worldIn.getBlockState(pos.down()).getBlock();
         boolean flag = block == Blocks.NETHERRACK || block == Blocks.MAGMA;
         if (worldIn.provider instanceof WorldProviderEnd && block == Blocks.BEDROCK) {
            flag = true;
         }

         int i = (Integer)state.getValue(AGE);
         if (!flag && worldIn.isRaining() && this.canDie(worldIn, pos) && rand.nextFloat() < 0.2F + (float)i * 0.03F) {
            worldIn.setBlockToAir(pos);
         } else {
            if (i < 15) {
               state = state.withProperty(AGE, i + rand.nextInt(3) / 2);
               worldIn.setBlockState(pos, state, 4);
            }

            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + rand.nextInt(10));
            if (!flag) {
               if (!this.canNeighborCatchFire(worldIn, pos)) {
                  if (!worldIn.getBlockState(pos.down()).isFullyOpaque() || i > 3) {
                     worldIn.setBlockToAir(pos);
                  }

                  return;
               }

               if (!this.canCatchFire(worldIn, pos.down()) && i == 15 && rand.nextInt(4) == 0) {
                  worldIn.setBlockToAir(pos);
                  return;
               }
            }

            boolean flag1 = worldIn.isBlockinHighHumidity(pos);
            int j = 0;
            if (flag1) {
               j = -50;
            }

            this.catchOnFire(worldIn, pos.east(), 300 + j, rand, i);
            this.catchOnFire(worldIn, pos.west(), 300 + j, rand, i);
            this.catchOnFire(worldIn, pos.down(), 250 + j, rand, i);
            this.catchOnFire(worldIn, pos.up(), 250 + j, rand, i);
            this.catchOnFire(worldIn, pos.north(), 300 + j, rand, i);
            this.catchOnFire(worldIn, pos.south(), 300 + j, rand, i);

            for(int k = -1; k <= 1; ++k) {
               for(int l = -1; l <= 1; ++l) {
                  for(int i1 = -1; i1 <= 4; ++i1) {
                     if (k != 0 || i1 != 0 || l != 0) {
                        int j1 = 100;
                        if (i1 > 1) {
                           j1 += (i1 - 1) * 100;
                        }

                        BlockPos blockpos = pos.add(k, i1, l);
                        int k1 = this.getNeighborEncouragement(worldIn, blockpos);
                        if (k1 > 0) {
                           int l1 = (k1 + 40 + worldIn.getDifficulty().getDifficultyId() * 7) / (i + 30);
                           if (flag1) {
                              l1 /= 2;
                           }

                           if (l1 > 0 && rand.nextInt(j1) <= l1 && (!worldIn.isRaining() || !this.canDie(worldIn, blockpos))) {
                              int i2 = i + rand.nextInt(5) / 4;
                              if (i2 > 15) {
                                 i2 = 15;
                              }

                              worldIn.setBlockState(blockpos, state.withProperty(AGE, i2), 3);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   protected boolean canDie(World worldIn, BlockPos pos) {
      return worldIn.isRainingAt(pos) || worldIn.isRainingAt(pos.west()) || worldIn.isRainingAt(pos.east()) || worldIn.isRainingAt(pos.north()) || worldIn.isRainingAt(pos.south());
   }

   public boolean requiresUpdates() {
      return false;
   }

   private int getFlammability(Block blockIn) {
      Integer integer = (Integer)this.flammabilities.get(blockIn);
      return integer == null ? 0 : integer;
   }

   private int getEncouragement(Block blockIn) {
      Integer integer = (Integer)this.encouragements.get(blockIn);
      return integer == null ? 0 : integer;
   }

   private void catchOnFire(World worldIn, BlockPos pos, int chance, Random random, int age) {
      int i = this.getFlammability(worldIn.getBlockState(pos).getBlock());
      if (random.nextInt(chance) < i) {
         IBlockState iblockstate = worldIn.getBlockState(pos);
         if (random.nextInt(age + 10) < 5 && !worldIn.isRainingAt(pos)) {
            int j = age + random.nextInt(5) / 4;
            if (j > 15) {
               j = 15;
            }

            worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, j), 3);
         } else {
            worldIn.setBlockToAir(pos);
         }

         if (iblockstate.getBlock() == Blocks.TNT) {
            Blocks.TNT.onBlockDestroyedByPlayer(worldIn, pos, iblockstate.withProperty(BlockTNT.EXPLODE, true));
         }
      }

   }

   private boolean canNeighborCatchFire(World worldIn, BlockPos pos) {
      EnumFacing[] var3 = EnumFacing.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing enumfacing = var3[var5];
         if (this.canCatchFire(worldIn, pos.offset(enumfacing))) {
            return true;
         }
      }

      return false;
   }

   private int getNeighborEncouragement(World worldIn, BlockPos pos) {
      if (!worldIn.isAirBlock(pos)) {
         return 0;
      } else {
         int i = 0;
         EnumFacing[] var4 = EnumFacing.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            EnumFacing enumfacing = var4[var6];
            i = Math.max(this.getEncouragement(worldIn.getBlockState(pos.offset(enumfacing)).getBlock()), i);
         }

         return i;
      }
   }

   public boolean isCollidable() {
      return false;
   }

   public boolean canCatchFire(IBlockAccess worldIn, BlockPos pos) {
      return this.getEncouragement(worldIn.getBlockState(pos).getBlock()) > 0;
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos.down()).isFullyOpaque() || this.canNeighborCatchFire(worldIn, pos);
   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      if (!worldIn.getBlockState(pos.down()).isFullyOpaque() && !this.canNeighborCatchFire(worldIn, pos)) {
         worldIn.setBlockToAir(pos);
      }

   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
      if (worldIn.provider.getDimensionType().getId() > 0 || !Blocks.PORTAL.trySpawnPortal(worldIn, pos)) {
         if (!worldIn.getBlockState(pos.down()).isFullyOpaque() && !this.canNeighborCatchFire(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
         } else {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + worldIn.rand.nextInt(10));
         }
      }

   }

   public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
      if (rand.nextInt(24) == 0) {
         worldIn.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
      }

      int j1;
      double d7;
      double d12;
      double d17;
      if (!worldIn.getBlockState(pos.down()).isFullyOpaque() && !Blocks.FIRE.canCatchFire(worldIn, pos.down())) {
         if (Blocks.FIRE.canCatchFire(worldIn, pos.west())) {
            for(j1 = 0; j1 < 2; ++j1) {
               d7 = (double)pos.getX() + rand.nextDouble() * 0.10000000149011612D;
               d12 = (double)pos.getY() + rand.nextDouble();
               d17 = (double)pos.getZ() + rand.nextDouble();
               worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
            }
         }

         if (Blocks.FIRE.canCatchFire(worldIn, pos.east())) {
            for(j1 = 0; j1 < 2; ++j1) {
               d7 = (double)(pos.getX() + 1) - rand.nextDouble() * 0.10000000149011612D;
               d12 = (double)pos.getY() + rand.nextDouble();
               d17 = (double)pos.getZ() + rand.nextDouble();
               worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
            }
         }

         if (Blocks.FIRE.canCatchFire(worldIn, pos.north())) {
            for(j1 = 0; j1 < 2; ++j1) {
               d7 = (double)pos.getX() + rand.nextDouble();
               d12 = (double)pos.getY() + rand.nextDouble();
               d17 = (double)pos.getZ() + rand.nextDouble() * 0.10000000149011612D;
               worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
            }
         }

         if (Blocks.FIRE.canCatchFire(worldIn, pos.south())) {
            for(j1 = 0; j1 < 2; ++j1) {
               d7 = (double)pos.getX() + rand.nextDouble();
               d12 = (double)pos.getY() + rand.nextDouble();
               d17 = (double)(pos.getZ() + 1) - rand.nextDouble() * 0.10000000149011612D;
               worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
            }
         }

         if (Blocks.FIRE.canCatchFire(worldIn, pos.up())) {
            for(j1 = 0; j1 < 2; ++j1) {
               d7 = (double)pos.getX() + rand.nextDouble();
               d12 = (double)(pos.getY() + 1) - rand.nextDouble() * 0.10000000149011612D;
               d17 = (double)pos.getZ() + rand.nextDouble();
               worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
            }
         }
      } else {
         for(j1 = 0; j1 < 3; ++j1) {
            d7 = (double)pos.getX() + rand.nextDouble();
            d12 = (double)pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
            d17 = (double)pos.getZ() + rand.nextDouble();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
         }
      }

   }

   public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
      return MapColor.TNT;
   }

   public BlockRenderLayer getBlockLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(AGE, meta);
   }

   public int getMetaFromState(IBlockState state) {
      return (Integer)state.getValue(AGE);
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{AGE, NORTH, EAST, SOUTH, WEST, UPPER});
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return BlockFaceShape.UNDEFINED;
   }
}
