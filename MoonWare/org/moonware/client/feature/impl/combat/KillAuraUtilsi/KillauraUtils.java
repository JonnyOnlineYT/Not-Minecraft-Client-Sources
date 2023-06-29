package org.moonware.client.feature.impl.combat.KillAuraUtilsi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.combat.AntiBot;
import org.moonware.client.feature.impl.combat.KillAura;
import org.moonware.client.friend.Friend;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.TpsHelper;
import org.moonware.client.helpers.misc.TimerHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class KillauraUtils implements Helper {
    public static TimerHelper timerHelper = new TimerHelper();

    public KillauraUtils() {
    }

    public static boolean canAttack(Entity player) {
        Iterator var2 = MoonWare.friendManager.getFriends().iterator();

        while(var2.hasNext()) {
            Friend friend = (Friend)var2.next();
            if (player.getName().equals(friend.getName())) {
                return false;
            }
        }

        if (player instanceof EntitySlime && !KillAura.Mobs.getBoolValue()) {
            return false;
        } else if (player instanceof EntityMagmaCube && !KillAura.Mobs.getBoolValue()) {
            return false;
        } else if (player instanceof EntityDragon && !KillAura.Mobs.getBoolValue()) {
            return false;
        } else if (player instanceof EntityArmorStand) {
            return false;
        }
        if (MoonWare.featureManager.getFeatureByClass(AntiBot.class).getState() && AntiBot.isBotPlayer.contains(player)) {
            return false;
        } else {
            if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
                if (player instanceof EntityPlayer && !KillAura.Players.getBoolValue()) {
                    return false;
                }

                if (player instanceof EntityAnimal && !KillAura.Players.getBoolValue()) {
                    return false;
                }

                if (player instanceof EntityMob && !KillAura.Mobs.getBoolValue()) {
                    return false;
                }

                if (player instanceof EntityVillager && !KillAura.Villagers.getBoolValue()) {
                    return false;
                }

                if (player instanceof EntityOcelot && !KillAura.Animals.getBoolValue()) {
                    return false;
                }

                if (player instanceof EntityWolf && !KillAura.Animals.getBoolValue()) {
                    return false;
                }

                if (player instanceof EntityEnderman && !KillAura.Mobs.getBoolValue()) {
                    return false;
                }

                if (player.isInvisible() && !KillAura.Invisibles.getBoolValue()) {
                    return false;
                }

            }
            if (player instanceof EntityEnderCrystal && !KillAura.Crystals.get()) {
                return false;
            }
            if (!canSeeEntityAtFov(player, KillAura.fov.getNumberValue() * 2.0F)) {
                return false;
            } else if (!range(player, KillAura.range.getNumberValue())) {
                return false;
            } else if (!player.canEntityBeSeen(Minecraft.player)) {
                return KillAura.walls.getBoolValue();
            } else {
                return player != Minecraft.player;
            }
        }
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffX = entityLiving.posX - Minecraft.player.posX;
        double diffZ = entityLiving.posZ - Minecraft.player.posZ;
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D);
        double difference = angleDifference(yaw, Minecraft.player.rotationYaw);
        return difference <= (double)scope;
    }

    public static double angleDifference(float oldYaw, float newYaw) {
        float yaw = Math.abs(oldYaw - newYaw) % 360.0F;
        if (yaw > 180.0F) {
            yaw = 360.0F - yaw;
        }

        return yaw;
    }

    private static boolean range(Entity entity, float range) {
        return Minecraft.player.getDistanceToEntity(entity) <= range;
    }
    public static TimerHelper oldTimerPvp = new TimerHelper();
    public static boolean canApsAttack() {
        float attackDelay = true ? 0.85F * TpsHelper.getTickRate() / 20.0f : 0.85F;
        return Minecraft.player.getCooledAttackStrength(0.0f) >= attackDelay;
    }
    public static void attackEntity(Entity target) {
        if (target != null && !(Minecraft.player.getHealth() < 0.0F) && !(target instanceof EntityEnderCrystal)) {

            if (!(Minecraft.player.getDistanceToEntity(target) > KillAura.range.getNumberValue())) {
                if (!target.isDead) {
                    //float attackDelay = KillAura.attackCoolDown.getNumberValue() * TpsHelper.getTickRate() / 20.0F;

                    float attackDelay = 0.85F;
                    if ((Minecraft.player.getCooledAttackStrength(0) >= attackDelay)) {
                        Minecraft.playerController.attackEntity(Minecraft.player, rayCast(target, KillAura.range.getNumberValue()));
                        Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                        KillAura.BreakShield((EntityLivingBase) target);
                    }
                }

            }
        }
        if (target instanceof EntityEnderCrystal && target != null) {
            boolean crystal = target instanceof EntityEnderCrystal;
            if ((Minecraft.player.getDistanceToEntity(target) <= 4.5)) {
                boolean blocking = Minecraft.player.isHandActive() && Minecraft.player.getActiveItemStack().getItem()
                        .getItemUseAction(Minecraft.player.getActiveItemStack()) == EnumAction.BLOCK;
                if (blocking) {
                    Minecraft.playerController.onStoppedUsingItem(Minecraft.player);
                }
                boolean needSwap = false;
//                if (CPacketEntityAction.Action.START_SPRINTING) {
//                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
//                    needSwap = true;
//                }
//                minCPS = 10;
//                hitTick = true;
                Minecraft.playerController.attackEntity(Minecraft.player, target);
                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
//                if (getAxe() >= 0 && shieldBreaker.isEnabled(false) && target instanceof EntityPlayer
//                        && isActiveItemStackBlocking((EntityPlayer) target, 1)) {
//                    mc.player.connection.sendPacket(new CPacketHeldItemChange(getAxe()));
////                    KillAura.break(target);
//                    mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
//                }
                if (blocking) {
                    Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(Minecraft.player.getActiveHand()));
                }
                if (needSwap) {
                    Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_SPRINTING));
                }
            }
        }
    }
    public static boolean isActiveItemStackBlocking(EntityPlayer other, int time) {
        if (other.isHandActive() && !other.activeItemStack.isEmpty()) {
            Item item = other.activeItemStack.getItem();
            if (item.getItemUseAction(other.activeItemStack) != EnumAction.BLOCK) {
                return false;
            } else {
                return item.getMaxItemUseDuration(other.activeItemStack) - other.activeItemStackUseCount >= time;
            }
        } else {
            return false;
        }
    }
    public static EntityLivingBase getSortEntities() {
        List<EntityLivingBase> entity = new ArrayList();
        Iterator var2 = Minecraft.world.loadedEntityList.iterator();

        while(true) {
            while(true) {
                EntityLivingBase player;
                do {
                    do {
                        Entity e;
                        do {
                            if (!var2.hasNext()) {
                                String mode = KillAura.sortMode.getOptions();
                                if (mode.equalsIgnoreCase("Distance")) {
                                    EntityPlayerSP var10001 = Minecraft.player;
                                    var10001.getClass();
                                    entity.sort(Comparator.comparingDouble(var10001::getDistanceToEntity));
                                } else if (mode.equalsIgnoreCase("Crosshair")) {
                                    entity.sort(Comparator.comparingDouble(KillauraUtils::Angle));
                                } else if (mode.equalsIgnoreCase("Health")) {
                                    entity.sort((o1, o2) -> {
                                        return (int)(o1.getHealth() - o2.getHealth());
                                    });
                                } else if (mode.equalsIgnoreCase("Higher Armor")) {
                                    entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue).reversed());
                                } else if (mode.equalsIgnoreCase("Lowest Armor")) {
                                    entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue));
                                }

                                if (KillAura.typeMode.currentMode.equalsIgnoreCase("Single") && KillAura.target != null && entity.contains(KillAura.target)) {
                                    entity.removeIf((x) -> {
                                        return x != KillAura.target;
                                    });
                                }

                                if (entity.isEmpty()) {
                                    return null;
                                }

                                return entity.get(0);
                            }

                            e = (Entity)var2.next();
                        } while(!(e instanceof EntityLivingBase));

                        player = (EntityLivingBase)e;
                    } while(!(Minecraft.player.getDistanceToEntity(player) < KillAura.range.getNumberValue()));
                } while(!canAttack(player));

                if (player.getHealth() > 0.0F && !player.isDead) {
                    entity.add(player);
                } else {
                    entity.remove(player);
                }
            }
        }
    }

    public static float Angle(EntityLivingBase entity) {
        double diffX = entity.posX - Minecraft.player.posX;
        double diffZ = entity.posZ - Minecraft.player.posZ;
        return (float)Math.abs(MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D - (double) Minecraft.player.rotationYaw));
    }

    public static Entity rayCast(Entity entityIn, double range) {
        Vec3d vec = entityIn.getPositionVector().add(new Vec3d(0.0D, entityIn.getEyeHeight(), 0.0D));
        Vec3d vecPositionVector = Minecraft.player.getPositionVector().add(new Vec3d(0.0D, Minecraft.player.getEyeHeight(), 0.0D));
        AxisAlignedBB axis = Minecraft.player.getEntityBoundingBox().addCoord(vec.xCoord - vecPositionVector.xCoord, vec.yCoord - vecPositionVector.yCoord, vec.zCoord - vecPositionVector.zCoord).expand(1.0D, 1.0D, 1.0D);
        Entity entityRayCast = null;
        Iterator var8 = Minecraft.world.getEntitiesWithinAABBExcludingEntity(Minecraft.player, axis).iterator();

        while(true) {
            while(true) {
                Entity entity;
                do {
                    do {
                        if (!var8.hasNext()) {
                            return entityRayCast;
                        }

                        entity = (Entity)var8.next();
                    } while(!entity.canBeCollidedWith());
                } while(!(entity instanceof EntityLivingBase));

                float size = entity.getCollisionBorderSize();
                AxisAlignedBB axis1 = entity.getEntityBoundingBox().expand(size, size, size);
                RayTraceResult rayTrace = axis1.calculateIntercept(vecPositionVector, vec);
                if (axis1.isVecInside(vecPositionVector)) {
                    if (range >= 0.0D) {
                        entityRayCast = entity;
                        range = 0.0D;
                    }
                } else if (rayTrace != null) {
                    double dist = vecPositionVector.distanceTo(rayTrace.hitVec);
                    if (range == 0.0D || dist < range) {
                        entityRayCast = entity;
                        range = dist;
                    }
                }
            }
        }
    }
}
