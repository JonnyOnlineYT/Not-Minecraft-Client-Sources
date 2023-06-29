package volcano.summer.base.manager.command;

import net.minecraft.client.Minecraft;
import volcano.summer.client.events.EventRender3D;

public abstract class Command {

	public String command;
	protected String arguments;
	protected String description;
	protected static Minecraft mc = Minecraft.getMinecraft();

	public Command(String command, String arguments) {
		this.description = new String();
		this.command = command;
		this.arguments = arguments;
	}

	public String getArguments() {
		return this.arguments;
	}

	public String getCommand() {
		return this.command;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void onRender(EventRender3D event) {
	}

	public abstract void run(String message);
}
