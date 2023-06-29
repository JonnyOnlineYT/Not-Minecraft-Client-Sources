package ru.wendoxd.command;

import net.minecraft.client.Minecraft;
import ru.wendoxd.command.impl.PrefixCommand;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventSendMessage;
import ru.wendoxd.utils.misc.ChatUtils;

public abstract class Command {

	protected static Minecraft mc = Minecraft.getMinecraft();

	public String name;
	public String desc;

	public Command() {
		name = getClass().getAnnotation(CommandInfo.class).name();
		desc = getClass().getAnnotation(CommandInfo.class).desc();
	}

	public abstract void execute(String[] args) throws Exception;

	public abstract void error();

	public void sendMessage(String message) {
		ChatUtils.addChatMessage(message);
	}

	public void onEvent(Event event) {
		if (event instanceof EventSendMessage) {
			String message = ((EventSendMessage) event).getMessage();
			if (((EventSendMessage) event).getMessage().startsWith(PrefixCommand.prefix) && message.length() > 1) {
				String[] split = message.substring(1).split(" ");
				if (split.length > 0) {
					if (split[0].equalsIgnoreCase(name)) {
						try {
							execute(split);
						} catch (Exception e) {
							error();
						}
						((EventSendMessage) event).setCanceled();
					}
				}
			}
		}
	}
}