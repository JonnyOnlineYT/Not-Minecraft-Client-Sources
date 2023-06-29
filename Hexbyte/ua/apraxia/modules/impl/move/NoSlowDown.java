package ua.apraxia.modules.impl.move;


import net.minecraft.network.play.client.CPacketPlayer;
import ua.apraxia.Hexbyte;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.packet.EventSendPacket;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;

public class NoSlowDown extends Module {
    public static SliderSetting percentage = new SliderSetting("Percentage", 100, 0, 100, 1);
    private final ModeSetting noSlowMode = new ModeSetting("NoSlow Mode", "Matrix", "Vanilla", "Sunrise");
    public int usingTicks;

    public NoSlowDown() {
        super("NoSlowDown", Categories.Movement);
        addSetting(noSlowMode, percentage);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket eventSendPacket) {
        CPacketPlayer packet = (CPacketPlayer) eventSendPacket.getPacket();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        usingTicks = mc.player.isUsingItem() ? ++usingTicks : 0;
        if (!Hexbyte.getInstance().moduleManagment.getModule(NoSlowDown.class).isModuleState() || !mc.player.isUsingItem()) {
            return;
        }
        if (noSlowMode.currentMode.equals("Sunrise")) {
            if (mc.player.isUsingItem()) {
                if (mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (mc.player.ticksExisted % 2 == 0) {
                        mc.player.motionX *= 0.35;
                        mc.player.motionZ *= 0.35;
                    }
                } else if (mc.player.fallDistance > 0.2) {
                    mc.player.motionX *= 0.9100000262260437;
                    mc.player.motionZ *= 0.9100000262260437;
                }
            }
        }
    }
}