package org.spray.heaven.ui.clickui.elements;

import org.spray.heaven.features.module.Setting;
import org.spray.heaven.features.module.modules.render.ClickUIMod;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.ui.clickui.base.AbstractElement;
import org.spray.heaven.ui.clickui.utils.RippleEffect;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import java.awt.*;

public class CheckBoxElement extends AbstractElement {

    private final RippleEffect rippleEffect = new RippleEffect();
    private final Animation animation;

    public CheckBoxElement(Setting setting) {
        super(setting);
        animation = new DecelerateAnimation(200, 1F, setting.isToggle() ? Direction.FORWARDS : Direction.BACKWARDS);
    }

    @Override
    public void init() {
        animation.setDirection(Direction.BACKWARDS);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
        animation.setDirection(setting.isToggle() ? Direction.FORWARDS : Direction.BACKWARDS);
        double paddingX = 7 * animation.getOutput();
        
		ClickUIMod clickUI = Wrapper.getModule().get("ClickGUI");

    	Color color = ClickUIMod.getColor(0);

//        Drawable.drawRectWH(x, y, width, height, bgcolor);

        rippleEffect.render((float) (x + width - 19f / 2), (float) (y + height / 2), 10, Colors.RIPPLE_COLOR.getRGB());

//        Drawable.drawBlurredShadow((int) (x + width - 18), (int) (y + height / 2 - 4), 15, 8, 6,
//                paddingX > 4 ? color
//                        : new Color(0xFFB2B1B1));

        RoundedShader.drawRound((float) (x + width - 18), (float) (y + height / 2 - 4), 15, 8, 4,
                paddingX > 4 ? color
                        : new Color(0xFFB2B1B1));

        RoundedShader.drawRound((float) (x + width - 17 + paddingX), (float) (y + height / 2 - 3), 6, 6, 3, true,
                new Color(-1));

        IFont.WEB_SETTINGS.drawString(setting.getName(), x + 3,
                y + height / 2 - (IFont.WEB_SETTINGS.getFontHeight() / 2), clickUI.textShadow.isToggle(), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovered && button == 0) {
            rippleEffect.start(0, 0, 200);

            setting.setToggle(!setting.isToggle());

            animation.setDirection(setting.isToggle() ? Direction.FORWARDS : Direction.BACKWARDS);
        }
    }

    @Override
    public void resetAnimation() {
        animation.setDirection(Direction.BACKWARDS);
    }

}
