package ua.apraxia.eventapi.events.impl.render;

import ua.apraxia.eventapi.events.Event;

public class EventBossBar implements Event {
    public String bossName;

    public EventBossBar(String bossName) {
        this.bossName = bossName;
    }

}