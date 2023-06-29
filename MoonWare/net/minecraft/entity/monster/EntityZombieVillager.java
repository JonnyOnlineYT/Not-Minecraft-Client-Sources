package net.minecraft.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombieVillager extends EntityZombie
{
    private static final DataParameter<Boolean> CONVERTING = EntityDataManager.createKey(EntityZombieVillager.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> field_190739_c = EntityDataManager.createKey(EntityZombieVillager.class, DataSerializers.VARINT);

    /**
     * Ticker used to determine the time remaining for this zombie to convert into a villager when cured.
     */
    private int conversionTime;
    private UUID field_191992_by;

    public EntityZombieVillager(World p_i47277_1_)
    {
        super(p_i47277_1_);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(CONVERTING, Boolean.valueOf(false));
        dataManager.register(field_190739_c, Integer.valueOf(0));
    }

    public void func_190733_a(int p_190733_1_)
    {
        dataManager.set(field_190739_c, Integer.valueOf(p_190733_1_));
    }

    public int func_190736_dl()
    {
        return Math.max(dataManager.get(field_190739_c).intValue() % 6, 0);
    }

    public static void func_190737_b(DataFixer p_190737_0_)
    {
        EntityLiving.registerFixesMob(p_190737_0_, EntityZombieVillager.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Profession", func_190736_dl());
        compound.setInteger("ConversionTime", isConverting() ? conversionTime : -1);

        if (field_191992_by != null)
        {
            compound.setUniqueId("ConversionPlayer", field_191992_by);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        func_190733_a(compound.getInteger("Profession"));

        if (compound.hasKey("ConversionTime", 99) && compound.getInteger("ConversionTime") > -1)
        {
            func_191991_a(compound.hasUniqueId("ConversionPlayer") ? compound.getUniqueId("ConversionPlayer") : null, compound.getInteger("ConversionTime"));
        }
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        func_190733_a(world.rand.nextInt(6));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (!world.isRemote && isConverting())
        {
            int i = func_190735_dq();
            conversionTime -= i;

            if (conversionTime <= 0)
            {
                func_190738_dp();
            }
        }

        super.onUpdate();
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.GOLDEN_APPLE && itemstack.getMetadata() == 0 && isPotionActive(MobEffects.WEAKNESS))
        {
            if (!player.capabilities.isCreativeMode)
            {
                itemstack.func_190918_g(1);
            }

            if (!world.isRemote)
            {
                func_191991_a(player.getUniqueID(), rand.nextInt(2401) + 3600);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return !isConverting();
    }

    /**
     * Returns whether this zombie is in the process of converting to a villager
     */
    public boolean isConverting()
    {
        return getDataManager().get(CONVERTING).booleanValue();
    }

    protected void func_191991_a(@Nullable UUID p_191991_1_, int p_191991_2_)
    {
        field_191992_by = p_191991_1_;
        conversionTime = p_191991_2_;
        getDataManager().set(CONVERTING, Boolean.valueOf(true));
        removePotionEffect(MobEffects.WEAKNESS);
        addPotionEffect(new PotionEffect(MobEffects.STRENGTH, p_191991_2_, Math.min(world.getDifficulty().getDifficultyId() - 1, 0)));
        world.setEntityState(this, (byte)16);
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 16)
        {
            if (!isSilent())
            {
                world.playSound(posX + 0.5D, posY + 0.5D, posZ + 0.5D, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, getSoundCategory(), 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    protected void func_190738_dp()
    {
        EntityVillager entityvillager = new EntityVillager(world);
        entityvillager.copyLocationAndAnglesFrom(this);
        entityvillager.setProfession(func_190736_dl());
        entityvillager.func_190672_a(world.getDifficultyForLocation(new BlockPos(entityvillager)), null, false);
        entityvillager.setLookingForHome();

        if (isChild())
        {
            entityvillager.setGrowingAge(-24000);
        }

        world.removeEntity(this);
        entityvillager.setNoAI(isAIDisabled());

        if (hasCustomName())
        {
            entityvillager.setCustomNameTag(getCustomNameTag());
            entityvillager.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
        }

        world.spawnEntityInWorld(entityvillager);

        if (field_191992_by != null)
        {
            EntityPlayer entityplayer = world.getPlayerEntityByUUID(field_191992_by);

            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.field_192137_q.func_192183_a((EntityPlayerMP)entityplayer, this, entityvillager);
            }
        }

        entityvillager.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
        world.playEvent(null, 1027, new BlockPos((int) posX, (int) posY, (int) posZ), 0);
    }

    protected int func_190735_dq()
    {
        int i = 1;

        if (rand.nextFloat() < 0.01F)
        {
            int j = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = (int) posX - 4; k < (int) posX + 4 && j < 14; ++k)
            {
                for (int l = (int) posY - 4; l < (int) posY + 4 && j < 14; ++l)
                {
                    for (int i1 = (int) posZ - 4; i1 < (int) posZ + 4 && j < 14; ++i1)
                    {
                        Block block = world.getBlockState(blockpos$mutableblockpos.setPos(k, l, i1)).getBlock();

                        if (block == Blocks.IRON_BARS || block == Blocks.BED)
                        {
                            if (rand.nextFloat() < 0.3F)
                            {
                                ++i;
                            }

                            ++j;
                        }
                    }
                }
            }
        }

        return i;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    protected float getSoundPitch()
    {
        return isChild() ? (rand.nextFloat() - rand.nextFloat()) * 0.2F + 2.0F : (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;
    }

    public SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_HURT;
    }

    public SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_DEATH;
    }

    public SoundEvent func_190731_di()
    {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP;
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.field_191183_as;
    }

    protected ItemStack func_190732_dj()
    {
        return ItemStack.EMPTY;
    }
}
