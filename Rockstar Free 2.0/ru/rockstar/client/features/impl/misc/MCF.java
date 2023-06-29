package ru.rockstar.client.features.impl.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventMouseKey;
import ru.rockstar.api.utils.friend.Friend;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;


public class MCF extends Feature {

    EntityLivingBase friend;

    public MCF() {
        super("MiddleClickFriend", "Добавляет игрока в френд лист при нажатии на кнопку мыши", 0, Category.MISC);
    }

    @EventTarget
    public void onMouseEvent(EventMouseKey event) {
        if (event.getKey() == 2 && mc.pointedEntity instanceof EntityLivingBase) {
            if (Main.instance.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(mc.pointedEntity.getName()))) {
                Main.instance.friendManager.getFriends().remove(Main.instance.friendManager.getFriend(mc.pointedEntity.getName()));
                NotificationPublisher.queue("MCF", "Removed " + "'" + mc.pointedEntity.getName() + "'" + " as Friend!", NotificationType.INFO);
            } else {
                Main.instance.friendManager.addFriend(new Friend(mc.pointedEntity.getName()));
                NotificationPublisher.queue("MCF", "Added " + mc.pointedEntity.getName() + " as Friend!", NotificationType.SUCCESS);
            }
        }
    }
}