package ru.neverhook.command;


import ru.neverhook.utils.other.ChatUtils;

public abstract class AbstractCommand implements Command {
    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;

    public AbstractCommand(String name, String description, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.usage = usage;
    }

    public void usage() {
        ChatUtils.addChatMessage("§cInvalid usage, try: " + usage);
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
