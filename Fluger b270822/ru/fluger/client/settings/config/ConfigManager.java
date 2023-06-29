/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.apache.commons.io.FilenameUtils
 */
package ru.fluger.client.settings.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;
import ru.fluger.client.Fluger;
import ru.fluger.client.settings.config.Config;
import ru.fluger.client.settings.config.Manager;

public final class ConfigManager
extends Manager<Config> {
    public static final File cfg_dir = new File(Fluger.name, "configs");
    public static final String EXTENSION = ".json";
    private static final ArrayList<Config> loadedConfigs = new ArrayList();

    public ConfigManager() {
        this.setContents(ConfigManager.loadConfigs());
        cfg_dir.mkdirs();
    }

    private static ArrayList<Config> loadConfigs() {
        File[] files = cfg_dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!FilenameUtils.getExtension((String)file.getName()).equals("json")) continue;
                loadedConfigs.add(new Config(FilenameUtils.removeExtension((String)file.getName())));
            }
        }
        return loadedConfigs;
    }

    public static ArrayList<Config> getLoadedConfigs() {
        return loadedConfigs;
    }

    public void load() {
        if (!cfg_dir.exists()) {
            cfg_dir.mkdirs();
        }
        if (cfg_dir != null) {
            File[] files;
            for (File f2 : files = cfg_dir.listFiles(f -> !f.isDirectory() && FilenameUtils.getExtension((String)f.getName()).equals("json"))) {
                Config config = new Config(FilenameUtils.removeExtension((String)f2.getName()).replace(" ", ""));
                loadedConfigs.add(config);
            }
        }
    }

    public boolean loadConfig(String configName) {
        if (configName == null) {
            return false;
        }
        Config config = this.findConfig(configName);
        if (config == null) {
            return false;
        }
        try {
            FileReader reader = new FileReader(config.getFile());
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject)parser.parse((Reader)reader);
            config.load(object);
            return true;
        }
        catch (FileNotFoundException e) {
            return false;
        }
    }

    public boolean saveConfig(String configName) {
        if (configName == null) {
            return false;
        }
        Config config = this.findConfig(configName);
        if (config == null) {
            Config newConfig = config = new Config(configName);
            this.getContents().add(newConfig);
        }
        String contentPrettyPrint = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)config.save());
        try {
            FileWriter writer = new FileWriter(config.getFile());
            writer.write(contentPrettyPrint);
            writer.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public Config findConfig(String configName) {
        if (configName == null) {
            return null;
        }
        for (Config config : this.getContents()) {
            if (!config.getName().equalsIgnoreCase(configName)) continue;
            return config;
        }
        if (new File(cfg_dir, configName + EXTENSION).exists()) {
            return new Config(configName);
        }
        return null;
    }

    public boolean deleteConfig(String configName) {
        if (configName == null) {
            return false;
        }
        Config config = this.findConfig(configName);
        if (config != null) {
            File f = config.getFile();
            this.getContents().remove(config);
            return f.exists() && f.delete();
        }
        return false;
    }
}

