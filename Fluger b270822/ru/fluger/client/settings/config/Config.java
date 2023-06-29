/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package ru.fluger.client.settings.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import ru.fluger.client.Fluger;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.settings.config.ConfigManager;
import ru.fluger.client.settings.config.ConfigUpdater;

public final class Config
implements ConfigUpdater {
    private String name;
    private final File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(ConfigManager.cfg_dir, name + ".json");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JsonObject save() {
        JsonObject jsonObject = new JsonObject();
        JsonObject modulesObject = new JsonObject();
        for (Feature module : Fluger.instance.featureManager.getFeatureList()) {
            modulesObject.add(module.getLabel(), (JsonElement)module.save());
        }
        jsonObject.addProperty("Version", Fluger.version);
        jsonObject.add("Features", (JsonElement)modulesObject);
        return jsonObject;
    }

    @Override
    public void load(JsonObject object) {
        if (object.has("Features")) {
            JsonObject modulesObject = object.getAsJsonObject("Features");
            for (Feature module : Fluger.instance.featureManager.getFeatureList()) {
                module.setState(false);
                module.load(modulesObject.getAsJsonObject(module.getLabel()));
            }
        }
    }
}

