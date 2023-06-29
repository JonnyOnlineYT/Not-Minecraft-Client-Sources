package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLeaves extends Block {
   public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
   public static final PropertyBool CHECK_DECAY = PropertyBool.create("check_decay");
   protected boolean leavesFancy;
   int[] surroundings;

   public BlockLeaves() {
      super(Material.LEAVES);
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
      this.setHardness(0.2F);
      this.setLightOpacity(1);
      this.setSoundType(SoundType.PLANT);
   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      int i = true;
      int j = true;
      int k = pos.getX();
      int l = pos.getY();
      int i1 = pos.getZ();
      if (worldIn.isAreaLoaded(new BlockPos(k - 2, l - 2, i1 - 2), new BlockPos(k + 2, l + 2, i1 + 2))) {
         for(int j1 = -1; j1 <= 1; ++j1) {
            for(int k1 = -1; k1 <= 1; ++k1) {
               for(int l1 = -1; l1 <= 1; ++l1) {
                  BlockPos blockpos = pos.add(j1, k1, l1);
                  IBlockState iblockstate = worldIn.getBlockState(blockpos);
                  if (iblockstate.getMaterial() == Material.LEAVES && !(Boolean)iblockstate.getValue(CHECK_DECAY)) {
                     worldIn.setBlockState(blockpos, iblockstate.withProperty(CHECK_DECAY, true), 4);
                  }
               }
            }
         }
      }

   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (!worldIn.isRemote && (Boolean)state.getValue(CHECK_DECAY) && (Boolean)state.getValue(DECAYABLE)) {
         int i = true;
         int j = true;
         int k = pos.getX();
         int l = pos.getY();
         int i1 = pos.getZ();
         int j1 = true;
         int k1 = true;
         int l1 = true;
         if (this.surroundings == null) {
            this.surroundings = new int['耀'];
         }

         if (worldIn.isAreaLoaded(new BlockPos(k - 5, l - 5, i1 - 5), new BlockPos(k + 5, l + 5, i1 + 5))) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            int i3 = -4;

            label114:
            while(true) {
               int j3;
               int k3;
               if (i3 > 4) {
                  i3 = 1;

                  while(true) {
                     if (i3 > 4) {
                        break label114;
                     }

                     for(j3 = -4; j3 <= 4; ++j3) {
                        for(k3 = -4; k3 <= 4; ++k3) {
                           for(int l3 = -4; l3 <= 4; ++l3) {
                              if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16] == i3 - 1) {
                                 if (this.surroundings[(j3 + 16 - 1) * 1024 + (k3 + 16) * 32 + l3 + 16] == -2) {
                                    this.surroundings[(j3 + 16 - 1) * 1024 + (k3 + 16) * 32 + l3 + 16] = i3;
                                 }

                                 if (this.surroundings[(j3 + 16 + 1) * 1024 + (k3 + 16) * 32 + l3 + 16] == -2) {
                                    this.surroundings[(j3 + 16 + 1) * 1024 + (k3 + 16) * 32 + l3 + 16] = i3;
                                 }

                                 if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16 - 1) * 32 + l3 + 16] == -2) {
                                    this.surroundings[(j3 + 16) * 1024 + (k3 + 16 - 1) * 32 + l3 + 16] = i3;
                                 }

                                 if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16 + 1) * 32 + l3 + 16] == -2) {
                                    this.surroundings[(j3 + 16) * 1024 + (k3 + 16 + 1) * 32 + l3 + 16] = i3;
                                 }

                                 if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + (l3 + 16 - 1)] == -2) {
                                    this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + (l3 + 16 - 1)] = i3;
                                 }

                                 if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16 + 1] == -2) {
                                    this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16 + 1] = i3;
                                 }
                              }
                           }
                        }
                     }

                     ++i3;
                  }
               }

               for(j3 = -4; j3 <= 4; ++j3) {
                  for(k3 = -4; k3 <= 4; ++k3) {
                     IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(k + i3, l + j3, i1 + k3));
                     Block block = iblockstate.getBlock();
                     if (block != Blocks.LOG && block != Blocks.LOG2) {
                        if (iblockstate.getMaterial() == Material.LEAVES) {
                           this.surroundings[(i3 + 16) * 1024 + (j3 + 16) * 32 + k3 + 16] = -2;
                        } else {
                           this.surroundings[(i3 + 16) * 1024 + (j3 + 16) * 32 + k3 + 16] = -1;
                        }
                     } else {
                        this.surroundings[(i3 + 16) * 1024 + (j3 + 16) * 32 + k3 + 16] = 0;
                     }
                  }
               }

               ++i3;
            }
         }

         int l2 = this.surroundings[16912];
         if (l2 >= 0) {
            worldIn.setBlockState(pos, state.withProperty(CHECK_DECAY, false), 4);
         } else {
            this.destroy(worldIn, pos);
         }
      }

   }

   public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
      if (worldIn.isRainingAt(pos.up()) && !worldIn.getBlockState(pos.down()).isFullyOpaque() && rand.nextInt(15) == 1) {
         double d0 = (double)((float)pos.getX() + rand.nextFloat());
         double d1 = (double)pos.getY() - 0.05D;
         double d2 = (double)((float)pos.getZ() + rand.nextFloat());
         worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
      }

   }

   private void destroy(World worldIn, BlockPos pos) {
      this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
      worldIn.setBlockToAir(pos);
   }

   public int quantityDropped(Random random) {
      return random.nextInt(20) == 0 ? 1 : 0;
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Item.getItemFromBlock(Blocks.SAPLING);
   }

   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
      if (!worldIn.isRemote) {
         int i = this.getSaplingDropChance(state);
         if (fortune > 0) {
            i -= 2 << fortune;
            if (i < 10) {
               i = 10;
            }
         }

         if (worldIn.rand.nextInt(i) == 0) {
            Item item = this.getItemDropped(state, worldIn.rand, fortune);
            spawnAsEntity(worldIn, pos, new ItemStack(item, 1, this.damageDropped(state)));
         }

         i = 200;
         if (fortune > 0) {
            i -= 10 << fortune;
            if (i < 40) {
               i = 40;
            }
         }

         this.dropApple(worldIn, pos, state, i);
      }

   }

   protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
   }

   protected int getSaplingDropChance(IBlockState state) {
      return 20;
   }

   public boolean isOpaqueCube(IBlockState state) {
      return !this.leavesFancy;
   }

   public void setGraphicsLevel(boolean fancy) {
      this.leavesFancy = fancy;
   }

   public BlockRenderLayer getBlockLayer() {
      return this.leavesFancy ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
   }

   public boolean causesSuffocation(IBlockState p_176214_1_) {
      return false;
   }

   public abstract BlockPlanks.EnumType getWoodType(int var1);

   public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      return !this.leavesFancy && blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
   }
}
