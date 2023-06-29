package org.spray.infinity.features.component.macro;

public class Macro {

	private String message;
	private int key = -2;
	
	public Macro(String message, int key) {
		setMessage(message);
		setKey(key);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

}
