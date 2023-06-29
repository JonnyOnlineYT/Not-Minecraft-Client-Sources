package org.spray.infinity.features;

import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

/**
 * @Enaium base
 *
 */
public class Setting {

	private Module module;
	private String name;
	private Supplier<Boolean> visible;
	private boolean toggle, defaultToogle;
	private double currentValueDouble, minValueDouble, maxValueDouble, defaultDouble;
	private int currentValueInt, minValueInt, maxValueInt, defaultInt;
	private float currentValueFloat, minValueFloat, maxValueFloat, defaultFloat;
	private ArrayList<Block> blocks, defaultBlocks;
	private ArrayList<Block> renderBlocks;
	private ArrayList<String> modes;
	private String currentMode, defaultMode;
	private String text, defaultText;
	private Color color, defaultColor;

	private Category category;

	public enum Category {
		BOOLEAN, VALUE_INT, VALUE_DOUBLE, VALUE_FLOAT, MODE, COLOR, BLOCKS, TEXT;
	}

	// boolean
	public Setting(Module module, String name, boolean toggle) {
		this.module = module;
		this.name = name;
		this.toggle = toggle;
		this.defaultToogle = toggle;
		this.category = Category.BOOLEAN;
		this.visible = () -> true;
	}

	// int number
	public Setting(Module module, String name, int currentValueInt, int minValueInt, int maxValueInt) {
		this.module = module;
		this.name = name;
		this.currentValueInt = currentValueInt;
		this.defaultInt = currentValueInt;
		this.minValueInt = minValueInt;
		this.maxValueInt = maxValueInt;
		this.category = Category.VALUE_INT;
		this.visible = () -> true;
	}

	// double number
	public Setting(Module module, String name, double currentValueDouble, double minValueDouble,
			double maxValueDouble) {
		this.module = module;
		this.name = name;
		this.currentValueDouble = currentValueDouble;
		this.defaultDouble = currentValueDouble;
		this.minValueDouble = minValueDouble;
		this.maxValueDouble = maxValueDouble;
		this.category = Category.VALUE_DOUBLE;
		this.visible = () -> true;
	}

	// float number
	public Setting(Module module, String name, float currentValueFloat, float minValueFloat, float maxValueFloat) {
		this.module = module;
		this.name = name;
		this.currentValueFloat = currentValueFloat;
		this.defaultFloat = currentValueFloat;
		this.minValueFloat = minValueFloat;
		this.maxValueFloat = maxValueFloat;
		this.category = Category.VALUE_FLOAT;
		this.visible = () -> true;
	}

	// String mode
	public Setting(Module module, String name, String currentMode, ArrayList<String> options) {
		this.module = module;
		this.name = name;
		this.currentMode = currentMode;
		this.defaultMode = currentMode;
		this.modes = options;
		this.category = Category.MODE;
		this.visible = () -> true;
	}

	// Color
	public Setting(Module module, String name, Color currentColor) {
		this.module = module;
		this.name = name;
		this.color = currentColor;
		this.defaultColor = currentColor;
		this.category = Category.COLOR;
		this.visible = () -> true;
	}

	// Blocks
	public Setting(Module module, String name, ArrayList<Block> blocks, ArrayList<Block> renderBlocks) {
		this.module = module;
		this.name = name;
		this.blocks = blocks;
		this.defaultBlocks = blocks;
		this.renderBlocks = renderBlocks;
		this.category = Category.BLOCKS;
		this.visible = () -> true;
	}

	// Text
	public Setting(Module module, String name, String text) {
		this.module = module;
		this.name = name;
		this.text = text;
		this.defaultText = text;
		this.category = Category.TEXT;
		this.visible = () -> true;
	}

	public Setting setVisible(Supplier<Boolean> visible) {
		this.visible = visible;
		return this;
	}

