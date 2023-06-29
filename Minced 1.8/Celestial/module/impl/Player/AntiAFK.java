package Celestial.module.impl.Player;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.math.GCDFix;
import Celestial.utils.math.MathematicHelper;
import Celestial.utils.math.TimerHelper;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.NumberSetting;
import org.apache.commons.lang3.RandomStringUtils;

public class AntiAFK extends Module {
    private final BooleanSetting sendMessage;
    private final BooleanSetting spin;

    public final NumberSetting sendDelay;
    public final BooleanSetting jump = new BooleanSetting("Jump", true, () -> true);
    public TimerHelper timerHelper = new TimerHelper();
    public float rot = 0;

    public AntiAFK() {
        super("AntiAFK", "Не дает серверу кикнуть вас, за афк", ModuleCategory.Player);
        spin = new BooleanSetting("Spin", true, () -> true);
        sendMessage = new BooleanSetting("Random SMS", true, () -> true);
        sendDelay = new NumberSetting("Send Delay", 1, 1, 20, 1, sendMessage::getCurrentValue, NumberSetting.NumberType.SEC);
        addSettings(spin, sendMessage, sendDelay, jump);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (jump.getCurrentValue() && Helper.mc.player.onGround && !Helper.mc.gameSettings.keyBindJump.pressed) {
            Helper.mc.player.jump();
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (spin.getCurrentValue()) {
            float yaw = GCDFix.getFixedRotation((float) (Math.floor((double) this.spinAim(25)) + (double) MathematicHelper.randomizeFloat(-4.0F, 1.0F)));
            event.setYaw(yaw);
            Helper.mc.player.renderYawOffset = yaw;
            Helper.mc.player.rotationPitchHead = 0;
            Helper.mc.player.rotationYawHead = yaw;
        }
        if (timerHelper.hasReached(sendDelay.getCurrentValue() * 1000) && sendMessage.getCurrentValue()) {
            Helper.mc.player.sendChatMessage("/" + RandomStringUtils.randomAlphabetic(3) + RandomStringUtils.randomNumeric(3));
            timerHelper.reset();
        }
    }

    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}
