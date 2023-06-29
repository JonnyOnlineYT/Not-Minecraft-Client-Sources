package ua.apraxia.cmd.impl;
import ua.apraxia.cmd.CommandAbstract;
import ua.apraxia.utility.other.ChatUtility;

public class GPSCommand
        extends CommandAbstract {
    public static int x;
    public static int z;
    public static String mode;

    public GPSCommand() {
        super("gps", "gps coommand", "Использование: .gps <x> <z> <off/on>", "gps");
    }

    @Override
    public void execute(String ... args) {
        if (args.length < 4) {
            ChatUtility.addChatMessage(this.getUsage());
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
