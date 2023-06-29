package splash.client.managers.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import splash.Splash;
import splash.api.config.ClientConfig;
import splash.api.manager.ClientManager;
import splash.api.module.Module;
import splash.api.notification.Notification;
import splash.api.value.Value;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.utilities.file.FilesReader;
import splash.utilities.hastebin.Hastebin;
import splash.utilities.system.ClientLogger;
import splash.utilities.visual.ColorUtilities;
import splash.utilities.web.InterwebsUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Ice
 * Created: 20:22, 11-Jun-20
 * Project: Client
 */
public class ConfigManager extends ClientManager<ClientConfig> {

    @Override
    public String managerName() {
        return "ConfigLoader";
    }

    public void saveConfig(ClientConfig clientConfig) {
    	Splash.getInstance().getNotificationManager().show(new Notification("Saved " + clientConfig.getConfigName(), "Config", 1));
        Splash.getInstance().clickUI.reload(false);
        JsonObject jsonObject = new JsonObject();
      for(Module module : Splash.getInstance().getModuleManager().getContents()) {
          JsonObject moduleObject = new JsonObject();
          moduleObject.addProperty("name", module.getModuleDisplayName());
          moduleObject.addProperty("macro", module.getModuleMacro());
          moduleObject.addProperty("active", module.isModuleActive());

          Splash.getInstance().getValueManager().getValuesFrom(module).forEach(value -> moduleObject.addProperty(value.getValueName(), String.valueOf(value.getValue())));

          jsonObject.add(module.getModuleDisplayName(), moduleObject);
      }

        try {
            FileWriter fileWriter = new FileWriter(clientConfig.getConfigFile());
            fileWriter.write(new GsonBuilder().create().toJson(jsonObject));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        addContent(clientConfig);
    }

    public String saveConfigToWeb(ClientConfig clientConfig) {
        return Hastebin.postWithCode(FilesReader.readFile(clientConfig.getConfigFile()), true);
    }

    public void loadConfig(ClientConfig clientConfig) {
        Splash.getInstance().getModuleManager().getContents().forEach(module -> module.setModuleActive(false));
        Splash.getInstance().getNotificationManager().show(new Notification("Loaded " + clientConfig.getConfigName(), "Config", 1));
        String content = FilesReader.readFile(clientConfig.getConfigFile());

        JsonObject configurationObject = new GsonBuilder().create().fromJson(content, JsonObject.class);

        for(Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
            if (entry.getValue() instanceof JsonObject) {
                JsonObject moduleObject = (JsonObject) entry.getValue();

                for(Module module : Splash.getInstance().getModuleManager().getContents()) {
                    if(module.getModuleDisplayName().equalsIgnoreCase(moduleObject.get("name").getAsString())) {
                        module.setModuleMacro(moduleObject.get("macro").getAsInt());
                            if(moduleObject.get("active").getAsBoolean()) {
                                module.activateModule();
                            } else {
                                module.setModuleActive(false);
                            }
                            module.setColor(ColorUtilities.getGerman(0.9F));

                        for(Value value : Splash.getInstance().getValueManager().getValuesFrom(module)) {
                            if(moduleObject.get(value.getValueName()) != null) {
                                if(value instanceof NumberValue) {
                                    if(value.getValue() instanceof Double) {
                                        value.setValueObject(moduleObject.get(value.getValueName()).getAsDouble());
                                    }
                                    if(value.getValue() instanceof Integer) {
                                        value.setValueObject(moduleObject.get(value.getValueName()).getAsInt());
                                    }
                                    if(value.getValue() instanceof Float) {
                                        value.setValueObject(moduleObject.get(value.getValueName()).getAsFloat());
                                    }
                                }
                                if(value instanceof BooleanValue) {
                                    value.setValueObject(moduleObject.get(value.getValueName()).getAsBoolean());
                                }
                                if(value instanceof ModeValue) {
                                    for(int i = 0;i < ((ModeValue) value).getModes().length;i++) {
                                        if(((ModeValue) value).getModes()[i].name().equalsIgnoreCase(moduleObject.get(value.getValueName()).getAsString())) {
                                             value.setValueObject(((ModeValue) value).getModes()[i]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Splash.getInstance().getModuleManager().getContents().forEach(module -> module.setColor(ColorUtilities.getGerman(0.9F)));
    }

    public void loadConfigFromWeb(String configCode, String configName) {
        ClientConfig clientConfig = new ClientConfig(configName);
        try {
            FileWriter fileWriter = new FileWriter(clientConfig.getConfigFile());
            fileWriter.write(InterwebsUtils.getContent("https://hastebin.com/raw/" + configCode));
            fileWriter.close();
            Splash.getInstance().getConfigManager().addContent(new ClientConfig(configName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfigs() {
        List<String> searchResult = new ArrayList<>();
        FilesReader.search(".*\\.splashc", Splash.getInstance().getSplashConfigsDirectory(), searchResult);
        for (String configName : searchResult) {

            addContent(new ClientConfig(configName.replace(".splashc", "")));
        }
    }

    public void deleteConfig(ClientConfig clientConfig) {
        if(clientConfig.getConfigFile() != null) {
            if(clientConfig.getConfigFile().delete()) {
                Splash.getInstance().getConfigManager().deleteConfig(clientConfig);
                Splash.getInstance().getConfigManager().getContents().remove(clientConfig);
                Splash.getInstance().getConfigManager().loadConfigs();
            } else {
                ClientLogger.printErrorToConsole("Error");
            }
        }
    }
}