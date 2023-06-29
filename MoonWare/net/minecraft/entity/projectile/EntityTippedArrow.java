package net.minecraft.entity.projectile;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityTippedArrow extends EntityArrow
{
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityTippedArrow.class, DataSerializers.VARINT);
    private PotionType potion = PotionTypes.EMPTY;
    private final Set<PotionEffect> customPotionEffects = Sets.newHashSet();
    private boolean field_191509_at;

    public EntityTippedArrow(World worldIn)
    {
        super(worldIn);
    }

    public EntityTippedArrow(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public EntityTippedArrow(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter);
    }

    public void setPotionEffect(ItemStack stack)
    {
        if (stack.getItem() == Items.TIPPED_ARROW)
        {
            potion = PotionUtils.getPotionFromItem(stack);
            Collection<PotionEffect> collection = PotionUtils.getFullEffectsFromItem(stack);

            if (!collection.isEmpty())
            {
                for (PotionEffect potioneffect : collection)
                {
                    customPotionEffects.add(new PotionEffect(potioneffect));
                }
            }

            int i = func_191508_b(stack);

            if (i == -1)
            {
                func_190548_o();
            }
            else
            {
                func_191507_d(i);
            }
        }
        else if (stack.getItem() == Items.ARROW)
        {
            potion = PotionTypes.EMPTY;
            customPotionEffects.clear();
            dataManager.set(COLOR, Integer.valueOf(-1));
        }
    }

    public static int func_191508_b(ItemStack p_191508_0_)
    {
        NBTTagCompound nbttagcompound = p_191508_0_.getTagCompound();
        return nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99) ? nbttagcompound.getInteger("CustomPotionColor") : -1;
    }

    private void func_190548_o()
    {
        field_191509_at = false;
        dataManager.set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(potion, customPotionEffects))));
    }

    public void addEffect(PotionEffect effect)
    {
        customPotionEffects.add(effect);
        getDataManager().set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(potion, customPotionEffects))));
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(COLOR, Integer.valueOf(-1));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (world.isRemote)
        {
            if (inGround)
            {
                if (timeInGround % 5 == 0)
                {
                    spawnPotionParticles(1);
                }
            }
            else
            {
                spawnPotionParticles(2);
            }
        }
        else if (inGround && timeInGround != 0 && !customPotionEffects.isEmpty() && timeInGround >= 600)
        {
            world.setEntityState(this, (byte)0);
            potion = PotionTypes.EMPTY;
            customPotionEffects.clear();
            dataManager.set(COLOR, Integer.valueOf(-1));
        }
    }

    private void spawnPotionParticles(int particleCount)
    {
        int i = getColor();

        if (i != -1 && particleCount > 0)
        {
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i >> 0 & 255) / 255.0D;

            for (int j = 0; j < particleCount; ++j)
            {
                world.spawnParticle(EnumParticleTypes.SPELL_MOB, posX + (rand.nextDouble() - 0.5D) * (double) width, posY + rand.nextDouble() * (double) height, posZ + (rand.nextDouble() - 0.5D) * (double) width, d0, d1, d2);
            }
        }
    }

    public int getColor()
    {
        return dataManager.get(COLOR).intValue();
    }

    private void func_191507_d(int p_191507_1_)
    {
        field_191509_at = true;
        dataManager.set(COLOR, Integer.valueOf(p_191507_1_));
    }

    public static void registerFixesTippedArrow(DataFixer fixer)
    {
        EntityArrow.registerFixesArrow(fixer, "TippedArrow");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (potion != PotionTypes.EMPTY && potion != null)
        {
            compound.setString("Potion", PotionType.REGISTRY.getNameForObject(potion).toString());
        }

        if (field_191509_at)
        {
            compound.setInteger("Color", getColor());
        }

        if (!customPotionEffects.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (PotionEffect potioneffect : customPotionEffects)
            {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            compound.setTag("CustomPotionEffects", nbttaglist);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("Potion", 8))
        {
            potion = PotionUtils.getPotionTypeFromNBT(compound);
        }

        for (PotionEffect potioneffect : PotionUtils.getFullEffectsFromTag(compound))
        {
            addEffect(potioneffect);
        }

        if (compound.hasKey("Color", 99))
        {
            func_191507_d(compound.getInteger("Color"));
        }
        else
        {
            func_190548_o();
        }
    }

    protected void arrowHit(EntityLivingBase living)
    {
        super.arrowHit(living);

        for (PotionEffect potioneffect : potion.getEffects())
        {
            living.addPotionEffect(new PotionEffect(potioneffect.getPotion(), Math.max(potioneffect.getDuration() / 8, 1), potioneffect.getAmplifier(), potioneffect.getIsAmbient(), potioneffect.doesShowParticles()));
        }

        if (!customPotionEffects.isEmpty())
        {
            for (PotionEffect potioneffect1 : customPotionEffects)
            {
                living.addPotionEffect(potioneffect1);
            }
        }
    }

    protected ItemStack getArrowStack()
    {
        if (customPotionEffects.isEmpty() && potion == PotionTypes.EMPTY)
        {
            return new ItemStack(Items.ARROW);
        }
        else
        {
            ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
            PotionUtils.addPotionToItemStack(itemstack, potion);
            PotionUtils.appendEffects(itemstack, customPotionEffects);

            if (field_191509_at)
            {
                NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                if (nbttagcompound == null)
                {
                    nbttagcompound = new NBTTagCompound();
                    itemstack.setTagCompound(nbttagcompound);
                }

                nbttagcompound.setInteger("CustomPotionColor", getColor());
            }

            return itemstack;
        }
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 0)
        {
            int i = getColor();

            if (i != -1)
            {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i >> 0 & 255) / 255.0D;

                for (int j = 0; j < 20; ++j)
                {
                    world.spawnParticle(EnumParticleTypes.SPELL_MOB, posX + (rand.nextDouble() - 0.5D) * (double) width, posY + rand.nextDouble() * (double) height, posZ + (rand.nextDouble() - 0.5D) * (double) width, d0, d1, d2);
                }
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }
}
