package org.spray.heaven.ui.avx.view.views;

import org.spray.heaven.font.IFont;
import org.spray.heaven.ui.avx.view.View;
import org.spray.heaven.util.render.Drawable;

public class SelectableView extends View {

	private OnSelectListener selectListener;
	private final String title;
	private boolean selected;

	public SelectableView(String title) {
		this.title = title;
	}

	@Override
	protected void renderView(int mouseX, int mouseY, float delta) {
		if (selected) {
			Drawable.drawRectWH(x, y, 2, height, context.getTheme().getColorPrimary().getRGB());
		}

		IFont.WEB_SMALL.drawVCenteredString(title, x + 16, y, height, context.getTheme().getTextColor().getRGB());
	}

	@Override
	protected boolean viewClicked(int mouseX, int mouseY, int button) {
		setSelected(!selected);
		return true;
	}

	public void setSelected(boolean state) {
		if (selectListener != null)
			selectListener.onSelect(state);
		this.selected = state;
	}

	public void disable() {
		selected = false;
	}

	public void setOnSelectListener(OnSelectListener listener) {
		selectListener = listener;
	}

	public interface OnSelectListener {
		void onSelect(boolean state);
	}

}
