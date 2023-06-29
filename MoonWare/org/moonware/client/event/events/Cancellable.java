package org.moonware.client.event.events;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean state);

}
