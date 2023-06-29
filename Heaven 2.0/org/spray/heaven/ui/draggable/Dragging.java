package org.spray.heaven.ui.draggable;

import java.awt.Color;

import org.spray.heaven.features.module.Module;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.RenderUtil;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.minecraft.util.ResourceLocation;

public class Dragging {

	@Expose
	@SerializedName("x")
	private float xPos;
	@Expose
	@SerializedName("y")
	private float yPos;

	public float initialXVal;
	public float initialYVal;

	private float startX, startY;
	private boolean dragging;

	private float width, height;

	@Expose
	@SerializedName("name")
	private String name;

	private final Module module;

	public Animation hoverAnimation = new DecelerateAnimation(250, 1, Direction.BACKWARDS);

	public Dragging(Module module, String name, float initialXVal, float initialYVal) {
		this.module = module;
		this.name = name;
		this.xPos = initialXVal;
		this.yPos = initialYVal;
		this.initialXVal = initialXVal;
		this.initialYVal = initialYVal;
	}

	public Module getModule() {
		return module;
	}

	public String getName() {
		return name;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getX() {
		return xPos;
	}

	public void setX(float x) {
		this.xPos = x;
	}

	public float getY() {
		return yPos;
	}

	public void setY(float y) {
		this.yPos = y;
	}

	public void onDraw(int mouseX, int mouseY) {
		boolean hovering = Drawable.isHovered(mouseX, mouseY, xPos, yPos, width, height);
		if (dragging) {
			xPos = RenderUtil.scrollAnimate(xPos, (mouseX - startX), .7f);
			yPos = RenderUtil.scrollAnimate(yPos, (mouseY - startY), .7f);
		}
		hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
		RoundedShader.drawRoundOutline(xPos - 2, yPos - 2, width + 4, height + 4, 5, 0.5f,
				ColorUtil.applyOpacity(Color.BLACK, 0.6f),
				ColorUtil.applyOpacity(Color.WHITE, (float) hoverAnimation.getOutput()));

		Drawable.drawTexture(new ResourceLocation("heaven/textures/icons/mouse.png"),
				(xPos + width / 2) - (14 / 2), (yPos + height / 2) - (14 / 2), 14, 14);
	}

	public void onClick(int mouseX, int mouseY, int button) {
		boolean canDrag = Drawable.isHovered(mouseX, mouseY, xPos, yPos, width, height);
		if (button == 0 && canDrag) {
			dragging = true;
			startX = (int) (mouseX - xPos);
			startY = (int) (mouseY - yPos);
		}
	}

	public void onRelease(int button) {
		if (button == 0)
			dragging = false;
	}
}
