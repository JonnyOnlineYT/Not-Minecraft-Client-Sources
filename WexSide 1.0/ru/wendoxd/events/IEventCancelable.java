package ru.wendoxd.events;

public interface IEventCancelable {
    void setCanceled();

    boolean isCanceled();
}
