package org.spray.infinity.utils.file.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import org.spray.infinity.features.Module;
import org.spray.infinity.main.Infinity;

public class ConfigManager {

	public File DIR = new File(Infinity.getDirection(), "configs");

	private ArrayList<Config> configList;

	public ConfigManager() {
		configList = new ArrayList<>();
	}

	public void add(Config config) {
		Config possibleConfiguration = fromName(config.getName());
		if (possibleConfiguration == null) {
			configList.add(config);
		} else {
			configList.set(configList.indexOf(possibleConfiguration), config);
		}
	}

	public void delete(Config config) {
		if (this.configList.contains(config))
			config.delete();
		this.configList.remove(config);
	}

	public Config fromName(String name) {
		for (Config config : this.configList) {
			if (config.getName().equals(name))
				return config;
		}
		return null;
	}

	public void loadConfigs() {
		if (!DIR.exists())
			DIR.mkdirs();
		this.configList.clear();
		for (File file : (File[]) Objects.<File[]>requireNonNull(DIR.listFiles())) {

			if (file.getName().endsWith(".json")) {
				for (Module m : Infinity.getModuleManager().getModules())
					add(new Config(file, m));
			}
		}
	}

	public ArrayList<Config> getConfigList() {
		return this.configList;
	}

}
