package org.spray.heaven.util.combat;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.BlockUtil;

import java.util.Objects;

public class DamageUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float finalDamage = 1.0f;
        try {
            float doubleExplosionSize = 12.0F;
            double distancedSize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
            double blockDensity =
                    entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
            double v = (1.0D - distancedSize) * blockDensity;
            float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));

            if (entity instanceof EntityLivingBase) {
                finalDamage = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage),
                        new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
            }
        } catch (NullPointerException ignored) {
        }

        return finalDamage;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(),
                    (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp(k, 0.0F, 20.0F);
            damage *= 1.0F - f / 25.0F;

            if (entity.isPotionActive(Objects.requireNonNull(Potion.getPotionById(11)))) {
                damage = damage - (damage / 4);
            }
            damage = Math.max(damage, 0.0F);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(),
                (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    public static float calculateDamageThreaded(double posX, double posY, double posZ, boolean obsidian,
                                                PlayerInfo playerInfo) {
        return calculateDamageThreaded(posX, posY, posZ, obsidian, false, playerInfo);
    }

    public static float calculateDamageThreaded(double posX, double posY, double posZ, boolean obsidian, boolean self
            , PlayerInfo playerInfo) {
        float finalDamage = 1.0f;
        try {
            float doubleExplosionSize = 12.0F;
            double distancedSize = playerInfo.entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
            float density = obsidian && self ? 0.4f : obsidian ? 0.603f : 1f;
            double blockDensity = getBlockDensity(new Vec3d(posX, posY, posZ), playerInfo.entity.getEntityBoundingBox(),
                    obsidian); // 0.4f
            //		Wrapper.message("Density: " + blockDensity + " " + posX + "/" + posY + "/" + posZ);
            double v = (1.0D - distancedSize) * blockDensity;
            float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));

            finalDamage = getBlastReductionThreaded(playerInfo, getDamageMultiplied(damage));
        } catch (NullPointerException ignored) {
        }

        return finalDamage;
    }

    public static float getBlastReductionThreaded(PlayerInfo playerInfo, float damage) {
        damage = CombatRules.getDamageAfterAbsorb(damage, playerInfo.totalArmourValue, playerInfo.armourToughness);

        float f = MathHelper.clamp(playerInfo.enchantModifier, 0.0F, 20.0F);
        damage *= 1.0F - f / 25.0F;

        if (playerInfo.hasResistance) {
            damage = damage - (damage / 4);
        }
        damage = Math.max(damage, 0.0F);
        return damage;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getDifficultyId();
        return damage * (diff == 0 ? 0 : (diff == 2 ? 1 : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float getBlockDensity(Vec3d vec, AxisAlignedBB bb, boolean obsidian) {
        double d0 = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;

        if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D) {
            int j2 = 0;
            int k2 = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                        double d5 = bb.minX + (bb.maxX - bb.minX) * (double) f;
                        double d6 = bb.minY + (bb.maxY - bb.minY) * (double) f1;
                        double d7 = bb.minZ + (bb.maxZ - bb.minZ) * (double) f2;

                        if (rayTraceBlocks(new Vec3d(d5 + d3, obsidian ? d6 - 1 : d6, d7 + d4), vec, obsidian) ==
                                null) {
                            ++j2;
                        }

                        ++k2;
                    }
                }
            }

            return (float) j2 / (float) k2;
        } else {
            return 0.0F;
        }
    }

    public static RayTraceResult rayTraceBlocks(Vec3d start, Vec3d end, boolean obsidian) {
        if (!Double.isNaN(start.xCoord) && !Double.isNaN(start.yCoord) && !Double.isNaN(start.zCoord)) {
            if (!Double.isNaN(end.xCoord) && !Double.isNaN(end.yCoord) && !Double.isNaN(end.zCoord)) {
                int i = MathHelper.floor(end.xCoord);
                int j = MathHelper.floor(end.yCoord);
                int k = MathHelper.floor(end.zCoord);
                int l = MathHelper.floor(start.xCoord);
                int i1 = MathHelper.floor(start.yCoord);
                int j1 = MathHelper.floor(start.zCoord);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = BlockUtil.getState(blockpos);
                Block block = iblockstate.getBlock();

                if ((iblockstate.getCollisionBoundingBox(Wrapper.getWorld(), blockpos) != Block.NULL_AABB) &&
                        block.canCollideCheck(iblockstate, false)) {
                    return iblockstate.collisionRayTrace(Wrapper.getWorld(), blockpos, start, end);
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(start.xCoord) || Double.isNaN(start.yCoord) || Double.isNaN(start.zCoord)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = (double) l + 1.0D;
                    } else if (i < l) {
                        d0 = (double) l + 0.0D;
                    } else {
                        flag2 = false;
                    }

                    if (j > i1) {
                        d1 = (double) i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = (double) i1 + 0.0D;
                    } else {
                        flag = false;
                    }

                    if (k > j1) {
                        d2 = (double) j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = (double) j1 + 0.0D;
                    } else {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = end.xCoord - start.xCoord;
                    double d7 = end.yCoord - start.yCoord;
                    double d8 = end.zCoord - start.zCoord;

                    if (flag2) {
                        d3 = (d0 - start.xCoord) / d6;
                    }

                    if (flag) {
                        d4 = (d1 - start.yCoord) / d7;
                    }

                    if (flag1) {
                        d5 = (d2 - start.zCoord) / d8;
                    }

                    if (d3 == -0.0D) {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D) {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D) {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5) {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        start = new Vec3d(d0, start.yCoord + d7 * d3, start.zCoord + d8 * d3);
                    } else if (d4 < d5) {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        start = new Vec3d(start.xCoord + d6 * d4, d1, start.zCoord + d8 * d4);
                    } else {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        start = new Vec3d(start.xCoord + d6 * d5, start.yCoord + d7 * d5, d2);
                    }

                    l = MathHelper.floor(start.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(start.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(start.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = BlockUtil.getState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (iblockstate1.getMaterial() == Material.PORTAL ||
                            iblockstate1.getCollisionBoundingBox(Wrapper.getWorld(), blockpos) != Block.NULL_AABB) {
                        if (block1.canCollideCheck(iblockstate1, false)) {
                            return iblockstate1.collisionRayTrace(Wrapper.getWorld(), blockpos, start, end);
                        } else {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, start, enumfacing, blockpos);
                        }
                    }
                }

            }
        }
        return null;
    }
}