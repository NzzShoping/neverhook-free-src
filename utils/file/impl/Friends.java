package ru.neverhook.utils.file.impl;

import ru.neverhook.Main;
import ru.neverhook.api.friend.Friend;
import ru.neverhook.utils.file.FileManager;

import java.io.*;

public class Friends extends FileManager.CustomFile {

    public Friends(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    public void loadFile() throws IOException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.getFile()));
            String line;
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String name = curLine.split(":")[0];
                Main.instance.friendManager.addFriend(name);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFile() throws IOException {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (Friend friend : Main.instance.friendManager.getFriends()) {
                out.write(friend.getName().replace(" ", ""));
                out.write("\r\n");
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}