package ru.rockstar.client.features.impl.misc;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ListSetting;

public class ModuleSoundAlert extends Feature {
	public static ListSetting soundMode;
    public ModuleSoundAlert() {
        super("ModuleSoundAlert","Звуки при включении функции и выключении",0, Category.MISC);
        soundMode = new ListSetting("Sound Mode", "Rockstar", () -> true, "Minecraft", "Rockstar");
        this.addSettings(soundMode);
    }
}
