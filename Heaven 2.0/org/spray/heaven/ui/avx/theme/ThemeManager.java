package org.spray.heaven.ui.avx.theme;

import java.util.HashMap;

import org.spray.heaven.ui.avx.theme.themes.DarkTheme;
import org.spray.heaven.ui.avx.theme.themes.LightTheme;

public class ThemeManager {
	
	private HashMap<String, Theme> themes = new HashMap();
	
	private Theme currentTheme;
	
	public ThemeManager() {
		themes.put("Light", new LightTheme());
		themes.put("Dark", new DarkTheme());
		
		currentTheme = themes.get("Light");
	}
	
	public HashMap<String, Theme> getThemes() {
		return themes;
	}
	
	public Theme getTheme() {
		return currentTheme;
	}
	
	public void setTheme(Theme theme) {
		this.currentTheme = theme;
	}

}
