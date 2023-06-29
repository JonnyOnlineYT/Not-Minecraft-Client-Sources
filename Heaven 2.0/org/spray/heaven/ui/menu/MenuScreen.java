package org.spray.heaven.ui.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.avx.AVXContext;
import org.spray.heaven.ui.draggable.DragManager;
import org.spray.heaven.ui.avx.theme.ThemeManager;
import org.spray.heaven.ui.avx.view.views.ListView;

import net.minecraft.client.gui.GuiScreen;
import org.spray.heaven.util.render.Drawable;

public class MenuScreen extends GuiScreen {

	private final List<ListView> moduleViews = new ArrayList<>();
	private final ThemeManager themeManager;
	private final MenuPanel panel;

	public MenuScreen() {
		themeManager = new ThemeManager();
		AVXContext.init(themeManager);

		panel = new MenuPanel();
		panel.setDimension(20, 20, 340, 230);
		panel.setResizable(true);
		panel.setMin(408, 209);
	}

	@Override
	public void initGui() {

	}
	
	@Override
	public void updateScreen() {
		panel.tick();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		panel.render(mouseX, mouseY, partialTicks);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClicked(mouseX, mouseY, button);
		panel.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int button) {
		super.mouseReleased(mouseX, mouseY, button);
		panel.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public void keyTyped(char chr, int keyCode) throws IOException {
		if (keyCode == 1 || keyCode == Wrapper.getModule().get("ClickGUI").getKey()) {
			close();
		}
	}

	private void close() {
		Wrapper.getConfig().saveDefault();
		DragManager.saveDragData();
		mc.currentScreen = null;
		mc.displayGuiScreen(null);
	}

}
