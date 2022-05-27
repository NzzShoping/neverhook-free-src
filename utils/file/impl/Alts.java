package ru.neverhook.utils.file.impl;

import ru.neverhook.ui.newaltmanager.Alt;
import ru.neverhook.ui.newaltmanager.AltManager;
import ru.neverhook.utils.file.FileManager;

import java.io.*;

public class Alts extends FileManager.CustomFile {
    public Alts(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    public void loadFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getFile()));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] arguments = line.split(":");

            for (int i = 0; i < 2; ++i) {
                arguments[i].replace(" ", "");
            }

            if (arguments.length > 2) {
                AltManager.registry.add(new Alt(arguments[0], arguments[1], arguments[2], arguments.length > 3 ? Alt.Status.valueOf(arguments[3]) : Alt.Status.Unchecked));
            } else {
                AltManager.registry.add(new Alt(arguments[0], arguments[1]));
            }
        }

        bufferedReader.close();
    }

    public void saveFile() throws IOException {
        PrintWriter alts = new PrintWriter(new FileWriter(this.getFile()));

        for (Object o : AltManager.registry) {
            Alt alt = (Alt) o;
            if (alt.getMask().equals("")) {
                alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getUsername() + ":" + alt.getStatus());
            } else {
                alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getMask() + ":" + alt.getStatus());
            }
        }
        alts.close();
    }
}