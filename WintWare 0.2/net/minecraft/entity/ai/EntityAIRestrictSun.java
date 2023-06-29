package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAIRestrictSun extends EntityAIBase {
   private final EntityCreature theEntity;

   public EntityAIRestrictSun(EntityCreature creature) {
      this.theEntity = creature;
   }

   public boolean shouldExecute() {
      return this.theEntity.world.isDaytime() && this.theEntity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).func_190926_b();
   }

   public void startExecuting() {
      ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(true);
   }

   public void resetTask() {
      ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(false);
   }
}
