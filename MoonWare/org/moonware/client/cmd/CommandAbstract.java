package org.moonware.client.cmd;

import org.moonware.client.utils.MWUtils;
import org.moonware.client.helpers.Helper;

public abstract class CommandAbstract implements Command, Helper {

    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;

    public CommandAbstract(String name, String description, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.usage = usage;
    }

    public void usage() {
        MWUtils.sendChat("§cInvalid usage, try: " + usage + " or .help");
    }

    public String getUsage() {
        return usage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }
}
