package Celestial.utils.otherutils.gayutil;

import Celestial.event.events.Event;

public class MouseEvent implements Event {

    public int button;

    public MouseEvent(int button) {
        this.button = button;
    }

}
