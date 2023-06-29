package volcano.summer.client.value;

import java.util.ArrayList;
import java.util.List;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;

public class ModeValue extends Value {

	private String value;
	private String[] values;
	private ArrayList<String> valuess;
	public static ModeValue INSTANCE;

	public ModeValue(String name, String commandName, String value, String[] values, Module module) {
		super(name, commandName, values, module);
		this.value = value;
		this.values = values;
		INSTANCE = this;
	}

	public String getStringValue() {
		return this.value;
	}

	public void setStringValue(String value) {
		this.value = value;
		Summer.fileManager.saveFiles();
	}

	public String[] getStringValues() {
		return this.values;
	}

	public List<String> getModes() {
		return valuess;
	}

	public void next() {
		int index = -1;
		for (int i = 0; i < this.values.length; i++) {
			if (values[i].equalsIgnoreCase(this.value)) {
				index = i;
			}
		}
		if (index != -1) {
			if (index == this.values.length - 1) {
				index = -1;
			}
			index++;
		} else {
			index = 0;
		}
		setStringValue(this.values[index]);
	}

	public void increment() {
		int value = this.getStringValues().length + 1;
		if (value >= this.value.length())
			value = 0;
		this.setStringValue(this.valuess.get(value));
	}

	public void decrement() {
		int value = this.getStringValues().length - 1;
		if (value < 0)
			value = this.value.length() - 1;
		this.setStringValue(this.valuess.get(value));
	}

}
