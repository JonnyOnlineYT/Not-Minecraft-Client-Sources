package optifine;

import net.minecraft.block.state.BlockStateBase;

public class MatchBlock
{
    private int blockId = -1;
    private int[] metadatas;

    public MatchBlock(int p_i63_1_)
    {
        blockId = p_i63_1_;
    }

    public MatchBlock(int p_i64_1_, int p_i64_2_)
    {
        blockId = p_i64_1_;

        if (p_i64_2_ >= 0 && p_i64_2_ <= 15)
        {
            metadatas = new int[] {p_i64_2_};
        }
    }

    public MatchBlock(int p_i65_1_, int[] p_i65_2_)
    {
        blockId = p_i65_1_;
        metadatas = p_i65_2_;
    }

    public int getBlockId()
    {
        return blockId;
    }

    public int[] getMetadatas()
    {
        return metadatas;
    }

    public boolean matches(BlockStateBase p_matches_1_)
    {
        if (p_matches_1_.getBlockId() != blockId)
        {
            return false;
        }
        else
        {
            return Matches.metadata(p_matches_1_.getMetadata(), metadatas);
        }
    }

    public boolean matches(int p_matches_1_, int p_matches_2_)
    {
        if (p_matches_1_ != blockId)
        {
            return false;
        }
        else
        {
            return Matches.metadata(p_matches_2_, metadatas);
        }
    }

    public void addMetadata(int p_addMetadata_1_)
    {
        if (metadatas != null)
        {
            if (p_addMetadata_1_ >= 0 && p_addMetadata_1_ <= 15)
            {
                for (int i = 0; i < metadatas.length; ++i)
                {
                    if (metadatas[i] == p_addMetadata_1_)
                    {
                        return;
                    }
                }

                metadatas = Config.addIntToArray(metadatas, p_addMetadata_1_);
            }
        }
    }

    public String toString()
    {
        return "" + blockId + ":" + Config.arrayToString(metadatas);
    }
}
