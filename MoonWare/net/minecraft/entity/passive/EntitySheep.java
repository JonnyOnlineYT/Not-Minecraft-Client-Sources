package net.minecraft.entity.passive;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySheep extends EntityAnimal
{
    private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntitySheep.class, DataSerializers.BYTE);

    /**
     * Internal crafting inventory used to check the result of mixing dyes corresponding to the fleece color when
     * breeding sheep.
     */
    private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container()
    {
        public boolean canInteractWith(EntityPlayer playerIn)
        {
            return false;
        }
    }, 2, 1);
    private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);

    /**
     * Used to control movement as well as wool regrowth. Set to 40 on handleHealthUpdate and counts down with each
     * tick.
     */
    private int sheepTimer;
    private EntityAIEatGrass entityAIEatGrass;

    private static float[] func_192020_c(EnumDyeColor p_192020_0_)
    {
        float[] afloat = p_192020_0_.func_193349_f();
        float f = 0.75F;
        return new float[] {afloat[0] * 0.75F, afloat[1] * 0.75F, afloat[2] * 0.75F};
    }

    public static float[] getDyeRgb(EnumDyeColor dyeColor)
    {
        return DYE_TO_RGB.get(dyeColor);
    }

    public EntitySheep(World worldIn)
    {
        super(worldIn);
        setSize(0.9F, 1.3F);
        inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.DYE));
        inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.DYE));
    }

    protected void initEntityAI()
    {
        entityAIEatGrass = new EntityAIEatGrass(this);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        tasks.addTask(2, new EntityAIMate(this, 1.0D));
        tasks.addTask(3, new EntityAITempt(this, 1.1D, Items.WHEAT, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        tasks.addTask(5, entityAIEatGrass);
        tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
    }

    protected void updateAITasks()
    {
        sheepTimer = entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (world.isRemote)
        {
            sheepTimer = Math.max(0, sheepTimer - 1);
        }

        super.onLivingUpdate();
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(DYE_COLOR, Byte.valueOf((byte)0));
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        if (getSheared())
        {
            return LootTableList.ENTITIES_SHEEP;
        }
        else
        {
            switch (getFleeceColor())
            {
                case WHITE:
                default:
                    return LootTableList.ENTITIES_SHEEP_WHITE;

                case ORANGE:
                    return LootTableList.ENTITIES_SHEEP_ORANGE;

                case MAGENTA:
                    return LootTableList.ENTITIES_SHEEP_MAGENTA;

                case LIGHT_BLUE:
                    return LootTableList.ENTITIES_SHEEP_LIGHT_BLUE;

                case YELLOW:
                    return LootTableList.ENTITIES_SHEEP_YELLOW;

                case LIME:
                    return LootTableList.ENTITIES_SHEEP_LIME;

                case PINK:
                    return LootTableList.ENTITIES_SHEEP_PINK;

                case GRAY:
                    return LootTableList.ENTITIES_SHEEP_GRAY;

                case SILVER:
                    return LootTableList.ENTITIES_SHEEP_SILVER;

                case CYAN:
                    return LootTableList.ENTITIES_SHEEP_CYAN;

                case PURPLE:
                    return LootTableList.ENTITIES_SHEEP_PURPLE;

                case BLUE:
                    return LootTableList.ENTITIES_SHEEP_BLUE;

                case BROWN:
                    return LootTableList.ENTITIES_SHEEP_BROWN;

                case GREEN:
                    return LootTableList.ENTITIES_SHEEP_GREEN;

                case RED:
                    return LootTableList.ENTITIES_SHEEP_RED;

                case BLACK:
                    return LootTableList.ENTITIES_SHEEP_BLACK;
            }
        }
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 10)
        {
            sheepTimer = 40;
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    public float getHeadRotationPointY(float p_70894_1_)
    {
        if (sheepTimer <= 0)
        {
            return 0.0F;
        }
        else if (sheepTimer >= 4 && sheepTimer <= 36)
        {
            return 1.0F;
        }
        else
        {
            return sheepTimer < 4 ? ((float) sheepTimer - p_70894_1_) / 4.0F : -((float)(sheepTimer - 40) - p_70894_1_) / 4.0F;
        }
    }

    public float getHeadRotationAngleX(float p_70890_1_)
    {
        if (sheepTimer > 4 && sheepTimer <= 36)
        {
            float f = ((float)(sheepTimer - 4) - p_70890_1_) / 32.0F;
            return ((float)Math.PI / 5F) + ((float)Math.PI * 7F / 100F) * MathHelper.sin(f * 28.7F);
        }
        else
        {
            return sheepTimer > 0 ? ((float)Math.PI / 5F) : rotationPitch * 0.017453292F;
        }
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.SHEARS && !getSheared() && !isChild())
        {
            if (!world.isRemote)
            {
                setSheared(true);
                int i = 1 + rand.nextInt(3);

                for (int j = 0; j < i; ++j)
                {
                    EntityItem entityitem = entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, getFleeceColor().getMetadata()), 1.0F);
                    entityitem.motionY += rand.nextFloat() * 0.05F;
                    entityitem.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    entityitem.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                }
            }

            itemstack.damageItem(1, player);
            playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        }

        return super.processInteract(player, hand);
    }

    public static void registerFixesSheep(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntitySheep.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Sheared", getSheared());
        compound.setByte("Color", (byte) getFleeceColor().getMetadata());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setSheared(compound.getBoolean("Sheared"));
        setFleeceColor(EnumDyeColor.byMetadata(compound.getByte("Color")));
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }

    /**
     * Gets the wool color of this sheep.
     */
    public EnumDyeColor getFleeceColor()
    {
        return EnumDyeColor.byMetadata(dataManager.get(DYE_COLOR).byteValue() & 15);
    }

    /**
     * Sets the wool color of this sheep
     */
    public void setFleeceColor(EnumDyeColor color)
    {
        byte b0 = dataManager.get(DYE_COLOR).byteValue();
        dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 & 240 | color.getMetadata() & 15)));
    }

    /**
     * returns true if a sheeps wool has been sheared
     */
    public boolean getSheared()
    {
        return (dataManager.get(DYE_COLOR).byteValue() & 16) != 0;
    }

    /**
     * make a sheep sheared if set to true
     */
    public void setSheared(boolean sheared)
    {
        byte b0 = dataManager.get(DYE_COLOR).byteValue();

        if (sheared)
        {
            dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 | 16)));
        }
        else
        {
            dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 & -17)));
        }
    }

    /**
     * Chooses a "vanilla" sheep color based on the provided random.
     */
    public static EnumDyeColor getRandomSheepColor(Random random)
    {
        int i = random.nextInt(100);

        if (i < 5)
        {
            return EnumDyeColor.BLACK;
        }
        else if (i < 10)
        {
            return EnumDyeColor.GRAY;
        }
        else if (i < 15)
        {
            return EnumDyeColor.SILVER;
        }
        else if (i < 18)
        {
            return EnumDyeColor.BROWN;
        }
        else
        {
            return random.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE;
        }
    }

    public EntitySheep createChild(EntityAgeable ageable)
    {
        EntitySheep entitysheep = (EntitySheep)ageable;
        EntitySheep entitysheep1 = new EntitySheep(world);
        entitysheep1.setFleeceColor(getDyeColorMixFromParents(this, entitysheep));
        return entitysheep1;
    }

    /**
     * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This
     * function is used in the AIEatGrass)
     */
    public void eatGrassBonus()
    {
        setSheared(false);

        if (isChild())
        {
            addGrowth(60);
        }
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        setFleeceColor(getRandomSheepColor(world.rand));
        return livingdata;
    }

    /**
     * Attempts to mix both parent sheep to come up with a mixed dye color.
     */
    private EnumDyeColor getDyeColorMixFromParents(EntityAnimal father, EntityAnimal mother)
    {
        int i = ((EntitySheep)father).getFleeceColor().getDyeDamage();
        int j = ((EntitySheep)mother).getFleeceColor().getDyeDamage();
        inventoryCrafting.getStackInSlot(0).setItemDamage(i);
        inventoryCrafting.getStackInSlot(1).setItemDamage(j);
        ItemStack itemstack = CraftingManager.findMatchingRecipe(inventoryCrafting, father.world);
        int k;

        if (itemstack.getItem() == Items.DYE)
        {
            k = itemstack.getMetadata();
        }
        else
        {
            k = world.rand.nextBoolean() ? i : j;
        }

        return EnumDyeColor.byDyeDamage(k);
    }

    public float getEyeHeight()
    {
        return 0.95F * height;
    }

    static
    {
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
        {
            DYE_TO_RGB.put(enumdyecolor, func_192020_c(enumdyecolor));
        }

        DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[] {0.9019608F, 0.9019608F, 0.9019608F});
    }
}
