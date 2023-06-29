package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityItem extends Entity
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityItem.class, DataSerializers.OPTIONAL_ITEM_STACK);

    /**
     * The age of this EntityItem (used to animate it up and down as well as expire it)
     */
    private int age;
    private int delayBeforeCanPickup;

    /** The health of this EntityItem. (For example, damage for tools) */
    private int health;
    private String thrower;
    private String owner;

    /** The EntityItem's random initial float height. */
    public float hoverStart;

    public EntityItem(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        health = 5;
        hoverStart = (float)(Math.random() * Math.PI * 2.0D);
        setSize(0.25F, 0.25F);
        setPosition(x, y, z);
        rotationYaw = (float)(Math.random() * 360.0D);
        motionX = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
        motionY = 0.20000000298023224D;
        motionZ = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
    }

    public EntityItem(World worldIn, double x, double y, double z, ItemStack stack)
    {
        this(worldIn, x, y, z);
        setEntityItemStack(stack);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public EntityItem(World worldIn)
    {
        super(worldIn);
        health = 5;
        hoverStart = (float)(Math.random() * Math.PI * 2.0D);
        setSize(0.25F, 0.25F);
        setEntityItemStack(ItemStack.EMPTY);
    }

    protected void entityInit()
    {
        getDataManager().register(ITEM, ItemStack.EMPTY);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (getEntityItem().isEmpty())
        {
            setDead();
        }
        else
        {
            super.onUpdate();

            if (delayBeforeCanPickup > 0 && delayBeforeCanPickup != 32767)
            {
                --delayBeforeCanPickup;
            }

            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;
            double d0 = motionX;
            double d1 = motionY;
            double d2 = motionZ;

            if (!hasNoGravity())
            {
                motionY -= 0.03999999910593033D;
            }

            if (world.isRemote)
            {
                noClip = false;
            }
            else
            {
                noClip = pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);
            }

            moveEntity(MoverType.SELF, motionX, motionY, motionZ);
            boolean flag = (int) prevPosX != (int) posX || (int) prevPosY != (int) posY || (int) prevPosZ != (int) posZ;

            if (flag || ticksExisted % 25 == 0)
            {
                if (world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA)
                {
                    motionY = 0.20000000298023224D;
                    motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
                    motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
                    playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + rand.nextFloat() * 0.4F);
                }

                if (!world.isRemote)
                {
                    searchForOtherItemsNearby();
                }
            }

            float f = 0.98F;

            if (onGround)
            {
                f = world.getBlockState(new BlockPos(MathHelper.floor(posX), MathHelper.floor(getEntityBoundingBox().minY) - 1, MathHelper.floor(posZ))).getBlock().slipperiness * 0.98F;
            }

            motionX *= f;
            motionY *= 0.9800000190734863D;
            motionZ *= f;

            if (onGround)
            {
                motionY *= -0.5D;
            }

            if (age != -32768)
            {
                ++age;
            }

            handleWaterMovement();

            if (!world.isRemote)
            {
                double d3 = motionX - d0;
                double d4 = motionY - d1;
                double d5 = motionZ - d2;
                double d6 = d3 * d3 + d4 * d4 + d5 * d5;

                if (d6 > 0.01D)
                {
                    isAirBorne = true;
                }
            }

            if (!world.isRemote && age >= 6000)
            {
                setDead();
            }
        }
    }

    /**
     * Looks for other itemstacks nearby and tries to stack them together
     */
    private void searchForOtherItemsNearby()
    {
        for (EntityItem entityitem : world.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().expand(0.5D, 0.0D, 0.5D)))
        {
            combineItems(entityitem);
        }
    }

    /**
     * Tries to merge this item with the item passed as the parameter. Returns true if successful. Either this item or
     * the other item will  be removed from the world.
     */
    private boolean combineItems(EntityItem other)
    {
        if (other == this)
        {
            return false;
        }
        else if (other.isEntityAlive() && isEntityAlive())
        {
            ItemStack itemstack = getEntityItem();
            ItemStack itemstack1 = other.getEntityItem();

            if (delayBeforeCanPickup != 32767 && other.delayBeforeCanPickup != 32767)
            {
                if (age != -32768 && other.age != -32768)
                {
                    if (itemstack1.getItem() != itemstack.getItem())
                    {
                        return false;
                    }
                    else if (itemstack1.hasTagCompound() ^ itemstack.hasTagCompound())
                    {
                        return false;
                    }
                    else if (itemstack1.hasTagCompound() && !itemstack1.getTagCompound().equals(itemstack.getTagCompound()))
                    {
                        return false;
                    }
                    else if (itemstack1.getItem() == null)
                    {
                        return false;
                    }
                    else if (itemstack1.getItem().getHasSubtypes() && itemstack1.getMetadata() != itemstack.getMetadata())
                    {
                        return false;
                    }
                    else if (itemstack1.getCount() < itemstack.getCount())
                    {
                        return other.combineItems(this);
                    }
                    else if (itemstack1.getCount() + itemstack.getCount() > itemstack1.getMaxStackSize())
                    {
                        return false;
                    }
                    else
                    {
                        itemstack1.func_190917_f(itemstack.getCount());
                        other.delayBeforeCanPickup = Math.max(other.delayBeforeCanPickup, delayBeforeCanPickup);
                        other.age = Math.min(other.age, age);
                        other.setEntityItemStack(itemstack1);
                        setDead();
                        return true;
                    }
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * sets the age of the item so that it'll despawn one minute after it has been dropped (instead of five). Used when
     * items are dropped from players in creative mode
     */
    public void setAgeToCreativeDespawnTime()
    {
        age = 4800;
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        if (world.handleMaterialAcceleration(getEntityBoundingBox(), Material.WATER, this))
        {
            if (!inWater && !firstUpdate)
            {
                resetHeight();
            }

            inWater = true;
        }
        else
        {
            inWater = false;
        }

        return inWater;
    }

    /**
     * Will deal the specified amount of fire damage to the entity if the entity isn't immune to fire damage.
     */
    protected void dealFireDamage(int amount)
    {
        attackEntityFrom(DamageSource.inFire, (float)amount);
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
        else if (!getEntityItem().isEmpty() && getEntityItem().getItem() == Items.NETHER_STAR && source.isExplosion())
        {
            return false;
        }
        else
        {
            setBeenAttacked();
            health = (int)((float) health - amount);

            if (health <= 0)
            {
                setDead();
            }

            return false;
        }
    }

    public static void registerFixesItem(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityItem.class, "Item"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setShort("Health", (short) health);
        compound.setShort("Age", (short) age);
        compound.setShort("PickupDelay", (short) delayBeforeCanPickup);

        if (getThrower() != null)
        {
            compound.setString("Thrower", thrower);
        }

        if (getOwner() != null)
        {
            compound.setString("Owner", owner);
        }

        if (!getEntityItem().isEmpty())
        {
            compound.setTag("Item", getEntityItem().writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        health = compound.getShort("Health");
        age = compound.getShort("Age");

        if (compound.hasKey("PickupDelay"))
        {
            delayBeforeCanPickup = compound.getShort("PickupDelay");
        }

        if (compound.hasKey("Owner"))
        {
            owner = compound.getString("Owner");
        }

        if (compound.hasKey("Thrower"))
        {
            thrower = compound.getString("Thrower");
        }

        NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");
        setEntityItemStack(new ItemStack(nbttagcompound));

        if (getEntityItem().isEmpty())
        {
            setDead();
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!world.isRemote)
        {
            ItemStack itemstack = getEntityItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            if (delayBeforeCanPickup == 0 && (owner == null || 6000 - age <= 200 || owner.equals(entityIn.getName())) && entityIn.inventory.addItemStackToInventory(itemstack))
            {
                entityIn.onItemPickup(this, i);

                if (itemstack.isEmpty())
                {
                    setDead();
                    itemstack.func_190920_e(i);
                }

                entityIn.addStat(StatList.getObjectsPickedUpStats(item), i);
            }
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return hasCustomName() ? getCustomNameTag() : I18n.translateToLocal("item." + getEntityItem().getUnlocalizedName());
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    @Nullable
    public Entity changeDimension(int dimensionIn)
    {
        Entity entity = super.changeDimension(dimensionIn);

        if (!world.isRemote && entity instanceof EntityItem)
        {
            ((EntityItem)entity).searchForOtherItemsNearby();
        }

        return entity;
    }

    /**
     * Returns the ItemStack corresponding to the Entity (Note: if no item exists, will log an error but still return an
     * ItemStack containing Block.stone)
     */
    public ItemStack getEntityItem()
    {
        return getDataManager().get(ITEM);
    }
    public ItemStack getItem() {
        return getDataManager().get(ITEM);
    }

    /**
     * Sets the ItemStack for this entity
     */
    public void setEntityItemStack(ItemStack stack)
    {
        getDataManager().set(ITEM, stack);
        getDataManager().setDirty(ITEM);
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getThrower()
    {
        return thrower;
    }

    public void setThrower(String thrower)
    {
        this.thrower = thrower;
    }

    public int getAge()
    {
        return age;
    }

    public void setDefaultPickupDelay()
    {
        delayBeforeCanPickup = 10;
    }

    public void setNoPickupDelay()
    {
        delayBeforeCanPickup = 0;
    }

    public void setInfinitePickupDelay()
    {
        delayBeforeCanPickup = 32767;
    }

    public void setPickupDelay(int ticks)
    {
        delayBeforeCanPickup = ticks;
    }

    public boolean cannotPickup()
    {
        return delayBeforeCanPickup > 0;
    }

    public void setNoDespawn()
    {
        age = -6000;
    }

    public void makeFakeItem()
    {
        setInfinitePickupDelay();
        age = 5999;
    }
}
