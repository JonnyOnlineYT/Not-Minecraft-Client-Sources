package org.spray.heaven.ui.avx.view.views;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.avx.view.ViewAdapter;
import org.spray.heaven.ui.avx.view.ViewHolder;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.Timer;

public class ScrollableListView extends ListView {

	public static int START = 0, END = 1;

	private Timer scrollTimer = new Timer();

	private int scrollBarSide = END;

	private float scrollSpeed;
	private float prevScrollProgress;
	private float scrollProgress;

	private boolean mouseScroll;

	public ScrollableListView(ViewAdapter adapter, ListMode mode) {
		super(adapter, mode);
	}

	@Override
	protected void viewTick() {
		if (getHoldersHeight() < height) {
			setScrollProgress(0);
		} else {
			int difference = getHeightDifference();

			setScrollProgress(scrollProgress + scrollSpeed);
			scrollSpeed *= 0.49;

			if (scrollProgress < 0)
				scrollSpeed = scrollProgress * -0.39f;
			else if (scrollProgress > difference)
				scrollSpeed = (scrollProgress - difference) * -0.39f;
		}
	}

	@Override
	protected void renderView(int mouseX, int mouseY, float delta) {
		if (getHoldersHeight() > height && isHovered(mouseX, mouseY)) {

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

		int offsetX = 0, offsetY = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			ViewHolder holder = holders.get(i);

			holder.setPoints(x, y - getScrollProgress(), width, 20);

			if (mode == ListMode.LIST) {
				holder.setX(holder.getPointX());
				holder.setY(holder.getPointY() + (holder.getHeight() + offset) * i);
			} else if (mode == ListMode.GRID) {
				holder.setWidth((width / mode.columns) - (offset / 2f));
				holder.setX(holder.getPointX() + (offsetX * holder.getWidth() + (offsetX != 0 ? (offset / 2f) : 0)));
				holder.setY(holder.getPointY() + offsetY);
			}

			adapter.render(holder, mouseX, mouseY, delta, i);

			offsetX++;
			if (offsetX >= mode.columns) {
				offsetX = 0;
				offsetY += (holder.getHeight() + offset);
			}
		}
	}

	public int getHeightDifference() {
		return (int) (getHoldersHeight() - height - padding);
	}

	protected float getScrollProgress() {
		return prevScrollProgress + (scrollProgress - prevScrollProgress) * Wrapper.MC.getRenderPartialTicks();
	}

	protected void setScrollProgress(float value) {
		prevScrollProgress = scrollProgress;
		scrollProgress = value;
	}

	public void setScrollBarSide(int side) {
		this.scrollBarSide = side;
	}

	public int getHoldersHeight() {
		int height = 0, offsetX = 0;
		int lines = 0, prevLines = 0;
		for (ViewHolder holder : holders) {

			if (mode == ListMode.GRID) {
				offsetX++;
				prevLines = lines;
				if (offsetX == mode.columns - 1) {
					lines++;
				}
				if (offsetX >= mode.columns) {
					offsetX = 0;
				}

				if (lines != prevLines) {
					height += holder.getHeight() + offset;
				}
			} else {
				height += holder.getHeight() + offset;
			}
		}

		return height;
	}
}
