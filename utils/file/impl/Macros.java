package ru.neverhook.utils.file.impl;

import org.lwjgl.input.Keyboard;
import ru.neverhook.Main;
import ru.neverhook.ui.macro.Macro;
import ru.neverhook.utils.file.FileManager;

import java.io.*;

public class Macros extends FileManager.CustomFile {

    public Macros(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    public void loadFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.getFile().getAbsolutePath());
            DataInputStream in = new DataInputStream(fileInputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String bind = curLine.split(":")[0];
                String value = curLine.split(":")[1];
                Main.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(bind), value));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (Macro m : Main.instance.macroManager.getMacros()) {
                out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue());
                out.write("\r\n");
            }
            out.close();
        } catch (Exception ignored) {

        }
    }
}