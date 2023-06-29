package net.minecraft.world.chunk.storage;

public class NibbleArrayReader
{
    public final byte[] data;
    private final int depthBits;
    private final int depthBitsPlusFour;

    public NibbleArrayReader(byte[] dataIn, int depthBitsIn)
    {
        data = dataIn;
        depthBits = depthBitsIn;
        depthBitsPlusFour = depthBitsIn + 4;
    }

    public int get(int x, int y, int z)
    {
        int i = x << depthBitsPlusFour | z << depthBits | y;
        int j = i >> 1;
        int k = i & 1;
        return k == 0 ? data[j] & 15 : data[j] >> 4 & 15;
    }
}
