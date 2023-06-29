package me.wintware.client.utils.combat;

import com.google.common.base.Predicates;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RaycastUtil {
   private static final Minecraft mc = Minecraft.getMinecraft();

   public static Entity rayCastEntity(double range, float yaw, float pitch) {
      Entity renderViewEntity = mc.getRenderViewEntity();
      if (renderViewEntity != null && mc.world != null) {
         double blockReachDistance = range;
         Vec3d eyePosition = renderViewEntity.getPositionEyes(1.0F);
         float yawCos = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
         float yawSin = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
         float pitchCos = -MathHelper.cos(-pitch * 0.017453292F);
         float pitchSin = MathHelper.sin(-pitch * 0.017453292F);
         Vec3d entityLook = new Vec3d((double)(yawSin * pitchCos), (double)pitchSin, (double)(yawCos * pitchCos));
         Vec3d vector = eyePosition.addVector(entityLook.xCoord * range, entityLook.yCoord * range, entityLook.zCoord * range);
         List<Entity> entityList = mc.world.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(entityLook.xCoord * range, entityLook.yCoord * range, entityLook.zCoord * range).expand(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
         Entity pointedEntity = null;
         Iterator var16 = entityList.iterator();

         while(true) {
            while(var16.hasNext()) {
               Entity entity = (Entity)var16.next();
               float collisionBorderSize = entity.getCollisionBorderSize();
               AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
               RayTraceResult movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);
               if (axisAlignedBB.isVecInside(eyePosition)) {
                  if (blockReachDistance >= 0.0D) {
                     pointedEntity = entity;
                     blockReachDistance = 0.0D;
                  }
               } else if (movingObjectPosition != null) {
                  double eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec);
                  if (eyeDistance < blockReachDistance || blockReachDistance == 0.0D) {
                     if (entity == renderViewEntity.ridingEntity) {
                        if (blockReachDistance == 0.0D) {
                           pointedEntity = entity;
                        }
                     } else {
                        pointedEntity = entity;
                        blockReachDistance = eyeDistance;
                     }
                  }
               }
            }

            return pointedEntity;
         }
      } else {
         return null;
      }
   }

   public static Vec3d getVectorForRotation(float pitch, float yaw) {
      float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
      float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
      float f2 = -MathHelper.cos(-pitch * 0.017453292F);
      float f3 = MathHelper.sin(-pitch * 0.017453292F);
      return new Vec3d((double)(f1 * f2), (double)f3, (double)(f * f2));
   }
}
