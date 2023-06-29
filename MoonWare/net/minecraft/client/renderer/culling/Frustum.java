package net.minecraft.client.renderer.culling;

import net.minecraft.util.math.AxisAlignedBB;

public class Frustum implements ICamera
{
    private final ClippingHelper clippingHelper;
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public Frustum()
    {
        this(ClippingHelperImpl.getInstance());
    }

    public Frustum(ClippingHelper p_i46196_1_)
    {
        clippingHelper = p_i46196_1_;
    }

    public void setPosition(double p_78547_1_, double p_78547_3_, double p_78547_5_)
    {
        xPosition = p_78547_1_;
        yPosition = p_78547_3_;
        zPosition = p_78547_5_;
    }

    /**
     * Calls the clipping helper. Returns true if the box is inside all 6 clipping planes, otherwise returns false.
     */
    public boolean isBoxInFrustum(double p_78548_1_, double p_78548_3_, double p_78548_5_, double p_78548_7_, double p_78548_9_, double p_78548_11_)
    {
        return clippingHelper.isBoxInFrustum(p_78548_1_ - xPosition, p_78548_3_ - yPosition, p_78548_5_ - zPosition, p_78548_7_ - xPosition, p_78548_9_ - yPosition, p_78548_11_ - zPosition);
    }

    /**
     * Returns true if the bounding box is inside all 6 clipping planes, otherwise returns false.
     */
    public boolean isBoundingBoxInFrustum(AxisAlignedBB p_78546_1_)
    {
        return isBoxInFrustum(p_78546_1_.minX, p_78546_1_.minY, p_78546_1_.minZ, p_78546_1_.maxX, p_78546_1_.maxY, p_78546_1_.maxZ);
    }
}
