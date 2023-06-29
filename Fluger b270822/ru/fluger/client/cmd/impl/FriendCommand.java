/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package ru.fluger.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import ru.fluger.client.Fluger;
import ru.fluger.client.cmd.CommandAbstract;
import ru.fluger.client.helpers.misc.ChatHelper;

public class FriendCommand
extends CommandAbstract {
    public FriendCommand() {
        super("friend", "friend list", "\ufffd6.friend" + (Object)ChatFormatting.WHITE + " add \ufffd3<nickname>" + (Object)ChatFormatting.WHITE + " | \ufffd6.friend" + (Object)ChatFormatting.WHITE + " del \ufffd3<nickname>" + (Object)ChatFormatting.WHITE + " | \ufffd6.friend" + (Object)ChatFormatting.WHITE + " list " + (Object)ChatFormatting.WHITE + "| \ufffd6.friend" + (Object)ChatFormatting.WHITE + " clear", "friend");
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
                            ChatHelper.addChatMessage((Object)ChatFormatting.RED + "You can't add yourself!");
                            return;
                        }
                        if (!Fluger.instance.friendManager.isFriend(name)) {
                            Fluger.instance.friendManager.addFriend(name);
                            ChatHelper.addChatMessage("Friend " + (Object)ChatFormatting.GREEN + name + (Object)ChatFormatting.WHITE + " successfully added to your friend list!");
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("del") && Fluger.instance.friendManager.isFriend(name = arguments[2])) {
                        Fluger.instance.friendManager.removeFriend(name);
                        ChatHelper.addChatMessage("Friend " + (Object)ChatFormatting.RED + name + (Object)ChatFormatting.WHITE + " deleted from your friend list!");
                    }
                    if (arguments[1].equalsIgnoreCase("clear")) {
                        if (Fluger.instance.friendManager.getFriends().isEmpty()) {
                            ChatHelper.addChatMessage((Object)ChatFormatting.RED + "Your friend list is empty!");
                            return;
                        }
                        Fluger.instance.friendManager.getFriends().clear();
                        ChatHelper.addChatMessage("Your " + (Object)ChatFormatting.GREEN + "friend list " + (Object)ChatFormatting.WHITE + "was cleared!");
                    }
                    if (arguments[1].equalsIgnoreCase("list")) {
                        if (Fluger.instance.friendManager.getFriends().isEmpty()) {
                            ChatHelper.addChatMessage((Object)ChatFormatting.RED + "Your friend list is empty!");
                            return;
                        }
                        Fluger.instance.friendManager.getFriends().forEach(friend -> ChatHelper.addChatMessage((Object)ChatFormatting.GREEN + "Friend list: " + (Object)ChatFormatting.RED + friend.getName()));
                    }
                }
            } else {
                ChatHelper.addChatMessage(this.getUsage());
            }
        }
        catch (Exception e) {
            ChatHelper.addChatMessage("\ufffdcNo, no, no. Usage: " + this.getUsage());
        }
    }
}

