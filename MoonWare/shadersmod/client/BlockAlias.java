package shadersmod.client;

import optifine.MatchBlock;

public class BlockAlias
{
    private int blockId;
    private MatchBlock[] matchBlocks;

    public BlockAlias(int blockId, MatchBlock[] matchBlocks)
    {
        this.blockId = blockId;
        this.matchBlocks = matchBlocks;
    }

    public int getBlockId()
    {
        return blockId;
    }

    public boolean matches(int id, int metadata)
    {
        for (int i = 0; i < matchBlocks.length; ++i)
        {
            MatchBlock matchblock = matchBlocks[i];

            if (matchblock.matches(id, metadata))
            {
                return true;
            }
        }

        return false;
    }

    public int[] getMatchBlockIds()
    {
        int[] aint = new int[matchBlocks.length];

        for (int i = 0; i < aint.length; ++i)
        {
            aint[i] = matchBlocks[i].getBlockId();
        }

        return aint;
    }
}
