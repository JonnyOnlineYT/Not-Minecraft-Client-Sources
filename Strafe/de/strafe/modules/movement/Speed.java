package de.strafe.modules.movement;

import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.settings.impl.BooleanSetting;
import de.strafe.settings.impl.ModeSetting;
import de.strafe.settings.impl.NumberSetting;
import de.strafe.utils.IMinecraft;
import com.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Ncp B-Hop", "Ncp B-Hop", "Y-Port");
    public NumberSetting speed = new NumberSetting("Speed", 1.1, 1, 2, 1);
    public NumberSetting timer = new NumberSetting("Timer", 1.01, 1, 1, 1);

    public Speed() {
        super("Speed", 0, Category.MOVEMENT);
        addSettings(mode, speed, timer);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (IMinecraft.mc.thePlayer.moveForward > 0 || IMinecraft.mc.thePlayer.moveStrafing > 0) {
            if (IMinecraft.mc.thePlayer.onGround) {
                IMinecraft.mc.timer.timerSpeed = 1F;
                IMinecraft.mc.thePlayer.jump();
                IMinecraft.mc.thePlayer.motionX *= speed.getValue();
                IMinecraft.mc.thePlayer.motionZ *= speed.getValue();
            } else {
                if (IMinecraft.mc.thePlayer.ticksExisted % 3 == 0) {
                    IMinecraft.mc.timer.timerSpeed = 1.2F;
                } else {
                    IMinecraft.mc.timer.timerSpeed = (float) timer.getValue();
                }

                IMinecraft.mc.thePlayer.motionX *= 1.0F;
                IMinecraft.mc.thePlayer.motionZ *= 1.0F;
                IMinecraft.mc.thePlayer.jumpMovementFactor = 0.0265F;
                IMinecraft.mc.thePlayer.moveStrafing *= 1F;
                IMinecraft.mc.thePlayer.moveForward *= 1F;
                if (IMinecraft.mc.thePlayer.fallDistance >= 0.3) {
                    IMinecraft.mc.timer.timerSpeed = 1.2F;
                    IMinecraft.mc.thePlayer.motionY -= 62F;
                }
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        IMinecraft.mc.timer.timerSpeed = 1.0F;
    }

}
