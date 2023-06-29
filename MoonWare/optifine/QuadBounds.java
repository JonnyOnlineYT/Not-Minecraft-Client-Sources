package optifine;

import net.minecraft.util.EnumFacing;

public class QuadBounds
{
    private float minX = Float.MAX_VALUE;
    private float minY = Float.MAX_VALUE;
    private float minZ = Float.MAX_VALUE;
    private float maxX = -3.4028235E38F;
    private float maxY = -3.4028235E38F;
    private float maxZ = -3.4028235E38F;

    public QuadBounds(int[] p_i76_1_)
    {
        int i = p_i76_1_.length / 4;

        for (int j = 0; j < 4; ++j)
        {
            int k = j * i;
            float f = Float.intBitsToFloat(p_i76_1_[k + 0]);
            float f1 = Float.intBitsToFloat(p_i76_1_[k + 1]);
            float f2 = Float.intBitsToFloat(p_i76_1_[k + 2]);

            if (minX > f)
            {
                minX = f;
            }

            if (minY > f1)
            {
                minY = f1;
            }

            if (minZ > f2)
            {
                minZ = f2;
            }

            if (maxX < f)
            {
                maxX = f;
            }

            if (maxY < f1)
            {
                maxY = f1;
            }

            if (maxZ < f2)
            {
                maxZ = f2;
            }
        }
    }

    public float getMinX()
    {
        return minX;
    }

    public float getMinY()
    {
        return minY;
    }

    public float getMinZ()
    {
        return minZ;
    }

    public float getMaxX()
    {
        return maxX;
    }

    public float getMaxY()
    {
        return maxY;
    }

    public float getMaxZ()
    {
        return maxZ;
    }

    public boolean isFaceQuad(EnumFacing p_isFaceQuad_1_)
    {
        float f;
        float f1;
        float f2;

        switch (p_isFaceQuad_1_)
        {
            case DOWN:
                f = getMinY();
                f1 = getMaxY();
                f2 = 0.0F;
                break;

            case UP:
                f = getMinY();
                f1 = getMaxY();
                f2 = 1.0F;
                break;

            case NORTH:
                f = getMinZ();
                f1 = getMaxZ();
                f2 = 0.0F;
                break;

            case SOUTH:
                f = getMinZ();
                f1 = getMaxZ();
                f2 = 1.0F;
                break;

            case WEST:
                f = getMinX();
                f1 = getMaxX();
                f2 = 0.0F;
                break;

            case EAST:
                f = getMinX();
                f1 = getMaxX();
                f2 = 1.0F;
                break;

            default:
                return false;
        }

        return f == f2 && f1 == f2;
    }

    public boolean isFullQuad(EnumFacing p_isFullQuad_1_)
    {
        float f;
        float f1;
        float f2;
        float f3;

        switch (p_isFullQuad_1_)
        {
            case DOWN:
            case UP:
                f = getMinX();
                f1 = getMaxX();
                f2 = getMinZ();
                f3 = getMaxZ();
                break;

            case NORTH:
            case SOUTH:
                f = getMinX();
                f1 = getMaxX();
                f2 = getMinY();
                f3 = getMaxY();
                break;

            case WEST:
            case EAST:
                f = getMinY();
                f1 = getMaxY();
                f2 = getMinZ();
                f3 = getMaxZ();
                break;

            default:
                return false;
        }

        return f == 0.0F && f1 == 1.0F && f2 == 0.0F && f3 == 1.0F;
    }
}
