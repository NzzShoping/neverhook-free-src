package ru.neverhook.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import ru.neverhook.Main;
import ru.neverhook.event.EventManager;
import ru.neverhook.feature.hud.Notifications;
import ru.neverhook.feature.other.ModuleSoundAlert;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.ui.notification.NotificationManager;
import ru.neverhook.ui.notification.NotificationType;
import ru.neverhook.utils.animation.Translate;

public class Feature {

    protected static Minecraft mc = Minecraft.getMinecraft();
    private final Category category;
    private final String name;
    public Translate translate = new Translate(0.0F, 0.0F);
    public boolean visible = true;
    private String displayName;
    private String suffix;
    private int key;
    private boolean state;

    public Feature(String name, Category category) {
        this.name = name;
        this.category = category;
        this.state = false;
        this.key = 0;
    }

    public void onEnable() {
        EventManager.register(this);
        if (!(getName().contains("ClickGui") || getName().contains("Client Font")) && Notifications.state.getValue()) {
            NotificationManager.queue("Module", getName() + " was" + " Enabled!", 2, NotificationType.INFO);
        }
        if (!(getName().contains("ClickGui") || getName().contains("Client Font"))) {
            if (Main.instance.featureManager.getFeatureByClass(ModuleSoundAlert.class).getState()) {
                mc.player.playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, 1F, 1F);
            }
        }
    }

    public Translate getTranslate() {
        return this.translate;
    }

    public void onDisable() {
        EventManager.unregister(this);

        if (!(getName().contains("ClickGui") || getName().contains("Client Font")) && Notifications.state.getValue()) {
            NotificationManager.queue("Module", getName() + " was" + " Disabled!", 2, NotificationType.INFO);
        }
        if (!(getName().contains("ClickGui") || getName().contains("Client Font"))) {
            if (Main.instance.featureManager.getFeatureByClass(ModuleSoundAlert.class).getState()) {
                mc.player.playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, 1F, 1F);
            }
        }
    }

    public void toggle() {
        this.state = !this.state;
        if (this.state) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void addSettings(Setting... settings) {
        for (Setting setting : settings) {
            Main.instance.setmgr.addSetting(setting);
        }
    }

    public String getName() {
        return this.name;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        if (state) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        this.state = state;
    }

    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isHidden() {
        return !visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.setDisplayName(this.getName() + " " + TextFormatting.GRAY + suffix);
    }
}
