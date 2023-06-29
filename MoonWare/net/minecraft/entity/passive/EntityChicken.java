package net.minecraft.entity.passive;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityChicken extends EntityAnimal
{
    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;

    /** The time until the next egg is spawned. */
    public int timeUntilNextEgg;
    public boolean chickenJockey;

    public EntityChicken(World worldIn)
    {
        super(worldIn);
        setSize(0.4F, 0.7F);
        timeUntilNextEgg = rand.nextInt(6000) + 6000;
        setPathPriority(PathNodeType.WATER, 0.0F);
    }

    protected void initEntityAI()
    {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        tasks.addTask(2, new EntityAIMate(this, 1.0D));
        tasks.addTask(3, new EntityAITempt(this, 1.0D, false, TEMPTATION_ITEMS));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(7, new EntityAILookIdle(this));
    }

    public float getEyeHeight()
    {
        return height;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        oFlap = wingRotation;
        oFlapSpeed = destPos;
        destPos = (float)((double) destPos + (double)(onGround ? -1 : 4) * 0.3D);
        destPos = MathHelper.clamp(destPos, 0.0F, 1.0F);

        if (!onGround && wingRotDelta < 1.0F)
        {
            wingRotDelta = 1.0F;
        }

        wingRotDelta = (float)((double) wingRotDelta * 0.9D);

        if (!onGround && motionY < 0.0D)
        {
            motionY *= 0.6D;
        }

        wingRotation += wingRotDelta * 2.0F;

        if (!world.isRemote && !isChild() && !isChickenJockey() && --timeUntilNextEgg <= 0)
        {
            playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            dropItem(Items.EGG, 1);
            timeUntilNextEgg = rand.nextInt(6000) + 6000;
        }
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_CHICKEN;
    }

    public EntityChicken createChild(EntityAgeable ageable)
    {
        return new EntityChicken(world);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
        return TEMPTATION_ITEMS.contains(stack.getItem());
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer player)
    {
        return isChickenJockey() ? 10 : super.getExperiencePoints(player);
    }

    public static void registerFixesChicken(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityChicken.class);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        chickenJockey = compound.getBoolean("IsChickenJockey");

        if (compound.hasKey("EggLayTime"))
        {
            timeUntilNextEgg = compound.getInteger("EggLayTime");
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("IsChickenJockey", chickenJockey);
        compound.setInteger("EggLayTime", timeUntilNextEgg);
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return isChickenJockey() && !isBeingRidden();
    }

    public void updatePassenger(Entity passenger)
    {
        super.updatePassenger(passenger);
        float f = MathHelper.sin(renderYawOffset * 0.017453292F);
        float f1 = MathHelper.cos(renderYawOffset * 0.017453292F);
        float f2 = 0.1F;
        float f3 = 0.0F;
        passenger.setPosition(posX + (double)(0.1F * f), posY + (double)(height * 0.5F) + passenger.getYOffset() + 0.0D, posZ - (double)(0.1F * f1));

        if (passenger instanceof EntityLivingBase)
        {
            ((EntityLivingBase)passenger).renderYawOffset = renderYawOffset;
        }
    }

    /**
     * Determines if this chicken is a jokey with a zombie riding it.
     */
    public boolean isChickenJockey()
    {
        return chickenJockey;
    }

    /**
     * Sets whether this chicken is a jockey or not.
     */
    public void setChickenJockey(boolean jockey)
    {
        chickenJockey = jockey;
    }
}
