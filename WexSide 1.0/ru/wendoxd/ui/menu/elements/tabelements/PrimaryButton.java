package ru.wendoxd.ui.menu.elements.tabelements;

import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.Tab;
import ru.wendoxd.ui.menu.utils.MenuAPI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class PrimaryButton {
	Frame frame;
	String name, path, modifiedPath, description;
	List<Supplier<Boolean>> visibleConditions;
	Supplier<Boolean> enabledCondition;
	boolean ignoreVisible;

	public PrimaryButton(String name, Supplier<Boolean> condition) {
		this.name = name;
		this.enabledCondition = condition;
		this.visibleConditions = new ArrayList<Supplier<Boolean>>();
	}

	public static String buttonToString(PrimaryButton button) {
		String buttonText = null;
		if (button instanceof CheckBox) {
			buttonText = "cb";
		} else if (button instanceof ColorPicker) {
			buttonText = "cp";
		} else if (button instanceof MultiSelectBox) {
			buttonText = "msb";
		} else if (button instanceof SelectBox) {
			buttonText = "sb";
		} else if (button instanceof Slider) {
			buttonText = "slider";
		}
		return buttonText;
	}

	public void setEnabledCondition(Supplier<Boolean> supplier) {
		this.enabledCondition = supplier;
	}

	public static PrimaryButton forName(String name) {
		for (Tab tab : MenuAPI.tabs) {
			for (Frame frame : tab.getFrames()) {
				for (PrimaryButton button : frame.getButtons()) {
					if (button.getPath().equalsIgnoreCase(name)) {
						return button;
					}
				}
			}
		}
		return null;
	}

	public abstract float draw(float x, float yOffset);

	public abstract boolean mouseClicked(int x, int y, int mb);

	public abstract void animation();

	public abstract void set(float animation);

	public abstract void reset();

	public abstract double boundAnimation();

	public abstract void load(DataInputStream dis) throws Exception;

	public abstract void save(DataOutputStream dos) throws Exception;

	public void addVisibleCondition(Supplier<Boolean> condition) {
		this.visibleConditions.add(condition);
	}

	public Frame getFrame() {
		return this.frame;
	}

	public PrimaryButton markDescription(String description) {
		this.description = description;
		return this;
	}

	public void ignoreVisible() {
		this.ignoreVisible = true;
	}

	public boolean isIgnoreVisible() {
		return this.ignoreVisible;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
	}

	public String getPath() {
		if (this.modifiedPath != null && !this.path.endsWith(this.modifiedPath)) {
			this.releasePath();
		}
		return this.path;
	}

	public void releasePath() {
		this.path = this.frame.getTab().getName() + ":" + this.frame.getName() + ":" + buttonToString(this) + ":"
				+ this.name + (this.modifiedPath == null ? "" : ":" + this.modifiedPath);
	}

	public void modifyPath(String modifiedPath) {
		this.modifiedPath = modifiedPath;
	}

	public void keyTyped(char c, int keyCode) {

	}

	public String getName() {
		return this.name;
	}

	public boolean isVisible() {
		if (enabledCondition == null)
			return true;
		boolean visible = enabledCondition.get();
		for (Supplier<Boolean> supplier : this.visibleConditions) {
			if (!supplier.get()) {
				visible = false;
			}
		}
		return visible;
	}

	public String getDescription() {
		return this.description;
	}
}
