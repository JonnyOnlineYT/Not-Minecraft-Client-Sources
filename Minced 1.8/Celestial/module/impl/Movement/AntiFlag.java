package Celestial.module.impl.Movement;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventReceivePacket;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.Smertnix;
import Celestial.ui.notif.NotifModern;
import Celestial.ui.notif.NotifRender;
import Celestial.ui.settings.Setting;
import Celestial.ui.settings.impl.BooleanSetting;


public class AntiFlag extends Module {
    public static BooleanSetting disable = new BooleanSetting("Auto Disable", true, () -> true);

    public AntiFlag() {
        super("AntiFlag", "������������� ��������� ������ ��� ��� �������", ModuleCategory.Movement);
        addSettings(new Setting[]{(Setting) disable});
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (isEnabled() &&
                event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerPosLook) {
            if (Smertnix.instance.featureManager.getFeature(Speed.class).isEnabled()) {
                featureAlert("Speed");
                if (disable.getCurrentValue()) {
                    Smertnix.instance.featureManager.getFeature(Speed.class).toggle();
                }
            } else if (Smertnix.instance.featureManager.getFeature(Spider.class).isEnabled() && Helper.mc.player.isCollidedHorizontally) {
                featureAlert("Spider");
                if (disable.getCurrentValue())
                    Smertnix.instance.featureManager.getFeature(Spider.class).toggle();
            } else if (Smertnix.instance.featureManager.getFeature(elytrafly.class).isEnabled()) {
                featureAlert("ElytraLeaveSunrise");
                if (disable.getCurrentValue()) {
                    Smertnix.instance.featureManager.getFeature(elytraleave.class).toggle();
                }
            } else if (Smertnix.instance.featureManager.getFeature(elytrafly.class).isEnabled() && Helper.mc.player.isInLiquid()) {
                featureAlert("ElytraFlySunrise");
                if (disable.getCurrentValue()) {
                    Smertnix.instance.featureManager.getFeature(elytrafly.class).toggle();
                }
            } else if (Smertnix.instance.featureManager.getFeature(Timer.class).isEnabled()) {
                featureAlert("Timer");
                if (disable.getCurrentValue()) {
                    Smertnix.instance.featureManager.getFeature(Timer.class).toggle();
                }
            }
        }
    }

    public void featureAlert(String feature) {
        NotifRender.queue("Anti Flag Debug", "Module " + feature + " was flagged" + (Helper.mc.player.isInWater() ? " on water" : "") + (Helper.mc.player.isInLava() ? " in lava" : "") + "!", 3, NotifModern.WARNING);
    }
}
