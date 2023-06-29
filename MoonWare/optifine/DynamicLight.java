package optifine;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DynamicLight
{
    private Entity entity;
    private double offsetY;
    private double lastPosX = -2.147483648E9D;
    private double lastPosY = -2.147483648E9D;
    private double lastPosZ = -2.147483648E9D;
    private int lastLightLevel;
    private boolean underwater;
    private long timeCheckMs;
    private Set<BlockPos> setLitChunkPos = new HashSet<BlockPos>();
    private BlockPos.MutableBlockPos blockPosMutable = new BlockPos.MutableBlockPos();

    public DynamicLight(Entity p_i36_1_)
    {
        entity = p_i36_1_;
        offsetY = p_i36_1_.getEyeHeight();
    }

    public void update(RenderGlobal p_update_1_)
    {
        if (Config.isDynamicLightsFast())
        {
            long i = System.currentTimeMillis();

            if (i < timeCheckMs + 500L)
            {
                return;
            }

            timeCheckMs = i;
        }

        double d6 = entity.posX - 0.5D;
        double d0 = entity.posY - 0.5D + offsetY;
        double d1 = entity.posZ - 0.5D;
        int j = DynamicLights.getLightLevel(entity);
        double d2 = d6 - lastPosX;
        double d3 = d0 - lastPosY;
        double d4 = d1 - lastPosZ;
        double d5 = 0.1D;

        if (Math.abs(d2) > d5 || Math.abs(d3) > d5 || Math.abs(d4) > d5 || lastLightLevel != j)
        {
            lastPosX = d6;
            lastPosY = d0;
            lastPosZ = d1;
            lastLightLevel = j;
            underwater = false;
            World world = p_update_1_.getWorld();

            if (world != null)
            {
                blockPosMutable.setPos(MathHelper.floor(d6), MathHelper.floor(d0), MathHelper.floor(d1));
                IBlockState iblockstate = world.getBlockState(blockPosMutable);
                Block block = iblockstate.getBlock();
                underwater = block == Blocks.WATER;
            }

            Set<BlockPos> set = new HashSet<BlockPos>();

            if (j > 0)
            {
                EnumFacing enumfacing2 = (MathHelper.floor(d6) & 15) >= 8 ? EnumFacing.EAST : EnumFacing.WEST;
                EnumFacing enumfacing = (MathHelper.floor(d0) & 15) >= 8 ? EnumFacing.UP : EnumFacing.DOWN;
                EnumFacing enumfacing1 = (MathHelper.floor(d1) & 15) >= 8 ? EnumFacing.SOUTH : EnumFacing.NORTH;
                BlockPos blockpos = new BlockPos(d6, d0, d1);
                RenderChunk renderchunk = p_update_1_.getRenderChunk(blockpos);
                BlockPos blockpos1 = getChunkPos(renderchunk, blockpos, enumfacing2);
                RenderChunk renderchunk1 = p_update_1_.getRenderChunk(blockpos1);
                BlockPos blockpos2 = getChunkPos(renderchunk, blockpos, enumfacing1);
                RenderChunk renderchunk2 = p_update_1_.getRenderChunk(blockpos2);
                BlockPos blockpos3 = getChunkPos(renderchunk1, blockpos1, enumfacing1);
                RenderChunk renderchunk3 = p_update_1_.getRenderChunk(blockpos3);
                BlockPos blockpos4 = getChunkPos(renderchunk, blockpos, enumfacing);
                RenderChunk renderchunk4 = p_update_1_.getRenderChunk(blockpos4);
                BlockPos blockpos5 = getChunkPos(renderchunk4, blockpos4, enumfacing2);
                RenderChunk renderchunk5 = p_update_1_.getRenderChunk(blockpos5);
                BlockPos blockpos6 = getChunkPos(renderchunk4, blockpos4, enumfacing1);
                RenderChunk renderchunk6 = p_update_1_.getRenderChunk(blockpos6);
                BlockPos blockpos7 = getChunkPos(renderchunk5, blockpos5, enumfacing1);
                RenderChunk renderchunk7 = p_update_1_.getRenderChunk(blockpos7);
                updateChunkLight(renderchunk, setLitChunkPos, set);
                updateChunkLight(renderchunk1, setLitChunkPos, set);
                updateChunkLight(renderchunk2, setLitChunkPos, set);
                updateChunkLight(renderchunk3, setLitChunkPos, set);
                updateChunkLight(renderchunk4, setLitChunkPos, set);
                updateChunkLight(renderchunk5, setLitChunkPos, set);
                updateChunkLight(renderchunk6, setLitChunkPos, set);
                updateChunkLight(renderchunk7, setLitChunkPos, set);
            }

            updateLitChunks(p_update_1_);
            setLitChunkPos = set;
        }
    }

    private BlockPos getChunkPos(RenderChunk p_getChunkPos_1_, BlockPos p_getChunkPos_2_, EnumFacing p_getChunkPos_3_)
    {
        return p_getChunkPos_1_ != null ? p_getChunkPos_1_.getBlockPosOffset16(p_getChunkPos_3_) : p_getChunkPos_2_.offset(p_getChunkPos_3_, 16);
    }

    private void updateChunkLight(RenderChunk p_updateChunkLight_1_, Set<BlockPos> p_updateChunkLight_2_, Set<BlockPos> p_updateChunkLight_3_)
    {
        if (p_updateChunkLight_1_ != null)
        {
            CompiledChunk compiledchunk = p_updateChunkLight_1_.getCompiledChunk();

            if (compiledchunk != null && !compiledchunk.isEmpty())
            {
                p_updateChunkLight_1_.setNeedsUpdate(false);
            }

            BlockPos blockpos = p_updateChunkLight_1_.getPosition().toImmutable();

            if (p_updateChunkLight_2_ != null)
            {
                p_updateChunkLight_2_.remove(blockpos);
            }

            if (p_updateChunkLight_3_ != null)
            {
                p_updateChunkLight_3_.add(blockpos);
            }
        }
    }

    public void updateLitChunks(RenderGlobal p_updateLitChunks_1_)
    {
        for (BlockPos blockpos : setLitChunkPos)
        {
            RenderChunk renderchunk = p_updateLitChunks_1_.getRenderChunk(blockpos);
            updateChunkLight(renderchunk, null, null);
        }
    }

    public Entity getEntity()
    {
        return entity;
    }

    public double getLastPosX()
    {
        return lastPosX;
    }

    public double getLastPosY()
    {
        return lastPosY;
    }

    public double getLastPosZ()
    {
        return lastPosZ;
    }

    public int getLastLightLevel()
    {
        return lastLightLevel;
    }

    public boolean isUnderwater()
    {
        return underwater;
    }

    public double getOffsetY()
    {
        return offsetY;
    }

    public String toString()
    {
        return "Entity: " + entity + ", offsetY: " + offsetY;
    }
}
