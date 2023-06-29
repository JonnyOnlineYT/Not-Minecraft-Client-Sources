package org.spray.heaven.ui.menu;

import java.awt.Color;

import org.spray.heaven.ui.menu.views.MColorPicker;

public class MenuProvider {
	
	private static MenuProvider INSTANCE;
	
	private ColorPickerListener pickerListener;

	public static void init() {
		INSTANCE = new MenuProvider();
	}
	
	public static MenuProvider getInstance() {
		return INSTANCE;
	}
	
	public MColorPicker showPicker(int x, int y, Color color, String title) {
		return pickerListener.showPicker(x, y, color, title);
	}
	
	public void setColorPickerListener(ColorPickerListener pickerListener) {
		this.pickerListener = pickerListener;
	}
	
	public interface ColorPickerListener {
		
		MColorPicker showPicker(int x, int y, Color color, String title);
		
	}

}
