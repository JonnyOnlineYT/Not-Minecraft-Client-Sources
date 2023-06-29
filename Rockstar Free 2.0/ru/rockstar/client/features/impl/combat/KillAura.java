package ru.rockstar.client.features.impl.combat;


import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.*;
import ru.rockstar.api.utils.combat.*;
import ru.rockstar.api.utils.friend.Friend;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.api.utils.other.Util;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.movement.Jesus;
import ru.rockstar.client.features.impl.movement.TargetStrafe;
import ru.rockstar.client.features.impl.visuals.NameProtect;
import ru.rockstar.client.ui.draggable.impl.TargetHudComponent;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import org.apache.logging.log4j.core.lookup.Interpolator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class KillAura extends Feature {

	public static int hitCounter = 0;
    public static EntityLivingBase target;
    public static double healthBarWidth;
    public static double hudHeight;
    private boolean isBreaked;
    public static float yaw;
    public static float pitch;
    public static float progress;
    public static float progress2;
    private long lastMS;
    public static boolean canBlock;
    public static NumberSetting range;
    public static ListSetting clickMode;
    public static NumberSetting fov;
    public static ListSetting breakerMode;
    public static ListSetting rotationMode;
    public static ListSetting targetHudMode;
    public static ListSetting sortMode;
    public static BooleanSetting targetHud;
    public static NumberSetting thudX;
    public static NumberSetting thudY;
    public static BooleanSetting shieldBreak;
    public static BooleanSetting shieldFix;
    public static BooleanSetting onlyCrits;
    public static BooleanSetting stopSprint;
    public static BooleanSetting ignoreNakedPlayer;
    public static BooleanSetting weaponOnly;
    public static BooleanSetting rayTrace;
    public static BooleanSetting walls;
    public static BooleanSetting BetterCrits;
    public static BooleanSetting teamCheck;
    public static BooleanSetting invisiblecheck;
    public static BooleanSetting auramobs;
    public static BooleanSetting auraplayers;
    public static BooleanSetting shieldDesync;
    public static BooleanSetting shieldBypass;
    public static BooleanSetting resolver;
    public static BooleanSetting smartCrits;
    public static NumberSetting hitChance = new NumberSetting("Hit Chance", 75, 10, 100, 5, () -> true);
    public static NumberSetting rotPredict = new NumberSetting("Rotation Predict", 0.05F, 0.0F, 10, 0.001F, () -> true);
    public static NumberSetting fixDelay = new NumberSetting("Shield Fix Delay", 0F, 0, 300, 10F, () -> shieldFix.getBoolValue());
    public static NumberSetting minAps;
    public static NumberSetting maxAps;
    public static NumberSetting attackCoolDown;
    public static BooleanSetting autoBlock;
    public static NumberSetting breakerDelay;
    public static NumberSetting breakerAps;
    public static NumberSetting critFallDistance;
    public static NumberSetting addrange;
    public static BooleanSetting autoShieldUnPress;
    public static BooleanSetting doubleTap;
    public static BooleanSetting autoJump;
    public static BooleanSetting smartRange;
    public static double hurttimeBarWidth;
    List<EntityLivingBase> targets;
    float pitch2 = 0;
    float yaw2 = 0;
    private boolean isBlocking;
    private int changeSlotCounter;
    public static int desynctimer;
    public static ListSetting rotationStrafeMode;
    public static ListSetting strafeMode;

    public KillAura() {
        super("KillAura", "Автоматически аттакует энтити.", 0, Category.COMBAT);
        this.targets = new ArrayList<>();
        sortMode = new ListSetting("Sorting Mode", "Distance", () -> true, "Distance", "Health");
        breakerMode = new ListSetting("Breaker Mode", "New", () -> shieldBreak.getBoolValue(), "New", "Matrix", "AAC");
        rotationStrafeMode = new ListSetting("Rotation Strafe Mode", "Default", () -> !strafeMode.currentMode.equals("None"), new String[] { "Default", "Silent" });
        strafeMode = new ListSetting("Strafe Mode", "None", () -> true, new String[] { "None", "Always-F" });
        targetHudMode = new ListSetting("TargetHud Mode", "Rockstar", () -> targetHud.getBoolValue(), "Astolfo", "Beach", "Celestial Premium", "Nursultan", "Rockstar Old", "Rockstar", "Rockstar New");
        clickMode = new ListSetting("Click Mode", "1.9", () -> true, "1.9", "1.8");
        rotationMode = new ListSetting("Rotation Mode", "Matrix", () -> true, "Matrix New", "Matrix", "Snap", "Client", "AAC", "ReallyWorld", "Legit", "SunRise", "MagicGrief", "Twister", "Rage");
        fov = new NumberSetting("FOV", "Позволяет редактировать радиус в котором вы можете ударить игрока", 180, 0, 180, 1, () -> true);
        
        smartRange = new BooleanSetting("Smart Range", "Киллаура автоматически подбирает значение дистанции", true, () -> true);

        
        range = new NumberSetting("AttackRange", "Дистанция в которой вы можете ударить игрока", 3.6F, 3, 6, 0.05F, () -> !smartRange.getBoolValue());
        addrange = new NumberSetting("AddingRange", "Дистанция прибавленная к основной, в которой киллаура не пытается ударить", 0F, 0, 10, 0.05F, () -> true);
        attackCoolDown = new NumberSetting("Cooldown", "Редактирует скорость удара", 0.85F, 0.1F, 1F, 0.01F, () -> clickMode.currentMode.equals("1.9"));
        minAps = new NumberSetting("Min CPS", "Минимальное количество кликов в секунду", 12, 1, 20, 1, () -> clickMode.currentMode.equals("1.8"), NumberSetting.NumberType.APS);
        maxAps = new NumberSetting("Max CPS", "Максимальное количество кликов в секунду", 13, 1, 20, 1, () -> clickMode.currentMode.equals("1.8"), NumberSetting.NumberType.APS);
        auraplayers = new BooleanSetting("Players", "Позволяет бить игроков", true, () -> true);
        auramobs = new BooleanSetting("Mobs", "Позволяет бить мобов", true, () -> true);
        invisiblecheck = new BooleanSetting("Invisible", "Позволяет бить невидемых существ", true, () -> true);
        walls = new BooleanSetting("Walls", "Позволяет бить сквозь стены", true, () -> true);
        rayTrace = new BooleanSetting("RayTrace", "Проверяет смотрит ли ротация на хитбокс", true, () -> true);
        weaponOnly = new BooleanSetting("Weapon Only", "Позволяет бить только с оружием в руках", false, () -> true);
        ignoreNakedPlayer = new BooleanSetting("Ignore Naked Players", "Не бьет голых игроков", false, () -> true);
        stopSprint = new BooleanSetting("Stop Sprinting", "Автоматически выключает спринт", false, () -> true);
        doubleTap = new BooleanSetting("DoubleTap", "Тапает 2 раза, вместо одного", false, () -> true);
        onlyCrits = new BooleanSetting("OnlyCrits", "Бьет в нужный момент для крита",true, () -> !clickMode.currentMode.equalsIgnoreCase("1.8"));
        
        smartCrits = new BooleanSetting("SmartCrits", "Если вы на земле, бьет не критами", true, () -> onlyCrits.getBoolValue());
        
        BetterCrits = new BooleanSetting("BetterCrits", "Улучшает проходимость хитов", false, () -> onlyCrits.getBoolValue() && !clickMode.currentMode.equalsIgnoreCase("1.8"));
        critFallDistance = new NumberSetting("Criticals Fall Distance", "Регулировка дистанции до земли для крита", 0.2F, 0.1F, 1F, 0.01f, () -> onlyCrits.getBoolValue());
        teamCheck = new BooleanSetting("Team Check", false, () -> true);
        shieldBreak = new BooleanSetting("Break Shield", "Автоматически ломает щит сопернику", false, () -> !clickMode.currentMode.equalsIgnoreCase("1.8"));
        breakerDelay = new NumberSetting("Breaker Delay", "Регулировка ломания щита", 50, 0, 50, 1, () -> shieldBreak.getBoolValue());
        breakerAps = new NumberSetting("Breaker APS", "Регулировка скорости ломания щита", 1, 0, 10, 1, () -> shieldBreak.getBoolValue());
        shieldFix = new BooleanSetting("Shield Fix", "Автоматически зажимает щит(обход)", false, () -> !clickMode.currentMode.equalsIgnoreCase("1.8"));
        shieldBypass = new BooleanSetting("Shield Bypass", "Позволяет бить через щит", false, () -> !clickMode.currentMode.equalsIgnoreCase("1.8"));
        autoShieldUnPress = new BooleanSetting("Auto Shield UnPress", "Автоматически отжимает щит если у соперника топор в руках", false, () -> !clickMode.currentMode.equalsIgnoreCase("1.8"));
        autoBlock = new BooleanSetting("Auto Block", "Автоматически жмет пкм при ударе (нужно для 1.8 серверов)", false, () -> clickMode.currentMode.equalsIgnoreCase("1.8"));
        targetHud = new BooleanSetting("TargetHud", "Отображает хп, еду, броню соперника на экране", true, () -> true);
        autoJump = new BooleanSetting("AutoJump", "Автоматически прыгает", false, () -> true);
        
        resolver = new BooleanSetting("Resolver", "Игнорирует вражеский ShieldDesync", false, () -> true);
        thudX = new NumberSetting("TargetHud X", 360, -500, 500, 1, () -> targetHud.getBoolValue());
        thudY = new NumberSetting("TargetHud Y", 150, -300, 300, 1, () -> targetHud.getBoolValue());
        addSettings(sortMode, clickMode, rotationMode, fov, hitChance, smartRange, range,addrange, attackCoolDown, minAps, maxAps, rotPredict, auraplayers, auramobs, invisiblecheck, walls, rayTrace, weaponOnly, ignoreNakedPlayer, stopSprint, onlyCrits,smartCrits, BetterCrits, critFallDistance, strafeMode,  rotationStrafeMode, teamCheck, doubleTap, shieldBreak, breakerMode, breakerDelay, autoShieldUnPress, shieldFix, fixDelay, shieldBypass, shieldDesync, resolver, autoJump, autoBlock, targetHud, targetHudMode);
    }
    
    @Override
    public void onEnable() {
    	if (target == null) {
    		hitCounter = 0;
    	}
    	this.lastMS = System.currentTimeMillis();
    	this.progress = 0.0f;
    	this.progress2 = 0.0f;
        super.onEnable();
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
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
    public void onUpdate(EventUpdate event) {
    	if (!(target instanceof EntityPlayer))
    		return;
    	if (autoJump.getBoolValue()) {
    		if (mc.player.onGround && target != null) {
    			mc.player.jump();
    		}
    	}
    	if (target == null) {
    		hitCounter = 0;
    	}
    	/*if (shieldFix.getBoolValue()) {
            if (target.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    mc.gameSettings.keyBindUseItem.pressed = false;
                }
            } else {
                mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
            }
        }*/
    }

    @EventTarget
    public void onEventPreMotion(EventPreMotionUpdate mamanooma) {
        if (isToggled()) {
            if (Minecraft.getMinecraft().player.isEntityAlive()) {
                target = KillAuraHelper.getSortEntities();
                if (target == null) {
                    return;
                }
                if (target.getHealth() > 0.0f) {
                	BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ);
                    Block block = mc.world.getBlockState(blockPos).getBlock();
                    
                	if (!Jesus.inWater) {
                		 if (!MovementHelper.isBlockAboveHead()) {
                             if (!(mc.player.fallDistance >= critFallDistance.getNumberValue() || block instanceof BlockLiquid || !onlyCrits.getBoolValue() || mc.player.isRiding() || mc.player.isOnLadder() || mc.player.isInLiquid() || mc.player.isInWeb)) {
                                 mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                                 return;
                             }
                         } else if (!(!(mc.player.fallDistance > 0.0f) || mc.player.onGround || !onlyCrits.getBoolValue() || mc.player.isRiding() || mc.player.isOnLadder() || mc.player.isInLiquid() || mc.player.isInWeb)) {
                             mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                             return;
                         }
                	}
                	
                    if (!RotationHelper.isLookingAtEntity(yaw, pitch, 0.14F, 0.14F, 0.14F, target, RangeHelper.getRange()) && rayTrace.getBoolValue()) {
                        return;
                    }
                    
                    
                    
                    attackEntitySuccess(target);
                }
            }
        }
    }
    
    @EventTarget
    public void jija(Event3D xaski) {
        if (target != null && target.getHealth() > 0.0 && mc.player.getDistanceToEntity(target) <= (ru.rockstar.api.utils.combat.RangeHelper.getRange() + addrange.getNumberValue()) && isToggled()) {
/*
                    double everyTime = 1500;
                    double drawTime = (System.currentTimeMillis() % everyTime);
                    boolean drawMode = drawTime > (everyTime / 2);
                    double drawPercent = drawTime / (everyTime / 2);
                    // true when goes up
                    if (!drawMode) {
                        drawPercent = 1 - drawPercent;
                    } else {
                        drawPercent -= 1;
                    }

                    drawPercent = MathHelper.easeInOutQuad(drawPercent, 2);

                    mc.entityRenderer.disableLightmap();
                    GL11.glPushMatrix();
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glEnable(GL11.GL_LINE_SMOOTH);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    GL11.glShadeModel(7425);
                    mc.entityRenderer.disableLightmap();

                    double radius = 0.4f;
                    double height = target.height + 0.1;
                    double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * xaski.getPartialTicks() - mc.renderManager.viewerPosX;
                    double y = (target.lastTickPosY + (target.posY - target.lastTickPosY) * xaski.getPartialTicks() - mc.renderManager.viewerPosY) + height * drawPercent;
                    double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * xaski.getPartialTicks() - mc.renderManager.viewerPosZ;
                    double eased = (height / 3) * ((drawPercent > 0.5) ? 1 - drawPercent : drawPercent) * ((drawMode) ? -1 : 1);

                    for (int lox = 0; lox < 360; lox += 5) {
                        Color color = ClientHelper.getClientColor(5, lox, 5);
                        double x1 = x - Math.sin(lox * Math.PI / 180F) * radius;
                        double z1 = z + Math.cos(lox * Math.PI / 180F) * radius;
                        double x2 = x - Math.sin((lox - 5) * Math.PI / 180F) * radius;
                        double z2 = z + Math.cos((lox - 5) * Math.PI / 180F) * radius;
                        GL11.glBegin(GL11.GL_QUADS);
                        DrawHelper.glColor(color, 0f);
                        GL11.glVertex3d(x1, y + eased, z1);
                        GL11.glVertex3d(x2, y + eased, z2);
                        DrawHelper.glColor(color, 255);
                        GL11.glVertex3d(x2, y, z2);
                        GL11.glVertex3d(x1, y, z1);
                        GL11.glEnd();

                        GL11.glBegin(GL_LINE_LOOP);
                        GL11.glVertex3d(x2, y, z2);
                        GL11.glVertex3d(x1, y, z1);
                        GL11.glEnd();
                    }

                    GL11.glEnable(GL11.GL_CULL_FACE);
                    GL11.glShadeModel(7424);
                    GL11.glColor4f(1f, 1f, 1f, 1f);
                    GL11.glDisable(GL11.GL_LINE_SMOOTH);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glPopMatrix();
                      */
        	
            }
        }
        
    
    private boolean isShield(ItemStack itemStack) {
        return (itemStack != null && !itemStack.isEmpty() && itemStack.getItem() instanceof ItemShield);
    }

    private void attackEntitySuccess(EntityLivingBase target) {
        if (target.getHealth() > 0) {
            switch (clickMode.getOptions()) {
                case "1.9":
                    float attackDelay = 0.5f;
                    if (shieldBypass.getBoolValue()) {
                		int slot = InventoryHelper.getAxe();
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                	}
                    String mode = rotationMode.getOptions();
                    if ((mc.player.getCooledAttackStrength(0.5f) >= (mode.equalsIgnoreCase("ReallyWorld") ? 0.95f : attackCoolDown.getNumberValue()) || (target.getHealth() <= 1 && mc.player.ticksExisted % 5 == 0))  && mc.player.getDistanceToEntity(target) <= (Jesus.inWater ? RangeHelper.getRange() : RangeHelper.getRange() - 1)) {
                    	if (!resolver.getBoolValue() || (resolver.getBoolValue() && ResolverHelper.canAttack())) {
                    	if (mode.equalsIgnoreCase("Snap")) {
                    		float[] rotVisual = RotationHelper.getMatrixRotations(target, false, 2, 2);
                    		mc.player.rotationYaw = rotVisual[0];
                            this.yaw = rotVisual[0];
                            this.pitch = rotVisual[1];
                            mc.player.rotationPitch = rotVisual[1];
                    	}
                    	if (BetterCrits.getBoolValue()) {
                    		mc.gameSettings.keyBindSneak.pressed = true;
                    	}
                    	}
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    	if (!resolver.getBoolValue() || (resolver.getBoolValue() && ResolverHelper.canAttack())) {

                        mc.playerController.attackEntity(mc.player, EntityHelper.rayCast(target, RangeHelper.getRange()));
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        
                        //
                        if (doubleTap.getBoolValue() && timerHelper.hasReached(10)) {
                        	 mc.playerController.attackEntity(mc.player, EntityHelper.rayCast(target, RangeHelper.getRange()));
                             mc.player.swingArm(EnumHand.MAIN_HAND);
                             timerHelper.reset();
                        }
                    	}
                        if (breakerMode.getOptions().equalsIgnoreCase("New") && isToggled()) {
                        	int item1 = InventoryHelper.getAxe();
                            if (InventoryHelper.doesHotbarHaveAxe() && shieldBreak.getBoolValue() && timerHelper.hasReached(breakerDelay.getNumberValue() * 10)) {
                                if (InventoryHelper.getAxe() >= 0 && target instanceof EntityPlayer && target.isHandActive() && target.getActiveItemStack().getItem() instanceof ItemShield) {
                                	mc.player.connection.sendPacket(new CPacketHeldItemChange(item1));
                                    mc.playerController.attackEntity(mc.player, target);
                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                                    mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                                    timerHelper.reset();
                                }
                            }
                    	}
                        
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                    	if (!resolver.getBoolValue() || (resolver.getBoolValue() && ResolverHelper.canAttack())) {

                        if (BetterCrits.getBoolValue()) {
                    		mc.gameSettings.keyBindSneak.pressed = false;
                    	}
                    	}
                        hitCounter++;
                	}
                    break;
                case "1.8":
                    if (KillAuraHelper.canApsAttack() && mc.player.getDistanceToEntity(target) <= RangeHelper.getRange()) {
                        if (this.isBlocking && autoBlock.getBoolValue() && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                            if (timerHelper.hasReached(100)) {
                                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                                this.isBlocking = false;
                                timerHelper.reset();
                            }
                        }
                        mc.playerController.attackEntity(mc.player, EntityHelper.rayCast(target, RangeHelper.getRange()));
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        hitCounter++;
                    }
                    break;
            }
        }
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent eventAttackSilent) {
        if (mc.player.isBlocking() && mc.player.getHeldItemOffhand().getItem() instanceof ItemShield && shieldFix.getBoolValue() && timerHelper.hasReached(fixDelay.getNumberValue())) {
        	mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-0.8, -0.8, -0.8), EnumFacing.DOWN));
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
            timerHelper.reset();
        }
    }
    
    @EventTarget
    public void onSendPacket(final EventPacket event) {
    	String mode = rotationMode.getOptions();
    	if (mode.equalsIgnoreCase("Matrix New") || mode.equalsIgnoreCase("ReallyWorld")) {
    		if (event.getPacket() instanceof SPacketPlayerPosLook) {
                final SPacketPlayerPosLook packet1 = (SPacketPlayerPosLook)event.getPacket();
                mc.player.rotationYaw = packet1.getYaw();
                mc.player.rotationPitch = packet1.getPitch();
            }
    	}
    }

    @EventTarget
    public void onRotations(EventPreMotionUpdate event) {
        if (isToggled()) {
            if (target != null) {
                if (target.getHealth() > 0.0f) {
                	
                	if (autoShieldUnPress.getBoolValue()) {
                    	if (mc.player.getHeldItemMainhand().getItem() instanceof ItemShield && mc.player.isBlocking()) {
                    		if (target.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                                if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                                    mc.gameSettings.keyBindUseItem.pressed = false;
                                }
                            } else {
                                mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
                            }
                    	}
                    }
                	
                	
                	
                	if (AutoGapple.gDebug) 
                    	return;
                    String mode = rotationMode.getOptions();
                    float[] UltraRot = RotationHelper.rotats(target);
                    float[] Rots = RotationHelper.getRatations(target);
                    float[] rr = RotationHelper.getMatrixRotations(target, true, 1, 1);
                    float[] rotVisual2 = RotationHelper.getMatrixRotations(target, false, 2, 2);
                    float[] rotVisual = RotationHelper.getMatrixRotations(target, false, 2, 2);
                    float[] rotations2 = RotationHelper.getSunriseRotations(target, true, 360, 360, 2, 2);
                    float[] rotations23 = RotationHelper.getAACRotation(target, false, 360, 360, 3, 3);
                    final float[] rotsgg = RotationHelper.getRotations(target, false);

                    if (mode.equalsIgnoreCase("Matrix New")) {
                    	pitch2 = GCDFix.getFixedRotation(net.minecraft.util.math.MathHelper.lerp(pitch2, rr[1], 0.169f));
                    	event.setYaw(rr[0]);
                        event.setPitch(pitch2);
                        yaw = rr[0];
                        pitch = pitch2;
                        mc.player.renderYawOffset = rr[0];
                        mc.player.rotationYawHead = rr[0];
                        mc.player.rotationPitchHead = pitch2;
        				
                    }
                    if (mode.equalsIgnoreCase("Matrix")) {
                        float[] yaw3;
                        
                    	yaw3 = RotationHelper.rotats(target);
                        pitch2 = GCDFix.getFixedRotation(MathHelper.Rotate(pitch2, Rots[1], 0.1f, 2.2f));
                        event.setYaw(yaw3[0]);
                        event.setPitch(pitch2);
                        yaw = yaw3[0];
                        pitch = pitch2;
                        mc.player.renderYawOffset = yaw3[0];
                        mc.player.rotationYawHead = yaw3[0];
                        mc.player.rotationPitchHead = pitch2;
                    } else if (mode.equalsIgnoreCase("Snap")) {
                    	/*
                    	float[] rotations21 = RotationHelper.getRatations(target);
                        event.setYaw(rotations21[0]);
                        pitch2 = GCDFix.getFixedRotation(net.minecraft.util.math.MathHelper.lerp(pitch2, rotations21[1], 0.069f));
                        yaw = rotations21[0];
                        pitch = pitch2;
                        mc.player.renderYawOffset = rotations21[0];
                        mc.player.rotationYawHead = rotations21[0];
                        mc.player.rotationPitchHead = pitch2;
                        event.setPitch(pitch2);*/
                    } else if (mode.equalsIgnoreCase("Twister")) {
                    	 float[] rotations21 = RotationHelper.getRatations(target);
                         event.setYaw(rotations2[0]);
                         pitch2 = GCDFix.getFixedRotation(net.minecraft.util.math.MathHelper.lerp(pitch2, rotations2[1], 0.169f));
                         yaw = rotations2[0];
                         pitch = pitch2;
                         mc.player.renderYawOffset = rotations2[0] + 370 * mc.player.getCooledAttackStrength(0.01f);
                         mc.player.rotationYawHead = rotations2[0] + 370 * mc.player.getCooledAttackStrength(0.01f);
                         mc.player.rotationPitchHead = pitch2;
                         event.setPitch(pitch2);
                    } else if (mode.equalsIgnoreCase("Bebra")) {
                   	 float[] rotations21 = RotationHelper.getRatations(target);
                     event.setYaw(rotations21[0]);
                     pitch2 = GCDFix.getFixedRotation(net.minecraft.util.math.MathHelper.lerp(pitch2, rotations21[1], 0.069f));
                     yaw = rotations21[0];
                     pitch = pitch2;
                     mc.player.renderYawOffset = rotations21[0] + 370 * mc.player.getCooledAttackStrength(0.1f);
                     mc.player.rotationYawHead = rotations21[0] + 370 * mc.player.getCooledAttackStrength(0.1f);
                     mc.player.rotationPitchHead = pitch2;
                     event.setPitch(pitch2);
                } else if (mode.equalsIgnoreCase("Rage")) {
                   	 float[] rotations21 = RotationHelper.getRatations(target);
                   	 if (mc.player.getCooledAttackStrength(1) > 0.5f) {
                   		event.setYaw(rotations21[0]);
                        pitch2 = GCDFix.getFixedRotation(net.minecraft.util.math.MathHelper.lerp(pitch2, rotations21[1], 0.069f));
                        yaw = rotations21[0];
                        pitch = pitch2;
                        mc.player.renderYawOffset = rotations21[0];
                        mc.player.rotationYawHead = rotations21[0];
                        mc.player.rotationPitchHead = pitch2;
                        event.setPitch(pitch2);
                   	 }
                } else if (mode.equalsIgnoreCase("Client")) {
                        mc.player.rotationYaw = rotVisual[0];
                        yaw = rotVisual[0];
                        pitch = rotVisual[1];
                        mc.player.rotationPitch = rotVisual[1];
                    } else if (mode.equalsIgnoreCase("AAC")) {
                        if (mc.player.getCooledAttackStrength(1) == 1) {
                            event.setYaw(rotations23[0]);
                            event.setPitch(rotations23[1]);
                            yaw = rotations23[0];
                            pitch = rotations23[1];
                            mc.player.renderYawOffset = rotations23[0];
                            mc.player.rotationYawHead = rotations23[0];
                            mc.player.rotationPitchHead = rotations23[1];
                        }
                    } else if (mode.equalsIgnoreCase("MagicGrief")) {
                    	
                    	event.setYaw(rotations2[0]);
                        event.setPitch(rotations2[1]);
                        yaw = rotations2[0];
                        pitch = rotations2[1];
                        mc.player.renderYawOffset = rotations2[0];
                        mc.player.rotationYawHead = rotations2[0];
                        mc.player.rotationPitchHead = rotations2[1];
                    } else if (mode.equalsIgnoreCase("ReallyWorld")) {
                    	 
                    	event.setYaw(rotations2[0]);
                        event.setPitch(rotations2[1]);
                        yaw = rotations2[0];
                        pitch = rotations2[1];
                        mc.player.renderYawOffset = rotations2[0];
                        mc.player.rotationYawHead = rotations2[0];
                        mc.player.rotationPitchHead = rotations2[1];
                       	 
                    } else if (mode.equalsIgnoreCase("Legit")) {
                    	if (!RotationHelper.isLookingAtEntity(yaw, pitch, 0.14F, 0.14F, 0.14F, target, RangeHelper.getRange())) {
                    		mc.player.rotationYaw = rotVisual2[0];
                            yaw = rotVisual2[0];
                            pitch = rotVisual2[1];
                            mc.player.rotationPitch = rotVisual2[1];
                    	}
                    } else if (mode.equalsIgnoreCase("SunRise")) {
                        float[] rotations21 = RotationHelper.getRatations(target);
                        event.setYaw(rotations21[0]);
                        pitch2 = GCDFix.getFixedRotation(net.minecraft.util.math.MathHelper.lerp(pitch2, rotations21[1], 0.069f));
                        yaw = rotations21[0];
                        pitch = pitch2;
                        mc.player.renderYawOffset = rotations21[0];
                        mc.player.rotationYawHead = rotations21[0];
                        mc.player.rotationPitchHead = pitch2;
                        event.setPitch(pitch2);
                } else if (mode.equalsIgnoreCase("StormHVH")) {
                    float[] rotations21 = RotationHelper.getRatations(target);
                    event.setYaw(rotations21[0]);
                    pitch2 = GCDFix.getFixedRotation(net.minecraft.util.math.MathHelper.lerp(pitch2, rotations21[1], 0.069f));
                    yaw = rotations21[0];
                    pitch = pitch2;
                    mc.player.renderYawOffset = rotations21[0];
                    mc.player.rotationYawHead = rotations21[0];
                    mc.player.rotationPitchHead = pitch2;
                    event.setPitch(pitch2);
                    yaw2 = GCDFix.getFixedRotation(MathHelper.Rotate(yaw2, Rots[0], 0.1f, 35));
                    pitch2 = GCDFix.getFixedRotation(MathHelper.Rotate(pitch2, Rots[1], 0.1f, 2.2f));
                    event.setYaw(yaw2);
                    event.setPitch(pitch2);
                    yaw = yaw2;
                    pitch = pitch2;
                    mc.player.renderYawOffset = yaw2;
                    mc.player.rotationYawHead = yaw2;
                    mc.player.rotationPitchHead = pitch2;
            		mc.player.rotationYaw = rotVisual[0];
                    this.yaw = rotVisual[0];
                    this.pitch = rotVisual[1];
                    mc.player.rotationPitch = rotVisual[1];
                    mc.player.rotationYaw = rotVisual2[0];
                    this.yaw = rotVisual2[0];
                    this.pitch = rotVisual2[1];
                    mc.player.rotationPitch = rotVisual2[1];
            }
            } else {
                yaw2 = mc.player.rotationYaw;
                pitch2 = mc.player.rotationPitch;
            }
        } else {
            yaw2 = mc.player.rotationYaw;
            pitch2 = mc.player.rotationPitch;
        }
        }

    }
    
    
    

    public void destroyShield(EntityLivingBase entityLivingBase) {
        for (int a = 1; a < 9; a++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(a);
            if (itemStack.getItem() instanceof ItemAxe) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(a));
            }
        }
        if (timerHelper.hasReached(breakerDelay.getNumberValue())) {
            mc.playerController.attackEntity(mc.player, entityLivingBase);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            timerHelper.reset();
        }
        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
    }
    @EventTarget
    public void onShieldBreaker(final EventPreMotionUpdate eventPreMotionUpdate) {
    	String mode = breakerMode.getOptions();
    	boolean aboba = true;
    	if (target == null) 
    		return;
    	if (mode.equalsIgnoreCase("Matrix") && isToggled()) {
    		if (InventoryHelper.doesHotbarHaveAxe() && shieldBreak.getBoolValue() && (target.getHeldItemOffhand().getItem() instanceof ItemShield || target.getHeldItemMainhand().getItem() instanceof ItemShield)) {
                if (target.isHandActive()) {
                    if (target.canEntityBeSeen(mc.player) && mc.player.canEntityBeSeen(target) && RotationHelper.isLookingAtEntity(yaw, pitch, 0.2f, 0.2f, 0.2f, target, RangeHelper.getRange())) {
                    	if (this.timerHelper.hasReached(breakerDelay.getNumberValue())) {
                            if (mc.player.inventory.currentItem != InventoryHelper.getAxe()) {
                            		int slot = InventoryHelper.getAxe();
                	                mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                	                mc.playerController.attackEntity(mc.player, target);
                	                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                	                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                	                mc.player.resetCooldown();
                	                timerHelper.reset();
                            }
                            this.timerHelper.reset();
                        }
                        if (mc.player.inventory.currentItem == InventoryHelper.getAxe()) {
                        	mc.playerController.attackEntity(mc.player, target);
                        	 mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        	                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                            this.changeSlotCounter = -1;
                        }
                        else {
                            this.changeSlotCounter = 0;
                    }
                }
                else if (mc.player.inventory.currentItem != InventoryHelper.getSwordAtHotbar() && this.changeSlotCounter == -1 && InventoryHelper.getSwordAtHotbar() != -1 && (!target.isBlocking() || !target.isHandActive() || !target.isActiveItemStackBlocking())) {
                    mc.player.resetCooldown();
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem = InventoryHelper.getSwordAtHotbar()));
                    this.changeSlotCounter = 0;
                }
                }
    		}
    	}
    	if (mode.equalsIgnoreCase("AAC")) {
    		if (target == null) {
                return;
            }
            if (InventoryHelper.doesHotbarHaveAxe() && this.shieldBreak.getBoolValue() && (target.getHeldItemOffhand().getItem() instanceof ItemShield || target.getHeldItemMainhand().getItem() instanceof ItemShield)) {
                if (target.isBlocking() && target.isHandActive() && target.isActiveItemStackBlocking(2) && mc.player.getDistanceToEntity(target) < RangeHelper.getRange() && RotationHelper.isLookingAtEntity(this.yaw, this.pitch, 0.06f, 0.06f, 0.06f, target, RangeHelper.getRange())) {
                    if (RotationHelper.isAimAtMe(target, 65.0f)) {
                        if (mc.player.inventory.currentItem != InventoryHelper.getAxe()) {
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem = InventoryHelper.getAxe()));
                        }
                        if (mc.player.inventory.currentItem == InventoryHelper.getAxe()) {
                            if (timerHelper.hasReached(breakerDelay.getNumberValue() * 10)) {
                                this.isBreaked = true;
                                mc.playerController.attackEntity(mc.player, target);
                                mc.player.swingArm(EnumHand.MAIN_HAND);
                                mc.player.resetCooldown();
                                timerHelper.reset();
                            }
                            this.changeSlotCounter = -1;
                        }
                        else {
                            this.changeSlotCounter = 0;
                        }
                    }
                }
                else if (mc.player.inventory.currentItem != InventoryHelper.getSwordAtHotbar() && this.changeSlotCounter == -1 && InventoryHelper.getSwordAtHotbar() != -1 && (!target.isBlocking() || !target.isHandActive() || !target.isActiveItemStackBlocking(2))) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem = InventoryHelper.getSwordAtHotbar()));
                    this.changeSlotCounter = 0;
                    NotificationPublisher.queue(TextFormatting.GREEN + "Shield-Breaker", TextFormatting.RESET + "Successfully destroyed " + target.getName() + " shield", NotificationType.SUCCESS);
                    this.isBreaked = false;
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
    @EventTarget
    public void onStrafe(final EventStrafe eventStrafe) {
    	if (target != null) {
    		final String mode = strafeMode.getOptions();
            final String rotStrafeMode = rotationStrafeMode.getOptions();
            float strafe = eventStrafe.getStrafe();
            float forward = eventStrafe.getForward();
            final float friction = eventStrafe.getFriction();
            if (target.getHealth() > 0.0f && mode.equalsIgnoreCase("Always-F")) {
                if (rotStrafeMode.equalsIgnoreCase("Silent")) {
                    eventStrafe.setCancelled(true);
                    MovementHelper.calculateSilentMove(eventStrafe, RotationHelper.Rotation.packetYaw);
                }
                else if (rotStrafeMode.equalsIgnoreCase("Default")) {
                    eventStrafe.setCancelled(true);
                    float f = strafe * strafe + forward * forward;
                    if (f >= 1.0E-4f) {
                        f = (float)(friction / Math.max(1.0, Math.sqrt(f)));
                        strafe *= f;
                        forward *= f;
                        final float yawSin = MathHelper.sin(RotationHelper.Rotation.packetYaw * 3.1415927f / 180.0f);
                        final float yawCos = MathHelper.cos(RotationHelper.Rotation.packetYaw * 3.1415927f / 180.0f);
                        final EntityPlayerSP player = mc.player;
                        player.motionX += strafe * yawCos - forward * yawSin;
                        final EntityPlayerSP player2 = mc.player;
                        player2.motionZ += forward * yawCos + strafe * yawSin;
                    }
                }
            }
    	}
    }

    @EventTarget
    public void onNickRemove(EventNameTags event) {
        if (targetHudMode.currentMode.equalsIgnoreCase("Astolfo")) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void e(Event2D e) {
    	String mode = targetHudMode.getOptions();
    	if (mode.equalsIgnoreCase("Rockstar") || mode.equalsIgnoreCase("Rockstar New")) {
    		if (this.progress2 < 1.0f) {
              		 this.progress2 = (System.currentTimeMillis() - this.lastMS) / 250.0f;
    		} else {
    			this.progress2 = 1.0f;
    		}
    		
    		if (progress2 >= 0.7f) {
    			if (this.progress < 1.0f) {
             		 this.progress = (System.currentTimeMillis() - this.lastMS) / 550.0f;
    			} else {
    				this.progress = 1.0f;
    			}
    		}
    	} else {
    		if (this.progress2 >= 1.0f) {
                this.progress2 = 1.0f;
            }
            else {
                this.progress2 = (System.currentTimeMillis() - this.lastMS) / 550.0f;
            }
    		
    		if (this.progress >= 1.0f) {
                this.progress = 1.0f;
            }
            else {
           	 if (progress2 >= 1.0f) {
           		 this.progress = (System.currentTimeMillis() - this.lastMS) / 1050.0f;
           	 }
            } 
    	}
    	
    	boolean a = progress2 >= 1.0f;
    	
    	
    	 
        if (isToggled()) {
            ScaledResolution sr = new ScaledResolution(mc);
            if (target != null) {
                if (mode.equalsIgnoreCase("Beach")) {
                    if (targetHud.getBoolValue() && target instanceof EntityPlayer) {
                    	final float x = TargetHudComponent.x;
                        final float y = TargetHudComponent.y;
                        int color = 15;
                        double hpWidth = (target.getHealth() / target.getMaxHealth() * 78);
                        this.healthBarWidth = AnimationHelper.animate(hpWidth, this.healthBarWidth, 5 * Feature.deltaTime());
                        DrawHelper.drawNewRect(x + 122, y - 14, x + 250, y + 25, new Color(color, color, color, 0).getRGB());
                        DrawHelper.drawGlowRoundedRect(x + 122, y - 14, x + 250, y + 25, new Color(color, color, color, 150).getRGB(), 8, 10);
                        Util.drawHead1(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), (int) x + 129, (int) (y - 4f));
                        Gui.drawRect(x + 160, y + 13.0f, x + 160.0f + healthBarWidth, y + 18.0f, ClientHelper.getClientColor().getRGB());
                        mc.neverlose500_13.drawStringWithShadow("Hp: " + (int) target.getHealth() / 2.0f + " | Ground: " + (this.target.onGround ? "true" : "false"), x + 121.0f + 46.0f - mc.neverlose500_16.getStringWidth(String.valueOf((int) target.getHealth() / 2.0f)) / 2.0f, y + 6f, -1);
                        mc.neverlose500_18.drawStringWithShadow(target.getName(), x + 158, y - 5.0f, -1);
                        mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 228, (int) y - 35);
                        mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)  x + 228, (int) y - 35);

                    } else {
                        this.healthBarWidth = 92.0;
                        this.hudHeight = 0.0;
                        target = null;
                    }
                }
            }
            if (target != null) {
                if (mode.equalsIgnoreCase("Akrien")) {
                    if (targetHud.getBoolValue() && target instanceof EntityPlayer) {
                    	final float x = TargetHudComponent.x;
                        final float y = TargetHudComponent.y;
                        int color = 15;
                        double hpWidth = (target.getHealth() / target.getMaxHealth() * 78);
                        this.healthBarWidth = AnimationHelper.animate(hpWidth, this.healthBarWidth, 5 * Feature.deltaTime());
                        DrawHelper.drawGlowRoundedRect(x + 122, y - 14, x + 250, y + 25, new Color(color, color, color, 150).getRGB(), 8, 10);
                        Util.drawHead1(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), (int) x + 129, (int) (y - 4f));
                        Gui.drawRect(x + 160, y + 13.0f, x + 160.0f + healthBarWidth, y + 18.0f, ClientHelper.getClientColor().getRGB());
                        mc.neverlose500_13.drawStringWithShadow("Hp: " + (int) target.getHealth() / 2.0f + " | Ground: " + (this.target.onGround ? "true" : "false"), x + 121.0f + 46.0f - mc.neverlose500_16.getStringWidth(String.valueOf((int) target.getHealth() / 2.0f)) / 2.0f, y + 6f, -1);
                        mc.neverlose500_18.drawStringWithShadow(target.getName(), x + 158, y - 5.0f, -1);
                        mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 228, (int) y - 35);
                        mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)  x + 228, (int) y - 35);
                    } else {
                        this.healthBarWidth = 92.0;
                        this.hudHeight = 0.0;
                        target = null;
                    }
                }
            }
            if (target != null) {
                if (mode.equalsIgnoreCase("Rockstar Old")) {
                    if (targetHud.getBoolValue() && target instanceof EntityPlayer) {
                    	final float x = TargetHudComponent.x;
                        final float y = TargetHudComponent.y;
                        int color = 15;
                        double hpWidth = (target.getHealth() / target.getMaxHealth() * 78);
                        this.healthBarWidth = AnimationHelper.animate(hpWidth, this.healthBarWidth, 3 * Feature.deltaTime());
                        DrawHelper.drawGradientRect(x + 120, y - 14, x + 122, y - 14 + 39 * progress2, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
                        if (progress2 >= 1.0f) {
                        	DrawHelper.drawNewRect(x + 122, y - 14, x + 122 + 128 * progress, y + 25, new Color(color, color, color, 0).getRGB());
                            DrawHelper.drawNewRect(x + 122, y - 13.5f, x + 122 + 128 * progress, y + 24.5f, new Color(0, 0, 0, 150).getRGB());
                            DrawHelper.drawGradientRect(x + 120 + 130 * progress, y - 14, x + 122 + 130 * progress, y + 25, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
                        }
                        if (progress == 1.0) {
                        	if (!target.getName().isEmpty()) {
                        		Util.drawHead2(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), (int) x + 216, (int) (y - 9f));
                        	}
                            mc.mntsb.drawStringWithShadow("Hp: " + (int) target.getHealth() + " | Distance: " + (int) mc.player.getDistanceToEntity(target), x + 101.0f + 35.0f - mc.neverlose500_16.getStringWidth(String.valueOf((int) target.getHealth() / 2.0f)) / 2.0f, y + 4f, -1);
                            mc.mntsb.drawStringWithShadow(target.getName(), x + 128, y - 7.0f, -1);
                            mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 228, (int) y - 35);
                            mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)  x + 222, (int) y - 35);
                        }
                        if (progress >= 0.5 && progress2 >= 1.0f) {
                        	 DrawHelper.drawGradientRect1(x + 160 - 32, y + 13.0f, x + 160.0f + (healthBarWidth - 32) * progress, y + 20.0f, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
                        }
                    } else {
                        this.healthBarWidth = 92.0;
                        this.hudHeight = 0.0;
                        target = null;
                    }
                }
            } else {
            	this.progress = 0.0f;
            	this.progress2 = 0.0f;
            }
            
            if (target == null || mc.world == null) {
            	GlStateManager.pushMatrix();
                GlStateManager.enable(GL11.GL_SCISSOR_TEST);
                RenderUtils.scissorRect(0,0,sr.getScaledHeight(),sr.getScaledHeight());
                GlStateManager.disable(GL11.GL_SCISSOR_TEST);
                GlStateManager.popMatrix();
                
            	this.progress = 0.0f;
            	this.progress2 = 0.0f;
            	return;
            }
                if (mode.equalsIgnoreCase("Rockstar")) {
                    if (targetHud.getBoolValue() && target instanceof EntityPlayer) {
                    	final float x = TargetHudComponent.x;
                        final float y = TargetHudComponent.y;
                        int color = 15;
                        double hpWidth = (target.getHealth() / target.getMaxHealth() * 78);
                        this.healthBarWidth = AnimationHelper.animate(hpWidth, this.healthBarWidth, 3 * Feature.deltaTime());
                        RoundedUtil.drawGradientHorizontal(x + 182 - 60 * progress2, y - 13.5f * progress2, (mc.mntsb_20.getStringWidth(target.getName()) > 60 ? mc.mntsb_20.getStringWidth(target.getName()) + 60 : 60 + 70) * progress2, 9.5f + 30 * progress2, 7, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
                        
                        GlStateManager.pushMatrix();
                        GlStateManager.enable(GL11.GL_SCISSOR_TEST);
                        RenderUtils.scissorRect(x + 182 - 60, y - 13.5f, x + (mc.mntsb_20.getStringWidth(target.getName()) > 60 ? mc.mntsb_20.getStringWidth(target.getName()) + 115 : 115 + 70) - 60 + 128, y - 13.5f + 9.5f + 30);
                        if (!target.getName().isEmpty()) {
                        	Util.drawHead2(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), (int) ((int) x + 216 + (mc.mntsb_20.getStringWidth(target.getName()) > 60 ? mc.mntsb_20.getStringWidth(target.getName()) + 30 : 30 + 70) - 100 * progress), (int) (y - 9f));
                        }
                        mc.mntsb_13.drawStringWithShadow("Distance: " + String.format("%.1f", Float.valueOf(mc.player.getDistanceToEntity(target))), x + 101.0f - 70 + 105.0f * progress - mc.neverlose500_16.getStringWidth(String.valueOf((int) target.getHealth() / 2.0f)) / 2.0f, y + 5.5f, -1);
                        mc.mntsb_20.drawStringWithShadow(target.getName(), x + 128 - 105 + 105 * progress, y - 7.0f, -1);
                        mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 228, (int) y - 35);
                        mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)  x + 222, (int) y - 35);
                        
                        //DrawHelper.drawGradientRect1(x - 1 + 160 - 32 - 105 + 105 * progress, y + 13.0f - 1, x + 1 - 105 + 105 * progress + 160.0f + (78 - 32), y + 1 + 20.0f, ClientHelper.getClientColor().darker().getRGB(), ClientHelper.getClientColor().darker().getRGB());
                        
                        if (target.hurtTime > 0) {
                        DrawHelper.drawTriangle((float) (x - 105 + 105 * progress + 160.0f + (healthBarWidth - 32)) - 4.5f, y + 8, 4.0F, 4.0F, new Color(50, 50, 50, 255 * target.hurtTime / 10).getRGB(), new Color(50, 50, 50, 255 * target.hurtTime / 10).getRGB());
                        RenderUtils.drawFilledCircle((float) (x - 105 + 105 * progress + 160 + (healthBarWidth - 32.5f)), (int) (y + 6), 4, new Color(50, 50, 50, 255 * target.hurtTime / 10));
                        DrawHelper.drawGradientRect1(x, y, x, y, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
                        mc.mntsb_10.drawCenteredString("" + (int) target.getHealth(), (float) (x - 105 + 105 * progress + 160 + (healthBarWidth - 32.5f)), (int) (y + 6), -1);
                        }
                        
                        DrawHelper.drawGradientRect1(x + 160 - 32 - 105 + 105 * progress, y + 13.0f, x - 105 + 105 * progress + 160.0f + (healthBarWidth - 32), y + 20.0f, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
                        
                        GlStateManager.disable(GL11.GL_SCISSOR_TEST);
                        GlStateManager.popMatrix();
                        
                    } else {
                        this.healthBarWidth = 92.0;
                        this.hudHeight = 0.0;
                        target = null;
                    }
                }
            
            
            
            if (target == null || mc.world == null) {
            	GlStateManager.pushMatrix();
                GlStateManager.enable(GL11.GL_SCISSOR_TEST);
                RenderUtils.scissorRect(0,0,sr.getScaledHeight(),sr.getScaledHeight());
                GlStateManager.disable(GL11.GL_SCISSOR_TEST);
                GlStateManager.popMatrix();
                
            	this.progress = 0.0f;
            	this.progress2 = 0.0f;
            	return;
            }
                if (mode.equalsIgnoreCase("Rockstar New")) {
                    if (targetHud.getBoolValue() && target instanceof EntityPlayer) {
                    	final float x = TargetHudComponent.x;
                        final float y = TargetHudComponent.y;
                        int color = 15;
                        double hpWidth = (target.getHealth() / target.getMaxHealth() * 78);
                        this.healthBarWidth = AnimationHelper.animate(hpWidth, this.healthBarWidth, 3 * Feature.deltaTime());
                        RoundedUtil.drawHorizontalGradientOutlinedRoundedRectWithGlow(x + 182 - 60 * progress2, y - 13.5f * progress2, (mc.mntsb_20.getStringWidth(target.getName()) > 60 ? mc.mntsb_20.getStringWidth(target.getName()) + 60 : 60 + 70) * progress2, 9.5f + 30 * progress2, 7, 1, 15, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
                        
                        GlStateManager.pushMatrix();
                        GlStateManager.enable(GL11.GL_SCISSOR_TEST);
                        RenderUtils.scissorRect(x + 182 - 60, y - 13.5f, x + (mc.mntsb_20.getStringWidth(target.getName()) > 60 ? mc.mntsb_20.getStringWidth(target.getName()) + 115 : 115 + 70) - 60 + 128, y - 13.5f + 9.5f + 30);
                        if (!target.getName().isEmpty()) {
                        	Util.drawHead2(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), (int) ((int) x + 216 + (mc.mntsb_20.getStringWidth(target.getName()) > 60 ? mc.mntsb_20.getStringWidth(target.getName()) + 30 : 30 + 70) - 100 * progress), (int) (y - 9f));
                        }
                        mc.mntsb_13.drawStringWithShadow("Distance: " + String.format("%.1f", Float.valueOf(mc.player.getDistanceToEntity(target))), x + 101.0f - 70 + 105.0f * progress - mc.neverlose500_16.getStringWidth(String.valueOf((int) target.getHealth() / 2.0f)) / 2.0f, y + 5.5f, -1);
                        mc.mntsb_20.drawStringWithShadow(target.getName(), x + 128 - 105 + 105 * progress, y - 7.0f, -1);
                        mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 228, (int) y - 35);
                        mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)  x + 222, (int) y - 35);
                        
                        //DrawHelper.drawGradientRect1(x - 1 + 160 - 32 - 105 + 105 * progress, y + 13.0f - 1, x + 1 - 105 + 105 * progress + 160.0f + (78 - 32), y + 1 + 20.0f, ClientHelper.getClientColor().darker().getRGB(), ClientHelper.getClientColor().darker().getRGB());
                        
                        if (target.hurtTime > 0) {
                        DrawHelper.drawTriangle((float) (x - 105 + 105 * progress + 160.0f + (healthBarWidth - 32)) - 4.5f, y + 8, 4.0F, 4.0F, new Color(50, 50, 50, 255 * target.hurtTime / 10).getRGB(), new Color(50, 50, 50, 255 * target.hurtTime / 10).getRGB());
                        RenderUtils.drawFilledCircle((float) (x - 105 + 105 * progress + 160 + (healthBarWidth - 32.5f)), (int) (y + 6), 4, new Color(50, 50, 50, 255 * target.hurtTime / 10));
                        DrawHelper.drawGradientRect1(x, y, x, y, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
                        mc.mntsb_10.drawCenteredString("" + (int) target.getHealth(), (float) (x - 105 + 105 * progress + 160 + (healthBarWidth - 32.5f)), (int) (y + 6), -1);
                        }
                        
                        DrawHelper.drawGradientRect1(x + 160 - 32 - 105 + 105 * progress, y + 13.0f, x - 105 + 105 * progress + 160.0f + (healthBarWidth - 32), y + 20.0f, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
                        
                        GlStateManager.disable(GL11.GL_SCISSOR_TEST);
                        GlStateManager.popMatrix();
                        
                        
                        
                        
                        
                    } else {
                        this.healthBarWidth = 92.0;
                        this.hudHeight = 0.0;
                        target = null;
                    }
                }
            
            
            
            if (target != null) {
                if (mode.equalsIgnoreCase("Nursultan")) {
                    if (targetHud.getBoolValue() && target instanceof EntityPlayer) {
                    	final float x = TargetHudComponent.x;
                        final float y = TargetHudComponent.y;
                        int color = 15;
                        double hpWidth = (target.getHealth() / target.getMaxHealth() * 78);
                        this.healthBarWidth = AnimationHelper.animate(hpWidth, this.healthBarWidth, 5 * Feature.deltaTime());
                        DrawHelper.drawNewRect(x + 122, y - 14, x + 250, y + 25, new Color(color, color, color, 0).getRGB());
                        DrawHelper.drawGlowRoundedRect(x + 122, y - 14, x + 260, y + 40, new Color(color, color, color, 150).getRGB(), 8, 10);
                        Util.drawHead2(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), (int) x + 128, (int) (y - 8f));
                        DrawHelper.drawGradientRect1(x + 160 - 32, y + 13.0f + 12, (float) (x + 160.0f - 32 + healthBarWidth * 1.43f), y + 18.0f + 14.5f, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
                        mc.neverlose500_14.drawStringWithShadow("" + (int) target.getHealth() / 2.0f, x + 160.0f - 32 + healthBarWidth * 1.43f + 1, y + 27.5f, -1);
                        mc.neverlose500_18.drawStringWithShadow(target.getName(), x + 160, y + 0.0f, -1);
                        mc.neverlose500_18.drawStringWithShadow("Distance:" + (int) mc.player.getDistanceToEntity(target), x + 160, y + 9.0f, -1);
                        mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 228, (int) y - 35);
                        mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)  x + 228, (int) y - 35);
                    } else {
                        this.healthBarWidth = 92.0;
                        this.hudHeight = 0.0;
                        target = null;
                    }
                }
            }
            if (mode.equalsIgnoreCase("Astolfo")) {
                if (isToggled()) {
                    if (target != null) {
                        if (target instanceof EntityPlayer && targetHud.getBoolValue()) {
                            float scaledWidth = sr.getScaledWidth();
                            float scaledHeight = sr.getScaledHeight();
                            final float x = TargetHudComponent.x;
                            final float y = TargetHudComponent.y;
                            double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
                            healthWid = net.minecraft.util.math.MathHelper.clamp(healthWid, 0.0D, 120.0D);
                            double check = target != null && target.getHealth() < (target instanceof EntityPlayer ? 18 : 10) && target.getHealth() > 1 ? 8 : 0;
                            this.healthBarWidth = MathHelper.lerp((float) healthWid, (float) this.healthBarWidth, 5 * Feature.deltaTime());
                            DrawHelper.drawGlowRoundedRect(x, y, x + 155, y + 62, new Color(20, 20, 20, 255).getRGB(), 6, 5);

                            if (!target.getName().isEmpty()) {
                                mc.fontRendererObj.drawStringWithShadow(Main.instance.featureDirector.getFeatureByClass(NameProtect.class).isToggled() ? "Protected" : target.getName(), x + 31, y + 5, -1);
                            }
                            GlStateManager.pushMatrix();
                            GlStateManager.translate(x, y, 1);
                            GL11.glScalef(2.5f, 2.5f, 2.5f);
                            GlStateManager.translate(-x - 3, -y - 2, 1);
                            mc.fontRendererObj.drawStringWithShadow(ru.rockstar.api.utils.math.MathHelper.round((target.getHealth() / 2.0f), 1) + " \u2764", x + 16, y + 10, new Color(ClientHelper.getClientColor().getRGB()).getRGB());
                            GlStateManager.popMatrix();
                            GlStateManager.color(1, 1, 1, 1);

                            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 7);
                            mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 1);


                            GuiInventory.drawEntityOnScreen((int) x + 16, (int) y + 55, 25, target.rotationYaw, -target.rotationPitch, target);
                            DrawHelper.drawRect2(x + 30, y + 48, 120, 8, new Color(ClientHelper.getClientColor().getRGB()).darker().darker().darker().getRGB());
                            DrawHelper.drawRect2(x + 30, y + 48, healthBarWidth + check, 8, new Color(ClientHelper.getClientColor().getRGB()).darker().darker().getRGB());
                            DrawHelper.drawRect2(x + 30, y + 48, healthWid, 8, new Color(ClientHelper.getClientColor().getRGB()).getRGB());


                        }

                    }
                }
            }
            if (mode.equalsIgnoreCase("Celestial Premium")) {
                if (target != null) {
                    if (targetHud.getBoolValue() && target instanceof EntityPlayer) {
                        ScaledResolution sr1 = new ScaledResolution(mc);
                        final float scaledWidth = sr1.getScaledWidth();
                        final float scaledHeight = sr1.getScaledHeight();
                        final float x = TargetHudComponent.x;
                        final float y = TargetHudComponent.y;
                        final float health = this.target.getHealth();
                        double hpPercentage = health / this.target.getMaxHealth();
                        hpPercentage = MathHelper.clamp(hpPercentage, 0.0, 1.0);
                        final double hpWidth = 110 * hpPercentage;
                        final String healthStr = String.valueOf((int) this.target.getHealth() / 2.0f);
                        this.healthBarWidth = AnimationHelper.animate(hpWidth, this.healthBarWidth, 5 * Feature.deltaTime());
                        this.hudHeight = AnimationHelper.animate(40.0, this.hudHeight, 5 * Feature.deltaTime());
                        DrawHelper.drawNewRect((float) (x + 125), y - 19, x + 275, y + 29, new Color(32, 31, 32, 150).getRGB());
                        DrawHelper.drawGlowRoundedRect((float) (x + 120), y - 26, x + 280, y + 35f, new Color(32, 31, 32, 150).getRGB(), 15, 10);
                        DrawHelper.drawGlowRoundedRect((float) (x + 127.5), (float) (y - 11.5), x + 273, y - 11, new Color(140, 140, 140).getRGB(), 6, 10);
                        DrawHelper.drawGlowRoundedRect(x + 162f, y + 18, (float) (x + 162f + this.healthBarWidth), y + 20f, new Color(ClientHelper.getClientColor().getRed() / 255.0f, ClientHelper.getClientColor().getGreen() / 255.0f, ClientHelper.getClientColor().getBlue() / 255.0f, 115.0f / 255.0f).getRGB(), 6, 25);
                        DrawHelper.drawGlowRoundedRect(x + 162f, y + 18, (float) (x + 162f + hpWidth), y + 20, ClientHelper.getClientColor().getRGB(), 6, 25);

                        mc.sfui16.drawStringWithShadow("Ground: " + (this.target.onGround ? "true;" : "false;"), x + 162f, y - 3f, -1);
                        mc.sfui16.drawStringWithShadow("HurtTime", x + 162.5f, y + 7f, -1);
                        mc.neverlose500_13.drawCenteredString(this.target.getName(), (float) (x + 275 / 1.38), (float) (y - 16.7), -1);
                        double hurttimePercentage = net.minecraft.util.math.MathHelper.clamp(target.hurtTime, 1.0, 0.3);
                        final double hurttimeWidth = 71.0 * hurttimePercentage;
                        this.hurttimeBarWidth = AnimationHelper.animate(hurttimeWidth, this.hurttimeBarWidth, 0.0529999852180481);
                        DrawHelper.drawGlowRoundedRect(x + 201f, y + 9f, x + 272, y + 11, new Color(40, 40, 40).getRGB(), 2, 10);
                        DrawHelper.drawGlowRoundedRect(x + 201f, y + 9f, (float) (x + 201 + this.hurttimeBarWidth), y + 11, ClientHelper.getClientColor().getRGB(), 2, 4);
                        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 255, (int) y - 5);
                        mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 259, (int) y - 10);

                        //Gui.drawRect(x + 44, y + 219 - 406, x + 166, y + 222.5 - 406, Main.getClientColor().getRGB());
                        DrawHelper.drawGlowRoundedRect((float) (x + 125.5), y - 20.5f, x + 275, y - 18, ClientHelper.getClientColor().getRGB(), 4, 6);
                        Util.drawHead3(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), (int) x + 127 + this.target.hurtTime / 2, (int) (y - 8f) + this.target.hurtTime / 2, 32 - this.target.hurtTime, 32 - this.target.hurtTime);
                    } else {
                        this.healthBarWidth = 92.0;
                        this.hudHeight = 0.0;
                    }
                }
            }
        }
    }
}