package ru.wendoxd.modules.impl.visuals;

import ru.wendoxd.modules.Module;
import ru.wendoxd.modules.impl.combat.Aura;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class SwingAnimations extends Module {
	private Frame swinganimations_frame = new Frame("SwingAnimations");
	public static CheckBox enabled = new CheckBox("Animations");
	public static SelectBox animations = new SelectBox("Animation",
			new String[] { "Default", "Blocking", "Wex", "DropDown" }, () -> enabled.isEnabled(true));
	public static CheckBox spin = new CheckBox("Spin", () -> enabled.isEnabled(true));
	public static Slider right_x = new Slider("Right X", 2, -2, 2, 0.5, () -> enabled.isEnabled(true));
	public static Slider right_y = new Slider("Right Y", 2, -1, 1, 0.5, () -> enabled.isEnabled(true));
	public static Slider right_z = new Slider("Right Z", 2, -2, 2, 0.5, () -> enabled.isEnabled(true));
	public static Slider left_x = new Slider("Left X", 2, -2, 2, 0.5, () -> enabled.isEnabled(true));
	public static Slider left_y = new Slider("Left Y", 2, -1, 1, 0.5, () -> enabled.isEnabled(true));
	public static Slider left_z = new Slider("Left Z", 2, -2, 2, 0.5, () -> enabled.isEnabled(true));
	public static CheckBox onlyAura = new CheckBox("Only On Aura", () -> enabled.isEnabled(true));

	@Override
	protected void initSettings() {
		MenuAPI.visuals.register(swinganimations_frame);
		swinganimations_frame.register(enabled, animations, onlyAura, spin, right_x, right_y, right_z, left_x, left_y,
				left_z);
	}

	public static boolean isEnabled() {
		if (onlyAura.isEnabled(false) && Aura.target == null) {
			return false;
		}
		return true;
	}
}
