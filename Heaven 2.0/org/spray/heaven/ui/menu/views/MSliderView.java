package org.spray.heaven.ui.menu.views;

import org.spray.heaven.ui.avx.view.View;
import org.spray.heaven.util.render.RenderUtil;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import net.minecraft.util.math.MathHelper;

public class MSliderView extends View {

	private onUpdateListener listener;

	private float animation;
	private final double min;
	private final double max;
	private double value;
	private boolean dragging;

	@Override
	public void viewTick() {
		double currentPos = (value - min) / (max - min);
		animation = (float) (dragging ? currentPos : RenderUtil.scrollAnimate(animation, (float) currentPos, .5f));
	}

	public MSliderView(double value, double min, double max) {
		this.value = value;
		this.min = min;
		this.max = max;
	}

	@Override
	public void renderView(int mouseX, int mouseY, float delta) {
		float cr = 5;
		float ch = 1;

		double currentPos = (value - min) / (max - min);
		animation = (float) (dragging ? currentPos : RenderUtil.scrollAnimate(animation, (float) currentPos, .5f));
		RoundedShader.drawRound(x + 4, y + (height - ch) / 2, (float) ((width - 8)), ch, ch / 2f,
				context.getTheme().getInBackground());

		RoundedShader.drawRound(x + 4, y + (height - ch) / 2, (float) ((width - 8) * animation), ch, ch / 2f,
				context.getTheme().getColorPrimary());

		RoundedShader.drawRound((float) (x + 2 + (width - 8) * animation), y + (height - cr) / 2, cr, cr,
				cr / 2f - 0.5f, context.getTheme().getColorPrimary());

		if (dragging)
			setValue(mouseX, x + 7, width - 14);
	}

	@Override
	public boolean viewClicked(int mouseX, int mouseY, int button) {
		if (button == 0) {
			this.dragging = true;
			return true;
		}

		return dragging;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int button) {
		this.dragging = false;
	}
	
	@Override
	public void onClose() {
		animation = 0;
	}

	private void setValue(int mouseX, double x, double width) {
		double diff = max - min;
		double percentBar = MathHelper.clamp((mouseX - x) / width, 0.0, 1.0);
		double value = min + percentBar * diff;

		this.value = value;
		listener.onUpdate(value);
	}

	public void setOnUpdateListener(onUpdateListener listener) {
		this.listener = listener;
	}

	public interface onUpdateListener {

		void onUpdate(double value);

	}

}
