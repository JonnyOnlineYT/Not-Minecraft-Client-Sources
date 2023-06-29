package splash.client.events.key;

import me.hippo.systems.lwjeb.event.Cancelable;

/**
 * Author: Ice
 * Created: 17:38, 30-May-20
 * Project: Client
 */
public class EventKey extends Cancelable {

    private int pressedKey;

    public EventKey(int pressedKey) {
        this.pressedKey = pressedKey;
    }

    public int getPressedKey() {
        return pressedKey;
    }

    public void setPressedKey(int pressedKey) {
        this.pressedKey = pressedKey;
    }
}
