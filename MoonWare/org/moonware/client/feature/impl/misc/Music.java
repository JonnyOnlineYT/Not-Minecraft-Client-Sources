package org.moonware.client.feature.impl.misc;

import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;

public class Music extends Feature {
    public static ListSetting buildnumber = new ListSetting("music number", "music1", () -> true, "music1", "music2", "music3", "music3", "music4", "music5", "music6", "music7", "music8", "music9", "music10", "music11", "music12", "music13", "music14", "music15");
    public static BooleanSetting Musics = new BooleanSetting("ready", "готовность к запуску, лучше не изменять", true, () -> true);
    public static boolean stat;

    public Music() {
        super("MusicPlayer", "Проигрывает вашу заданную музыку wav формата", Type.Other);
        addSettings(buildnumber);
    }

    public void onEnable() {
        stat = true;
        stat = false;
    }
    public void onDisable() {
        stat = false;
    }
}

