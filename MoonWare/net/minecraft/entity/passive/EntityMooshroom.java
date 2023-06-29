package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityMooshroom extends EntityCow
{
    public EntityMooshroom(World worldIn)
    {
        super(worldIn);
        setSize(0.9F, 1.4F);
        spawnableBlock = Blocks.MYCELIUM;
    }

    public static void registerFixesMooshroom(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityMooshroom.class);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.BOWL && getGrowingAge() >= 0 && !player.capabilities.isCreativeMode)
        {
            itemstack.func_190918_g(1);

            if (itemstack.isEmpty())
            {
                player.setHeldItem(hand, new ItemStack(Items.MUSHROOM_STEW));
            }
            else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MUSHROOM_STEW)))
            {
                player.dropItem(new ItemStack(Items.MUSHROOM_STEW), false);
            }

            return true;
        }
        else if (itemstack.getItem() == Items.SHEARS && getGrowingAge() >= 0)
        {
            setDead();
            world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX, posY + (double)(height / 2.0F), posZ, 0.0D, 0.0D, 0.0D);

            if (!world.isRemote)
            {
                EntityCow entitycow = new EntityCow(world);
                entitycow.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
                entitycow.setHealth(getHealth());
                entitycow.renderYawOffset = renderYawOffset;

                if (hasCustomName())
                {
                    entitycow.setCustomNameTag(getCustomNameTag());
                }

                world.spawnEntityInWorld(entitycow);

                for (int i = 0; i < 5; ++i)
                {
                    world.spawnEntityInWorld(new EntityItem(world, posX, posY + (double) height, posZ, new ItemStack(Blocks.RED_MUSHROOM)));
                }

                itemstack.damageItem(1, player);
                playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1.0F, 1.0F);
            }

            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
    }

    public EntityMooshroom createChild(EntityAgeable ageable)
    {
        return new EntityMooshroom(world);
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_MUSHROOM_COW;
    }
}
