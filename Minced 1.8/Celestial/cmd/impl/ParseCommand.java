package Celestial.cmd.impl;


import Celestial.cmd.CommandAbstract;
import com.mojang.realmsclient.gui.ChatFormatting;
import Celestial.ui.notif.NotifModern;
import Celestial.ui.notif.NotifRender;
import Celestial.utils.other.ChatUtils;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.lwjgl.Sys;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ParseCommand
        extends CommandAbstract {
    public ParseCommand() {
        super("parser", "parser", (Object)((Object)ChatFormatting.RED) + ".parser" + (Object)((Object)ChatFormatting.WHITE) + " parse | dir", "parser");
    }

    @Override
    public void execute(String ... args) {
        if (args.length >= 2) {
            String upperCase = args[1].toUpperCase();
            if (args.length == 2 && upperCase.equalsIgnoreCase("PARSE")) {
                try {
                    List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy(ParseCommand.mc.player.connection.getPlayerInfoMap());
                    File fileFolder = new File("C://Minced/configs/", "parser");
                    if (!fileFolder.exists()) {
                        fileFolder.mkdirs();
                    }
                    File file = new File("C://Minced/configs/parser/", String.valueOf(ParseCommand.mc.getCurrentServerData().serverIP.split(":")[0]) + ".txt");
                    BufferedWriter out = new BufferedWriter(new FileWriter(file));
                    if (file.exists()) {
                        file.delete();
                    } else {
                        file.createNewFile();
                    }
                    for (NetworkPlayerInfo n : players) {
                        if (n.getPlayerTeam().getColorPrefix().length() <= 3) continue;
                        out.write(String.valueOf(n.getPlayerTeam().getColorPrefix()) + " : " + n.getGameProfile().getName());
                        out.write("\r\n");
                    }
                    out.close();
                    ChatUtils.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully parsed! " + (Object)((Object)ChatFormatting.WHITE) + "please check your game directory");
                    NotifRender.queue((Object)((Object)ChatFormatting.GREEN) + "Parse Manager", (Object)((Object)ChatFormatting.GREEN) + "Successfully parsed! " + (Object)((Object)ChatFormatting.WHITE) + "please check your game directory", 5, NotifModern.SUCCESS);
                }
                catch (Exception players) {}
            } else if (args.length == 2 && upperCase.equalsIgnoreCase("DIR")) {
                File file = new File("C:\\Minced\\configs\\parser");
                Sys.openURL(file.getAbsolutePath());
            }
        } else {
            ChatUtils.addChatMessage(this.getUsage());
        }
    }
}

