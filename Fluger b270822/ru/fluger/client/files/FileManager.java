/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import ru.fluger.client.Fluger;
import ru.fluger.client.files.impl.FriendConfig;
import ru.fluger.client.files.impl.HudConfig;
import ru.fluger.client.files.impl.MacroConfig;

public class FileManager {
    private static final File directory;
    public static ArrayList<CustomFile> files;

    public FileManager() {
        files.add(new FriendConfig("FriendConfig", true));
        files.add(new HudConfig("HudConfig", true));
        files.add(new MacroConfig("MacroConfig", true));
    }

    public void loadFiles() {
        for (CustomFile file : files) {
            try {
                if (!file.loadOnStart()) continue;
                file.loadFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFiles() {
        for (CustomFile f : files) {
            try {
                f.saveFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CustomFile getFile(Class<?> clazz) {
        CustomFile file;
        Iterator<CustomFile> customFileIterator = files.iterator();
        do {
            if (customFileIterator.hasNext()) continue;
            return null;
        } while ((file = customFileIterator.next()).getClass() != clazz);
        return file;
    }

    static {
        files = new ArrayList();
        directory = new File(Fluger.name);
    }

    public static abstract class CustomFile {
        private final File file;
        private final String name;
        private final boolean load;

        public CustomFile(String name, boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(directory, name + ".json");
            if (!this.file.exists()) {
                try {
                    this.saveFile();
                }
                catch (Exception e) {
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

