package me.wintware.client.module.player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {
   Random random;

   public Scaffold() {
      super("Scaffold", Category.Player);
   }

   private boolean hasNeighbour(BlockPos blockPos) {
      EnumFacing[] arrayOfEnumFacing;
      int i = (arrayOfEnumFacing = EnumFacing.values()).length;

      for(byte b = 0; b < i; ++b) {
         EnumFacing side = arrayOfEnumFacing[b];
         BlockPos neighbour = blockPos.offset(side);
         if (!mc.world.getBlockState(neighbour).getMaterial().isReplaceable()) {
            return true;
         }
      }

      return false;
   }

   public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
      return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, (double)ticks));
   }

   public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
      return getInterpolatedAmount(entity, ticks, ticks, ticks);
   }

   public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
      return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
   }

   @EventTarget
   public void onPreMotion(EventPreMotionUpdate event) {
      event.setPitch(90.0F);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      double x = mc.player.lastTickPosX;
      double z = mc.player.lastTickPosZ;
      double y = mc.player.lastTickPosY;
      if (mc.player.onGround) {
         MovementUtil.setSpeed(0.0010000000474974513D);
      }

      Vec3d vec3d = getInterpolatedPos(mc.player, 5000.0F);
      BlockPos blockPos = (new BlockPos(vec3d)).down();
      BlockPos belowBlockPos = blockPos.down();
      if (mc.world.getBlockState(blockPos).getMaterial().isReplaceable()) {
         int newSlot = -1;

         int oldSlot;
         for(oldSlot = 0; oldSlot < 9; ++oldSlot) {
            ItemStack stack = mc.player.inventory.getStackInSlot(oldSlot);
            if (stack != ItemStack.field_190927_a && stack.getItem() instanceof ItemBlock) {
               Block block = ((ItemBlock)stack.getItem()).getBlock();
               List<Block> blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST);
               if (!blackList.contains(block) && !(block instanceof BlockContainer) && Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock() && (!(((ItemBlock)stack.getItem()).getBlock() instanceof BlockFalling) || !mc.world.getBlockState(belowBlockPos).getMaterial().isReplaceable())) {
                  newSlot = oldSlot;
                  break;
               }
            }
         }

         if (newSlot != -1) {
            oldSlot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = newSlot;
            if (!this.hasNeighbour(blockPos)) {
               EnumFacing[] arrayOfEnumFacing;
               int j = (arrayOfEnumFacing = EnumFacing.values()).length;
               byte b = 0;

               while(true) {
                  if (b >= j) {
                     return;
                  }

                  EnumFacing side = arrayOfEnumFacing[b];
                  BlockPos neighbour = blockPos.offset(side);
                  if (this.hasNeighbour(neighbour)) {
                     blockPos = neighbour;
                     break;
                  }

                  ++b;
               }
            }

            placeBlockScaffold(blockPos);
            mc.player.inventory.currentItem = oldSlot;
         }
      }
   }

   private void setSneaking(boolean b) {
      KeyBinding sneakBinding = mc.gameSettings.keyBindSneak;

      try {
         Field field = sneakBinding.getClass().getDeclaredField("pressed");
         field.setAccessible(true);
         field.setBoolean(sneakBinding, b);
      } catch (IllegalAccessException | NoSuchFieldException var4) {
         var4.printStackTrace();
      }

   }

   public static boolean placeBlockScaffold(BlockPos pos) {
      Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ);
      EnumFacing[] arrayOfEnumFacing;
      int i = (arrayOfEnumFacing = EnumFacing.values()).length;

      for(byte b = 0; b < i; ++b) {
         EnumFacing side = arrayOfEnumFacing[b];
         BlockPos neighbor = pos.offset(side);
         EnumFacing side2 = side.getOpposite();
         if (eyesPos.squareDistanceTo((new Vec3d(pos)).addVector(0.5D, 0.5D, 0.5D)) < eyesPos.squareDistanceTo((new Vec3d(neighbor)).addVector(0.5D, 0.5D, 0.5D)) && canBeClicked(neighbor)) {
            Vec3d hitVec = (new Vec3d(neighbor)).addVector(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
            if (eyesPos.squareDistanceTo(hitVec) <= 18.0625D) {
               faceVectorPacketInstant(hitVec);
               processRightClickBlock(neighbor, side2, hitVec);
               mc.player.swingArm(EnumHand.MAIN_HAND);
               mc.rightClickDelayTimer = 15;
               return true;
            }
         }
      }

      return false;
   }

   private static PlayerControllerMP getPlayerController() {
      return Minecraft.getMinecraft().playerController;
   }

   public static void processRightClickBlock(BlockPos pos, EnumFacing side, Vec3d hitVec) {
      getPlayerController().processRightClickBlock(mc.player, mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
   }

   public static IBlockState getState(BlockPos pos) {
      return mc.world.getBlockState(pos);
   }

   public static Block getBlock(BlockPos pos) {
      return getState(pos).getBlock();
   }

   public static boolean canBeClicked(BlockPos pos) {
      return getBlock(pos).canCollideCheck(getState(pos), false);
   }

   public static void faceVectorPacketInstant(Vec3d vec) {
      float[] rotations = getNeededRotations2(vec);
   }

   private static float[] getNeededRotations2(Vec3d vec) {
      Vec3d eyesPos = getEyesPos();
      double diffX = vec.xCoord - eyesPos.xCoord;
      double diffY = vec.yCoord - eyesPos.yCoord;
      double diffZ = vec.zCoord - eyesPos.zCoord;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
   }

   public static Vec3d getEyesPos() {
      return new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ);
   }

   public boolean isAirBlock(Block block) {
      if (block.getMaterial((IBlockState)null).isReplaceable()) {
         return !(block instanceof BlockSnow);
      } else {
         return false;
      }
   }

   public void onDisable() {
      super.onDisable();
      this.setSneaking(false);
   }
}
