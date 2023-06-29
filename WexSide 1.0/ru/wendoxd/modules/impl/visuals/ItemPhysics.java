package ru.wendoxd.modules.impl.visuals;

import ru.wendoxd.events.Event;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class ItemPhysics extends Module {

    private Frame itemphysics_frame = new Frame("ItemPhysics");
    public static CheckBox itemphysics = new CheckBox("ItemPhysics");

    @Override
    protected void initSettings() {
        itemphysics_frame.register(itemphysics.markSetting("ItemPhysics"));
        MenuAPI.visuals.register(itemphysics_frame);
    }

    @Override
    public void onEvent(Event event) {

    }
}
