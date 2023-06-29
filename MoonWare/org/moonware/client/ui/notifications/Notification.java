package org.moonware.client.ui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.moonware.client.feature.impl.hud.Notifications;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.render.ScreenHelper;
import org.moonware.client.ui.notifications.Animations.AHL;
import org.moonware.client.ui.notifications.impl.Type;

public class Notification
        implements Helper {
    private final AHL screenHelper;
    private final String title;
    private final String content;
    private final int time;
    private final Type type;
    private final TimerHelper timer;

    public Notification(String title, String content, Type type, int second, float xSpeed,float ySpeed) {
        ScreenHelper screenHelper1 = new ScreenHelper(9,-20);
        this.title = title;
        this.content = content;
        time = second;
        this.type = type;
        timer = new TimerHelper();
        ScaledResolution sr = new ScaledResolution(Helper.mc);
        switch (Notifications.startposition.currentMode) {
            case "Right-down":
                screenHelper1 = new ScreenHelper((float) (sr.getScaledWidth() - getWidth() - 9), sr.getScaledHeight() + 20);
            case "Left-down":
                screenHelper1 = new ScreenHelper(9, sr.getScaledHeight() + 20);
            case "Left-up":
                screenHelper1 = new ScreenHelper(9,  -20);
        }
        this.screenHelper =  new AHL((sr.getScaledWidth() - getWidth() - 9), sr.getScaledHeight() + 20,xSpeed,ySpeed);
       /* if (Notifications.startposition.currentMode.equalsIgnoreCase("Right-down")) {
            screenHelper =  new ScreenHelper((float) (sr.getScaledWidth() - getWidth() - 9), sr.getScaledHeight() + 20);
        }else if (Notifications.startposition.currentMode.equalsIgnoreCase("Left-down")) {
            screenHelper  = new ScreenHelper(9, sr.getScaledHeight() + 20);
        }else {
            screenHelper = new ScreenHelper(9,  -20);
        }*/
    }

    public int getWidth() {
        return Minecraft.font.getStringWidth(content) + (Notifications.timePeriod.getCurrentValue() ? 70 : 90);
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
    public int getX() {
        return time;
    }
    public int getY() {
        return time;
    }
    public Type getType() {
        return type;
    }

    public TimerHelper getTimer() {
        return timer;
    }

    public AHL getTranslate() {
        return this.screenHelper;
    }
}

