package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class CommandHandler implements ICommandManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<String, ICommand> commandMap = Maps.newHashMap();
    private final Set<ICommand> commandSet = Sets.newHashSet();

    /**
     * Attempt to execute a command. This method should return the number of times that the command was executed. If the
     * command does not exist or if the player does not have permission, 0 will be returned. A number greater than 1 can
     * be returned if a player selector is used.
     */
    public int executeCommand(ICommandSender sender, String rawCommand) {
        rawCommand = rawCommand.trim();

        if (rawCommand.startsWith("/")) {
            rawCommand = rawCommand.substring(1);
        }

        String[] astring = rawCommand.split(" ");
        String s = astring[0];
        astring = dropFirstString(astring);
        ICommand icommand = this.commandMap.get(s);
        int i = 0;

        try {
            int j = this.getUsernameIndex(icommand, astring);

            if (icommand == null) {
                TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation("commands.generic.notFound");
                textcomponenttranslation1.getStyle().setColor(TextFormatting.RED);
                sender.addChatMessage(textcomponenttranslation1);
            } else if (icommand.checkPermission(this.getServer(), sender)) {
                if (j > -1) {
                    List<Entity> list = EntitySelector.matchEntities(sender, astring[j], Entity.class);
                    String s1 = astring[j];
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());

                    if (list.isEmpty()) {
                        throw new PlayerNotFoundException("commands.generic.selector.notFound", astring[j]);
                    }

                    for (Entity entity : list) {
                        astring[j] = entity.getCachedUniqueIdString();

                        if (this.tryExecute(sender, astring, icommand, rawCommand)) {
                            ++i;
                        }
                    }

                    astring[j] = s1;
                } else {
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);

                    if (this.tryExecute(sender, astring, icommand, rawCommand)) {
                        ++i;
                    }
                }
            } else {
                TextComponentTranslation textcomponenttranslation2 = new TextComponentTranslation("commands.generic.permission");
                textcomponenttranslation2.getStyle().setColor(TextFormatting.RED);
                sender.addChatMessage(textcomponenttranslation2);
            }
        } catch (CommandException commandexception) {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            sender.addChatMessage(textcomponenttranslation);
        }

        sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, i);
        return i;
    }

    protected boolean tryExecute(ICommandSender sender, String[] args, ICommand command, String input) {
        try {
            command.execute(this.getServer(), sender, args);
            return true;
        } catch (WrongUsageException wrongusageexception) {
            TextComponentTranslation textcomponenttranslation2 = new TextComponentTranslation("commands.generic.usage", new TextComponentTranslation(wrongusageexception.getMessage(), wrongusageexception.getErrorObjects()));
            textcomponenttranslation2.getStyle().setColor(TextFormatting.RED);
            sender.addChatMessage(textcomponenttranslation2);
        } catch (CommandException commandexception) {
            TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            textcomponenttranslation1.getStyle().setColor(TextFormatting.RED);
            sender.addChatMessage(textcomponenttranslation1);
        } catch (Throwable throwable) {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.generic.exception");
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            sender.addChatMessage(textcomponenttranslation);
            LOGGER.warn("Couldn't process command: " + input, throwable);
        }

        return false;
    }

    protected abstract MinecraftServer getServer();

    /**
     * adds the command and any aliases it has to the internal map of available commands
     */
    public ICommand registerCommand(ICommand command) {
        this.commandMap.put(command.getCommandName(), command);
        this.commandSet.add(command);

        for (String s : command.getCommandAliases()) {
            ICommand icommand = this.commandMap.get(s);

            if (icommand == null || !icommand.getCommandName().equals(s)) {
                this.commandMap.put(s, command);
            }
        }

        return command;
    }

    /**
     * creates a new array and sets elements 0..n-2 to be 0..n-1 of the input (n elements)
     */
    private static String[] dropFirstString(String[] input) {
        String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }

    public List<String> getTabCompletionOptions(ICommandSender sender, String input, @Nullable BlockPos pos) {
        String[] astring = input.split(" ", -1);
        String s = astring[0];

        if (astring.length == 1) {
            List<String> list = Lists.newArrayList();

            for (Entry<String, ICommand> entry : this.commandMap.entrySet()) {
                if (CommandBase.doesStringStartWith(s, entry.getKey()) && entry.getValue().checkPermission(this.getServer(), sender)) {
                    list.add(entry.getKey());
                }
            }

            return list;
        } else {
            if (astring.length > 1) {
                ICommand icommand = this.commandMap.get(s);

                if (icommand != null && icommand.checkPermission(this.getServer(), sender)) {
                    return icommand.getTabCompletionOptions(this.getServer(), sender, dropFirstString(astring), pos);
                }
            }

            return Collections.emptyList();
        }
    }

    public List<ICommand> getPossibleCommands(ICommandSender sender) {
        List<ICommand> list = Lists.newArrayList();

        for (ICommand icommand : this.commandSet) {
            if (icommand.checkPermission(this.getServer(), sender)) {
                list.add(icommand);
            }
        }

        return list;
    }

    public Map<String, ICommand> getCommands() {
        return this.commandMap;
    }

    /**
     * Return a command's first parameter index containing a valid username.
     */
    private int getUsernameIndex(ICommand command, String[] args) throws CommandException {
        if (command == null) {
            return -1;
        } else {
            for (int i = 0; i < args.length; ++i) {
                if (command.isUsernameIndex(args, i) && EntitySelector.matchesMultiplePlayers(args[i])) {
                    return i;
                }
            }

            return -1;
        }
    }
}
