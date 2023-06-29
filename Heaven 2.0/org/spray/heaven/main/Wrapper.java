package org.spray.heaven.main;

import java.net.URI;

import org.spray.heaven.features.command.CommandRegister;
import org.spray.heaven.features.misc.FriendManager;
import org.spray.heaven.features.misc.MacrosManager;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleRegister;
import org.spray.heaven.font.IFont;
import org.spray.heaven.notifications.Notification.Type;
import org.spray.heaven.notifications.NotificationManager;
import org.spray.heaven.ui.clickui.ClickUI;
import org.spray.heaven.ui.draggable.DragManager;
import org.spray.heaven.ui.draggable.Dragging;
import org.spray.heaven.ui.menu.MenuScreen;
import org.spray.heaven.util.file.config.ConfigRegister;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.Packet;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.world.World;

public class Wrapper extends Initialize {

    public static Minecraft MC = Minecraft.getMinecraft();
    public static ScaledResolution RESOLUTION = new ScaledResolution(MC);
    public static ClickUI UI;
    public static MenuScreen MENU;

    public static void message(String text) {
        MC.ingameGUI.getChatGUI().printChatMessage(
                new TextComponentKeybind(ChatFormatting.GREEN + Heaven.NAME + " => " + ChatFormatting.GRAY + text));
    }

    public static void notify(String text, Type type) {
        if (Wrapper.getModule().get("Notifications").isEnabled())
            getNotification().call(text, type, IFont.WEB_SETTINGS);
        else
            message(text);
    }

    public static void sendPacket(Packet<?> packetIn) {
        getPlayer().connection.sendPacket(packetIn);
    }

    //
    public static EntityPlayerSP getPlayer() {
        return MC.player;
    }

    public static World getWorld() {
        return MC.world;
    }

    public static FontRenderer getFont() {
        return MC.fontRendererObj;
    }

    public static ModuleRegister getModule() {
        return module;
    }

    public static CommandRegister getCommand() {
        return command;
    }

    public static ConfigRegister getConfig() {
        return config;
    }

    public static FriendManager getFriend() {
        return friend;
    }

    public static MacrosManager getMacros() {
        return macros;
    }

    public static NotificationManager getNotification() {
        return notification;
    }

    public static void setNotification(NotificationManager notification) {
        Initialize.notification = notification;
    }

    public static void setModule(ModuleRegister module) {
        Initialize.module = module;
    }

    public static void setCommand(CommandRegister command) {
        Initialize.command = command;
    }

    public static void setConfig(ConfigRegister config) {
        Initialize.config = config;
    }

    public static void setFriend(FriendManager friend) {
        Initialize.friend = friend;
    }

    public static void setMacros(MacrosManager macros) {
        Initialize.macros = macros;
    }

    // From GuiScreen openWebLink() method
    public static void openLink(URI url) {
        try {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{url});
        } catch (Throwable throwable1) {
        }
    }

    public static Dragging createDrag(Module module, String name, float x, float y) {
        DragManager.draggables.put(name, new Dragging(module, name, x, y));
        return DragManager.draggables.get(name);
    }

}
