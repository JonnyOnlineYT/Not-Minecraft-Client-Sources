package splash.api.event.events.key;

import me.hippo.systems.lwjeb.event.Cancelable;

/**
 * Author: Ice
 * Created: 17:38, 30-May-20
 * Project: Client
 */
public class KeyEvent extends Cancelable {

    private int pressedKey;

    public KeyEvent(int pressedKey) {
        this.pressedKey = pressedKey;
    }

    public int getPressedKey() {
        return pressedKey;
    }

    public void setPressedKey(int pressedKey) {
        this.pressedKey = pressedKey;
    }
}
