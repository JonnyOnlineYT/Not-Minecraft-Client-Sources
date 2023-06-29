package Celestial.module.impl.Movement;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.ui.settings.impl.ListSetting;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends Module {
    public static ListSetting noFallMode = new ListSetting("NoFall Mode", "Vanilla", () -> true, "Vanilla", "Matrix");

    public NoFall() {
        super("NoFall", "Отключает урон от падения", ModuleCategory.Movement);
        addSettings(noFallMode);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = noFallMode.getOptions();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Vanilla")) {
            if (Helper.mc.player.fallDistance > 3) {
                event.setOnGround(true);
                Helper.mc.player.connection.sendPacket(new CPacketPlayer(true));
            }
        } else if (mode.equalsIgnoreCase("Matrix")) {
            if (Helper.mc.player.fallDistance >= 2) {
                Helper.mc.timer.timerSpeed = 0.01f;
                Helper.mc.player.connection.sendPacket(new CPacketPlayer.Position(Helper.mc.player.posX, Helper.mc.player.posY, Helper.mc.player.posZ, false));
                Helper.mc.player.connection.sendPacket(new CPacketPlayer.Position(Helper.mc.player.posX, Helper.mc.player.posY, Helper.mc.player.posZ, true));
                Helper.mc.timer.timerSpeed = 1f;
                Helper.mc.player.fallDistance = 0;
            }
        }
    }
}
