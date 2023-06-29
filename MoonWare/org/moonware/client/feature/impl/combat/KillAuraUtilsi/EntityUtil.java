package org.moonware.client.feature.impl.combat.KillAuraUtilsi;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import optifine.Reflector;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.combat.AntiBot;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUtil {
    public enum EnumPriority {
        CLOSEST, CROSSHAIR, HEALTH
    }
    public interface IEntityFilter {
        boolean canRaycast(Entity entity);
    }
    public static boolean isNaked(EntityPlayer player) {
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack legs = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack feet = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

        return chest.isEmpty() && legs.isEmpty() && feet.isEmpty() && head.isEmpty();
    }
    public static double[] interpolate(Entity entity) {
        double partialTicks = Wrapper.MC.getRenderPartialTicks();
        return new double[] { entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks,
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks,
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks };
    }
    public static boolean isValid(Entity entity, double fov, boolean players, boolean friends, boolean naked,
                                  boolean invisible, boolean mobs, boolean animals, boolean throughWalls) {
        if (entity instanceof EntityPlayer
                && AntiBot.isBotPlayer == entity)
            return false;

        if (!RotationUtil.isInFOV(entity, fov))
            return false;

        if (!throughWalls && !Minecraft.player.canEntityBeSeen(entity))
            return false;

        if (naked && entity != Minecraft.player && !MoonWare.friendManager.getFriends().contains(entity.getName())
                && entity instanceof EntityPlayer) {
            return !isNaked((EntityPlayer) entity);
        }

        return isValid(entity, players, friends, invisible, mobs, animals);
    }
    public static boolean isValid(Entity entity, boolean players, boolean friends, boolean self, boolean invisible,
                                  boolean mobs, boolean animals) {
        if (isValid(entity, players, friends, invisible, mobs, animals))
            return true;

        return self && entity == Wrapper.getPlayer() && Minecraft.gameSettings.thirdPersonView != 0;
    }
    public static boolean isValid(Entity entity, boolean players, boolean friends, boolean invisibles, boolean mobs,
                                  boolean animals) {
        if (!(entity instanceof EntityLivingBase) || entity == Minecraft.player
                || entity instanceof EntityArmorStand)
            return false;

        if (((EntityLivingBase) entity).getHealth() <= 0)
            return false;

        if (!invisibles && entity.isInvisible())
            return false;

        if (!friends && MoonWare.friendManager.getFriends().contains(entity.getName()))
            return false;

        if (players && entity instanceof EntityPlayer)
            return true;

        if (mobs && isMonster(entity))
            return true;

        return animals && isAnimal(entity);
    }
    public static boolean isAnimal(Entity e) {
        return e instanceof IAnimals;
    }

    public static boolean isMonster(Entity e) {
        return e instanceof IMob;
    }

    public static List<Entity> getTargets(String priority, double fov, boolean players, boolean friends, boolean naked,
                                          boolean invisible, boolean mobs, boolean animals, boolean throughWalls) {
        return Minecraft.player.world.loadedEntityList.stream()
                .filter(entity -> isValid(entity, fov, players, friends, naked, invisible, mobs, animals, throughWalls))
                .sorted(getPriority(EnumPriority.valueOf(priority.toUpperCase()))).collect(Collectors.toList());
    }
    public static Comparator<Entity> getPriority(EnumPriority priority) {
        Comparator<Entity> comparator;

        if (priority.equals(EnumPriority.CROSSHAIR)) {
            comparator = Comparator.comparing(e -> {
                Vec3d center = e.getEntityBoundingBox().getCenter();

                double diffX = center.xCoord - Wrapper.getPlayer().posX;
                double diffY = center.yCoord - Wrapper.getPlayer().getEyeHeight();
                double diffZ = center.zCoord - Wrapper.getPlayer().posZ;

                double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

                float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
                float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

                return -(Math.abs(MathHelper.wrapDegrees(yaw - Wrapper.getPlayer().rotationYaw))
                        + Math.abs(MathHelper.wrapDegrees(pitch - Wrapper.getPlayer().rotationPitch)));
            });
        } else if (priority.equals(EnumPriority.HEALTH)) {
            comparator = Comparator.comparing(e -> -((EntityLivingBase) e).getHealth());
        } else {
            comparator = Comparator.comparing(e -> -Wrapper.getPlayer().getDistanceToEntity(e));
        }

        return comparator;
    }
    public static Entity getTarget(String priority, double range, double fov, boolean players, boolean friends,
                                   boolean naked, boolean invisible, boolean mobs, boolean animals, boolean throughWalls) {
        Entity entity = null;
        for (Entity e : getTargets(priority, fov, players, friends, naked, invisible, mobs, animals, throughWalls)) {
            if (e instanceof EntityLivingBase) {
                if (Wrapper.getPlayer().getDistanceToEntity(e) <= range)
                    entity = e;
            }
        }
        return entity;
    }
    public static Entity raycastEntity(double range, float yaw, float pitch,
                                       IEntityFilter entityFilter) {
        Entity renderViewEntity = Minecraft.getRenderViewEntity();

        if (renderViewEntity != null && Minecraft.world != null) {
            double blockReachDistance = range;
            Vec3d eyePosition = renderViewEntity.getPositionEyes(1F);

            float yawCos = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
            float yawSin = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
            float pitchCos = -MathHelper.cos(-pitch * 0.017453292F);
            float pitchSin = MathHelper.sin(-pitch * 0.017453292F);

            Vec3d entityLook = new Vec3d(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
            Vec3d vector = eyePosition.addVector(entityLook.xCoord * blockReachDistance,
                    entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance);
            List<Entity> entityList = Minecraft.world.getEntitiesInAABBexcluding(renderViewEntity,
                    renderViewEntity.getEntityBoundingBox()
                            .addCoord(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance,
                                    entityLook.zCoord * blockReachDistance)
                            .expand(1D, 1D, 1D),
                    Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));

            Entity pointedEntity = null;

            for (Entity entity : entityList) {
                if (!entityFilter.canRaycast(entity))
                    continue;

                float collisionBorderSize = entity.getCollisionBorderSize();
                AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(collisionBorderSize,
                        collisionBorderSize, collisionBorderSize);
                RayTraceResult movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);

                if (axisAlignedBB.isVecInside(eyePosition)) {
                    if (blockReachDistance >= 0.0D) {
                        pointedEntity = entity;
                        blockReachDistance = 0.0D;
                    }
                } else if (movingObjectPosition != null) {
                    double eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec);

                    boolean flag1 = false;

                    if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                        flag1 = Reflector.callBoolean(entity, Reflector.ForgeEntity_canRiderInteract);
                    }

                    if (eyeDistance < blockReachDistance || blockReachDistance == 0.0D) {
                        if (entity == renderViewEntity.getRidingEntity() && !flag1) {
                            if (blockReachDistance == 0.0D)
                                pointedEntity = entity;
                        } else {
                            pointedEntity = entity;
                            blockReachDistance = eyeDistance;
                        }
                    }
                }
            }

            return pointedEntity;
        }

        return null;
    }
    private static Minecraft mc = Minecraft.getMinecraft();
    public static boolean isBlockAboveHead() {
        AxisAlignedBB bb = new AxisAlignedBB(Minecraft.player.posX - 0.3,
                Minecraft.player.posY + (double) Minecraft.player.getEyeHeight(), Minecraft.player.posZ + 0.3,
                Minecraft.player.posX + 0.3, Minecraft.player.posY + 2.5, Minecraft.player.posZ - 0.3);
        return !Minecraft.player.world.getCollisionBoxes(Minecraft.player, bb).isEmpty();
    }
}
