package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelNamespaced;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class BlockModelShapes
{
    private final Map<IBlockState, IBakedModel> bakedModelStore = Maps.newIdentityHashMap();
    private final BlockStateMapper blockStateMapper = new BlockStateMapper();
    private final ModelManager modelManager;

    public BlockModelShapes(ModelManager manager)
    {
        modelManager = manager;
        registerAllBlocks();
    }

    public BlockStateMapper getBlockStateMapper()
    {
        return blockStateMapper;
    }

    public TextureAtlasSprite getTexture(IBlockState state)
    {
        Block block = state.getBlock();
        IBakedModel ibakedmodel = getModelForState(state);

        if (ibakedmodel == null || ibakedmodel == modelManager.getMissingModel())
        {
            if (block == Blocks.WALL_SIGN || block == Blocks.STANDING_SIGN || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST || block == Blocks.STANDING_BANNER || block == Blocks.WALL_BANNER || block == Blocks.BED)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/planks_oak");
            }

            if (block == Blocks.ENDER_CHEST)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/obsidian");
            }

            if (block == Blocks.FLOWING_LAVA || block == Blocks.LAVA)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/lava_still");
            }

            if (block == Blocks.FLOWING_WATER || block == Blocks.WATER)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/water_still");
            }

            if (block == Blocks.SKULL)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/soul_sand");
            }

            if (block == Blocks.BARRIER)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:items/barrier");
            }

            if (block == Blocks.STRUCTURE_VOID)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:items/structure_void");
            }

            if (block == Blocks.WHITE_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_white");
            }

            if (block == Blocks.ORANGE_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_orange");
            }

            if (block == Blocks.MAGENTA_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_magenta");
            }

            if (block == Blocks.LIGHT_BLUE_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_light_blue");
            }

            if (block == Blocks.YELLOW_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_yellow");
            }

            if (block == Blocks.LIME_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_lime");
            }

            if (block == Blocks.PINK_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_pink");
            }

            if (block == Blocks.GRAY_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_gray");
            }

            if (block == Blocks.SILVER_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_silver");
            }

            if (block == Blocks.CYAN_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_cyan");
            }

            if (block == Blocks.PURPLE_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_purple");
            }

            if (block == Blocks.BLUE_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_blue");
            }

            if (block == Blocks.BROWN_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_brown");
            }

            if (block == Blocks.GREEN_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_green");
            }

            if (block == Blocks.RED_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_red");
            }

            if (block == Blocks.BLACK_SHULKER_BOX)
            {
                return modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_black");
            }
        }

        if (ibakedmodel == null)
        {
            ibakedmodel = modelManager.getMissingModel();
        }

        return ibakedmodel.getParticleTexture();
    }

    public IBakedModel getModelForState(IBlockState state)
    {
        IBakedModel ibakedmodel = bakedModelStore.get(state);

        if (ibakedmodel == null)
        {
            ibakedmodel = modelManager.getMissingModel();
        }

        return ibakedmodel;
    }

    public ModelManager getModelManager()
    {
        return modelManager;
    }

    public void reloadModels()
    {
        bakedModelStore.clear();

        for (Map.Entry<IBlockState, ModelNamespaced> entry : blockStateMapper.putAllStateModelLocations().entrySet())
        {
            bakedModelStore.put(entry.getKey(), modelManager.getModel(entry.getValue()));
        }
    }

    public void registerBlockWithStateMapper(Block assoc, IStateMapper stateMapper)
    {

        blockStateMapper.registerBlockStateMapper(assoc, stateMapper);
    }

    public void registerBuiltInBlocks(Block... builtIns)
    {
        blockStateMapper.registerBuiltInBlocks(builtIns);
    }

    private void registerAllBlocks()
    {
        registerBuiltInBlocks(Blocks.AIR, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.PISTON_EXTENSION, Blocks.CHEST, Blocks.ENDER_CHEST, Blocks.TRAPPED_CHEST, Blocks.STANDING_SIGN, Blocks.SKULL, Blocks.END_PORTAL, Blocks.BARRIER, Blocks.WALL_SIGN, Blocks.WALL_BANNER, Blocks.STANDING_BANNER, Blocks.END_GATEWAY, Blocks.STRUCTURE_VOID, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BED);
        registerBlockWithStateMapper(Blocks.STONE, (new StateMap.Builder()).withName(BlockStone.VARIANT).build());
        registerBlockWithStateMapper(Blocks.PRISMARINE, (new StateMap.Builder()).withName(BlockPrismarine.VARIANT).build());
        registerBlockWithStateMapper(Blocks.LEAVES, (new StateMap.Builder()).withName(BlockOldLeaf.VARIANT).withSuffix("_leaves").ignore(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE).build());
        registerBlockWithStateMapper(Blocks.LEAVES2, (new StateMap.Builder()).withName(BlockNewLeaf.VARIANT).withSuffix("_leaves").ignore(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE).build());
        registerBlockWithStateMapper(Blocks.CACTUS, (new StateMap.Builder()).ignore(BlockCactus.AGE).build());
        registerBlockWithStateMapper(Blocks.REEDS, (new StateMap.Builder()).ignore(BlockReed.AGE).build());
        registerBlockWithStateMapper(Blocks.JUKEBOX, (new StateMap.Builder()).ignore(BlockJukebox.HAS_RECORD).build());
        registerBlockWithStateMapper(Blocks.COBBLESTONE_WALL, (new StateMap.Builder()).withName(BlockWall.VARIANT).withSuffix("_wall").build());
        registerBlockWithStateMapper(Blocks.DOUBLE_PLANT, (new StateMap.Builder()).withName(BlockDoublePlant.VARIANT).ignore(BlockDoublePlant.FACING).build());
        registerBlockWithStateMapper(Blocks.OAK_FENCE_GATE, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
        registerBlockWithStateMapper(Blocks.SPRUCE_FENCE_GATE, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
        registerBlockWithStateMapper(Blocks.BIRCH_FENCE_GATE, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
        registerBlockWithStateMapper(Blocks.JUNGLE_FENCE_GATE, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
        registerBlockWithStateMapper(Blocks.DARK_OAK_FENCE_GATE, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
        registerBlockWithStateMapper(Blocks.ACACIA_FENCE_GATE, (new StateMap.Builder()).ignore(BlockFenceGate.POWERED).build());
        registerBlockWithStateMapper(Blocks.TRIPWIRE, (new StateMap.Builder()).ignore(BlockTripWire.DISARMED, BlockTripWire.POWERED).build());
        registerBlockWithStateMapper(Blocks.DOUBLE_WOODEN_SLAB, (new StateMap.Builder()).withName(BlockPlanks.VARIANT).withSuffix("_double_slab").build());
        registerBlockWithStateMapper(Blocks.WOODEN_SLAB, (new StateMap.Builder()).withName(BlockPlanks.VARIANT).withSuffix("_slab").build());
        registerBlockWithStateMapper(Blocks.TNT, (new StateMap.Builder()).ignore(BlockTNT.EXPLODE).build());
        registerBlockWithStateMapper(Blocks.FIRE, (new StateMap.Builder()).ignore(BlockFire.AGE).build());
        registerBlockWithStateMapper(Blocks.REDSTONE_WIRE, (new StateMap.Builder()).ignore(BlockRedstoneWire.POWER).build());
        registerBlockWithStateMapper(Blocks.OAK_DOOR, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
        registerBlockWithStateMapper(Blocks.SPRUCE_DOOR, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
        registerBlockWithStateMapper(Blocks.BIRCH_DOOR, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
        registerBlockWithStateMapper(Blocks.JUNGLE_DOOR, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
        registerBlockWithStateMapper(Blocks.ACACIA_DOOR, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
        registerBlockWithStateMapper(Blocks.DARK_OAK_DOOR, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
        registerBlockWithStateMapper(Blocks.IRON_DOOR, (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
        registerBlockWithStateMapper(Blocks.WOOL, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_wool").build());
        registerBlockWithStateMapper(Blocks.CARPET, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_carpet").build());
        registerBlockWithStateMapper(Blocks.STAINED_HARDENED_CLAY, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_stained_hardened_clay").build());
        registerBlockWithStateMapper(Blocks.STAINED_GLASS_PANE, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_stained_glass_pane").build());
        registerBlockWithStateMapper(Blocks.STAINED_GLASS, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_stained_glass").build());
        registerBlockWithStateMapper(Blocks.SANDSTONE, (new StateMap.Builder()).withName(BlockSandStone.TYPE).build());
        registerBlockWithStateMapper(Blocks.RED_SANDSTONE, (new StateMap.Builder()).withName(BlockRedSandstone.TYPE).build());
        registerBlockWithStateMapper(Blocks.TALLGRASS, (new StateMap.Builder()).withName(BlockTallGrass.TYPE).build());
        registerBlockWithStateMapper(Blocks.YELLOW_FLOWER, (new StateMap.Builder()).withName(Blocks.YELLOW_FLOWER.getTypeProperty()).build());
        registerBlockWithStateMapper(Blocks.RED_FLOWER, (new StateMap.Builder()).withName(Blocks.RED_FLOWER.getTypeProperty()).build());
        registerBlockWithStateMapper(Blocks.STONE_SLAB, (new StateMap.Builder()).withName(BlockStoneSlab.VARIANT).withSuffix("_slab").build());
        registerBlockWithStateMapper(Blocks.STONE_SLAB2, (new StateMap.Builder()).withName(BlockStoneSlabNew.VARIANT).withSuffix("_slab").build());
        registerBlockWithStateMapper(Blocks.MONSTER_EGG, (new StateMap.Builder()).withName(BlockSilverfish.VARIANT).withSuffix("_monster_egg").build());
        registerBlockWithStateMapper(Blocks.STONEBRICK, (new StateMap.Builder()).withName(BlockStoneBrick.VARIANT).build());
        registerBlockWithStateMapper(Blocks.DISPENSER, (new StateMap.Builder()).ignore(BlockDispenser.TRIGGERED).build());
        registerBlockWithStateMapper(Blocks.DROPPER, (new StateMap.Builder()).ignore(BlockDispenser.TRIGGERED).build());
        registerBlockWithStateMapper(Blocks.LOG, (new StateMap.Builder()).withName(BlockOldLog.VARIANT).withSuffix("_log").build());
        registerBlockWithStateMapper(Blocks.LOG2, (new StateMap.Builder()).withName(BlockNewLog.VARIANT).withSuffix("_log").build());
        registerBlockWithStateMapper(Blocks.PLANKS, (new StateMap.Builder()).withName(BlockPlanks.VARIANT).withSuffix("_planks").build());
        registerBlockWithStateMapper(Blocks.SAPLING, (new StateMap.Builder()).withName(BlockSapling.TYPE).withSuffix("_sapling").build());
        registerBlockWithStateMapper(Blocks.SAND, (new StateMap.Builder()).withName(BlockSand.VARIANT).build());
        registerBlockWithStateMapper(Blocks.HOPPER, (new StateMap.Builder()).ignore(BlockHopper.ENABLED).build());
        registerBlockWithStateMapper(Blocks.FLOWER_POT, (new StateMap.Builder()).ignore(BlockFlowerPot.LEGACY_DATA).build());
        registerBlockWithStateMapper(Blocks.field_192443_dR, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_concrete").build());
        registerBlockWithStateMapper(Blocks.field_192444_dS, (new StateMap.Builder()).withName(BlockColored.COLOR).withSuffix("_concrete_powder").build());
        registerBlockWithStateMapper(Blocks.QUARTZ_BLOCK, new StateMapperBase()
        {
            protected ModelNamespaced getModelResourceLocation(IBlockState state)
            {
                BlockQuartz.EnumType blockquartz$enumtype = state.getValue(BlockQuartz.VARIANT);

                switch (blockquartz$enumtype)
                {
                    case DEFAULT:
                    default:
                        return new ModelNamespaced("quartz_block", "normal");

                    case CHISELED:
                        return new ModelNamespaced("chiseled_quartz_block", "normal");

                    case LINES_Y:
                        return new ModelNamespaced("quartz_column", "axis=y");

                    case LINES_X:
                        return new ModelNamespaced("quartz_column", "axis=x");

                    case LINES_Z:
                        return new ModelNamespaced("quartz_column", "axis=z");
                }
            }
        });
        registerBlockWithStateMapper(Blocks.DEADBUSH, new StateMapperBase()
        {
            protected ModelNamespaced getModelResourceLocation(IBlockState state)
            {
                return new ModelNamespaced("dead_bush", "normal");
            }
        });
        registerBlockWithStateMapper(Blocks.PUMPKIN_STEM, new StateMapperBase()
        {
            protected ModelNamespaced getModelResourceLocation(IBlockState state)
            {
                Map < IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());

                if (state.getValue(BlockStem.FACING) != EnumFacing.UP)
                {
                    map.remove(BlockStem.AGE);
                }

                return new ModelNamespaced(Block.REGISTRY.getNameForObject(state.getBlock()), getPropertyString(map));
            }
        });
        registerBlockWithStateMapper(Blocks.MELON_STEM, new StateMapperBase()
        {
            protected ModelNamespaced getModelResourceLocation(IBlockState state)
            {
                Map < IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());

                if (state.getValue(BlockStem.FACING) != EnumFacing.UP)
                {
                    map.remove(BlockStem.AGE);
                }

                return new ModelNamespaced(Block.REGISTRY.getNameForObject(state.getBlock()), getPropertyString(map));
            }
        });
        registerBlockWithStateMapper(Blocks.DIRT, new StateMapperBase()
        {
            protected ModelNamespaced getModelResourceLocation(IBlockState state)
            {
                Map < IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());
                String s = BlockDirt.VARIANT.getName((BlockDirt.DirtType)map.remove(BlockDirt.VARIANT));

                if (BlockDirt.DirtType.PODZOL != state.getValue(BlockDirt.VARIANT))
                {
                    map.remove(BlockDirt.SNOWY);
                }

                return new ModelNamespaced(s, getPropertyString(map));
            }
        });
        registerBlockWithStateMapper(Blocks.DOUBLE_STONE_SLAB, new StateMapperBase()
        {
            protected ModelNamespaced getModelResourceLocation(IBlockState state)
            {
                Map < IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());
                String s = BlockStoneSlab.VARIANT.getName((BlockStoneSlab.EnumType)map.remove(BlockStoneSlab.VARIANT));
                map.remove(BlockStoneSlab.SEAMLESS);
                String s1 = state.getValue(BlockStoneSlab.SEAMLESS).booleanValue() ? "all" : "normal";
                return new ModelNamespaced(s + "_double_slab", s1);
            }
        });
        registerBlockWithStateMapper(Blocks.DOUBLE_STONE_SLAB2, new StateMapperBase()
        {
            protected ModelNamespaced getModelResourceLocation(IBlockState state)
            {
                Map < IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());
                String s = BlockStoneSlabNew.VARIANT.getName((BlockStoneSlabNew.EnumType)map.remove(BlockStoneSlabNew.VARIANT));
                map.remove(BlockStoneSlab.SEAMLESS);
                String s1 = state.getValue(BlockStoneSlabNew.SEAMLESS).booleanValue() ? "all" : "normal";
                return new ModelNamespaced(s + "_double_slab", s1);
            }
        });
    }
}
