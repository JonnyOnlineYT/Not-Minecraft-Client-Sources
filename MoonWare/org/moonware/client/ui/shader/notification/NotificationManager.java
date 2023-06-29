package org.moonware.client.ui.shader.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Formatting;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.feature.impl.hud.Notifications;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.blur.GaussianBlur;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.helpers.render.ScreenHelper;
import org.moonware.client.utils.MWFont;
import org.moonware.client.utils.MWUtils;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

public class NotificationManager
        implements Helper {
    private static final List<Notification> notifications = new CopyOnWriteArrayList<Notification>();
    public static float y;
    private static float scale;
    private static float alphaanim;
    private static float scalehelper;

    public static void publicity(String title, String content, int second, NotificationType type) {
        Font font = Minecraft.font;
        notifications.add(new Notification(title, content, type, second * 1000, font));
    }


    public static void renderNotification(ScaledResolution sr) {

        if (Notifications.backGroundMode.currentMode.equals("Blur") && Minecraft.gameSettings.ofFastRender) {
            Minecraft.gameSettings.ofFastRender = false;
        }
        if (!notifications.isEmpty() && MoonWare.featureManager.getFeatureByClass(Notifications.class).getState()) {
            int srScaledHeight = sr.getScaledHeight();
            y = srScaledHeight - 45;
            int scaledWidth = sr.getScaledWidth();
            for (Notification notification : notifications) {
                if (notification == null) continue;
                ScreenHelper screenHelper = notification.getTranslate();
                int width = notification.getWidth();

                if (!notification.getTimer().hasReached(notification.getTime() - 120)) {
                    if (!notification.getTimer().hasReached(250))
                        notification.setScale(1.2);
                    else
                        notification.setScale(1);

                    screenHelper.calculateCompensation((float) (scaledWidth - width + 20), y, 0.25f, 0.55f);
                    alphaanim = AnimationHelper.calculateCompensation(alphaanim, 251.0F, 0, 1);
                } else {
                    if (!notification.getTimer().hasReached(notification.getTime())) {
                        screenHelper.calculateCompensation((float) (scaledWidth - width), y, 0.32f, 0.55f);
                        notification.setScale(1.1f);
                    }
                    else{
                        screenHelper.calculateCompensation((float) (scaledWidth + 65), y, 0.25f, 0.55f);
                        notification.setScale(0.5f);
                    }

                    alphaanim = AnimationHelper.calculateCompensation(alphaanim, 10.0F, 0, 1);


                    if (Minecraft.player != null && Minecraft.world != null && notification.getTimer().getTime() > (long) (notification.getTime() + 100)) {
                        notifications.remove(notification);
                    }
                }
                if (notification.getTimer().getTime() <= (long) notification.getTime()) {
                    y -= 45.0f;
                }
                float translateX = screenHelper.getX();
                float translateY = screenHelper.getY();
                float nScale = (float) notification.getScale();
                Runnable bg = () ->MWUtils.scale(translateX -20, translateY,nScale, () -> drawRunnable(translateX - 20,translateY,notification));
                if ((notification.getScale() > 0.99 && notification.getScale() < 1.09)) {
                    //RenderUtils2.drawBlur(7, () -> RenderHelper2.drawRainbowRound(translateX,translateY, notification.getWidth() - 10, 28, 6, true,true,true, true,2,4));
                }
                if (notification.getScale() >= 0.99f && notification.getScale() <= 1.01f) {
                    glPushMatrix();
                    StencilUtil.initStencilToWrite();
                    bg.run();
                    StencilUtil.readStencilBuffer(1);
                    GaussianBlur.renderBlur(8);
                    StencilUtil.uninitStencilBuffer();
                    RenderUtils2.drawShadow(2,2,bg);

                    glPopMatrix();
                }
                MWUtils.scale(translateX -20, translateY,nScale, () -> drawRunnable(translateX - 20,translateY,notification));
                //RenderUtils2.drawBlur(7, () -> MWUtils.scale(translateX, translateY,nScale, () -> drawRunnable(translateX,translateY,notification)));
            }
        }
    }

    public static void drawRunnable(double translateX,double translateY, Notification notification) {
        RoundedUtil.drawRound((float) translateX, (float) translateY, notification.getWidth() - 10, 38, 6, new Color(70, 70, 70,180));
        RenderHelper.drawImage(new Namespaced("moonware/zicons/logo.png"), (float) translateX, (float) translateY,36,36,Color.WHITE);
        RoundedUtil.drawRound((float) (translateX + 20 + 2 + 14), (float) (translateY + 2), 2,34,1.3f, new Color(255,255,255,255));
        MWFont.MONTSERRAT_BOLD.get(20).draw("Информация",  translateX + 28 + 14, translateY + 3,-1);
        MWFont.MONTSERRAT_BOLD.get(18).draw(notification.getTitle(),  translateX + 28 + 14, translateY + 1 + MWFont.MONTSERRAT_BOLD.get(20).getHeight() + 6 + 4,new Color(231,231,231).getRGB());
        MWFont.MONTSERRAT_BOLD.get(18).drawGradient(notification.getContent() + Formatting.BOLD + ".",  translateX + 28 + 14, translateY + 28 - 2 + 4,new Color(190,190,190).getRGB());
    }
}