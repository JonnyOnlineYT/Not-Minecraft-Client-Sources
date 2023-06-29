package de.strafe.utils;

import de.strafe.modules.Module;
import com.eventapi.EventManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import de.strafe.Strafe;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XButtonn
 * @created 23.02.2022 - 11:55
 */

@UtilityClass
public class FileUtil implements IMinecraft {

	private final File dir = new File(mc.mcDataDir, Strafe.INSTANCE.NAME);
	private final File MODULE_FILE = new File(dir, "modules.json");

	public static String readFile(String path){
		StringBuilder result = new StringBuilder();
		try {
			File file = new File(path);
			if(!file.exists()){
				file.createNewFile();
			}
			FileInputStream fIn = new FileInputStream(file);
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fIn))) {
				String str;
				while((str = bufferedReader.readLine()) != null){
					result.append(str);
					result.append(System.lineSeparator());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static String readFile(File file){
		StringBuilder result = new StringBuilder();

		try {
			FileInputStream fIn = new FileInputStream(file);
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fIn))) {
				String str;
				while((str = bufferedReader.readLine()) != null){
					result.append(str);
					result.append(System.lineSeparator());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static List<File> getFilesFromDir(String dir) {
		List<File> file = new ArrayList<>();
		File startFolder = new File(dir);

		File[] files = startFolder.listFiles();
		for(File tempFile : files) {
			if(startFolder.isDirectory()) {
				file.addAll(getFilesFromDir(tempFile.getAbsolutePath()));
			} else {
				file.add(tempFile);
			}
		}
		return file;
	}

	
	private void saveModules() throws IOException {
		ArrayList<ModuleData> modules = new ArrayList<>();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		BufferedWriter reader = new BufferedWriter(new FileWriter(MODULE_FILE));

		Strafe.INSTANCE.moduleManager.modules.forEach(module -> modules.add(new ModuleData(module.getName(), module.isToggled(), module.getKey())));
		reader.write(gson.toJson(modules));
		reader.close();
	}

	private void loadModules() throws IOException {
		JsonReader reader = new JsonReader(new FileReader(MODULE_FILE));
		List<ModuleData> modules = new Gson().fromJson(reader, new TypeToken<List<ModuleData>>() {
		}.getType());
		try {
			modules.forEach(data -> {
				final Module m = Strafe.INSTANCE.moduleManager.getModule(data.getName());
				if (m != null) {
					m.toggled = data.isToggled();
					m.key = data.getKey();
					if (m.toggled) EventManager.register(m);
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

			reader.close();
		}

		@SneakyThrows
		public void load () {
			if (!dir.exists()) dir.mkdir();
			if (!MODULE_FILE.exists()) MODULE_FILE.createNewFile();
			loadModules();
		}

		@SneakyThrows
		public void save () {
			saveModules();
		}

		@Value
		class ModuleData {
			String name;
			boolean toggled;
			int key;
		}

	}
