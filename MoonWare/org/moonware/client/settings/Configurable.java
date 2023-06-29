package org.moonware.client.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Configurable {

    private final ArrayList<Setting> settingList = new ArrayList<>();

    public final void addSettings(Setting... options) {
        settingList.addAll(Arrays.asList(options));
    }

    public final List<Setting> getSettings() {
        return settingList;
    }
    public String getOptions() {
        return String.valueOf(settingList);
    }
}
