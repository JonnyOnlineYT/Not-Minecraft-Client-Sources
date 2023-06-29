package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ParticleExplosionHuge extends Particle {
   private int timeSinceStart;
   private final int maximumTime = 8;

   protected ParticleExplosionHuge(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1214_8_, double p_i1214_10_, double p_i1214_12_) {
      super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
   }

   public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
   }

   public void onUpdate() {
      for(int i = 0; i < 6; ++i) {
         double d0 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
         double d1 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
         double d2 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
         World var10000 = this.worldObj;
         EnumParticleTypes var10001 = EnumParticleTypes.EXPLOSION_LARGE;
         float var10005 = (float)this.timeSinceStart;
         this.getClass();
         var10000.spawnParticle(var10001, d0, d1, d2, (double)(var10005 / 8.0F), 0.0D, 0.0D);
      }

      ++this.timeSinceStart;
      int var8 = this.timeSinceStart;
      this.getClass();
      if (var8 == 8) {
         this.setExpired();
      }

   }

   public int getFXLayer() {
      return 1;
   }

   public static class Factory implements IParticleFactory {
      public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
         return new ParticleExplosionHuge(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
      }
   }
}
