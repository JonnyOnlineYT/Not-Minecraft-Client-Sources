package ru.wendoxd.modules.impl.player;

import net.minecraft.network.play.server.SPacketChat;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class AutoRegister extends Module {

	private Frame autoregister_frame = new Frame("AutoRegister");
	private final CheckBox autoregister = new CheckBox("AutoRegister").markArrayList("AutoRegister");
	private final String password = "qwerty56789";

	@Override
	protected void initSettings() {
		autoregister_frame.register(autoregister);
		MenuAPI.player.register(autoregister_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventReceivePacket && autoregister.isEnabled(false)) {
			if (((EventReceivePacket) event).getPacket() instanceof SPacketChat) {
				SPacketChat packet = (SPacketChat) ((EventReceivePacket) event).getPacket();
				String unformattedText = packet.getChatComponent().getUnformattedText();

				if (unformattedText.contains("/reg") || unformattedText.contains("/register")
						|| unformattedText.contains("Зарегистрируйтесь")) {
					mc.player.sendChatMessage("/reg " + password + " " + password);
				} else if (unformattedText.contains("Авторизуйтесь") || unformattedText.contains("/l")) {
					mc.player.sendChatMessage("/login " + password);
				}
			}
		}
	}
}
