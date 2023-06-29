/*
 * Decompiled with CFR 0.150.
 */
package Celestial.ui.notif;

import Celestial.utils.math.TimerHelper;
import Celestial.ui.font.MCFontRenderer;
import Celestial.ui.celestun4ik.screenutil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notif {
    public final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public static final int HEIGHT = 30;
    private final String title;
    private final String content;
    private final int time;
    private final NotifModern type;
    private final TimerHelper timer;
    private final MCFontRenderer fontRenderer;
    public double x = this.sr.getScaledWidth();
    public double y = this.sr.getScaledHeight();
    public double notificationTimeBarWidth;
    private final screenutil screenHelper;

    public Notif(String title, String content, NotifModern type, int second, MCFontRenderer fontRenderer) {
        this.title = title;
        this.content = content;
        this.time = second;
        this.type = type;
        this.timer = new TimerHelper();
        this.fontRenderer = fontRenderer;
        this.screenHelper = new screenutil(this.sr.getScaledWidth() - this.getWidth() + this.getWidth(), this.sr.getScaledHeight() - 60);
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

    public final NotifModern getType() {
        return this.type;
    }

    public final TimerHelper getTimer() {
        return this.timer;
    }

    public screenutil getTranslate() {
        return this.screenHelper;
    }
}

