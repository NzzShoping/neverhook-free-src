package ru.neverhook.feature.other;

import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventChatMessage;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.notification.NotificationManager;
import ru.neverhook.ui.notification.NotificationType;
import ru.neverhook.utils.other.ChatUtils;

public class AutoAuth extends Feature {

    public static String password = "qwerty123";

    public AutoAuth() {
        super("AutoAuth", Category.Player);
    }

    @EventTarget
    public void onReceiveChat(EventChatMessage event) {
        if (event.getMessage().contains("/reg") || event.getMessage().contains("/register") || event.getMessage().contains("Зарегестрируйтесь")) {
            mc.player.sendChatMessage("/reg " + password + " " + password);
            ChatUtils.addChatMessage("Your password: " + password);
            NotificationManager.queue("AutoAuth", "You are successfully registered!", 4, NotificationType.SUCCESS);
        } else if (event.getMessage().contains("Авторизуйтесь") || event.getMessage().contains("/l")) {
            mc.player.sendChatMessage("/login " + password);
        }
    }
}
