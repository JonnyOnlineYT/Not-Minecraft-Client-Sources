package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

/**
 * event for InGameHud method = render()
 * 
 * @author spray
 *
 */
public class HudRenderEvent implements Event {
	public int screenWidth, screenHeight;
	public Window window;
	public MatrixStack matrixStack;

	public HudRenderEvent(Window window, int screenWidth, int screenHeight, MatrixStack matrixStak) {
		this.window = window;
		this.screenHeight = screenWidth;
		this.screenHeight = screenHeight;
		this.matrixStack = matrixStak;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public Window getWindow() {
		return window;
	}

	public void setWindow(Window window) {
		this.window = window;
	}

	public MatrixStack getMatrixStack() {
		return matrixStack;
	}

	public void setMatrixStack(MatrixStack matrixStack) {
		this.matrixStack = matrixStack;
	}
}
