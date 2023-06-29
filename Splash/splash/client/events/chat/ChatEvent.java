package splash.client.events.chat;

import me.hippo.systems.lwjeb.event.Cancelable;

/**
 * Author: Ice
 * Created: 23:50, 05-Jun-20
 * Project: Client
 */
public class ChatEvent extends Cancelable {

    private String chatMessage;

    public ChatEvent(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
}
