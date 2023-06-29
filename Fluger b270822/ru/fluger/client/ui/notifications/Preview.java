/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package ru.fluger.client.ui.notifications;

import java.awt.Color;
import java.util.Timer;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.Counter;
import ru.fluger.client.Fluger;
import ru.fluger.client.TextureEngine;
import ru.fluger.client.helpers.render.AnimationHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.RoundedUtil;
import ru.fluger.client.ui.notifications.PreviewInfo;
import ru.fluger.client.ui.notifications.PreviewType;

public class Preview {
    protected Minecraft mc = Minecraft.getMinecraft();
    private String information;
    private float width;
    private float height;
    private float x;
    float y;
    private PreviewType type;
    private int seconds;
    private Timer timer;
    public boolean done;
    public boolean removed;
    private UUID uuid;
    public TextureEngine texture = new TextureEngine("nightmare/chat.png", Fluger.scale, 32, 32);
    public float animation = 0.0f;
    Counter counter = new Counter();

    public Preview(PreviewInfo info) {
        this.type = info.type();
        this.information = info.text();
        this.uuid = UUID.randomUUID();
        this.timer = new Timer();
        this.seconds = info.seconds();
        this.x = 0.0f;
        this.y = 0.0f;
        this.width = 65.0f;
        this.height = 20.0f;
        this.x = -this.width;
        this.size(180.0f, 28.0f);
        this.animation = 0.0f;
        this.counter.reset();
    }

    public void render(int y, int posX, int posY) {
        int widthText;
        String time_display;
        int widthDisplay;
        this.x = posX;
        this.y = posY;
        GL11.glPushMatrix();
        float speed = (float)(3.5 * Fluger.deltaTime());
        if (!this.done) {
            this.animation = AnimationHelper.animation(this.animation, 1.0f, speed);
        }
        if (this.done) {
            this.animation = AnimationHelper.animation(this.animation, 0.0f, speed);
            if (this.animation == 0.0f) {
                this.removed = true;
            }
        }
        GL11.glTranslated((double)(this.x + this.width / 2.0f), (double)(this.y + (float)y + this.height / 2.0f), (double)0.0);
        GL11.glScaled((double)this.animation, (double)this.animation, (double)0.0);
        GL11.glTranslated((double)(-(this.x + this.width / 2.0f)), (double)(-(this.y + (float)y + this.height / 2.0f)), (double)0.0);
        RoundedUtil.drawRound(this.x, this.y + (float)y, this.width, this.height, 5.0f, new Color(18, 18, 18, 240));
        RenderHelper.renderBlurredShadow(new Color(18, 18, 18, 240), (double)this.x, (double)(this.y + (float)y), (double)this.getWidth(), this.getHeight(), 20);
        RenderHelper.drawBlurredShadow(this.x + 5.0f, this.y + 4.0f + (float)y, this.texture.getWidth() - 15, this.texture.getHeight() - 15, 40, new Color(0, 0, 0));
        this.mc.smallfontRenderer.drawString(this.information, this.x + (float)(this.texture.getWidth() / 2) + 10.0f, this.y + this.height / 2.0f - (float)(this.mc.smallfontRenderer.getFontHeight() / 2) + (float)y - 4.0f, -1);
        long has = this.seconds * 1000;
        long time = has - this.counter.getValue();
        String disp_time = String.valueOf((double)time / 1000.0);
        String a = String.valueOf(disp_time.charAt(0)) + "." + disp_time.charAt(2);
        if (this.seconds == 1337) {
            a = "1337";
        }
        if ((float)(widthDisplay = this.mc.smallfontRenderer.getStringWidth(time_display = "\u0418\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044f \u0438\u0441\u0447\u0435\u0437\u043d\u0435\u0442 \u0447\u0435\u0440\u0435\u0437: \u00a7f" + a + "\u00a7r \u0441\u0435\u043a") + 30) > this.width) {
            this.width = widthDisplay;
        }
        if ((float)(widthText = this.mc.smallfontRenderer.getStringWidth(this.information) + 30) > this.width) {
            this.width = widthText;
        }
        this.mc.smallfontRenderer.drawString(time_display, this.x + (float)(this.texture.getWidth() / 2) + 10.0f, this.y + this.height / 2.0f - (float)(this.mc.smallfontRenderer.getFontHeight() / 2) + (float)y + 4.0f, new Color(40, 40, 40, 255).getRGB());
        this.texture.bind((int)this.x, (int)this.y + (int)this.height / 2 - this.texture.getHeight() / 4 - 1 + y);
        GL11.glPopMatrix();
    }

    public String getInfo() {
        return String.join((CharSequence)" ", this.information);
    }

    public int getSeconds() {
        return this.seconds;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public PreviewType getType() {
        return this.type;
    }

    public void size(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setType(PreviewType type) {
        this.type = type;
    }

    public Timer getTimer() {
        return this.timer;
    }
}

