package ru.neverhook.utils.other;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import ru.neverhook.Main;

public class DiscordUtils implements MinecraftHelper {

    private static final String discordID = "824587285575630878";
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();

        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.details = "vk.com/neverhook";
        discordRichPresence.largeImageKey = "ava";
        discordRichPresence.largeImageText = "Build: " + Main.build;
        String stringKek = "";
        if (mc.currentScreen instanceof GuiMainMenu) {
            stringKek = "In Main Menu";
        } else if (mc.currentScreen instanceof GuiMultiplayer) {
            stringKek = "In Multiplayer Menu";
        } else if (mc.isSingleplayer()) {
            stringKek = "In SinglePlayer";
        } else if (mc.getCurrentServerData() != null) {
            stringKek = "Playing " + mc.getCurrentServerData().serverIP;
        } else if (mc.currentScreen instanceof GuiOptions) {
            stringKek = "In Options Gui";
        }
        discordRichPresence.state = stringKek;
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}
