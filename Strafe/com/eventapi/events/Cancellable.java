package com.eventapi.events;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean state);

}
