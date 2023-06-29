package ru.rockstar.client.features.impl.misc;

import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.other.DiscordUtils;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class DiscordRPC extends Feature {

    public DiscordRPC() {
        super("DiscordRPC","Показывает чит в профиле дискорда",0, Category.MISC);
    }

    @Override
    public void onEnable() {
        DiscordUtils.startRPC();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        DiscordUtils.stopRPC();
        super.onDisable();
    }
}
