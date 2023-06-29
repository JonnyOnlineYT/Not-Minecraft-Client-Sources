package org.moonware.client.feature.impl.hud.ArrayGlowComp;

import org.moonware.client.event.events.Event;

public class ShaderEvent implements Event {
    public final boolean bloom;
    public ShaderEvent(boolean bloom){
        this.bloom = bloom;
    }
}
