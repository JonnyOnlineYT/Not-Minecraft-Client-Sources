package ru.rockstar.api.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.client.features.impl.misc.FakeHack;

public class FakeHackCommand extends CommandAbstract
{
    public FakeHackCommand() {
        super("fakehack", "fakehack", "§6.fakehack" + ChatFormatting.LIGHT_PURPLE + " add | del | clear§3<name>", new String[] { "§6.fakehack" + ChatFormatting.LIGHT_PURPLE + " add | del | clear §3<name>", "fakehack" });
    }
    
    @Override
    public void execute(final String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("fakehack")) {
                    if (arguments[1].equals("add")) {
                        FakeHack.fakeHackers.add(arguments[2]);
                        NotificationPublisher.queue("FakeHack Manager", ChatFormatting.GREEN + "Added player " + ChatFormatting.RED + arguments[2] + ChatFormatting.WHITE + " as HACKER!", NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("del")) {
                        final EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByName(arguments[2]);
                        if (player == null) {
                            Main.msg("§cThat player could not be found!", true);
                            return;
                        }
                        if (FakeHack.isFakeHacker(player)) {
                            FakeHack.removeHacker(player);
                            NotificationPublisher.queue("FakeHack Manager", ChatFormatting.GREEN + "Hacker " + ChatFormatting.WHITE + "was removed!", NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equals("clear")) {
                        if (FakeHack.fakeHackers.isEmpty()) {
                            NotificationPublisher.queue("FakeHack Manager", "Your FakeHack list is empty!", NotificationType.ERROR);
                            return;
                        }
                        FakeHack.fakeHackers.clear();
                        NotificationPublisher.queue("FakeHack Manager", ChatFormatting.GREEN + "Your FakeHack list " + ChatFormatting.WHITE + " successfully cleared!", NotificationType.SUCCESS);
                    }
                }
            }
            else {
                Main.msg(this.getUsage(), true);
            }
        }
        catch (Exception ex) {}
    }
}
