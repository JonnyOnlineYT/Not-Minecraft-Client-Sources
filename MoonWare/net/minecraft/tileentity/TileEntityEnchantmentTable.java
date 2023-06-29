package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.IInteractionObject;

public class TileEntityEnchantmentTable extends TileEntity implements ITickable, IInteractionObject
{
    public int tickCount;
    public float pageFlip;
    public float pageFlipPrev;
    public float flipT;
    public float flipA;
    public float bookSpread;
    public float bookSpreadPrev;
    public float bookRotation;
    public float bookRotationPrev;
    public float tRot;
    private static final Random rand = new Random();
    private String customName;

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (hasCustomName())
        {
            compound.setString("CustomName", customName);
        }

        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("CustomName", 8))
        {
            customName = compound.getString("CustomName");
        }
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        bookSpreadPrev = bookSpread;
        bookRotationPrev = bookRotation;
        EntityPlayer entityplayer = world.getClosestPlayer((float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, (float) pos.getZ() + 0.5F, 3.0D, false);

        if (entityplayer != null)
        {
            double d0 = entityplayer.posX - (double)((float) pos.getX() + 0.5F);
            double d1 = entityplayer.posZ - (double)((float) pos.getZ() + 0.5F);
            tRot = (float)MathHelper.atan2(d1, d0);
            bookSpread += 0.1F;

            if (bookSpread < 0.5F || rand.nextInt(40) == 0)
            {
                float f1 = flipT;

                while (true)
                {
                    flipT += (float)(rand.nextInt(4) - rand.nextInt(4));

                    if (f1 != flipT)
                    {
                        break;
                    }
                }
            }
        }
        else
        {
            tRot += 0.02F;
            bookSpread -= 0.1F;
        }

        while (bookRotation >= (float)Math.PI)
        {
            bookRotation -= ((float)Math.PI * 2F);
        }

        while (bookRotation < -(float)Math.PI)
        {
            bookRotation += ((float)Math.PI * 2F);
        }

        while (tRot >= (float)Math.PI)
        {
            tRot -= ((float)Math.PI * 2F);
        }

        while (tRot < -(float)Math.PI)
        {
            tRot += ((float)Math.PI * 2F);
        }

        float f2;

        for (f2 = tRot - bookRotation; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F))
        {
        }

        while (f2 < -(float)Math.PI)
        {
            f2 += ((float)Math.PI * 2F);
        }

        bookRotation += f2 * 0.4F;
        bookSpread = MathHelper.clamp(bookSpread, 0.0F, 1.0F);
        ++tickCount;
        pageFlipPrev = pageFlip;
        float f = (flipT - pageFlip) * 0.4F;
        float f3 = 0.2F;
        f = MathHelper.clamp(f, -0.2F, 0.2F);
        flipA += (f - flipA) * 0.9F;
        pageFlip += flipA;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return hasCustomName() ? customName : "container.enchant";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return customName != null && !customName.isEmpty();
    }

    public void setCustomName(String customNameIn)
    {
        customName = customNameIn;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return hasCustomName() ? new TextComponent(getName()) : new TranslatableComponent(getName());
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerEnchantment(playerInventory, world, pos);
    }

    public String getGuiID()
    {
        return "minecraft:enchanting_table";
    }
}
