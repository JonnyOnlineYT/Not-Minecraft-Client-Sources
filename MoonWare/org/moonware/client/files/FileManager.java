package org.moonware.client.files;

import org.moonware.client.files.impl.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class FileManager {

    public static File directory = new File("moonware");
    public static ArrayList<CustomFile> files = new ArrayList<>();

    public FileManager() {
        files.add(new FriendConfig("FriendConfig", true));
        files.add(new MacroConfig("MacroConfig", true));
        files.add(new XrayConfig("XrayConfig", true));
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

            }
        }
    }

    public CustomFile getFile(Class<?> clazz) {
        Iterator<CustomFile> customFileIterator = files.iterator();

        CustomFile file;
        do {
            if (!customFileIterator.hasNext()) {
                return null;
            }

            file = customFileIterator.next();
        } while (file.getClass() != clazz);

        return file;
    }

    public abstract static class CustomFile {

        private final File file;
        private final String name;
        private final boolean load;

        public CustomFile(String name, boolean loadOnStart) {
            this.name = name;
            load = loadOnStart;
            file = new File(directory, name + ".json");
            if (!file.exists()) {
                try {
                    saveFile();
                } catch (Exception e) {

                }
            }
        }

        public final File getFile() {
            return file;
        }

        private boolean loadOnStart() {
            return load;
        }

        public final String getName() {
            return name;
        }

        public abstract void loadFile() throws Exception;

        public abstract void saveFile() throws Exception;
    }
}
