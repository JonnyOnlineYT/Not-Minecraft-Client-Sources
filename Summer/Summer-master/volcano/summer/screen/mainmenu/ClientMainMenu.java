package volcano.summer.screen.mainmenu;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import net.minecraft.client.Minecraft;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.file.FileManager;

public class ClientMainMenu extends PanoramaScreen {
	private static int key;
	private static final GuiModdedMainMenu menuModded;

	@Override
	public void initGui() {
		this.load();
		if (this.getClass().equals(ClientMainMenu.class)) {
			this.display();
		}
	}

	private void load() {
		String file = "";
		try {
			file = FileUtils.readFileToString(this.getFile());
		} catch (IOException e) {
			return;
		}
		for (final String line : file.split("\n")) {
			if (line.contains("key")) {
				final String[] split = line.split(":");
				if (split.length > 1) {
					try {
						ClientMainMenu.key = Integer.parseInt(split[1]);
					} catch (NumberFormatException ex) {
					}
				}
			}
		}
	}

	@Override
	protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
		if (keyCode == ClientMainMenu.key) {
			this.display();
		}
	}

	private void display() {
		Minecraft.getMinecraft().displayGuiScreen(new GuiModdedMainMenu());
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void save() {
		try {
			FileUtils.write(this.getFile(), "Swap key (Toggles menus):" + ClientMainMenu.key);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		final File file = new File(this.getFolder().getAbsolutePath() + File.separator + "MainMenu.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public File getFolder() {
		final File folder = new File(Summer.getDir().getAbsolutePath() + File.separator + FileManager.Files);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}

	static {
		ClientMainMenu.key = 41;
		menuModded = new GuiModdedMainMenu();
	}
}