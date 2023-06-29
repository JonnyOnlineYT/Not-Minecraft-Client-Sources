package de.strafe.modules.combat;

import de.strafe.modules.Category;
import com.eventapi.EventTarget;
import de.strafe.events.EventPreMotion;
import de.strafe.events.EventUpdate;
import de.strafe.modules.Module;
import de.strafe.utils.Rotation;
import de.strafe.utils.RotationUtil;
import de.strafe.utils.TimeUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author XButtonn
 * @created 21.02.2022 - 12:35
 */

public class Killaura extends Module {

    private final TimeUtil timer = new TimeUtil();
    private EntityLivingBase target;

    public Killaura() {
        super("Killaura", 0, Category.COMBAT);
    }

    @Override
    public void onEnable() {
        target = null;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        RotationUtil.rotation = null;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        setTarget();

        if (target != null)
            attack();
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        if (target == null) {
            RotationUtil.rotation = null;
            return;
        }

        event.setYaw(RotationUtil.getCurrentRotation().getYaw());
        event.setPitch(RotationUtil.getCurrentRotation().getPitch());

        setRotation();
    }

    void setTarget() {
        final Stream<?> stream = mc.theWorld.loadedEntityList.stream().filter(this::validate).sorted(Comparator.comparing(e -> mc.thePlayer.getDistanceToEntity(e)));
        target = (EntityLivingBase) stream.findFirst().orElse(null);
    }

    void attack() {
        if (this.timer.hasReached(6500 / 10)) {
            this.timer.reset();
            mc.clickMouse();
            mc.thePlayer.swingItem();
            mc.playerController.attackEntity(mc.thePlayer, target);
        }
    }

    void setRotation() {
        final Rotation rotation = RotationUtil.killauraRotations(target);
        final Rotation smooth = new Rotation(
                RotationUtil.updateRotation(RotationUtil.getCurrentRotation().getYaw(), rotation.getYaw(), 150),
                RotationUtil.updateRotation(RotationUtil.getCurrentRotation().getYaw(), rotation.getPitch(), 150));

        RotationUtil.rotation = smooth;
    }

    public boolean validate(Object o) {
        if (o instanceof EntityPlayerSP) return false;
        if (!(o instanceof EntityLivingBase)) return false;
        final EntityLivingBase entity = (EntityLivingBase) o;
        if (entity.getHealth() <= 0 || entity.isDead) return false;
        if (entity.isInvisible()) return false;
        if (mc.thePlayer.getDistanceToEntity(entity) > 4) return false;
        return mc.thePlayer.canEntityBeSeen(entity);
    }
}
