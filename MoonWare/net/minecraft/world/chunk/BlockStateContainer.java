package net.minecraft.world.chunk;

import javax.annotation.Nullable;

import baritone.utils.accessor.IBitArray;
import baritone.utils.accessor.IBlockStateContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BitArray;
import net.minecraft.util.math.MathHelper;

public class BlockStateContainer implements IBlockStatePaletteResizer, IBlockStateContainer
{
    private static final IBlockStatePalette REGISTRY_BASED_PALETTE = new BlockStatePaletteRegistry();
    protected static final IBlockState AIR_BLOCK_STATE = Blocks.AIR.getDefaultState();
    protected BitArray storage;
    protected IBlockStatePalette palette;
    private int bits;

    public BlockStateContainer()
    {
        setBits(4);
    }

    private static int getIndex(int x, int y, int z)
    {
        return y << 8 | z << 4 | x;
    }

    private void setBits(int bitsIn)
    {
        if (bitsIn != bits)
        {
            bits = bitsIn;

            if (bits <= 4)
            {
                bits = 4;
                palette = new BlockStatePaletteLinear(bits, this);
            }
            else if (bits <= 8)
            {
                palette = new BlockStatePaletteHashMap(bits, this);
            }
            else
            {
                palette = REGISTRY_BASED_PALETTE;
                bits = MathHelper.log2DeBruijn(Block.BLOCK_STATE_IDS.size());
            }

            palette.idFor(AIR_BLOCK_STATE);
            storage = new BitArray(bits, 4096);
        }
    }

    public int onResize(int p_186008_1_, IBlockState state)
    {
        BitArray bitarray = storage;
        IBlockStatePalette iblockstatepalette = palette;
        setBits(p_186008_1_);

        for (int i = 0; i < bitarray.size(); ++i)
        {
            IBlockState iblockstate = iblockstatepalette.getBlockState(bitarray.getAt(i));

            if (iblockstate != null)
            {
                set(i, iblockstate);
            }
        }

        return palette.idFor(state);
    }

    public void set(int x, int y, int z, IBlockState state)
    {
        set(getIndex(x, y, z), state);
    }

    protected void set(int index, IBlockState state)
    {
        int i = palette.idFor(state);
        storage.setAt(index, i);
    }

    public IBlockState get(int x, int y, int z)
    {
        return get(getIndex(x, y, z));
    }

    protected IBlockState get(int index)
    {
        IBlockState iblockstate = palette.getBlockState(storage.getAt(index));
        return iblockstate == null ? AIR_BLOCK_STATE : iblockstate;
    }

    public void read(PacketBuffer buf)
    {
        int i = buf.readByte();

        if (bits != i)
        {
            setBits(i);
        }

        palette.read(buf);
        buf.readLongArray(storage.getBackingLongArray());
    }

    public void write(PacketBuffer buf)
    {
        buf.writeByte(bits);
        palette.write(buf);
        buf.writeLongArray(storage.getBackingLongArray());
    }

    @Nullable
    public NibbleArray getDataForNBT(byte[] p_186017_1_, NibbleArray p_186017_2_)
    {
        NibbleArray nibblearray = null;

        for (int i = 0; i < 4096; ++i)
        {
            int j = Block.BLOCK_STATE_IDS.get(get(i));
            int k = i & 15;
            int l = i >> 8 & 15;
            int i1 = i >> 4 & 15;

            if ((j >> 12 & 15) != 0)
            {
                if (nibblearray == null)
                {
                    nibblearray = new NibbleArray();
                }

                nibblearray.set(k, l, i1, j >> 12 & 15);
            }

            p_186017_1_[i] = (byte)(j >> 4 & 255);
            p_186017_2_.set(k, l, i1, j & 15);
        }

        return nibblearray;
    }

    public void setDataFromNBT(byte[] p_186019_1_, NibbleArray p_186019_2_, @Nullable NibbleArray p_186019_3_)
    {
        for (int i = 0; i < 4096; ++i)
        {
            int j = i & 15;
            int k = i >> 8 & 15;
            int l = i >> 4 & 15;
            int i1 = p_186019_3_ == null ? 0 : p_186019_3_.get(j, k, l);
            int j1 = i1 << 12 | (p_186019_1_[i] & 255) << 4 | p_186019_2_.get(j, k, l);
            set(i, Block.BLOCK_STATE_IDS.getByValue(j1));
        }
    }

    public int getSerializedSize()
    {
        return 1 + palette.getSerializedState() + PacketBuffer.getVarIntSize(storage.size()) + storage.getBackingLongArray().length * 8;
    }

    @Override
    public IBlockState getAtPalette(int index) {
        return palette.getBlockState(index);
    }

    @Override
    public int[] storageArray() {
        return ((IBitArray)storage).toArray();
    }
}
