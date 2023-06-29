package net.minecraft.entity.passive;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityHorse extends AbstractHorse
{
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    private static final DataParameter<Integer> HORSE_VARIANT = EntityDataManager.createKey(EntityHorse.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> HORSE_ARMOR = EntityDataManager.createKey(EntityHorse.class, DataSerializers.VARINT);
    private static final String[] HORSE_TEXTURES = {"textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png"};
    private static final String[] HORSE_TEXTURES_ABBR = {"hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb"};
    private static final String[] HORSE_MARKING_TEXTURES = {null, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png"};
    private static final String[] HORSE_MARKING_TEXTURES_ABBR = {"", "wo_", "wmo", "wdo", "bdo"};
    private String texturePrefix;
    private final String[] horseTexturesArray = new String[3];

    public EntityHorse(World worldIn)
    {
        super(worldIn);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(HORSE_VARIANT, Integer.valueOf(0));
        dataManager.register(HORSE_ARMOR, Integer.valueOf(HorseArmorType.NONE.getOrdinal()));
    }

    public static void registerFixesHorse(DataFixer fixer)
    {
        AbstractHorse.func_190683_c(fixer, EntityHorse.class);
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityHorse.class, "ArmorItem"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", getHorseVariant());

        if (!horseChest.getStackInSlot(1).isEmpty())
        {
            compound.setTag("ArmorItem", horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setHorseVariant(compound.getInteger("Variant"));

        if (compound.hasKey("ArmorItem", 10))
        {
            ItemStack itemstack = new ItemStack(compound.getCompoundTag("ArmorItem"));

            if (!itemstack.isEmpty() && HorseArmorType.isHorseArmor(itemstack.getItem()))
            {
                horseChest.setInventorySlotContents(1, itemstack);
            }
        }

        updateHorseSlots();
    }

    public void setHorseVariant(int variant)
    {
        dataManager.set(HORSE_VARIANT, Integer.valueOf(variant));
        resetTexturePrefix();
    }

    public int getHorseVariant()
    {
        return dataManager.get(HORSE_VARIANT).intValue();
    }

    private void resetTexturePrefix()
    {
        texturePrefix = null;
    }

    private void setHorseTexturePaths()
    {
        int i = getHorseVariant();
        int j = (i & 255) % 7;
        int k = ((i & 65280) >> 8) % 5;
        HorseArmorType horsearmortype = getHorseArmorType();
        horseTexturesArray[0] = HORSE_TEXTURES[j];
        horseTexturesArray[1] = HORSE_MARKING_TEXTURES[k];
        horseTexturesArray[2] = horsearmortype.getTextureName();
        texturePrefix = "horse/" + HORSE_TEXTURES_ABBR[j] + HORSE_MARKING_TEXTURES_ABBR[k] + horsearmortype.getHash();
    }

    public String getHorseTexture()
    {
        if (texturePrefix == null)
        {
            setHorseTexturePaths();
        }

        return texturePrefix;
    }

    public String[] getVariantTexturePaths()
    {
        if (texturePrefix == null)
        {
            setHorseTexturePaths();
        }

        return horseTexturesArray;
    }

    /**
     * Updates the items in the saddle and armor slots of the horse's inventory.
     */
    protected void updateHorseSlots()
    {
        super.updateHorseSlots();
        setHorseArmorStack(horseChest.getStackInSlot(1));
    }

    /**
     * Set horse armor stack (for example: new ItemStack(Items.iron_horse_armor))
     */
    public void setHorseArmorStack(ItemStack itemStackIn)
    {
        HorseArmorType horsearmortype = HorseArmorType.getByItemStack(itemStackIn);
        dataManager.set(HORSE_ARMOR, Integer.valueOf(horsearmortype.getOrdinal()));
        resetTexturePrefix();

        if (!world.isRemote)
        {
            getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            int i = horsearmortype.getProtection();

            if (i != 0)
            {
                getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier((new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", i, 0)).setSaved(false));
            }
        }
    }

    public HorseArmorType getHorseArmorType()
    {
        return HorseArmorType.getByOrdinal(dataManager.get(HORSE_ARMOR).intValue());
    }

    /**
     * Called by InventoryBasic.onInventoryChanged() on a array that is never filled.
     */
    public void onInventoryChanged(IInventory invBasic)
    {
        HorseArmorType horsearmortype = getHorseArmorType();
        super.onInventoryChanged(invBasic);
        HorseArmorType horsearmortype1 = getHorseArmorType();

        if (ticksExisted > 20 && horsearmortype != horsearmortype1 && horsearmortype1 != HorseArmorType.NONE)
        {
            playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
        }
    }

    protected void func_190680_a(SoundType p_190680_1_)
    {
        super.func_190680_a(p_190680_1_);

        if (rand.nextInt(10) == 0)
        {
            playSound(SoundEvents.ENTITY_HORSE_BREATHE, p_190680_1_.getVolume() * 0.6F, p_190680_1_.getPitch());
        }
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getModifiedMaxHealth());
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getModifiedMovementSpeed());
        getEntityAttribute(AbstractHorse.JUMP_STRENGTH).setBaseValue(getModifiedJumpStrength());
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (world.isRemote && dataManager.isDirty())
        {
            dataManager.setClean();
            resetTexturePrefix();
        }
    }

    protected SoundEvent getAmbientSound()
    {
        super.getAmbientSound();
        return SoundEvents.ENTITY_HORSE_AMBIENT;
    }

    protected SoundEvent getDeathSound()
    {
        super.getDeathSound();
        return SoundEvents.ENTITY_HORSE_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        super.getHurtSound(p_184601_1_);
        return SoundEvents.ENTITY_HORSE_HURT;
    }

    protected SoundEvent getAngrySound()
    {
        super.getAngrySound();
        return SoundEvents.ENTITY_HORSE_ANGRY;
    }

    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_HORSE;
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = !itemstack.isEmpty();

        if (flag && itemstack.getItem() == Items.SPAWN_EGG)
        {
            return super.processInteract(player, hand);
        }
        else
        {
            if (!isChild())
            {
                if (isTame() && player.isSneaking())
                {
                    openGUI(player);
                    return true;
                }

                if (isBeingRidden())
                {
                    return super.processInteract(player, hand);
                }
            }

            if (flag)
            {
                if (func_190678_b(player, itemstack))
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        itemstack.func_190918_g(1);
                    }

                    return true;
                }

                if (itemstack.interactWithEntity(player, this, hand))
                {
                    return true;
                }

                if (!isTame())
                {
                    func_190687_dF();
                    return true;
                }

                boolean flag1 = HorseArmorType.getByItemStack(itemstack) != HorseArmorType.NONE;
                boolean flag2 = !isChild() && !isHorseSaddled() && itemstack.getItem() == Items.SADDLE;

                if (flag1 || flag2)
                {
                    openGUI(player);
                    return true;
                }
            }

            if (isChild())
            {
                return super.processInteract(player, hand);
            }
            else
            {
                mountTo(player);
                return true;
            }
        }
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        if (otherAnimal == this)
        {
            return false;
        }
        else if (!(otherAnimal instanceof EntityDonkey) && !(otherAnimal instanceof EntityHorse))
        {
            return false;
        }
        else
        {
            return canMate() && ((AbstractHorse)otherAnimal).canMate();
        }
    }

    public EntityAgeable createChild(EntityAgeable ageable)
    {
        AbstractHorse abstracthorse;

        if (ageable instanceof EntityDonkey)
        {
            abstracthorse = new EntityMule(world);
        }
        else
        {
            EntityHorse entityhorse = (EntityHorse)ageable;
            abstracthorse = new EntityHorse(world);
            int j = rand.nextInt(9);
            int i;

            if (j < 4)
            {
                i = getHorseVariant() & 255;
            }
            else if (j < 8)
            {
                i = entityhorse.getHorseVariant() & 255;
            }
            else
            {
                i = rand.nextInt(7);
            }

            int k = rand.nextInt(5);

            if (k < 2)
            {
                i = i | getHorseVariant() & 65280;
            }
            else if (k < 4)
            {
                i = i | entityhorse.getHorseVariant() & 65280;
            }
            else
            {
                i = i | rand.nextInt(5) << 8 & 65280;
            }

            ((EntityHorse)abstracthorse).setHorseVariant(i);
        }

        func_190681_a(ageable, abstracthorse);
        return abstracthorse;
    }

    public boolean func_190677_dK()
    {
        return true;
    }

    public boolean func_190682_f(ItemStack p_190682_1_)
    {
        return HorseArmorType.isHorseArmor(p_190682_1_.getItem());
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        int i;

        if (livingdata instanceof EntityHorse.GroupData)
        {
            i = ((EntityHorse.GroupData)livingdata).field_190885_a;
        }
        else
        {
            i = rand.nextInt(7);
            livingdata = new EntityHorse.GroupData(i);
        }

        setHorseVariant(i | rand.nextInt(5) << 8);
        return livingdata;
    }

    public static class GroupData implements IEntityLivingData
    {
        public int field_190885_a;

        public GroupData(int p_i47337_1_)
        {
            field_190885_a = p_i47337_1_;
        }
    }
}
