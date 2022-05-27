package ru.neverhook.utils.other;

import net.minecraft.client.multiplayer.ServerData;

public class ConnectionUtils {

    public static ServerData serverData;

    public static int getPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win"))
            return 2;
        if (osName.contains("mac"))
            return 3;
        if (osName.contains("solaris"))
            return 1;
        if (osName.contains("sunos"))
            return 1;
        if (osName.contains("linux"))
            return 0;
        if (osName.contains("unix"))
            return 0;

        return 4;
    }
}
