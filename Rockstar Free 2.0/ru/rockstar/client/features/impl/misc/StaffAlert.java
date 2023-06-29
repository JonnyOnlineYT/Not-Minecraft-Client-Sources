package ru.rockstar.client.features.impl.misc;

import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventReceivePacket;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;

public class StaffAlert extends Feature {
    public StaffAlert() {
        super("StaffAlert", "Оповещает о модере/хелпере на сервере", 0, Category.MISC);
    }

    public static ArrayList<EntityPlayer> staff = new ArrayList();
    
    private boolean isJoined;
    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        SPacketPlayerListItem packetPlayInPlayerListItem;
        if (event.getPacket() instanceof SPacketPlayerListItem && (packetPlayInPlayerListItem = (SPacketPlayerListItem) event.getPacket()).getAction() == SPacketPlayerListItem.Action.UPDATE_LATENCY) {
            this.isJoined = true;
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (EntityPlayer staffPlayer : GuiPlayerTabOverlay.getPlayers()) {
            if (staffPlayer == null || staffPlayer == mc.player || !staffPlayer.getDisplayName().getUnformattedText().contains("HELPER") && !staffPlayer.getDisplayName().getUnformattedText().contains("ST.HELPER") && !staffPlayer.getDisplayName().getUnformattedText().contains("MODER") && !staffPlayer.getDisplayName().getUnformattedText().contains("ST.MODER") && !staffPlayer.getDisplayName().getUnformattedText().contains("ADMIN") && !staffPlayer.getDisplayName().getUnformattedText().contains("Админ") && !staffPlayer.getDisplayName().getUnformattedText().contains("Хелпер") && !staffPlayer.getDisplayName().getUnformattedText().contains("Модер") || staffPlayer.ticksExisted >= 10 || !this.isJoined)
                continue;
            for (EntityPlayer s : staff) {
            	if (s.getName().equalsIgnoreCase(staffPlayer.getName())) {
            		return;
            	}
            }
            if (mc.world == null || mc.player == null) {
        		staff.remove(staffPlayer);
        	} else {
        		staff.add(staffPlayer);
        	}
            Main.msg(ChatFormatting.WHITE + "Администратор " + ChatFormatting.RESET + staffPlayer.getDisplayName().getUnformattedText() + ChatFormatting.WHITE + " зашел на сервер / вышел из ваниша", true);
            NotificationPublisher.queue("§6Staff Alert", ChatFormatting.WHITE + "Администратор " + ChatFormatting.RESET + staffPlayer.getDisplayName().getUnformattedText() + ChatFormatting.WHITE + " зашел на сервер / вышел из ваниша", NotificationType.WARNING);
            this.isJoined = false;
        }
    }
}