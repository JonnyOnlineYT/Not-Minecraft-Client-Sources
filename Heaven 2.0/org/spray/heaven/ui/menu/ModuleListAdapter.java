package org.spray.heaven.ui.menu;

import java.util.List;

import org.spray.heaven.features.module.Module;
import org.spray.heaven.ui.avx.view.ViewAdapter;

public class ModuleListAdapter extends ViewAdapter<Module, ModuleViewHolder> {
	
	public ModuleListAdapter(List<Module> dataSet) {
		super(dataSet);
	}

	@Override
	public void renderVH(Module data, ModuleViewHolder holder, int mouseX, int mouseY, float delta) {
		holder.render(data, mouseX, mouseY, delta);
	}

	@Override
	public ModuleViewHolder createVH(int pos) {
		return new ModuleViewHolder(pos);
	}

}
