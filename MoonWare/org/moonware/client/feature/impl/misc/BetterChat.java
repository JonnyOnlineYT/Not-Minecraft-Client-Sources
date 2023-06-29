package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Formatting;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class BetterChat extends Feature {

    private String lastMessage = "";
    private int amount;
    private int line;

    public BetterChat() {
        super("BetterChat", "Убирает спам", Type.Other);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        SPacketChat sPacketChat;
        Packet<?> packet = event.getPacket();
        if (packet instanceof SPacketCloseWindow) {
            if (Minecraft.screen instanceof ChatScreen) {
                event.setCancelled(true);
            }
        } else if (packet instanceof SPacketChat && (sPacketChat = (SPacketChat) packet).getChatType() == ChatType.CHAT) {
            Component message = sPacketChat.getChatComponent();
            String rawMessage = message.asFormattedString();
            ChatHud chatGui = Minecraft.ingameGUI.getChatGUI();
            if (lastMessage.equals(rawMessage)) {
                chatGui.deleteChatLine(line);
                ++amount;
                sPacketChat.getChatComponent().append(Formatting.GRAY + " [x" + amount + "]");
            } else {
                amount = 1;
            }
            ++line;
            lastMessage = rawMessage;
            chatGui.printChatMessageWithOptionalDeletion(message, line);
            if (line > 256) {
                line = 0;
            }
            event.setCancelled(true);
        }
    }
}
