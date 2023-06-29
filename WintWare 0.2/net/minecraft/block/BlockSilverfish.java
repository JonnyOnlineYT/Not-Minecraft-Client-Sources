package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSilverfish extends Block {
   public static final PropertyEnum<BlockSilverfish.EnumType> VARIANT = PropertyEnum.create("variant", BlockSilverfish.EnumType.class);

   public BlockSilverfish() {
      super(Material.CLAY);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockSilverfish.EnumType.STONE));
      this.setHardness(0.0F);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   public int quantityDropped(Random random) {
      return 0;
   }

   public static boolean canContainSilverfish(IBlockState blockState) {
      Block block = blockState.getBlock();
      return blockState == Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE) || block == Blocks.COBBLESTONE || block == Blocks.STONEBRICK;
   }

   protected ItemStack getSilkTouchDrop(IBlockState state) {
      switch((BlockSilverfish.EnumType)state.getValue(VARIANT)) {
      case COBBLESTONE:
         return new ItemStack(Blocks.COBBLESTONE);
      case STONEBRICK:
         return new ItemStack(Blocks.STONEBRICK);
      case MOSSY_STONEBRICK:
         return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.MOSSY.getMetadata());
      case CRACKED_STONEBRICK:
         return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.CRACKED.getMetadata());
      case CHISELED_STONEBRICK:
         return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.CHISELED.getMetadata());
      default:
         return new ItemStack(Blocks.STONE);
      }
   }

   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
      if (!worldIn.isRemote && worldIn.getGameRules().getBoolean("doTileDrops")) {
         EntitySilverfish entitysilverfish = new EntitySilverfish(worldIn);
         entitysilverfish.setLocationAndAngles((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
         worldIn.spawnEntityInWorld(entitysilverfish);
         entitysilverfish.spawnExplosionParticle();
      }

   }

   public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
      return new ItemStack(this, 1, state.getBlock().getMetaFromState(state));
   }

   public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
      BlockSilverfish.EnumType[] var3 = BlockSilverfish.EnumType.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockSilverfish.EnumType blocksilverfish$enumtype = var3[var5];
         tab.add(new ItemStack(this, 1, blocksilverfish$enumtype.getMetadata()));
      }

   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(VARIANT, BlockSilverfish.EnumType.byMetadata(meta));
   }

   public int getMetaFromState(IBlockState state) {
      return ((BlockSilverfish.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{VARIANT});
   }

   public static enum EnumType implements IStringSerializable {
      STONE(0, "stone") {
         public IBlockState getModelBlock() {
            return Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE);
         }
      },
      COBBLESTONE(1, "cobblestone", "cobble") {
         public IBlockState getModelBlock() {
            return Blocks.COBBLESTONE.getDefaultState();
         }
      },
      STONEBRICK(2, "stone_brick", "brick") {
         public IBlockState getModelBlock() {
            return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
         }
      },
      MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick") {
         public IBlockState getModelBlock() {
            return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
         }
      },
      CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick") {
         public IBlockState getModelBlock() {
            return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
         }
      },
      CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick") {
         public IBlockState getModelBlock() {
            return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
         }
      };

      private static final BlockSilverfish.EnumType[] META_LOOKUP = new BlockSilverfish.EnumType[values().length];
      private final int meta;
      private final String name;
      private final String unlocalizedName;

      private EnumType(int meta, String name) {
         this(meta, name, name);
      }

      private EnumType(int meta, String name, String unlocalizedName) {
         this.meta = meta;
         this.name = name;
         this.unlocalizedName = unlocalizedName;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String toString() {
         return this.name;
      }

      public static BlockSilverfish.EnumType byMetadata(int meta) {
         if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
         }

         return META_LOOKUP[meta];
      }

      public String getName() {
         return this.name;
      }

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      public abstract IBlockState getModelBlock();

      public static BlockSilverfish.EnumType forModelBlock(IBlockState model) {
         BlockSilverfish.EnumType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            BlockSilverfish.EnumType blocksilverfish$enumtype = var1[var3];
            if (model == blocksilverfish$enumtype.getModelBlock()) {
               return blocksilverfish$enumtype;
            }
         }

         return STONE;
      }

      // $FF: synthetic method
      EnumType(int x2, String x3, Object x4) {
         this(x2, x3);
      }

      // $FF: synthetic method
      EnumType(int x2, String x3, String x4, Object x5) {
         this(x2, x3, x4);
      }

      static {
         BlockSilverfish.EnumType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockSilverfish.EnumType blocksilverfish$enumtype = var0[var2];
            META_LOOKUP[blocksilverfish$enumtype.getMetadata()] = blocksilverfish$enumtype;
         }

      }
   }
}
