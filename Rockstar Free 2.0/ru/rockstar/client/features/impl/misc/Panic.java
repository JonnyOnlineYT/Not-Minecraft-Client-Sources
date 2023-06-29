package ru.rockstar.client.features.impl.misc;

import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class Panic extends Feature {
    public Panic() {
        super("Panic", "Выключает все модули чита", 0, Category.MISC);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (Feature feature : Main.instance.featureDirector.getFeatureList()) {
            if (feature.isToggled()) {
                feature.toggle();
            }
        }
    }
}
