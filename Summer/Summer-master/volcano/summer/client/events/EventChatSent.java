package volcano.summer.client.events;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;
import volcano.summer.base.manager.command.CommandManager;
import volcano.summer.base.manager.event.Event;

public class EventChatSent extends Event {

	private boolean cancel;
	private static String message;

	public EventChatSent(String message) {
		this.message = message;
	}

	public void checkForCommands(String message) {
		if (message.startsWith(CommandManager.prefix)) {
			for (Command command : Summer.commandManager.commands) {
				if (this.message.split(" ")[0]
						.equalsIgnoreCase(String.valueOf(CommandManager.prefix) + command.getCommand())) {
					try {
						command.run(message);
					} catch (Exception e) {
						Summer.tellPlayer("Incorrect syntax! " + command.getCommand() + " " + command.getArguments());
					}
					this.cancel = true;
				}
			}
			if (!this.cancel) {
				Summer.tellPlayer("Command \"" + this.message + "\" was not found!");
				this.cancel = true;
			}
		}
	}

	public static String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public boolean getCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
