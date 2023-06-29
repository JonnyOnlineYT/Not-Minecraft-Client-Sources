package net.minecraft.client.particle;

import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Particle
{
    private static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    protected World worldObj;
    protected double prevPosX;
    protected double prevPosY;
    protected double prevPosZ;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected double motionX;
    protected double motionY;
    protected double motionZ;
    private AxisAlignedBB boundingBox;
    protected boolean isCollided;

    /** Determines if particle to block collision is to be used */
    protected boolean canCollide;
    protected boolean isExpired;
    protected float width;
    protected float height;
    protected Random rand;
    protected int particleTextureIndexX;
    protected int particleTextureIndexY;
    protected float particleTextureJitterX;
    protected float particleTextureJitterY;
    protected int particleAge;
    protected int particleMaxAge;
    protected float particleScale;
    protected float particleGravity;

    /** The red amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0. */
    protected float particleRed;

    /**
     * The green amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0.
     */
    protected float particleGreen;

    /**
     * The blue amount of color. Used as a percentage, 1.0 = 255 and 0.0 = 0.
     */
    protected float particleBlue;

    /** Particle alpha */
    protected float particleAlpha;
    protected TextureAtlasSprite particleTexture;

    /** The amount the particle will be rotated in rendering. */
    protected float particleAngle;

    /**
     * The particle angle from the last tick. Appears to be used for calculating the rendered angle with partial ticks.
     */
    protected float prevParticleAngle;
    public static double interpPosX;
    public static double interpPosY;
    public static double interpPosZ;
    public static Vec3d cameraViewDir;

    protected Particle(World worldIn, double posXIn, double posYIn, double posZIn)
    {
        boundingBox = EMPTY_AABB;
        width = 0.6F;
        height = 1.8F;
        rand = new Random();
        particleAlpha = 1.0F;
        worldObj = worldIn;
        setSize(0.2F, 0.2F);
        setPosition(posXIn, posYIn, posZIn);
        prevPosX = posXIn;
        prevPosY = posYIn;
        prevPosZ = posZIn;
        particleRed = 1.0F;
        particleGreen = 1.0F;
        particleBlue = 1.0F;
        particleTextureJitterX = rand.nextFloat() * 3.0F;
        particleTextureJitterY = rand.nextFloat() * 3.0F;
        particleScale = (rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
        particleMaxAge = (int)(4.0F / (rand.nextFloat() * 0.9F + 0.1F));
        particleAge = 0;
        canCollide = true;
    }

    public Particle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn);
        motionX = xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        motionY = ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        motionZ = zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        float f = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
        float f1 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX = motionX / (double)f1 * (double)f * 0.4000000059604645D;
        motionY = motionY / (double)f1 * (double)f * 0.4000000059604645D + 0.10000000149011612D;
        motionZ = motionZ / (double)f1 * (double)f * 0.4000000059604645D;
    }

    public Particle multiplyVelocity(float multiplier)
    {
        motionX *= multiplier;
        motionY = (motionY - 0.10000000149011612D) * (double)multiplier + 0.10000000149011612D;
        motionZ *= multiplier;
        return this;
    }

    public Particle multipleParticleScaleBy(float scale)
    {
        setSize(0.2F * scale, 0.2F * scale);
        particleScale *= scale;
        return this;
    }

    public void setRBGColorF(float particleRedIn, float particleGreenIn, float particleBlueIn)
    {
        particleRed = particleRedIn;
        particleGreen = particleGreenIn;
        particleBlue = particleBlueIn;
    }

    /**
     * Sets the particle alpha (float)
     */
    public void setAlphaF(float alpha)
    {
        particleAlpha = alpha;
    }

    public boolean isTransparent()
    {
        return false;
    }

    public float getRedColorF()
    {
        return particleRed;
    }

    public float getGreenColorF()
    {
        return particleGreen;
    }

    public float getBlueColorF()
    {
        return particleBlue;
    }

    public void setMaxAge(int p_187114_1_)
    {
        particleMaxAge = p_187114_1_;
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

        motionY -= 0.04D * (double) particleGravity;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.9800000190734863D;
        motionY *= 0.9800000190734863D;
        motionZ *= 0.9800000190734863D;

        if (isCollided)
        {
            motionX *= 0.699999988079071D;
            motionZ *= 0.699999988079071D;
        }
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = (float) particleTextureIndexX / 16.0F;
        float f1 = f + 0.0624375F;
        float f2 = (float) particleTextureIndexY / 16.0F;
        float f3 = f2 + 0.0624375F;
        float f4 = 0.1F * particleScale;

        if (particleTexture != null)
        {
            f = particleTexture.getMinU();
            f1 = particleTexture.getMaxU();
            f2 = particleTexture.getMinV();
            f3 = particleTexture.getMaxV();
        }

        float f5 = (float)(prevPosX + (posX - prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(prevPosY + (posY - prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(prevPosZ + (posZ - prevPosZ) * (double)partialTicks - interpPosZ);
        int i = getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = {new Vec3d(-rotationX * f4 - rotationXY * f4, -rotationZ * f4, -rotationYZ * f4 - rotationXZ * f4), new Vec3d(-rotationX * f4 + rotationXY * f4, rotationZ * f4, -rotationYZ * f4 + rotationXZ * f4), new Vec3d(rotationX * f4 + rotationXY * f4, rotationZ * f4, rotationYZ * f4 + rotationXZ * f4), new Vec3d(rotationX * f4 - rotationXY * f4, -rotationZ * f4, rotationYZ * f4 - rotationXZ * f4)};

        if (particleAngle != 0.0F)
        {
            float f8 = particleAngle + (particleAngle - prevParticleAngle) * partialTicks;
            float f9 = MathHelper.cos(f8 * 0.5F);
            float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.xCoord;
            float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.yCoord;
            float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.zCoord;
            Vec3d vec3d = new Vec3d(f10, f11, f12);

            for (int l = 0; l < 4; ++l)
            {
                avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double)(f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale(2.0F * f9));
            }
        }

        worldRendererIn.pos((double)f5 + avec3d[0].xCoord, (double)f6 + avec3d[0].yCoord, (double)f7 + avec3d[0].zCoord).tex(f1, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)f5 + avec3d[1].xCoord, (double)f6 + avec3d[1].yCoord, (double)f7 + avec3d[1].zCoord).tex(f1, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)f5 + avec3d[2].xCoord, (double)f6 + avec3d[2].yCoord, (double)f7 + avec3d[2].zCoord).tex(f, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)f5 + avec3d[3].xCoord, (double)f6 + avec3d[3].yCoord, (double)f7 + avec3d[3].zCoord).tex(f, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
    }

    /**
     * Retrieve what effect layer (what texture) the particle should be rendered with. 0 for the particle sprite sheet,
     * 1 for the main Texture atlas, and 3 for a custom texture
     */
    public int getFXLayer()
    {
        return 0;
    }

    /**
     * Sets the texture used by the particle.
     */
    public void setParticleTexture(TextureAtlasSprite texture)
    {
        int i = getFXLayer();

        if (i == 1)
        {
            particleTexture = texture;
        }
        else
        {
            throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
        }
    }

    /**
     * Public method to set private field particleTextureIndex.
     */
    public void setParticleTextureIndex(int particleTextureIndex)
    {
        if (getFXLayer() != 0)
        {
            throw new RuntimeException("Invalid call to Particle.setMiscTex");
        }
        else
        {
            particleTextureIndexX = particleTextureIndex % 16;
            particleTextureIndexY = particleTextureIndex / 16;
        }
    }

    public void nextTextureIndexX()
    {
        ++particleTextureIndexX;
    }

    public String toString()
    {
        return getClass().getSimpleName() + ", Pos (" + posX + "," + posY + "," + posZ + "), RGBA (" + particleRed + "," + particleGreen + "," + particleBlue + "," + particleAlpha + "), Age " + particleAge;
    }

    /**
     * Called to indicate that this particle effect has expired and should be discontinued.
     */
    public void setExpired()
    {
        isExpired = true;
    }

    protected void setSize(float p_187115_1_, float p_187115_2_)
    {
        if (p_187115_1_ != width || p_187115_2_ != height)
        {
            width = p_187115_1_;
            height = p_187115_2_;
            AxisAlignedBB axisalignedbb = getEntityBoundingBox();
            setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double) width, axisalignedbb.minY + (double) height, axisalignedbb.minZ + (double) width));
        }
    }

    public void setPosition(double p_187109_1_, double p_187109_3_, double p_187109_5_)
    {
        posX = p_187109_1_;
        posY = p_187109_3_;
        posZ = p_187109_5_;
        float f = width / 2.0F;
        float f1 = height;
        setEntityBoundingBox(new AxisAlignedBB(p_187109_1_ - (double)f, p_187109_3_, p_187109_5_ - (double)f, p_187109_1_ + (double)f, p_187109_3_ + (double)f1, p_187109_5_ + (double)f));
    }

    public void moveEntity(double x, double y, double z)
    {
        double d0 = y;

        if (canCollide)
        {
            List<AxisAlignedBB> list = worldObj.getCollisionBoxes(null, getEntityBoundingBox().addCoord(x, y, z));

            for (AxisAlignedBB axisalignedbb : list)
            {
                y = axisalignedbb.calculateYOffset(getEntityBoundingBox(), y);
            }

            setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, y, 0.0D));

            for (AxisAlignedBB axisalignedbb1 : list)
            {
                x = axisalignedbb1.calculateXOffset(getEntityBoundingBox(), x);
            }

            setEntityBoundingBox(getEntityBoundingBox().offset(x, 0.0D, 0.0D));

            for (AxisAlignedBB axisalignedbb2 : list)
            {
                z = axisalignedbb2.calculateZOffset(getEntityBoundingBox(), z);
            }

            setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, 0.0D, z));
        }
        else
        {
            setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
        }

        resetPositionToBB();
        isCollided = y != y && d0 < 0.0D;

        if (x != x)
        {
            motionX = 0.0D;
        }

        if (z != z)
        {
            motionZ = 0.0D;
        }
    }

    protected void resetPositionToBB()
    {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox();
        posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
        posY = axisalignedbb.minY;
        posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
    }

    public int getBrightnessForRender(float p_189214_1_)
    {
        BlockPos blockpos = new BlockPos(posX, posY, posZ);
        return worldObj.isBlockLoaded(blockpos) ? worldObj.getCombinedLight(blockpos, 0) : 0;
    }

    /**
     * Returns true if this effect has not yet expired. "I feel happy! I feel happy!"
     */
    public boolean isAlive()
    {
        return !isExpired;
    }

    public AxisAlignedBB getEntityBoundingBox()
    {
        return boundingBox;
    }

    public void setEntityBoundingBox(AxisAlignedBB bb)
    {
        boundingBox = bb;
    }
}
