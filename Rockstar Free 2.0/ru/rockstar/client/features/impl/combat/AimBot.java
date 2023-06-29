package ru.rockstar.client.features.impl.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.MathHelper;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotion;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.utils.combat.EntityHelper;
import ru.rockstar.api.utils.combat.RotationHelper;
import ru.rockstar.api.utils.friend.Friend;
import ru.rockstar.api.utils.math.GCDCalcHelper;
import ru.rockstar.api.utils.math.MathematicHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;


public class AimBot extends Feature
{
    public static NumberSetting range;
    public static BooleanSetting ignoreNakedPlayers;
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting team;
    public static BooleanSetting walls;
    public static BooleanSetting invis;
    public static BooleanSetting click;
    public static NumberSetting fov;
    public static NumberSetting predict;
    public static NumberSetting rotYawSpeed;
    public static NumberSetting rotPitchSpeed;
    public static NumberSetting rotYawRandom;
    public static NumberSetting rotPitchRandom;
    public static ListSetting sort;
    public static ListSetting part;
    
    public AimBot() {
        super("AimBot", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439 \u0430\u0438\u043c \u043d\u0430 \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0435\u0439 \u0432\u043e\u043a\u0440\u0443\u0433 \u0442\u0435\u0431\u044f", 0, Category.COMBAT);
        AimBot.sort = new ListSetting("Assist Sort Mode", "Distance", () -> true, new String[] { "Distance", "Higher Armor", "Lowest Armor", "Health", "Angle", "HurtTime" });
        AimBot.part = new ListSetting("Aim-Part Mode", "Chest", () -> true, new String[] { "Chest", "Head", "Leggings", "Boots" });
        AimBot.range = new NumberSetting("Range", 100, 1.0f, 500.0f, 1, () -> true);
        AimBot.players = new BooleanSetting("Players", true, () -> true);
        AimBot.mobs = new BooleanSetting("Mobs", false, () -> true);
        AimBot.team = new BooleanSetting("Team Check", false, () -> true);
        AimBot.walls = new BooleanSetting("Walls", false, () -> true);
        AimBot.invis = new BooleanSetting("Invisible", false, () -> true);
        AimBot.click = new BooleanSetting("Click Only", false, () -> true);
        AimBot.predict = new NumberSetting("Aim Predict", 0.5f, 0.0f, 5.0f, 0.1f, () -> true);
        AimBot.fov = new NumberSetting("Assist FOV", 180.0f, 5.0f, 180.0f, 5.0f, () -> true);
        AimBot.rotYawSpeed = new NumberSetting("Rotation Yaw Speed", 1.0f, 0.1f, 5.0f, 0.1f, () -> true);
        AimBot.rotPitchSpeed = new NumberSetting("Rotation Pitch Speed", 1.0f, 0.1f, 5.0f, 0.1f, () -> true);
        AimBot.rotYawRandom = new NumberSetting("Yaw Randomize", 1.0f, 0.0f, 3.0f, 0.1f, () -> true);
        AimBot.rotPitchRandom = new NumberSetting("Pitch Randomize", 1.0f, 0.0f, 3.0f, 0.1f, () -> true);
        AimBot.ignoreNakedPlayers = new BooleanSetting("Ignore Naked Players", false, () -> true);
        this.addSettings(AimBot.players, AimBot.mobs, AimBot.walls, AimBot.invis, AimBot.team, AimBot.click, AimBot.range, AimBot.predict, AimBot.fov, AimBot.rotYawSpeed, AimBot.rotPitchSpeed, AimBot.rotPitchRandom, AimBot.rotYawRandom, AimBot.sort, AimBot.part, AimBot.ignoreNakedPlayers);
    }
    
    public static boolean canSeeEntityAtFov(final Entity entityIn, final float scope) {
        final double diffX = entityIn.posX - AimBot.mc.player.posX;
        final double diffZ = entityIn.posZ - AimBot.mc.player.posZ;
        final float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        final double difference = angleDifference(newYaw, AimBot.mc.player.rotationYaw);
        return difference <= scope;
    }
    
    public static double angleDifference(final double a, final double b) {
        float yaw360 = (float)(Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }
    
    public static EntityLivingBase getSortEntities() {
        final List<EntityLivingBase> entity = new ArrayList<EntityLivingBase>();
        for (final Entity e : AimBot.mc.world.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                final EntityLivingBase player = (EntityLivingBase)e;
                if (AimBot.mc.player.getDistanceToEntity(player) >= AimBot.range.getNumberValue() || !canAssist(player)) {
                    continue;
                }
                if (player.getHealth() > 0.0f) {
                    entity.add(player);
                }
                else {
                    entity.remove(player);
                }
            }
        }
        final String sortMode = AimBot.sort.getOptions();
        if (sortMode.equalsIgnoreCase("Angle")) {
            entity.sort((o1, o2) -> (int)(Math.abs(RotationHelper.getAngleEntity(o1) - AimBot.mc.player.rotationYaw) - Math.abs(RotationHelper.getAngleEntity(o2) - AimBot.mc.player.rotationYaw)));
        }
        else if (sortMode.equalsIgnoreCase("Lowest Armor")) {
            entity.sort(Comparator.comparing((Function<? super EntityLivingBase, ? extends Comparable>)EntityLivingBase::getTotalArmorValue));
        }
        else if (sortMode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
        }
        else if (sortMode.equalsIgnoreCase("Distance")) {
        	entity.sort(Comparator.comparingDouble(mc.player::getDistanceToEntity));
        }
        if (entity.isEmpty()) {
            return null;
        }
        return entity.get(0);
    }
    
    public static boolean canAssist(final EntityLivingBase player) {
        for (final Friend friend : Main.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityWaterMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !AimBot.players.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !AimBot.mobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityWaterMob && !AimBot.mobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !AimBot.mobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !AimBot.mobs.getBoolValue()) {
                return false;
            }
        }
        if (AimBot.ignoreNakedPlayers.getBoolValue() && player instanceof EntityPlayer && EntityHelper.checkArmor(player)) {
            return false;
        }
        if (player.isInvisible() && !AimBot.invis.getBoolValue()) {
            return false;
        }
        if (AimBot.ignoreNakedPlayers.getBoolValue() && EntityHelper.checkArmor(player)) {
            return false;
        }
        if (!canSeeEntityAtFov(player, AimBot.fov.getNumberValue() * 2.0f)) {
            return false;
        }
        if (AimBot.team.getBoolValue() && EntityHelper.isTeamWithYou(player)) {
            return false;
        }
        if (!player.canEntityBeSeen(AimBot.mc.player)) {
            return AimBot.walls.getBoolValue();
        }
        return player != AimBot.mc.player;
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotionUpdate event) {
        final EntityLivingBase entity = getSortEntities();
        if (entity != null && AimBot.mc.player.getDistanceToEntity(entity) <= AimBot.range.getNumberValue() && entity != AimBot.mc.player) {
            final float[] rots = this.getRotationsForAssist(entity);
            if (AimBot.click.getBoolValue() && !AimBot.mc.gameSettings.keyBindAttack.isKeyDown()) {
                return;
            }
            if (canAssist(entity) && entity.getHealth() > 0.0f) {
                AimBot.mc.player.rotationYaw = rots[0];
                AimBot.mc.player.rotationPitch = rots[1];
            }
        }
    }
    
