package org.spray.heaven.features.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

public class Setting {

	private String name;
	private boolean toggle;
	private double value, minValue, maxValue;
	private List<String> modes;
	private String currentMode;
	private Color color;
	private int key;

	private boolean alphaActive = false; // For color setting
	
	private ConcurrentLinkedQueue<Integer> blocks, renderBlocks;

	private Supplier<Boolean> visible = () -> true;

	private final Category category;

	// Boolean
	public Setting(String name, boolean state) {
		this.name = name;
		this.toggle = state;
		this.category = Category.BOOLEAN;
		this.key = Keyboard.KEY_NONE;
	}

	// Number
	public Setting(String name, double currentValue, double minValue, double maxValue) {
		this.name = name;
		this.value = currentValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.category = Category.NUMBER;
	}

	// Mode
	public Setting(String name, String currentMode, List<String> modes) {
		this.name = name;
		this.currentMode = currentMode;
		this.modes = modes;
		this.category = Category.MODE;
	}

	// Color
	public Setting(String name, Color currentColor) {
		this.name = name;
		this.color = currentColor;
		this.category = Category.COLOR;
		this.alphaActive = true;
	}

	// Block
	public Setting(String name, ConcurrentLinkedQueue<Integer> blocks, ConcurrentLinkedQueue<Integer> renderBlocks) {
		this.name = name;
		this.blocks = blocks;
		this.renderBlocks = renderBlocks;
		this.category = Category.BLOCK;
	}

	// Key
	public Setting(String name, int key) {
		this.name = name;
		this.key = key;
		this.category = Category.KEY;
	}

	public Setting setVisible(Supplier<Boolean> state) {
		this.visible = state;
		return this;
	}

	public Setting disableAlpha() {
		this.alphaActive = false;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isToggle() {
		return toggle;
	}

	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public List<String> getModes() {
		return modes;
	}

	public void setModes(ArrayList<String> modes) {
		this.modes = modes;
	}

	public String getCurrentMode() {
		return currentMode;
	}

	public void setCurrentMode(String currentMode) {
		this.currentMode = currentMode;
	}

	public Color getColor() {
		return color;
	}

	public boolean isAlphaActive() {
		return alphaActive;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public ConcurrentLinkedQueue<Integer> getBlocks() {
		return blocks;
	}

	public ConcurrentLinkedQueue<Integer> getRenderBlocks() {
		return renderBlocks;
	}

	public boolean isVisible() {
		return visible.get();
	}

	public Category getCategory() {
		return category;
	}

	public enum Category {
		BOOLEAN, NUMBER, MODE, COLOR, BLOCK, KEY;
	}

	/**
	 * Called when changing the value (only menu)
	 */
	public void onUpdate() {
	}

	public void onKeyPressed(int key) {
		if (!category.equals(Category.BOOLEAN))
			return;

		if (key == this.key)
			setToggle(!isToggle());
	}

	public void load(List<Setting> settings, JsonObject jsonObject) {
		for (Setting setting : settings) {
			if (jsonObject.get(setting.getName()) == null || jsonObject.get(setting.getName()).isJsonNull())
				continue;

			switch (setting.getCategory()) {
			case MODE:
				setting.setCurrentMode(jsonObject.get(setting.getName()).getAsString());
				break;
			case BOOLEAN:
				setting.setToggle(jsonObject.get(setting.getName()).getAsBoolean());

				if (jsonObject.get(setting.getName() + "_key") != null)
				setting.setKey(jsonObject.get(setting.getName() + "_key").getAsInt());
				break;
			case NUMBER:
				setting.setValue(jsonObject.get(setting.getName()).getAsDouble());
				break;
			case COLOR:
				setting.setColor(new Color(jsonObject.get(setting.getName()).getAsInt()));
				break;
			case KEY:
				setting.setKey(jsonObject.get(setting.getName()).getAsInt());
				break;
			case BLOCK:
				JsonArray jsonArray = jsonObject.get(setting.getName()).getAsJsonArray();
				if (jsonArray == null)
					return;

				for (JsonElement jsonElement : jsonArray) {
					if (!setting.getBlocks().contains(jsonElement.getAsInt()))
						setting.getBlocks().add(jsonElement.getAsInt());
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * @param settings
	 * @param dataJson
	 * @param jsonArray
	 */
	public void save(List<Setting> settings, JsonObject dataJson, JsonArray jsonArray) {
		if (settings == null)
			return;

		for (Setting setting : settings) {
			switch (setting.getCategory()) {
			case BOOLEAN:
				dataJson.addProperty(setting.getName(), setting.isToggle());
				if (setting.getKey() != Keyboard.KEY_NONE)
					dataJson.addProperty(setting.getName() + "_key", setting.getKey());
				break;
			case MODE:
				dataJson.addProperty(setting.getName(), setting.getCurrentMode());
				break;
			case NUMBER:
				dataJson.addProperty(setting.getName(), setting.getValue());
				break;
			case COLOR:
				dataJson.addProperty(setting.getName(), setting.getColor().getRGB());
				break;
			case KEY:
				dataJson.addProperty(setting.getName(), setting.getKey());
				break;
			case BLOCK:
				for (Integer id : setting.getBlocks()) {
					jsonArray.add(id);
				}
				dataJson.add(setting.getName(), jsonArray);
				break;
			default:
				break;
			}
		}
	}
}
