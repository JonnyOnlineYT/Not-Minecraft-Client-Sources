package org.spray.heaven.util.file.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import org.spray.heaven.features.module.Module;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.Notification;
import org.spray.heaven.protect.events.ClientInitializeEvent;
import org.spray.heaven.util.file.FileManager;

import com.mojang.realmsclient.gui.ChatFormatting;

public class ConfigRegister {

	private final Config defaultConfig = new Config("default", "heaven", "");
	private final ArrayList<Config> configList;

	public ConfigRegister(ClientInitializeEvent event) {
		configList = new ArrayList<>();

		try {
			defaultConfig.createFile();
		} catch (IOException ignored) {
//			e.printStackTrace();
		}
		event.check();
	}

	public void add(Config config) {
		Config possibleConfiguration = fromName(config.getName());
		if (possibleConfiguration == null) {
			this.configList.add(config);
			Wrapper.notify(ChatFormatting.GRAY + "New config " + ChatFormatting.WHITE + config.getName()
					+ ChatFormatting.GREEN + " added", Notification.Type.SUCCESS);
		} else {
			this.configList.set(this.configList.indexOf(possibleConfiguration), config);
			Wrapper.notify(ChatFormatting.GRAY + "Config " + ChatFormatting.WHITE + config.getName()
			+ ChatFormatting.GREEN + " overwritten", Notification.Type.SUCCESS);
		}
	}

	public boolean delete(Config config) {
		if (this.configList.contains(config)) {
			this.configList.remove(config);
			Wrapper.notify(config.getName() + " config " + ChatFormatting.GREEN + "deleted", Notification.Type.INFO);
			return config.delete();
		}
		return false;
	}

	public Config fromName(String name) {
		for (Config config : this.configList) {
			if (config.getName().equals(name))
				return config;
		}
		return null;
	}

	public void saveDefault() {
		defaultConfig.save();
	}

	public void loadDefault() {
		defaultConfig.load();
	}

	public void loadConfigs() {
		this.configList.clear();
		for (File file : (File[]) Objects.<File[]>requireNonNull(FileManager.configDir.listFiles())) {
			if (file.getName().endsWith(".json")) {
				for (Module m : Wrapper.getModule().getModules().values())
					add(new Config(file));
			}
		}
	}

	public ArrayList<Config> getConfigList() {
		return this.configList;
	}

}
