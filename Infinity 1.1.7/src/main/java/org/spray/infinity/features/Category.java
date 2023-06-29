package org.spray.infinity.features;

public enum Category {
	COMBAT("Combat"),
	MOVEMENT("Movement"),
	WORLD("World"),
	PLAYER("Player"),
	VISUAL("Visual"),
	MISC("Misc"),
	HIDDEN("Hidden"),
	ENABLED("Enabled");
	
	private String name;
	
	Category(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
} 
