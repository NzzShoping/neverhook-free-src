package ru.neverhook.feature.world;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.SendMessageEvent;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class ChatAppend extends Feature {

    public ChatAppend() {
        super("ChatAppend", Category.World);
    }

    @EventTarget
    public void onChatMessage(SendMessageEvent event) {
        if (event.getMessage().startsWith("/"))
            return;

        event.message = event.message + " | " + Main.name;
    }

}
