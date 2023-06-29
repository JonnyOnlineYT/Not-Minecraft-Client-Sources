package net.minecraft.block.state.pattern;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPattern
{
    private final Predicate<BlockWorldState>[][][] blockMatches;
    private final int fingerLength;
    private final int thumbLength;
    private final int palmLength;

    public BlockPattern(Predicate<BlockWorldState>[][][] predicatesIn)
    {
        blockMatches = predicatesIn;
        fingerLength = predicatesIn.length;

        if (fingerLength > 0)
        {
            thumbLength = predicatesIn[0].length;

            if (thumbLength > 0)
            {
                palmLength = predicatesIn[0][0].length;
            }
            else
            {
                palmLength = 0;
            }
        }
        else
        {
            thumbLength = 0;
            palmLength = 0;
        }
    }

    public int getFingerLength()
    {
        return fingerLength;
    }

    public int getThumbLength()
    {
        return thumbLength;
    }

    public int getPalmLength()
    {
        return palmLength;
    }

    @Nullable

    /**
     * checks that the given pattern & rotation is at the block co-ordinates.
     */
    private BlockPattern.PatternHelper checkPatternAt(BlockPos pos, EnumFacing finger, EnumFacing thumb, LoadingCache<BlockPos, BlockWorldState> lcache)
    {
        for (int i = 0; i < palmLength; ++i)
        {
            for (int j = 0; j < thumbLength; ++j)
            {
                for (int k = 0; k < fingerLength; ++k)
                {
                    if (!blockMatches[k][j][i].apply(lcache.getUnchecked(translateOffset(pos, finger, thumb, i, j, k))))
                    {
                        return null;
                    }
                }
            }
        }

        return new BlockPattern.PatternHelper(pos, finger, thumb, lcache, palmLength, thumbLength, fingerLength);
    }

    @Nullable

    /**
     * Calculates whether the given world position matches the pattern. Warning, fairly heavy function. @return a
     * BlockPattern.PatternHelper if found, null otherwise.
     */
    public BlockPattern.PatternHelper match(World worldIn, BlockPos pos)
    {
        LoadingCache<BlockPos, BlockWorldState> loadingcache = createLoadingCache(worldIn, false);
        int i = Math.max(Math.max(palmLength, thumbLength), fingerLength);

        for (BlockPos blockpos : BlockPos.getAllInBox(pos, pos.add(i - 1, i - 1, i - 1)))
        {
            for (EnumFacing enumfacing : EnumFacing.values())
            {
                for (EnumFacing enumfacing1 : EnumFacing.values())
                {
                    if (enumfacing1 != enumfacing && enumfacing1 != enumfacing.getOpposite())
                    {
                        BlockPattern.PatternHelper blockpattern$patternhelper = checkPatternAt(blockpos, enumfacing, enumfacing1, loadingcache);

                        if (blockpattern$patternhelper != null)
                        {
                            return blockpattern$patternhelper;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static LoadingCache<BlockPos, BlockWorldState> createLoadingCache(World worldIn, boolean forceLoadIn)
    {
        return CacheBuilder.newBuilder().build(new BlockPattern.CacheLoader(worldIn, forceLoadIn));
    }

    /**
     * Offsets the position of pos in the direction of finger and thumb facing by offset amounts, follows the right-hand
     * rule for cross products (finger, thumb, palm) @return A new BlockPos offset in the facing directions
     */
    protected static BlockPos translateOffset(BlockPos pos, EnumFacing finger, EnumFacing thumb, int palmOffset, int thumbOffset, int fingerOffset)
    {
        if (finger != thumb && finger != thumb.getOpposite())
        {
            Vec3i vec3i = new Vec3i(finger.getFrontOffsetX(), finger.getFrontOffsetY(), finger.getFrontOffsetZ());
            Vec3i vec3i1 = new Vec3i(thumb.getFrontOffsetX(), thumb.getFrontOffsetY(), thumb.getFrontOffsetZ());
            Vec3i vec3i2 = vec3i.crossProduct(vec3i1);
            return pos.add(vec3i1.getX() * -thumbOffset + vec3i2.getX() * palmOffset + vec3i.getX() * fingerOffset, vec3i1.getY() * -thumbOffset + vec3i2.getY() * palmOffset + vec3i.getY() * fingerOffset, vec3i1.getZ() * -thumbOffset + vec3i2.getZ() * palmOffset + vec3i.getZ() * fingerOffset);
        }
        else
        {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }
    }

    static class CacheLoader extends com.google.common.cache.CacheLoader<BlockPos, BlockWorldState>
    {
        private final World world;
        private final boolean forceLoad;

        public CacheLoader(World worldIn, boolean forceLoadIn)
        {
            world = worldIn;
            forceLoad = forceLoadIn;
        }

        public BlockWorldState load(BlockPos p_load_1_) throws Exception
        {
            return new BlockWorldState(world, p_load_1_, forceLoad);
        }
    }

    public static class PatternHelper
    {
        private final BlockPos frontTopLeft;
        private final EnumFacing forwards;
        private final EnumFacing up;
        private final LoadingCache<BlockPos, BlockWorldState> lcache;
        private final int width;
        private final int height;
        private final int depth;

        public PatternHelper(BlockPos posIn, EnumFacing fingerIn, EnumFacing thumbIn, LoadingCache<BlockPos, BlockWorldState> lcacheIn, int p_i46378_5_, int p_i46378_6_, int p_i46378_7_)
        {
            frontTopLeft = posIn;
            forwards = fingerIn;
            up = thumbIn;
            lcache = lcacheIn;
            width = p_i46378_5_;
            height = p_i46378_6_;
            depth = p_i46378_7_;
        }

        public BlockPos getFrontTopLeft()
        {
            return frontTopLeft;
        }

        public EnumFacing getForwards()
        {
            return forwards;
        }

        public EnumFacing getUp()
        {
            return up;
        }

        public int getWidth()
        {
            return width;
        }

        public int getHeight()
        {
            return height;
        }

        public BlockWorldState translateOffset(int palmOffset, int thumbOffset, int fingerOffset)
        {
            return lcache.getUnchecked(BlockPattern.translateOffset(frontTopLeft, getForwards(), getUp(), palmOffset, thumbOffset, fingerOffset));
        }

        public String toString()
        {
            return MoreObjects.toStringHelper(this).add("up", up).add("forwards", forwards).add("frontTopLeft", frontTopLeft).toString();
        }
    }
}
