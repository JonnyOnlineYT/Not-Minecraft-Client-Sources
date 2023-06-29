package org.moonware.client.feature.impl.combat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Mouse;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventAttackSilent;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.combat.KillAuraUtilsi.KillauraUtils;
import org.moonware.client.feature.impl.combat.KillAuraUtilsi.RotationHelper;
import org.moonware.client.feature.impl.movement.LiquidWalk;
import org.moonware.client.helpers.math.GCDFix;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.player.InventoryHelper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.helpers.world.EntityHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import static org.moonware.client.helpers.math.SHelper.EaseOutBack2;
import static org.moonware.client.helpers.math.SHelper.Rotate;

public class KillAura extends Feature {
    public static TimerHelper timerHelper = new TimerHelper();
    public static float yaw;
    public static float pitch;
    public float pitch2;
    private int notiTicks;
    public static boolean isAttacking;
    TimerHelper shieldFixerTimer = new TimerHelper();
    public float yaw2;
    public static boolean isBreaked;
    public static EntityLivingBase target;
    public static ListSetting rotationMode = new ListSetting("Rotation Mode", "Matrix", () -> true, "Vanilla", "Matrix", "Sunrise", "Snap", "Custom");
    public static ListSetting typeMode = new ListSetting("Type Mode", "Single", () -> true, "Single", "Switch");
    public static ListSetting sortMode = new ListSetting("Priority Mode", "Distance", () -> typeMode.currentMode.equalsIgnoreCase("Switch"), "Distance", "Health", "Crosshair", "Higher Armor", "Lowest Armor");
    public static NumberSetting fov = new NumberSetting("FOV", "Позволяет редактировать радиус в котором вы можете ударить игрока", 180, 0, 180, 1, () -> true);
    public static NumberSetting attackCoolDown = new NumberSetting("Attack CoolDown", "Редактирует скорость удара", 0.85F, 0.1F, 1F, 0.01F, () -> !rotationMode.currentMode.equals("Snap"));
    public static NumberSetting range = new NumberSetting("AttackRange", "Дистанция в которой вы можете ударить игрока", 3.6F, 3, 6, 0.01f, () -> true);
    public static NumberSetting yawrandom = new NumberSetting("Yaw Random", 1.6f, 0.1f, 20, 0.01F, () -> rotationMode.currentMode.equals("Custom"));
    public static NumberSetting pitchRandom = new NumberSetting("Pitch Random", 1.6f, 0.1f, 20, 0.01F, () -> rotationMode.currentMode.equals("Custom"));
    public static BooleanSetting staticPitch = new BooleanSetting("Static Pitch", false, () -> rotationMode.currentMode.equals("Custom"));
    public static NumberSetting pitchHead = new NumberSetting("Pitch Head", 0.35f, 0.1f, 1.2f, 0.01F, () -> rotationMode.currentMode.equals("Custom"));
    public BooleanSetting rayCast = new BooleanSetting("RayCast", "Проверяет навелась ли ротация на хит-бокс энтити", false, () -> true);
    public static BooleanSetting walls = new BooleanSetting("Walls", "Позволяет бить сквозь стены", true, () -> true);
    public static BooleanSetting onlyCritical = new BooleanSetting("Only Critical", "Бьет в нужный момент для крита", false, () -> true);
    public BooleanSetting spaceOnly = new BooleanSetting("Space Only", "Only Crits будут работать если зажат пробел", false, () -> onlyCritical.getBoolValue());
    public static NumberSetting criticalFallDistance = new NumberSetting("Critical Fall Distance", "Регулировка дистанции до земли для крита", 0.2F, 0.08F, 1F, 0.01f, () -> onlyCritical.getBoolValue());
    public BooleanSetting shieldFixer = new BooleanSetting("ShieldFixer", "Отжимает щит во время удара, помогает обойти Matrix", false, () -> true);
    public NumberSetting fixerDelay = new NumberSetting("Fixer Delay", "Регулировка как долго щит будет отжмиматься (чем больше, тем щит будет дольше отжиматься)", 150.0f, 0.0f, 600.0f, 10.0f, () -> shieldFixer.getBoolValue());
    public BooleanSetting shieldDesync = new BooleanSetting("Shield Desync", false, () -> true);
    public static BooleanSetting shieldBreaker = new BooleanSetting("ShieldBreaker", "Автоматически ломает щит противнику", false, () -> true);
    public static NumberSetting breakerDelay = new NumberSetting("Breaker Delay", 150.0f, 0.0f, 600.0f, 10.0f, () -> shieldBreaker.getBoolValue());

