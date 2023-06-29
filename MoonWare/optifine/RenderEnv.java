package optifine;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RenderEnv
{
    private IBlockAccess blockAccess;
    private IBlockState blockState;
    private BlockPos blockPos;
    private int blockId = -1;
    private int metadata = -1;
    private int breakingAnimation = -1;
    private int smartLeaves = -1;
    private float[] quadBounds = new float[EnumFacing.VALUES.length * 2];
    private BitSet boundsFlags = new BitSet(3);
    private BlockModelRenderer.AmbientOcclusionFace aoFace = new BlockModelRenderer.AmbientOcclusionFace();
    private BlockPosM colorizerBlockPosM;
    private boolean[] borderFlags;
    private boolean[] borderFlags2;
    private boolean[] borderFlags3;
    private EnumFacing[] borderDirections;
    private List<BakedQuad> listQuadsCustomizer = new ArrayList<BakedQuad>();
    private List<BakedQuad> listQuadsCtmMultipass = new ArrayList<BakedQuad>();
    private BakedQuad[] arrayQuadsCtm1 = new BakedQuad[1];
    private BakedQuad[] arrayQuadsCtm2 = new BakedQuad[2];
    private BakedQuad[] arrayQuadsCtm3 = new BakedQuad[3];
    private BakedQuad[] arrayQuadsCtm4 = new BakedQuad[4];
    private RegionRenderCacheBuilder regionRenderCacheBuilder;
    private ListQuadsOverlay[] listsQuadsOverlay = new ListQuadsOverlay[BlockRenderLayer.values().length];
    private boolean overlaysRendered;
    private static final int UNKNOWN = -1;
    private static final int FALSE = 0;
    private static final int TRUE = 1;

    public RenderEnv(IBlockAccess p_i96_1_, IBlockState p_i96_2_, BlockPos p_i96_3_)
    {
        blockAccess = p_i96_1_;
        blockState = p_i96_2_;
        blockPos = p_i96_3_;
    }

    public void reset(IBlockAccess p_reset_1_, IBlockState p_reset_2_, BlockPos p_reset_3_)
    {
        if (blockAccess != p_reset_1_ || blockState != p_reset_2_ || blockPos != p_reset_3_)
        {
            blockAccess = p_reset_1_;
            blockState = p_reset_2_;
            blockPos = p_reset_3_;
            blockId = -1;
            metadata = -1;
            breakingAnimation = -1;
            smartLeaves = -1;
            boundsFlags.clear();
        }
    }

    public int getBlockId()
    {
        if (blockId < 0)
        {
            if (blockState instanceof BlockStateBase)
            {
                BlockStateBase blockstatebase = (BlockStateBase) blockState;
                blockId = blockstatebase.getBlockId();
            }
            else
            {
                blockId = Block.getIdFromBlock(blockState.getBlock());
            }
        }

        return blockId;
    }

    public int getMetadata()
    {
        if (metadata < 0)
        {
            if (blockState instanceof BlockStateBase)
            {
                BlockStateBase blockstatebase = (BlockStateBase) blockState;
                metadata = blockstatebase.getMetadata();
            }
            else
            {
                metadata = blockState.getBlock().getMetaFromState(blockState);
            }
        }

        return metadata;
    }

    public float[] getQuadBounds()
    {
        return quadBounds;
    }

    public BitSet getBoundsFlags()
    {
        return boundsFlags;
    }

    public BlockModelRenderer.AmbientOcclusionFace getAoFace()
    {
        return aoFace;
    }

    public boolean isBreakingAnimation(List p_isBreakingAnimation_1_)
    {
        if (breakingAnimation == -1 && p_isBreakingAnimation_1_.size() > 0)
        {
            if (p_isBreakingAnimation_1_.get(0) instanceof BakedQuadRetextured)
            {
                breakingAnimation = 1;
            }
            else
            {
                breakingAnimation = 0;
            }
        }

        return breakingAnimation == 1;
    }

    public boolean isBreakingAnimation(BakedQuad p_isBreakingAnimation_1_)
    {
        if (breakingAnimation < 0)
        {
            if (p_isBreakingAnimation_1_ instanceof BakedQuadRetextured)
            {
                breakingAnimation = 1;
            }
            else
            {
                breakingAnimation = 0;
            }
        }

        return breakingAnimation == 1;
    }

    public boolean isBreakingAnimation()
    {
        return breakingAnimation == 1;
    }

    public IBlockState getBlockState()
    {
        return blockState;
    }

    public BlockPosM getColorizerBlockPosM()
    {
        if (colorizerBlockPosM == null)
        {
            colorizerBlockPosM = new BlockPosM(0, 0, 0);
        }

        return colorizerBlockPosM;
    }

    public boolean[] getBorderFlags()
    {
        if (borderFlags == null)
        {
            borderFlags = new boolean[4];
        }

        return borderFlags;
    }

    public boolean[] getBorderFlags2()
    {
        if (borderFlags2 == null)
        {
            borderFlags2 = new boolean[4];
        }

        return borderFlags2;
    }

    public boolean[] getBorderFlags3()
    {
        if (borderFlags3 == null)
        {
            borderFlags3 = new boolean[4];
        }

        return borderFlags3;
    }

    public EnumFacing[] getBorderDirections()
    {
        if (borderDirections == null)
        {
            borderDirections = new EnumFacing[4];
        }

        return borderDirections;
    }

    public EnumFacing[] getBorderDirections(EnumFacing p_getBorderDirections_1_, EnumFacing p_getBorderDirections_2_, EnumFacing p_getBorderDirections_3_, EnumFacing p_getBorderDirections_4_)
    {
        EnumFacing[] aenumfacing = getBorderDirections();
        aenumfacing[0] = p_getBorderDirections_1_;
        aenumfacing[1] = p_getBorderDirections_2_;
        aenumfacing[2] = p_getBorderDirections_3_;
        aenumfacing[3] = p_getBorderDirections_4_;
        return aenumfacing;
    }

    public boolean isSmartLeaves()
    {
        if (smartLeaves == -1)
        {
            if (Config.isTreesSmart() && blockState.getBlock() instanceof BlockLeaves)
            {
                smartLeaves = 1;
            }
            else
            {
                smartLeaves = 0;
            }
        }

        return smartLeaves == 1;
    }

    public List<BakedQuad> getListQuadsCustomizer()
    {
        return listQuadsCustomizer;
    }

    public BakedQuad[] getArrayQuadsCtm(BakedQuad p_getArrayQuadsCtm_1_)
    {
        arrayQuadsCtm1[0] = p_getArrayQuadsCtm_1_;
        return arrayQuadsCtm1;
    }

    public BakedQuad[] getArrayQuadsCtm(BakedQuad p_getArrayQuadsCtm_1_, BakedQuad p_getArrayQuadsCtm_2_)
    {
        arrayQuadsCtm2[0] = p_getArrayQuadsCtm_1_;
        arrayQuadsCtm2[1] = p_getArrayQuadsCtm_2_;
        return arrayQuadsCtm2;
    }

    public BakedQuad[] getArrayQuadsCtm(BakedQuad p_getArrayQuadsCtm_1_, BakedQuad p_getArrayQuadsCtm_2_, BakedQuad p_getArrayQuadsCtm_3_)
    {
        arrayQuadsCtm3[0] = p_getArrayQuadsCtm_1_;
        arrayQuadsCtm3[1] = p_getArrayQuadsCtm_2_;
        arrayQuadsCtm3[2] = p_getArrayQuadsCtm_3_;
        return arrayQuadsCtm3;
    }

    public BakedQuad[] getArrayQuadsCtm(BakedQuad p_getArrayQuadsCtm_1_, BakedQuad p_getArrayQuadsCtm_2_, BakedQuad p_getArrayQuadsCtm_3_, BakedQuad p_getArrayQuadsCtm_4_)
    {
        arrayQuadsCtm4[0] = p_getArrayQuadsCtm_1_;
        arrayQuadsCtm4[1] = p_getArrayQuadsCtm_2_;
        arrayQuadsCtm4[2] = p_getArrayQuadsCtm_3_;
        arrayQuadsCtm4[3] = p_getArrayQuadsCtm_4_;
        return arrayQuadsCtm4;
    }

    public List<BakedQuad> getListQuadsCtmMultipass(BakedQuad[] p_getListQuadsCtmMultipass_1_)
    {
        listQuadsCtmMultipass.clear();

        if (p_getListQuadsCtmMultipass_1_ != null)
        {
            for (int i = 0; i < p_getListQuadsCtmMultipass_1_.length; ++i)
            {
                BakedQuad bakedquad = p_getListQuadsCtmMultipass_1_[i];
                listQuadsCtmMultipass.add(bakedquad);
            }
        }

        return listQuadsCtmMultipass;
    }

    public RegionRenderCacheBuilder getRegionRenderCacheBuilder()
    {
        return regionRenderCacheBuilder;
    }

    public void setRegionRenderCacheBuilder(RegionRenderCacheBuilder p_setRegionRenderCacheBuilder_1_)
    {
        regionRenderCacheBuilder = p_setRegionRenderCacheBuilder_1_;
    }

    public ListQuadsOverlay getListQuadsOverlay(BlockRenderLayer p_getListQuadsOverlay_1_)
    {
        ListQuadsOverlay listquadsoverlay = listsQuadsOverlay[p_getListQuadsOverlay_1_.ordinal()];

        if (listquadsoverlay == null)
        {
            listquadsoverlay = new ListQuadsOverlay();
            listsQuadsOverlay[p_getListQuadsOverlay_1_.ordinal()] = listquadsoverlay;
        }

        return listquadsoverlay;
    }

    public boolean isOverlaysRendered()
    {
        return overlaysRendered;
    }

    public void setOverlaysRendered(boolean p_setOverlaysRendered_1_)
    {
        overlaysRendered = p_setOverlaysRendered_1_;
    }
}
