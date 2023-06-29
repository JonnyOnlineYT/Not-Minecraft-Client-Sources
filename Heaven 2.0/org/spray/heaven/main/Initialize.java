package org.spray.heaven.main;

import org.spray.heaven.features.command.CommandRegister;
import org.spray.heaven.features.misc.FriendManager;
import org.spray.heaven.features.misc.MacrosManager;
import org.spray.heaven.features.module.ModuleRegister;
import org.spray.heaven.notifications.NotificationManager;
import org.spray.heaven.ui.altscreens.AltScreen;
import org.spray.heaven.util.file.config.ConfigRegister;

public class Initialize {

    protected static NotificationManager notification;
    protected static ModuleRegister module;
    protected static CommandRegister command;
    protected static ConfigRegister config;
    protected static FriendManager friend;
    public static AltScreen alt = new AltScreen();
    protected static MacrosManager macros;

}
