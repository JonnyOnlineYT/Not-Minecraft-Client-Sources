package volcano.summer.base.manager.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import net.minecraft.client.Minecraft;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class Config {

	private String name;
	private File modsFile, modesFile, directory;
	private Minecraft mc;

	public Config(String name, File directory, File mods, File modes) {
		mc = Minecraft.getMinecraft();
		this.name = name;
		this.modsFile = mods;
		this.modesFile = modes;
		this.directory = directory;
		makePath(directory);
	}

	public void load() {
		loadMods();
		loadModes();
	}

	public void save() {
		saveMods();
		saveModes();

	}

	private void saveMods() {
		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(modsFile));

			for (Module m : Summer.moduleManager.getMods()) {
				printWriter.println(String.valueOf(m.getName() + ":" + m.getState()));
			}
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void saveModes() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(modesFile));
			for (Value value : Summer.valueManager.values) {
				if (value == null) {
					continue;
				}
				if (value instanceof ModeValue) {
					ModeValue v = (ModeValue) value;
					writer.write(String.valueOf(v.getName()) + ":" + v.getStringValue());
					writer.newLine();
				} else {
					writer.write(String.valueOf(value.getName()) + ":" + value.getValue());
					writer.newLine();
				}
			}
			writer.close();
			Summer.tellPlayer("Config '" + name + "' saved.");
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void loadMods() {
		for (Module m : Summer.moduleManager.getToggledModules()) {
			m.toggleMod();
		}
		try {
			Scanner inFile = new Scanner(new BufferedReader(new FileReader(modsFile)));
			while (inFile.hasNextLine()) {
				String f = inFile.nextLine();
				Module m = Summer.moduleManager.getModuleByString((f.split(":")[0]));
				Boolean b = Boolean.parseBoolean(f.split(":")[1]);
				if (b) {
					m.toggleMod();
				}
			}
			inFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadModes() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(modesFile));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] arguments = line.split(":");
				if (arguments.length == 2) {
					for (Value value : Summer.valueManager.values) {
						if (value == null) {
							continue;
						}
						if (value instanceof ModeValue) {
							if (!arguments[0].equalsIgnoreCase(value.getName())) {
								continue;
							}
							try {
								((ModeValue) value).setStringValue(arguments[1]);
							} catch (Exception e3) {
								value.setValue(value.getDefaultValue());
							}
						} else {
							if (!arguments[0].equalsIgnoreCase(value.getName())) {
								continue;
							}
							try {
								if (value.getValue() instanceof Boolean) {
									value.setValue(Boolean.parseBoolean(arguments[1]));
								} else if (value.getValue() instanceof Byte) {
									value.setValue(Byte.parseByte(arguments[1]));
								} else if (value.getValue() instanceof Double) {
									value.setValue(Double.parseDouble(arguments[1]));
								} else if (value.getValue() instanceof Float) {
									value.setValue(Float.parseFloat(arguments[1]));
								} else if (value.getValue() instanceof Integer) {
									value.setValue(Integer.parseInt(arguments[1]));
								} else if (value.getValue() instanceof Long) {
									value.setValue(Long.parseLong(arguments[1]));
								} else {
									if (!(value.getValue() instanceof String) && !(value instanceof ModeValue)) {
										continue;
									}
									value.setValue(arguments[1]);
								}
							} catch (Exception e3) {
								value.setValue(value.getDefaultValue());
							}
						}
					}
				}
			}
			reader.close();
			Summer.tellPlayer("Config '" + name + "' loaded.");
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void makePath(File directory) {
		File moduleDirectory = directory;
		if (!moduleDirectory.exists()) {
			if (moduleDirectory.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getModsFile() {
		return modsFile;
	}

	public void setModsFile(File modsFile) {
		this.modsFile = modsFile;
	}

	public File getModesFile() {
		return modesFile;
	}

	public void setModesFile(File modesFile) {
		this.modesFile = modesFile;
	}

	public File getDirectory() {
		return this.directory;
	}

}