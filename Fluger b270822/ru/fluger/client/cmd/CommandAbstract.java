/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.cmd;

import ru.fluger.client.cmd.Command;
import ru.fluger.client.helpers.Helper;
import ru.fluger.client.helpers.misc.ChatHelper;

public abstract class CommandAbstract
implements Command,
Helper {
    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;

    public CommandAbstract(String name, String description, String usage, String ... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.usage = usage;
    }

    public void usage() {
        ChatHelper.addChatMessage("\ufffdcInvalid usage, try: " + this.usage + " or .help");
    }

    public String getUsage() {
        return this.usage;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String[] getAliases() {
        return this.aliases;
    }
}

