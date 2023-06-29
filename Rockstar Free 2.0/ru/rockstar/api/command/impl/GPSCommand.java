package ru.rockstar.api.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityEnderPearl;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class GPSCommand extends CommandAbstract {
    public static int x, z;
    public static String mode;

    public GPSCommand() {
        super("gps", "gps coommand", "§bUsage: §6.gps <x> <z> <off/on>", "gps");
    }

    @Override
    public void execute(String... args) {
        if (args.length < 4) {
        	Main.msg(this.getUsage(), true);
        } else {
            mode = args[3].toLowerCase();
            if (mode.equalsIgnoreCase("on")) {
                x = Integer.parseInt(args[1]);
                z = Integer.parseInt(args[2]);
            } else if (mode.equalsIgnoreCase("off")) {
                x = 0;
                z = 0;
            }
        }
    }
}
