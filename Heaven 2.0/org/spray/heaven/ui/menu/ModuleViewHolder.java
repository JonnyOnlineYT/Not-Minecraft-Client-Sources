package org.spray.heaven.ui.menu;

import org.spray.heaven.features.module.Module;
import org.spray.heaven.font.IFont;
import org.spray.heaven.ui.avx.view.ViewHolder;

public class ModuleViewHolder extends ViewHolder<Module> {

	protected boolean opened = false;

	public ModuleViewHolder(int pos) {
		super(pos);
	}

	@Override
	public void renderVH(Module data, int mouseX, int mouseY, float delta) {
		IFont.WEB_SMALL.drawVCenteredString(data.getName(), x + 2, y, height,
				data.isEnabled() ? context.getTheme().getColorPrimary().getRGB()
						: context.getTheme().getTextColor().getRGB());
	}

	public void updateOpened() {
		opened = true;
	}
}
