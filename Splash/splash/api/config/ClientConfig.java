package splash.api.config;

import splash.Splash;
import splash.utilities.file.FilesReader;

import java.io.*;
import java.util.Properties;

/**
 * Author: Ice
 * Created: 20:19, 11-Jun-20
 * Project: Client
 */
public class ClientConfig {

    private String configName;
    private File configFile;

    public ClientConfig(String configName) {
        this.configName = configName;
        this.configFile = new File(Splash.getInstance().getSplashConfigsDirectory() + "/" + getConfigName() + ".splashc");
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public File getConfigFile() {
        return configFile;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }
}