    public static BooleanSetting breakNotifications = new BooleanSetting("Break Notifications", true, () -> shieldBreaker.getBoolValue());
    public static BooleanSetting silentMove = new BooleanSetting("SilentMove", false, () -> true);
    //public static MultipleBoolSetting targetsSetting = new MultipleBoolSetting("Targets", new BooleanSettingComponent("Players", true), new BooleanSettingComponent("Mobs"), new BooleanSettingComponent("Animals"), new BooleanSettingComponent("Villagers"), new BooleanSettingComponent("Invisibles", true));
    public static BooleanSetting Players = new BooleanSetting("Players",true);
    public static BooleanSetting Mobs = new BooleanSetting("Mobs",true);
    public static BooleanSetting Animals = new BooleanSetting("Animals",true);
    public static BooleanSetting Villagers= new BooleanSetting("Villagers",true);
    public static BooleanSetting Invisibles = new BooleanSetting("Invisibles",true);
    public static BooleanSetting Crystals = new BooleanSetting("Crystals",true,() -> false);
    public static BooleanSetting Resolver = new BooleanSetting("Resolve Target", "**TEST**",false);


    public KillAura() {
        super("KillAura", "Автоматически аттакует энтити", Type.Combat);
        addSettings(rotationMode, Resolver, typeMode, sortMode, Players, Mobs, Animals, Villagers, Invisibles, Crystals, fov, attackCoolDown, range, rayCast, yawrandom, pitchRandom, pitchHead, staticPitch, walls, onlyCritical, spaceOnly, criticalFallDistance, shieldBreaker,breakerDelay, breakNotifications, shieldFixer, fixerDelay, shieldDesync, silentMove);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        /* Interact Fix */
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cPacketUseEntity = (CPacketUseEntity) event.getPacket();

            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT) {
                event.setCancelled(true);
            }

            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onPreAttack(EventPreMotion event) {
        String mode = rotationMode.getOptions();

        setSuffix("" + mode);
        /* Sorting */
        target = KillauraUtils.getSortEntities();

        /* хуйня ебаная */
        if (target == null) {
            return;
        }
        /* RayCast */

        if (!rotationMode.currentMode.equals("Snap") && !RotationHelper.isLookingAtEntity(false, yaw, pitch, 0.12f, 0.12f, 0.12f, target, (range.getNumberValue())) && rayCast.getBoolValue()) {
            return;
        }

        /* Only Critical */
        Minecraft.player.jumpTicks = 0;
        BlockPos blockPos = new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 0.1, Minecraft.player.posZ);
        Block block = Minecraft.world.getBlockState(blockPos).getBlock();
        float f2 = Minecraft.player.getCooledAttackStrength(0.5F);
        boolean flag = (f2 > 0.9F);
        if (!flag && onlyCritical.getBoolValue())
            return;
        if (!(!Minecraft.gameSettings.keyBindJump.isKeyDown() && spaceOnly.getBoolValue())) {
            if (MovementHelper.airBlockAboveHead()) {
                if (!(Minecraft.player.fallDistance >= criticalFallDistance.getNumberValue() || block instanceof BlockLiquid || !onlyCritical.getBoolValue() || Minecraft.player.isRiding() || Minecraft.player.isOnLadder() || Minecraft.player.isInLiquid() || Minecraft.player.isInWeb)) {
                    Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    return;
                }
            } else if (!(!(Minecraft.player.fallDistance > 0.0f) || Minecraft.player.onGround || !onlyCritical.getBoolValue() || Minecraft.player.isRiding() || Minecraft.player.isOnLadder() || Minecraft.player.isInLiquid() || Minecraft.player.isInWeb)) {
                Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.STOP_SPRINTING));
                return;
            }
        }
        if (rotationMode.currentMode.equals("Snap") && Minecraft.player.getCooledAttackStrength(0.0f) >= attackCoolDown.getNumberValue()) {
            float[] rots1 = RotationHelper.getRotations(target);
            Minecraft.player.rotationYaw = rots1[0];
            Minecraft.player.rotationPitch = rots1[1];
        }
        KillauraUtils.attackEntity(target);
    }

    @EventTarget
    public void onRotations(EventPreMotion event) {

        String mode = rotationMode.getOptions();

        if (target == null) {
            return;
        }

        if (!target.isDead) {
            /* ROTATIONS */
            float[] matrix = RotationHelper.getRotations(target);
            float[] fake = RotationHelper.getFakeRotations(target);

            float[] custom = RotationHelper.getCustomRotations(target);

            if (Resolver.isToggle()) {
//                    if (target.posY < 0) {
//                        pitch = 1;
//                    } else if (target.posY > 255) {
//                        pitch = 90;
//                    }

                if (Math.abs(target.posX - target.lastTickPosX) > 0.50
                        || Math.abs(target.posZ - target.lastTickPosZ) > 0.50) {
                    target.setEntityBoundingBox(new AxisAlignedBB(target.posX, target.posY, target.posZ,
                            target.lastTickPosX, target.lastTickPosY, target.lastTickPosZ));
                }
            }
            if (mode.equalsIgnoreCase("Matrix")) {
                event.setYaw(matrix[0]);
                event.setPitch(matrix[1]);
                yaw = matrix[0];
                pitch = matrix[1];
                Minecraft.player.renderYawOffset = matrix[0];
                Minecraft.player.rotationYawHead = matrix[0];
                Minecraft.player.rotationPitchHead = matrix[1];
            } else if (mode.equalsIgnoreCase("Sunrise")) {
                float[] sunriseRots = org.moonware.client.helpers.math.RotationHelper.getRotations1(target, true, 0, 0);
                yaw2 = GCDFix.getFixedRotation(EaseOutBack2(yaw2, sunriseRots[0], attackCoolDown.getNumberValue() * 0.7f + 0.3f));
                pitch2 = GCDFix.getFixedRotation(Rotate(pitch2, sunriseRots[1], 0.35f, 2.1f));
                event.setYaw(yaw2);
                event.setPitch(pitch2);

                yaw = yaw2;
                pitch = pitch2;
                Minecraft.player.renderYawOffset = sunriseRots[0];
                Minecraft.player.rotationYawHead = sunriseRots[0];
                Minecraft.player.rotationPitchHead = sunriseRots[1];
            } else if (mode.equalsIgnoreCase("Custom")) {
                event.setYaw(custom[0]);
                event.setPitch(custom[1]);
                yaw = custom[0];
                pitch = custom[1];
                Minecraft.player.renderYawOffset = custom[0];
                Minecraft.player.rotationYawHead = custom[0];
                Minecraft.player.rotationPitchHead = custom[1];
            }

        }
    }
    private int getAxe() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
            if (!(itemStack.getItem() instanceof ItemAxe)) continue;
            return i;
        }
        return 1;
    }
    private final TimerHelper shieldBreakerTimer = new TimerHelper();
    private static int changeSlotCounter;
    @EventTarget
    public void onAttackSilent(EventAttackSilent eventAttackSilent) {
        if (KillAura.mc.player.isBlocking() && KillAura.mc.player.getHeldItemOffhand().getItem() instanceof ItemShield && shieldFixer.getBoolValue() && timerHelper.hasReached(fixerDelay.getNumberValue())) {
            KillAura.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-0.8, -0.8, -0.8), EnumFacing.DOWN));
            KillAura.mc.playerController.processRightClick(KillAura.mc.player, KillAura.mc.world, EnumHand.OFF_HAND);
            timerHelper.reset();
        }
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {
        /* SHIELD Desync */
        if (shieldDesync.getBoolValue() && Minecraft.player.isBlocking() && target != null && Minecraft.player.ticksExisted % 8 == 0) {
            Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.DOWN));
            Minecraft.playerController.processRightClick(Minecraft.player, Minecraft.world, EnumHand.OFF_HAND);
        }
        if (shieldFixer.getBoolValue()) {
            if (target.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                if (Minecraft.gameSettings.keyBindUseItem.isKeyDown()) {
                    Minecraft.gameSettings.keyBindUseItem.pressed = false;
                }
            } else {
                Minecraft.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
            }
        }
    }

    @EventTarget
    public void onSound(EventReceivePacket sound) {

    }

    public static void BreakShield(EntityLivingBase tg) {
        if (shieldBreaker.getBoolValue() && (target.getHeldItemOffhand().getItem() instanceof ItemShield || target.getHeldItemMainhand().getItem() instanceof ItemShield) && target.isHandActive()) {
            if (target.canEntityBeSeen(KillAura.mc.player) && KillAura.mc.player.canEntityBeSeen(target) && RotationHelper.isLookingAtEntity(yaw, pitch, 0.2f, 0.2f, 0.2f, target, range.getNumberValue())) {
                if (timerHelper.hasReached(breakerDelay.getNumberValue())) {
                    if (KillAura.mc.player.inventory.currentItem != InventoryHelper.getAxe()) {
                        int slot = InventoryHelper.getAxe();
                        KillAura.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                        KillAura.mc.playerController.attackEntity(KillAura.mc.player, EntityHelper.rayCast(target, range.getNumberValue()));
                        KillAura.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                        KillAura.mc.player.connection.sendPacket(new CPacketHeldItemChange(KillAura.mc.player.inventory.currentItem));
                        KillAura.mc.player.resetCooldown();
                        timerHelper.reset();
                    }
                    timerHelper.reset();
                }
                if (KillAura.mc.player.inventory.currentItem == InventoryHelper.getAxe()) {
                    KillAura.mc.playerController.attackEntity(KillAura.mc.player, EntityHelper.rayCast(target, range.getNumberValue()));
                    KillAura.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                    KillAura.mc.player.connection.sendPacket(new CPacketHeldItemChange(KillAura.mc.player.inventory.currentItem));
                    changeSlotCounter = -1;
                } else {
                    changeSlotCounter = 0;
                }
            } else if (!(KillAura.mc.player.inventory.currentItem == InventoryHelper.getSwordAtHotbar() || changeSlotCounter != -1 || InventoryHelper.getSwordAtHotbar() == -1 || target.isBlocking() && target.isHandActive() && target.isActiveItemStackBlocking())) {
                KillAura.mc.player.resetCooldown();
                KillAura.mc.player.inventory.currentItem = InventoryHelper.getSwordAtHotbar();
                KillAura.mc.player.connection.sendPacket(new CPacketHeldItemChange(KillAura.mc.player.inventory.currentItem));
                changeSlotCounter = 0;
            }
        }
        boolean aboba = false;
        int slot = InventoryHelper.getAxe();
        if (target == null) {
            return;
        }
        if (InventoryHelper.doesHotbarHaveAxe()) {
            if (shieldBreaker.getBoolValue() && (target.getHeldItemOffhand().getItem() instanceof ItemShield || target.getHeldItemMainhand().getItem() instanceof ItemShield)) {
                if (target.isBlocking() && KillAura.mc.player.getDistanceToEntity(target) < range.getNumberValue() && timerHelper.hasReached(breakerDelay.getNumberValue() * 10.0f)) {
                    if (Resolver.getBoolValue()) {
                        KeyBinding.setKeyBindState(KillAura.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                    }
                    KillAura.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                    KillAura.mc.playerController.attackEntity(KillAura.mc.player, EntityHelper.rayCast(target, range.getNumberValue()));
                    KillAura.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                    KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                    KillAura.mc.player.connection.sendPacket(new CPacketHeldItemChange(KillAura.mc.player.inventory.currentItem));
                    timerHelper.reset();
                } else {
                    if (!MoonWare.featureManager.getFeatureByClass(LiquidWalk.class).getState() && !(Minecraft.player.fallDistance >= criticalFallDistance.getNumberValue()) && onlyCritical.getBoolValue()) {
                        return;
                    }
                    if (KillAura.target.ticksExisted % 3 == 1) {
                        KillauraUtils.attackEntity(target);
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        target = null;
        super.onDisable();
    }
}
