package org.spray.heaven.features.command;

import org.spray.heaven.main.Wrapper;

import net.minecraft.client.Minecraft;

public abstract class Command {

	protected final Minecraft mc = Wrapper.MC;
	public static String PREFIX = ".";

	private String name;
	private String desc;

	public Command() {
		setName(this.getClass().getAnnotation(CommandInfo.class).name());
		setDesc(this.getClass().getAnnotation(CommandInfo.class).desc());
	}

	// methods
	public abstract void command(String[] args, String msg);

	public abstract void error();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void send(String message) {
		Wrapper.message(message);
	}

}
