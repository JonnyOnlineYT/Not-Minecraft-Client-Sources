package ru.rockstar.api.utils.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.utils.Helper;
import ru.rockstar.api.utils.font.FontRenderer;
import ru.rockstar.api.utils.math.MathematicHelper;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.api.utils.render.Shifting;
import ru.rockstar.client.features.impl.display.Notifications;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.animation.Interpolator;

public final class NotificationPublisher implements Helper {
    private static final List<Notification> notifications = new CopyOnWriteArrayList<>();
    public double anim = 0;

    public void publish() {
        if (Main.instance.featureDirector.getFeatureByClass(Notifications.class).isToggled()) {
        	String mode = Notifications.notifMode.getOptions();
        	
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            int srScaledHeight = sr.getScaledHeight();
            int scaledWidth = sr.getScaledWidth();
            int y = srScaledHeight - 60;
            for (Notification notification : notifications) {
            	if (mode.equalsIgnoreCase("Rect")) {
            		Shifting translate = notification.getTranslate();
                    int width = notification.getWidth();
                    if (!notification.getTimer().elapsed(notification.getTime())) {
                        notification.scissorBoxWidth = AnimationHelper.animate(width, notification.scissorBoxWidth,
                                0.05 * Minecraft.getSystemTime() / 5);
                        translate.interpolate(scaledWidth - width, y, 0.015);
                    } else {
                        notification.scissorBoxWidth = AnimationHelper.animate(0.0, notification.scissorBoxWidth,
                                0.05 * Minecraft.getSystemTime() / 4.0);
                        if (notification.scissorBoxWidth < 1.0) {
                            notifications.remove(notification);
                        }
                        y += 30;
                    }
                    float notifX = (float) (translate.getX() + translate.getY() + 30 - y);
                    float notifY = (float) translate.getY();
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    
                    this.anim = (float)Interpolator.LINEAR.interpolate((double)this.anim, (!notification.getTimer().elapsed(notification.getTime())) ? 1 : 0, 0.1);
                    		
                    		
                    RoundedUtil.drawRound((float) (notifX - 92), (float) (notifY), mc.mntsb.getStringWidth(notification.getContent()) + 50, (notifY + 28) - 3 - notifY + 4, 5, new Color(40,40,40, (int) (255)));
                    
                    
                    
                    String time = " (" + notification.getTimer().getElapsedTime() / 5 + "s)";
                    mc.mntsb_18.drawStringWithShadow(notification.getTitle(), notifX - 70 + 5, notifY + 7, -1);
                    mc.mntsb.drawStringWithShadow(notification.getContent(), notifX - 70 + 5, notifY + 17, new Color(245, 245, 245).getRGB());
                    
                    DrawHelper.drawCircle(notifX - 78, notifY + 14, 0, 360, 7, 3, false, new Color(52, 52, 52,220));
                    DrawHelper.drawCircle(notifX - 78, notifY + 14, 10, 400 - notification.getTimer().getElapsedTime() / 5, 7, 3, false, new Color(255, 255, 255,220));

                    GlStateManager.popMatrix();
                    

                    if (notifications.size() > 1) {
                        y -= 35;
                    }
            	} else if (mode.equalsIgnoreCase("System")) {
            		if (SystemTray.isSupported()) {
                        SystemTray tray = SystemTray.getSystemTray();

                        java.awt.Image image = Toolkit.getDefaultToolkit().getImage("assets/minecraft/icon32.png");
                        TrayIcon trayIcon = new TrayIcon(image);
                        try {
            				tray.add(trayIcon);
            			} catch (AWTException e) {
            				e.printStackTrace();
            			}
                        trayIcon.displayMessage(notification.getTitle(), notification.getContent(),
                                TrayIcon.MessageType.INFO);
                    }
            	}
            }
        }

    }

    public static void queue(String title, String content, NotificationType type) {
        FontRenderer fr = mc.neverlose500_16;
        notifications.add(new Notification(title, content, type, fr));
    }
}