package org.spray.heaven.notifications;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.spray.heaven.font.FontRenderer;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.Timer;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.RenderUtil;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import java.awt.*;

public class Notification {

    private ResourceLocation image;
    private final FontRenderer font;
    private final String message;
    private final String l;

    private final Timer timer;

    private final Type type;

    private double lastY, posY, width, height, animationX;
    private int color;
    private final int imageWidth;
    private final long stayTime;

    private boolean finished;

    private final Animation animation = new DecelerateAnimation(380, 1, Direction.BACKWARDS);
    private final Animation animationY = new DecelerateAnimation(380, 1);

    public Notification(String message, Type type, FontRenderer font) {
        image =
                new ResourceLocation("heaven/textures/icons/notifications/" + type.name.toLowerCase() + ".png");
        this.message = message;
        this.font = font;
        this.type = type;
        timer = new Timer();
        timer.reset();

        color = -1;
        switch (type) {
            case SUCCESS:
            case ENABLED:
                color = 0xFF7FFA84;
                l = "a";
                break;
            case ERROR:
            case DISABLED:
                color = 0xFFFD6D66;
                l = "c";
                break;
            default:
                l = "b";
                break;
        }

        ScaledResolution sr = new ScaledResolution(Wrapper.MC);
        width = IFont.WEB_SMALL.getStringWidth(message) + 34;
        animationX = width;
        stayTime = 3400;
        imageWidth = 9;
        height = 33;
        posY = sr.getScaledHeight() - height;
    }

    public void render(double getY, double lastY) {
        Color scolor = new Color(0xFF171717);
        Color icolor = new Color(scolor.getRed(), scolor.getGreen(), scolor.getBlue(), 170);
        this.lastY = lastY;
        ScaledResolution resolution = new ScaledResolution(Wrapper.MC);

        animationY.setDirection(finished ? Direction.BACKWARDS : Direction.FORWARDS);
        animation.setDirection(isFinished() || finished ? Direction.FORWARDS : Direction.BACKWARDS);
        animationX = width * animation.getOutput();
        posY = RenderUtil.animate(posY, getY);

        int x1 = (int) ((resolution.getScaledWidth() - 6) - width + animationX), y1 = (int) posY;

        RoundedShader.drawRound((float) x1, y1, (float) width, (float) height, 2, icolor);

        Drawable.drawTexture(image, x1 + 9, y1 + 3 + (height - 9) / 2, 9, 9);

        IFont.WEB_SMALL.drawString(type.getName(), (x1 + 6), y1 + 2, -1, 0.8f);
        IFont.WEB_SMALL.drawString(message, (float) (x1 + imageWidth + 16),
                (float) y1 + 4 + (height - font.getFontHeight()) / 2, -1,
                0.93f);
        Drawable.stopScissor();
    }

    public boolean shouldDelete() {
        return (isFinished() || finished) && animationX >= width - 5;
    }

    private boolean isFinished() {
        return timer.hasReached(stayTime);
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public double getHeight() {
        return height;
    }

    public enum Type {
        SUCCESS("Success"),
        INFO("Information"),
        WARNING("Warning"),
        ERROR("Error"),
        ENABLED("Module toggled"),
        DISABLED("Module toggled");

        final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
