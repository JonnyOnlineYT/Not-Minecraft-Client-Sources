package org.spray.heaven.ui.avx;

import java.util.HashMap;

import org.spray.heaven.ui.avx.theme.Theme;
import org.spray.heaven.ui.avx.theme.ThemeManager;

public class AVXContext {
	
	private static AVXContext INSTANCE;
	
	private ThemeManager themeManager;
	
	public static void init(ThemeManager themeManager) {
		INSTANCE = new AVXContext();
		INSTANCE.themeManager = themeManager;
	}
	
	public static AVXContext getInstance() {
		return INSTANCE;
	}
	
	public Theme getTheme() {
		return themeManager.getTheme();
	}
	
	public HashMap<String, Theme> getThemes() {
		return themeManager.getThemes();
	}
	
	public void setTheme(Theme theme) {
		themeManager.setTheme(theme);
	}

}
