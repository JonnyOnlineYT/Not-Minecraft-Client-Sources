package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.input.EventMouse;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.friend.Friend;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class MiddleClickFriend extends Feature {

    public MiddleClickFriend() {
        super("MiddleClickFriend", "Добавляет игрока в френд лист при нажатии на кнопку мыши", Type.Other);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.getKey() == 2 && Minecraft.pointedEntity instanceof EntityLivingBase) {
            if (MoonWare.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(Minecraft.pointedEntity.getName()))) {
                MoonWare.friendManager.getFriends().remove(MoonWare.friendManager.getFriend(Minecraft.pointedEntity.getName()));
                MWUtils.sendChat(Formatting.RED + "Removed " + Formatting.RESET + "'" + Minecraft.pointedEntity.getName() + "'" + " as Friend!");
                NotificationManager.publicity("MCF", "Removed " + "'" + Minecraft.pointedEntity.getName() + "'" + " as Friend!", 4, NotificationType.INFO);
            } else {
                MoonWare.friendManager.addFriend(new Friend(Minecraft.pointedEntity.getName()));
                MWUtils.sendChat(Formatting.GREEN + "Added " + Formatting.RESET + Minecraft.pointedEntity.getName() + " as Friend!");
                NotificationManager.publicity("MCF", "Added " + Minecraft.pointedEntity.getName() + " as Friend!", 4, NotificationType.SUCCESS);
            }
        }
    }
}