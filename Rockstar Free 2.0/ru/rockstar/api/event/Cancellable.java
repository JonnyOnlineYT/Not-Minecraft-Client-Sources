package ru.rockstar.api.event;

public interface Cancellable
{
    boolean isCancelled();
    
    void setCancelled(final boolean p0);
}
