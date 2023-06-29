package ru.fluger.client.feature.impl.hud;

import ru.fluger.client.Fluger;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;

public class Notifications
extends Feature {
    public Notifications() {
        super("Notifications", "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", Type.Hud);
    }

    @Override
    public void onEnable() {
        Fluger.instance.notifications.getList().clear();
        super.onEnable();
    }
}