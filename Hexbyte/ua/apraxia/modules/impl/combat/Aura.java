package ua.apraxia.modules.impl.combat;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import org.lwjgl.opengl.GL11;

import ua.apraxia.Hexbyte;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventEntitySync;
import ua.apraxia.eventapi.events.impl.player.EventInteractWithEntity;
import ua.apraxia.eventapi.events.impl.player.EventPreMotion;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.eventapi.events.impl.render.EventRender2D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.impl.display.TargetHUD;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.math.AdvancedCast;
import ua.apraxia.utility.math.CastUtility;
import ua.apraxia.utility.math.RaycastUtility;
import ua.apraxia.utility.math.RotationsUtility;
import ua.apraxia.utility.other.ChatUtility;
import ua.apraxia.utility.other.MoveUtility;
import ua.apraxia.vecmath.Vector2f;

import java.util.*;
import java.util.Map.Entry;

public class Aura extends Module {
    public static Vector2f serverRotation = new Vector2f(), visibleRotation = new Vector2f();

    public SliderSetting fov = new SliderSetting("FOV", 0, 0, 180, 0.5f);
    public SliderSetting distance = new SliderSetting("Distance",  2, 0, 6, 0.5f);
    public SliderSetting rotate = new SliderSetting("Rotate",  2, 0, 3, 0.5f);
    public ModeSetting rotation = new ModeSetting("Rotation", "Matrix", "Nexus");
    public ModeSetting targets = new ModeSetting("Targets", "Players", "Mobs", "Animals", "Friends",  "Villagers",  "Slimes", "Crystals");
    public BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", true);
    public BooleanSetting weaponOnly = new BooleanSetting("Weapon Only", true);
    public BooleanSetting clientLook = new BooleanSetting("Client Look", true);
    public BooleanSetting shieldBreaker = new BooleanSetting("Shield Breaker", true);
    public BooleanSetting ignoreNaked = new BooleanSetting("Ignore Naked", true);
    public BooleanSetting resolver = new BooleanSetting("Resolve Position", true);
    public BooleanSetting ignoreInvisible = new BooleanSetting("Ignore Invisibles", true);
    public BooleanSetting criticals = new BooleanSetting("Criticals", true);
    public BooleanSetting criticals_autojump = new BooleanSetting("Auto Jump", true);
    public BooleanSetting noInteract = new BooleanSetting("NoInteract", true);
    public BooleanSetting shieldDesync = new BooleanSetting("Shield Desync", true);
    public BooleanSetting shieldDesyncOnlyOnAura = new BooleanSetting("Wait Target", true);
    public static EntityLivingBase target;
    public static double prevCircleStep, circleStep;
    public static boolean hitTick;
    public int nextShieldTime;
    public float prevAdditionYaw;
    public static long consumeTime;
    public static int minCPS;
    public int addition, swapBackAxe;
    public boolean thisContextRotatedBefore, thisContextAttackedBefore, requestForAttack, attackAllowed;
    public static Aura instance;

    public Aura() {
        super("Aura", Categories.Combat);
        addSetting(fov, distance, rotate, rotation, targets, ignoreWalls, weaponOnly, clientLook, shieldBreaker, ignoreNaked, resolver, ignoreInvisible, criticals, criticals_autojump, noInteract, shieldDesync, shieldDesyncOnlyOnAura);
        instance = this;
    }
    @EventTarget
    public void onUpdate(EventInteractWithEntity event) {
        EventInteractWithEntity eiwe = (EventInteractWithEntity) event;
        if (Aura.target != null) {
            eiwe.setCanceled();
        }
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (this.resolver.value) {
            this.resolvePlayers();
        }
        this.aura((EventUpdate) event);
        if (this.resolver.value) {
            this.releaseResolver();
        }
        if (mc.player.onGround && !MoveUtility.isInLiquid() && !mc.player.isOnLadder() && !mc.player.isInWeb
                && !mc.player.isPotionActive(MobEffects.SLOWNESS) && Hexbyte.getInstance().moduleManagment.getModule(Aura.class).isModuleState() && target != null
                && criticals_autojump.value) {
            mc.player.jump();
        }
    }

