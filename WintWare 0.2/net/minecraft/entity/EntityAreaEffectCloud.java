package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityAreaEffectCloud extends Entity {
   private static final DataParameter<Float> RADIUS;
   private static final DataParameter<Integer> COLOR;
   private static final DataParameter<Boolean> IGNORE_RADIUS;
   private static final DataParameter<Integer> PARTICLE;
   private static final DataParameter<Integer> PARTICLE_PARAM_1;
   private static final DataParameter<Integer> PARTICLE_PARAM_2;
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

   public EntityAreaEffectCloud(World worldIn) {
      super(worldIn);
      this.potion = PotionTypes.EMPTY;
      this.effects = Lists.newArrayList();
      this.reapplicationDelayMap = Maps.newHashMap();
      this.duration = 600;
      this.waitTime = 20;
      this.reapplicationDelay = 20;
      this.noClip = true;
      this.isImmuneToFire = true;
      this.setRadius(3.0F);
   }

   public EntityAreaEffectCloud(World worldIn, double x, double y, double z) {
      this(worldIn);
      this.setPosition(x, y, z);
   }

   protected void entityInit() {
      this.getDataManager().register(COLOR, 0);
      this.getDataManager().register(RADIUS, 0.5F);
      this.getDataManager().register(IGNORE_RADIUS, false);
      this.getDataManager().register(PARTICLE, EnumParticleTypes.SPELL_MOB.getParticleID());
      this.getDataManager().register(PARTICLE_PARAM_1, 0);
      this.getDataManager().register(PARTICLE_PARAM_2, 0);
   }

   public void setRadius(float radiusIn) {
      double d0 = this.posX;
      double d1 = this.posY;
      double d2 = this.posZ;
      this.setSize(radiusIn * 2.0F, 0.5F);
      this.setPosition(d0, d1, d2);
      if (!this.world.isRemote) {
         this.getDataManager().set(RADIUS, radiusIn);
      }

   }

   public float getRadius() {
      return (Float)this.getDataManager().get(RADIUS);
   }

   public void setPotion(PotionType potionIn) {
      this.potion = potionIn;
      if (!this.colorSet) {
         this.func_190618_C();
      }

   }

   private void func_190618_C() {
      if (this.potion == PotionTypes.EMPTY && this.effects.isEmpty()) {
         this.getDataManager().set(COLOR, 0);
      } else {
         this.getDataManager().set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.effects)));
      }

   }

   public void addEffect(PotionEffect effect) {
      this.effects.add(effect);
      if (!this.colorSet) {
         this.func_190618_C();
      }

   }

   public int getColor() {
      return (Integer)this.getDataManager().get(COLOR);
   }

   public void setColor(int colorIn) {
      this.colorSet = true;
      this.getDataManager().set(COLOR, colorIn);
   }

   public EnumParticleTypes getParticle() {
      return EnumParticleTypes.getParticleFromId((Integer)this.getDataManager().get(PARTICLE));
   }

   public void setParticle(EnumParticleTypes particleIn) {
      this.getDataManager().set(PARTICLE, particleIn.getParticleID());
   }

   public int getParticleParam1() {
      return (Integer)this.getDataManager().get(PARTICLE_PARAM_1);
   }

   public void setParticleParam1(int particleParam) {
      this.getDataManager().set(PARTICLE_PARAM_1, particleParam);
   }

   public int getParticleParam2() {
      return (Integer)this.getDataManager().get(PARTICLE_PARAM_2);
   }

   public void setParticleParam2(int particleParam) {
      this.getDataManager().set(PARTICLE_PARAM_2, particleParam);
   }

   protected void setIgnoreRadius(boolean ignoreRadius) {
      this.getDataManager().set(IGNORE_RADIUS, ignoreRadius);
   }

   public boolean shouldIgnoreRadius() {
      return (Boolean)this.getDataManager().get(IGNORE_RADIUS);
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int durationIn) {
      this.duration = durationIn;
   }

   public void onUpdate() {
      super.onUpdate();
      boolean flag = this.shouldIgnoreRadius();
      float f = this.getRadius();
      if (this.world.isRemote) {
         EnumParticleTypes enumparticletypes = this.getParticle();
         int[] aint = new int[enumparticletypes.getArgumentCount()];
         if (aint.length > 0) {
            aint[0] = this.getParticleParam1();
         }

         if (aint.length > 1) {
            aint[1] = this.getParticleParam2();
         }

         float f2;
         float f3;
         float f4;
         int k;
         int l;
         int i1;
         if (flag) {
            if (this.rand.nextBoolean()) {
               for(int i = 0; i < 2; ++i) {
                  float f1 = this.rand.nextFloat() * 6.2831855F;
                  f2 = MathHelper.sqrt(this.rand.nextFloat()) * 0.2F;
                  f3 = MathHelper.cos(f1) * f2;
                  f4 = MathHelper.sin(f1) * f2;
                  if (enumparticletypes == EnumParticleTypes.SPELL_MOB) {
                     int j = this.rand.nextBoolean() ? 16777215 : this.getColor();
                     k = j >> 16 & 255;
                     l = j >> 8 & 255;
                     i1 = j & 255;
                     this.world.func_190523_a(EnumParticleTypes.SPELL_MOB.getParticleID(), this.posX + (double)f3, this.posY, this.posZ + (double)f4, (double)((float)k / 255.0F), (double)((float)l / 255.0F), (double)((float)i1 / 255.0F));
                  } else {
                     this.world.func_190523_a(enumparticletypes.getParticleID(), this.posX + (double)f3, this.posY, this.posZ + (double)f4, 0.0D, 0.0D, 0.0D, aint);
                  }
               }
            }
         } else {
            float f5 = 3.1415927F * f * f;

            for(int k1 = 0; (float)k1 < f5; ++k1) {
               f2 = this.rand.nextFloat() * 6.2831855F;
               f3 = MathHelper.sqrt(this.rand.nextFloat()) * f;
               f4 = MathHelper.cos(f2) * f3;
               float f9 = MathHelper.sin(f2) * f3;
               if (enumparticletypes == EnumParticleTypes.SPELL_MOB) {
                  k = this.getColor();
                  l = k >> 16 & 255;
                  i1 = k >> 8 & 255;
                  int j1 = k & 255;
                  this.world.func_190523_a(EnumParticleTypes.SPELL_MOB.getParticleID(), this.posX + (double)f4, this.posY, this.posZ + (double)f9, (double)((float)l / 255.0F), (double)((float)i1 / 255.0F), (double)((float)j1 / 255.0F));
               } else {
                  this.world.func_190523_a(enumparticletypes.getParticleID(), this.posX + (double)f4, this.posY, this.posZ + (double)f9, (0.5D - this.rand.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.rand.nextDouble()) * 0.15D, aint);
               }
            }
         }
      } else {
         if (this.ticksExisted >= this.waitTime + this.duration) {
            this.setDead();
            return;
         }

         boolean flag1 = this.ticksExisted < this.waitTime;
         if (flag != flag1) {
            this.setIgnoreRadius(flag1);
         }

         if (flag1) {
            return;
         }

         if (this.radiusPerTick != 0.0F) {
            f += this.radiusPerTick;
            if (f < 0.5F) {
               this.setDead();
               return;
            }

            this.setRadius(f);
         }

         if (this.ticksExisted % 5 == 0) {
            Iterator iterator = this.reapplicationDelayMap.entrySet().iterator();

            while(iterator.hasNext()) {
               Entry<Entity, Integer> entry = (Entry)iterator.next();
               if (this.ticksExisted >= (Integer)entry.getValue()) {
                  iterator.remove();
               }
            }

            List<PotionEffect> lstPotions = Lists.newArrayList();
            Iterator var23 = this.potion.getEffects().iterator();

            while(var23.hasNext()) {
               PotionEffect potioneffect1 = (PotionEffect)var23.next();
               lstPotions.add(new PotionEffect(potioneffect1.getPotion(), potioneffect1.getDuration() / 4, potioneffect1.getAmplifier(), potioneffect1.getIsAmbient(), potioneffect1.doesShowParticles()));
            }

            lstPotions.addAll(this.effects);
            if (lstPotions.isEmpty()) {
               this.reapplicationDelayMap.clear();
            } else {
               List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox());
               if (!list.isEmpty()) {
                  Iterator var26 = list.iterator();

                  while(true) {
                     EntityLivingBase entitylivingbase;
                     double d2;
                     do {
                        do {
                           do {
                              if (!var26.hasNext()) {
                                 return;
                              }

                              entitylivingbase = (EntityLivingBase)var26.next();
                           } while(this.reapplicationDelayMap.containsKey(entitylivingbase));
                        } while(!entitylivingbase.canBeHitWithPotion());

                        double d0 = entitylivingbase.posX - this.posX;
                        double d1 = entitylivingbase.posZ - this.posZ;
                        d2 = d0 * d0 + d1 * d1;
                     } while(!(d2 <= (double)(f * f)));

                     this.reapplicationDelayMap.put(entitylivingbase, this.ticksExisted + this.reapplicationDelay);
                     Iterator var15 = lstPotions.iterator();

                     while(var15.hasNext()) {
                        PotionEffect potioneffect = (PotionEffect)var15.next();
                        if (potioneffect.getPotion().isInstant()) {
                           potioneffect.getPotion().affectEntity(this, this.getOwner(), entitylivingbase, potioneffect.getAmplifier(), 0.5D);
                        } else {
                           entitylivingbase.addPotionEffect(new PotionEffect(potioneffect));
                        }
                     }

                     if (this.radiusOnUse != 0.0F) {
                        f += this.radiusOnUse;
                        if (f < 0.5F) {
                           this.setDead();
                           return;
                        }

                        this.setRadius(f);
                     }

                     if (this.durationOnUse != 0) {
                        this.duration += this.durationOnUse;
                        if (this.duration <= 0) {
                           this.setDead();
                           return;
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public void setRadiusOnUse(float radiusOnUseIn) {
      this.radiusOnUse = radiusOnUseIn;
   }

   public void setRadiusPerTick(float radiusPerTickIn) {
      this.radiusPerTick = radiusPerTickIn;
   }

   public void setWaitTime(int waitTimeIn) {
      this.waitTime = waitTimeIn;
   }

   public void setOwner(@Nullable EntityLivingBase ownerIn) {
      this.owner = ownerIn;
      this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
   }

   @Nullable
   public EntityLivingBase getOwner() {
      if (this.owner == null && this.ownerUniqueId != null && this.world instanceof WorldServer) {
         Entity entity = ((WorldServer)this.world).getEntityFromUuid(this.ownerUniqueId);
         if (entity instanceof EntityLivingBase) {
            this.owner = (EntityLivingBase)entity;
         }
      }

      return this.owner;
   }

   protected void readEntityFromNBT(NBTTagCompound compound) {
      this.ticksExisted = compound.getInteger("Age");
      this.duration = compound.getInteger("Duration");
      this.waitTime = compound.getInteger("WaitTime");
      this.reapplicationDelay = compound.getInteger("ReapplicationDelay");
      this.durationOnUse = compound.getInteger("DurationOnUse");
      this.radiusOnUse = compound.getFloat("RadiusOnUse");
      this.radiusPerTick = compound.getFloat("RadiusPerTick");
      this.setRadius(compound.getFloat("Radius"));
      this.ownerUniqueId = compound.getUniqueId("OwnerUUID");
      if (compound.hasKey("Particle", 8)) {
         EnumParticleTypes enumparticletypes = EnumParticleTypes.getByName(compound.getString("Particle"));
         if (enumparticletypes != null) {
            this.setParticle(enumparticletypes);
            this.setParticleParam1(compound.getInteger("ParticleParam1"));
            this.setParticleParam2(compound.getInteger("ParticleParam2"));
         }
      }

      if (compound.hasKey("Color", 99)) {
         this.setColor(compound.getInteger("Color"));
      }

      if (compound.hasKey("Potion", 8)) {
         this.setPotion(PotionUtils.getPotionTypeFromNBT(compound));
      }

      if (compound.hasKey("Effects", 9)) {
         NBTTagList nbttaglist = compound.getTagList("Effects", 10);
         this.effects.clear();

         for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttaglist.getCompoundTagAt(i));
            if (potioneffect != null) {
               this.addEffect(potioneffect);
            }
         }
      }

   }

   protected void writeEntityToNBT(NBTTagCompound compound) {
      compound.setInteger("Age", this.ticksExisted);
      compound.setInteger("Duration", this.duration);
      compound.setInteger("WaitTime", this.waitTime);
      compound.setInteger("ReapplicationDelay", this.reapplicationDelay);
      compound.setInteger("DurationOnUse", this.durationOnUse);
      compound.setFloat("RadiusOnUse", this.radiusOnUse);
      compound.setFloat("RadiusPerTick", this.radiusPerTick);
      compound.setFloat("Radius", this.getRadius());
      compound.setString("Particle", this.getParticle().getParticleName());
      compound.setInteger("ParticleParam1", this.getParticleParam1());
      compound.setInteger("ParticleParam2", this.getParticleParam2());
      if (this.ownerUniqueId != null) {
         compound.setUniqueId("OwnerUUID", this.ownerUniqueId);
      }

      if (this.colorSet) {
         compound.setInteger("Color", this.getColor());
      }

      if (this.potion != PotionTypes.EMPTY && this.potion != null) {
         compound.setString("Potion", ((ResourceLocation)PotionType.REGISTRY.getNameForObject(this.potion)).toString());
      }

      if (!this.effects.isEmpty()) {
         NBTTagList nbttaglist = new NBTTagList();
         Iterator var3 = this.effects.iterator();

         while(var3.hasNext()) {
            PotionEffect potioneffect = (PotionEffect)var3.next();
            nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
         }

         compound.setTag("Effects", nbttaglist);
      }

   }

   public void notifyDataManagerChange(DataParameter<?> key) {
      if (RADIUS.equals(key)) {
         this.setRadius(this.getRadius());
      }

      super.notifyDataManagerChange(key);
   }

   public EnumPushReaction getPushReaction() {
      return EnumPushReaction.IGNORE;
   }

   static {
      RADIUS = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.FLOAT);
      COLOR = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
      IGNORE_RADIUS = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.BOOLEAN);
      PARTICLE = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
      PARTICLE_PARAM_1 = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
      PARTICLE_PARAM_2 = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
   }
}