    private float[] getRotationsForAssist(final EntityLivingBase entityIn) {
        final float yaw = RotationHelper.updateRotation(GCDCalcHelper.getFixedRotation(AimBot.mc.player.rotationYaw + MathematicHelper.randomizeFloat(-AimBot.rotYawRandom.getNumberValue(), AimBot.rotYawRandom.getNumberValue())), this.getRotation(entityIn, 10)[0], AimBot.rotYawSpeed.getNumberValue() * 10.0f);
        final float pitch = RotationHelper.updateRotation(GCDCalcHelper.getFixedRotation(AimBot.mc.player.rotationPitch + MathematicHelper.randomizeFloat(-AimBot.rotPitchRandom.getNumberValue(), AimBot.rotPitchRandom.getNumberValue())), this.getRotation(entityIn, 10)[1], AimBot.rotPitchSpeed.getNumberValue() * 10.0f);
        return new float[] { yaw, pitch };
    }
    
    private float[] getRotation(final Entity e, final float predictValue) {
        final String mode = AimBot.part.getOptions();
        float aimPoint = 0.0f;
        if (mode.equalsIgnoreCase("Head")) {
            aimPoint = 0.0f;
        }
        else if (mode.equalsIgnoreCase("Chest")) {
            aimPoint = 0.5f;
        }
        else if (mode.equalsIgnoreCase("Leggings")) {
            aimPoint = 0.9f;
        }
        else if (mode.equalsIgnoreCase("Boots")) {
            aimPoint = 1.3f;
        }
        final double xDelta = e.posX + (e.posX - e.prevPosX) * predictValue - AimBot.mc.player.posX - AimBot.mc.player.motionX * predictValue;
        final double zDelta = e.posZ + (e.posZ - e.prevPosZ) * predictValue - AimBot.mc.player.posZ - AimBot.mc.player.motionZ * predictValue;
        final double diffY = e.posY + e.getEyeHeight() - (AimBot.mc.player.posY + AimBot.mc.player.getEyeHeight() + aimPoint);
        final double distance = MathHelper.sqrt(xDelta * xDelta + zDelta * zDelta);
        float yaw = (float)(MathHelper.atan2(zDelta, xDelta) * 180.0 / 3.141592653589793 - 90.0) + MathematicHelper.randomizeFloat(-AimBot.rotYawRandom.getNumberValue(), AimBot.rotYawRandom.getNumberValue());
        float pitch = (float)(-(MathHelper.atan2(diffY, distance) * 180.0 / 3.141592653589793)) + MathematicHelper.randomizeFloat(-AimBot.rotPitchRandom.getNumberValue(), AimBot.rotPitchRandom.getNumberValue());
        yaw = AimBot.mc.player.rotationYaw + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(yaw - AimBot.mc.player.rotationYaw));
        pitch = AimBot.mc.player.rotationPitch + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(pitch - AimBot.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        return new float[] { yaw, pitch };
    }
}