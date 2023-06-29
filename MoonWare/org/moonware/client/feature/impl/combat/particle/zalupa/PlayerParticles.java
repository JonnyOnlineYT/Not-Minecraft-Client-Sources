package org.moonware.client.feature.impl.combat.particle.zalupa;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.feature.impl.combat.particle.zalupa.Utils.Vec3;
import org.moonware.client.helpers.player.MovementHelper;

import javax.vecmath.Vector3f;
import java.util.ArrayList;

public class PlayerParticles {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static float[] getRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + ent.getEyeHeight() / 4.0F;
        return getRotationFromPosition(x, z, y);
    }
    public static Block getBlock(double offsetX, double offsetY, double offsetZ) {
        return Minecraft.world.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }
    public static void damagePlayer() {
        for (int i = 0; i <= 3 * 15; ++i) {
        }
    }
    public static boolean isBlockUnder() {
        if(Minecraft.player.posY < 0)
            return false;
        for(int off = 0; off < (int) Minecraft.player.posY+2; off += 2){
            AxisAlignedBB bb = Minecraft.player.getEntityBoundingBox().offset(0, -off, 0);
            if(!Minecraft.world.getCollisionBoxes(Minecraft.player, bb).isEmpty()){
                return true;
            }
        }
        return false;
    }
    private static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.player.posX;
        double zDiff = z - Minecraft.player.posZ;
        double yDiff = y - Minecraft.player.posY - 0.6D;
        double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float getMaxFallDist() {
        PotionEffect potioneffect = Minecraft.player.getActivePotionEffect(Potion.getPotionById(373));
        int f2 = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
        return Minecraft.player.getMaxFallHeight() + f2;
        //  int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
        //return mc.thePlayer.getMaxFallHeight() + f;
    }

    public static float getDirection() {
        float yaw = Minecraft.player.rotationYawHead;
        float forward = Minecraft.player.moveForward;
        float strafe = Minecraft.player.moveStrafing;
        yaw += (forward < 0.0F ? 180 : 0);
        if (strafe < 0.0F) {
            yaw += (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
        }
        if (strafe > 0.0F) {
            yaw -= (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
        }
        return yaw * 0.017453292F;
    }

    public static double getBaseMoveSpeed() {
        return MovementHelper.getBaseMoveSpeed();

    }
    public static boolean isInWater() {
        return Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ)).getBlock() instanceof BlockLiquid;
    }

    public static Block getBlock(BlockPos pos) {
        return Minecraft.world.getBlockState(pos).getBlock();
    }

    public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
        return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }

    public static ArrayList<Vector3f> vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
        double d;
        ArrayList positions = new ArrayList();
        double posX = tpX - Minecraft.player.posX;
        double posY = tpY - (Minecraft.player.posY + (double) Minecraft.player.getEyeHeight() + 1.1);
        double posZ = tpZ - Minecraft.player.posZ;
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793 - 90.0);
        float pitch = (float)((- Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ))) * 180.0 / 3.141592653589793);
        double tmpX = Minecraft.player.posX;
        double tmpY = Minecraft.player.posY;
        double tmpZ = Minecraft.player.posZ;
        double steps = 1.0;
        for (d = speed; d < getDistance(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, tpX, tpY, tpZ); d += speed) {
            steps += 1.0;
        }
        for (d = speed; d < getDistance(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, tpX, tpY, tpZ); d += speed) {
            tmpX = Minecraft.player.posX - Math.sin(getDirection(yaw)) * d;
            tmpZ = Minecraft.player.posZ + Math.cos(getDirection(yaw)) * d;
            positions.add(new Vector3f((float)tmpX, (float)(tmpY -= (Minecraft.player.posY - tpY) / steps), (float)tmpZ));
        }
        positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }

    public static float getDirection(float yaw) {
        if (Minecraft.player.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.player.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Minecraft.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.player.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (Minecraft.player.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= 0.017453292f;
    }

    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d2 = y1 - y2;
        double d3 = z1 - z2;
        return MathHelper.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
    }

    /*
    public static void blockHit(Entity en, boolean value) {
        ItemStack stack = mc.player.getCustomNameTag();
        if (mc.thePlayer.getCurrentEquippedItem() != null && en != null && value && stack.getItem() instanceof ItemSword && (double)mc.thePlayer.swingProgress > 0.2) {
            mc.thePlayer.getCurrentEquippedItem().useItemRightClick((World)mc.theWorld, (EntityPlayer)mc.thePlayer);
        }
    }

     */

    public static float getItemAtkDamage(ItemStack itemStack) {
        /*
        Iterator iterator;
        Multimap multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
            double damage;
            Map.Entry entry = (Map.Entry)iterator.next();
            AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
            double d = damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0;
            if (attributeModifier.getAmount() > 1.0) {
                return 1.0f + (float)damage;
            }
            return 1.0f;
        }

         */
        return 1.0f;
    }

    /*
    public static int bestWeapon(Entity target) {
        mc.player.inventory.currentItem = 0;
        int firstSlot = 0;
        int bestWeapon = -1;
        int j = 1;
        for (int i = 0; i < 9; i = (int)((byte)(i + 1))) {
            mc.player.inventory.currentItem = i;
            ItemStack itemStack = mc.player.getHeldItem();
            if (itemStack == null) continue;
            int itemAtkDamage = (int)PlayerParticles.getItemAtkDamage(itemStack);
            //   if ((itemAtkDamage = (int)((float)itemAtkDamage + EnchantmentHelper.getEnchantedItem((ItemStack)itemStack, (EnumCreatureAttribute)EnumCreatureAttribute.UNDEFINED))) <= j) continue;
            j = itemAtkDamage;
            bestWeapon = i;
        }
        if (bestWeapon != -1) {
            return bestWeapon;
        }
        return firstSlot;
    }

    public static void shiftClick(Item i) {
        for (int i1 = 9; i1 < 37; ++i1) {
            ItemStack itemstack = PlayerParticles.mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
            if (itemstack == null || itemstack.getItem() != i) continue;
            PlayerParticles.mc.playerController.windowClick(0, i1, 0, 1, (EntityPlayer)PlayerParticles.mc.thePlayer);
            break;
        }
    }

     */

    public static boolean hotbarIsFull() {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemstack = Minecraft.player.inventory.getStackInSlot(i);
            if (itemstack != null) continue;
            return false;
        }
        return true;
    }

    public static Vec3 getLook(float p_174806_1_, float p_174806_2_) {
        float var3 = MathHelper.cos(-p_174806_2_ * 0.017453292F - 3.1415927F);
        float var4 = MathHelper.sin(-p_174806_2_ * 0.017453292F - 3.1415927F);
        float var5 = -MathHelper.cos(-p_174806_1_ * 0.017453292F);
        float var6 = MathHelper.sin(-p_174806_1_ * 0.017453292F);
        return new Vec3(var4 * var5, var6, var3 * var5);
    }
    public static void tellPlayer(String string) {
        //mc.player.addChatMessage((ITextComponent) string);

    }
    public static boolean isMoving() {
        if ((!Minecraft.player.isCollidedHorizontally) && (!Minecraft.player.isSneaking())) {
            return ((Minecraft.player.movementInput.moveForward != 0.0F || Minecraft.player.movementInput.moveStrafe != 0.0F));
        }
        return false;
    }

    public EntityLivingBase getEntity() {

        return null;
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }

    /*
    public static double getDistanceToFall() {
        double distance = 0.0;
        double i2 = mc.thePlayer.posY;
        while (i2 > 0.0) {
            if (i2 < 0.0) break;
            Block block = BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, i2, mc.thePlayer.posZ));
            if (block.getMaterial() != Material.air && block.isCollidable() && (block.isFullBlock() || block instanceof BlockSlab || block instanceof BlockBarrier || block instanceof BlockStairs || block instanceof BlockGlass || block instanceof BlockStainedGlass)) {
                if (block instanceof BlockSlab) {
                    i2 -= 0.5;
                }
                distance = i2;
                break;
            }
            i2 -= 0.1;
        }
        return mc.thePlayer.posY - distance;
    }

     */
}
