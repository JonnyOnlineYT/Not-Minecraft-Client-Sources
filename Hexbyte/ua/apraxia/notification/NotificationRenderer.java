//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ua.apraxia.notification;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import ua.apraxia.Hexbyte;
import ua.apraxia.utility.Utility;
import ua.apraxia.utility.font.Fonts;
import ua.apraxia.utility.render.RoundedUtility;

public final class NotificationRenderer implements Utility {
    private static final List<Notification> NOTIFICATIONS = new CopyOnWriteArrayList();

    public NotificationRenderer() {
    }

    public static void queue(String title, String content, int second, NotificationMode type) {
        NOTIFICATIONS.add(new Notification(title, content, type, second * 1000, Fonts.sfbolt16));
    }

    public static void publish(ScaledResolution sr) {
        Minecraft var10000 = mc;
        if (!Minecraft.gameSettings.showDebugInfo && mc.world != null && !NOTIFICATIONS.isEmpty()) {
            int y = sr.getScaledHeight() - 40;

            for(Iterator var4 = NOTIFICATIONS.iterator(); var4.hasNext(); y -= 35) {
                Notification notification = (Notification)var4.next();
                double better = (double)Fonts.sfbolt16.getStringWidth(notification.getTitle() + " " + notification.getContent());
                if (!notification.getTimer().hasReached((double)(notification.getTime() / 2))) {
                    notification.notificationTimeBarWidth = 360.0;
                } else {
                    notification.notificationTimeBarWidth = (double)MathHelper.EaseOutBack((float)notification.notificationTimeBarWidth, 0.0F, (float)(4.0 * Hexbyte.deltaTime()));
                }

                if (!notification.getTimer().hasReached((double)notification.getTime())) {
                    notification.x = (double)MathHelper.EaseOutBack((float)notification.x, (float)((double)notification.sr.getScaledWidth() - better), (float)(5.0 * Hexbyte.deltaTime()));
                    notification.y = (double)MathHelper.EaseOutBack((float)notification.y, (float)y, (float)(5.0 * Hexbyte.deltaTime()));
                } else {
                    notification.x = (double)MathHelper.EaseOutBack((float)notification.x, (float)(notification.sr.getScaledWidth() + 50), (float)(5.0 * Hexbyte.deltaTime()));
                    notification.y = (double)MathHelper.EaseOutBack((float)notification.y, (float)y, (float)(5.0 * Hexbyte.deltaTime()));
                    if (notification.x > (double)(notification.sr.getScaledWidth() + 24)) {
                        var10000 = mc;
                        if (Minecraft.player != null && mc.world != null) {
                            var10000 = mc;
                            if (!Minecraft.gameSettings.showDebugInfo) {
                                NOTIFICATIONS.remove(notification);
                            }
                        }
                    }
                }

                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                RoundedUtility.drawRound((float)notification.x, (float)(notification.y - 10.0), (float)sr.getScaledWidth(), 27.0F, 3.0F, new Color(9, 9, 9));
                Fonts.icons30.drawString(notification.getType().getIconString(), (float)(notification.x + 5.0), (float)(notification.y - 1.0), (new Color(255, 255, 255)).getRGB());
                Fonts.sfsemib14.drawString(notification.getTitle(), (float)(notification.x + 25.0), (float)(notification.y - 4.0), -1);
                Fonts.medium14.drawString(notification.getContent(), (float)(notification.x + 25.0), (float)(notification.y + 8.0), -1);
                GlStateManager.popMatrix();
            }
        }

    }
}
