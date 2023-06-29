package me.wintware.client.module.world;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.MoveEvent;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.renderer.Vector3d;

public class SafeWalk extends Module {
   private Vector3d vec = new Vector3d();

   public SafeWalk() {
      super("SafeWalk", Category.World);
   }

   @EventTarget
   public void event(MoveEvent event) {
      double x = event.x;
      double y = event.y;
      double z = event.z;
      if (mc.player.onGround) {
         double increment = 0.05D;

         label53:
         while(true) {
            while(x != 0.0D && this.isOffsetBBEmpty(x, -1.0D, 0.0D)) {
               if (x < increment && x >= -increment) {
                  x = 0.0D;
               } else if (x > 0.0D) {
                  x -= increment;
               } else {
                  x += increment;
               }
            }

            while(true) {
               while(z != 0.0D && this.isOffsetBBEmpty(0.0D, -1.0D, z)) {
                  if (z < increment && z >= -increment) {
                     z = 0.0D;
                  } else if (z > 0.0D) {
                     z -= increment;
                  } else {
                     z += increment;
                  }
               }

               while(true) {
                  while(true) {
                     if (x == 0.0D || z == 0.0D || !this.isOffsetBBEmpty(x, -1.0D, z)) {
                        break label53;
                     }

                     if (x < increment && x >= -increment) {
                        x = 0.0D;
                     } else if (x > 0.0D) {
                        x -= increment;
                     } else {
                        x += increment;
                     }

                     if (z < increment && z >= -increment) {
                        z = 0.0D;
                     } else if (z > 0.0D) {
                        z -= increment;
                     } else {
                        z += increment;
                     }
                  }
               }
            }
         }
      }

      event.x = x;
      event.y = y;
      event.z = z;
   }

   public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
      this.vec.x = offsetX;
      this.vec.y = offsetY;
      this.vec.z = offsetZ;
      return mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(this.vec.x, this.vec.y, this.vec.z)).isEmpty();
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }
}
