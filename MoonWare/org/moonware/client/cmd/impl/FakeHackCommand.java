package org.moonware.client.cmd.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.cmd.CommandAbstract;
import org.moonware.client.feature.impl.misc.FakeHack;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class FakeHackCommand extends CommandAbstract {

    public FakeHackCommand() {
        super("fakehack", "fakehack", "§6.fakehack" + Formatting.LIGHT_PURPLE + " add | remove | clear" + "§3<name>", "§6.fakehack" + Formatting.LIGHT_PURPLE + " add | del | clear" + "§3<name>", "fakehack");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("fakehack")) {
                    if (arguments[1].equals("add")) {
                        FakeHack.fakeHackers.add(arguments[2]);
                        MWUtils.sendChat(Formatting.GREEN + "Added" + " player " + Formatting.RED + arguments[2] + Formatting.WHITE + " as HACKER!");
                        NotificationManager.publicity("FakeHack Manager", Formatting.GREEN + "Added" + " player " + Formatting.RED + arguments[2] + Formatting.WHITE + " as HACKER!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("remove")) {
                        EntityPlayer player = Minecraft.world.getPlayerEntityByName(arguments[2]);
                        if (player == null) {
                            MWUtils.sendChat("§cThat player could not be found!");
                            return;
                        }
                        if (FakeHack.isFakeHacker(player)) {
                            FakeHack.removeHacker(player);
                            MWUtils.sendChat(Formatting.GREEN + "Hacker " + Formatting.RED + player.getName() + " " + Formatting.WHITE + "was removed!");
                            NotificationManager.publicity("FakeHack Manager", Formatting.GREEN + "Hacker " + Formatting.WHITE + "was removed!", 4, NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equals("clear")) {
                        if (FakeHack.fakeHackers.isEmpty()) {
                            MWUtils.sendChat(Formatting.RED + "Your FakeHack list is empty!");
                            NotificationManager.publicity("FakeHack Manager", "Your FakeHack list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        FakeHack.fakeHackers.clear();
                        MWUtils.sendChat(Formatting.GREEN + "Your FakeHack list " + Formatting.WHITE + " successfully cleared!");
                        NotificationManager.publicity("FakeHack Manager", Formatting.GREEN + "Your FakeHack list " + Formatting.WHITE + " successfully cleared!", 4, NotificationType.SUCCESS);
                    }
                }
            } else {
                MWUtils.sendChat(getUsage());
            }
        } catch (Exception ignored) {

        }
    }
}
