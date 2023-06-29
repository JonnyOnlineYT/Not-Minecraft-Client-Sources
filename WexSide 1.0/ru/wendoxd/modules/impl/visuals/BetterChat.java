package ru.wendoxd.modules.impl.visuals;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class BetterChat extends Module {

	private Frame betterchat_frame = new Frame("BetterChat");
	private final CheckBox betterchat = new CheckBox("BetterChat").markArrayList("BetterChat");
	private String lastMessage = "";
	private int amount;
	private int line;

	@Override
	protected void initSettings() {
		betterchat_frame.register(betterchat);
		MenuAPI.hud.register(betterchat_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventReceivePacket && betterchat.isEnabled(false)) {
			if (((EventReceivePacket) event).getPacket() instanceof SPacketCloseWindow) {
				if (mc.currentScreen instanceof GuiChat) {
					((EventReceivePacket) event).setCanceled();
				}
			} else if (((EventReceivePacket) event).getPacket() instanceof SPacketChat
					&& ((SPacketChat) ((EventReceivePacket) event).getPacket()).func_192590_c() == ChatType.CHAT) {
				ITextComponent message = ((SPacketChat) ((EventReceivePacket) event).getPacket()).getChatComponent();
				String rawMessage = message.getFormattedText();
				if (lastMessage.equals(rawMessage)) {
					mc.ingameGUI.getChatGUI().deleteChatLine(line);
					amount++;
					((SPacketChat) ((EventReceivePacket) event).getPacket()).getChatComponent()
							.appendText(TextFormatting.GRAY + " [x" + amount + "]");
				} else {
					amount = 1;
				}
				line++;
				lastMessage = rawMessage;
				mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(message, line);
				if (line > 256) {
					line = 0;
				}
				((EventReceivePacket) event).setCanceled();
			}
		}
	}
}
