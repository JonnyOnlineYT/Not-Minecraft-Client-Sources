package volcano.summer.base.manager.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.Minecraft;
import volcano.summer.base.Summer;

public class ConfigManager {

	private Minecraft mc = Minecraft.getMinecraft();
	private CopyOnWriteArrayList<Config> configs;

	public ConfigManager() {
		configs = new CopyOnWriteArrayList<Config>();
		makePath();
	}

	public void addConfig(String name) {
		Config c = new Config(name,
				new File(String.valueOf(mc.mcDataDir.toString()) + "\\" + Summer.name + "\\Configs\\" + name),
				new File(String.valueOf(mc.mcDataDir.toString()).toString() + "\\" + Summer.name + "\\" + "Configs"
						+ "\\" + name + "\\" + "mods" + ".txt"),
				new File(String.valueOf(mc.mcDataDir.toString()).toString() + "\\" + Summer.name + "\\" + "Configs"
						+ "\\" + name + "\\" + "modes" + ".txt"));
		configs.add(c);
		c.save();
	}

	public void removeConfig(String name) {
		for (Config c : configs) {
			if (c.getName().equalsIgnoreCase(name)) {
				try {
					delete(c.getDirectory());
					configs.remove(c);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			throw new FileNotFoundException("Failed to delete file: " + f);
	}

	private void makePath() {
		File moduleDirectory = new File(
				String.valueOf(mc.mcDataDir.toString()) + "\\" + Summer.name + "\\" + "Configs");
		if (!moduleDirectory.exists()) {
			if (moduleDirectory.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	}

	private File getFile() {
		return new File(String.valueOf(mc.mcDataDir.toString()) + "\\" + Summer.name + "\\" + "Configs" + "\\"
				+ "configs" + ".txt");
	}

	public void saveConfigs() {
		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(getFile()));
			for (Config c : configs) {
				printWriter.println(String.valueOf(
						c.getName() + ";" + c.getDirectory() + ";" + c.getModsFile() + ";" + c.getModesFile()));
			}
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadConfigs() {
		try {
			Scanner inFile = new Scanner(new BufferedReader(new FileReader(getFile())));
			while (inFile.hasNextLine()) {
				String[] f = inFile.nextLine().split(";");
				configs.add(new Config(f[0], new File(f[1]), new File(f[2]), new File(f[3])));
			}
			inFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Config getConfig(String s) {
		for (Config c : configs) {
			if (c.getName().equalsIgnoreCase(s.toLowerCase())) {
				return c;
			}
		}
		return null;
	}

	public CopyOnWriteArrayList<Config> getConfigs() {
		return this.configs;
	}
}
