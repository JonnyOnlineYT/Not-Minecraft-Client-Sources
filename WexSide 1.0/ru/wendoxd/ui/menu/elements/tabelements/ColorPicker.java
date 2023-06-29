package ru.wendoxd.ui.menu.elements.tabelements;

import ru.wendoxd.ui.menu.impl.MenuWindow;
import ru.wendoxd.ui.menu.impl.windows.ColorWindow;
import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.Animation;

import java.util.function.Supplier;

public class ColorPicker extends CheckBox {
    private final Animation animation;
    private final Animation animationOutline;
    private final Animation colorOutline;
    private final Bound2D bound;
    private final Bound2D colorBound;
    private final ColorWindow colorWindow;

    public ColorPicker(String name) {
        this(name, null);
    }

    public ColorPicker(String name, Supplier<Boolean> condition) {
        super(name, condition);
        this.colorWindow = new ColorWindow(this);
        MenuAPI.windows.add(this.colorWindow);
        this.bound = new Bound2D(0, 0, 0, 0);
        this.colorBound = new Bound2D(0, 0, 0, 0);
        this.animation = new Animation();
        this.animationOutline = new Animation();
        this.colorOutline = new Animation();
    }

    @Override
    public float draw(float x, float yOffset) {
        float anim = (float) animation.get();
        float outline = (float) animationOutline.get();
        RenderUtils.drawRect(x, yOffset, x + 110, yOffset + 16 * anim, RenderUtils.rgba(23, 23, 23, 255));
        if (animation.get() > 0) {
            Fonts.ICONS_20.drawString("g", x + 96.5f, yOffset + 7f * anim,
                    RenderUtils.rgba(170 + 45 * colorOutline.get(), 170 + 45 * colorOutline.get(),
                            170 + 45 * colorOutline.get(), 255 * anim));
            this.colorBound.update(x + 94, yOffset + 2.5f, x + 106, yOffset + 17);
            Fonts.MNTSB_13.drawString(name, x + 7, yOffset + 7f * anim,
                    RenderUtils.rgba(170 + (45 * outline), 170 + (45 * outline), 170 + (45 * outline), (255 * anim)));
            this.bound.update(x, yOffset, x + 110, yOffset + 17 * anim);
        }
        return 16 * anim;
    }

    @Override
    public boolean isEnabled(boolean menu) {
        return this.isVisible();
    }

    @Override
    public void animation() {
        this.colorOutline.update(this.colorBound.inBound());
        this.animation.update(this.isVisible());
        this.animationOutline.update(this.bound.inBound() && MenuAPI.contextTab.isAnimationsAllowed());
    }

    @Override
    public void set(float animation) {
        this.animation.set(animation);
    }

    @Override
    public boolean mouseClicked(int x, int y, int mb) {
        if (colorBound.inBound() && isVisible()) {
            boolean prev = this.colorWindow.isActive();
            for (MenuWindow window : MenuAPI.windows) {
                if (window instanceof ColorWindow) {
                    window.update(false);
                }
            }
            this.colorWindow.update(!prev);
            if (this.colorWindow.isActive()) {
                this.colorWindow.setXY(MenuAPI.menuWindow.getX() + MenuAPI.bound.getMaxX() + 5, y);
            }
            return true;
        }
        return this.bound.inBound();
    }

    @Override
    public void reset() {
        this.colorOutline.reset();
        this.animationOutline.reset();
    }
}
