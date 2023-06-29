package org.spray.infinity.features.module.visual;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;

import net.minecraft.client.util.math.MatrixStack;

@ModuleInfo(category = Category.VISUAL, desc = "Changes the position of the model's hands", key = -2, name = "ViewModel", visible = true)
public class ViewModel extends Module {

	private Setting x = new Setting(this, "X", 0D, -3D, 3D);
	private Setting y = new Setting(this, "Y", 0D, -3D, 3D);
	private Setting z = new Setting(this, "Z", 0D, -3D, 3D);

	private Setting scaleX = new Setting(this, "Scale X", 1D, -3D, 3D);
	private Setting scaleY = new Setting(this, "Scale Y", 1D, -3D, 3D);
	private Setting scaleZ = new Setting(this, "Scale Z", 1D, -3D, 3D);

	/*
	 * HeldItemRendererMixin action
	 */
	public void transform(MatrixStack matrices) {
		if (!isEnabled())
			return;

		matrices.scale((float) scaleX.getCurrentValueDouble(), (float) scaleY.getCurrentValueDouble(),
				(float) scaleZ.getCurrentValueDouble());
		matrices.translate(x.getCurrentValueDouble(), y.getCurrentValueDouble(), z.getCurrentValueDouble());
	}

}
