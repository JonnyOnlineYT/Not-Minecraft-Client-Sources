package volcano.summer.client.commands;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;
import volcano.summer.client.events.EventChatSent;

public class Prefix extends Command {

	public Prefix() {
		super("prefix", "<prefix>");
	}

	@Override
	public void run(String message) {
		String setprefix = EventChatSent.getMessage().split(" ")[1];
		if (setprefix.equalsIgnoreCase(setprefix)) {
			Summer.commandManager.setPrefix(setprefix);
			Summer.tellPlayer("Prefix Has Been Set To: " + setprefix);
		}
	}
}