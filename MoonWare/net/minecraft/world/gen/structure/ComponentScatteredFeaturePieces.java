package net.minecraft.world.gen.structure;

import java.util.Map;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Namespaced;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class ComponentScatteredFeaturePieces
{
    public static void registerScatteredFeaturePieces()
    {
        MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.DesertPyramid.class, "TeDP");
        MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.JunglePyramid.class, "TeJP");
        MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.SwampHut.class, "TeSH");
        MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.Igloo.class, "Iglu");
    }

    public static class DesertPyramid extends ComponentScatteredFeaturePieces.Feature
    {
        private final boolean[] hasPlacedChest = new boolean[4];

        public DesertPyramid()
        {
        }

        public DesertPyramid(Random p_i2062_1_, int p_i2062_2_, int p_i2062_3_)
        {
            super(p_i2062_1_, p_i2062_2_, 64, p_i2062_3_, 21, 15, 21);
        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("hasPlacedChest0", hasPlacedChest[0]);
            tagCompound.setBoolean("hasPlacedChest1", hasPlacedChest[1]);
            tagCompound.setBoolean("hasPlacedChest2", hasPlacedChest[2]);
            tagCompound.setBoolean("hasPlacedChest3", hasPlacedChest[3]);
        }

        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            hasPlacedChest[0] = tagCompound.getBoolean("hasPlacedChest0");
            hasPlacedChest[1] = tagCompound.getBoolean("hasPlacedChest1");
            hasPlacedChest[2] = tagCompound.getBoolean("hasPlacedChest2");
            hasPlacedChest[3] = tagCompound.getBoolean("hasPlacedChest3");
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
        {
            fillWithBlocks(worldIn, structureBoundingBoxIn, 0, -4, 0, scatteredFeatureSizeX - 1, 0, scatteredFeatureSizeZ - 1, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);

            for (int i = 1; i <= 9; ++i)
            {
                fillWithBlocks(worldIn, structureBoundingBoxIn, i, i, i, scatteredFeatureSizeX - 1 - i, i, scatteredFeatureSizeZ - 1 - i, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, i + 1, i, i + 1, scatteredFeatureSizeX - 2 - i, i, scatteredFeatureSizeZ - 2 - i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            for (int i2 = 0; i2 < scatteredFeatureSizeX; ++i2)
            {
                for (int j = 0; j < scatteredFeatureSizeZ; ++j)
                {
                    int k = -5;
                    replaceAirAndLiquidDownwards(worldIn, Blocks.SANDSTONE.getDefaultState(), i2, -5, j, structureBoundingBoxIn);
                }
            }

            IBlockState iblockstate1 = Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
            IBlockState iblockstate2 = Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
            IBlockState iblockstate3 = Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
            IBlockState iblockstate = Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
            int l = ~EnumDyeColor.ORANGE.getDyeDamage() & 15;
            int i1 = ~EnumDyeColor.BLUE.getDyeDamage() & 15;
            fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 9, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 10, 1, 3, 10, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            setBlockState(worldIn, iblockstate1, 2, 10, 0, structureBoundingBoxIn);
            setBlockState(worldIn, iblockstate2, 2, 10, 4, structureBoundingBoxIn);
            setBlockState(worldIn, iblockstate3, 0, 10, 2, structureBoundingBoxIn);
            setBlockState(worldIn, iblockstate, 4, 10, 2, structureBoundingBoxIn);
            fillWithBlocks(worldIn, structureBoundingBoxIn, scatteredFeatureSizeX - 5, 0, 0, scatteredFeatureSizeX - 1, 9, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, scatteredFeatureSizeX - 4, 10, 1, scatteredFeatureSizeX - 2, 10, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            setBlockState(worldIn, iblockstate1, scatteredFeatureSizeX - 3, 10, 0, structureBoundingBoxIn);
            setBlockState(worldIn, iblockstate2, scatteredFeatureSizeX - 3, 10, 4, structureBoundingBoxIn);
            setBlockState(worldIn, iblockstate3, scatteredFeatureSizeX - 5, 10, 2, structureBoundingBoxIn);
            setBlockState(worldIn, iblockstate, scatteredFeatureSizeX - 1, 10, 2, structureBoundingBoxIn);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 0, 12, 4, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 0, 11, 3, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 1, 1, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 2, 1, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 3, 1, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, 3, 1, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 3, 1, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 2, 1, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 1, 1, structureBoundingBoxIn);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 8, 3, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 2, 8, 2, 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 1, 16, 3, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 2, 16, 2, 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 5, scatteredFeatureSizeX - 6, 4, scatteredFeatureSizeZ - 6, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 4, 9, 11, 4, 11, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 8, 8, 3, 8, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 8, 12, 3, 8, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 12, 8, 3, 12, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 12, 12, 3, 12, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 4, 4, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, scatteredFeatureSizeX - 5, 1, 5, scatteredFeatureSizeX - 2, 4, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 7, 9, 6, 7, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, scatteredFeatureSizeX - 7, 7, 9, scatteredFeatureSizeX - 7, 7, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 9, 5, 7, 11, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, scatteredFeatureSizeX - 6, 5, 9, scatteredFeatureSizeX - 6, 7, 11, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 5, 5, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 5, 6, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 6, 6, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), scatteredFeatureSizeX - 6, 5, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), scatteredFeatureSizeX - 6, 6, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), scatteredFeatureSizeX - 7, 6, 10, structureBoundingBoxIn);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 4, 2, 6, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, scatteredFeatureSizeX - 3, 4, 4, scatteredFeatureSizeX - 3, 6, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            setBlockState(worldIn, iblockstate1, 2, 4, 5, structureBoundingBoxIn);
            setBlockState(worldIn, iblockstate1, 2, 3, 4, structureBoundingBoxIn);
            setBlockState(worldIn, iblockstate1, scatteredFeatureSizeX - 3, 4, 5, structureBoundingBoxIn);
            setBlockState(worldIn, iblockstate1, scatteredFeatureSizeX - 3, 3, 4, structureBoundingBoxIn);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 3, 2, 2, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, scatteredFeatureSizeX - 3, 1, 3, scatteredFeatureSizeX - 2, 2, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            setBlockState(worldIn, Blocks.SANDSTONE.getDefaultState(), 1, 1, 2, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getDefaultState(), scatteredFeatureSizeX - 2, 1, 2, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), 1, 2, 2, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), scatteredFeatureSizeX - 2, 2, 2, structureBoundingBoxIn);
            setBlockState(worldIn, iblockstate, 2, 1, 2, structureBoundingBoxIn);
            setBlockState(worldIn, iblockstate3, scatteredFeatureSizeX - 3, 1, 2, structureBoundingBoxIn);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 5, 4, 3, 18, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, scatteredFeatureSizeX - 5, 3, 5, scatteredFeatureSizeX - 5, 3, 17, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 5, 4, 2, 16, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, scatteredFeatureSizeX - 6, 1, 5, scatteredFeatureSizeX - 5, 2, 16, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

            for (int j1 = 5; j1 <= 17; j1 += 2)
            {
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 4, 1, j1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 4, 2, j1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), scatteredFeatureSizeX - 5, 1, j1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), scatteredFeatureSizeX - 5, 2, j1, structureBoundingBoxIn);
            }

            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 10, 0, 7, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 10, 0, 8, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 9, 0, 9, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 11, 0, 9, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 8, 0, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 12, 0, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 7, 0, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 13, 0, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 9, 0, 11, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 11, 0, 11, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 10, 0, 12, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 10, 0, 13, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(i1), 10, 0, 10, structureBoundingBoxIn);

            for (int j2 = 0; j2 <= scatteredFeatureSizeX - 1; j2 += scatteredFeatureSizeX - 1)
            {
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j2, 2, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), j2, 2, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j2, 2, 3, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j2, 3, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), j2, 3, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j2, 3, 3, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), j2, 4, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j2, 4, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), j2, 4, 3, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j2, 5, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), j2, 5, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j2, 5, 3, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), j2, 6, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j2, 6, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), j2, 6, 3, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), j2, 7, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), j2, 7, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), j2, 7, 3, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j2, 8, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j2, 8, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j2, 8, 3, structureBoundingBoxIn);
            }

            for (int k2 = 2; k2 <= scatteredFeatureSizeX - 3; k2 += scatteredFeatureSizeX - 3 - 2)
            {
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k2 - 1, 2, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), k2, 2, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k2 + 1, 2, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k2 - 1, 3, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), k2, 3, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k2 + 1, 3, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), k2 - 1, 4, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), k2, 4, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), k2 + 1, 4, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k2 - 1, 5, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), k2, 5, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k2 + 1, 5, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), k2 - 1, 6, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), k2, 6, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), k2 + 1, 6, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), k2 - 1, 7, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), k2, 7, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), k2 + 1, 7, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k2 - 1, 8, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k2, 8, 0, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k2 + 1, 8, 0, structureBoundingBoxIn);
            }

            fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 4, 0, 12, 6, 0, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 8, 6, 0, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 12, 6, 0, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 9, 5, 0, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, 5, 0, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(l), 11, 5, 0, structureBoundingBoxIn);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -14, 8, 12, -11, 12, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -10, 8, 12, -10, 12, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -9, 8, 12, -9, 12, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -8, 8, 12, -1, 12, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 9, -11, 9, 11, -1, 11, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            setBlockState(worldIn, Blocks.STONE_PRESSURE_PLATE.getDefaultState(), 10, -11, 10, structureBoundingBoxIn);
            fillWithBlocks(worldIn, structureBoundingBoxIn, 9, -13, 9, 11, -13, 11, Blocks.TNT.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 8, -11, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 8, -10, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 7, -10, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 7, -11, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 12, -11, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 12, -10, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 13, -10, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 13, -11, 10, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 10, -11, 8, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 10, -10, 8, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 7, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 7, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 10, -11, 12, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.AIR.getDefaultState(), 10, -10, 12, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 13, structureBoundingBoxIn);
            setBlockState(worldIn, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 13, structureBoundingBoxIn);

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                if (!hasPlacedChest[enumfacing.getHorizontalIndex()])
                {
                    int k1 = enumfacing.getFrontOffsetX() * 2;
                    int l1 = enumfacing.getFrontOffsetZ() * 2;
                    hasPlacedChest[enumfacing.getHorizontalIndex()] = generateChest(worldIn, structureBoundingBoxIn, randomIn, 10 + k1, -11, 10 + l1, LootTableList.CHESTS_DESERT_PYRAMID);
                }
            }

            return true;
        }
    }

    abstract static class Feature extends StructureComponent
    {
        protected int scatteredFeatureSizeX;
        protected int scatteredFeatureSizeY;
        protected int scatteredFeatureSizeZ;
        protected int horizontalPos = -1;

        public Feature()
        {
        }

        protected Feature(Random rand, int x, int y, int z, int sizeX, int sizeY, int sizeZ)
        {
            super(0);
            scatteredFeatureSizeX = sizeX;
            scatteredFeatureSizeY = sizeY;
            scatteredFeatureSizeZ = sizeZ;
            setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));

            if (getCoordBaseMode().getAxis() == EnumFacing.Axis.Z)
            {
                boundingBox = new StructureBoundingBox(x, y, z, x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1);
            }
            else
            {
                boundingBox = new StructureBoundingBox(x, y, z, x + sizeZ - 1, y + sizeY - 1, z + sizeX - 1);
            }
        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            tagCompound.setInteger("Width", scatteredFeatureSizeX);
            tagCompound.setInteger("Height", scatteredFeatureSizeY);
            tagCompound.setInteger("Depth", scatteredFeatureSizeZ);
            tagCompound.setInteger("HPos", horizontalPos);
        }

        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            scatteredFeatureSizeX = tagCompound.getInteger("Width");
            scatteredFeatureSizeY = tagCompound.getInteger("Height");
            scatteredFeatureSizeZ = tagCompound.getInteger("Depth");
            horizontalPos = tagCompound.getInteger("HPos");
        }

        protected boolean offsetToAverageGroundLevel(World worldIn, StructureBoundingBox structurebb, int yOffset)
        {
            if (horizontalPos >= 0)
            {
                return true;
            }
            else
            {
                int i = 0;
                int j = 0;
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int k = boundingBox.minZ; k <= boundingBox.maxZ; ++k)
                {
                    for (int l = boundingBox.minX; l <= boundingBox.maxX; ++l)
                    {
                        blockpos$mutableblockpos.setPos(l, 64, k);

                        if (structurebb.isVecInside(blockpos$mutableblockpos))
                        {
                            i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel());
                            ++j;
                        }
                    }
                }

                if (j == 0)
                {
                    return false;
                }
                else
                {
                    horizontalPos = i / j;
                    boundingBox.offset(0, horizontalPos - boundingBox.minY + yOffset, 0);
                    return true;
                }
            }
        }
    }

    public static class Igloo extends ComponentScatteredFeaturePieces.Feature
    {
        private static final Namespaced IGLOO_TOP_ID = new Namespaced("igloo/igloo_top");
        private static final Namespaced IGLOO_MIDDLE_ID = new Namespaced("igloo/igloo_middle");
        private static final Namespaced IGLOO_BOTTOM_ID = new Namespaced("igloo/igloo_bottom");

        public Igloo()
        {
        }

        public Igloo(Random rand, int x, int z)
        {
            super(rand, x, 64, z, 7, 5, 8);
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
        {
            if (!offsetToAverageGroundLevel(worldIn, structureBoundingBoxIn, -1))
            {
                return false;
            }
            else
            {
                StructureBoundingBox structureboundingbox = getBoundingBox();
                BlockPos blockpos = new BlockPos(structureboundingbox.minX, structureboundingbox.minY, structureboundingbox.minZ);
                Rotation[] arotation = Rotation.values();
                MinecraftServer minecraftserver = worldIn.getInstanceServer();
                TemplateManager templatemanager = worldIn.getSaveHandler().getStructureTemplateManager();
                PlacementSettings placementsettings = (new PlacementSettings()).setRotation(arotation[randomIn.nextInt(arotation.length)]).setReplacedBlock(Blocks.STRUCTURE_VOID).setBoundingBox(structureboundingbox);
                Template template = templatemanager.getTemplate(minecraftserver, IGLOO_TOP_ID);
                template.addBlocksToWorldChunk(worldIn, blockpos, placementsettings);

                if (randomIn.nextDouble() < 0.5D)
                {
                    Template template1 = templatemanager.getTemplate(minecraftserver, IGLOO_MIDDLE_ID);
                    Template template2 = templatemanager.getTemplate(minecraftserver, IGLOO_BOTTOM_ID);
                    int i = randomIn.nextInt(8) + 4;

                    for (int j = 0; j < i; ++j)
                    {
                        BlockPos blockpos1 = template.calculateConnectedPos(placementsettings, new BlockPos(3, -1 - j * 3, 5), placementsettings, new BlockPos(1, 2, 1));
                        template1.addBlocksToWorldChunk(worldIn, blockpos.add(blockpos1), placementsettings);
                    }

                    BlockPos blockpos4 = blockpos.add(template.calculateConnectedPos(placementsettings, new BlockPos(3, -1 - i * 3, 5), placementsettings, new BlockPos(3, 5, 7)));
                    template2.addBlocksToWorldChunk(worldIn, blockpos4, placementsettings);
                    Map<BlockPos, String> map = template2.getDataBlocks(blockpos4, placementsettings);

                    for (Map.Entry<BlockPos, String> entry : map.entrySet())
                    {
                        if ("chest".equals(entry.getValue()))
                        {
                            BlockPos blockpos2 = entry.getKey();
                            worldIn.setBlockState(blockpos2, Blocks.AIR.getDefaultState(), 3);
                            TileEntity tileentity = worldIn.getTileEntity(blockpos2.down());

                            if (tileentity instanceof TileEntityChest)
                            {
                                ((TileEntityChest)tileentity).setLootTable(LootTableList.CHESTS_IGLOO_CHEST, randomIn.nextLong());
                            }
                        }
                    }
                }
                else
                {
                    BlockPos blockpos3 = Template.transformedBlockPos(placementsettings, new BlockPos(3, 0, 5));
                    worldIn.setBlockState(blockpos.add(blockpos3), Blocks.SNOW.getDefaultState(), 3);
                }

                return true;
            }
        }
    }

    public static class JunglePyramid extends ComponentScatteredFeaturePieces.Feature
    {
        private boolean placedMainChest;
        private boolean placedHiddenChest;
        private boolean placedTrap1;
        private boolean placedTrap2;
        private static final ComponentScatteredFeaturePieces.JunglePyramid.Stones junglePyramidsRandomScatteredStones = new ComponentScatteredFeaturePieces.JunglePyramid.Stones();

        public JunglePyramid()
        {
        }

        public JunglePyramid(Random rand, int x, int z)
        {
            super(rand, x, 64, z, 12, 10, 15);
        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("placedMainChest", placedMainChest);
            tagCompound.setBoolean("placedHiddenChest", placedHiddenChest);
            tagCompound.setBoolean("placedTrap1", placedTrap1);
            tagCompound.setBoolean("placedTrap2", placedTrap2);
        }

        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            placedMainChest = tagCompound.getBoolean("placedMainChest");
            placedHiddenChest = tagCompound.getBoolean("placedHiddenChest");
            placedTrap1 = tagCompound.getBoolean("placedTrap1");
            placedTrap2 = tagCompound.getBoolean("placedTrap2");
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
        {
            if (!offsetToAverageGroundLevel(worldIn, structureBoundingBoxIn, 0))
            {
                return false;
            }
            else
            {
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, -4, 0, scatteredFeatureSizeX - 1, 0, scatteredFeatureSizeZ - 1, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 2, 9, 2, 2, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 12, 9, 2, 12, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 3, 2, 2, 11, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 1, 3, 9, 2, 11, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 1, 10, 6, 1, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 13, 10, 6, 13, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 2, 1, 6, 12, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 10, 3, 2, 10, 6, 12, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 3, 2, 9, 3, 12, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 6, 2, 9, 6, 12, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 3, 7, 3, 8, 7, 11, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 8, 4, 7, 8, 10, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 3, 8, 2, 11);
                fillWithAir(worldIn, structureBoundingBoxIn, 4, 3, 6, 7, 3, 9);
                fillWithAir(worldIn, structureBoundingBoxIn, 2, 4, 2, 9, 5, 12);
                fillWithAir(worldIn, structureBoundingBoxIn, 4, 6, 5, 7, 6, 9);
                fillWithAir(worldIn, structureBoundingBoxIn, 5, 7, 6, 6, 7, 8);
                fillWithAir(worldIn, structureBoundingBoxIn, 5, 1, 2, 6, 2, 2);
                fillWithAir(worldIn, structureBoundingBoxIn, 5, 2, 12, 6, 2, 12);
                fillWithAir(worldIn, structureBoundingBoxIn, 5, 5, 1, 6, 5, 1);
                fillWithAir(worldIn, structureBoundingBoxIn, 5, 5, 13, 6, 5, 13);
                setBlockState(worldIn, Blocks.AIR.getDefaultState(), 1, 5, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.AIR.getDefaultState(), 10, 5, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.AIR.getDefaultState(), 1, 5, 9, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.AIR.getDefaultState(), 10, 5, 9, structureBoundingBoxIn);

                for (int i = 0; i <= 14; i += 14)
                {
                    fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 4, i, 2, 5, i, false, randomIn, junglePyramidsRandomScatteredStones);
                    fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 4, i, 4, 5, i, false, randomIn, junglePyramidsRandomScatteredStones);
                    fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 4, i, 7, 5, i, false, randomIn, junglePyramidsRandomScatteredStones);
                    fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 4, i, 9, 5, i, false, randomIn, junglePyramidsRandomScatteredStones);
                }

                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 6, 0, 6, 6, 0, false, randomIn, junglePyramidsRandomScatteredStones);

                for (int l = 0; l <= 11; l += 11)
                {
                    for (int j = 2; j <= 12; j += 2)
                    {
                        fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, l, 4, j, l, 5, j, false, randomIn, junglePyramidsRandomScatteredStones);
                    }

                    fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, l, 6, 5, l, 6, 5, false, randomIn, junglePyramidsRandomScatteredStones);
                    fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, l, 6, 9, l, 6, 9, false, randomIn, junglePyramidsRandomScatteredStones);
                }

                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 7, 2, 2, 9, 2, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 7, 2, 9, 9, 2, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 7, 12, 2, 9, 12, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 7, 12, 9, 9, 12, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 9, 4, 4, 9, 4, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 9, 4, 7, 9, 4, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 9, 10, 4, 9, 10, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 9, 10, 7, 9, 10, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 9, 7, 6, 9, 7, false, randomIn, junglePyramidsRandomScatteredStones);
                IBlockState iblockstate2 = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
                IBlockState iblockstate3 = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
                IBlockState iblockstate = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
                IBlockState iblockstate1 = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
                setBlockState(worldIn, iblockstate1, 5, 9, 6, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate1, 6, 9, 6, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate, 5, 9, 8, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate, 6, 9, 8, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate1, 4, 0, 0, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate1, 5, 0, 0, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate1, 6, 0, 0, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate1, 7, 0, 0, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate1, 4, 1, 8, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate1, 4, 2, 9, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate1, 4, 3, 10, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate1, 7, 1, 8, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate1, 7, 2, 9, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate1, 7, 3, 10, structureBoundingBoxIn);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 4, 1, 9, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 1, 9, 7, 1, 9, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 10, 7, 2, 10, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 4, 5, 6, 4, 5, false, randomIn, junglePyramidsRandomScatteredStones);
                setBlockState(worldIn, iblockstate2, 4, 4, 5, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate3, 7, 4, 5, structureBoundingBoxIn);

                for (int k = 0; k < 4; ++k)
                {
                    setBlockState(worldIn, iblockstate, 5, 0 - k, 6 + k, structureBoundingBoxIn);
                    setBlockState(worldIn, iblockstate, 6, 0 - k, 6 + k, structureBoundingBoxIn);
                    fillWithAir(worldIn, structureBoundingBoxIn, 5, 0 - k, 7 + k, 6, 0 - k, 9 + k);
                }

                fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 12, 10, -1, 13);
                fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 1, 3, -1, 13);
                fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 1, 9, -1, 5);

                for (int i1 = 1; i1 <= 13; i1 += 2)
                {
                    fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, -3, i1, 1, -2, i1, false, randomIn, junglePyramidsRandomScatteredStones);
                }

                for (int j1 = 2; j1 <= 12; j1 += 2)
                {
                    fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, -1, j1, 3, -1, j1, false, randomIn, junglePyramidsRandomScatteredStones);
                }

                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, -2, 1, 5, -2, 1, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, -2, 1, 9, -2, 1, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 6, -3, 1, 6, -3, 1, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 6, -1, 1, 6, -1, 1, false, randomIn, junglePyramidsRandomScatteredStones);
                setBlockState(worldIn, Blocks.TRIPWIRE_HOOK.getDefaultState().withProperty(BlockTripWireHook.FACING, EnumFacing.EAST).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 1, -3, 8, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.TRIPWIRE_HOOK.getDefaultState().withProperty(BlockTripWireHook.FACING, EnumFacing.WEST).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 4, -3, 8, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.TRIPWIRE.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 2, -3, 8, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.TRIPWIRE.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 3, -3, 8, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 7, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 6, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 4, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 3, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 4, -3, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 3, -3, 1, structureBoundingBoxIn);

                if (!placedTrap1)
                {
                    placedTrap1 = createDispenser(worldIn, structureBoundingBoxIn, randomIn, 3, -2, 1, EnumFacing.NORTH, LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER);
                }

                setBlockState(worldIn, Blocks.VINE.getDefaultState().withProperty(BlockVine.SOUTH, Boolean.valueOf(true)), 3, -2, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.TRIPWIRE_HOOK.getDefaultState().withProperty(BlockTripWireHook.FACING, EnumFacing.NORTH).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 7, -3, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.TRIPWIRE_HOOK.getDefaultState().withProperty(BlockTripWireHook.FACING, EnumFacing.SOUTH).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 7, -3, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.TRIPWIRE.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.TRIPWIRE.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 3, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.TRIPWIRE.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 4, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 8, -3, 6, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 9, -3, 6, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 9, -3, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 9, -3, 4, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 9, -2, 4, structureBoundingBoxIn);

                if (!placedTrap2)
                {
                    placedTrap2 = createDispenser(worldIn, structureBoundingBoxIn, randomIn, 9, -2, 3, EnumFacing.WEST, LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER);
                }

                setBlockState(worldIn, Blocks.VINE.getDefaultState().withProperty(BlockVine.EAST, Boolean.valueOf(true)), 8, -1, 3, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.VINE.getDefaultState().withProperty(BlockVine.EAST, Boolean.valueOf(true)), 8, -2, 3, structureBoundingBoxIn);

                if (!placedMainChest)
                {
                    placedMainChest = generateChest(worldIn, structureBoundingBoxIn, randomIn, 8, -3, 3, LootTableList.CHESTS_JUNGLE_TEMPLE);
                }

                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 9, -3, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 8, -3, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 4, -3, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 5, -2, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 5, -1, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 6, -3, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 7, -2, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 7, -1, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 8, -3, 5, structureBoundingBoxIn);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, -1, 1, 9, -1, 5, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithAir(worldIn, structureBoundingBoxIn, 8, -3, 8, 10, -1, 10);
                setBlockState(worldIn, Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.CHISELED_META), 8, -2, 11, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.CHISELED_META), 9, -2, 11, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.CHISELED_META), 10, -2, 11, structureBoundingBoxIn);
                IBlockState iblockstate4 = Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, BlockLever.EnumOrientation.NORTH);
                setBlockState(worldIn, iblockstate4, 8, -2, 12, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate4, 9, -2, 12, structureBoundingBoxIn);
                setBlockState(worldIn, iblockstate4, 10, -2, 12, structureBoundingBoxIn);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 8, -3, 8, 8, -3, 10, false, randomIn, junglePyramidsRandomScatteredStones);
                fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 10, -3, 8, 10, -3, 10, false, randomIn, junglePyramidsRandomScatteredStones);
                setBlockState(worldIn, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 10, -2, 9, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 8, -2, 9, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 8, -2, 10, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.REDSTONE_WIRE.getDefaultState(), 10, -1, 9, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STICKY_PISTON.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP), 9, -2, 8, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STICKY_PISTON.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.WEST), 10, -2, 8, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.STICKY_PISTON.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.WEST), 10, -1, 8, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.UNPOWERED_REPEATER.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH), 10, -2, 10, structureBoundingBoxIn);

                if (!placedHiddenChest)
                {
                    placedHiddenChest = generateChest(worldIn, structureBoundingBoxIn, randomIn, 9, -3, 10, LootTableList.CHESTS_JUNGLE_TEMPLE);
                }

                return true;
            }
        }

        static class Stones extends StructureComponent.BlockSelector
        {
            private Stones()
            {
            }

            public void selectBlocks(Random rand, int x, int y, int z, boolean p_75062_5_)
            {
                if (rand.nextFloat() < 0.4F)
                {
                    blockstate = Blocks.COBBLESTONE.getDefaultState();
                }
                else
                {
                    blockstate = Blocks.MOSSY_COBBLESTONE.getDefaultState();
                }
            }
        }
    }

    public static class SwampHut extends ComponentScatteredFeaturePieces.Feature
    {
        private boolean hasWitch;

        public SwampHut()
        {
        }

        public SwampHut(Random p_i2066_1_, int p_i2066_2_, int p_i2066_3_)
        {
            super(p_i2066_1_, p_i2066_2_, 64, p_i2066_3_, 7, 7, 9);
        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("Witch", hasWitch);
        }

        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            hasWitch = tagCompound.getBoolean("Witch");
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
        {
            if (!offsetToAverageGroundLevel(worldIn, structureBoundingBoxIn, 0))
            {
                return false;
            }
            else
            {
                fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 5, 1, 7, Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 5, 4, 7, Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 4, 1, 0, Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 2, 3, 3, 2, Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 3, 1, 3, 6, Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 3, 5, 3, 6, Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 7, 4, 3, 7, Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 1, 3, 2, Blocks.LOG.getDefaultState(), Blocks.LOG.getDefaultState(), false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 3, 2, Blocks.LOG.getDefaultState(), Blocks.LOG.getDefaultState(), false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 1, 3, 7, Blocks.LOG.getDefaultState(), Blocks.LOG.getDefaultState(), false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 7, 5, 3, 7, Blocks.LOG.getDefaultState(), Blocks.LOG.getDefaultState(), false);
                setBlockState(worldIn, Blocks.OAK_FENCE.getDefaultState(), 2, 3, 2, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.OAK_FENCE.getDefaultState(), 3, 3, 7, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.AIR.getDefaultState(), 1, 3, 4, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.AIR.getDefaultState(), 5, 3, 4, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.AIR.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.FLOWER_POT.getDefaultState().withProperty(BlockFlowerPot.CONTENTS, BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 1, 3, 5, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.CRAFTING_TABLE.getDefaultState(), 3, 2, 6, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.CAULDRON.getDefaultState(), 4, 2, 6, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.OAK_FENCE.getDefaultState(), 1, 2, 1, structureBoundingBoxIn);
                setBlockState(worldIn, Blocks.OAK_FENCE.getDefaultState(), 5, 2, 1, structureBoundingBoxIn);
                IBlockState iblockstate = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
                IBlockState iblockstate1 = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
                IBlockState iblockstate2 = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
                IBlockState iblockstate3 = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 6, 4, 1, iblockstate, iblockstate, false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 2, 0, 4, 7, iblockstate1, iblockstate1, false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 2, 6, 4, 7, iblockstate2, iblockstate2, false);
                fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 8, 6, 4, 8, iblockstate3, iblockstate3, false);

                for (int i = 2; i <= 7; i += 5)
                {
                    for (int j = 1; j <= 5; j += 4)
                    {
                        replaceAirAndLiquidDownwards(worldIn, Blocks.LOG.getDefaultState(), j, -1, i, structureBoundingBoxIn);
                    }
                }

                if (!hasWitch)
                {
                    int l = getXWithOffset(2, 5);
                    int i1 = getYWithOffset(2);
                    int k = getZWithOffset(2, 5);

                    if (structureBoundingBoxIn.isVecInside(new BlockPos(l, i1, k)))
                    {
                        hasWitch = true;
                        EntityWitch entitywitch = new EntityWitch(worldIn);
                        entitywitch.enablePersistence();
                        entitywitch.setLocationAndAngles((double)l + 0.5D, i1, (double)k + 0.5D, 0.0F, 0.0F);
                        entitywitch.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(l, i1, k)), null);
                        worldIn.spawnEntityInWorld(entitywitch);
                    }
                }

                return true;
            }
        }
    }
}
