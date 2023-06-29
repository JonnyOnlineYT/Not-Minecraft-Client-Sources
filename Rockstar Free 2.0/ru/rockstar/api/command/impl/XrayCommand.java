package ru.rockstar.api.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.client.features.impl.visuals.XRay;


public class XrayCommand extends CommandAbstract {
   public static ArrayList<Integer> blockIDS = new ArrayList();

   public XrayCommand() {
      super("xray", "xray", "§6.xray add §3<blockId> | §6.xray remove §3<blockId> | §6.xray list | §6.xray clear", "xray");
   }

   public void execute(String... arguments) {
      if (arguments.length >= 2) {
         if (!Main.instance.featureDirector.getFeatureByClass(XRay.class).isToggled()) {
            Main.msg(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "please enable XRay module!", true);
            NotificationPublisher.queue("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "please enable XRay module!", NotificationType.SUCCESS);
            return;
         }

         if (arguments[0].equalsIgnoreCase("xray")) {
            if (arguments[1].equalsIgnoreCase("add")) {
               if (!arguments[2].isEmpty()) {
                  if (!blockIDS.contains(Integer.parseInt(arguments[2]))) {
                     blockIDS.add(Integer.parseInt(arguments[2]));
                     Main.msg(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "added block: " + ChatFormatting.RED + "\"" + Block.getBlockById(Integer.parseInt(arguments[2])).getLocalizedName() + "\"", true);
                     NotificationPublisher.queue("XrayManager", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "added block: " + ChatFormatting.RED + "\"" + Block.getBlockById(Integer.parseInt(arguments[2])).getLocalizedName() + "\"", NotificationType.SUCCESS);
                  } else {
                     Main.msg(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block already have in list!", true);
                     NotificationPublisher.queue("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block already have in list!", NotificationType.SUCCESS);
                  }
               }
            } else if (arguments[1].equalsIgnoreCase("remove")) {
               if (blockIDS.contains(new Integer(arguments[2]))) {
                  blockIDS.remove(new Integer(arguments[2]));
                  Main.msg(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "removed block by id " + Integer.parseInt(arguments[2]), true);
                  NotificationPublisher.queue("XrayManager", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "removed block by id " + Integer.parseInt(arguments[2]), NotificationType.SUCCESS);
               } else {
                  Main.msg(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block doesn't contains in your list!", true);
                  NotificationPublisher.queue("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block doesn't contains in your list!", NotificationType.SUCCESS);
               }
            } else if (arguments[1].equalsIgnoreCase("list")) {
               if (!blockIDS.isEmpty()) {
                  Iterator var2 = blockIDS.iterator();

                  while(var2.hasNext()) {
                     Integer integer = (Integer)var2.next();
                     Main.msg(ChatFormatting.RED + Block.getBlockById(integer).getLocalizedName() + ChatFormatting.LIGHT_PURPLE + " ID: " + integer, true);
                  }
               } else {
                  Main.msg(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!", true);
                  NotificationPublisher.queue("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!", NotificationType.SUCCESS);
               }
            } else if (arguments[1].equalsIgnoreCase("clear")) {
               if (blockIDS.isEmpty()) {
                  Main.msg(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!", true);
                  NotificationPublisher.queue("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!", NotificationType.SUCCESS);
               } else {
                  blockIDS.clear();
                  Main.msg(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "clear block list!", true);
                  NotificationPublisher.queue("XrayManager", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "clear block list!", NotificationType.SUCCESS);
               }
            }
         }
      }

   }
}
