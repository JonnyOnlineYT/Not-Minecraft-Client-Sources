	package org.spray.heaven.ui.avx.theme.themes;

import java.awt.Color;

import org.spray.heaven.ui.avx.theme.Theme;
import org.spray.heaven.ui.clickui.Colors;

    public class LightTheme extends Theme {

	@Override
	public Color getColorPrimary() {
		return Colors.CLIENT_COLOR;
	}

	@Override
	public Color getBackground() {
		return new Color(240, 240, 240);
	}

	@Override
	public Color getTextColor() {
		return new Color(6, 6, 6);
	}

	@Override
	public Color getInBackground() {
		return new Color(0xffD6D6D6);
	}

	@Override
	public Color getShadowColor() {
		return new Color(0xFF232323);
	}
	
}
