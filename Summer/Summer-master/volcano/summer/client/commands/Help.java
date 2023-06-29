package volcano.summer.client.commands;

import java.util.ArrayList;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.util.ChatComponentText;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;

public class Help extends Command {

	public static void logToChatClean(String message) {
		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
	}

	public Help() {
		super("Help", "");
	}

	@Override
	public void run(String message) {
		this.logToChatClean(ChatFormatting.GOLD + "----- Summer Commands -----");
		ArrayList<Command> builtInCommands = new ArrayList();
		for (Command c : Summer.commandManager.getCommands()) {
			if ((c.getCommand().equalsIgnoreCase("connect")) || (c.getCommand().equalsIgnoreCase("bind"))
					|| (c.getCommand().equalsIgnoreCase("toggle")) || (c.getCommand().equalsIgnoreCase("friend"))
					|| (c.getCommand().equalsIgnoreCase("damage")) || (c.getCommand().equalsIgnoreCase("hclip"))
					|| (c.getCommand().equalsIgnoreCase("preifx")) || (c.getCommand().equalsIgnoreCase("rename"))
					|| (c.getCommand().equalsIgnoreCase("say")) || (c.getCommand().equalsIgnoreCase("vclip"))
					|| (c.getCommand().equalsIgnoreCase("settings")) || (c.getCommand().equalsIgnoreCase("SS"))
					|| (c.getCommand().equalsIgnoreCase("config"))) {
				builtInCommands.add(c);
			}
		}
		Summer.tellPlayer("Press Insert on a module to bind it in the tabgui.");
		for (Command bC : builtInCommands) {
			Summer.tellPlayer(bC.getCommand() + " " + ChatFormatting.WHITE + bC.getArguments());
		}
	}
}