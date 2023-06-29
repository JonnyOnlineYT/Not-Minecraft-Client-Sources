package ua.apraxia.modules.settings.config;

import com.google.gson.JsonObject;
import ua.apraxia.Hexbyte;
import ua.apraxia.modules.Module;

import java.io.File;

public final class Config implements ConfigUpdater {

    private final String name;
    private final File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(ConfigManager.configDirectory, name + ".hex");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
            }
        }
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    @Override
    public JsonObject save() {
        JsonObject jsonObject = new JsonObject();
        JsonObject modulesObject = new JsonObject();
        JsonObject panelObject = new JsonObject();

        for (Module module : Hexbyte.getInstance().moduleManagment.getAllFeatures()) {
            modulesObject.add(module.getModuleName(), module.save());
        }

        jsonObject.add("Features", modulesObject);

        return jsonObject;
    }

    @Override
    public void load(JsonObject object) {
        if (object.has("Features")) {
            JsonObject modulesObject = object.getAsJsonObject("Features");
            for (Module module : Hexbyte.getInstance().moduleManagment.getAllFeatures()) {
                module.setState(false);
                module.load(modulesObject.getAsJsonObject(module.getModuleName()));
            }
        }
    }
}