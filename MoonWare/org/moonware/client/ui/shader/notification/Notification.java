package org.moonware.client.ui.shader.notification;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.ScaledResolution;
import org.moonware.client.feature.impl.hud.Notifications;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.render.ScreenHelper;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;

public class Notification
        implements Helper {
    private final ScreenHelper screenHelper;
    private final Font font;
    private final String title;
    private final String content;
    private final int time;
    private final NotificationType type;
    private final TimerHelper timer;
    private double scale;
    private double scaleA;


    public Notification(String title, String content, NotificationType type, int second, Font font) {
        ScreenHelper screenHelper1 = new ScreenHelper(9,-20);
        scale = 0;
        this.title = title;
        this.content = content;
        time = second;
        this.type = type;
        timer = new TimerHelper();
        this.font = font;
        ScaledResolution sr = new ScaledResolution(Helper.mc);
        //this.screenHelper = new ScreenHelper(sr.getScaledWidth() + 100, sr.getScaledHeight() - 60);
        //this.screenHelper = new ScreenHelper(sr.getScaledWidth() - 25, sr.getScaledHeight() + 20);
        switch (Notifications.startposition.currentMode) {
            case "Right-down":
                screenHelper1 = new ScreenHelper((float) (sr.getScaledWidth() - getWidth() - 9), sr.getScaledHeight() + 20);
            case "Left-down":
                screenHelper1 = new ScreenHelper(9, sr.getScaledHeight() + 20);
            case "Left-up":
                screenHelper1 = new ScreenHelper(9,  -20);
        }
        if (Notifications.startposition.currentMode.equalsIgnoreCase("Right-down")) {
            screenHelper =  new ScreenHelper((float) (sr.getScaledWidth() + getWidth()), sr.getScaledHeight() - 10);
        }else if (Notifications.startposition.currentMode.equalsIgnoreCase("Left-down")) {
            screenHelper  = new ScreenHelper(9, sr.getScaledHeight() + 20);
        }else {
            screenHelper = new ScreenHelper(9,  -20);
        }
    }

    public int getWidth() {
        return MWFont.MONTSERRAT_REGULAR.get(18).getWidth(content) + 85;
    }
    public void setScale (double s) {
        this.scale = s;
    }
    public double getScale() {
        this.scaleA = Interpolator.linear(this.scaleA, this.scale,2f/20);
        return this.scaleA;
    }
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getTime() {
        return time;
    }

    public NotificationType getType() {
        return type;
    }

    public TimerHelper getTimer() {
        return timer;
    }

    public ScreenHelper getTranslate() {
        return screenHelper;
    }
}
