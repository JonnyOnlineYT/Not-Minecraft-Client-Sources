package de.strafe.modules.movement;

import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.utils.IMinecraft;
import de.strafe.utils.TimeUtil;
import com.eventapi.EventTarget;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;

public class LongJump extends Module {
    public TimeUtil time = new TimeUtil();

    public LongJump() {
        super("Long Jump", 0, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if ((IMinecraft.mc.gameSettings.keyBindForward.isKeyDown() || IMinecraft.mc.gameSettings.keyBindLeft.isKeyDown() || IMinecraft.mc.gameSettings.keyBindRight.isKeyDown() || IMinecraft.mc.gameSettings.keyBindBack.isKeyDown()) && IMinecraft.mc.gameSettings.keyBindJump.isKeyDown()) {
            float dir = IMinecraft.mc.thePlayer.rotationYaw + ((IMinecraft.mc.thePlayer.moveForward < 0) ? 180 : 0) + ((IMinecraft.mc.thePlayer.moveStrafing > 0) ? (-90F * ((IMinecraft.mc.thePlayer.moveForward < 0) ? -.5F : ((IMinecraft.mc.thePlayer.moveForward > 0) ? .4F : 1F))) : 0);
            float xDir = (float) Math.cos((dir + 90F) * Math.PI / 180);
            float zDir = (float) Math.sin((dir + 90F) * Math.PI / 180);
            if (IMinecraft.mc.thePlayer.isCollidedVertically && (IMinecraft.mc.gameSettings.keyBindForward.isKeyDown() || IMinecraft.mc.gameSettings.keyBindLeft.isKeyDown() || IMinecraft.mc.gameSettings.keyBindRight.isKeyDown() || IMinecraft.mc.gameSettings.keyBindBack.isKeyDown()) && IMinecraft.mc.gameSettings.keyBindJump.isKeyDown()) {
                IMinecraft.mc.thePlayer.motionX = xDir * .42F;
                IMinecraft.mc.thePlayer.motionZ = zDir * .42F;
                IMinecraft.mc.thePlayer.motionY += 2.5F;
                IMinecraft.mc.timer.timerSpeed = 1.0F;
            }
            if (IMinecraft.mc.thePlayer.motionY == .33319999363422365 && (IMinecraft.mc.gameSettings.keyBindForward.isKeyDown() || IMinecraft.mc.gameSettings.keyBindLeft.isKeyDown() || IMinecraft.mc.gameSettings.keyBindRight.isKeyDown() || IMinecraft.mc.gameSettings.keyBindBack.isKeyDown())) {
                if (IMinecraft.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    IMinecraft.mc.thePlayer.motionX = xDir * 1.34;
                    IMinecraft.mc.thePlayer.motionZ = zDir * 1.34;
                } else {
                    IMinecraft.mc.thePlayer.motionX = xDir * 1.261;
                    IMinecraft.mc.thePlayer.motionZ = zDir * 1.261;
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
        IMinecraft.mc.thePlayer.motionY *= 1F;
        IMinecraft.mc.thePlayer.motionX *= 1F;
        IMinecraft.mc.thePlayer.motionZ *= 1F;
    }

}
