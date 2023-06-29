package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceiveMessage;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class AutoAuth extends Feature {

    public static String password = "MoonWareIsTheBest123";

    public AutoAuth() {
        super("AutoAuth", "Автоматически регестрируется и логинится на серверах", Type.Other);
    }

    @EventTarget
    public void onReceiveMessage(EventReceiveMessage event) {
        if (event.getMessage().contains("/reg") || event.getMessage().contains("/register") || event.getMessage().contains("Зарегистрируйтесь")) {
            Minecraft.player.sendChatMessage("/reg " + password + " " + password);
            MWUtils.sendChat("Your password: " + Formatting.RED + password);
            NotificationManager.publicity("AutoAuth", "You are successfully registered!", 4, NotificationType.SUCCESS);
        } else if (event.getMessage().contains("Авторизуйтесь") || event.getMessage().contains("/l")) {
            Minecraft.player.sendChatMessage("/login " + password);
            NotificationManager.publicity("AutoAuth", "You are successfully login!", 4, NotificationType.SUCCESS);
        }
    }
}
