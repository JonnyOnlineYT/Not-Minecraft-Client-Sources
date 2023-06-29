package de.strafe.events;

import com.eventapi.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author XButtonn
 * @created 21.02.2022 - 12:25
 */

@AllArgsConstructor
@Getter
@Setter
public class EventPreMotion implements Event {

    private float yaw;
    private float pitch;
    private boolean onGround;

}
