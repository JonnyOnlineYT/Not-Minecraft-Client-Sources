package ru.rockstar.client.features.impl.player;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventReceiveMessage;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class AutoAuth extends Feature {

    public static String password = "qwerty123";

    public AutoAuth() {
        super("AutoAuth", "Автоматически регестрируется и логинится на серверах",0, Category.MISC);
    }

    @EventTarget
    public void onReceiveMessage(EventReceiveMessage event) {
        if (event.getMessage().contains("/reg") || event.getMessage().contains("/register") || event.getMessage().contains("Зарегистрируйтесь")) {
            mc.player.sendChatMessage("/reg " + password + " " + password);
            Main.msg("Your password: " + ChatFormatting.RED + password,true);
            NotificationPublisher.queue("AutoAuth", "You are successfully registered!", NotificationType.SUCCESS);
        } else if (event.getMessage().contains("Авторизуйтесь") || event.getMessage().contains("/l")) {
            mc.player.sendChatMessage("/login " + password);
            NotificationPublisher.queue("AutoAuth", "You are successfully login!",  NotificationType.SUCCESS);
        }
    }
}
