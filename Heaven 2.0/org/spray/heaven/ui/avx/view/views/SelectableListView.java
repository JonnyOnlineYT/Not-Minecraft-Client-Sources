package org.spray.heaven.ui.avx.view.views;

import java.util.ArrayList;
import java.util.List;

import org.spray.heaven.ui.avx.view.View;

public class SelectableListView extends View {

	private List<SelectableView> views = new ArrayList();

	private OnSelectListener listener;

	private float elementHeight = 20;
	protected int padding = 1;
	private int offset = 0;

	@Override
	protected void renderView(int mouseX, int mouseY, float delta) {
		int io = 0;
		for (SelectableView view : views) {
			view.setPoints(padding, padding + io, width - padding, elementHeight - padding);
			float bx = view.getPointX() + view.getWidth() > width - padding ? x + width - view.getWidth() - padding
					: x + padding + view.getPointX();
			view.setX(bx);
			view.setY(y + padding + view.getPointY());
			view.render(mouseX, mouseY, delta);

			io += view.getHeight() + offset;
		}
	}

	@Override
	protected boolean viewClicked(int mouseX, int mouseY, int button) {
		for (View view : views) {
			if (view.mouseClicked(mouseX, mouseY, button))
				return true;
		}
		return false;
	}

	public void add(SelectableView view) {
		view.setOnSelectListener(new SelectableView.OnSelectListener() {
			@Override
			public void onSelect(boolean state) {
				reset();

				if (listener != null)
					listener.onSelect(view, views.indexOf(view), state);
			}
		});
		views.add(view);
	}

	public void reset() {
		views.forEach(v -> v.disable());
	}

	public void setOnSelectListener(OnSelectListener listener) {
		this.listener = listener;
	}

	/**
	 * Set the indents of elements from each other
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Set internal margins
	 * 
	 * @param padding
	 */
	public void setPadding(int padding) {
		this.padding = padding;
	}

	public void setElementHeight(float height) {
		elementHeight = height;
	}

	public interface OnSelectListener {
		void onSelect(SelectableView view, int index, boolean state);
	}
}
