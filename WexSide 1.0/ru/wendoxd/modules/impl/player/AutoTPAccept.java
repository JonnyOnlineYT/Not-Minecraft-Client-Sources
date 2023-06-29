package ru.wendoxd.modules.impl.player;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import net.minecraft.network.play.server.SPacketChat;
import ru.wendoxd.WexSide;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.misc.TimerUtils;

public class AutoTPAccept extends Module {

	private final TimerUtils timerUtils = new TimerUtils();
	private Frame autotpaccept_frame = new Frame("AutoTPAccept");
	private final CheckBox autotpaccept = new CheckBox("AutoTPAccept").markArrayList("AutoTPAccept");
	private final CheckBox onlyfriends = new CheckBox("Only Friends", () -> autotpaccept.isEnabled(true));
	private Deque<String> messages = new ConcurrentLinkedDeque<String>();
	private Map<String, Integer> table = new HashMap();

	@Override
	protected void initSettings() {
		autotpaccept.markSetting("AutoTPAccept");
		autotpaccept_frame.register(autotpaccept, onlyfriends);
		MenuAPI.player.register(autotpaccept_frame);
		this.table.put("§7Заявка будет автоматически отменена через§r§6 120 секунд§r", 2);
		this.table.put("§6Заявка будет автоматически отменена через§r§c 15 секунд§r§6.§r", 4);
		this.table.put("§fТп автоматически отменится через§r§6 15 секунд§r§f.§r", 4);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventReceivePacket && autotpaccept.isEnabled(false)) {
			if (((EventReceivePacket) event).getPacket() instanceof SPacketChat) {
				try {
					SPacketChat packet = (SPacketChat) ((EventReceivePacket) event).getPacket();
					String m = packet.getChatComponent().getUnformattedText();
					StringBuilder builder = new StringBuilder();
					char[] buffer = m.toCharArray();
					for (int i = 0; i < buffer.length; i++) {
						if (buffer[i] == '§') {
							i++;
						} else {
							builder.append(buffer[i]);
						}
					}
					this.messages.add(builder.toString());
					int line;
					if ((line = this.table.getOrDefault(packet.getChatComponent().getFormattedText(), 999)) != 999) {
						String nickName = null;
						while (line > 0) {
							String msg = this.messages.pollLast();
							nickName = msg;
							line--;
						}
						if (nickName != null) {
							boolean isFriend = !onlyfriends.isEnabled(false);
							for (String friend : WexSide.friendManager.getFriends()) {
								String[] split = nickName.split(" ");
								for (String str : split) {
									if (friend.contains(str)) {
										isFriend = true;
										break;
									}
								}
							}
							if (isFriend) {
								mc.player.sendChatMessage("/tpaccept");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
