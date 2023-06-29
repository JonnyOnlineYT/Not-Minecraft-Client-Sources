package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.math.RotationHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class TargetStrafe
        extends Feature {
    public static NumberSetting range;
    public static NumberSetting spd;
    public static NumberSetting boostValue;
    public static NumberSetting boostTicks;
    public static NumberSetting speedIfUsing;
    public static BooleanSetting reversed;
    public static NumberSetting reversedDistance;
    public static BooleanSetting boost;
    public static BooleanSetting autoJump;
    public static BooleanSetting wellMoreExploit;
    public static BooleanSetting smartStrafe;
    public static BooleanSetting usingItemCheck;
    public static BooleanSetting autoShift;
    public static BooleanSetting autoThirdPerson;
    public static BooleanSetting autoDisable;
    public static BooleanSetting alwaysSprint;
    public static BooleanSetting switchOnLook;
    public static NumberSetting lookRadius;
    private double wrap;
    private boolean switchDir = true;
    public boolean canShift;

    public TargetStrafe() {
        super("TargetStrafe", "\u0421\u0442\u0440\u0435\u0444\u0438\u0442 \u0432\u043e\u043a\u0440\u0443\u0433 \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0435\u0439", Type.Combat);
        spd = new NumberSetting("Strafe Speed", 0.23f, 0.1f, 2.0f, 0.01f, () -> true);
        range = new NumberSetting("Strafe Distance", 2.4f, 0.1f, 6.0f, 0.1f, () -> true);
        boost = new BooleanSetting("DamageBoost", false, () -> true);
        boostValue = new NumberSetting("Boost Value", 0.5f, 0.1f, 4.0f, 0.01f, boost::getCurrentValue);
        boostTicks = new NumberSetting("Boost Ticks", 8.0f, 0.0f, 9.0f, 1.0f, boost::getCurrentValue);
        reversed = new BooleanSetting("Reversed", false, () -> true);
        reversedDistance = new NumberSetting("Reversed Distance", 3.0f, 1.0f, 6.0f, 0.1f, () -> reversed.getCurrentValue());
        autoJump = new BooleanSetting("AutoJump", true, () -> true);
        autoShift = new BooleanSetting("AutoShift", false, () -> true);
        wellMoreExploit = new BooleanSetting("OldWellMore Exploit", "\u041d\u0435 \u0440\u0435\u043a\u043e\u043c\u0435\u043d\u0434\u0443\u0435\u0442\u0441\u044f \u043a \u0432\u043a\u043b\u044e\u0447\u0430\u043d\u0438\u044e \u043d\u0430 \u043d\u043e\u0432\u043e\u043c \u0430\u043d\u0442\u0438\u0447\u0438\u0442\u0435 \u0432\u0435\u043b\u043b\u043c\u043e\u0440\u0430!", false, () -> true);
        smartStrafe = new BooleanSetting("Smart Strafe", true, () -> true);
        autoThirdPerson = new BooleanSetting("Auto Third Person", false, () -> true);
        autoDisable = new BooleanSetting("Auto Disable", false, () -> true);
        alwaysSprint = new BooleanSetting("Always Sprint", false, () -> true);
        usingItemCheck = new BooleanSetting("Using Item Check", false, () -> true);
        speedIfUsing = new NumberSetting("Speed if using", 0.21f, 0.1f, 2.0f, 0.01f, usingItemCheck::getCurrentValue);
        switchOnLook = new BooleanSetting("Switch On Look", false, () -> true);
        lookRadius = new NumberSetting("Look Radius", 65.0f, 5.0f, 180.0f, 1.0f, () -> switchOnLook.getCurrentValue());
        addSettings(boost, boostTicks, boostValue, reversed, reversedDistance, autoJump, autoShift, usingItemCheck, speedIfUsing, autoThirdPerson, autoDisable, alwaysSprint, wellMoreExploit, smartStrafe, spd, range, switchOnLook, lookRadius);
    }

    public boolean needToSwitch(double x, double z) {
        if (Minecraft.player.isCollidedHorizontally || Minecraft.gameSettings.keyBindLeft.isPressed() || Minecraft.gameSettings.keyBindRight.isPressed()) {
            return true;
        }
        if (RotationHelper.isAimAtMe(KillAura.target, lookRadius.getCurrentValue()) && switchOnLook.getCurrentValue()) {
            return !switchDir;
        }
        for (int i = (int)(Minecraft.player.posY + 4.0); i >= 0; --i) {
            BlockPos playerPos = new BlockPos(x, i, z);
            if (Minecraft.world.getBlockState(playerPos).getBlock().equals(Blocks.LAVA) || Minecraft.world.getBlockState(playerPos).getBlock().equals(Blocks.FIRE)) {
                return true;
            }
            if (Minecraft.world.isAirBlock(playerPos)) continue;
            return false;
        }
        return true;
    }

    private float wrapDS(float x, float z) {
        double diffX = (double)x - Minecraft.player.posX;
        double diffZ = (double)z - Minecraft.player.posZ;
        return (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix("" + range.getCurrentValue());
        EntityLivingBase entity = KillAura.target;
        if (Minecraft.player == null || Minecraft.world == null) {
            return;
        }
        if (alwaysSprint.getCurrentValue()) {
            Minecraft.player.setSprinting(true);
        }
        if (autoThirdPerson.getCurrentValue()) {
            if (entity.getHealth() > 0.0f && Minecraft.player.getDistanceToEntity(entity) <= KillAura.range.getCurrentValue() && Minecraft.player.getHealth() > 0.0f) {
                if (getState() && MoonWare.featureManager.getFeatureByClass(KillAura.class).getState()) {
                    Minecraft.gameSettings.thirdPersonView = 1;
                }
            } else {
                Minecraft.gameSettings.thirdPersonView = 0;
            }
        }
        if (autoDisable.getCurrentValue() && (Minecraft.screen instanceof DeathScreen && !Minecraft.player.isEntityAlive() || Minecraft.player.ticksExisted <= 1)) {
            EventManager.unregister(this);
            onDisable();
            if (getState()) {
                setState(false);
            }
            NotificationManager.publicity("AutoDisable", "TargetStrafe was toggled off!", 4, NotificationType.INFO);
        }
        if (entity == null) {
            return;
        }
        if (Minecraft.player.getDistanceToEntity(entity) <= KillAura.range.getCurrentValue() && entity.getHealth() > 0.0f) {
            canShift = true;
            float speed = (float) Minecraft.player.hurtTime > boostTicks.getCurrentValue() && boost.getCurrentValue() && !Minecraft.player.onGround ? boostValue.getCurrentValue() : ((Minecraft.player.isUsingItem() || Minecraft.gameSettings.keyBindUseItem.isKeyDown()) && usingItemCheck.getCurrentValue() ? speedIfUsing.getCurrentValue() : spd.getCurrentValue());
            float searchValue = MoonWare.featureManager.getFeatureByClass(TargetStrafe.class).getState() && reversed.getCurrentValue() && Minecraft.player.getDistanceToEntity(KillAura.target) < reversedDistance.getCurrentValue() ? -90.0f : 0.0f;
            float value = MathHelper.clamp(Minecraft.player.getDistanceToEntity(entity), 0.01f, KillAura.range.getCurrentValue());
            wrap = (float)Math.atan2(Minecraft.player.posZ - entity.posZ, Minecraft.player.posX - entity.posX);
            wrap += switchDir ? (double)(speed / value) : (double)(-(speed / value));
            float x = (float)(entity.posX + (double) range.getCurrentValue() * Math.cos(wrap));
            float z = (float)(entity.posZ + (double) range.getCurrentValue() * Math.sin(wrap));
            if (smartStrafe.getCurrentValue() && needToSwitch(x, z)) {
                switchDir = !switchDir;
                wrap += 2.0f * (switchDir ? speed / value : -(speed / value));
                x = (float)(entity.posX + (double) range.getCurrentValue() * Math.cos(wrap));
                z = (float)(entity.posZ + (double) range.getCurrentValue() * Math.sin(wrap));
            }
            if ((float) Minecraft.player.hurtTime > boostTicks.getCurrentValue() && boost.getCurrentValue() && !Minecraft.player.onGround) {
                Minecraft.player.jumpMovementFactor *= 60.0f;
            }
            float reversedValue = !Minecraft.gameSettings.keyBindLeft.isKeyDown() && !Minecraft.gameSettings.keyBindRight.isKeyDown() && !Minecraft.player.isCollidedHorizontally ? searchValue : 0.0f;
            Minecraft.player.motionX = (double)speed * -Math.sin((float)Math.toRadians(wrapDS(x + reversedValue, z + reversedValue)));
            Minecraft.player.motionZ = (double)speed * Math.cos((float)Math.toRadians(wrapDS(x + reversedValue, z + reversedValue)));
            if (autoJump.getCurrentValue() && MoonWare.featureManager.getFeatureByClass(KillAura.class).getState() && MoonWare.featureManager.getFeatureByClass(TargetStrafe.class).getState()) {
                if (wellMoreExploit.getCurrentValue() && Minecraft.player.motionY > 0.0 && !Minecraft.player.onGround) {
                    Minecraft.player.motionY -= 0.00105;
                }
            }
            if (autoShift.getCurrentValue() && canShift) {
                Minecraft.gameSettings.keyBindSneak.setPressed((double) Minecraft.player.fallDistance > (double) KillAura.criticalFallDistance.getCurrentValue() + 0.1);
            }
        }
    }
}