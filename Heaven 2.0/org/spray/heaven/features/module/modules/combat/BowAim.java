package org.spray.heaven.features.module.modules.combat;

import org.lwjgl.input.Keyboard;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.EntityUtil;

@ModuleInfo(name = "BowAim", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class BowAim extends Module {

    private Setting players = register(new Setting("Players", true));
    private Setting friends = register(new Setting("Friends", false).setVisible(() -> players.isToggle()));
    private Setting invisibles = register(new Setting("Invisibles", false));
    private Setting mobs = register(new Setting("Mobs", true));
    private Setting animals = register(new Setting("Animals", true));
    private Setting ignoreNaked = register(new Setting("Ignore Naked", false));
    private Setting ignoreWalls = register(new Setting("Ignore Walls", false));

    private Setting lockview = register(new Setting("Lock View", false));

    private Setting range = register(new Setting("Range", 40D, 0D, 70D));
    private Setting fov = register(new Setting("FOV", 360D, 0D, 360D));

    private double sideMultiplier;
    private double upMultiplier;
    private Vec3d predict;

    @EventTarget
    public void onMotionUpdate(MotionEvent event) {
        if (event.getType().equals(EventType.PRE)) {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive()
                    && mc.player.getItemInUseMaxCount() > 0) {
                Entity target = EntityUtil.getTarget(range.getValue(), fov.getValue(), players.isToggle(),
                        friends.isToggle(), ignoreNaked.isToggle(), invisibles.isToggle(), mobs.isToggle(),
                        animals.isToggle(), ignoreWalls.isToggle());

                if (target == null)
                    return;

                double xPos = target.posX;
                double yPos = target.posY;
                double zPos = target.posZ;
                sideMultiplier = Wrapper.getPlayer().getDistanceToEntity(target)
                        / ((Wrapper.getPlayer().getDistanceToEntity(target) / 2) / 1) * 5;
                upMultiplier = (Wrapper.getPlayer().getDistanceSqToEntity(target) / 320) * 1.1;
                predict = new Vec3d((xPos - 0.5) + (xPos - target.lastTickPosX) * sideMultiplier, yPos + upMultiplier,
                        (zPos - 0.5) + (zPos - target.lastTickPosZ) * sideMultiplier);
                float[] rotation = lookAtPredict(predict);

                event.setRotation(rotation[0], rotation[1], lockview.isToggle());
            }
        }
    }

    private float[] lookAtPredict(Vec3d vec) {
        double diffX = vec.xCoord + 0.5 - Wrapper.getPlayer().posX;
        double diffY = vec.yCoord + 0.5 - (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
        double diffZ = vec.zCoord + 0.5 - Wrapper.getPlayer().posZ;
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        return new float[] {
                Wrapper.getPlayer().rotationYaw + MathHelper.wrapDegrees(yaw - Wrapper.getPlayer().rotationYaw),
                Wrapper.getPlayer().rotationPitch + MathHelper.wrapDegrees(pitch - Wrapper.getPlayer().rotationPitch) };
    }

}
