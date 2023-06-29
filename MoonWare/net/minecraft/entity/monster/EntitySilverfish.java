package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySilverfish extends EntityMob
{
    private EntitySilverfish.AISummonSilverfish summonSilverfish;

    public EntitySilverfish(World worldIn)
    {
        super(worldIn);
        setSize(0.4F, 0.3F);
    }

    public static void registerFixesSilverfish(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntitySilverfish.class);
    }

    protected void initEntityAI()
    {
        summonSilverfish = new EntitySilverfish.AISummonSilverfish(this);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(3, summonSilverfish);
        tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        tasks.addTask(5, new EntitySilverfish.AIHideInStone(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return 0.1D;
    }

    public float getEyeHeight()
    {
        return 0.1F;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SILVERFISH_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if ((source instanceof EntityDamageSource || source == DamageSource.magic) && summonSilverfish != null)
            {
                summonSilverfish.notifyHurt();
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_SILVERFISH;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        renderYawOffset = rotationYaw;
        super.onUpdate();
    }

    /**
     * Set the render yaw offset
     */
    public void setRenderYawOffset(float offset)
    {
        rotationYaw = offset;
        super.setRenderYawOffset(offset);
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return world.getBlockState(pos.down()).getBlock() == Blocks.STONE ? 10.0F : super.getBlockPathWeight(pos);
    }

    /**
     * Checks to make sure the light is not too bright where the mob is spawning
     */
    protected boolean isValidLightLevel()
    {
        return true;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        if (super.getCanSpawnHere())
        {
            EntityPlayer entityplayer = world.getNearestPlayerNotCreative(this, 5.0D);
            return entityplayer == null;
        }
        else
        {
            return false;
        }
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    static class AIHideInStone extends EntityAIWander
    {
        private EnumFacing facing;
        private boolean doMerge;

        public AIHideInStone(EntitySilverfish silverfishIn)
        {
            super(silverfishIn, 1.0D, 10);
            setMutexBits(1);
        }

        public boolean shouldExecute()
        {
            if (entity.getAttackTarget() != null)
            {
                return false;
            }
            else if (!entity.getNavigator().noPath())
            {
                return false;
            }
            else
            {
                Random random = entity.getRNG();

                if (entity.world.getGameRules().getBoolean("mobGriefing") && random.nextInt(10) == 0)
                {
                    facing = EnumFacing.random(random);
                    BlockPos blockpos = (new BlockPos(entity.posX, entity.posY + 0.5D, entity.posZ)).offset(facing);
                    IBlockState iblockstate = entity.world.getBlockState(blockpos);

                    if (BlockSilverfish.canContainSilverfish(iblockstate))
                    {
                        doMerge = true;
                        return true;
                    }
                }

                doMerge = false;
                return super.shouldExecute();
            }
        }

        public boolean continueExecuting()
        {
            return !doMerge && super.continueExecuting();
        }

        public void startExecuting()
        {
            if (!doMerge)
            {
                super.startExecuting();
            }
            else
            {
                World world = entity.world;
                BlockPos blockpos = (new BlockPos(entity.posX, entity.posY + 0.5D, entity.posZ)).offset(facing);
                IBlockState iblockstate = world.getBlockState(blockpos);

                if (BlockSilverfish.canContainSilverfish(iblockstate))
                {
                    world.setBlockState(blockpos, Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.forModelBlock(iblockstate)), 3);
                    entity.spawnExplosionParticle();
                    entity.setDead();
                }
            }
        }
    }

    static class AISummonSilverfish extends EntityAIBase
    {
        private final EntitySilverfish silverfish;
        private int lookForFriends;

        public AISummonSilverfish(EntitySilverfish silverfishIn)
        {
            silverfish = silverfishIn;
        }

        public void notifyHurt()
        {
            if (lookForFriends == 0)
            {
                lookForFriends = 20;
            }
        }

        public boolean shouldExecute()
        {
            return lookForFriends > 0;
        }

        public void updateTask()
        {
            --lookForFriends;

            if (lookForFriends <= 0)
            {
                World world = silverfish.world;
                Random random = silverfish.getRNG();
                BlockPos blockpos = new BlockPos(silverfish);

                for (int i = 0; i <= 5 && i >= -5; i = (i <= 0 ? 1 : 0) - i)
                {
                    for (int j = 0; j <= 10 && j >= -10; j = (j <= 0 ? 1 : 0) - j)
                    {
                        for (int k = 0; k <= 10 && k >= -10; k = (k <= 0 ? 1 : 0) - k)
                        {
                            BlockPos blockpos1 = blockpos.add(j, i, k);
                            IBlockState iblockstate = world.getBlockState(blockpos1);

                            if (iblockstate.getBlock() == Blocks.MONSTER_EGG)
                            {
                                if (world.getGameRules().getBoolean("mobGriefing"))
                                {
                                    world.destroyBlock(blockpos1, true);
                                }
                                else
                                {
                                    world.setBlockState(blockpos1, iblockstate.getValue(BlockSilverfish.VARIANT).getModelBlock(), 3);
                                }

                                if (random.nextBoolean())
                                {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
