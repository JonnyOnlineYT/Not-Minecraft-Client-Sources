package volcano.summer.client.commands;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;
import volcano.summer.client.events.EventChatSent;

public class Vclip extends Command {

	public Vclip() {
		super("Vclip", "<blocks>");
	}

	@Override
	public void run(String message) {
		double blocks = Double.parseDouble(EventChatSent.getMessage().split(" ")[1]);
		this.mc.thePlayer.func_174826_a(this.mc.thePlayer.getEntityBoundingBox().offset(0.0D, blocks, 0.0D));
		Summer.tellPlayer("Teleported " + blocks + " blocks.");
	}
}
