package ru.wendoxd.modules.impl.visuals;

import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.ColorPicker;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class Weather extends Module {
	public static Frame weatherFrame = new Frame("World");
	public static CheckBox weather = new CheckBox("Features");
	public static CheckBox worldcolor = new CheckBox("WorldColor", () -> weather.isEnabled(true)).markColorPicker();
	public static MultiSelectBox types = new MultiSelectBox("Type", new String[] { "Snow" },
			() -> weather.isEnabled(true));
	public static ColorPicker snow = new ColorPicker("Snow Color", () -> types.get(0) && weather.isEnabled(true));
	public static CheckBox customFog = new CheckBox("Custom Fog", () -> weather.isEnabled(true)).markColorPicker();
	public static Slider fogDistance = new Slider("Distance", 1, 0, 4, 0,
			() -> weather.isEnabled(true) && customFog.isEnabled(true));

	@Override
	public void initSettings() {
		MenuAPI.visuals.register(weatherFrame);
		weatherFrame.register(weather, worldcolor, types, snow, customFog, fogDistance);
	}
}
