package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface IPhase {
   boolean getIsStationary();

   void doClientRenderEffects();

   void doLocalUpdate();

   void onCrystalDestroyed(EntityEnderCrystal var1, BlockPos var2, DamageSource var3, EntityPlayer var4);

   void initPhase();

   void removeAreaEffect();

   float getMaxRiseOrFall();

   float getYawFactor();

   PhaseList<? extends IPhase> getPhaseList();

   @Nullable
   Vec3d getTargetLocation();

   float getAdjustedDamage(MultiPartEntityPart var1, DamageSource var2, float var3);
}
