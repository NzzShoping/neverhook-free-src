package ru.neverhook.utils.other;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;

public class ChatUtils implements MinecraftHelper {

    public static String chatPrefix = "\2477[" + ChatFormatting.LIGHT_PURPLE + "N" + ChatFormatting.WHITE + "ever" + ChatFormatting.LIGHT_PURPLE + "H" + ChatFormatting.WHITE + "ook" + ChatFormatting.RESET + "\2477] \2478>> \247f";

    public static void addChatMessage(String message) {
        mc.player.addChatMessage(new TextComponentString(chatPrefix + message));
    }
}
