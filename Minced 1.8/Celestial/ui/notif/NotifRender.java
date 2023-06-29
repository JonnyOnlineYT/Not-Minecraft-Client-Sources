package Celestial.ui.notif;

import Celestial.Smertnix;
import Celestial.module.impl.Render.Notifications;
import Celestial.utils.Helper;
import Celestial.utils.render.RenderUtils;
import Celestial.utils.render.RoundedUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import Celestial.ui.celestun4ik.guiscreencomponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class NotifRender implements Helper {
    private static final List<Notif> NOTIFICATIONS = new CopyOnWriteArrayList<>();

    public static void queue(String title, String content, int second, NotifModern type) {
        NOTIFICATIONS.add(new Notif(title, content, type, second * 1000, Minecraft.getMinecraft().mntsb_15));
    }

    public static void publish(ScaledResolution sr) {
        if (Smertnix.instance.featureManager.getFeature(Notifications.class).isEnabled() && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && !mc.gameSettings.showDebugInfo && mc.world != null && !(mc.currentScreen instanceof guiscreencomponent)) {
            if (!NOTIFICATIONS.isEmpty()) {
                int y = sr.getScaledHeight() - 40;
                double better;
                for (Notif notification : NOTIFICATIONS) {
                    better = Minecraft.getMinecraft().neverlose500_18.getStringWidth(notification.getTitle() + " " + notification.getContent());

                    if (!notification.getTimer().hasReached(notification.getTime() / 2)) {
                        notification.notificationTimeBarWidth = 360;
                    } else {
                        notification.notificationTimeBarWidth = MathHelper.EaseOutBack((float) notification.notificationTimeBarWidth, 0, (float) (4 * Smertnix.deltaTime()));
                    }

                    if (!notification.getTimer().hasReached(notification.getTime())) {
                        notification.x = sr.getScaledWidth() - 185;
                        notification.y = MathHelper.EaseOutBack((float) notification.y, (float) y, (float) (5 * Smertnix.deltaTime()));
                    } else {
                        notification.x = sr.getScaledWidth() - 185;
                        NOTIFICATIONS.remove(notification);
                    }
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    RenderUtils.drawBlurredShadow((float) (notification.x + 18 + (110 - mc.mntsb_14.getStringWidth(notification.getContent()))), (float) (notification.y - 16 - 3), 10 + Minecraft.getMinecraft().mntsb_14.getStringWidth(notification.getContent()) - 7 + 47, 15.0f + 13, 25, new Color(0, 0, 0, 173));
                    RoundedUtil.drawRound((float) (notification.x + 18 + (110 - mc.mntsb_14.getStringWidth(notification.getContent()))), (float) (notification.y - 16 - 3), 10 +  Minecraft.getMinecraft().mntsb_14.getStringWidth(notification.getContent()) - 7 + 47, 15.0f + 10 + 3, 4, new Color(25,25,25));
                    RenderUtils.drawImage(new ResourceLocation("celestial/images/notification/" + notification.getType().getTitleString() + ".png"), (float) (notification.x + 21 + (110 - mc.mntsb_14.getStringWidth(notification.getContent()))), (float) (notification.y - 13 - 3), 19 + 3, 19 + 3, new Color(255,255,255, 255));

                    Minecraft.getMinecraft().mntsb_18.drawString(notification.getTitle(), (float) (notification.x + 44 + 2 + (110 - mc.mntsb_14.getStringWidth(notification.getContent()))), (float) (notification.y - 13), new Color(255, 255, 255, 255).getRGB());
                    Minecraft.getMinecraft().mntsb_16.drawString(TextFormatting.WHITE + notification.getContent(), (float) (notification.x +  41 + 3 + (120 - mc.mntsb_15.getStringWidth(notification.getContent()))), (float) (notification.y - 2), new Color(255,255,255, 255).getRGB());
                    GlStateManager.popMatrix();
                    y -= 35;
                }
            }
        }
    }
}
// Где тут анимка, уберу нахуй.