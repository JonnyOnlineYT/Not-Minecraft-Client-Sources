package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntitySpellcasterIllager extends AbstractIllager
{
    private static final DataParameter<Byte> field_193088_c = EntityDataManager.createKey(EntitySpellcasterIllager.class, DataSerializers.BYTE);
    protected int field_193087_b;
    private EntitySpellcasterIllager.SpellType field_193089_bx = EntitySpellcasterIllager.SpellType.NONE;

    public EntitySpellcasterIllager(World p_i47506_1_)
    {
        super(p_i47506_1_);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(field_193088_c, Byte.valueOf((byte)0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        field_193087_b = compound.getInteger("SpellTicks");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("SpellTicks", field_193087_b);
    }

    public AbstractIllager.IllagerArmPose func_193077_p()
    {
        return func_193082_dl() ? AbstractIllager.IllagerArmPose.SPELLCASTING : AbstractIllager.IllagerArmPose.CROSSED;
    }

    public boolean func_193082_dl()
    {
        if (world.isRemote)
        {
            return dataManager.get(field_193088_c).byteValue() > 0;
        }
        else
        {
            return field_193087_b > 0;
        }
    }

    public void func_193081_a(EntitySpellcasterIllager.SpellType p_193081_1_)
    {
        field_193089_bx = p_193081_1_;
        dataManager.set(field_193088_c, Byte.valueOf((byte)p_193081_1_.field_193345_g));
    }

    protected EntitySpellcasterIllager.SpellType func_193083_dm()
    {
        return !world.isRemote ? field_193089_bx : EntitySpellcasterIllager.SpellType.func_193337_a(dataManager.get(field_193088_c).byteValue());
    }

    protected void updateAITasks()
    {
        super.updateAITasks();

        if (field_193087_b > 0)
        {
            --field_193087_b;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (world.isRemote && func_193082_dl())
        {
            EntitySpellcasterIllager.SpellType entityspellcasterillager$spelltype = func_193083_dm();
            double d0 = entityspellcasterillager$spelltype.field_193346_h[0];
            double d1 = entityspellcasterillager$spelltype.field_193346_h[1];
            double d2 = entityspellcasterillager$spelltype.field_193346_h[2];
            float f = renderYawOffset * 0.017453292F + MathHelper.cos((float) ticksExisted * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            world.spawnParticle(EnumParticleTypes.SPELL_MOB, posX + (double)f1 * 0.6D, posY + 1.8D, posZ + (double)f2 * 0.6D, d0, d1, d2);
            world.spawnParticle(EnumParticleTypes.SPELL_MOB, posX - (double)f1 * 0.6D, posY + 1.8D, posZ - (double)f2 * 0.6D, d0, d1, d2);
        }
    }

    protected int func_193085_dn()
    {
        return field_193087_b;
    }

    protected abstract SoundEvent func_193086_dk();

    public class AICastingApell extends EntityAIBase
    {
        public AICastingApell()
        {
            setMutexBits(3);
        }

        public boolean shouldExecute()
        {
            return func_193085_dn() > 0;
        }

        public void startExecuting()
        {
            super.startExecuting();
            navigator.clearPathEntity();
        }

        public void resetTask()
        {
            super.resetTask();
            func_193081_a(EntitySpellcasterIllager.SpellType.NONE);
        }

        public void updateTask()
        {
            if (getAttackTarget() != null)
            {
                getLookHelper().setLookPositionWithEntity(getAttackTarget(), (float) getHorizontalFaceSpeed(), (float) getVerticalFaceSpeed());
            }
        }
    }

    public abstract class AIUseSpell extends EntityAIBase
    {
        protected int field_193321_c;
        protected int field_193322_d;

        public boolean shouldExecute()
        {
            if (getAttackTarget() == null)
            {
                return false;
            }
            else if (func_193082_dl())
            {
                return false;
            }
            else
            {
                return ticksExisted >= field_193322_d;
            }
        }

        public boolean continueExecuting()
        {
            return getAttackTarget() != null && field_193321_c > 0;
        }

        public void startExecuting()
        {
            field_193321_c = func_190867_m();
            field_193087_b = func_190869_f();
            field_193322_d = ticksExisted + func_190872_i();
            SoundEvent soundevent = func_190871_k();

            if (soundevent != null)
            {
                playSound(soundevent, 1.0F, 1.0F);
            }

            func_193081_a(func_193320_l());
        }

        public void updateTask()
        {
            --field_193321_c;

            if (field_193321_c == 0)
            {
                func_190868_j();
                playSound(func_193086_dk(), 1.0F, 1.0F);
            }
        }

        protected abstract void func_190868_j();

        protected int func_190867_m()
        {
            return 20;
        }

        protected abstract int func_190869_f();

        protected abstract int func_190872_i();

        @Nullable
        protected abstract SoundEvent func_190871_k();

        protected abstract EntitySpellcasterIllager.SpellType func_193320_l();
    }

    public enum SpellType
    {
        NONE(0, 0.0D, 0.0D, 0.0D),
        SUMMON_VEX(1, 0.7D, 0.7D, 0.8D),
        FANGS(2, 0.4D, 0.3D, 0.35D),
        WOLOLO(3, 0.7D, 0.5D, 0.2D),
        DISAPPEAR(4, 0.3D, 0.3D, 0.8D),
        BLINDNESS(5, 0.1D, 0.1D, 0.2D);

        private final int field_193345_g;
        private final double[] field_193346_h;

        SpellType(int p_i47561_3_, double p_i47561_4_, double p_i47561_6_, double p_i47561_8_)
        {
            field_193345_g = p_i47561_3_;
            field_193346_h = new double[] {p_i47561_4_, p_i47561_6_, p_i47561_8_};
        }

        public static EntitySpellcasterIllager.SpellType func_193337_a(int p_193337_0_)
        {
            for (EntitySpellcasterIllager.SpellType entityspellcasterillager$spelltype : values())
            {
                if (p_193337_0_ == entityspellcasterillager$spelltype.field_193345_g)
                {
                    return entityspellcasterillager$spelltype;
                }
            }

            return NONE;
        }
    }
}
