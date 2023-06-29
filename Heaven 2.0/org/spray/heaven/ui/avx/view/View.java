package org.spray.heaven.ui.avx.view;

import org.spray.heaven.util.render.Drawable;

/**
 * @author spray
 * @since Android UI :D
 */
public class View extends ViewHeader {

	protected int padding = 0;
	protected boolean hidden = false;

	public void render(int mouseX, int mouseY, float delta) {
		if (!hidden) {
			renderView(mouseX, mouseY, delta);
		}
	}
	
	public void tick() {
		if (!hidden)
			viewTick();
	}

	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (!hidden && isHovered(mouseX, mouseY))
			return viewClicked(mouseX, mouseY, button);
		
		return false;
	}

	public void mouseReleased(int mouseX, int mouseY, int button) {
	}
	
	public void onClose() {}
	
	/*
	 * Internal methods
	 */
	protected void renderView(int mouseX, int mouseY, float delta) {}
	protected boolean viewClicked(int mouseX, int mouseY, int button) {
		return false;
	}
	protected void viewTick() {}
	
	public boolean isHovered(int mouseX, int mouseY) {
		return Drawable.isHovered(mouseX, mouseY, x, y, width, height);
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
	}

	public void show() {
		hidden = false;
	}

	public void hide() {
		onClose();
		hidden = true;
	}
}
