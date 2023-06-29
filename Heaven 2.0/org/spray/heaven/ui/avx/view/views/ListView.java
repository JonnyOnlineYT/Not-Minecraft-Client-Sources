package org.spray.heaven.ui.avx.view.views;

import java.util.ArrayList;
import java.util.List;

import org.spray.heaven.ui.avx.view.View;
import org.spray.heaven.ui.avx.view.ViewAdapter;
import org.spray.heaven.ui.avx.view.ViewHolder;

public class ListView extends View {

	protected final List<ViewHolder> holders = new ArrayList();
	protected final ViewAdapter adapter;
	protected OnClickListener clickListener;

	protected ListMode mode;

	protected float elementHeight = 20;
	protected int padding = 1;
	protected int offset = 0;

	public ListView(ViewAdapter adapter, ListMode mode) {
		this.adapter = adapter;

		for (int i = 0; i < adapter.getCount(); i++) {
			ViewHolder holder = (ViewHolder) adapter.createVH(i);
			if (holder == null)
				continue;
			
			holder.setOnClickListener(new org.spray.heaven.ui.avx.listeners.OnClickListener() {
				@Override
				public void onMouseClick(int button) {
					if (clickListener != null)
						clickListener.onMouseClick(adapter.getData().get(holder.getPos()), holder, button);
				}
			});
			holders.add(holder);
		}

		this.mode = mode;
	}
	
	@Override
	protected void viewTick() {
		holders.forEach(ViewHolder::tick);
	}

	@Override
	protected void renderView(int mouseX, int mouseY, float delta) {
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

	@Override
	protected boolean viewClicked(int mouseX, int mouseY, int button) {
		for (ViewHolder holder : holders) {
			if (holder.mouseClicked(mouseX, mouseY, button))
				return true;
		}
		return false;
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int button) {
		holders.forEach(holder -> holder.mouseReleased(mouseX, mouseY, button));
	}
	
	protected float getScrollProgress() {
		return 0;
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

	public void setColumns(int i) {
		mode.setColumns(i);
	}

	public void setElementHeight(float height) {
		elementHeight = height;
	}

	public void setOnClickListener(OnClickListener listener) {
		clickListener = listener;
	}

	public enum ListMode {
		LIST(1), GRID(2);

		int columns;

		ListMode(int columns) {
			this.columns = columns;
		}

		void setColumns(int i) {
			this.columns = i;
		}
	}

	public interface OnClickListener<T, V> {
		void onMouseClick(T data, V viewHolder, int button);
	}

}