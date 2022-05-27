package ru.neverhook.utils.config;

import net.minecraft.client.Minecraft;
import ru.neverhook.Main;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.ui.clickgui.settings.SettingsManager;

import java.io.*;
import java.util.ArrayList;

public class Config {

    public File dir;
    public File dataFile;

    public Config() {
        this.dir = new File(Minecraft.getMinecraft().mcDataDir, Main.name);
        if (!this.dir.exists()) {
            this.dir.mkdir();
        }
        this.dataFile = new File(this.dir, "config.txt");
        if (!this.dataFile.exists()) {
            try {
                this.dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.load();
    }

    public void save() {

        ArrayList<String> toSave = new ArrayList<>();
        for (Feature feature : Main.instance.featureManager.getFeatureList()) {
            toSave.add("Module:" + feature.getName() + ":" + feature.getState() + ":" + feature.getKey());
        }
        for (Setting set : SettingsManager.getSettings()) {
            if (set.isCheck()) {
                toSave.add("Setting:" + set.getName() + ":" + set.getModule().getName() + ":" + set.getValue());
            } else if (set.isCombo()) {
                toSave.add("Setting:" + set.getName() + ":" + set.getModule().getName() + ":" + set.getValString());
            } else if (set.isSlider()) {
                toSave.add("Setting:" + set.getName() + ":" + set.getModule().getName() + ":" + set.getValDouble());
            } else if (set.isColorSlider()) {
                toSave.add("Setting:" + set.getName() + ":" + set.getModule().getName() + ":" + set.getColorValue());
            }
        }

        try {
            PrintWriter reader = new PrintWriter(dataFile);
            for (String str : toSave) {
                reader.println(str);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                lines.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String s : lines) {
            String[] args = s.split(":");
            if (s.toLowerCase().startsWith("module:")) {
                Feature feature = Main.instance.featureManager.getFeaturesByName(args[1]);
                if (feature == null) {
                    continue;
                }
                feature.setState(Boolean.parseBoolean(args[2]));
                feature.setKey(Integer.parseInt(args[3]));
            } else {
                if (!s.toLowerCase().startsWith("setting:")) {
                    continue;
                }
                Feature m = Main.instance.featureManager.getFeaturesByName(args[2]);
                if (m == null) {
                    continue;
                }
                Setting set = Main.instance.setmgr.getSettingByName(args[1]);
                if (set == null) {
                    continue;
                }
                if (set.isCheck()) {
                    set.setValue(Boolean.parseBoolean(args[3]));
                } else if (set.isCombo()) {
                    set.setValString(args[3]);
                } else if (set.isSlider()) {
                    set.setValDouble(Double.parseDouble(args[3]));
                } else if (set.isColorSlider()) {
                    set.setColorValue(Integer.parseInt(args[3]));
                }
            }
        }
    }
}