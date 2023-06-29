package org.spray.heaven.ui.avx.view.views;

import org.spray.heaven.ui.avx.view.View;

public abstract class DraggableView extends View {

	private float prevX, prevY;
	protected boolean dragging;

	@Override
	protected void renderView(int mouseX, int mouseY, float delta) {
		if (this.dragging) {
			this.x = this.prevX + mouseX;
			this.y = this.prevY + mouseY;
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (isHovered(mouseX, mouseY) && button == 0) {
			this.dragging = true;
			this.prevX = this.x - mouseX;
			this.prevY = this.y - mouseY;
			
			if (onClickListener != null)
			onClickListener.onMouseClick(button);
		}
		return false;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int button) {
		if (button == 0)
			this.dragging = false;
	}

	public abstract boolean isHovered(int mouseX, int mouseY);

}
