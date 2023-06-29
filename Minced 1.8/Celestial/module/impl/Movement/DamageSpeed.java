package Celestial.module.impl.Movement;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventReceivePacket;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.math.TimerHelper;
import Celestial.utils.movement.MovementUtils;

public class DamageSpeed extends Module {
    public static float ticks = 0;
    public TimerHelper timerHelper = new TimerHelper();
    private boolean damage;
    public DamageSpeed() {
        super("DamageSpeed", "Увеличивает вашу скорость", ModuleCategory.Movement);

    }

    @EventTarget
    public void onUpdate(EventReceivePacket event) {
        double radian = MovementUtils.getAllDirection();
        if (!Helper.mc.gameSettings.keyBindSneak.isKeyDown() && !Helper.mc.gameSettings.keyBindJump.isKeyDown() && !Helper.mc.player.isCollidedHorizontally  && this.damage &&  MovementUtils.isMoving()) {
            if (!Helper.mc.gameSettings.keyBindSneak.isKeyDown() && Helper.mc.player.onGround) {
                Helper.mc.player.addVelocity(-Math.sin(radian) * 9.5 / 24.5, 0, Math.cos(radian) * 9.5 / 24.5);
            }
        }
        if (!Helper.mc.gameSettings.keyBindSneak.isKeyDown() && !Helper.mc.gameSettings.keyBindJump.isKeyDown() && !Helper.mc.player.isCollidedHorizontally  && this.damage && MovementUtils.isMoving()) {

            Helper.mc.player.isCollidedHorizontally = true;
            ticks += 1;
            //   ChatUtils.addChatMessage(ChatFormatting.AQUA + "[debug] " + ChatFormatting.GREEN + "current tick is" + " " + ChatFormatting.RED + ticks); //debug
        }

        if (isEnabled() &&
                event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerPosLook) {
            super.onDisable();

        }

        if (DamageSpeed.mc.player.hurtTime == 9) {
            this.damage = true;
        }
        if (ticks == 17) {
            super.onDisable();
            this.damage = false;
        }
    }

    @Override
    public void onDisable() {
        ticks = 0;
        Helper.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}