package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PhaseSittingFlaming extends PhaseSittingBase
{
    private int flameTicks;
    private int flameCount;
    private EntityAreaEffectCloud areaEffectCloud;

    public PhaseSittingFlaming(EntityDragon dragonIn)
    {
        super(dragonIn);
    }

    /**
     * Generates particle effects appropriate to the phase (or sometimes sounds).
     * Called by dragon's onLivingUpdate. Only used when worldObj.isRemote.
     */
    public void doClientRenderEffects()
    {
        ++flameTicks;

        if (flameTicks % 2 == 0 && flameTicks < 10)
        {
            Vec3d vec3d = dragon.getHeadLookVec(1.0F).normalize();
            vec3d.rotateYaw(-((float)Math.PI / 4F));
            double d0 = dragon.dragonPartHead.posX;
            double d1 = dragon.dragonPartHead.posY + (double)(dragon.dragonPartHead.height / 2.0F);
            double d2 = dragon.dragonPartHead.posZ;

            for (int i = 0; i < 8; ++i)
            {
                double d3 = d0 + dragon.getRNG().nextGaussian() / 2.0D;
                double d4 = d1 + dragon.getRNG().nextGaussian() / 2.0D;
                double d5 = d2 + dragon.getRNG().nextGaussian() / 2.0D;

                for (int j = 0; j < 6; ++j)
                {
                    dragon.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, d3, d4, d5, -vec3d.xCoord * 0.07999999821186066D * (double)j, -vec3d.yCoord * 0.6000000238418579D, -vec3d.zCoord * 0.07999999821186066D * (double)j);
                }

                vec3d.rotateYaw(0.19634955F);
            }
        }
    }

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    public void doLocalUpdate()
    {
        ++flameTicks;

        if (flameTicks >= 200)
        {
            if (flameCount >= 4)
            {
                dragon.getPhaseManager().setPhase(PhaseList.TAKEOFF);
            }
            else
            {
                dragon.getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
            }
        }
        else if (flameTicks == 10)
        {
            Vec3d vec3d = (new Vec3d(dragon.dragonPartHead.posX - dragon.posX, 0.0D, dragon.dragonPartHead.posZ - dragon.posZ)).normalize();
            float f = 5.0F;
            double d0 = dragon.dragonPartHead.posX + vec3d.xCoord * 5.0D / 2.0D;
            double d1 = dragon.dragonPartHead.posZ + vec3d.zCoord * 5.0D / 2.0D;
            double d2 = dragon.dragonPartHead.posY + (double)(dragon.dragonPartHead.height / 2.0F);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(d0), MathHelper.floor(d2), MathHelper.floor(d1));

            while (dragon.world.isAirBlock(blockpos$mutableblockpos))
            {
                --d2;
                blockpos$mutableblockpos.setPos(MathHelper.floor(d0), MathHelper.floor(d2), MathHelper.floor(d1));
            }

            d2 = MathHelper.floor(d2) + 1;
            areaEffectCloud = new EntityAreaEffectCloud(dragon.world, d0, d2, d1);
            areaEffectCloud.setOwner(dragon);
            areaEffectCloud.setRadius(5.0F);
            areaEffectCloud.setDuration(200);
            areaEffectCloud.setParticle(EnumParticleTypes.DRAGON_BREATH);
            areaEffectCloud.addEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE));
            dragon.world.spawnEntityInWorld(areaEffectCloud);
        }
    }

    /**
     * Called when this phase is set to active
     */
    public void initPhase()
    {
        flameTicks = 0;
        ++flameCount;
    }

    public void removeAreaEffect()
    {
        if (areaEffectCloud != null)
        {
            areaEffectCloud.setDead();
            areaEffectCloud = null;
        }
    }

    public PhaseList<PhaseSittingFlaming> getPhaseList()
    {
        return PhaseList.SITTING_FLAMING;
    }

    public void resetFlameCount()
    {
        flameCount = 0;
    }
}
