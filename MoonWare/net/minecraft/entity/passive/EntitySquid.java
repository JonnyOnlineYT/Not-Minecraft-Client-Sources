package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySquid extends EntityWaterMob
{
    public float squidPitch;
    public float prevSquidPitch;
    public float squidYaw;
    public float prevSquidYaw;

    /**
     * appears to be rotation in radians; we already have pitch & yaw, so this completes the triumvirate.
     */
    public float squidRotation;

    /** previous squidRotation in radians */
    public float prevSquidRotation;

    /** angle of the tentacles in radians */
    public float tentacleAngle;

    /** the last calculated angle of the tentacles in radians */
    public float lastTentacleAngle;
    private float randomMotionSpeed;

    /** change in squidRotation in radians. */
    private float rotationVelocity;
    private float rotateSpeed;
    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;

    public EntitySquid(World worldIn)
    {
        super(worldIn);
        setSize(0.8F, 0.8F);
        rand.setSeed(1 + getEntityId());
        rotationVelocity = 1.0F / (rand.nextFloat() + 1.0F) * 0.2F;
    }

    public static void registerFixesSquid(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntitySquid.class);
    }

    protected void initEntityAI()
    {
        tasks.addTask(0, new EntitySquid.AIMoveRandom(this));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    }

    public float getEyeHeight()
    {
        return height * 0.5F;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SQUID_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_SQUID_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SQUID_DEATH;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_SQUID;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        prevSquidPitch = squidPitch;
        prevSquidYaw = squidYaw;
        prevSquidRotation = squidRotation;
        lastTentacleAngle = tentacleAngle;
        squidRotation += rotationVelocity;

        if ((double) squidRotation > (Math.PI * 2D))
        {
            if (world.isRemote)
            {
                squidRotation = ((float)Math.PI * 2F);
            }
            else
            {
                squidRotation = (float)((double) squidRotation - (Math.PI * 2D));

                if (rand.nextInt(10) == 0)
                {
                    rotationVelocity = 1.0F / (rand.nextFloat() + 1.0F) * 0.2F;
                }

                world.setEntityState(this, (byte)19);
            }
        }

        if (inWater)
        {
            if (squidRotation < (float)Math.PI)
            {
                float f = squidRotation / (float)Math.PI;
                tentacleAngle = MathHelper.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25F;

                if ((double)f > 0.75D)
                {
                    randomMotionSpeed = 1.0F;
                    rotateSpeed = 1.0F;
                }
                else
                {
                    rotateSpeed *= 0.8F;
                }
            }
            else
            {
                tentacleAngle = 0.0F;
                randomMotionSpeed *= 0.9F;
                rotateSpeed *= 0.99F;
            }

            if (!world.isRemote)
            {
                motionX = randomMotionVecX * randomMotionSpeed;
                motionY = randomMotionVecY * randomMotionSpeed;
                motionZ = randomMotionVecZ * randomMotionSpeed;
            }

            float f1 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
            renderYawOffset += (-((float)MathHelper.atan2(motionX, motionZ)) * (180F / (float)Math.PI) - renderYawOffset) * 0.1F;
            rotationYaw = renderYawOffset;
            squidYaw = (float)((double) squidYaw + Math.PI * (double) rotateSpeed * 1.5D);
            squidPitch += (-((float)MathHelper.atan2(f1, motionY)) * (180F / (float)Math.PI) - squidPitch) * 0.1F;
        }
        else
        {
            tentacleAngle = MathHelper.abs(MathHelper.sin(squidRotation)) * (float)Math.PI * 0.25F;

            if (!world.isRemote)
            {
                motionX = 0.0D;
                motionZ = 0.0D;

                if (isPotionActive(MobEffects.LEVITATION))
                {
                    motionY += 0.05D * (double)(getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - motionY;
                }
                else if (!hasNoGravity())
                {
                    motionY -= 0.08D;
                }

                motionY *= 0.9800000190734863D;
            }

            squidPitch = (float)((double) squidPitch + (double)(-90.0F - squidPitch) * 0.02D);
        }
    }

    public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_)
    {
        moveEntity(MoverType.SELF, motionX, motionY, motionZ);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return posY > 45.0D && posY < (double) world.getSeaLevel() && super.getCanSpawnHere();
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 19)
        {
            squidRotation = 0.0F;
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn)
    {
        randomMotionVecX = randomMotionVecXIn;
        randomMotionVecY = randomMotionVecYIn;
        randomMotionVecZ = randomMotionVecZIn;
    }

    public boolean hasMovementVector()
    {
        return randomMotionVecX != 0.0F || randomMotionVecY != 0.0F || randomMotionVecZ != 0.0F;
    }

    static class AIMoveRandom extends EntityAIBase
    {
        private final EntitySquid squid;

        public AIMoveRandom(EntitySquid p_i45859_1_)
        {
            squid = p_i45859_1_;
        }

        public boolean shouldExecute()
        {
            return true;
        }

        public void updateTask()
        {
            int i = squid.getAge();

            if (i > 100)
            {
                squid.setMovementVector(0.0F, 0.0F, 0.0F);
            }
            else if (squid.getRNG().nextInt(50) == 0 || !squid.inWater || !squid.hasMovementVector())
            {
                float f = squid.getRNG().nextFloat() * ((float)Math.PI * 2F);
                float f1 = MathHelper.cos(f) * 0.2F;
                float f2 = -0.1F + squid.getRNG().nextFloat() * 0.2F;
                float f3 = MathHelper.sin(f) * 0.2F;
                squid.setMovementVector(f1, f2, f3);
            }
        }
    }
}
