package ru.neverhook.command;

import ru.neverhook.command.impl.*;
import ru.neverhook.event.EventManager;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final ArrayList<Command> commands = new ArrayList<Command>();

    public CommandManager() {
        EventManager.register(new CommandHandler(this));
        commands.add(new FakeHackCommand());
        commands.add(new BindCommand());
        commands.add(new ClipCommand());
        commands.add(new FriendCommand());
        commands.add(new MacroCommand());
    }

    public List getCommands() {
        return this.commands;
    }

    public boolean execute(String args) {
        String noPrefix = args.substring(1);
        String[] split = noPrefix.split(" ");
        if (split.length > 0) {
            List commands = this.commands;
            int i = 0;

            for (int commandsSize = commands.size(); i < commandsSize; ++i) {
                AbstractCommand command = (AbstractCommand) commands.get(i);
                String[] var8 = command.getAliases();
                for (String alias : var8) {
                    if (split[0].equalsIgnoreCase(alias)) {
                        command.execute(split);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
