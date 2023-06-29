package volcano.summer.base.manager.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.file.files.Alts;
import volcano.summer.base.manager.file.files.Binds;
import volcano.summer.base.manager.file.files.Friends;
import volcano.summer.base.manager.file.files.Modules;
import volcano.summer.base.manager.file.files.Values;

public class FileManager {

	private static Minecraft mc = Minecraft.getMinecraft();

	public static ArrayList<CustomFile> Files = new ArrayList();
	private static File directory = new File(String.valueOf(mc.mcDataDir.toString()) + "\\" + Summer.name);
	private static File moduleDirectory = new File(
			String.valueOf(mc.mcDataDir.toString()) + "\\" + Summer.name + "\\" + "Modules");

	public FileManager() {
		makeDirectories();
		Files.add(new Friends("Friends", false, true));
		Files.add(new Alts("Alts", false, true));
		Files.add(new Modules("Modules", true, true));
		Files.add(new Binds("Binds", true, true));
		Files.add(new Values());
	}

	public void loadFiles() {
		for (CustomFile f : Files) {
			try {
				if (f.loadOnStart()) {
					f.loadFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveFiles() {
		for (CustomFile f : Files) {
			try {
				if (f.getClass() != Modules.class) {
					f.saveFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static CustomFile getFile(Class<? extends CustomFile> clazz) {
		for (CustomFile file : Files) {
			if (file.getClass() == clazz) {
				return file;
			}
		}
		return null;
	}

	public void makeDirectories() {
		if (!directory.exists()) {
			if (directory.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		if (!moduleDirectory.exists()) {
			if (moduleDirectory.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	}

	public static abstract class CustomFile {
		protected final File file;
		private final String name;
		private boolean load;

		public CustomFile(String name, boolean Module, boolean loadOnStart) {
			this.name = name;
			this.load = loadOnStart;
			if (Module) {
				this.file = new File(FileManager.moduleDirectory, String.valueOf(name) + ".Durax");
			} else {
				this.file = new File(FileManager.directory, String.valueOf(name) + ".Durax");
			}
			if (!this.file.exists()) {
				try {
					saveFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public final File getFile() {
			return this.file;
		}

		private boolean loadOnStart() {
			return this.load;
		}

		public final String getName() {
			return this.name;
		}

		public abstract void loadFile() throws IOException;

		public abstract void saveFile() throws IOException;
	}
}
