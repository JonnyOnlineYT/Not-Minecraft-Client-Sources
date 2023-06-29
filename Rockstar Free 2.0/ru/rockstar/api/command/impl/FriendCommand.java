package ru.rockstar.api.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;

public class FriendCommand extends CommandAbstract {

    public FriendCommand() {
        super("friend", "friend list", "§6.friend" + ChatFormatting.RED + " add " + "§7<nickname> | §6.friend" + ChatFormatting.RED + " del " + "§7<nickname> | §6.friend" + ChatFormatting.RED + " list " + "| §6.friend" + ChatFormatting.RED + " clear", "friend");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equalsIgnoreCase("friend")) {
                    if (arguments[1].equalsIgnoreCase("add")) {
                        String name = arguments[2];
                        if (name.equals(mc.player.getName())) {
                            NotificationPublisher.queue("Friend Manager", "You can't add yourself!",  NotificationType.ERROR);
                            return;
                        }
                        if (!Main.instance.friendManager.isFriend(name)) {
                            Main.instance.friendManager.addFriend(name);
                            NotificationPublisher.queue("Friend Manager", "Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!", NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("del")) {
                        String name = arguments[2];
                        if (Main.instance.friendManager.isFriend(name)) {
                            Main.instance.friendManager.removeFriend(name);
                            NotificationPublisher.queue("Friend Manager", "Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!",  NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("clear")) {
                        if (Main.instance.friendManager.getFriends().isEmpty()) {
                            Main.msg(ChatFormatting.RED + "Your friend list is empty!",true);
                            NotificationPublisher.queue("Friend Manager", "Your friend list is empty!", NotificationType.ERROR);
                            return;
                        }
                        Main.instance.friendManager.getFriends().clear();
                        Main.msg("Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!",true);
                        NotificationPublisher.queue("Friend Manager", "Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!", NotificationType.SUCCESS);
                    }
                    if (arguments[1].equalsIgnoreCase("list")) {
                        if (Main.instance.friendManager.getFriends().isEmpty()) {
                            NotificationPublisher.queue("Friend Manager", "Your friend list is empty!", NotificationType.ERROR);
                            return;
                        }
                        Main.instance.friendManager.getFriends().forEach(friend -> Main.msg(ChatFormatting.GREEN + "Friend list: " + ChatFormatting.RED + friend.getName(),true));
                    }
                }
            } else {
                Main.msg(getUsage(),true);
            }
        } catch (Exception e) {
            Main.msg("§cNo, no, no. Usage: " + getUsage(),true);
        }
    }
}
