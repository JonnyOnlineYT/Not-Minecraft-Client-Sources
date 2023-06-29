package net.minecraft.entity.ai;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class EntityAIVillagerInteract extends EntityAIWatchClosest2
{
    /** The delay before the villager throws an itemstack (in ticks) */
    private int interactionDelay;
    private final EntityVillager villager;

    public EntityAIVillagerInteract(EntityVillager villagerIn)
    {
        super(villagerIn, EntityVillager.class, 3.0F, 0.02F);
        villager = villagerIn;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();

        if (villager.canAbondonItems() && closestEntity instanceof EntityVillager && ((EntityVillager) closestEntity).wantsMoreFood())
        {
            interactionDelay = 10;
        }
        else
        {
            interactionDelay = 0;
        }
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        super.updateTask();

        if (interactionDelay > 0)
        {
            --interactionDelay;

            if (interactionDelay == 0)
            {
                InventoryBasic inventorybasic = villager.getVillagerInventory();

                for (int i = 0; i < inventorybasic.getSizeInventory(); ++i)
                {
                    ItemStack itemstack = inventorybasic.getStackInSlot(i);
                    ItemStack itemstack1 = ItemStack.EMPTY;

                    if (!itemstack.isEmpty())
                    {
                        Item item = itemstack.getItem();

                        if ((item == Items.BREAD || item == Items.POTATO || item == Items.CARROT || item == Items.BEETROOT) && itemstack.getCount() > 3)
                        {
                            int l = itemstack.getCount() / 2;
                            itemstack.func_190918_g(l);
                            itemstack1 = new ItemStack(item, l, itemstack.getMetadata());
                        }
                        else if (item == Items.WHEAT && itemstack.getCount() > 5)
                        {
                            int j = itemstack.getCount() / 2 / 3 * 3;
                            int k = j / 3;
                            itemstack.func_190918_g(j);
                            itemstack1 = new ItemStack(Items.BREAD, k, 0);
                        }

                        if (itemstack.isEmpty())
                        {
                            inventorybasic.setInventorySlotContents(i, ItemStack.EMPTY);
                        }
                    }

                    if (!itemstack1.isEmpty())
                    {
                        double d0 = villager.posY - 0.30000001192092896D + (double) villager.getEyeHeight();
                        EntityItem entityitem = new EntityItem(villager.world, villager.posX, d0, villager.posZ, itemstack1);
                        float f = 0.3F;
                        float f1 = villager.rotationYawHead;
                        float f2 = villager.rotationPitch;
                        entityitem.motionX = -MathHelper.sin(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F) * 0.3F;
                        entityitem.motionZ = MathHelper.cos(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F) * 0.3F;
                        entityitem.motionY = -MathHelper.sin(f2 * 0.017453292F) * 0.3F + 0.1F;
                        entityitem.setDefaultPickupDelay();
                        villager.world.spawnEntityInWorld(entityitem);
                        break;
                    }
                }
            }
        }
    }
}
