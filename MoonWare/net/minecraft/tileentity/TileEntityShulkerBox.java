package net.minecraft.tileentity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityShulkerBox extends TileEntityLockableLoot implements ITickable, ISidedInventory
{
    private static final int[] field_190595_a = new int[27];
    private NonNullList<ItemStack> field_190596_f;
    private boolean field_190597_g;
    private int field_190598_h;
    private TileEntityShulkerBox.AnimationStatus field_190599_i;
    private float field_190600_j;
    private float field_190601_k;
    private EnumDyeColor field_190602_l;
    private boolean field_190594_p;

    public TileEntityShulkerBox()
    {
        this(null);
    }

    public TileEntityShulkerBox(@Nullable EnumDyeColor p_i47242_1_)
    {
        field_190596_f = NonNullList.func_191197_a(27, ItemStack.EMPTY);
        field_190599_i = TileEntityShulkerBox.AnimationStatus.CLOSED;
        field_190602_l = p_i47242_1_;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        func_190583_o();

        if (field_190599_i == TileEntityShulkerBox.AnimationStatus.OPENING || field_190599_i == TileEntityShulkerBox.AnimationStatus.CLOSING)
        {
            func_190589_G();
        }
    }

    protected void func_190583_o()
    {
        field_190601_k = field_190600_j;

        switch (field_190599_i)
        {
            case CLOSED:
                field_190600_j = 0.0F;
                break;

            case OPENING:
                field_190600_j += 0.1F;

                if (field_190600_j >= 1.0F)
                {
                    func_190589_G();
                    field_190599_i = TileEntityShulkerBox.AnimationStatus.OPENED;
                    field_190600_j = 1.0F;
                }

                break;

            case CLOSING:
                field_190600_j -= 0.1F;

                if (field_190600_j <= 0.0F)
                {
                    field_190599_i = TileEntityShulkerBox.AnimationStatus.CLOSED;
                    field_190600_j = 0.0F;
                }

                break;

            case OPENED:
                field_190600_j = 1.0F;
        }
    }

    public TileEntityShulkerBox.AnimationStatus func_190591_p()
    {
        return field_190599_i;
    }

    public AxisAlignedBB func_190584_a(IBlockState p_190584_1_)
    {
        return func_190587_b(p_190584_1_.getValue(BlockShulkerBox.field_190957_a));
    }

    public AxisAlignedBB func_190587_b(EnumFacing p_190587_1_)
    {
        return Block.FULL_BLOCK_AABB.addCoord(0.5F * func_190585_a(1.0F) * (float)p_190587_1_.getFrontOffsetX(), 0.5F * func_190585_a(1.0F) * (float)p_190587_1_.getFrontOffsetY(), 0.5F * func_190585_a(1.0F) * (float)p_190587_1_.getFrontOffsetZ());
    }

    private AxisAlignedBB func_190588_c(EnumFacing p_190588_1_)
    {
        EnumFacing enumfacing = p_190588_1_.getOpposite();
        return func_190587_b(p_190588_1_).func_191195_a(enumfacing.getFrontOffsetX(), enumfacing.getFrontOffsetY(), enumfacing.getFrontOffsetZ());
    }

    private void func_190589_G()
    {
        IBlockState iblockstate = world.getBlockState(getPos());

        if (iblockstate.getBlock() instanceof BlockShulkerBox)
        {
            EnumFacing enumfacing = iblockstate.getValue(BlockShulkerBox.field_190957_a);
            AxisAlignedBB axisalignedbb = func_190588_c(enumfacing).offset(pos);
            List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);

            if (!list.isEmpty())
            {
                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity = list.get(i);

                    if (entity.getPushReaction() != EnumPushReaction.IGNORE)
                    {
                        double d0 = 0.0D;
                        double d1 = 0.0D;
                        double d2 = 0.0D;
                        AxisAlignedBB axisalignedbb1 = entity.getEntityBoundingBox();

                        switch (enumfacing.getAxis())
                        {
                            case X:
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                                {
                                    d0 = axisalignedbb.maxX - axisalignedbb1.minX;
                                }
                                else
                                {
                                    d0 = axisalignedbb1.maxX - axisalignedbb.minX;
                                }

                                d0 = d0 + 0.01D;
                                break;

                            case Y:
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                                {
                                    d1 = axisalignedbb.maxY - axisalignedbb1.minY;
                                }
                                else
                                {
                                    d1 = axisalignedbb1.maxY - axisalignedbb.minY;
                                }

                                d1 = d1 + 0.01D;
                                break;

                            case Z:
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                                {
                                    d2 = axisalignedbb.maxZ - axisalignedbb1.minZ;
                                }
                                else
                                {
                                    d2 = axisalignedbb1.maxZ - axisalignedbb.minZ;
                                }

                                d2 = d2 + 0.01D;
                        }

                        entity.moveEntity(MoverType.SHULKER_BOX, d0 * (double)enumfacing.getFrontOffsetX(), d1 * (double)enumfacing.getFrontOffsetY(), d2 * (double)enumfacing.getFrontOffsetZ());
                    }
                }
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return field_190596_f.size();
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            field_190598_h = type;

            if (type == 0)
            {
                field_190599_i = TileEntityShulkerBox.AnimationStatus.CLOSING;
            }

            if (type == 1)
            {
                field_190599_i = TileEntityShulkerBox.AnimationStatus.OPENING;
            }

            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (field_190598_h < 0)
            {
                field_190598_h = 0;
            }

            ++field_190598_h;
            world.addBlockEvent(pos, getBlockType(), 1, field_190598_h);

            if (field_190598_h == 1)
            {
                world.playSound(null, pos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            --field_190598_h;
            world.addBlockEvent(pos, getBlockType(), 1, field_190598_h);

            if (field_190598_h <= 0)
            {
                world.playSound(null, pos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerShulkerBox(playerInventory, this, playerIn);
    }

    public String getGuiID()
    {
        return "minecraft:shulker_box";
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return hasCustomName() ? field_190577_o : "container.shulkerBox";
    }

    public static void func_190593_a(DataFixer p_190593_0_)
    {
        p_190593_0_.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityShulkerBox.class, "Items"));
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        func_190586_e(compound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        return func_190580_f(compound);
    }

    public void func_190586_e(NBTTagCompound p_190586_1_)
    {
        field_190596_f = NonNullList.func_191197_a(getSizeInventory(), ItemStack.EMPTY);

        if (!checkLootAndRead(p_190586_1_) && p_190586_1_.hasKey("Items", 9))
        {
            ItemStackHelper.func_191283_b(p_190586_1_, field_190596_f);
        }

        if (p_190586_1_.hasKey("CustomName", 8))
        {
            field_190577_o = p_190586_1_.getString("CustomName");
        }
    }

    public NBTTagCompound func_190580_f(NBTTagCompound p_190580_1_)
    {
        if (!checkLootAndWrite(p_190580_1_))
        {
            ItemStackHelper.func_191281_a(p_190580_1_, field_190596_f, false);
        }

        if (hasCustomName())
        {
            p_190580_1_.setString("CustomName", field_190577_o);
        }

        if (!p_190580_1_.hasKey("Lock") && isLocked())
        {
            getLockCode().toNBT(p_190580_1_);
        }

        return p_190580_1_;
    }

    protected NonNullList<ItemStack> func_190576_q()
    {
        return field_190596_f;
    }

    public boolean func_191420_l()
    {
        for (ItemStack itemstack : field_190596_f)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public int[] getSlotsForFace(EnumFacing side)
    {
        return field_190595_a;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return !(Block.getBlockFromItem(itemStackIn.getItem()) instanceof BlockShulkerBox);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return true;
    }

    public void clear()
    {
        field_190597_g = true;
        super.clear();
    }

    public boolean func_190590_r()
    {
        return field_190597_g;
    }

    public float func_190585_a(float p_190585_1_)
    {
        return field_190601_k + (field_190600_j - field_190601_k) * p_190585_1_;
    }

    public EnumDyeColor func_190592_s()
    {
        if (field_190602_l == null)
        {
            field_190602_l = BlockShulkerBox.func_190954_c(getBlockType());
        }

        return field_190602_l;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 10, getUpdateTag());
    }

    public boolean func_190581_E()
    {
        return field_190594_p;
    }

    public void func_190579_a(boolean p_190579_1_)
    {
        field_190594_p = p_190579_1_;
    }

    public boolean func_190582_F()
    {
        return !func_190581_E() || !func_191420_l() || hasCustomName() || lootTable != null;
    }

    static
    {
        for (int i = 0; i < field_190595_a.length; field_190595_a[i] = i++)
        {
        }
    }

    public enum AnimationStatus
    {
        CLOSED,
        OPENING,
        OPENED,
        CLOSING
    }
}
