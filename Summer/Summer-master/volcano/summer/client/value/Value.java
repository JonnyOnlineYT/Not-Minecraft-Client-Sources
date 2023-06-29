package volcano.summer.client.value;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;

public class Value<T> {

	protected T value;
	private T defaultValue;
	private Module module;
	private String name;
	private String commandName;
	private boolean onlyint = false;

	public Value(String name, String commandName, T value, Module module) {
		this.name = name;
		this.commandName = commandName;
		this.value = value;
		this.defaultValue = value;
		this.module = module;
		Summer.valueManager.values.add(this);
	}

	public T getValue() {
		return this.value;
	}

	public T getDefaultValue() {
		return this.defaultValue;
	}

	public String getName() {
		return this.name;
	}

	public String getCommandName() {
		return this.commandName;
	}

	public Module getModule() {
		return this.module;
	}

	public void setValue(T value) {
		this.value = value;
		Summer.fileManager.saveFiles();
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setCommandName(final String commandName) {
		this.commandName = commandName;
	}

	public boolean onlyInt() {
		return this.onlyint;
	}
}
