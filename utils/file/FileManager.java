package ru.neverhook.utils.file;

import ru.neverhook.Main;
import ru.neverhook.utils.file.impl.Alts;
import ru.neverhook.utils.file.impl.Friends;
import ru.neverhook.utils.file.impl.Macros;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class FileManager {
    private static final File directory;
    public static ArrayList<CustomFile> files = new ArrayList<>();

    static {
        directory = new File(Main.name);
    }

    public FileManager() {
        files.add(new Alts("alts", true));
        files.add(new Macros("macro", true));
        files.add(new Friends("friends", true));
    }

    public void loadFiles() {
        for (CustomFile file : files) {
            try {
                if (file.loadOnStart()) {
                    file.loadFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void saveFiles() {
        for (CustomFile f : files) {
            try {
                f.saveFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CustomFile getFile(Class clazz) {
        Iterator<CustomFile> var2 = files.iterator();

        CustomFile file;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            file = var2.next();
        } while (file.getClass() != clazz);

        return file;
    }

    public abstract static class CustomFile {
        private final File file;
        private final String name;
        private final boolean load;

        public CustomFile(String name, boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(FileManager.directory, name + ".txt");
            if (!this.file.exists()) {
                try {
                    this.saveFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public final File getFile() {
            return this.file;
        }

        private boolean loadOnStart() {
            return this.load;
        }

        public final String getName() {
            return this.name;
        }

        public abstract void loadFile() throws IOException;

        public abstract void saveFile() throws IOException;
    }
}