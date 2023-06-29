package org.spray.heaven.features.module;

public enum Category {
	COMBAT("Combat"), MOVEMENT("Movement"), PLAYER("Player"), MISC("Misc"), RENDER("Render"), DISPLAY("Display");

	String name;

	Category(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
