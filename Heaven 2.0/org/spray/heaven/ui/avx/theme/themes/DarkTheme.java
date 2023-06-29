package org.spray.heaven.ui.avx.theme.themes;

import java.awt.Color;

import org.spray.heaven.ui.avx.theme.Theme;
import org.spray.heaven.ui.clickui.Colors;

public class DarkTheme extends Theme {

	@Override
	public Color getColorPrimary() {
		return Colors.CLIENT_COLOR;
	}

	@Override
	public Color getBackground() {
		return new Color(0xFF100F12);
	}

	@Override
	public Color getTextColor() {
		return new Color(235, 235, 235);
	}

	@Override
	public Color getInBackground() {
		return new Color(0xFF2F2F2F);
	}

	@Override
	public Color getShadowColor() {
		return new Color(0xFF0A0A0A);
	}

}
