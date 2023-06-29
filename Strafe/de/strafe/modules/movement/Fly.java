package de.strafe.modules.movement;

import com.eventapi.EventTarget;
import de.strafe.events.EventUpdate;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.settings.impl.BooleanSetting;
import de.strafe.settings.impl.ModeSetting;
import de.strafe.settings.impl.NumberSetting;
import de.strafe.utils.IMinecraft;
import de.strafe.utils.MovementUtil;
import de.strafe.utils.TimeUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {
    public TimeUtil time = new TimeUtil();

    public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Blocks Mc", "Verus", "Glide", "Damage Bedwars");
    public NumberSetting speed = new NumberSetting("Speed", 6, 4, 10, 1);
    public BooleanSetting boost = new BooleanSetting("Boost", true);
    public BooleanSetting kick = new BooleanSetting("AntiKick", true);

    public Fly() {
        super("Fly", 0, Category.MOVEMENT);
        addSettings(mode, speed, kick, boost);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (mc.thePlayer.isDead) {
            mc.timer.timerSpeed = 1.0F;
            MovementUtil.setMotion(0);
        }
        if (mode.is("Vanilla")) {
            IMinecraft.mc.thePlayer.motionY = 0F;
            MovementUtil.setMotion(speed.getValue() / 2);
            if (!IMinecraft.mc.thePlayer.onGround) {
                if (kick.enabled) {
                    IMinecraft.mc.timer.timerSpeed = 0.26F;
                    MovementUtil.setMotion(speed.getValue() + 1.8);
                }
                if (IMinecraft.mc.gameSettings.keyBindJump.isKeyDown()) {
                    IMinecraft.mc.thePlayer.motionY = 2.5F;
                }
                if (IMinecraft.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    IMinecraft.mc.thePlayer.motionY = -2.5F;
                }
            }
        }
        if (mode.is("Blocks Mc")) {
            if (mc.thePlayer.hurtTime != 0) {
                if (!mc.thePlayer.capabilities.isCreativeMode) {
                    MovementUtil.setMotion(speed.getValue() * 1.90);
                    IMinecraft.mc.thePlayer.motionY = 0.00000001F;
                    IMinecraft.mc.timer.timerSpeed = 0.18F;
                    if(boost.enabled) {
                        if (time.hasReached(Long.parseLong("500"))) {
                            MovementUtil.setMotion(speed.getValue() * 2.35);
                        }
                        if (IMinecraft.mc.gameSettings.keyBindJump.isKeyDown()) {
                            IMinecraft.mc.thePlayer.motionY = 1.5F;
                        }
                        if (IMinecraft.mc.gameSettings.keyBindSneak.isKeyDown()) {
                            IMinecraft.mc.thePlayer.motionY = -1.5F;
                        }
                    }

                }
            }
        }
        if (mode.is("Verus")) {
            if (mc.thePlayer.hurtTime != 0) {
                if (!mc.thePlayer.capabilities.isCreativeMode) {
                    MovementUtil.setMotion(speed.getValue() * 1.75);
                    IMinecraft.mc.thePlayer.motionY = 0.0000001F;
                    IMinecraft.mc.timer.timerSpeed = 0.28F;
                }
            }
        }
        if (mode.is("Glide")) {
            IMinecraft.mc.thePlayer.motionY = 0F;
            IMinecraft.mc.timer.timerSpeed = 0.28F;
            MovementUtil.setMotion(speed.getValue() + 2.25);
            if (time.hasReached(200)) {
                MovementUtil.setMotion(speed.getValue() + 2);
                time.reset();
            }
            if (mc.gameSettings.keyBindBack.isPressed()) {
                MovementUtil.setMotion(0);
                IMinecraft.mc.timer.timerSpeed = 0.15F;
            }
        }
    }

    public void damage() {
        IMinecraft.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(IMinecraft.mc.thePlayer.posX, IMinecraft.mc.thePlayer.posY + 3.35, IMinecraft.mc.thePlayer.posZ, false));
        IMinecraft.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(IMinecraft.mc.thePlayer.posX, IMinecraft.mc.thePlayer.posY, IMinecraft.mc.thePlayer.posZ, false));
        IMinecraft.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(IMinecraft.mc.thePlayer.posX, IMinecraft.mc.thePlayer.posY, IMinecraft.mc.thePlayer.posZ, true));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mode.is("Blocks Mc") || mode.is("Verus")) {
            damage();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.timer.timerSpeed = 1.0F;
        MovementUtil.setMotion(0);
    }
}
