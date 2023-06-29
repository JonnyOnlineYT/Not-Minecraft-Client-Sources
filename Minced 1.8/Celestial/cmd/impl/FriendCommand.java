package Celestial.cmd.impl;

import Celestial.cmd.CommandAbstract;
import com.mojang.realmsclient.gui.ChatFormatting;
import Celestial.Smertnix;
import Celestial.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;


public class FriendCommand
        extends CommandAbstract {
    public FriendCommand() {
        super("friend", "friend list", "\u0412\u00a76.friend" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " add " + "\u0412\u00a73<nickname> | \u0412\u00a76.friend" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " del " + "\u0412\u00a73<nickname> | \u0412\u00a76.friend" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " list " + "| \u0412\u00a76.friend" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " clear", "friend");
    }

    @Override
    public void execute(String ... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equalsIgnoreCase("friend")) {
                    String name;
                    if (arguments[1].equalsIgnoreCase("add")) {
                        name = arguments[2];
                        if (name.equals(Minecraft.getMinecraft().player.getName())) {
                            ChatUtils.addChatMessage((Object)((Object)ChatFormatting.RED) + "You can't add yourself!");
                            return;
                        }
                        if (!Smertnix.instance.friendManager.isFriend(name)) {
                            Smertnix.instance.friendManager.addFriend(name);
                            ChatUtils.addChatMessage("Friend " + (Object)((Object)ChatFormatting.GREEN) + name + (Object)((Object)ChatFormatting.WHITE) + " successfully added to your friend list!");
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("del") && Smertnix.instance.friendManager.isFriend(name = arguments[2])) {
                        Smertnix.instance.friendManager.removeFriend(name);
                        ChatUtils.addChatMessage("Friend " + (Object)((Object)ChatFormatting.RED) + name + (Object)((Object)ChatFormatting.WHITE) + " deleted from your friend list!");
                    }
                    if (arguments[1].equalsIgnoreCase("clear")) {
                        if (Smertnix.instance.friendManager.getFriends().isEmpty()) {
                            ChatUtils.addChatMessage((Object)((Object)ChatFormatting.RED) + "Your friend list is empty!");
                            return;
                        }
                        Smertnix.instance.friendManager.getFriends().clear();
                        ChatUtils.addChatMessage("Your " + (Object)((Object)ChatFormatting.GREEN) + "friend list " + (Object)((Object)ChatFormatting.WHITE) + "was cleared!");
                    }
                    if (arguments[1].equalsIgnoreCase("list")) {
                        if (Smertnix.instance.friendManager.getFriends().isEmpty()) {
                            ChatUtils.addChatMessage((Object)((Object)ChatFormatting.RED) + "Your friend list is empty!");
                            return;
                        }
                        Smertnix.instance.friendManager.getFriends().forEach(friend -> ChatUtils.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Friend list: " + (Object)((Object)ChatFormatting.RED) + friend.getName()));
                    }
                }
            } else {
                ChatUtils.addChatMessage(this.getUsage());
            }
        }
        catch (Exception e2) {
            ChatUtils.addChatMessage("\u0412\u00a7cNo, no, no. Usage: " + this.getUsage());
        }
    }
}