	public Setting setColor(Color color) {
		this.color = color;
		return this;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
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

	public double getCurrentValueDouble() {
		return currentValueDouble;
	}

	public void setCurrentValueDouble(double currentValueDouble) {
		this.currentValueDouble = currentValueDouble;
	}

	public double getMinValueDouble() {
		return minValueDouble;
	}

	public void setMinValueDouble(double minValueDouble) {
		this.minValueDouble = minValueDouble;
	}

	public double getMaxValueDouble() {
		return maxValueDouble;
	}

	public void setMaxValueDouble(double maxValueDouble) {
		this.maxValueDouble = maxValueDouble;
	}

	public int getCurrentValueInt() {
		return currentValueInt;
	}

	public void setCurrentValueInt(int currentValueInt) {
		this.currentValueInt = currentValueInt;
	}

	public int getMinValueInt() {
		return minValueInt;
	}

	public void setMinValueInt(int minValueInt) {
		this.minValueInt = minValueInt;
	}

	public int getMaxValueInt() {
		return maxValueInt;
	}

	public void setMaxValueInt(int maxValueInt) {
		this.maxValueInt = maxValueInt;
	}

	public float getCurrentValueFloat() {
		return currentValueFloat;
	}

	public void setCurrentValueFloat(float currentValueFloat) {
		this.currentValueFloat = currentValueFloat;
	}

	public float getMinValueFloat() {
		return minValueFloat;
	}

	public void setMinValueFloat(float minValueFloat) {
		this.minValueFloat = minValueFloat;
	}

	public float getMaxValueFloat() {
		return maxValueFloat;
	}

	public void setMaxValueFloat(float maxValueFloat) {
		this.maxValueFloat = maxValueFloat;
	}

	public ArrayList<String> getModes() {
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

	public void setColor(int color) {
		this.color = new Color(color);
	}

	public void setText(String text) {
		this.text = text;
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public void addBlockFromId(int id) {
		BlockState block = Block.getStateFromRawId(id);
		if (!blocks.contains(block.getBlock())) {
			blocks.add(block.getBlock());
		}
	}

	public ArrayList<Block> getRenderBlocks() {
		return renderBlocks;
	}

	public int getCurrentModeIndex() {
		int index = 0;
		for (String s : modes) {
			if (s.equalsIgnoreCase(currentMode)) {
				return index;
			}
			index++;
		}
		return index;
	}

	public boolean isBoolean() {
		return this.category.equals(Category.BOOLEAN);
	}

	public boolean isValueInt() {
		return this.category.equals(Category.VALUE_INT);
	}

	public boolean isValueDouble() {
		return this.category.equals(Category.VALUE_DOUBLE);
	}

	public boolean isValueFloat() {
		return this.category.equals(Category.VALUE_FLOAT);
	}

	public boolean isValue() {
		return this.category.equals(Category.VALUE_INT) || this.category.equals(Category.VALUE_DOUBLE)
				|| this.category.equals(Category.VALUE_FLOAT);
	}

	public boolean isMode() {
		return this.category.equals(Category.MODE);
	}

	public boolean isColor() {
		return this.category.equals(Category.COLOR);
	}

	public boolean isBlock() {
		return this.category.equals(Category.BLOCKS);
	}

	public boolean isVisible() {
		return visible.get().booleanValue();
	}

	public String getText() {
		return text;
	}

	public boolean isDefaultToogle() {
		return defaultToogle;
	}

	public double getDefaultDouble() {
		return defaultDouble;
	}

	public int getDefaultInt() {
		return defaultInt;
	}

	public float getDefaultFloat() {
		return defaultFloat;
	}

	public ArrayList<Block> getDefaultBlocks() {
		return defaultBlocks;
	}

	public String getDefaultMode() {
		return defaultMode;
	}

	public Color getDefaultColor() {
		return defaultColor;
	}

	public String getDefaultText() {
		return defaultText;
	}

	public Category getCategory() {
		return category;
	}

}
