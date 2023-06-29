package org.moonware.client.cmd.impl;

import net.minecraft.block.Block;
import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.cmd.CommandAbstract;
import org.moonware.client.feature.impl.visual.XRay;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

import java.util.ArrayList;

public class XrayCommand extends CommandAbstract {

    public static ArrayList<Integer> blockIDS = new ArrayList<>();

    public XrayCommand() {
        super("xray", "xray", "§6.xray" + Formatting.LIGHT_PURPLE + " add " + "§3<blockId> | §6.xray" + Formatting.LIGHT_PURPLE + " remove " + "§3<blockId> | §6.xray" + Formatting.LIGHT_PURPLE + " list | §6.xray" + Formatting.LIGHT_PURPLE + " clear", "xray");
    }

    @Override
    public void execute(String... arguments) {
        if (arguments.length >= 2) {
            if (!MoonWare.featureManager.getFeatureByClass(XRay.class).getState()) {
                MWUtils.sendChat(Formatting.RED + "Error " + Formatting.WHITE + "please enable XRay module!");
                NotificationManager.publicity("XrayManager", Formatting.RED + "Error " + Formatting.WHITE + "please enable XRay module!", 4, NotificationType.SUCCESS);
                return;
            }
            if (arguments[0].equalsIgnoreCase("xray")) {
                if (arguments[1].equalsIgnoreCase("add")) {
                    if (!arguments[2].isEmpty()) {
                        if (!blockIDS.contains(Integer.parseInt(arguments[2]))) {
                            blockIDS.add(Integer.parseInt(arguments[2]));
                            MWUtils.sendChat(Formatting.GREEN + "Successfully " + Formatting.WHITE + "added block: " + Formatting.RED + "\"" + Block.getBlockById(Integer.parseInt(arguments[2])).getLocalizedName() + "\"");
                            NotificationManager.publicity("XrayManager", Formatting.GREEN + "Successfully " + Formatting.WHITE + "added block: " + Formatting.RED + "\"" + Block.getBlockById(Integer.parseInt(arguments[2])).getLocalizedName() + "\"", 4, NotificationType.SUCCESS);
                        } else {
                            MWUtils.sendChat(Formatting.RED + "Error " + Formatting.WHITE + "this block already have in list!");
                            NotificationManager.publicity("XrayManager", Formatting.RED + "Error " + Formatting.WHITE + "this block already have in list!", 4, NotificationType.SUCCESS);
                        }
                    }
                } else if (arguments[1].equalsIgnoreCase("remove")) {
                    if (blockIDS.contains(new Integer(arguments[2]))) {
                        blockIDS.remove(new Integer(arguments[2]));
                        MWUtils.sendChat(Formatting.GREEN + "Successfully " + Formatting.WHITE + "removed block by id " + Integer.parseInt(arguments[2]));
                        NotificationManager.publicity("XrayManager", Formatting.GREEN + "Successfully " + Formatting.WHITE + "removed block by id " + Integer.parseInt(arguments[2]), 4, NotificationType.SUCCESS);
                    } else {
                        MWUtils.sendChat(Formatting.RED + "Error " + Formatting.WHITE + "this block doesn't contains in your list!");
                        NotificationManager.publicity("XrayManager", Formatting.RED + "Error " + Formatting.WHITE + "this block doesn't contains in your list!", 4, NotificationType.SUCCESS);
                    }
                } else if (arguments[1].equalsIgnoreCase("list")) {
                    if (!blockIDS.isEmpty()) {
                        for (Integer integer : blockIDS) {
                            MWUtils.sendChat(Formatting.RED + Block.getBlockById(integer).getLocalizedName() + Formatting.LIGHT_PURPLE + " ID: " + integer);
                        }
                    } else {
                        MWUtils.sendChat(Formatting.RED + "Error " + Formatting.WHITE + "your block list is empty!");
                        NotificationManager.publicity("XrayManager", Formatting.RED + "Error " + Formatting.WHITE + "your block list is empty!", 4, NotificationType.SUCCESS);
                    }
                } else if (arguments[1].equalsIgnoreCase("clear")) {
                    if (blockIDS.isEmpty()) {
                        MWUtils.sendChat(Formatting.RED + "Error " + Formatting.WHITE + "your block list is empty!");
                        NotificationManager.publicity("XrayManager", Formatting.RED + "Error " + Formatting.WHITE + "your block list is empty!", 4, NotificationType.SUCCESS);
                    } else {
                        blockIDS.clear();
                        MWUtils.sendChat(Formatting.GREEN + "Successfully " + Formatting.WHITE + "clear block list!");
                        NotificationManager.publicity("XrayManager", Formatting.GREEN + "Successfully " + Formatting.WHITE + "clear block list!", 4, NotificationType.SUCCESS);
                    }
                }
            }
        }
    }
}