    @EventTarget
    public void onRender(EventPreMotion e) {
        /*    if (target != null) {
                if (!isEntityValid(target)) {
                    target = null;
                }
            }
            if (target == null) {
                target = findTarget();
            }

          if (target != null) {
                mc.player.rotationYawHead = visibleRotation.x;
                mc.player.renderYawOffset = visibleRotation.x;
                mc.player.rotationPitchHead = visibleRotation.y;
                (e).setYaw(serverRotation.x);
                (e).setPitch(serverRotation.y);
            }

            if (target == null) {
                return;
            } */
    };
    @EventTarget
    public void onRender2D(EventEntitySync event) {
        if (Hexbyte.getInstance().moduleManagment.getModule(Aura.class).isModuleState() && target != null) {
            mc.player.rotationYawHead = visibleRotation.x;
            mc.player.renderYawOffset = visibleRotation.x;
            mc.player.rotationPitchHead = visibleRotation.y;
            if (clientLook.value) {
                mc.player.rotationYaw = visibleRotation.x;
                mc.player.rotationPitch = visibleRotation.y;
            }
            (event).setYaw(serverRotation.x);
            (event).setPitch(serverRotation.y);
        }
    }
    public void resolvePlayers() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player instanceof EntityOtherPlayerMP) {
                ((EntityOtherPlayerMP) player).resolve();
            }
        }
    }

    public void releaseResolver() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player instanceof EntityOtherPlayerMP) {
                ((EntityOtherPlayerMP) player).releaseResolver();
            }
        }
    }

    public boolean weaponOnly() {
        if (!weaponOnly.value) {
            return true;
        }
        return (mc.player.getHeldItemMainhand().getItem() instanceof ItemSword
                || mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe);
    }

    public void aura(EventUpdate event) {
        boolean shieldDesyncActive = shieldDesync.value;
        if (shieldDesyncOnlyOnAura.value && target == null) {
            shieldDesyncActive = false;
        }
        if (isActiveItemStackBlocking(mc.player, 4 + new Random().nextInt(4)) && shieldDesyncActive
                && mc.player.isHandActive()) {
            mc.playerController.onStoppedUsingItem(mc.player);
        }
        if (minCPS > 0) {
            minCPS--;
        }
        if (!Hexbyte.getInstance().moduleManagment.getModule(Aura.class).isModuleState()) {
            target = null;
            serverRotation.x = mc.player.rotationYaw;
            serverRotation.y = mc.player.rotationPitch;
            visibleRotation.x = mc.player.rotationYaw;
            visibleRotation.y = mc.player.rotationPitch;
            return;
        }

        if (target != null) {
            if (!isEntityValid(target)) {
                target = null;
            }

        }

        if (target == null) {
            target = findTarget();
        }

        if (target == null) {
            serverRotation.x = mc.player.rotationYaw;
            serverRotation.y = mc.player.rotationPitch;
            visibleRotation.x = mc.player.rotationYaw;
            visibleRotation.y = mc.player.rotationPitch;
            return;
        }
        if (!weaponOnly()) {
            return;
        }
        this.thisContextRotatedBefore = false;
        attack(target, event);
        if (!this.thisContextRotatedBefore) {
            rotate(target, false);
        }
    }

    public void attack(Entity base, EventUpdate event) {
        if (base instanceof EntityEnderCrystal || (canAttack() && minCPS == 0)) {
            if (getBestHitbox(base, getDistanceToEntity(base)) != null) {
                boolean crystal = base instanceof EntityEnderCrystal;
                if (!crystal)
                    rotate(base, true);
                if (AdvancedCast.instance.getMouseOver(base, serverRotation.x, serverRotation.y,
                        distance.value, ignoreWalls(base)) == base
                        || (crystal && mc.player.getDistance(base) <= 4.5)) {
                    boolean blocking = mc.player.isHandActive() && mc.player.getActiveItemStack().getItem()
                            .getItemUseAction(mc.player.getActiveItemStack()) == EnumAction.BLOCK;
                    if (blocking) {
                        mc.playerController.onStoppedUsingItem(mc.player);
                    }
                    boolean needSwap = false;
                    if (CPacketEntityAction.lastUpdatedSprint) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SPRINTING));
                        needSwap = true;
                    }
                    minCPS = 10;
                    hitTick = true;
                    mc.playerController.attackEntity(mc.player, base);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    if (getAxe() >= 0 && shieldBreaker.value && base instanceof EntityPlayer
                            && isActiveItemStackBlocking((EntityPlayer) base, 1)) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(getAxe()));
                        shieldBreaker((EntityPlayer) base);
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                    }
                    if (blocking) {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                    }
                    if (needSwap) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SPRINTING));
                    }
                }
            }
        }
    }

    public double getDistanceToEntity(Entity entity) {
        double dstValue = distance.value;
        if (entity instanceof EntityEnderCrystal) {
            return rotation.is("Matrix") ? 4.5 : dstValue;
        }
        return dstValue;
    }

    public void shieldBreaker(EntityPlayer base) {
        mc.playerController.attackEntityNoEvent(mc.player, base);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public boolean isNakedPlayer(EntityLivingBase base) {
        if (!(base instanceof EntityOtherPlayerMP)) {
            return false;
        }
        return base.getTotalArmorValue() == 0;
    }

    public boolean isInvisible(EntityLivingBase base) {
        if (!(base instanceof EntityOtherPlayerMP)) {
            return false;
        }
        return base.isInvisible();
    }

    public static void init() {
        System.out.println("ez?");
    }

    public void rotate(Entity base, boolean attackContext) {
        this.thisContextRotatedBefore = true;
        Vec3d bestHitbox = getBestHitbox(base, rotate.value + distance.value);
        if (bestHitbox == null) {
            bestHitbox = base.getPositionEyes(1);
        }
        float pitchToHead = 0;
        EntityPlayerSP client = Minecraft.getMinecraft().player;
        {
            double x = bestHitbox.x - client.posX;
            double y = base.getPositionEyes(1).y - client.getPositionEyes(1).y;
            double z = bestHitbox.z - client.posZ;
            double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
            pitchToHead = (float) (-Math.toDegrees(Math.atan2(y, dst)));
        }
        float sensitivity = 1.0001f;
        double x = bestHitbox.x - client.posX;
        double y = bestHitbox.y - client.getPositionEyes(1).y;
        double z = bestHitbox.z - client.posZ;
        double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
        float yawToTarget = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90);
        float pitchToTarget = (float) (-Math.toDegrees(Math.atan2(y, dst)));
        float yawDelta = MathHelper.wrapDegrees(yawToTarget - serverRotation.x) / sensitivity;
        float pitchDelta = (pitchToTarget - serverRotation.y) / sensitivity;
        if (yawDelta > 180) {
            yawDelta = yawDelta - 180;
        }
        int yawDeltaAbs = (int) Math.abs(yawDelta);
        if (yawDeltaAbs < fov.value) {
            visibleRotation = new Vector2f(visibleRotation.x + MathHelper.wrapDegrees(yawToTarget - visibleRotation.x),
                    pitchToHead);
            if(rotation.is("Matrix")) {
                float pitchDeltaAbs = Math.abs(pitchDelta);
                float additionYaw = Math.min(Math.max(yawDeltaAbs, 1), 80);
                float additionPitch = Math.max(attackContext ? pitchDeltaAbs : 1, 2);
                if (Math.abs(additionYaw - this.prevAdditionYaw) <= 3.0f) {
                    additionYaw = this.prevAdditionYaw + 3.1f;
                }
                float newYaw = serverRotation.x + (yawDelta > 0 ? additionYaw : -additionYaw) * sensitivity;
                float newPitch = MathHelper.clamp(
                        serverRotation.y + (pitchDelta > 0 ? additionPitch : -additionPitch) * sensitivity, -90, 90);
                serverRotation.x = newYaw;
                serverRotation.y = newPitch;
                this.prevAdditionYaw = additionYaw;
            }
            if(rotation.is("Nexus")) {
                if (attackContext) {
                    int pitchDeltaAbs = (int) Math.abs(pitchDelta);
                    int additionYaw = (int) yawDeltaAbs;
                    int additionPitch = (int) (pitchDeltaAbs);
                    float newYaw = serverRotation.x + (yawDelta > 0 ? additionYaw : -additionYaw) * sensitivity;
                    float newPitch = MathHelper.clamp(
                            serverRotation.y + (pitchDelta > 0 ? additionPitch : -additionPitch) * sensitivity, -90,
                            90);
                    serverRotation.x = newYaw;
                    serverRotation.y = newPitch;
                    mc.player.rotationYaw = newYaw;
                    mc.player.rotationPitch = newPitch;
                }
            }
        } else {
            visibleRotation.x = mc.player.rotationYaw;
            visibleRotation.y = mc.player.rotationPitch;
        }
    }


    public boolean canAttack() {
        EntityPlayerSP client = mc.player;
        boolean reasonForCancelCritical = client.isPotionActive(MobEffects.SLOWNESS) || client.isOnLadder()
                || MoveUtility.isInLiquid() || client.isInWeb;
        if (client.getCooledAttackStrength(1.5f) < 0.93) {
            return false;
        }
        if (!reasonForCancelCritical && criticals.value) {
            int r = (int) mc.player.posY;
            int c = (int) Math.ceil(mc.player.posY);
            if (r != c && mc.player.onGround && MoveUtility.isBlockAboveHead()) {
                return true;
            }
            return !client.onGround && client.fallDistance > 0;
        }
        return true;
    }

    public static int getAxe() {
        for (int i = 0; i < 9; i++) {
            ItemStack s = mc.player.inventory.getStackInSlot(i);
            if (s.getItem() instanceof ItemAxe) {
                return i;
            }
        }
        return -1;
    }

    public EntityLivingBase findTarget() {
        List<EntityLivingBase> targets = new ArrayList<>();

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase && isEntityValid((EntityLivingBase) entity)) {
                targets.add((EntityLivingBase) entity);
            }
        }
        targets.sort((e1, e2) -> {
            int dst1 = (int) (Minecraft.player.getDistance(e1) * 1000);
            int dst2 = (int) (Minecraft.player.getDistance(e2) * 1000);
            return dst1 - dst2;
        });

        return targets.isEmpty() ? null : targets.get(0);

    }

    public boolean isEntityValid(EntityLivingBase entity) {
        if(Minecraft.player.getDistance(entity) > distance.value){
            return false;
        }
        if(entity instanceof EntityArmorStand){
            return false;
        }
        if (ignoreNaked.value) {
            if (isNakedPlayer(entity))
                return false;
        }
        if(entity == Minecraft.player){
            return false;
        }
        if (ignoreInvisible.value) {
            if (isInvisible(entity))
                return false;
        }
        if (entity.getHealth() <= 0) {
            return false;
        }
        if (!targetsCheck(entity)) {
            return false;
        }
        //  System.out.println(target);
        //  System.out.println(entity.getName());
        if (!ignoreWalls(entity)) {
            if (getBestHitbox(entity, rotate.value + distance.value) == null) {
                return false;
            }
        } else
            return !(entity.getDistance(Minecraft.player) > distance.value + rotate.value);
        return true;


    }

    public Vec3d getBestHitbox(Entity target, double rotateDistance) {
        if (target.getDistanceSqToEntity(target) >= 36) {
            return null;
        }
        Vec3d head = findHitboxCoord(Hitbox.HEAD, target);
        Vec3d chest = findHitboxCoord(Hitbox.CHEST, target);
        Vec3d legs = findHitboxCoord(Hitbox.LEGS, target);
        ArrayList<Vec3d> points = new ArrayList<>(Arrays.asList(head, chest, legs));
        points.removeIf(point -> !isHitBoxVisible(target, point, rotateDistance));
        if (points.isEmpty()) {
            return null;
        }
        points.sort((d1, d2) -> {
            Vector2f r1 = getDeltaForCoord(serverRotation, d1);
            Vector2f r2 = getDeltaForCoord(serverRotation, d2);
            float y1 = Math.abs(r1.y);
            float y2 = Math.abs(r2.y);
            return (int) ((y1 - y2) * 1000);
        });
        return points.get(0);
    }

    public boolean targetsCheck(EntityLivingBase entity) {
        CastUtility castHelper = new CastUtility();
        if (targets.is("Players")) {
            castHelper.apply(CastUtility.EntityType.PLAYERS);
        }
        if (targets.is("Friends")) {
            castHelper.apply(CastUtility.EntityType.FRIENDS);
        }
        if (targets.is("Mobs")) {
            castHelper.apply(CastUtility.EntityType.MOBS);
        }
        if (targets.is("Animals")) {
            castHelper.apply(CastUtility.EntityType.ANIMALS);
        }
        if (targets.is("Villagers")) {
            castHelper.apply(CastUtility.EntityType.VILLAGERS);
        }
        if (entity instanceof EntitySlime) {
            return targets.is("Slimes");
        }

        return CastUtility.isInstanceof(entity, castHelper.build()) != null && !entity.isDead;
    }

    public boolean ignoreWalls(Entity input) {
        if (input instanceof EntityEnderCrystal) {
            return true;
        }
        BlockPos pos = new BlockPos(mc.player.lastReportedPosX, mc.player.lastReportedPosY, mc.player.lastReportedPosZ);
        if (mc.world.getBlockState(pos).getMaterial() != Material.AIR && rotation.is("Matrix")) {
            return true;
        }
        return ignoreWalls.value;
    }

    public static Vector2f getDeltaForCoord(Vector2f rot, Vec3d point) {
        EntityPlayerSP client = Minecraft.getMinecraft().player;
        double x = point.x - client.posX;
        double y = point.y - client.getPositionEyes(1).y;
        double z = point.z - client.posZ;
        double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
        float yawToTarget = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90);
        float pitchToTarget = (float) (-Math.toDegrees(Math.atan2(y, dst)));
        float yawDelta = MathHelper.wrapDegrees(yawToTarget - rot.x);
        float pitchDelta = (pitchToTarget - rot.y);
        return new Vector2f(yawDelta, pitchDelta);
    }

    public boolean isHitBoxVisible(Entity target, Vec3d vector, double dst) {
        return RaycastUtility.getPointedEntity(getRotationForCoord(vector), dst, 1, !ignoreWalls(target),
                target) == target;
    }

    public static Vector2f getRotationForCoord(Vec3d point) {
        EntityPlayerSP client = Minecraft.getMinecraft().player;
        double x = point.x - client.posX;
        double y = point.y - client.getPositionEyes(1).y;
        double z = point.z - client.posZ;
        double dst = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
        float yawToTarget = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90);
        float pitchToTarget = (float) (-Math.toDegrees(Math.atan2(y, dst)));
        return new Vector2f(yawToTarget, pitchToTarget);
    }

    public static Vec3d findHitboxCoord(Hitbox box, Entity target) {
        double yCoord = 0;
        switch (box) {
            case HEAD:
                yCoord = target.getEyeHeight();
                break;
            case CHEST:
                yCoord = target.getEyeHeight() / 2;
                break;
            case LEGS:
                yCoord = 0.05;
                break;
        }
        return target.getPositionVector().add(0, yCoord, 0);
    }

    public static boolean isActiveItemStackBlocking(EntityPlayer other, int time) {
        if (other.isHandActive() && !other.getActiveItemStack().isEmpty()) {
            Item item = other.getActiveItemStack().getItem();
            if (item.getItemUseAction(other.getActiveItemStack()) != EnumAction.BLOCK) {
                return false;
            } else {
                return item.getMaxItemUseDuration(other.getActiveItemStack()) - other.activeItemStackUseCount >= time;
            }
        } else {
            return false;
        }
    }

    public enum Hitbox {
        HEAD, CHEST, LEGS
    }
}