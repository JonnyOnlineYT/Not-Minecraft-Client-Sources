package org.spray.heaven.ui.avx.view;

import org.spray.heaven.util.render.Drawable;

public abstract class ViewHolder<T> extends ViewHeader {

	public int zIndex = 0;
	private int pos;

	public ViewHolder(int pos) {
		this.pos = pos;
	}

	protected boolean hidden = false;
	
	public void tick() {}

	public void render(T data, int mouseX, int mouseY, float delta) {
		if (!hidden) {
			renderVH(data, mouseX, mouseY, delta);
		}
	}

	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (Drawable.isHovered(mouseX, mouseY, x, y, width, height)) {
			if (onClickListener != null)
				onClickListener.onMouseClick(button);

			return viewClicked(mouseX, mouseY, button);
		}
		return false;
	}
	
	public void mouseReleased(int mouseX, int mouseY, int button) {
	}

	protected abstract void renderVH(T data, int mouseX, int mouseY, float delta);

	protected boolean viewClicked(int mouseX, int mouseY, int button) {
		return false;
	}

	public int getPos() {
		return pos;
	}

	public void show() {
		hidden = false;
	}

	public void hide() {
		hidden = true;
	}

}
