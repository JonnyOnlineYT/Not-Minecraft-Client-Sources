package volcano.summer.client.commands;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;
import volcano.summer.client.events.EventChatSent;

public class Connect extends Command {

	public Connect() {
		super("Connect", "<serverIP>");
	}

	@Override
	public void run(String message) {
		String serverip = EventChatSent.getMessage().split(" ")[1];
		ServerData serverData = new ServerData("", serverip);
		this.mc.theWorld.sendQuittingDisconnectingPacket();
		this.mc.loadWorld(null);
		this.mc.displayGuiScreen(new GuiConnecting(null, this.mc, serverData));
		Summer.tellPlayer("Connecting...");
	}
}