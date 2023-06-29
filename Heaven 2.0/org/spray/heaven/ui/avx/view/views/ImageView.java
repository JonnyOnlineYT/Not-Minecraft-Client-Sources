package org.spray.heaven.ui.avx.view.views;

import java.awt.Color;

import org.spray.heaven.ui.avx.listeners.OnClickListenerM;
import org.spray.heaven.ui.avx.view.View;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;

import net.minecraft.util.ResourceLocation;

public class ImageView extends View {

	private final Animation animation = new DecelerateAnimation(200, 1, Direction.BACKWARDS);
	private OnClickListenerM clickListener;
	private ResourceLocation resource;
	private Color color = Color.WHITE;
	private Color hoverColor = Color.GRAY;

	public ImageView(ResourceLocation resource) {
		this.resource = resource;
	}

	@Override
	protected void renderView(int mouseX, int mouseY, float delta) {
		animation.setDirection(isHovered(mouseX, mouseY) ? Direction.FORWARDS : Direction.BACKWARDS);
		float[] fractions = { 0.0F, 1.0F };
		Color[] colors = { color, hoverColor };
		Color blend = ColorUtil.blendColors(fractions, colors, (float) animation.getOutput());
		Drawable.drawTexture(resource, x + padding, y + padding, width - (padding * 2), height - (padding * 2), blend);
	}

	@Override
	protected boolean viewClicked(int mouseX, int mouseY, int button) {
		clickListener.onMouseClick(mouseX, mouseY, button);
		return true;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setHoverColor(Color color) {
		this.hoverColor = color;
	}
	
	public void setOnClickListener(OnClickListenerM listener) {
		clickListener = listener;
	}

}
