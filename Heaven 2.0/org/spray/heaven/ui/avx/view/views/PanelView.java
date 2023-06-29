package org.spray.heaven.ui.avx.view.views;

import java.util.ArrayList;
import java.util.List;

import org.spray.heaven.ui.avx.view.View;
import org.spray.heaven.util.render.Drawable;

public class PanelView extends DraggableView {

	private final ArrayList<View> views = new ArrayList();
	protected boolean resizable, resizing;
	private float minWidth, minHeight;
	private float prevWidth, prevHeight;
	protected float padding = 6;

	@Override
	public void renderView(int mouseX, int mouseY, float delta) {
		super.renderView(mouseX, mouseY, delta);
		renderContent(mouseX, mouseY, delta);
		views.forEach(view -> {
			view.setX(getVX(view));
			view.setY(getVY(view));
			view.render(mouseX, mouseY, delta);
		});

		if (resizable && resizing) {
			dragging = false;
			width = prevWidth + mouseX;
			height = prevHeight + mouseY;

			width = Math.max(width, minWidth);
			height = Math.max(height, minHeight);
		}
	}

	protected void renderContent(int mouseX, int mouseY, float delta) {
	}

	protected float getVX(View view) {
		return view.getPointX() + view.getWidth() > width - padding ? x + width - view.getWidth() - padding
				: x + padding + view.getPointX();
	}

	protected float getVY(View view) {
		return view.getPointY() + view.getHeight() > y + height - padding ? y + height - view.getHeight() - padding
				: y + padding + view.getPointY();
	}

	@Override
	public void tick() {
		views.forEach(View::tick);
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (viewClicked(mouseX, mouseY, button))
			return true;
		
		for (View view : views) {
			if (view.mouseClicked(mouseX, mouseY, button))
				return true;
		}

		if (Drawable.isHovered(mouseX, mouseY, x + width - 10, y + height - 10, 10, 10) && button == 0) {
			resizing = true;
			prevWidth = width - mouseX;
			prevHeight = height - mouseY;

			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int button) {
		super.mouseReleased(mouseX, mouseY, button);
		resizing = false;
	}

	@Override
	public boolean isHovered(int mouseX, int mouseY) {
		return Drawable.isHovered(mouseX, mouseY, x, y, width, height);
	}

	public void add(View view) {
		views.add(view);
	}

	public void addAll(List<View> views) {
		views.addAll(views);
	}

	public void setResizable(boolean state) {
		resizable = state;
	}

	public void setDimension(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		minWidth = width * 0.8f;
		minHeight = height * 0.8f;
	}

	public void setMin(float width, float height) {
		minWidth = width;
		minHeight = height;
	}

}
