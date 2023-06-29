package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.motion.EventMove;
import org.moonware.client.event.events.impl.player.EventPostMotion;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.combat.*;
import org.moonware.client.feature.impl.movement.Flight;
import org.moonware.client.feature.impl.movement.Speed;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class NoSlowDown
        extends Feature {
    private boolean isNotJump;
    public static NumberSetting percentage;
    public static BooleanSetting soulSand;
    public static BooleanSetting slimeBlock;
    public static BooleanSetting autoJump;
    public static BooleanSetting jumpBoost;
    public static BooleanSetting bowing;
    public static BooleanSetting eating;
    public static BooleanSetting drinking;
    public static BooleanSetting blocking;
    public static ListSetting noSlowMode;
    private final TimerHelper sendTimer = new TimerHelper();
    public static int usingTicks;

    public NoSlowDown() {
        super("NoSlowDown", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0437\u0430\u043c\u0435\u0434\u043b\u0435\u043d\u0438\u0435 \u043f\u0440\u0438 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0438 \u0435\u0434\u044b \u0438 \u0434\u0440\u0443\u0433\u0438\u0445 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432", Type.Movement);
        percentage = new NumberSetting("Percentage", 100.0f, 0.0f, 100.0f, 1.0f, () -> true);
        noSlowMode = new ListSetting("NoSlow Mode", "Default", () -> true, "Default", "AAC5", "Sunrise New", "Sunrise Safe", "Matrix New", "Sunrise Old", "Sunrise Fast", "Old Matrix", "Matrix Jump", "Matrix");
        eating = new BooleanSetting("Eating", true, () -> true);
        bowing = new BooleanSetting("Bowing", true, () -> true);
        drinking = new BooleanSetting("Drinking", true, () -> true);
        blocking = new BooleanSetting("Blocking", true, () -> true);
        autoJump = new BooleanSetting("Auto Jump", false, () -> !noSlowMode.currentMode.equals("Sunrise"));
        jumpBoost = new BooleanSetting("Jump Boost", false, () -> !noSlowMode.currentMode.equals("Sunrise") && autoJump.getBoolValue());
        soulSand = new BooleanSetting("Soul Sand", false, () -> true);
        slimeBlock = new BooleanSetting("Slime", true, () -> true);
        addSettings(noSlowMode, eating, blocking, bowing, drinking, autoJump, jumpBoost, soulSand, slimeBlock, percentage);
    }

    public static boolean canNoSLow() {
        if (Minecraft.player.isEating() && !eating.getBoolValue()) {
            return false;
        }
        if (Minecraft.player.isBowing() && !bowing.getBoolValue()) {
            return false;
        }
        if (Minecraft.player.isDrinking() && !drinking.getBoolValue()) {
            return false;
        }
        if (Minecraft.player.isBlocking() && !blocking.getCurrentValue()) {
            return false;
        }
        if (MoonWare.featureManager.getFeatureByClass(Speed.class).getState() && Speed.speedMode.currentMode.equals("Sunrise YPort")) {
            return false;
        }
        if (MoonWare.featureManager.getFeatureByClass(Criticals.class).getState() && Criticals.critMode.currentMode.equals("Sunrise Air") && Aura.target != null && MoonWare.featureManager.getFeatureByClass(Aura.class).getState()) {
            return false;
        }
        return !noSlowMode.currentMode.equals("Sunrise Safe") || usingTicks >= 3;
    }

    @Override
    public void onDisable() {
        Minecraft.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    @EventTarget
    public void onMove(EventMove eventMove) {
        if (!canNoSLow()) {
            return;
        }
        if (noSlowMode.currentMode.equals("Sunrise Fast")) {
            if (MoonWare.featureManager.getFeatureByClass(Speed.class).getState() && Speed.speedMode.currentMode.equals("Sunrise Ground")) {
                return;
            }
            if (Minecraft.player.isUsingItem() && Minecraft.gameSettings.keyBindForward.isKeyDown()) {
                MovementHelper.setSpeed(MovementHelper.getSpeed());
                if (!MovementHelper.isMoving() || Minecraft.player.movementInput.jump) {
                    return;
                }
                if (Minecraft.player.onGround) {
                    float value = Minecraft.player.rotationYaw * ((float)Math.PI / 180);
                    Minecraft.player.motionX -= Math.sin(value) * (double)0.2075f;
                    Minecraft.player.motionZ += Math.cos(value) * (double)0.2075f;
                    eventMove.setX(Minecraft.player.motionX);
                    eventMove.setY(1.0E-9);
                    eventMove.setZ(Minecraft.player.motionZ);
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix(noSlowMode.getCurrentMode());
        usingTicks = Minecraft.player.isUsingItem() ? ++usingTicks : 0;
        boolean bl = isNotJump = !Minecraft.gameSettings.keyBindJump.isKeyDown();
        if (!getState() || !Minecraft.player.isUsingItem()) {
            return;
        }
        if (!canNoSLow()) {
            return;
        }
        if (autoJump.getCurrentValue() && Minecraft.player.isUsingItem()) {
            if (jumpBoost.getCurrentValue()) {
                Minecraft.player.jumpMovementFactor *= 1.04f;
                Minecraft.player.motionY = Minecraft.player.motionY > 0.0 && !Minecraft.player.onGround ? (Minecraft.player.motionY -= 0.00994) : (Minecraft.player.motionY -= 0.00995);
            }
            if (Minecraft.player.onGround) {
                Minecraft.gameSettings.keyBindJump.pressed = false;
                Minecraft.player.jump();
                if (jumpBoost.getCurrentValue()) {
                    Minecraft.player.motionX *= 1.005;
                    Minecraft.player.motionZ *= 1.005;
                }
            }
        }
        if (noSlowMode.currentMode.equals("Matrix Jump")) {
            if (Minecraft.player.isUsingItem() && MovementHelper.isMoving() && (double) Minecraft.player.fallDistance > 0.7) {
                Minecraft.player.motionX *= 0.97f;
                Minecraft.player.motionZ *= 0.97f;
            }
        } else if (noSlowMode.currentMode.equals("Matrix New")) {
            if (Minecraft.player.isUsingItem()) {
                if (Minecraft.player.onGround && !Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                    if (Minecraft.player.ticksExisted % 2 == 0) {
                        Minecraft.player.motionX *= 0.48;
                        Minecraft.player.motionZ *= 0.48;
                    }
                } else if ((double) Minecraft.player.fallDistance > 0.7) {
                    Minecraft.player.motionX *= 0.97f;
                    Minecraft.player.motionZ *= 0.97f;
                }
            }
        } else if (noSlowMode.currentMode.equals("Sunrise New")) {
            if (Minecraft.player.isUsingItem()) {
                if (Minecraft.player.onGround && !Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                    if (Minecraft.player.ticksExisted % 2 == 0) {
                        Minecraft.player.motionX *= 0.47;
                        Minecraft.player.motionZ *= 0.47;
                    }
                } else if ((double) Minecraft.player.fallDistance > 0.2) {
                    Minecraft.player.motionX *= 0.93f;
                    Minecraft.player.motionZ *= 0.93f;
                }
            }
        } else if (noSlowMode.currentMode.equals("Sunrise Safe")) {
            if (Minecraft.player.isUsingItem()) {
                if (Minecraft.player.onGround && !Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                    if (Minecraft.player.ticksExisted % 2 == 0) {
                        Minecraft.player.motionX *= 0.47;
                        Minecraft.player.motionZ *= 0.47;
                    }
                } else if ((double) Minecraft.player.fallDistance > 0.2) {
                    Minecraft.player.motionX *= 0.91f;
                    Minecraft.player.motionZ *= 0.91f;
                }
            }
        } else if (noSlowMode.currentMode.equals("Matrix")) {
            if (Minecraft.player.isUsingItem() && !Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                if (!MovementHelper.isBlockUnder(0.2f)) {
                    Minecraft.player.motionY = 0.0;
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
            } else if ((double) Minecraft.player.fallDistance > 0.7) {
                Minecraft.player.motionX *= 0.97f;
                Minecraft.player.motionZ *= 0.97f;
            }
        }
    }

    @EventTarget
    public void onPlayerState(EventPostMotion event) {
        if (!getState() || !Minecraft.player.isUsingItem()) {
            return;
        }
        if (!canNoSLow()) {
            return;
        }
        if (noSlowMode.currentMode.equals("AAC5")) {
            Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
    }

    @EventTarget
    public void onSendPacket(EventPreMotion event) {
        if (!getState() || !Minecraft.player.isUsingItem()) {
            return;
        }
        if (!canNoSLow()) {
            return;
        }
        if (noSlowMode.currentMode.equals("Matrix")) {
            if (Minecraft.player.isUsingItem() && !Minecraft.gameSettings.keyBindJump.isKeyDown() && !MovementHelper.isBlockUnder(0.2f)) {
                event.setPosY(Minecraft.player.ticksExisted % 2 == 0 ? event.getPosY() + 5.0E-4 : event.getPosY() + 3.0E-4);
                event.setOnGround(!Minecraft.player.onGround);
            }
        } else if (noSlowMode.currentMode.equals("Sunrise Old")) {
            if (Minecraft.player.isUsingItem()) {
                if (!Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                    if (!MoonWare.featureManager.getFeatureByClass(KillAura.class).getState() || KillAura.target == null || !Criticals.critMode.currentMode.equals("Sunrise")) {
                        Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        event.setOnGround(false);
                        event.setPosY(Minecraft.player.ticksExisted % 2 != 1 ? event.getPosY() + (double)0.14f : event.getPosY() + (double)0.09f);
                    }
                } else if (Minecraft.player.ticksExisted % 2 == 0) {
                    Minecraft.player.motionX *= 0.97;
                    Minecraft.player.motionZ *= 0.97;
                }
            }
        } else if (noSlowMode.currentMode.equals("Old Matrix") && Minecraft.player.isUsingItem()) {
            if (isNotJump) {
                if (MoonWare.featureManager.getFeatureByClass(Flight.class).getState() || MoonWare.featureManager.getFeatureByClass(Criticals.class).getState() && Criticals.critMode.currentMode.equals("Old Matrix") && MoonWare.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target != null) {
                    return;
                }
                if (sendTimer.hasReached(300.0f)) {
                    Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    sendTimer.reset();
                }
                event.setOnGround(false);
                event.setPosY(Minecraft.player.ticksExisted % 2 != 1 ? event.getPosY() + 0.08 : event.getPosY() + 0.05);
            } else if (!MoonWare.featureManager.getFeatureByClass(TargetStrafe.class).getState() && !MoonWare.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target == null && (double) Minecraft.player.fallDistance > 0.7) {
                Minecraft.player.motionX *= 0.97f;
                Minecraft.player.motionZ *= 0.97f;
            }
        }
    }
}