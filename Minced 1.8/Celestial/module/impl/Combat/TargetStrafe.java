package Celestial.module.impl.Combat;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventMove;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.math.RotationHelper;
import Celestial.utils.movement.MovementUtils;
import Celestial.Smertnix;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.entity.EntityLivingBase;

public class TargetStrafe extends Module {
    public NumberSetting tstrafeRange;
    public NumberSetting spd;
    public BooleanSetting autoJump;
    public BooleanSetting damageBoost;
    public NumberSetting boostValue;

    public static int direction = -1;

    public TargetStrafe() {
        super("TargetStrafe", "Стрефит вокруг энтети", ModuleCategory.Combat);
        tstrafeRange = new NumberSetting("Target radius", "бля ахуенаа", 2.4F, 0.1F, 6.0F, 0.1F, () -> true);
        spd = new NumberSetting("Target speed", 0.23F, 0.1F, 2, 0.01F, () -> true);
        autoJump = new BooleanSetting("Autojump", true, () -> true);
        damageBoost = new BooleanSetting("Damage boost", false, () -> true);
        boostValue = new NumberSetting("Damage boost value", 0.5f, 0.1f, 4.0f, 0.01f, () -> damageBoost.getCurrentValue());
        addSettings(tstrafeRange, spd, damageBoost, boostValue, autoJump);
    }

    @EventTarget
    public void onMotionUpdate(EventMove e) {
        EntityLivingBase entity = KillAura.target;
        float[] rotations = RotationHelper.getTargetRotations(entity);
        if (mc.player.getDistanceToEntity(entity) <= tstrafeRange.getNumberValue()) {
            if (mc.player.hurtTime > 0 && damageBoost.getCurrentValue()) {
                MovementUtils.setMotion(e, spd.getNumberValue() + boostValue.getNumberValue(), rotations[0], direction, 0.0);
            } else {
                MovementUtils.setMotion(e, spd.getNumberValue(), rotations[0], direction, 0.0);
            }
        } else if (mc.player.hurtTime > 0 && damageBoost.getCurrentValue()) {
            MovementUtils.setMotion(e, spd.getNumberValue() + boostValue.getNumberValue(), rotations[0], direction, 1.0);
        } else {
            MovementUtils.setMotion(e, spd.getNumberValue(), rotations[0], direction, 1.0);
        }
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        if (mc.player.isCollidedHorizontally)
            switchDirection();
        if (mc.gameSettings.keyBindLeft.isPressed())
            direction = 1;
        if (mc.gameSettings.keyBindRight.isPressed())
            direction = -1;
        if (KillAura.target.getHealth() > 0) {
            if (autoJump.getCurrentValue() && Smertnix.instance.featureManager.getFeature(KillAura.class).isEnabled() && Smertnix.instance.featureManager.getFeature(TargetStrafe.class).isEnabled()) {
                if (mc.player.onGround) {
                    mc.player.jump();
                }
            }
        }
    }

    @EventTarget
    public void onSwitchDir(EventUpdate event) {
        if (KillAura.target == null)
            return;
        if (mc.player.isCollidedHorizontally)
            switchDirection();
        if (mc.gameSettings.keyBindLeft.isKeyDown())
            direction = 1;
        if (mc.gameSettings.keyBindRight.isKeyDown())
            direction = -1;
    }

    private void switchDirection() {
        direction = direction == 1 ? -1 : 1;
    }

    @EventTarget
    public void onMove(EventMove e) {
        if (Smertnix.instance.featureManager.getFeature(KillAura.class).isEnabled() && KillAura.target != null && KillAura.target.getHealth() > 0) {
            if (mc.player.isCollidedHorizontally)
                switchDirection();
            if (KillAura.target.getHealth() > 0.0F)
                onMotionUpdate(e);

        }
    }
}