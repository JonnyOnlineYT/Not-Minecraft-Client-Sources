package ru.wendoxd.ui.menu.impl.windows;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.impl.MenuWindow;
import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.ColorShell;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.Animation;
import ru.wendoxd.utils.visual.shaders.ShaderShell;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ColorWindow extends MenuWindow {
    private static ResourceLocation COLOR_PICKER;
    private static BufferedImage IMAGE;
    private static ColorShell copyColor = new ColorShell();
    public static final int OFFSET;
    private final List<ButtonBox> buttons = new ArrayList<>();
    private final CheckBox rainbow;
    private final Animation openAnimation, closeAnimation, settingAnimation, settingOpenedAnimation, copyAnimation,
            pasteAnimation;
    private final CheckBox checkBox;
    private final Bound2D bound, close, setting, copy, paste;
    private int brightness;
    private boolean opened, settingOpened, active;

    public ColorWindow(CheckBox checkBox) {
        this.settingOpenedAnimation = new Animation();
        this.copyAnimation = new Animation();
        this.pasteAnimation = new Animation();
        this.bound = new Bound2D(0, 0, width(), height(), true);
        this.close = new Bound2D(0, 0, 15, 13.5f, true);
        this.copy = new Bound2D(0, 0, 20, 8, true);
        this.paste = new Bound2D(0, 0, 20, 8, true);
        this.setting = new Bound2D(0, 0, 15, 13.5f, true);
        this.rainbow = new CheckBox("Rainbow", this);
        this.checkBox = checkBox;
        this.brightness = 100;
        this.openAnimation = new Animation();
        this.settingAnimation = new Animation();
        this.closeAnimation = new Animation();
        for (int i = 0; i < 5; i++)
            this.buttons.add(new ButtonBox(i));
    }

    @Override
    public void draw() {
        double anim = openAnimation.get();

        if (anim == 0)
            return;

        GL11.glPushMatrix();
        RenderUtils.sizeAnimation(width(), height(), anim * MenuAPI.transparencies.get());
        RenderUtils.drawRect(0, 0, width(), 20, RenderUtils.rgba(25, 25, 25, 255));
        RenderUtils.drawRect(0, 20, width(), height(), RenderUtils.rgba(23, 23, 23, 255));
        if (anim == 1 && MenuAPI.transparencies.get() == 1) {
            Fonts.MNTSB_13.drawString(this.checkBox.getName(), 16, 8.5f, RenderUtils.rgba(180, 180, 180, 255));
            Fonts.ICONS_20.drawString("g", 5, 8.5f, RenderUtils.rgba(180, 180, 180, 255));
            Fonts.ICONS_25.drawString("i", 105, 7.5f, RenderUtils.rgba(180 + (40 * closeAnimation.get()),
                    180 + (40 * closeAnimation.get()), 180 + (40 * closeAnimation.get()), 255));
            Fonts.ICONS_25.drawString("h", 93, 7.6f, RenderUtils.rgba(180 + (40 * settingAnimation.get()),
                    180 + (40 * settingAnimation.get()), 180 + (40 * settingAnimation.get()), 255));
            ShaderShell.CIRCLE_TEXTURE_SHADER.attach();
            ShaderShell.CIRCLE_TEXTURE_SHADER.set1F("radius", 0.5f);
            ShaderShell.CIRCLE_TEXTURE_SHADER.set1F("glow", 0.05f);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            MenuAPI.drawTexturedRect(COLOR_PICKER, 10, 23, 258, 255, 0.39);
            ShaderShell.CIRCLE_TEXTURE_SHADER.detach();
            if (this.active) {
                if (!Mouse.isButtonDown(0))
                    this.active = false;
                int mx = MenuAPI.mouseX - getX() + 7;
                int my = MenuAPI.mouseY - getY() - 7;
                int x = MenuAPI.mouseX - getX() - 10;
                int y = MenuAPI.mouseY - getY() - 20;
                int deltaX = x - 50;
                int deltaY = y - 50;
                double dst = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                if (dst < 50) {
                    int offX = x * OFFSET;
                    int offY = y * OFFSET;
                    int color = IMAGE.getRGB(offX, offY);
                    int red = (int) (((color >> 16) & 255) * brightness / 100.f);
                    int green = (int) (((color >> 8) & 255) * brightness / 100.f);
                    int blue = (int) ((color & 255) * brightness / 100.f);
                    drawCheckeredBackground(mx, my, mx + 9, my + 9);
                    RenderUtils.drawRect(mx - 1, my - 1, mx + 10, my + 10, RenderUtils.rgba(0, 0, 0, 255));
                    RenderUtils.drawRect(mx, my, mx + 9, my + 9, RenderUtils.rgba(red, green, blue, 255));
                    this.checkBox.getColor().setRGB(red, green, blue);
                }
            }
            int color = this.checkBox.getColor().build();
            int offX = 0;
            for (ButtonBox box : this.buttons) {
                box.draw(31 + offX * 17, 125);
                offX++;
            }
            Fonts.MNTSB_12.drawCenteredString("Color : ", 18, 131, RenderUtils.rgba(180, 180, 180, 255));
            drawCheckeredBackground(7, 140, 27, 147);
            RenderUtils.drawRect(7, 140, 27, 147, color);
            if (this.settingOpenedAnimation.get() == 1) {
                this.rainbow.draw(1, 148);
                RenderUtils.drawRect(68, 153, 88, 161, RenderUtils.rgba(25 + 10 * copyAnimation.get(),
                        25 + 10 * copyAnimation.get(), 25 + 10 * copyAnimation.get(), 255));
                RenderUtils.drawRect(68 + 23, 153, 88 + 24, 161, RenderUtils.rgba(25 + 10 * pasteAnimation.get(),
                        25 + 10 * pasteAnimation.get(), 25 + 10 * pasteAnimation.get(), 255));
                this.copy.offset(getX() + 68, getY() + 153);
                this.paste.offset(getX() + 68 + 23, getY() + 153);
                Fonts.MNTSB_12.drawCenteredString("Copy", 77.5f, 156, RenderUtils.rgba(180, 180, 180, 255));
                Fonts.MNTSB_12.drawCenteredString("Paste", 101f, 156, RenderUtils.rgba(180, 180, 180, 255));
            }
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }

    public float getAnimation() {
        return (float) (this.openAnimation.get() * MenuAPI.transparencies.get());
    }

    private void drawCheckeredBackground(float x, float y, float right, float bottom) {
        RenderUtils.drawRect(x, y, right, bottom, -1);

        for (boolean off = false; y < bottom; y++) {
            for (float x1 = x + ((off = !off) ? 1 : 0); x1 < right; x1 += 2) {
                RenderUtils.drawRect(x1, y, x1 + 1, y + 1, 0xFF808080);
            }
        }
    }

    @Override
    public boolean click(int x, int y, int mb) {
        if (!isActive())
            return false;
        if (this.copy.inBound()) {
            ColorWindow.copyColor = new ColorShell()
                    .setRGBA(this.checkBox.getColor().getRed(), this.checkBox.getColor().getGreen(),
                            this.checkBox.getColor().getBlue(), this.checkBox.getColor().getAlpha())
                    .setRainbow(this.checkBox.getColor().isRainbow());
            return true;
        }
        if (this.paste.inBound()) {
            this.checkBox.getColor()
                    .setRGBA(ColorWindow.copyColor.getRed(), ColorWindow.copyColor.getGreen(),
                            ColorWindow.copyColor.getBlue(), ColorWindow.copyColor.getAlpha())
                    .setRainbow(ColorWindow.copyColor.isRainbow());
            this.checkBox.getColor().setRainbow(ColorWindow.copyColor.isRainbow());
            return true;
        }
        for (ButtonBox box : this.buttons) {
            if (box.animation.get() > 0)
                return true;
        }
        if (this.setting.inBound()) {
            this.settingOpened = !this.settingOpened;
            return true;
        }
        if (this.settingOpened) {
            boolean b = this.rainbow.mouseClicked(getX(), getY() + 148, mb);
            this.checkBox.getColor().setRainbow(this.rainbow.isEnabled(true));
            if (b)
                return true;
        }
        int mx = MenuAPI.mouseX - getX() - 10;
        int my = MenuAPI.mouseY - getY() - 20;
        int deltaX = mx - 50;
        int deltaY = my - 50;
        double dst = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (dst < 50) {
            this.active = true;
            return true;
        }
        if (this.close.inBound()) {
            this.update(false);
            return true;
        }
        return false;
    }

    @Override
    public void update(boolean opened) {
        this.opened = opened;
    }

    @Override
    public boolean isActive() {
        return this.opened;
    }

    @Override
    public void onAnimation() {
        this.openAnimation.update(this.opened);
        this.settingOpenedAnimation.update(this.settingOpened);
        if (this.settingOpenedAnimation.get() != 1) {
            this.rainbow.reset();
        }
        this.copyAnimation.update(this.copy.inBound());
        this.pasteAnimation.update(this.paste.inBound());
        this.rainbow.setEnabled(this.checkBox.getColor().isRainbow());
        this.rainbow.animation();
        this.close.offset(getX() + 100, getY() + 4f);
        this.setting.offset(getX() + 86, getY() + 4f);
        this.closeAnimation.update(this.close.inBound());
        this.settingAnimation.update(this.setting.inBound());
        if (!this.checkBox.isEnabled(true)) {
            if (this.opened)
                this.update(false);
        } else {
            if (this.isActive())
                for (ButtonBox box : this.buttons)
                    box.animation();
        }
    }

    @Override
    public Bound2D getBound() {
        return this.bound;
    }

    @Override
    public int width() {
        return 120;
    }

    @Override
    public int height() {
        return 153 + (int) (12 * settingOpenedAnimation.get());
    }

    static {
        try {
            COLOR_PICKER = new ResourceLocation("wexside/colorpicker.png");
            IMAGE = ImageIO
                    .read(Minecraft.getMinecraft().getResourceManager().getResource(COLOR_PICKER).getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        OFFSET = IMAGE.getWidth() / 100;
    }

    // не убирать
    public static void callInitXD() {
        MenuAPI.drawTexturedRect(COLOR_PICKER, 10, 23, 258, 255, 0.39);
    }

    class ButtonBox {
        Animation animation;
        Bound2D bound;
        int id;

        ButtonBox(int id) {
            this.id = id;
            this.bound = new Bound2D(0, 0, 13, 13, true);
            this.animation = new Animation();
        }

        void draw(int x, int y) {
            String let = "";
            int val = 0;
            switch (this.id) {
                case 0:
                    val = ColorWindow.this.checkBox.getColor().getRed();
                    let = "R";
                    break;
                case 1:
                    val = ColorWindow.this.checkBox.getColor().getGreen();
                    let = "G";
                    break;
                case 2:
                    val = ColorWindow.this.checkBox.getColor().getBlue();
                    let = "B";
                    break;
                case 3:
                    val = ColorWindow.this.checkBox.getColor().getAlpha();
                    let = "A";
                    break;
                case 4:
                    val = ColorWindow.this.brightness;
                    let = "B";
                    break;
            }
            RenderUtils.drawRect(x, y, x + 13, y + 13, RenderUtils.rgba(25 + 10 * animation.get(),
                    25 + 10 * animation.get(), 25 + 10 * animation.get(), 255));
            Fonts.SEMI_BOLD_12.drawCenteredString(String.valueOf(val), x + 6.5f, y + 6,
                    RenderUtils.rgba(180, 180, 180, 255));
            Fonts.MNTSB_12.drawCenteredString(let, x + 6.5f, y + 17.5f, RenderUtils.rgba(180, 180, 180, 255));
            this.bound.offset(ColorWindow.this.getX() + x, ColorWindow.this.getY() + y);
            if (this.bound.inBound()) {
                int wheel = Mouse.getDWheel();
                if (wheel == 0)
                    return;
                int mw = wheel > 0 ? 1 : -1;
                switch (this.id) {
                    case 0:
                        ColorWindow.this.checkBox.getColor().changeRed(mw);
                        break;
                    case 1:
                        ColorWindow.this.checkBox.getColor().changeGreen(mw);
                        break;
                    case 2:
                        ColorWindow.this.checkBox.getColor().changeBlue(mw);
                        break;
                    case 3:
                        ColorWindow.this.checkBox.getColor().changeAlpha(mw);
                        break;
                    case 4:
                        ColorWindow.this.brightness = MathHelper.clamp(ColorWindow.this.brightness + mw, 0, 100);
                        break;
                }
            }
        }

        void animation() {
            boolean val = this.bound.inBound();
            this.animation.update(val);
            if (val) {
                int mw = 0;
                if (Mouse.isButtonDown(0))
                    mw = 1;
                else if (Mouse.isButtonDown(1))
                    mw = -1;
                if (mw == 0)
                    return;
                switch (this.id) {
                    case 0:
                        ColorWindow.this.checkBox.getColor().changeRed(mw);
                        break;
                    case 1:
                        ColorWindow.this.checkBox.getColor().changeGreen(mw);
                        break;
                    case 2:
                        ColorWindow.this.checkBox.getColor().changeBlue(mw);
                        break;
                    case 3:
                        ColorWindow.this.checkBox.getColor().changeAlpha(mw);
                        break;
                    case 4:
                        ColorWindow.this.brightness = MathHelper.clamp(ColorWindow.this.brightness + mw, 0, 100);
                        break;
                }
            }
        }
    }
}
