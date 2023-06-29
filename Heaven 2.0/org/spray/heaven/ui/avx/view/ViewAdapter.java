package org.spray.heaven.ui.avx.view;

import java.util.List;

public abstract class ViewAdapter<T, V> {
	
	private List<T> dataSet;
	
	public ViewAdapter(List<T> dataSet) {
		this.dataSet = dataSet;
	}
	
	public void render(V holder, int mouseX, int mouseY, float delta, int pos) {
		renderVH(dataSet.get(pos), holder, mouseX, mouseY, delta);
	}
	
	public int getCount() {
		return dataSet.size();
	}
	
	public List<T> getData() {
		return dataSet;
	}

	public abstract void renderVH(T data, V holder, int mouseX, int mouseY, float delta);
	public abstract V createVH(int pos);
	
}
