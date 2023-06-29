package org.spray.heaven.ui.clickui.utils;

import com.google.common.collect.Lists;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;

import java.util.List;

public class RippleEffect {

    private final List<Ripple> rippleList = Lists.newArrayList();

    public void render(float maxRadius, int color) {
        rippleList.forEach(ripple -> {
            ripple.render(maxRadius, color);
        });

        rippleList.removeIf(Ripple::isDone);
    }

    public void render(float x, float y, float maxRadius, int color) {
        rippleList.forEach(ripple -> {
            ripple.render(x, y, maxRadius, color);
        });

        rippleList.removeIf(Ripple::isDone);
    }

    public void start(float x, float y, int ms) {
        rippleList.add(new Ripple(x, y, ms));
    }

    public void start(float x, float y) {
        start(x, y, 440);
    }

    public void clear() {
        rippleList.clear();
    }

    private static class Ripple {

        private final Animation animation;
        private final float x;
        private final float y;

        public Ripple(float x, float y, int ms) {
            this.x = x;
            this.y = y;
            animation = new DecelerateAnimation(ms, 1f);
        }

        public void render(float maxRadius, int color) {
            render(x, y, maxRadius, color);
        }

        public void render(float x, float y, float maxRadius, int color) {
            Drawable.drawCircle(x, y, (float) (maxRadius * animation.getOutput()), color);
        }

        public boolean isDone() {
            return animation.isDone() && animation.getDirection() == Direction.FORWARDS;
        }
    }

}
