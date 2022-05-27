package ru.neverhook;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import ru.neverhook.api.friend.FriendManager;
import ru.neverhook.command.CommandManager;
import ru.neverhook.event.EventManager;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventKey;
import ru.neverhook.feature.Feature;
import ru.neverhook.feature.FeatureManager;
import ru.neverhook.feature.hud.ArreyList;
import ru.neverhook.ui.changelog.ChangeManager;
import ru.neverhook.ui.clickgui.ClickGuiScreen;
import ru.neverhook.ui.clickgui.settings.SettingsManager;
import ru.neverhook.ui.macro.MacroManager;
import ru.neverhook.utils.combat.RotationUtil;
import ru.neverhook.utils.config.Config;
import ru.neverhook.utils.file.FileManager;
import ru.neverhook.utils.file.impl.Macros;
import ru.neverhook.utils.font.FontRenderer;
import ru.neverhook.utils.visual.ColorUtils;

import java.awt.*;
import java.io.IOException;

public class Main {

    public static String name = "NeverHook", build = "Free-Build #1.1";
    public static Main instance = new Main();
    private static String API;
    public Config config;
    public SettingsManager setmgr;
    public FeatureManager featureManager;
    public FriendManager friendManager;
    public ClickGuiScreen clickGui;
    public CommandManager commandManager;
    public ChangeManager changeManager;
    public FileManager fileManager;
    public MacroManager macroManager;
    public RotationUtil.Rotation rotation;

    public static Color getClientColor() {
        Color color = Color.white;
        Color onecolor = new Color(ArreyList.onecolor.getColorValue());
        Color twoColor = new Color(ArreyList.twocolor.getColorValue());
        double time = ArreyList.time.getValDouble();
        String mode = Main.instance.setmgr.getSettingByName("ArrayList Color").getValString();
        float yDist = (float) 4;
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().fontRenderer.getHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = ColorUtils.rainbowCol((int) (1 * 200 * 0.1f), 0.8f, 1.0f);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfoColors1((int) yDist, yTotal);
        } else if (mode.equalsIgnoreCase("Pulse")) {
            color = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / time) / 100.0 + 6.0F * (yDist * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yDist * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("None")) {
            color = new Color(255, 255, 255);
        }
        return color;
    }


    public static ChangeManager getLogs() {
        return instance.changeManager;
    }

    public static FontRenderer getFontRender() {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer font = mc.fontRenderer;
        String mode = Main.instance.setmgr.getSettingByName("FontList").getValString();
        switch (mode.toLowerCase()) {
            case "comfortaa":
                font = mc.arraylist;
                break;
            case "sf ui":
                font = mc.fontRenderer;
                break;
            case "verdana":
                font = mc.verdana;
                break;
            case "robotoLight":
                font = mc.robotoLight;
                break;
            case "robotoregular":
                font = mc.robotoRegular;
                break;
            case "lato":
                font = mc.lato;
                break;

        }
        return font;
    }

    public static FileManager getFileManager() {
        return instance.fileManager;
    }

    public static String getAPI() {
        return API;
    }

    public static void setAPI(String API) {
        Main.API = API;
    }

    public void init() {
        setmgr = new SettingsManager();
        friendManager = new FriendManager();
        featureManager = new FeatureManager();
        commandManager = new CommandManager();
        (changeManager = new ChangeManager()).setChangeLogs();
        (fileManager = new FileManager()).loadFiles();
        clickGui = new ClickGuiScreen();
        macroManager = new MacroManager();
        rotation = new RotationUtil.Rotation();

        try {
            Main.getFileManager().getFile(Macros.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        config = new Config();

        EventManager.register(rotation);
        EventManager.register(this);
    }

    public void shutDown() {
        EventManager.unregister(this);
        (fileManager = new FileManager()).saveFiles();
    }

    @EventTarget
    public void onKey(EventKey event) {
        Main.instance.macroManager.getMacros().forEach(macro -> {
            if (macro.getKey() == Keyboard.getEventKey()) {
                macro.sendMacro();
            }
        });
        this.featureManager.getFeatureList().stream().filter(feature -> (feature.getKey() == event.getKey())).forEach(Feature::toggle);
    }
}
