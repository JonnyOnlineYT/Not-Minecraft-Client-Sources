package org.spray.infinity.event;

import java.util.List;

import org.spray.infinity.utils.Helper;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;

public class RenderTooltipEvent extends EventCancellable {

	private Screen screen;
	private MatrixStack matrix;
	private List<? extends OrderedText> text;
	private int x;
	private int y;

	public RenderTooltipEvent(MatrixStack matrix, List<? extends OrderedText> text, int x, int y) {
		this.matrix = matrix;
		screen = Helper.MC.currentScreen;
		this.text = text;
		this.x = x;
		this.y = y;
	}

	public Screen getScreen() {
		return screen;
	}

	public MatrixStack getMatrix() {
		return matrix;
	}

	public List<? extends OrderedText> getText() {
		return text;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setText(List<OrderedText> transform) {
		this.text = transform;

	}

}
