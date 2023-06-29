//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ua.apraxia.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import ua.apraxia.utility.font.FontRenderer;
import ua.apraxia.utility.math.ScreenUtility;
import ua.apraxia.utility.math.TimerUtility;

public class Notification {
    public final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public static final int HEIGHT = 30;
    private final String title;
    private final String content;
    private final int time;
    private final NotificationMode type;
    private final TimerUtility timer;
    private final FontRenderer fontRenderer;
    public double x;
    public double y;
    public double notificationTimeBarWidth;
    private final ScreenUtility screenHelper;

    public Notification(String title, String content, NotificationMode type, int second, FontRenderer fontRenderer) {
        this.x = (double)this.sr.getScaledWidth();
        this.y = (double)this.sr.getScaledHeight();
        this.title = title;
        this.content = content;
        this.time = second;
        this.type = type;
        this.timer = new TimerUtility();
        this.fontRenderer = fontRenderer;
        this.screenHelper = new ScreenUtility((float)(this.sr.getScaledWidth() - this.getWidth() + this.getWidth()), (float)(this.sr.getScaledHeight() - 60));
    }

    public final int getWidth() {
        return Math.max(100, Math.max(this.fontRenderer.getStringWidth(this.title), this.fontRenderer.getStringWidth(this.content)) + 90);
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getContent() {
        return this.content;
    }

    public final int getTime() {
        return this.time;
    }

    public final int getY() {
        return (int)this.y;
    }

    public final NotificationMode getType() {
        return this.type;
    }

    public final TimerUtility getTimer() {
        return this.timer;
    }

    public ScreenUtility getTranslate() {
        return this.screenHelper;
    }
}
