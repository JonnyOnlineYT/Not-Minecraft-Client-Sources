package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.settings.impl.ListSetting;
import org.lwjgl.input.Keyboard;

public class AutoShift extends Feature {

    public static ListSetting mode;

    public AutoShift() {
        super("AutoShift", "Игрок автоматически приседает при нажатии на ЛКМ", Type.Combat);
        mode = new ListSetting("ShiftTap Mode", "Client", () -> true, "Client", "Packet");
        addSettings(mode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mode.currentMode.equals("Client")) {
            if (!Keyboard.isKeyDown(Minecraft.gameSettings.keyBindSneak.getKeyCode())) {
                Minecraft.gameSettings.keyBindSneak.pressed = Minecraft.gameSettings.keyBindAttack.isKeyDown();
            }
        } else if (mode.currentMode.equals("Packet")) {
            if (Minecraft.gameSettings.keyBindAttack.isKeyDown()) {
                Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
    }

    @Override
    public void onDisable() {
        Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.STOP_SNEAKING));
        super.onDisable();
    }
}
