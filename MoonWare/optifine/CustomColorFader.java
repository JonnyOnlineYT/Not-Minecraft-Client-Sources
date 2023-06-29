package optifine;

import net.minecraft.util.math.Vec3d;

public class CustomColorFader
{
    private Vec3d color;
    private long timeUpdate = System.currentTimeMillis();

    public Vec3d getColor(double p_getColor_1_, double p_getColor_3_, double p_getColor_5_)
    {
        if (color == null)
        {
            color = new Vec3d(p_getColor_1_, p_getColor_3_, p_getColor_5_);
            return color;
        }
        else
        {
            long i = System.currentTimeMillis();
            long j = i - timeUpdate;

            if (j == 0L)
            {
                return color;
            }
            else
            {
                timeUpdate = i;

                if (Math.abs(p_getColor_1_ - color.xCoord) < 0.004D && Math.abs(p_getColor_3_ - color.yCoord) < 0.004D && Math.abs(p_getColor_5_ - color.zCoord) < 0.004D)
                {
                    return color;
                }
                else
                {
                    double d0 = (double)j * 0.001D;
                    d0 = Config.limit(d0, 0.0D, 1.0D);
                    double d1 = p_getColor_1_ - color.xCoord;
                    double d2 = p_getColor_3_ - color.yCoord;
                    double d3 = p_getColor_5_ - color.zCoord;
                    double d4 = color.xCoord + d1 * d0;
                    double d5 = color.yCoord + d2 * d0;
                    double d6 = color.zCoord + d3 * d0;
                    color = new Vec3d(d4, d5, d6);
                    return color;
                }
            }
        }
    }
}
