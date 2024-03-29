package net.minecraft.command;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandEffect extends CommandBase {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "effect";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender) {
        return "commands.effect.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.effect.usage");
        } else {
            EntityLivingBase entitylivingbase = getEntity(server, sender, args[0], EntityLivingBase.class);

            if ("clear".equals(args[1])) {
                if (entitylivingbase.getActivePotionEffects().isEmpty()) {
                    throw new CommandException("commands.effect.failure.notActive.all", entitylivingbase.getName());
                } else {
                    entitylivingbase.clearActivePotions();
                    notifyCommandListener(sender, this, "commands.effect.success.removed.all", entitylivingbase.getName());
                }
            } else {
                Potion potion;

                try {
                    potion = Potion.getPotionById(parseInt(args[1], 1));
                } catch (NumberInvalidException var11) {
                    potion = Potion.getPotionFromResourceLocation(args[1]);
                }

                if (potion == null) {
                    throw new NumberInvalidException("commands.effect.notFound", args[1]);
                } else {
                    int i = 600;
                    int j = 30;
                    int k = 0;

                    if (args.length >= 3) {
                        j = parseInt(args[2], 0, 1000000);

                        if (potion.isInstant()) {
                            i = j;
                        } else {
                            i = j * 20;
                        }
                    } else if (potion.isInstant()) {
                        i = 1;
                    }

                    if (args.length >= 4) {
                        k = parseInt(args[3], 0, 255);
                    }

                    boolean flag = args.length < 5 || !"true".equalsIgnoreCase(args[4]);

                    if (j > 0) {
                        PotionEffect potioneffect = new PotionEffect(potion, i, k, false, flag);
                        entitylivingbase.addPotionEffect(potioneffect);
                        notifyCommandListener(sender, this, "commands.effect.success", new TextComponentTranslation(potioneffect.getEffectName()), Potion.getIdFromPotion(potion), k, entitylivingbase.getName(), j);
                    } else if (entitylivingbase.isPotionActive(potion)) {
                        entitylivingbase.removePotionEffect(potion);
                        notifyCommandListener(sender, this, "commands.effect.success.removed", new TextComponentTranslation(potion.getName()), entitylivingbase.getName());
                    } else {
                        throw new CommandException("commands.effect.failure.notActive", new TextComponentTranslation(potion.getName()), entitylivingbase.getName());
                    }
                }
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        } else if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, Potion.REGISTRY.getKeys());
        } else {
            return args.length == 5 ? getListOfStringsMatchingLastWord(args, "true", "false") : Collections.emptyList();
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
