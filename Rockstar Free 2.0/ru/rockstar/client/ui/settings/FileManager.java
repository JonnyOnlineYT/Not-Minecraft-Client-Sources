package ru.rockstar.client.ui.settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import ru.rockstar.Main;
import ru.rockstar.client.ui.HudConfig;
import ru.rockstar.client.ui.altmanager.alt.AltConfig;
import ru.rockstar.client.ui.settings.impls.FriendConfig;
import ru.rockstar.client.ui.settings.impls.MacroConfig;

public class FileManager {

    public static File directory = new File(Main.instance.name);
    public static ArrayList<CustomFile> files = new ArrayList<>();

    public FileManager() {
        files.add(new FriendConfig("FriendConfig", true));
        files.add(new MacroConfig("MacroConfig", true));
        files.add(new AltConfig("AltConfig", true));
        files.add(new HudConfig("HudConfig", true));
    }

    public void loadFiles() {
        for (final CustomFile file : FileManager.files) {
            try {
                if (!file.loadOnStart()) {
                    continue;
                }
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public CustomFile getFile(final Class clazz) {
        for (final CustomFile file : FileManager.files) {
            if (file.getClass() == clazz) {
                return file;
            }
        }
        return null;
    }
    
    static {
        FileManager.files = new ArrayList<CustomFile>();
        final Main instance = Main.instance;
        directory = new File(Main.name);
    }
    
    public abstract static class CustomFile
    {
        private final File file;
        private final String name;
        private final boolean load;
        
        public CustomFile(final String name, final boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(FileManager.directory, name + ".json");
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
        
        public boolean loadOnStart() {
            return this.load;
        }
        
        public final String getName() {
            return this.name;
        }
        
        public abstract void loadFile() throws IOException;
        
        public abstract void saveFile() throws IOException;
    }
}
