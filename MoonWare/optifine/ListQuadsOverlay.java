package optifine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.init.Blocks;

public class ListQuadsOverlay
{
    private List<BakedQuad> listQuads = new ArrayList<BakedQuad>();
    private List<IBlockState> listBlockStates = new ArrayList<IBlockState>();
    private List<BakedQuad> listQuadsSingle = Arrays.asList();

    public void addQuad(BakedQuad p_addQuad_1_, IBlockState p_addQuad_2_)
    {
        listQuads.add(p_addQuad_1_);
        listBlockStates.add(p_addQuad_2_);
    }

    public int size()
    {
        return listQuads.size();
    }

    public BakedQuad getQuad(int p_getQuad_1_)
    {
        return listQuads.get(p_getQuad_1_);
    }

    public IBlockState getBlockState(int p_getBlockState_1_)
    {
        return p_getBlockState_1_ >= 0 && p_getBlockState_1_ < listBlockStates.size() ? listBlockStates.get(p_getBlockState_1_) : Blocks.AIR.getDefaultState();
    }

    public List<BakedQuad> getListQuadsSingle(BakedQuad p_getListQuadsSingle_1_)
    {
        listQuadsSingle.set(0, p_getListQuadsSingle_1_);
        return listQuadsSingle;
    }

    public void clear()
    {
        listQuads.clear();
        listBlockStates.clear();
    }
}
