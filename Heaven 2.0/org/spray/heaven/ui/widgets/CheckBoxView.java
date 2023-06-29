package org.spray.heaven.ui.widgets;

import java.awt.Color;

import org.spray.heaven.font.IFont;
import org.spray.heaven.ui.avx.view.View;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import net.minecraft.client.renderer.GlStateManager;

public class CheckBoxView extends View {

	private String text;
	private final Animation animation = new DecelerateAnimation(240, 1);
	private boolean state;

	public CheckBoxView(String text) {
		this.text = text;
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		animation.setDirection(state ? Direction.FORWARDS : Direction.BACKWARDS);

		RoundedShader.drawRound(x + 1, y + 2, height - 4, height - 4, 2, new Color(26, 26, 26, 220));
		if (!animation.finished(Direction.BACKWARDS)) {
			float centerX = (x + 3) + (height - 8) / 2;
			float centerY = (y + 4) + (height - 8) / 2;
			GlStateManager.pushMatrix();
			GlStateManager.translate(centerX, centerY, 1);
			GlStateManager.scale(animation.getOutput(), animation.getOutput(), 1);
			GlStateManager.translate(-centerX, -centerY, 1);
			RoundedShader.drawRound(x + 3, y + 4, height - 8, height - 8, 1, Colors.CLIENT_COLOR);
			GlStateManager.popMatrix();
		}

		IFont.WEB_LIST.drawVCenteredString(text, x + height + 2, y, height, -1);
	}
	
	@Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (Drawable.isHovered(mouseX, mouseY, x, y, width, height)) {
			onClickListener.onMouseClick(button);
			return true;
		}
		return false;
	}

	public CheckBoxView setState(boolean state) {
		this.state = state;
		return this;
	}

}
