package ru.wendoxd.events.impl.render;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventGameOverlay extends Event implements IEventCancelable {

    private boolean canceled;
    private final OverlayType overlayType;

    public EventGameOverlay(OverlayType overlayType) {
        this.overlayType = overlayType;
    }

    public OverlayType getOverlayType() {
        return overlayType;
    }

    @Override
    public void setCanceled() {
        canceled = true;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    public enum OverlayType {
        Hurt, PumpkinOverlay, TotemPop, CameraBounds, Fire, Light, BossBar, ChatRect, Fog, WaterFog, LavaFog
    }
}
