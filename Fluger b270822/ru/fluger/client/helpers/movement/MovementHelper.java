package ru.fluger.client.helpers.movement;

import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import ru.fluger.client.event.events.impl.player.EventMove;
import ru.fluger.client.helpers.Helper;

public class MovementHelper implements Helper {
   public static boolean isMoving() {
      return mc.player.movementInput.moveForward != 0.0F || mc.player.movementInput.moveStrafe != 0.0F;
   }

   public static int getSpeedEffect() {
      return mc.player.isPotionActive(MobEffects.SPEED) ? ((PotionEffect)Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED))).getAmplifier() + 1 : 0;
   }

   public static boolean isInsideBlock2() {
      for(int i = MathHelper.floor(mc.player.boundingBox.minX); i < MathHelper.floor(mc.player.boundingBox.maxX) + 1; ++i) {
         for(int j = MathHelper.floor(mc.player.boundingBox.minY + 1.0); j < MathHelper.floor(mc.player.boundingBox.maxY) + 2; ++j) {
            for(int k = MathHelper.floor(mc.player.boundingBox.minZ); k < MathHelper.floor(mc.player.boundingBox.maxZ) + 1; ++k) {
               Block block = mc.world.getBlockState(new BlockPos(i, j, k)).getBlock();
               if (block != null && !(block instanceof BlockAir)) {
                  AxisAlignedBB axisAlignedBB = block.getSelectedBoundingBox(mc.world.getBlockState(new BlockPos(i, j, k)), mc.world, new BlockPos(i, j, k));
                  if (block instanceof BlockHopper) {
                     axisAlignedBB = new AxisAlignedBB((double)i, (double)j, (double)k, (double)(i + 1), (double)(j + 1), (double)(k + 1));
                  }

                  if (axisAlignedBB != null && mc.player.boundingBox.intersectsWith(axisAlignedBB)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public static double[] getSpeed(double speed) {
      float moveForward = mc.player.movementInput.moveForward;
      float moveStrafe = mc.player.movementInput.moveStrafe;
      float rotationYaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
      if (moveForward != 0.0F) {
         if (moveStrafe > 0.0F) {
            rotationYaw += (float)(moveForward > 0.0F ? -45 : 45);
         } else if (moveStrafe < 0.0F) {
            rotationYaw += (float)(moveForward > 0.0F ? 45 : -45);
         }

         moveStrafe = 0.0F;
         if (moveForward > 0.0F) {
            moveForward = 1.0F;
         } else if (moveForward < 0.0F) {
            moveForward = -1.0F;
         }
      }

      double posX = (double)moveForward * speed * -Math.sin(Math.toRadians((double)rotationYaw)) + (double)moveStrafe * speed * Math.cos(Math.toRadians((double)rotationYaw));
      double posZ = (double)moveForward * speed * Math.cos(Math.toRadians((double)rotationYaw)) - (double)moveStrafe * speed * -Math.sin(Math.toRadians((double)rotationYaw));
      return new double[]{posX, posZ};
   }

   public static void teleport(CPacketPlayer player, double dist) {
      double forward = (double)mc.player.movementInput.moveForward;
      double strafe = (double)mc.player.movementInput.moveStrafe;
      float yaw = mc.player.rotationYaw;
      if (forward != 0.0) {
         if (strafe > 0.0) {
            yaw += (float)(forward > 0.0 ? -45 : 45);
         } else if (strafe < 0.0) {
            yaw += (float)(forward > 0.0 ? 45 : -45);
         }

         strafe = 0.0;
         if (forward > 0.0) {
            forward = 1.0;
         } else if (forward < 0.0) {
            forward = -1.0;
         }
      }

      double xspeed = forward * dist * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * dist * Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double zspeed = forward * dist * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * dist * Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      player.x = xspeed;
      player.z = zspeed;
   }

   public static void teleport(double dist) {
      double forward = (double)mc.player.movementInput.moveForward;
      double strafe = (double)mc.player.movementInput.moveStrafe;
      float yaw = mc.player.rotationYaw;
      if (forward != 0.0) {
         if (strafe > 0.0) {
            yaw += (float)(forward > 0.0 ? -45 : 45);
         } else if (strafe < 0.0) {
            yaw += (float)(forward > 0.0 ? 45 : -45);
         }

         strafe = 0.0;
         if (forward > 0.0) {
            forward = 1.0;
         } else if (forward < 0.0) {
            forward = -1.0;
         }
      }

      double x = mc.player.posX;
      double y = mc.player.posY;
      double z = mc.player.posZ;
      double xspeed = forward * dist * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * dist * Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double zspeed = forward * dist * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * dist * Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      mc.player.connection.sendPacket(new CPacketPlayer.Position(x + xspeed, y, z + zspeed, true));
      mc.player.connection.sendPacket(new CPacketPlayer.Position(x + xspeed, y + 0.1, z + zspeed, true));
      mc.player.connection.sendPacket(new CPacketPlayer.Position(x + xspeed, y, z + zspeed, true));
      mc.player.connection.sendPacket(new CPacketPlayer.Position(x + xspeed * 2.0, y, z + zspeed * 2.0, true));
   }

   public static float getPlayerDirection() {
      float rotationYaw = mc.player.rotationYaw;
      if (mc.player.moveForward < 0.0F) {
         rotationYaw += 180.0F;
      }

      float forward = 1.0F;
      if (mc.player.moveForward < 0.0F) {
         forward = -0.5F;
      } else if (mc.player.moveForward > 0.0F) {
         forward = 0.5F;
      }

      if (mc.player.moveStrafing > 0.0F) {
         rotationYaw -= 90.0F * forward;
      }

      if (mc.player.moveStrafing < 0.0F) {
         rotationYaw += 90.0F * forward;
      }

      return (float)Math.toRadians((double)rotationYaw);
   }

   public static float getEntityDirection(EntityLivingBase entity) {
      float rotationYaw = entity.rotationYaw;
      if (entity.moveForward < 0.0F) {
         rotationYaw += 180.0F;
      }

      float forward = 1.0F;
      if (entity.moveForward < 0.0F) {
         forward = -0.5F;
      } else if (entity.moveForward > 0.0F) {
         forward = 0.5F;
      }

      if (entity.moveStrafing > 0.0F) {
         rotationYaw -= 90.0F * forward;
      }

      if (entity.moveStrafing < 0.0F) {
         rotationYaw += 90.0F * forward;
      }

      return (float)Math.toRadians((double)rotationYaw);
   }

   public static boolean airBlockAbove2() {
      return !mc.world.checkBlockCollision(mc.player.getEntityBoundingBox().addCoord(0.0, mc.player.onGround ? 0.3 : 0.0, 0.0));
   }

   public static boolean isUnderBedrock() {
      if (!(mc.player.posY <= 3.0)) {
         return false;
      } else {
         RayTraceResult trace = mc.world.rayTraceBlocks(mc.player.getPositionVector(), new Vec3d(mc.player.posX, 0.0, mc.player.posZ), false, false, false);
         return trace == null || trace.typeOfHit != Type.BLOCK;
      }
   }

   public static boolean isBlockUnder(float value) {
      if (mc.player.posY < 0.0) {
         return false;
      } else {
         AxisAlignedBB bb = mc.player.getEntityBoundingBox().offset(0.0, (double)(-value), 0.0);
         return mc.world.getCollisionBoxes(mc.player, bb).isEmpty();
      }
   }

   public static float getMoveDirection() {
      double motionX = mc.player.motionX;
      double motionZ = mc.player.motionZ;
      float direction = (float)(Math.atan2(motionX, motionZ) / Math.PI * 180.0);
      return -direction;
   }

   public static boolean airBlockAboveHead() {
      AxisAlignedBB bb = new AxisAlignedBB(mc.player.posX - 0.3, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ + 0.3, mc.player.posX + 0.3, mc.player.posY + (!mc.player.onGround ? 1.5 : 2.5), mc.player.posZ - 0.3);
      return mc.world.getCollisionBoxes(mc.player, bb).isEmpty();
   }

   public static void setEventSpeed(EventMove event, double speed) {
      double forward = (double)mc.player.movementInput.moveForward;
      double strafe = (double)mc.player.movementInput.moveStrafe;
      float yaw = mc.player.rotationYaw;
      if (forward == 0.0 && strafe == 0.0) {
         event.setX(0.0);
         event.setZ(0.0);
      } else {
         if (forward != 0.0) {
            if (strafe > 0.0) {
               yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
               yaw += (float)(forward > 0.0 ? 45 : -45);
            }

            strafe = 0.0;
            if (forward > 0.0) {
               forward = 1.0;
            } else if (forward < 0.0) {
               forward = -1.0;
            }
         }

         event.setX(forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))));
         event.setZ(forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))));
      }

   }

   public static float getSpeed() {
      return (float)Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
   }

   public static void setMotion(double speed) {
      double forward = (double)mc.player.movementInput.moveForward;
      double strafe = (double)mc.player.movementInput.moveStrafe;
      float yaw = mc.player.rotationYaw;
      if (forward == 0.0 && strafe == 0.0) {
         mc.player.motionX = 0.0;
         mc.player.motionZ = 0.0;
      } else {
         if (forward != 0.0) {
            if (strafe > 0.0) {
               yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
               yaw += (float)(forward > 0.0 ? 45 : -45);
            }

            strafe = 0.0;
            if (forward > 0.0) {
               forward = 1.0;
            } else if (forward < 0.0) {
               forward = -1.0;
            }
         }

         double sin = (double)MathHelper.sin((float)Math.toRadians((double)(yaw + 90.0F)));
         double cos = (double)MathHelper.cos((float)Math.toRadians((double)(yaw + 90.0F)));
         mc.player.motionX = forward * speed * cos + strafe * speed * sin;
         mc.player.motionZ = forward * speed * sin - strafe * speed * cos;
      }

   }

   public static void setSpeed(float speed) {
      float yaw = mc.player.rotationYaw;
      float forward = mc.player.movementInput.moveForward;
      float strafe = mc.player.movementInput.moveStrafe;
      if (forward != 0.0F) {
         if (strafe > 0.0F) {
            yaw += (float)(forward > 0.0F ? -45 : 45);
         } else if (strafe < 0.0F) {
            yaw += (float)(forward > 0.0F ? 45 : -45);
         }

         strafe = 0.0F;
         if (forward > 0.0F) {
            forward = 1.0F;
         } else if (forward < 0.0F) {
            forward = -1.0F;
         }
      }

      mc.player.motionX = (double)(forward * speed) * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + (double)(strafe * speed) * Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      mc.player.motionZ = (double)(forward * speed) * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - (double)(strafe * speed) * Math.cos(Math.toRadians((double)(yaw + 90.0F)));
   }

   public static float getAllDirection() {
      float rotationYaw = mc.player.rotationYaw;
      float factor = 0.0F;
      if (mc.player.movementInput.moveForward > 0.0F) {
         factor = 1.0F;
      }

      if (mc.player.movementInput.moveForward < 0.0F) {
         factor = -1.0F;
      }

      if (factor == 0.0F) {
         if (mc.player.movementInput.moveStrafe > 0.0F) {
            rotationYaw -= 90.0F;
         }

         if (mc.player.movementInput.moveStrafe < 0.0F) {
            rotationYaw += 90.0F;
         }
      } else {
         if (mc.player.movementInput.moveStrafe > 0.0F) {
            rotationYaw -= 45.0F * factor;
         }

         if (mc.player.movementInput.moveStrafe < 0.0F) {
            rotationYaw += 45.0F * factor;
         }
      }

      if (factor < 0.0F) {
         rotationYaw -= 180.0F;
      }

      return (float)Math.toRadians((double)rotationYaw);
   }

   public static double getDirectionAll() {
      float rotationYaw = mc.player.rotationYaw;
      float forward = 1.0F;
      if (mc.player.moveForward < 0.0F) {
         rotationYaw += 180.0F;
      }

      if (mc.player.moveForward < 0.0F) {
         forward = -0.5F;
      } else if (mc.player.moveForward > 0.0F) {
         forward = 0.5F;
      }

      if (mc.player.moveStrafing > 0.0F) {
         rotationYaw -= 90.0F * forward;
      }

      if (mc.player.moveStrafing < 0.0F) {
         rotationYaw += 90.0F * forward;
      }

      return Math.toRadians((double)rotationYaw);
   }

   public static void strafe2() {
      if (!mc.gameSettings.keyBindBack.isKeyDown()) {
         strafe2(getSpeed());
      }
   }

   public static void strafePlayer() {
      double yaw = getDirectionAll();
      float speed = getSpeed();
      mc.player.motionX = -Math.sin(yaw) * (double)speed;
      mc.player.motionZ = Math.cos(yaw) * (double)speed;
   }

   public static double[] forward(double speed) {
      float forward = mc.player.movementInput.moveForward;
      float side = mc.player.movementInput.moveStrafe;
      float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
      if (forward != 0.0F) {
         if (side > 0.0F) {
            yaw += (float)(forward > 0.0F ? -45 : 45);
         } else if (side < 0.0F) {
            yaw += (float)(forward > 0.0F ? 45 : -45);
         }

         side = 0.0F;
         if (forward > 0.0F) {
            forward = 1.0F;
         } else if (forward < 0.0F) {
            forward = -1.0F;
         }
      }

      double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double posX = (double)forward * speed * cos + (double)side * speed * sin;
      double posZ = (double)forward * speed * sin - (double)side * speed * cos;
      return new double[]{posX, posZ};
   }

   public static double getBaseSpeed() {
      double baseSpeed = 0.2873;
      if (mc.player.isPotionActive(Potion.getPotionById(1))) {
         int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
         baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
      }

      return baseSpeed;
   }

   public static void strafe() {
      strafe(getSpeed());
   }

   public static void strafe(float speed) {
      if (isMoving()) {
         double yaw = (double)getPlayerDirection();
         mc.player.motionX = -Math.sin(yaw) * (double)speed;
         mc.player.motionZ = Math.cos(yaw) * (double)speed;
      }
   }

   public static void strafe2(float speed) {
      if (isMoving()) {
         double yaw = (double)getAllDirection();
         mc.player.motionX = -Math.sin(yaw) * (double)speed;
         mc.player.motionZ = Math.cos(yaw) * (double)speed;
      }
   }
}
