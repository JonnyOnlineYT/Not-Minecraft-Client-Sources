package net.minecraft.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPigZombie extends EntityZombie
{
    private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
    private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER = (new AttributeModifier(ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05D, 0)).setSaved(false);

    /** Above zero if this PigZombie is Angry. */
    private int angerLevel;

    /** A random delay until this PigZombie next makes a sound. */
    private int randomSoundDelay;
    private UUID angerTargetUUID;

    public EntityPigZombie(World worldIn)
    {
        super(worldIn);
        isImmuneToFire = true;
    }

    public void setRevengeTarget(@Nullable EntityLivingBase livingBase)
    {
        super.setRevengeTarget(livingBase);

        if (livingBase != null)
        {
            angerTargetUUID = livingBase.getUniqueID();
        }
    }

    protected void applyEntityAI()
    {
        targetTasks.addTask(1, new EntityPigZombie.AIHurtByAggressor(this));
        targetTasks.addTask(2, new EntityPigZombie.AITargetAggressor(this));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(EntityZombie.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    protected void updateAITasks()
    {
        IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (isAngry())
        {
            if (!isChild() && !iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER))
            {
                iattributeinstance.applyModifier(ATTACK_SPEED_BOOST_MODIFIER);
            }

            --angerLevel;
        }
        else if (iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER))
        {
            iattributeinstance.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
        }

        if (randomSoundDelay > 0 && --randomSoundDelay == 0)
        {
            playSound(SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, getSoundVolume() * 2.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        if (angerLevel > 0 && angerTargetUUID != null && getAITarget() == null)
        {
            EntityPlayer entityplayer = world.getPlayerEntityByUUID(angerTargetUUID);
            setRevengeTarget(entityplayer);
            attackingPlayer = entityplayer;
            recentlyHit = getRevengeTimer();
        }

        super.updateAITasks();
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    /**
     * Checks that the entity is not colliding with any blocks / liquids
     */
    public boolean isNotColliding()
    {
        return world.checkNoEntityCollision(getEntityBoundingBox(), this) && world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(getEntityBoundingBox());
    }

    public static void registerFixesPigZombie(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityPigZombie.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setShort("Anger", (short) angerLevel);

        if (angerTargetUUID != null)
        {
            compound.setString("HurtBy", angerTargetUUID.toString());
        }
        else
        {
            compound.setString("HurtBy", "");
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        angerLevel = compound.getShort("Anger");
        String s = compound.getString("HurtBy");

        if (!s.isEmpty())
        {
            angerTargetUUID = UUID.fromString(s);
            EntityPlayer entityplayer = world.getPlayerEntityByUUID(angerTargetUUID);
            setRevengeTarget(entityplayer);

            if (entityplayer != null)
            {
                attackingPlayer = entityplayer;
                recentlyHit = getRevengeTimer();
            }
        }
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
            Entity entity = source.getEntity();

            if (entity instanceof EntityPlayer)
            {
                becomeAngryAt(entity);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    /**
     * Causes this PigZombie to become angry at the supplied Entity (which will be a player).
     */
    private void becomeAngryAt(Entity p_70835_1_)
    {
        angerLevel = 400 + rand.nextInt(400);
        randomSoundDelay = rand.nextInt(40);

        if (p_70835_1_ instanceof EntityLivingBase)
        {
            setRevengeTarget((EntityLivingBase)p_70835_1_);
        }
    }

    public boolean isAngry()
    {
        return angerLevel > 0;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_PIG_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_ZOMBIE_PIG_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_PIG_DEATH;
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_ZOMBIE_PIGMAN;
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        return false;
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
    }

    protected ItemStack func_190732_dj()
    {
        return ItemStack.EMPTY;
    }

    public boolean func_191990_c(EntityPlayer p_191990_1_)
    {
        return isAngry();
    }

    static class AIHurtByAggressor extends EntityAIHurtByTarget
    {
        public AIHurtByAggressor(EntityPigZombie p_i45828_1_)
        {
            super(p_i45828_1_, true);
        }

        protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
        {
            super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);

            if (creatureIn instanceof EntityPigZombie)
            {
                ((EntityPigZombie)creatureIn).becomeAngryAt(entityLivingBaseIn);
            }
        }
    }

    static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        public AITargetAggressor(EntityPigZombie p_i45829_1_)
        {
            super(p_i45829_1_, EntityPlayer.class, true);
        }

        public boolean shouldExecute()
        {
            return ((EntityPigZombie) taskOwner).isAngry() && super.shouldExecute();
        }
    }
}
