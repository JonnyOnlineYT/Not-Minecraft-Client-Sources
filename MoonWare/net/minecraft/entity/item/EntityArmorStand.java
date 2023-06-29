package net.minecraft.entity.item;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityArmorStand extends EntityLivingBase
{
    private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
    private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
    private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
    private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
    public static final DataParameter<Byte> STATUS = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.BYTE);
    public static final DataParameter<Rotations> HEAD_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    public static final DataParameter<Rotations> BODY_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    public static final DataParameter<Rotations> LEFT_ARM_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    public static final DataParameter<Rotations> RIGHT_ARM_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    public static final DataParameter<Rotations> LEFT_LEG_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    public static final DataParameter<Rotations> RIGHT_LEG_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    private static final Predicate<Entity> IS_RIDEABLE_MINECART = new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_ instanceof EntityMinecart && ((EntityMinecart)p_apply_1_).getType() == EntityMinecart.Type.RIDEABLE;
        }
    };
    private final NonNullList<ItemStack> handItems;
    private final NonNullList<ItemStack> armorItems;
    private boolean canInteract;

    /**
     * After punching the stand, the cooldown before you can punch it again without breaking it.
     */
    public long punchCooldown;
    private int disabledSlots;
    private boolean wasMarker;
    private Rotations headRotation;
    private Rotations bodyRotation;
    private Rotations leftArmRotation;
    private Rotations rightArmRotation;
    private Rotations leftLegRotation;
    private Rotations rightLegRotation;

    public EntityArmorStand(World worldIn)
    {
        super(worldIn);
        handItems = NonNullList.func_191197_a(2, ItemStack.EMPTY);
        armorItems = NonNullList.func_191197_a(4, ItemStack.EMPTY);
        headRotation = DEFAULT_HEAD_ROTATION;
        bodyRotation = DEFAULT_BODY_ROTATION;
        leftArmRotation = DEFAULT_LEFTARM_ROTATION;
        rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
        leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
        rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
        noClip = hasNoGravity();
        setSize(0.5F, 1.975F);
    }

    public EntityArmorStand(World worldIn, double posX, double posY, double posZ)
    {
        this(worldIn);
        setPosition(posX, posY, posZ);
    }

    /**
     * Sets the width and height of the entity.
     */
    protected final void setSize(float width, float height)
    {
        double d0 = posX;
        double d1 = posY;
        double d2 = posZ;
        float f = hasMarker() ? 0.0F : (isChild() ? 0.5F : 1.0F);
        super.setSize(width * f, height * f);
        setPosition(d0, d1, d2);
    }

    /**
     * Returns whether the entity is in a server world
     */
    public boolean isServerWorld()
    {
        return super.isServerWorld() && !hasNoGravity();
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(STATUS, Byte.valueOf((byte)0));
        dataManager.register(HEAD_ROTATION, DEFAULT_HEAD_ROTATION);
        dataManager.register(BODY_ROTATION, DEFAULT_BODY_ROTATION);
        dataManager.register(LEFT_ARM_ROTATION, DEFAULT_LEFTARM_ROTATION);
        dataManager.register(RIGHT_ARM_ROTATION, DEFAULT_RIGHTARM_ROTATION);
        dataManager.register(LEFT_LEG_ROTATION, DEFAULT_LEFTLEG_ROTATION);
        dataManager.register(RIGHT_LEG_ROTATION, DEFAULT_RIGHTLEG_ROTATION);
    }

    public Iterable<ItemStack> getHeldEquipment()
    {
        return handItems;
    }

    public Iterable<ItemStack> getArmorInventoryList()
    {
        return armorItems;
    }

    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn)
    {
        switch (slotIn.getSlotType())
        {
            case HAND:
                return handItems.get(slotIn.getIndex());

            case ARMOR:
                return armorItems.get(slotIn.getIndex());

            default:
                return ItemStack.EMPTY;
        }
    }

    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
    {
        switch (slotIn.getSlotType())
        {
            case HAND:
                playEquipSound(stack);
                handItems.set(slotIn.getIndex(), stack);
                break;

            case ARMOR:
                playEquipSound(stack);
                armorItems.set(slotIn.getIndex(), stack);
        }
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        EntityEquipmentSlot entityequipmentslot;

        if (inventorySlot == 98)
        {
            entityequipmentslot = EntityEquipmentSlot.MAINHAND;
        }
        else if (inventorySlot == 99)
        {
            entityequipmentslot = EntityEquipmentSlot.OFFHAND;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.HEAD.getIndex())
        {
            entityequipmentslot = EntityEquipmentSlot.HEAD;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.CHEST.getIndex())
        {
            entityequipmentslot = EntityEquipmentSlot.CHEST;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.LEGS.getIndex())
        {
            entityequipmentslot = EntityEquipmentSlot.LEGS;
        }
        else
        {
            if (inventorySlot != 100 + EntityEquipmentSlot.FEET.getIndex())
            {
                return false;
            }

            entityequipmentslot = EntityEquipmentSlot.FEET;
        }

        if (!itemStackIn.isEmpty() && !EntityLiving.isItemStackInSlot(entityequipmentslot, itemStackIn) && entityequipmentslot != EntityEquipmentSlot.HEAD)
        {
            return false;
        }
        else
        {
            setItemStackToSlot(entityequipmentslot, itemStackIn);
            return true;
        }
    }

    public static void registerFixesArmorStand(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityArmorStand.class, "ArmorItems", "HandItems"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        NBTTagList nbttaglist = new NBTTagList();

        for (ItemStack itemstack : armorItems)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            if (!itemstack.isEmpty())
            {
                itemstack.writeToNBT(nbttagcompound);
            }

            nbttaglist.appendTag(nbttagcompound);
        }

        compound.setTag("ArmorItems", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (ItemStack itemstack1 : handItems)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            if (!itemstack1.isEmpty())
            {
                itemstack1.writeToNBT(nbttagcompound1);
            }

            nbttaglist1.appendTag(nbttagcompound1);
        }

        compound.setTag("HandItems", nbttaglist1);
        compound.setBoolean("Invisible", isInvisible());
        compound.setBoolean("Small", isSmall());
        compound.setBoolean("ShowArms", getShowArms());
        compound.setInteger("DisabledSlots", disabledSlots);
        compound.setBoolean("NoBasePlate", hasNoBasePlate());

        if (hasMarker())
        {
            compound.setBoolean("Marker", hasMarker());
        }

        compound.setTag("Pose", readPoseFromNBT());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("ArmorItems", 9))
        {
            NBTTagList nbttaglist = compound.getTagList("ArmorItems", 10);

            for (int i = 0; i < armorItems.size(); ++i)
            {
                armorItems.set(i, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
        }

        if (compound.hasKey("HandItems", 9))
        {
            NBTTagList nbttaglist1 = compound.getTagList("HandItems", 10);

            for (int j = 0; j < handItems.size(); ++j)
            {
                handItems.set(j, new ItemStack(nbttaglist1.getCompoundTagAt(j)));
            }
        }

        setInvisible(compound.getBoolean("Invisible"));
        setSmall(compound.getBoolean("Small"));
        setShowArms(compound.getBoolean("ShowArms"));
        disabledSlots = compound.getInteger("DisabledSlots");
        setNoBasePlate(compound.getBoolean("NoBasePlate"));
        setMarker(compound.getBoolean("Marker"));
        wasMarker = !hasMarker();
        noClip = hasNoGravity();
        NBTTagCompound nbttagcompound = compound.getCompoundTag("Pose");
        writePoseToNBT(nbttagcompound);
    }

    /**
     * Saves the pose to an NBTTagCompound.
     */
    private void writePoseToNBT(NBTTagCompound tagCompound)
    {
        NBTTagList nbttaglist = tagCompound.getTagList("Head", 5);
        setHeadRotation(nbttaglist.hasNoTags() ? DEFAULT_HEAD_ROTATION : new Rotations(nbttaglist));
        NBTTagList nbttaglist1 = tagCompound.getTagList("Body", 5);
        setBodyRotation(nbttaglist1.hasNoTags() ? DEFAULT_BODY_ROTATION : new Rotations(nbttaglist1));
        NBTTagList nbttaglist2 = tagCompound.getTagList("LeftArm", 5);
        setLeftArmRotation(nbttaglist2.hasNoTags() ? DEFAULT_LEFTARM_ROTATION : new Rotations(nbttaglist2));
        NBTTagList nbttaglist3 = tagCompound.getTagList("RightArm", 5);
        setRightArmRotation(nbttaglist3.hasNoTags() ? DEFAULT_RIGHTARM_ROTATION : new Rotations(nbttaglist3));
        NBTTagList nbttaglist4 = tagCompound.getTagList("LeftLeg", 5);
        setLeftLegRotation(nbttaglist4.hasNoTags() ? DEFAULT_LEFTLEG_ROTATION : new Rotations(nbttaglist4));
        NBTTagList nbttaglist5 = tagCompound.getTagList("RightLeg", 5);
        setRightLegRotation(nbttaglist5.hasNoTags() ? DEFAULT_RIGHTLEG_ROTATION : new Rotations(nbttaglist5));
    }

    private NBTTagCompound readPoseFromNBT()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (!DEFAULT_HEAD_ROTATION.equals(headRotation))
        {
            nbttagcompound.setTag("Head", headRotation.writeToNBT());
        }

        if (!DEFAULT_BODY_ROTATION.equals(bodyRotation))
        {
            nbttagcompound.setTag("Body", bodyRotation.writeToNBT());
        }

        if (!DEFAULT_LEFTARM_ROTATION.equals(leftArmRotation))
        {
            nbttagcompound.setTag("LeftArm", leftArmRotation.writeToNBT());
        }

        if (!DEFAULT_RIGHTARM_ROTATION.equals(rightArmRotation))
        {
            nbttagcompound.setTag("RightArm", rightArmRotation.writeToNBT());
        }

        if (!DEFAULT_LEFTLEG_ROTATION.equals(leftLegRotation))
        {
            nbttagcompound.setTag("LeftLeg", leftLegRotation.writeToNBT());
        }

        if (!DEFAULT_RIGHTLEG_ROTATION.equals(rightLegRotation))
        {
            nbttagcompound.setTag("RightLeg", rightLegRotation.writeToNBT());
        }

        return nbttagcompound;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return false;
    }

    protected void collideWithEntity(Entity entityIn)
    {
    }

    protected void collideWithNearbyEntities()
    {
        List<Entity> list = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox(), IS_RIDEABLE_MINECART);

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity = list.get(i);

            if (getDistanceSqToEntity(entity) <= 0.2D)
            {
                entity.applyEntityCollision(this);
            }
        }
    }

    /**
     * Applies the given player interaction to this Entity.
     */
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand stack)
    {
        ItemStack itemstack = player.getHeldItem(stack);

        if (!hasMarker() && itemstack.getItem() != Items.NAME_TAG)
        {
            if (!world.isRemote && !player.isSpectator())
            {
                EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);

                if (itemstack.isEmpty())
                {
                    EntityEquipmentSlot entityequipmentslot1 = func_190772_a(vec);
                    EntityEquipmentSlot entityequipmentslot2 = isDisabled(entityequipmentslot1) ? entityequipmentslot : entityequipmentslot1;

                    if (func_190630_a(entityequipmentslot2))
                    {
                        swapItem(player, entityequipmentslot2, itemstack, stack);
                    }
                }
                else
                {
                    if (isDisabled(entityequipmentslot))
                    {
                        return EnumActionResult.FAIL;
                    }

                    if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.HAND && !getShowArms())
                    {
                        return EnumActionResult.FAIL;
                    }

                    swapItem(player, entityequipmentslot, itemstack, stack);
                }

                return EnumActionResult.SUCCESS;
            }
            else
            {
                return EnumActionResult.SUCCESS;
            }
        }
        else
        {
            return EnumActionResult.PASS;
        }
    }

    protected EntityEquipmentSlot func_190772_a(Vec3d p_190772_1_)
    {
        EntityEquipmentSlot entityequipmentslot = EntityEquipmentSlot.MAINHAND;
        boolean flag = isSmall();
        double d0 = flag ? p_190772_1_.yCoord * 2.0D : p_190772_1_.yCoord;
        EntityEquipmentSlot entityequipmentslot1 = EntityEquipmentSlot.FEET;

        if (d0 >= 0.1D && d0 < 0.1D + (flag ? 0.8D : 0.45D) && func_190630_a(entityequipmentslot1))
        {
            entityequipmentslot = EntityEquipmentSlot.FEET;
        }
        else if (d0 >= 0.9D + (flag ? 0.3D : 0.0D) && d0 < 0.9D + (flag ? 1.0D : 0.7D) && func_190630_a(EntityEquipmentSlot.CHEST))
        {
            entityequipmentslot = EntityEquipmentSlot.CHEST;
        }
        else if (d0 >= 0.4D && d0 < 0.4D + (flag ? 1.0D : 0.8D) && func_190630_a(EntityEquipmentSlot.LEGS))
        {
            entityequipmentslot = EntityEquipmentSlot.LEGS;
        }
        else if (d0 >= 1.6D && func_190630_a(EntityEquipmentSlot.HEAD))
        {
            entityequipmentslot = EntityEquipmentSlot.HEAD;
        }

        return entityequipmentslot;
    }

    private boolean isDisabled(EntityEquipmentSlot slotIn)
    {
        return (disabledSlots & 1 << slotIn.getSlotIndex()) != 0;
    }

    private void swapItem(EntityPlayer player, EntityEquipmentSlot p_184795_2_, ItemStack p_184795_3_, EnumHand hand)
    {
        ItemStack itemstack = getItemStackFromSlot(p_184795_2_);

        if (itemstack.isEmpty() || (disabledSlots & 1 << p_184795_2_.getSlotIndex() + 8) == 0)
        {
            if (!itemstack.isEmpty() || (disabledSlots & 1 << p_184795_2_.getSlotIndex() + 16) == 0)
            {
                if (player.capabilities.isCreativeMode && itemstack.isEmpty() && !p_184795_3_.isEmpty())
                {
                    ItemStack itemstack2 = p_184795_3_.copy();
                    itemstack2.func_190920_e(1);
                    setItemStackToSlot(p_184795_2_, itemstack2);
                }
                else if (!p_184795_3_.isEmpty() && p_184795_3_.getCount() > 1)
                {
                    if (itemstack.isEmpty())
                    {
                        ItemStack itemstack1 = p_184795_3_.copy();
                        itemstack1.func_190920_e(1);
                        setItemStackToSlot(p_184795_2_, itemstack1);
                        p_184795_3_.func_190918_g(1);
                    }
                }
                else
                {
                    setItemStackToSlot(p_184795_2_, p_184795_3_);
                    player.setHeldItem(hand, itemstack);
                }
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!world.isRemote && !isDead)
        {
            if (DamageSource.outOfWorld.equals(source))
            {
                setDead();
                return false;
            }
            else if (!isEntityInvulnerable(source) && !canInteract && !hasMarker())
            {
                if (source.isExplosion())
                {
                    dropContents();
                    setDead();
                    return false;
                }
                else if (DamageSource.inFire.equals(source))
                {
                    if (isBurning())
                    {
                        damageArmorStand(0.15F);
                    }
                    else
                    {
                        setFire(5);
                    }

                    return false;
                }
                else if (DamageSource.onFire.equals(source) && getHealth() > 0.5F)
                {
                    damageArmorStand(4.0F);
                    return false;
                }
                else
                {
                    boolean flag = "arrow".equals(source.getDamageType());
                    boolean flag1 = "player".equals(source.getDamageType());

                    if (!flag1 && !flag)
                    {
                        return false;
                    }
                    else
                    {
                        if (source.getSourceOfDamage() instanceof EntityArrow)
                        {
                            source.getSourceOfDamage().setDead();
                        }

                        if (source.getEntity() instanceof EntityPlayer && !((EntityPlayer)source.getEntity()).capabilities.allowEdit)
                        {
                            return false;
                        }
                        else if (source.isCreativePlayer())
                        {
                            func_190773_I();
                            playParticles();
                            setDead();
                            return false;
                        }
                        else
                        {
                            long i = world.getTotalWorldTime();

                            if (i - punchCooldown > 5L && !flag)
                            {
                                world.setEntityState(this, (byte)32);
                                punchCooldown = i;
                            }
                            else
                            {
                                dropBlock();
                                playParticles();
                                setDead();
                            }

                            return false;
                        }
                    }
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

    public void handleStatusUpdate(byte id)
    {
        if (id == 32)
        {
            if (world.isRemote)
            {
                world.playSound(posX, posY, posZ, SoundEvents.ENTITY_ARMORSTAND_HIT, getSoundCategory(), 0.3F, 1.0F, false);
                punchCooldown = world.getTotalWorldTime();
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

        if (Double.isNaN(d0) || d0 == 0.0D)
        {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return distance < d0 * d0;
    }

    private void playParticles()
    {
        if (world instanceof WorldServer)
        {
            ((WorldServer) world).spawnParticle(EnumParticleTypes.BLOCK_DUST, posX, posY + (double) height / 1.5D, posZ, 10, width / 4.0F, height / 4.0F, width / 4.0F, 0.05D, Block.getStateId(Blocks.PLANKS.getDefaultState()));
        }
    }

    private void damageArmorStand(float damage)
    {
        float f = getHealth();
        f = f - damage;

        if (f <= 0.5F)
        {
            dropContents();
            setDead();
        }
        else
        {
            setHealth(f);
        }
    }

    private void dropBlock()
    {
        Block.spawnAsEntity(world, new BlockPos(this), new ItemStack(Items.ARMOR_STAND));
        dropContents();
    }

    private void dropContents()
    {
        func_190773_I();

        for (int i = 0; i < handItems.size(); ++i)
        {
            ItemStack itemstack = handItems.get(i);

            if (!itemstack.isEmpty())
            {
                Block.spawnAsEntity(world, (new BlockPos(this)).up(), itemstack);
                handItems.set(i, ItemStack.EMPTY);
            }
        }

        for (int j = 0; j < armorItems.size(); ++j)
        {
            ItemStack itemstack1 = armorItems.get(j);

            if (!itemstack1.isEmpty())
            {
                Block.spawnAsEntity(world, (new BlockPos(this)).up(), itemstack1);
                armorItems.set(j, ItemStack.EMPTY);
            }
        }
    }

    private void func_190773_I()
    {
        world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_ARMORSTAND_BREAK, getSoundCategory(), 1.0F, 1.0F);
    }

    protected float updateDistance(float p_110146_1_, float p_110146_2_)
    {
        prevRenderYawOffset = prevRotationYaw;
        renderYawOffset = rotationYaw;
        return 0.0F;
    }

    public float getEyeHeight()
    {
        return isChild() ? height * 0.5F : height * 0.9F;
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return hasMarker() ? 0.0D : 0.10000000149011612D;
    }

    public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_)
    {
        if (!hasNoGravity())
        {
            super.func_191986_a(p_191986_1_, p_191986_2_, p_191986_3_);
        }
    }

    /**
     * Set the render yaw offset
     */
    public void setRenderYawOffset(float offset)
    {
        prevRenderYawOffset = prevRotationYaw = offset;
        prevRotationYawHead = rotationYawHead = offset;
    }

    /**
     * Sets the head's yaw rotation of the entity.
     */
    public void setRotationYawHead(float rotation)
    {
        prevRenderYawOffset = prevRotationYaw = rotation;
        prevRotationYawHead = rotationYawHead = rotation;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        Rotations rotations = dataManager.get(HEAD_ROTATION);

        if (!headRotation.equals(rotations))
        {
            setHeadRotation(rotations);
        }

        Rotations rotations1 = dataManager.get(BODY_ROTATION);

        if (!bodyRotation.equals(rotations1))
        {
            setBodyRotation(rotations1);
        }

        Rotations rotations2 = dataManager.get(LEFT_ARM_ROTATION);

        if (!leftArmRotation.equals(rotations2))
        {
            setLeftArmRotation(rotations2);
        }

        Rotations rotations3 = dataManager.get(RIGHT_ARM_ROTATION);

        if (!rightArmRotation.equals(rotations3))
        {
            setRightArmRotation(rotations3);
        }

        Rotations rotations4 = dataManager.get(LEFT_LEG_ROTATION);

        if (!leftLegRotation.equals(rotations4))
        {
            setLeftLegRotation(rotations4);
        }

        Rotations rotations5 = dataManager.get(RIGHT_LEG_ROTATION);

        if (!rightLegRotation.equals(rotations5))
        {
            setRightLegRotation(rotations5);
        }

        boolean flag = hasMarker();

        if (wasMarker != flag)
        {
            updateBoundingBox(flag);
            preventEntitySpawning = !flag;
            wasMarker = flag;
        }
    }

    private void updateBoundingBox(boolean p_181550_1_)
    {
        if (p_181550_1_)
        {
            setSize(0.0F, 0.0F);
        }
        else
        {
            setSize(0.5F, 1.975F);
        }
    }

    /**
     * Clears potion metadata values if the entity has no potion effects. Otherwise, updates potion effect color,
     * ambience, and invisibility metadata values
     */
    protected void updatePotionMetadata()
    {
        setInvisible(canInteract);
    }

    public void setInvisible(boolean invisible)
    {
        canInteract = invisible;
        super.setInvisible(invisible);
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return isSmall();
    }

    /**
     * Called by the /kill command.
     */
    public void onKillCommand()
    {
        setDead();
    }

    public boolean isImmuneToExplosions()
    {
        return isInvisible();
    }

    public EnumPushReaction getPushReaction()
    {
        return hasMarker() ? EnumPushReaction.IGNORE : super.getPushReaction();
    }

    private void setSmall(boolean small)
    {
        dataManager.set(STATUS, Byte.valueOf(setBit(dataManager.get(STATUS).byteValue(), 1, small)));
        setSize(0.5F, 1.975F);
    }

    public boolean isSmall()
    {
        return (dataManager.get(STATUS).byteValue() & 1) != 0;
    }

    private void setShowArms(boolean showArms)
    {
        dataManager.set(STATUS, Byte.valueOf(setBit(dataManager.get(STATUS).byteValue(), 4, showArms)));
    }

    public boolean getShowArms()
    {
        return (dataManager.get(STATUS).byteValue() & 4) != 0;
    }

    private void setNoBasePlate(boolean noBasePlate)
    {
        dataManager.set(STATUS, Byte.valueOf(setBit(dataManager.get(STATUS).byteValue(), 8, noBasePlate)));
    }

    public boolean hasNoBasePlate()
    {
        return (dataManager.get(STATUS).byteValue() & 8) != 0;
    }

    /**
     * Marker defines where if true, the size is 0 and will not be rendered or intractable.
     */
    private void setMarker(boolean marker)
    {
        dataManager.set(STATUS, Byte.valueOf(setBit(dataManager.get(STATUS).byteValue(), 16, marker)));
        setSize(0.5F, 1.975F);
    }

    /**
     * Gets whether the armor stand has marker enabled. If true, the armor stand's bounding box is set to zero and
     * cannot be interacted with.
     */
    public boolean hasMarker()
    {
        return (dataManager.get(STATUS).byteValue() & 16) != 0;
    }

    private byte setBit(byte p_184797_1_, int p_184797_2_, boolean p_184797_3_)
    {
        if (p_184797_3_)
        {
            p_184797_1_ = (byte)(p_184797_1_ | p_184797_2_);
        }
        else
        {
            p_184797_1_ = (byte)(p_184797_1_ & ~p_184797_2_);
        }

        return p_184797_1_;
    }

    public void setHeadRotation(Rotations vec)
    {
        headRotation = vec;
        dataManager.set(HEAD_ROTATION, vec);
    }

    public void setBodyRotation(Rotations vec)
    {
        bodyRotation = vec;
        dataManager.set(BODY_ROTATION, vec);
    }

    public void setLeftArmRotation(Rotations vec)
    {
        leftArmRotation = vec;
        dataManager.set(LEFT_ARM_ROTATION, vec);
    }

    public void setRightArmRotation(Rotations vec)
    {
        rightArmRotation = vec;
        dataManager.set(RIGHT_ARM_ROTATION, vec);
    }

    public void setLeftLegRotation(Rotations vec)
    {
        leftLegRotation = vec;
        dataManager.set(LEFT_LEG_ROTATION, vec);
    }

    public void setRightLegRotation(Rotations vec)
    {
        rightLegRotation = vec;
        dataManager.set(RIGHT_LEG_ROTATION, vec);
    }

    public Rotations getHeadRotation()
    {
        return headRotation;
    }

    public Rotations getBodyRotation()
    {
        return bodyRotation;
    }

    public Rotations getLeftArmRotation()
    {
        return leftArmRotation;
    }

    public Rotations getRightArmRotation()
    {
        return rightArmRotation;
    }

    public Rotations getLeftLegRotation()
    {
        return leftLegRotation;
    }

    public Rotations getRightLegRotation()
    {
        return rightLegRotation;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return super.canBeCollidedWith() && !hasMarker();
    }

    public EnumHandSide getPrimaryHand()
    {
        return EnumHandSide.RIGHT;
    }

    protected SoundEvent getFallSound(int heightIn)
    {
        return SoundEvents.ENTITY_ARMORSTAND_FALL;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_ARMORSTAND_HIT;
    }

    @Nullable
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ARMORSTAND_BREAK;
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
    }

    /**
     * Returns false if the entity is an armor stand. Returns true for all other entity living bases.
     */
    public boolean canBeHitWithPotion()
    {
        return false;
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (STATUS.equals(key))
        {
            setSize(0.5F, 1.975F);
        }

        super.notifyDataManagerChange(key);
    }

    public boolean func_190631_cK()
    {
        return false;
    }
}
