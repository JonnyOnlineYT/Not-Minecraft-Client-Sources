package ru.rockstar.client.features.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.event.event.MoveEvent;
import ru.rockstar.api.utils.combat.RotationHelper;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import java.awt.*;

public class TargetStrafe extends Feature {
    public NumberSetting tstrafeRange;
    public NumberSetting spd;
    public BooleanSetting targetlockCheck;
    public BooleanSetting damageBoost;
    public NumberSetting damageBoostValue;
    private double circleAnim;

    public BooleanSetting render;
    public NumberSetting points;
    public BooleanSetting autoJump;
    public BooleanSetting autoShift;


    public static int direction = -1;

    public TargetStrafe() {
        super("TargetStrafe", "Крутится вокруг энтити", 0, Category.MOVEMENT);
        tstrafeRange = new NumberSetting("Strafe Distance", 2.4F, 0.1F, 6.0F, 0.1F, () -> true);
        spd = new NumberSetting("Strafe Speed", 0.23F, 0.1F, 2, 0.01F, () -> true);
        damageBoost = new BooleanSetting("Hurt Boost", "Ускоряет вас после того как вам нанесли урон", false, () -> true);
        damageBoostValue = new NumberSetting("HurtBoost Value", 0.5f, 0.1f, 4.0f, 0.1f, () -> damageBoost.getBoolValue());
        targetlockCheck = new BooleanSetting("Look Check", "Меняет деректорию после того как на вас посмотрело энтити", false, () -> true);
        render = new BooleanSetting("Render", false, () -> true);
        points = new NumberSetting("Points", "Кол-во поинтов в круге", 3, 1, 30, 1, () -> render.getBoolValue());
        autoJump = new BooleanSetting("AutoJump", true, () -> true);
        autoShift = new BooleanSetting("AutoShift", false, () -> true);
        addSettings(tstrafeRange, spd, targetlockCheck, damageBoost, damageBoostValue, render, points, autoShift, autoJump);
    }

    public boolean onMotionUpdate(MoveEvent e) {
        EntityLivingBase entity = KillAura.target;
        float[] rotations = RotationHelper.getNewFaceRotating(entity);
            if ((double) mc.player.getDistanceToEntity(entity) <= tstrafeRange.getNumberValue()) {
                if (mc.player.hurtTime > 0 && damageBoost.getBoolValue()) {
                    MovementHelper.setMotion(e, spd.getNumberValue() + damageBoostValue.getNumberValue(), rotations[0], direction, 0.0);
                } else {
                    MovementHelper.setMotion(e, spd.getNumberValue(), rotations[0], direction, 0.0);
                }
            } else if (mc.player.hurtTime > 0 && damageBoost.getBoolValue()) {
                MovementHelper.setMotion(e, spd.getNumberValue() + damageBoostValue.getNumberValue(), rotations[0], direction, 1.0);
            } else {
                MovementHelper.setMotion(e, spd.getNumberValue(), rotations[0], direction, 1.0);
            }
            return true;
        }
    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        if (mc.player.isCollidedHorizontally)
            switchDirection();
        if (mc.gameSettings.keyBindLeft.isPressed())
            direction = 1;
        if (mc.gameSettings.keyBindRight.isPressed())
            direction = -1;
        if (KillAura.target != null) {
            if (KillAura.target.getHealth() > 0.0F && autoJump.getBoolValue() && Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled() && Main.instance.featureDirector.getFeatureByClass(TargetStrafe.class).isToggled() && mc.player.onGround)
                mc.player.jump();
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (KillAura.target == null)
            return;
        if (mc.player.getDistanceToEntity(KillAura.target) <= ru.rockstar.api.utils.combat.RangeHelper.getRange()) {
            if (autoShift.getBoolValue()) {
                mc.gameSettings.keyBindSneak.setPressed(this.isToggled() && KillAura.target != null && mc.player.fallDistance > KillAura.critFallDistance.getNumberValue() + 0.1);
            }
        }
    }

    @EventTarget
    public void onSwitchDir(EventUpdate event) {
    	if (KillAura.target != null) {
    		if (mc.currentScreen instanceof GuiGameOver) {
                toggle();
                NotificationPublisher.queue(getLabel(), "was Toggled Off", NotificationType.INFO);
                return;
            }
            if (mc.player.ticksExisted <= 1) {
                toggle();
                NotificationPublisher.queue(getLabel(), "was Toggled Off", NotificationType.INFO);
                return;
            }

            if (mc.player.getDistanceToEntity(KillAura.target) <= ru.rockstar.api.utils.combat.RangeHelper.getRange()) {
                if (KillAura.target == null) return;
                if (mc.player.isCollidedHorizontally) {
                    switchDirection();
                }
                if (targetlockCheck.getBoolValue() && KillAura.target != null && RotationHelper.isAimAtMe(KillAura.target, 25)) {
                    switchDirection();
                }

                if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                    direction = 1;
                }

                if (mc.gameSettings.keyBindRight.isKeyDown()) {
                    direction = -1;
                }
            }
    	}
    }

    private void switchDirection() {
        if (direction == 1) {
            direction = -1;
        } else {
            direction = 1;
        }
    }

    @EventTarget
    public void onRender3D(Event3D e) {
        if (isToggled()) {
            if (KillAura.target != null && Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {
                if (Minecraft.getMinecraft().player.getDistanceToEntity(KillAura.target) <= ru.rockstar.api.utils.combat.RangeHelper.getRange() && KillAura.target.getHealth() > 0.0f) {
                    if (render.getBoolValue()) {
                        circleAnim += 0.015F * Minecraft.frameTime / 10;
                        DrawHelper.drawCircle3D(KillAura.target, circleAnim + 0.001, e.getPartialTicks(), (int) points.getNumberValue(), 4, Color.black.getRGB());
                        DrawHelper.drawCircle3D(KillAura.target, circleAnim - 0.001, e.getPartialTicks(), (int) points.getNumberValue(), 4, Color.black.getRGB());
                        for (float i = 0; i < 360.5; i += 1) {
                            DrawHelper.drawCircle3D(KillAura.target, circleAnim, e.getPartialTicks(), (int) points.getNumberValue(), 2, ClientHelper.getClientColor(i / 16, i, 5).getRGB());
                        }
                        circleAnim = MathHelper.clamp(circleAnim, 0, 1);
                    } else {
                        circleAnim = 0;
                    }
                }
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent e) {
        if (Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled() && KillAura.target != null) {
            if (mc.player.isCollidedHorizontally)
                switchDirection();
            if (KillAura.target.getHealth() > 0.0F)
                onMotionUpdate(e);
        }
    }

}


