package org.moonware.client.helpers.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import org.moonware.client.helpers.Helper;

public class EntityHelper implements Helper {

    public static double getDistance(double x, double y, double z, double x1, double y1, double z1) {
        double posX = x - x1;
        double posY = y - y1;
        double posZ = z - z1;
        return Math.sqrt(posX * posX + posY * posY + posZ * posZ);
    }
    public static String getName(NetworkPlayerInfo networkPlayerInfoIn) {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().asFormattedString() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }

    public static double getDistance(double x1, double z1, double x2, double z2) {
        double deltaX = x1 - x2;
        double deltaZ = z1 - z2;
        return Math.hypot(deltaX, deltaZ);
    }
    public static int getBestWeapon() {
        int originalSlot = Minecraft.player.inventory.currentItem;
        int weaponSlot = -1;
        float weaponDamage = 1.0f;
        for (int slot = 0; slot < 9; slot = (byte)(slot + 1)) {
            Minecraft.player.inventory.currentItem = slot;
            ItemStack itemStack = Minecraft.player.getHeldItem(EnumHand.MAIN_HAND);
            if (itemStack.getItem() instanceof ItemBow || itemStack.getItem() instanceof ItemPickaxe || itemStack.getItem() instanceof ItemSpade) continue;
            float damage = getItemDamage(itemStack);
            if (!((damage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED)) > weaponDamage)) continue;
            weaponDamage = damage;
            weaponSlot = slot;
        }
        if (weaponSlot != -1) {
            return weaponSlot;
        }
        return originalSlot;
    }
    public static int getItemDamage(ItemStack stack) {
        return stack.getMaxDamage() - stack.getItemDamage();
    }

    public static Entity rayCast(Entity entityIn, double range) {
        Vec3d vec = entityIn.getPositionVector().add(new Vec3d(0, entityIn.getEyeHeight(), 0));
        Vec3d vecPositionVector = Minecraft.player.getPositionVector().add(new Vec3d(0, Minecraft.player.getEyeHeight(), 0));
        AxisAlignedBB axis = Minecraft.player.getEntityBoundingBox().addCoord(vec.xCoord - vecPositionVector.xCoord, vec.yCoord - vecPositionVector.yCoord, vec.zCoord - vecPositionVector.zCoord).expand(1, 1, 1);
        Entity entityRayCast = null;
        for (Entity entity : Minecraft.world.getEntitiesWithinAABBExcludingEntity(Minecraft.player, axis)) {
            if (entity.canBeCollidedWith() && entity instanceof EntityLivingBase) {
                float size = entity.getCollisionBorderSize();
                AxisAlignedBB axis1 = entity.getEntityBoundingBox().expand(size, size, size);
                RayTraceResult rayTrace = axis1.calculateIntercept(vecPositionVector, vec);
                if (axis1.isVecInside(vecPositionVector)) {
                    if (range >= 0) {
                        entityRayCast = entity;
                        range = 0;
                    }
                } else if (rayTrace != null) {
                    double dist = vecPositionVector.distanceTo(rayTrace.hitVec);
                    if (range == 0 || dist < range) {
                        entityRayCast = entity;
                        range = dist;
                    }
                }
            }
        }

        return entityRayCast;
    }

    public static boolean isTeamWithYou(EntityLivingBase entity) {
        if (Minecraft.player != null && entity != null) {
            if (Minecraft.player.getDisplayName() != null && entity.getDisplayName() != null) {
                if (Minecraft.player.getTeam() != null && entity.getTeam() != null && Minecraft.player.getTeam().isSameTeam(entity.getTeam())) {
                    return true;
                }
                String targetName = entity.getDisplayName().asFormattedString().replace("§r", "");
                String clientName = Minecraft.player.getDisplayName().asFormattedString().replace("§r", "");
                return targetName.startsWith("§" + clientName.charAt(1));
            }
        }
        return false;
    }

    public static boolean checkArmor(Entity entity) {
        return ((EntityLivingBase) entity).getTotalArmorValue() < 6;
    }

    public static int getPing(EntityPlayer entityPlayer) {
        return (Minecraft.player.connection.getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime());
    }

    public static double getDistanceOfEntityToBlock(Entity entity, BlockPos pos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, pos.getX(), pos.getY(), pos.getZ());
    }
}
