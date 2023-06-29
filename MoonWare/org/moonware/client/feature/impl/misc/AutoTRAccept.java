package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketChat;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.Event;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.friend.Friend;
import org.moonware.client.settings.impl.BooleanSetting;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;

public class AutoTRAccept extends Feature {
    private Map<String, Integer> table = new HashMap();
    private BooleanSetting onlyfriends = new BooleanSetting("Only Friends", false);
    private Deque<String> messages = new ConcurrentLinkedDeque<String>();
    public AutoTRAccept() {
        super("AutoTPaccept", "", Type.Other);
        table.put("§7Заявка будет автоматически отменена через§r§6 120 секунд§r", 2);
        table.put("§6Заявка будет автоматически отменена через§r§c 15 секунд§r§6.§r", 4);
        table.put("§fТп автоматически отменится через§r§6 15 секунд§r§f.§r", 4);
        addSettings(onlyfriends);
    }

    @EventTarget
    public void onEvent(Event event) {
        if (event instanceof EventReceivePacket && getState()) {
            if (((EventReceivePacket) event).getPacket() instanceof SPacketChat) {
                try {
                    SPacketChat packet = (SPacketChat) ((EventReceivePacket) event).getPacket();
                    String m = packet.getChatComponent().asString();
                    StringBuilder builder = new StringBuilder();
                    char[] buffer = m.toCharArray();
                    for (int i = 0; i < buffer.length; i++) {
                        if (buffer[i] == '§') {
                            i++;
                        } else {
                            builder.append(buffer[i]);
                        }
                    }
                    messages.add(builder.toString());
                    int line;
                    if ((line = table.getOrDefault(packet.getChatComponent().asFormattedString(), 999)) != 999) {
                        String nickName = null;
                        while (line > 0) {
                            String msg = messages.pollLast();
                            nickName = msg;
                            line--;
                        }
                        if (nickName != null) {
                            boolean isFriend = !onlyfriends.isEnabled(false);
                            for (Friend friend : MoonWare.friendManager.getFriends()) {
                                String[] split = nickName.split(" ");
                                for (String str : split) {
                                    if (Objects.equals(friend.getName(), str)) {
                                        isFriend = true;
                                        break;
                                    }
                                }
                            }
                            if (isFriend) {
                                Minecraft.player.sendChatMessage("/tpaccept");
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
