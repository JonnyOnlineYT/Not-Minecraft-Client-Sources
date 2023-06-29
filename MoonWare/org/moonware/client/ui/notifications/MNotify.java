package org.moonware.client.ui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.hud.Notifications;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.ui.notifications.Animations.AHL;
import org.moonware.client.ui.notifications.impl.Type;
import org.moonware.client.utils.MWFont;
import org.moonware.client.utils.MWUtils;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MNotify {
    public static float y;
    private static final List<Notification> notifications = new CopyOnWriteArrayList<Notification>();

    public static void send(String title, String content, int second, Type type) {
        notifications.add(new Notification(title, content, type, second * 1000, 1200, 1000));
    }

    public static void render() {
        if (!notifications.isEmpty() && MoonWare.featureManager.getFeatureByClass(Notifications.class).getState()) {
            int srScaledHeight = Minecraft.getScaledRoundedHeight();
            y =-30;
            int scaledWidth = Minecraft.getScaledRoundedWidth();
            for (Notification notification : notifications) {
                if (notification == null) continue;
                AHL screenHelper = notification.getTranslate();
                int width = MWFont.MONTSERRAT_BOLD.get(20).getWidth(notification.getContent()) + (Notifications.timePeriod.getCurrentValue() ? 35 : 15);

                if (!notification.getTimer().hasReached(notification.getTime() - 100)) {
                    screenHelper.setX(notification.getWidth() + 5);
                    screenHelper.setY((int) y);
                    screenHelper.setS(1);
                } else {
                    screenHelper.setX(notification.getWidth() + 20);
                    screenHelper.setY((int) y);
                    screenHelper.setS(0);
                    if (Minecraft.player != null && Minecraft.world != null && notification.getTimer().getTime() > (long) (notification.getTime() + 500)) {
                        notifications.remove(notification);
                    }

                }
                if (notification.getTimer().getTime() <= (long) notification.getTime()) {
                    y -= 30.0f;
                }
                float translateX = screenHelper.getX();
                float translateY = screenHelper.getY();
                float S = screenHelper.getS();

                float yNM = MathHelper.clamp(translateY,0,y);
                MWUtils.scale(S,S,1,() ->
                        drawRunnable(scaledWidth,translateX,translateY,notification,srScaledHeight)
                );

                System.out.println(translateY);
            }
        }
    }
    public static void drawRunnable (float scaledWidth, float translateX, float translateY, Notification notification, float srScaledHeight) {
        RenderUtils2.drawBlur(7,() -> RoundedUtil.drawRound(scaledWidth - translateX,srScaledHeight + translateY,notification.getWidth(),25,3,new Color(31,31,31)));
        RoundedUtil.drawRound(scaledWidth - translateX,srScaledHeight + translateY,notification.getWidth(),25,3,new Color(31,31,31,90));
        MWFont.MONTSERRAT_BOLD.get(22).draw(notification.getTitle().toUpperCase(),scaledWidth - translateX,srScaledHeight + translateY,-1);
    }
}