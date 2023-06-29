package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class StaffAlert
        extends Feature {
    private boolean isJoined;

    public StaffAlert() {
        super("StaffAlert", "\u0423\u0432\u0435\u0434\u043e\u043c\u043b\u044f\u0435\u0442 \u0432\u0430\u0441, \u043a\u043e\u0433\u0434\u0430 \u0445\u0435\u043b\u043f\u0435\u0440 \u0438\u043b\u0438 \u043c\u043e\u0434\u0435\u0440\u0430\u0442\u043e\u0440 \u0437\u0430\u0448\u0435\u043b \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440", Type.Other);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        SPacketPlayerListItem packetPlayInPlayerListItem;
        if (event.getPacket() instanceof SPacketPlayerListItem && (packetPlayInPlayerListItem = (SPacketPlayerListItem)event.getPacket()).getAction() == SPacketPlayerListItem.Action.UPDATE_LATENCY) {
            isJoined = true;
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (EntityPlayer staffPlayer : GuiPlayerTabOverlay.getPlayers()) {
            if (staffPlayer == null || staffPlayer == Minecraft.player || !staffPlayer.getDisplayName().asString().contains("HELPER") && !staffPlayer.getDisplayName().asString().contains("SHELPER-1") && !staffPlayer.getDisplayName().asString().contains("SHELPER") && !staffPlayer.getDisplayName().asString().contains("SHELPER-2") && !staffPlayer.getDisplayName().asString().contains("MODER") && !staffPlayer.getDisplayName().asString().contains("J.MODER") || staffPlayer.ticksExisted >= 10 && !staffPlayer.getDisplayName().asString().contains("YT") || !isJoined) continue;
            MWUtils.sendChat(Formatting.RED + "Staff " + staffPlayer.getName() + Formatting.WHITE + " was connect/vanish");
            NotificationManager.publicity("Staff Alert", Formatting.RED + "Staff " + staffPlayer.getName() + Formatting.WHITE + " was connect/vanish", 10, NotificationType.WARNING);
            isJoined = false;
        }
    }
}
