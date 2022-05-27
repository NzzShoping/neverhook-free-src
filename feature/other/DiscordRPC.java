package ru.neverhook.feature.other;

import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.utils.other.DiscordUtils;

public class DiscordRPC extends Feature {

    public DiscordRPC() {
        super("DiscordRPC", Category.World);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        DiscordUtils.startRPC();
    }

    @Override
    public void onDisable() {
        DiscordUtils.stopRPC();
        super.onDisable();
    }
}
