package net.minecraft.world.gen.structure;

import java.util.Map;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class StructureComponentTemplate extends StructureComponent
{
    private static final PlacementSettings DEFAULT_PLACE_SETTINGS = new PlacementSettings();
    protected Template template;
    protected PlacementSettings placeSettings;
    protected BlockPos templatePosition;

    public StructureComponentTemplate()
    {
        placeSettings = DEFAULT_PLACE_SETTINGS.setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
    }

    public StructureComponentTemplate(int p_i46662_1_)
    {
        super(p_i46662_1_);
        placeSettings = DEFAULT_PLACE_SETTINGS.setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
    }

    protected void setup(Template p_186173_1_, BlockPos p_186173_2_, PlacementSettings p_186173_3_)
    {
        template = p_186173_1_;
        setCoordBaseMode(EnumFacing.NORTH);
        templatePosition = p_186173_2_;
        placeSettings = p_186173_3_;
        setBoundingBoxFromTemplate();
    }

    /**
     * (abstract) Helper method to write subclass data to NBT
     */
    protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("TPX", templatePosition.getX());
        tagCompound.setInteger("TPY", templatePosition.getY());
        tagCompound.setInteger("TPZ", templatePosition.getZ());
    }

    /**
     * (abstract) Helper method to read subclass data from NBT
     */
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
    {
        templatePosition = new BlockPos(tagCompound.getInteger("TPX"), tagCompound.getInteger("TPY"), tagCompound.getInteger("TPZ"));
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
    {
        placeSettings.setBoundingBox(structureBoundingBoxIn);
        template.addBlocksToWorld(worldIn, templatePosition, placeSettings, 18);
        Map<BlockPos, String> map = template.getDataBlocks(templatePosition, placeSettings);

        for (Map.Entry<BlockPos, String> entry : map.entrySet())
        {
            String s = entry.getValue();
            handleDataMarker(s, entry.getKey(), worldIn, randomIn, structureBoundingBoxIn);
        }

        return true;
    }

    protected abstract void handleDataMarker(String p_186175_1_, BlockPos p_186175_2_, World p_186175_3_, Random p_186175_4_, StructureBoundingBox p_186175_5_);

    private void setBoundingBoxFromTemplate()
    {
        Rotation rotation = placeSettings.getRotation();
        BlockPos blockpos = template.transformedSize(rotation);
        Mirror mirror = placeSettings.getMirror();
        boundingBox = new StructureBoundingBox(0, 0, 0, blockpos.getX(), blockpos.getY() - 1, blockpos.getZ());

        switch (rotation)
        {
            case NONE:
            default:
                break;

            case CLOCKWISE_90:
                boundingBox.offset(-blockpos.getX(), 0, 0);
                break;

            case COUNTERCLOCKWISE_90:
                boundingBox.offset(0, 0, -blockpos.getZ());
                break;

            case CLOCKWISE_180:
                boundingBox.offset(-blockpos.getX(), 0, -blockpos.getZ());
        }

        switch (mirror)
        {
            case NONE:
            default:
                break;

            case FRONT_BACK:
                BlockPos blockpos2 = BlockPos.ORIGIN;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90)
                {
                    if (rotation == Rotation.CLOCKWISE_180)
                    {
                        blockpos2 = blockpos2.offset(EnumFacing.EAST, blockpos.getX());
                    }
                    else
                    {
                        blockpos2 = blockpos2.offset(EnumFacing.WEST, blockpos.getX());
                    }
                }
                else
                {
                    blockpos2 = blockpos2.offset(rotation.rotate(EnumFacing.WEST), blockpos.getZ());
                }

                boundingBox.offset(blockpos2.getX(), 0, blockpos2.getZ());
                break;

            case LEFT_RIGHT:
                BlockPos blockpos1 = BlockPos.ORIGIN;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90)
                {
                    if (rotation == Rotation.CLOCKWISE_180)
                    {
                        blockpos1 = blockpos1.offset(EnumFacing.SOUTH, blockpos.getZ());
                    }
                    else
                    {
                        blockpos1 = blockpos1.offset(EnumFacing.NORTH, blockpos.getZ());
                    }
                }
                else
                {
                    blockpos1 = blockpos1.offset(rotation.rotate(EnumFacing.NORTH), blockpos.getX());
                }

                boundingBox.offset(blockpos1.getX(), 0, blockpos1.getZ());
        }

        boundingBox.offset(templatePosition.getX(), templatePosition.getY(), templatePosition.getZ());
    }

    public void offset(int x, int y, int z)
    {
        super.offset(x, y, z);
        templatePosition = templatePosition.add(x, y, z);
    }
}
