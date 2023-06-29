package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class StructureStart
{
    protected List<StructureComponent> components = Lists.newLinkedList();
    protected StructureBoundingBox boundingBox;
    private int chunkPosX;
    private int chunkPosZ;

    public StructureStart()
    {
    }

    public StructureStart(int chunkX, int chunkZ)
    {
        chunkPosX = chunkX;
        chunkPosZ = chunkZ;
    }

    public StructureBoundingBox getBoundingBox()
    {
        return boundingBox;
    }

    public List<StructureComponent> getComponents()
    {
        return components;
    }

    /**
     * Keeps iterating Structure Pieces and spawning them until the checks tell it to stop
     */
    public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
    {
        Iterator<StructureComponent> iterator = components.iterator();

        while (iterator.hasNext())
        {
            StructureComponent structurecomponent = iterator.next();

            if (structurecomponent.getBoundingBox().intersectsWith(structurebb) && !structurecomponent.addComponentParts(worldIn, rand, structurebb))
            {
                iterator.remove();
            }
        }
    }

    /**
     * Calculates total bounding box based on components' bounding boxes and saves it to boundingBox
     */
    protected void updateBoundingBox()
    {
        boundingBox = StructureBoundingBox.getNewBoundingBox();

        for (StructureComponent structurecomponent : components)
        {
            boundingBox.expandTo(structurecomponent.getBoundingBox());
        }
    }

    public NBTTagCompound writeStructureComponentsToNBT(int chunkX, int chunkZ)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("id", MapGenStructureIO.getStructureStartName(this));
        nbttagcompound.setInteger("ChunkX", chunkX);
        nbttagcompound.setInteger("ChunkZ", chunkZ);
        nbttagcompound.setTag("BB", boundingBox.toNBTTagIntArray());
        NBTTagList nbttaglist = new NBTTagList();

        for (StructureComponent structurecomponent : components)
        {
            nbttaglist.appendTag(structurecomponent.createStructureBaseNBT());
        }

        nbttagcompound.setTag("Children", nbttaglist);
        writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
    }

    public void readStructureComponentsFromNBT(World worldIn, NBTTagCompound tagCompound)
    {
        chunkPosX = tagCompound.getInteger("ChunkX");
        chunkPosZ = tagCompound.getInteger("ChunkZ");

        if (tagCompound.hasKey("BB"))
        {
            boundingBox = new StructureBoundingBox(tagCompound.getIntArray("BB"));
        }

        NBTTagList nbttaglist = tagCompound.getTagList("Children", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            components.add(MapGenStructureIO.getStructureComponent(nbttaglist.getCompoundTagAt(i), worldIn));
        }

        readFromNBT(tagCompound);
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
    }

    /**
     * offsets the structure Bounding Boxes up to a certain height, typically 63 - 10
     */
    protected void markAvailableHeight(World worldIn, Random rand, int p_75067_3_)
    {
        int i = worldIn.getSeaLevel() - p_75067_3_;
        int j = boundingBox.getYSize() + 1;

        if (j < i)
        {
            j += rand.nextInt(i - j);
        }

        int k = j - boundingBox.maxY;
        boundingBox.offset(0, k, 0);

        for (StructureComponent structurecomponent : components)
        {
            structurecomponent.offset(0, k, 0);
        }
    }

    protected void setRandomHeight(World worldIn, Random rand, int p_75070_3_, int p_75070_4_)
    {
        int i = p_75070_4_ - p_75070_3_ + 1 - boundingBox.getYSize();
        int j;

        if (i > 1)
        {
            j = p_75070_3_ + rand.nextInt(i);
        }
        else
        {
            j = p_75070_3_;
        }

        int k = j - boundingBox.minY;
        boundingBox.offset(0, k, 0);

        for (StructureComponent structurecomponent : components)
        {
            structurecomponent.offset(0, k, 0);
        }
    }

    /**
     * currently only defined for Villages, returns true if Village has more than 2 non-road components
     */
    public boolean isSizeableStructure()
    {
        return true;
    }

    public boolean isValidForPostProcess(ChunkPos pair)
    {
        return true;
    }

    public void notifyPostProcessAt(ChunkPos pair)
    {
    }

    public int getChunkPosX()
    {
        return chunkPosX;
    }

    public int getChunkPosZ()
    {
        return chunkPosZ;
    }
}
