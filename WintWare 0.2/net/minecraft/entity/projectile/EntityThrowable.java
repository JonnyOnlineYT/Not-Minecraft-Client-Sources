package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityThrowable extends Entity implements IProjectile {
   private int xTile;
   private int yTile;
   private int zTile;
   private Block inTile;
   protected boolean inGround;
   public int throwableShake;
   protected EntityLivingBase thrower;
   private String throwerName;
   private int ticksInGround;
   private int ticksInAir;
   public Entity ignoreEntity;
   private int ignoreTime;

   public EntityThrowable(World worldIn) {
      super(worldIn);
      this.xTile = -1;
      this.yTile = -1;
      this.zTile = -1;
      this.setSize(0.25F, 0.25F);
   }

   public EntityThrowable(World worldIn, double x, double y, double z) {
      this(worldIn);
      this.setPosition(x, y, z);
   }

   public EntityThrowable(World worldIn, EntityLivingBase throwerIn) {
      this(worldIn, throwerIn.posX, throwerIn.posY + (double)throwerIn.getEyeHeight() - 0.10000000149011612D, throwerIn.posZ);
      this.thrower = throwerIn;
   }

   protected void entityInit() {
   }

   public boolean isInRangeToRenderDist(double distance) {
      double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
      if (Double.isNaN(d0)) {
         d0 = 4.0D;
      }

      d0 *= 64.0D;
      return distance < d0 * d0;
   }

   public void setHeadingFromThrower(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
      float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
      float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
      float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
      this.setThrowableHeading((double)f, (double)f1, (double)f2, velocity, inaccuracy);
      this.motionX += entityThrower.motionX;
      this.motionZ += entityThrower.motionZ;
      if (!entityThrower.onGround) {
         this.motionY += entityThrower.motionY;
      }

   }

   public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
      float f = MathHelper.sqrt(x * x + y * y + z * z);
      x /= (double)f;
      y /= (double)f;
      z /= (double)f;
      x += this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
      y += this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
      z += this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
      x *= (double)velocity;
      y *= (double)velocity;
      z *= (double)velocity;
      this.motionX = x;
      this.motionY = y;
      this.motionZ = z;
      float f1 = MathHelper.sqrt(x * x + z * z);
      this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232D);
      this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * 57.29577951308232D);
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
      this.ticksInGround = 0;
   }

   public void setVelocity(double x, double y, double z) {
      this.motionX = x;
      this.motionY = y;
      this.motionZ = z;
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float f = MathHelper.sqrt(x * x + z * z);
         this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232D);
         this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * 57.29577951308232D);
         this.prevRotationYaw = this.rotationYaw;
         this.prevRotationPitch = this.rotationPitch;
      }

   }

   public void onUpdate() {
      this.lastTickPosX = this.posX;
      this.lastTickPosY = this.posY;
      this.lastTickPosZ = this.posZ;
      super.onUpdate();
      if (this.throwableShake > 0) {
         --this.throwableShake;
      }

      if (this.inGround) {
         if (this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
            ++this.ticksInGround;
            if (this.ticksInGround == 1200) {
               this.setDead();
            }

            return;
         }

         this.inGround = false;
         this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
         this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
         this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
         this.ticksInGround = 0;
         this.ticksInAir = 0;
      } else {
         ++this.ticksInAir;
      }

      Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
      Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1);
      vec3d = new Vec3d(this.posX, this.posY, this.posZ);
      vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      if (raytraceresult != null) {
         vec3d1 = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
      }

      Entity entity = null;
      List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0D));
      double d0 = 0.0D;
      boolean flag = false;

      for(int i = 0; i < list.size(); ++i) {
         Entity entity1 = (Entity)list.get(i);
         if (entity1.canBeCollidedWith()) {
            if (entity1 == this.ignoreEntity) {
               flag = true;
            } else if (this.thrower != null && this.ticksExisted < 2 && this.ignoreEntity == null) {
               this.ignoreEntity = entity1;
               flag = true;
            } else {
               flag = false;
               AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
               RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);
               if (raytraceresult1 != null) {
                  double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);
                  if (d1 < d0 || d0 == 0.0D) {
                     entity = entity1;
                     d0 = d1;
                  }
               }
            }
         }
      }

      if (this.ignoreEntity != null) {
         if (flag) {
            this.ignoreTime = 2;
         } else if (this.ignoreTime-- <= 0) {
            this.ignoreEntity = null;
         }
      }

      if (entity != null) {
         raytraceresult = new RayTraceResult(entity);
      }

      if (raytraceresult != null) {
         if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL) {
            this.setPortal(raytraceresult.getBlockPos());
         } else {
            this.onImpact(raytraceresult);
         }
      }

      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
      float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232D);

      for(this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * 57.29577951308232D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
      }

      while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
      this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
      float f1 = 0.99F;
      float f2 = this.getGravityVelocity();
      if (this.isInWater()) {
         for(int j = 0; j < 4; ++j) {
            float f3 = 0.25F;
            this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
         }

         f1 = 0.8F;
      }

      this.motionX *= (double)f1;
      this.motionY *= (double)f1;
      this.motionZ *= (double)f1;
      if (!this.hasNoGravity()) {
         this.motionY -= (double)f2;
      }

      this.setPosition(this.posX, this.posY, this.posZ);
   }

   protected float getGravityVelocity() {
      return 0.03F;
   }

   protected abstract void onImpact(RayTraceResult var1);

   public static void registerFixesThrowable(DataFixer fixer, String name) {
   }

   public void writeEntityToNBT(NBTTagCompound compound) {
      compound.setInteger("xTile", this.xTile);
      compound.setInteger("yTile", this.yTile);
      compound.setInteger("zTile", this.zTile);
      ResourceLocation resourcelocation = (ResourceLocation)Block.REGISTRY.getNameForObject(this.inTile);
      compound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
      compound.setByte("shake", (byte)this.throwableShake);
      compound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
      if ((this.throwerName == null || this.throwerName.isEmpty()) && this.thrower instanceof EntityPlayer) {
         this.throwerName = this.thrower.getName();
      }

      compound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
   }

   public void readEntityFromNBT(NBTTagCompound compound) {
      this.xTile = compound.getInteger("xTile");
      this.yTile = compound.getInteger("yTile");
      this.zTile = compound.getInteger("zTile");
      if (compound.hasKey("inTile", 8)) {
         this.inTile = Block.getBlockFromName(compound.getString("inTile"));
      } else {
         this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
      }

      this.throwableShake = compound.getByte("shake") & 255;
      this.inGround = compound.getByte("inGround") == 1;
      this.thrower = null;
      this.throwerName = compound.getString("ownerName");
      if (this.throwerName != null && this.throwerName.isEmpty()) {
         this.throwerName = null;
      }

      this.thrower = this.getThrower();
   }

   @Nullable
   public EntityLivingBase getThrower() {
      if (this.thrower == null && this.throwerName != null && !this.throwerName.isEmpty()) {
         this.thrower = this.world.getPlayerEntityByName(this.throwerName);
         if (this.thrower == null && this.world instanceof WorldServer) {
            try {
               Entity entity = ((WorldServer)this.world).getEntityFromUuid(UUID.fromString(this.throwerName));
               if (entity instanceof EntityLivingBase) {
                  this.thrower = (EntityLivingBase)entity;
               }
            } catch (Throwable var2) {
               this.thrower = null;
            }
         }
      }

      return this.thrower;
   }
}
