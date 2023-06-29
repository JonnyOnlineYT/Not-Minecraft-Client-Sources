package org.spray.heaven.ui.changelog;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Heaven;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.Timer;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import com.mojang.realmsclient.gui.ChatFormatting;

public class ChangeLog {

	private final Animation animation = new DecelerateAnimation(250, 0.4);
	private float x, y, width, height;
	private float offset;

	private double scrollSpeed;
	private double prevScrollProgress;
	private double scrollProgress;
	private boolean scrollHover; // scroll hover

	private Timer scrollTimer = new Timer();
	private List<Element> elements = new ArrayList<>();

	public ChangeLog(float x, float y, float width, float height, float offset) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.offset = offset;

		for (String line : Heaven.CHANGELOG) {
			elements.add(new Element(line));
		}
	}

	public void tick() {
		if (getElementsHeight() < height) {
			setScrollProgress(0);
			return;
		}

		int difference = getHeightDifference();

		setScrollProgress(scrollProgress + scrollSpeed);
		scrollSpeed *= 0.49;

		if (scrollTimer.hasReached(50)) {
			if (scrollProgress < 0)
				scrollSpeed = scrollProgress * -0.39;
			else if (scrollProgress > difference)
				scrollSpeed = (scrollProgress - difference) * -0.39;
		}
	}

	public void render(int mouseX, int mouseY) {
		double elementsHeight = getElementsHeight();
		scrollHover = Drawable.isHovered(mouseX, mouseY, x, y, width, elementsHeight);

		if (elementsHeight > height && scrollHover) {
			double amount = Mouse.getDWheel();
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
				amount = -0.1;
			else if (Keyboard.isKeyDown(Keyboard.KEY_UP))
				amount = 0.1;

			amount = MathUtil.clamp((float) amount, -6F, 6F);

			if (amount != 0) {
				double sa = amount < 0 ? amount - 10 : amount + 10;
				scrollSpeed -= sa;
				scrollTimer.reset();
			}
		}

		RoundedShader.drawRound(x, y, width, height + 6, 3, true, new Color(14, 14, 14, 90));

		Drawable.startScissor(x, y + 4, width, height - 2);

		int offsetY = 0;
		for (Element element : elements) {
			element.x = x + 2;
			element.y = (float) ((y + 4 + offsetY) - getScrollProgress());

			element.render();

			offsetY += offset;
		}

		double hy = y + 6;
		double hh = height - 4;
		int k1 = (int) ((hh - hy) * (hh - hy) / getElementsHeight());
		int l1 = (int) (getScrollProgress() * (hh - k1) / getHeightDifference() + hy);

		animation.setDirection((int) scrollSpeed != 0 ? Direction.FORWARDS : Direction.BACKWARDS);

		RoundedShader.drawRound((float) x + width - 2f, (float) l1, 1f, k1, 1f,
				ColorUtil.applyOpacity(Color.WHITE, (float) (0.3 + animation.getOutput())));

		Drawable.stopScissor();
	}

	private double getElementsHeight() {
		double height = 3;
		for (Element element : elements) {
			height += offset;
		}

		return height;
	}

	private int getHeightDifference() {
		double diffHeight = height;
		return (int) (getElementsHeight() - diffHeight);
	}

	private double getScrollProgress() {
		return prevScrollProgress + (scrollProgress - prevScrollProgress) * Wrapper.MC.getRenderPartialTicks();
	}

	private void setScrollProgress(double value) {
		prevScrollProgress = scrollProgress;
		scrollProgress = value;
	}

	public static class Element {

		private float x, y;
		private String text;

		public Element(String text) {
			this.text = text;
		}

		public void render() {
			String formatted = text;
			String prefix = text.substring(0, 3);
			String stext = text.substring(3, text.length());
			switch (prefix) {
			case "[+]":
				formatted = ChatFormatting.GREEN + prefix + ChatFormatting.RESET + stext;
				break;
			case "[-]":
				formatted = ChatFormatting.RED + prefix + ChatFormatting.RESET + stext;
				break;
			case "[/]":
				formatted = ChatFormatting.LIGHT_PURPLE + prefix + ChatFormatting.RESET + stext;
				break;
			case " - ":
				formatted = ChatFormatting.GRAY + prefix + ChatFormatting.RESET + stext;
				break;
			default:
				formatted = text;
				break;
			}
			IFont.WEB_LIST.drawStringWithShadow(formatted, x, y, 0xFFE2E2E2);
		}

	}

}
