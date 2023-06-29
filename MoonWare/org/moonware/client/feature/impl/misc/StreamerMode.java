package org.moonware.client.feature.impl.misc;

import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;

public class StreamerMode extends Feature {

    public static BooleanSetting ownName;
    public static BooleanSetting otherNames;
    public static BooleanSetting skinSpoof;
    public static BooleanSetting tabSpoof;
    public static BooleanSetting friendNames;
    public static BooleanSetting scoreBoardSpoof;
    public static BooleanSetting warpSpoof;
    public static BooleanSetting loginSpoof;

    public StreamerMode() {
        super("NameProtect", "Позволяет скрывать информацию о себе и других игроках на видео или стриме", Type.Other);
        ownName = new BooleanSetting("Own Name", true, () -> true);
        otherNames = new BooleanSetting("Other Names", true, () -> true);
        tabSpoof = new BooleanSetting("Tab Spoof", true, () -> true);
        friendNames = new BooleanSetting("Friend Names", false, () -> true);
        skinSpoof = new BooleanSetting("Skin Spoof", true, () -> true);
        scoreBoardSpoof = new BooleanSetting("ScoreBoard Spoof", true, () -> true);
        warpSpoof = new BooleanSetting("Warp Spoof", true, () -> true);
        loginSpoof = new BooleanSetting("Login Spoof", true, () -> true);
        addSettings(ownName, friendNames, otherNames, tabSpoof, skinSpoof, scoreBoardSpoof, warpSpoof, loginSpoof);
    }
    public boolean isEnabled() {
        return getState();
    }
}