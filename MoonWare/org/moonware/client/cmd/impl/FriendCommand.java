package org.moonware.client.cmd.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.cmd.CommandAbstract;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class FriendCommand extends CommandAbstract {

    public FriendCommand() {
        super("friend", "friend list", "§6.friend" + Formatting.LIGHT_PURPLE + " add " + "§3<nickname> | §6.friend" + Formatting.LIGHT_PURPLE + " del " + "§3<nickname> | §6.friend" + Formatting.LIGHT_PURPLE + " list " + "| §6.friend" + Formatting.LIGHT_PURPLE + " clear", "friend");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equalsIgnoreCase("friend")) {
                    if (arguments[1].equalsIgnoreCase("add")) {
                        String name = arguments[2];
                        if (name.equals(Minecraft.player.getName())) {
                            MWUtils.sendChat(Formatting.RED + "You can't add yourself!");
                            NotificationManager.publicity("Friend Manager", "You can't add yourself!", 4, NotificationType.ERROR);
                            return;
                        }
                        if (!MoonWare.friendManager.isFriend(name)) {
                            MoonWare.friendManager.addFriend(name);
                            MWUtils.sendChat("Friend " + Formatting.GREEN + name + Formatting.WHITE + " successfully added to your friend list!");
                            NotificationManager.publicity("Friend Manager", "Friend " + Formatting.RED + name + Formatting.WHITE + " deleted from your friend list!", 4, NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("del")) {
                        String name = arguments[2];
                        if (MoonWare.friendManager.isFriend(name)) {
                            MoonWare.friendManager.removeFriend(name);
                            MWUtils.sendChat("Friend " + Formatting.RED + name + Formatting.WHITE + " deleted from your friend list!");
                            NotificationManager.publicity("Friend Manager", "Friend " + Formatting.RED + name + Formatting.WHITE + " deleted from your friend list!", 4, NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("clear")) {
                        if (MoonWare.friendManager.getFriends().isEmpty()) {
                            MWUtils.sendChat(Formatting.RED + "Your friend list is empty!");
                            NotificationManager.publicity("Friend Manager", "Your friend list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        MoonWare.friendManager.getFriends().clear();
                        MWUtils.sendChat("Your " + Formatting.GREEN + "friend list " + Formatting.WHITE + "was cleared!");
                        NotificationManager.publicity("Friend Manager", "Your " + Formatting.GREEN + "friend list " + Formatting.WHITE + "was cleared!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equalsIgnoreCase("list")) {
                        if (MoonWare.friendManager.getFriends().isEmpty()) {
                            MWUtils.sendChat(Formatting.RED + "Your friend list is empty!");
                            NotificationManager.publicity("Friend Manager", "Your friend list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        MoonWare.friendManager.getFriends().forEach(friend -> MWUtils.sendChat(Formatting.GREEN + "Friend list: " + Formatting.RED + friend.getName()));
                    }
                }
            } else {
                MWUtils.sendChat(getUsage());
            }
        } catch (Exception e) {
            MWUtils.sendChat("§cNo, no, no. Usage: " + getUsage());
        }
    }
}
