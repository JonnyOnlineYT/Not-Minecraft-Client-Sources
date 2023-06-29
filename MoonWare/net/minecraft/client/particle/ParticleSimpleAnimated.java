package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSimpleAnimated extends Particle
{
    /**
     * The base texture index. The texture index starts at this + (numAgingFrames - 1), and works its way down to this
     * number as the particle decays.
     */
    private final int textureIdx;

    /**
     * How many different textures there are to progress through as the particle decays
     */
    private final int numAgingFrames;

    /**
     * Added to the ySpeed every tick. Usually a small (thousandths), negative value.
     */
    private final float yAccel;
    private float field_191239_M = 0.91F;

    /** The red value to drift toward */
    private float fadeTargetRed;

    /** The green value to drift toward */
    private float fadeTargetGreen;

    /** The blue value to drift toward */
    private float fadeTargetBlue;

    /** True if setColorFade has been called */
    private boolean fadingColor;

    public ParticleSimpleAnimated(World worldIn, double x, double y, double z, int textureIdxIn, int numFrames, float yAccelIn)
    {
        super(worldIn, x, y, z);
        textureIdx = textureIdxIn;
        numAgingFrames = numFrames;
        yAccel = yAccelIn;
    }

    public void setColor(int p_187146_1_)
    {
        float f = (float)((p_187146_1_ & 16711680) >> 16) / 255.0F;
        float f1 = (float)((p_187146_1_ & 65280) >> 8) / 255.0F;
        float f2 = (float)((p_187146_1_ & 255) >> 0) / 255.0F;
        float f3 = 1.0F;
        setRBGColorF(f * 1.0F, f1 * 1.0F, f2 * 1.0F);
    }

    /**
     * sets a color for the particle to drift toward (20% closer each tick, never actually getting very close)
     */
    public void setColorFade(int rgb)
    {
        fadeTargetRed = (float)((rgb & 16711680) >> 16) / 255.0F;
        fadeTargetGreen = (float)((rgb & 65280) >> 8) / 255.0F;
        fadeTargetBlue = (float)((rgb & 255) >> 0) / 255.0F;
        fadingColor = true;
    }

    public boolean isTransparent()
    {
        return true;
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (particleAge++ >= particleMaxAge)
        {
            setExpired();
        }

        if (particleAge > particleMaxAge / 2)
        {
            setAlphaF(1.0F - ((float) particleAge - (float)(particleMaxAge / 2)) / (float) particleMaxAge);

            if (fadingColor)
            {
                particleRed += (fadeTargetRed - particleRed) * 0.2F;
                particleGreen += (fadeTargetGreen - particleGreen) * 0.2F;
                particleBlue += (fadeTargetBlue - particleBlue) * 0.2F;
            }
        }

        setParticleTextureIndex(textureIdx + (numAgingFrames - 1 - particleAge * numAgingFrames / particleMaxAge));
        motionY += yAccel;
        moveEntity(motionX, motionY, motionZ);
        motionX *= field_191239_M;
        motionY *= field_191239_M;
        motionZ *= field_191239_M;

        if (isCollided)
        {
            motionX *= 0.699999988079071D;
            motionZ *= 0.699999988079071D;
        }
    }

    public int getBrightnessForRender(float p_189214_1_)
    {
        return 15728880;
    }

    protected void func_191238_f(float p_191238_1_)
    {
        field_191239_M = p_191238_1_;
    }
}
