package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.init.PotionTypes;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityAreaEffectCloud extends Entity
{
    private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IGNORE_RADIUS = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> PARTICLE = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> PARTICLE_PARAM_1 = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> PARTICLE_PARAM_2 = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private PotionType potion;
    private final List<PotionEffect> effects;
    private final Map<Entity, Integer> reapplicationDelayMap;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private boolean colorSet;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private EntityLivingBase owner;
    private UUID ownerUniqueId;

    public EntityAreaEffectCloud(World worldIn)
    {
        super(worldIn);
        potion = PotionTypes.EMPTY;
        effects = Lists.newArrayList();
        reapplicationDelayMap = Maps.newHashMap();
        duration = 600;
        waitTime = 20;
        reapplicationDelay = 20;
        noClip = true;
        isImmuneToFire = true;
        setRadius(3.0F);
    }

    public EntityAreaEffectCloud(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        setPosition(x, y, z);
    }

    protected void entityInit()
    {
        getDataManager().register(COLOR, Integer.valueOf(0));
        getDataManager().register(RADIUS, Float.valueOf(0.5F));
        getDataManager().register(IGNORE_RADIUS, Boolean.valueOf(false));
        getDataManager().register(PARTICLE, Integer.valueOf(EnumParticleTypes.SPELL_MOB.getParticleID()));
        getDataManager().register(PARTICLE_PARAM_1, Integer.valueOf(0));
        getDataManager().register(PARTICLE_PARAM_2, Integer.valueOf(0));
    }

    public void setRadius(float radiusIn)
    {
        double d0 = posX;
        double d1 = posY;
        double d2 = posZ;
        setSize(radiusIn * 2.0F, 0.5F);
        setPosition(d0, d1, d2);

        if (!world.isRemote)
        {
            getDataManager().set(RADIUS, Float.valueOf(radiusIn));
        }
    }

    public float getRadius()
    {
        return getDataManager().get(RADIUS).floatValue();
    }

    public void setPotion(PotionType potionIn)
    {
        potion = potionIn;

        if (!colorSet)
        {
            func_190618_C();
        }
    }

    private void func_190618_C()
    {
        if (potion == PotionTypes.EMPTY && effects.isEmpty())
        {
            getDataManager().set(COLOR, Integer.valueOf(0));
        }
        else
        {
            getDataManager().set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(potion, effects))));
        }
    }

    public void addEffect(PotionEffect effect)
    {
        effects.add(effect);

        if (!colorSet)
        {
            func_190618_C();
        }
    }

    public int getColor()
    {
        return getDataManager().get(COLOR).intValue();
    }

    public void setColor(int colorIn)
    {
        colorSet = true;
        getDataManager().set(COLOR, Integer.valueOf(colorIn));
    }

    public EnumParticleTypes getParticle()
    {
        return EnumParticleTypes.getParticleFromId(getDataManager().get(PARTICLE).intValue());
    }

    public void setParticle(EnumParticleTypes particleIn)
    {
        getDataManager().set(PARTICLE, Integer.valueOf(particleIn.getParticleID()));
    }

    public int getParticleParam1()
    {
        return getDataManager().get(PARTICLE_PARAM_1).intValue();
    }

    public void setParticleParam1(int particleParam)
    {
        getDataManager().set(PARTICLE_PARAM_1, Integer.valueOf(particleParam));
    }

    public int getParticleParam2()
    {
        return getDataManager().get(PARTICLE_PARAM_2).intValue();
    }

    public void setParticleParam2(int particleParam)
    {
        getDataManager().set(PARTICLE_PARAM_2, Integer.valueOf(particleParam));
    }

    /**
     * Sets if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    protected void setIgnoreRadius(boolean ignoreRadius)
    {
        getDataManager().set(IGNORE_RADIUS, Boolean.valueOf(ignoreRadius));
    }

    /**
     * Returns true if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    public boolean shouldIgnoreRadius()
    {
        return getDataManager().get(IGNORE_RADIUS).booleanValue();
    }

    public int getDuration()
    {
        return duration;
    }

    public void setDuration(int durationIn)
    {
        duration = durationIn;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        boolean flag = shouldIgnoreRadius();
        float f = getRadius();

        if (world.isRemote)
        {
            EnumParticleTypes enumparticletypes = getParticle();
            int[] aint = new int[enumparticletypes.getArgumentCount()];

            if (aint.length > 0)
            {
                aint[0] = getParticleParam1();
            }

            if (aint.length > 1)
            {
                aint[1] = getParticleParam2();
            }

            if (flag)
            {
                if (rand.nextBoolean())
                {
                    for (int i = 0; i < 2; ++i)
                    {
                        float f1 = rand.nextFloat() * ((float)Math.PI * 2F);
                        float f2 = MathHelper.sqrt(rand.nextFloat()) * 0.2F;
                        float f3 = MathHelper.cos(f1) * f2;
                        float f4 = MathHelper.sin(f1) * f2;

                        if (enumparticletypes == EnumParticleTypes.SPELL_MOB)
                        {
                            int j = rand.nextBoolean() ? 16777215 : getColor();
                            int k = j >> 16 & 255;
                            int l = j >> 8 & 255;
                            int i1 = j & 255;
                            world.func_190523_a(EnumParticleTypes.SPELL_MOB.getParticleID(), posX + (double)f3, posY, posZ + (double)f4, (float)k / 255.0F, (float)l / 255.0F, (float)i1 / 255.0F);
                        }
                        else
                        {
                            world.func_190523_a(enumparticletypes.getParticleID(), posX + (double)f3, posY, posZ + (double)f4, 0.0D, 0.0D, 0.0D, aint);
                        }
                    }
                }
            }
            else
            {
                float f5 = (float)Math.PI * f * f;

                for (int k1 = 0; (float)k1 < f5; ++k1)
                {
                    float f6 = rand.nextFloat() * ((float)Math.PI * 2F);
                    float f7 = MathHelper.sqrt(rand.nextFloat()) * f;
                    float f8 = MathHelper.cos(f6) * f7;
                    float f9 = MathHelper.sin(f6) * f7;

                    if (enumparticletypes == EnumParticleTypes.SPELL_MOB)
                    {
                        int l1 = getColor();
                        int i2 = l1 >> 16 & 255;
                        int j2 = l1 >> 8 & 255;
                        int j1 = l1 & 255;
                        world.func_190523_a(EnumParticleTypes.SPELL_MOB.getParticleID(), posX + (double)f8, posY, posZ + (double)f9, (float)i2 / 255.0F, (float)j2 / 255.0F, (float)j1 / 255.0F);
                    }
                    else
                    {
                        world.func_190523_a(enumparticletypes.getParticleID(), posX + (double)f8, posY, posZ + (double)f9, (0.5D - rand.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - rand.nextDouble()) * 0.15D, aint);
                    }
                }
            }
        }
        else
        {
            if (ticksExisted >= waitTime + duration)
            {
                setDead();
                return;
            }

            boolean flag1 = ticksExisted < waitTime;

            if (flag != flag1)
            {
                setIgnoreRadius(flag1);
            }

            if (flag1)
            {
                return;
            }

            if (radiusPerTick != 0.0F)
            {
                f += radiusPerTick;

                if (f < 0.5F)
                {
                    setDead();
                    return;
                }

                setRadius(f);
            }

            if (ticksExisted % 5 == 0)
            {
                Iterator<Map.Entry<Entity, Integer>> iterator = reapplicationDelayMap.entrySet().iterator();

                while (iterator.hasNext())
                {
                    Map.Entry<Entity, Integer> entry = iterator.next();

                    if (ticksExisted >= entry.getValue().intValue())
                    {
                        iterator.remove();
                    }
                }

                List<PotionEffect> lstPotions = Lists.newArrayList();

                for (PotionEffect potioneffect1 : potion.getEffects())
                {
                    lstPotions.add(new PotionEffect(potioneffect1.getPotion(), potioneffect1.getDuration() / 4, potioneffect1.getAmplifier(), potioneffect1.getIsAmbient(), potioneffect1.doesShowParticles()));
                }

                lstPotions.addAll(effects);

                if (lstPotions.isEmpty())
                {
                    reapplicationDelayMap.clear();
                }
                else
                {
                    List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox());

                    if (!list.isEmpty())
                    {
                        for (EntityLivingBase entitylivingbase : list)
                        {
                            if (!reapplicationDelayMap.containsKey(entitylivingbase) && entitylivingbase.canBeHitWithPotion())
                            {
                                double d0 = entitylivingbase.posX - posX;
                                double d1 = entitylivingbase.posZ - posZ;
                                double d2 = d0 * d0 + d1 * d1;

                                if (d2 <= (double)(f * f))
                                {
                                    reapplicationDelayMap.put(entitylivingbase, Integer.valueOf(ticksExisted + reapplicationDelay));

                                    for (PotionEffect potioneffect : lstPotions)
                                    {
                                        if (potioneffect.getPotion().isInstant())
                                        {
                                            potioneffect.getPotion().affectEntity(this, getOwner(), entitylivingbase, potioneffect.getAmplifier(), 0.5D);
                                        }
                                        else
                                        {
                                            entitylivingbase.addPotionEffect(new PotionEffect(potioneffect));
                                        }
                                    }

                                    if (radiusOnUse != 0.0F)
                                    {
                                        f += radiusOnUse;

                                        if (f < 0.5F)
                                        {
                                            setDead();
                                            return;
                                        }

                                        setRadius(f);
                                    }

                                    if (durationOnUse != 0)
                                    {
                                        duration += durationOnUse;

                                        if (duration <= 0)
                                        {
                                            setDead();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setRadiusOnUse(float radiusOnUseIn)
    {
        radiusOnUse = radiusOnUseIn;
    }

    public void setRadiusPerTick(float radiusPerTickIn)
    {
        radiusPerTick = radiusPerTickIn;
    }

    public void setWaitTime(int waitTimeIn)
    {
        waitTime = waitTimeIn;
    }

    public void setOwner(@Nullable EntityLivingBase ownerIn)
    {
        owner = ownerIn;
        ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    @Nullable
    public EntityLivingBase getOwner()
    {
        if (owner == null && ownerUniqueId != null && world instanceof WorldServer)
        {
            Entity entity = ((WorldServer) world).getEntityFromUuid(ownerUniqueId);

            if (entity instanceof EntityLivingBase)
            {
                owner = (EntityLivingBase)entity;
            }
        }

        return owner;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        ticksExisted = compound.getInteger("Age");
        duration = compound.getInteger("Duration");
        waitTime = compound.getInteger("WaitTime");
        reapplicationDelay = compound.getInteger("ReapplicationDelay");
        durationOnUse = compound.getInteger("DurationOnUse");
        radiusOnUse = compound.getFloat("RadiusOnUse");
        radiusPerTick = compound.getFloat("RadiusPerTick");
        setRadius(compound.getFloat("Radius"));
        ownerUniqueId = compound.getUniqueId("OwnerUUID");

        if (compound.hasKey("Particle", 8))
        {
            EnumParticleTypes enumparticletypes = EnumParticleTypes.getByName(compound.getString("Particle"));

            if (enumparticletypes != null)
            {
                setParticle(enumparticletypes);
                setParticleParam1(compound.getInteger("ParticleParam1"));
                setParticleParam2(compound.getInteger("ParticleParam2"));
            }
        }

        if (compound.hasKey("Color", 99))
        {
            setColor(compound.getInteger("Color"));
        }

        if (compound.hasKey("Potion", 8))
        {
            setPotion(PotionUtils.getPotionTypeFromNBT(compound));
        }

        if (compound.hasKey("Effects", 9))
        {
            NBTTagList nbttaglist = compound.getTagList("Effects", 10);
            effects.clear();

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttaglist.getCompoundTagAt(i));

                if (potioneffect != null)
                {
                    addEffect(potioneffect);
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("Age", ticksExisted);
        compound.setInteger("Duration", duration);
        compound.setInteger("WaitTime", waitTime);
        compound.setInteger("ReapplicationDelay", reapplicationDelay);
        compound.setInteger("DurationOnUse", durationOnUse);
        compound.setFloat("RadiusOnUse", radiusOnUse);
        compound.setFloat("RadiusPerTick", radiusPerTick);
        compound.setFloat("Radius", getRadius());
        compound.setString("Particle", getParticle().getParticleName());
        compound.setInteger("ParticleParam1", getParticleParam1());
        compound.setInteger("ParticleParam2", getParticleParam2());

        if (ownerUniqueId != null)
        {
            compound.setUniqueId("OwnerUUID", ownerUniqueId);
        }

        if (colorSet)
        {
            compound.setInteger("Color", getColor());
        }

        if (potion != PotionTypes.EMPTY && potion != null)
        {
            compound.setString("Potion", PotionType.REGISTRY.getNameForObject(potion).toString());
        }

        if (!effects.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (PotionEffect potioneffect : effects)
            {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            compound.setTag("Effects", nbttaglist);
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (RADIUS.equals(key))
        {
            setRadius(getRadius());
        }

        super.notifyDataManagerChange(key);
    }

    public EnumPushReaction getPushReaction()
    {
        return EnumPushReaction.IGNORE;
    }
}
