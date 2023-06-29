package ua.apraxia.modules.impl.player;


import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventPreMotion;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.math.GCDFix;
import ua.apraxia.utility.math.MathUtility;
import ua.apraxia.utility.math.TimerUtility;

public class AntiAFK extends Module {

    public final BooleanSetting jump = new BooleanSetting("Jump", true);
    public TimerUtility timerHelper = new TimerUtility();
    public BooleanSetting spin = new BooleanSetting("Spin", true);
    public BooleanSetting sendMessage = new BooleanSetting("Send Message", true);
    public SliderSetting sendDelay = new SliderSetting("Send Delay", 500, 100, 1000, 100);
    public float rot = 0;

    public AntiAFK() {
        super("AntiAFK", Categories.Player);
        addSetting(spin, sendMessage, sendDelay, jump);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if(mc.gameSettings.keyBindJump.pressed) {
            return;
        }
        if (jump.value && mc.player.onGround) {
            mc.player.jump();
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {

        if (spin.value) {
            float yaw = GCDFix.getFixedRotation((float) (Math.floor((double) this.spinAim(25)) + (double) MathUtility.randomNumber(-4.0F, 1.0F)));
            event.setYaw(yaw);
            mc.player.renderYawOffset = yaw;
            mc.player.rotationPitchHead = 0;
            mc.player.rotationYawHead = yaw;
        }
        if (timerHelper.hasReached(sendDelay.value * 20) && sendMessage.value) {
            mc.player.sendChatMessage("/penis");
            timerHelper.reset();
        }
    }
    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}
