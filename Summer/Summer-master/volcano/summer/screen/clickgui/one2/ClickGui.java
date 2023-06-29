package volcano.summer.screen.clickgui.one2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;
import volcano.summer.base.manager.module.Module.Category;
import volcano.summer.screen.clickgui.one2.elements.Element;
import volcano.summer.screen.clickgui.one2.elements.Panel;
import volcano.summer.screen.clickgui.one2.elements.base.ElementButton;
import volcano.summer.screen.clickgui.one2.elements.base.OnePanel;

public class ClickGui extends GuiScreen {
	private List<Panel> panels;
	private Panel top;
	private File settings;
	public static ClickGui INSTANCE;

	public ClickGui() {
		this.panels = new ArrayList<Panel>();
		this.settings = new File(Minecraft.getMinecraft().mcDataDir + "\\" + Summer.name + "\\" + "gui");
		this.loadPanels();
		INSTANCE = this;
	}

	private void loadPanels() {
		try {
			if (this.settings.exists()) {
				@SuppressWarnings("resource")
				final BufferedReader bufferedReader = new BufferedReader(new FileReader(this.settings));
				String readString = "";
				while ((readString = bufferedReader.readLine()) != null) {
					final String[] split = readString.split(":");
					final Panel panel = new OnePanel(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]),
							Boolean.parseBoolean(split[3]), Boolean.parseBoolean(split[4]), this);
					this.panels.add(panel);
				}
				this.setPanelOnTop(this.panels.get(this.panels.size() - 1));
			} else {
				final PrintWriter printWriter = new PrintWriter(new FileWriter(this.settings));
				printWriter.println();
				printWriter.close();
				int x = 2;
				Category[] values;
				for (int length = (values = Category.values()).length, i = 0; i < length; ++i) {
					final Category category = values[i];
					final Panel panel2 = new OnePanel(
							String.valueOf(category.name().substring(0, 1))
									+ category.name().substring(1, category.name().length()).toLowerCase(),
							x, 2, false, false, this);
					this.panels.add(panel2);
					x += panel2.getWidth() + 2;
				}
				this.setPanelOnTop(this.panels.get(this.panels.size() - 1));
				this.saveSettings();
			}
			for (final Panel panel3 : this.panels) {
				int y = panel3.getHeight();
				for (final Module mod : Summer.moduleManager.getMods()) {
					if (mod.getCategory().toString().equals(panel3.getTitle().toUpperCase())) {
						final Element element = new ElementButton(mod, panel3.getPosX() + 2, panel3.getPosY() + y,
								panel3);
						panel3.addElement(element);
						y += element.getHeight() + 2;
					}
				}
				panel3.setOpenHeight(y);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveSettings() {
		try {
			final PrintWriter printWriter = new PrintWriter(new FileWriter(this.settings));
			for (final Panel panel : this.panels) {
				printWriter.println(String.valueOf(panel.getTitle()) + ":" + panel.getPosX() + ":" + panel.getPosY()
						+ ":" + panel.isExpanded() + ":" + panel.isPinned());
			}
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPanelOnTop(final Panel panel) {
		this.top = panel;
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button) throws IOException {
		for (final Panel panel : this.panels) {
			panel.mouseClicked(mouseX, mouseY, button);
		}
		super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float button) {
		if (volcano.summer.client.modules.render.ClickGui.blur.getValue()) {
			this.mc.entityRenderer.func_175069_a(EntityRenderer.shaderResourceLocations[18]);
		}
		// this.drawDefaultBackground();
		if (this.panels.indexOf(this.top) != this.panels.size() - 1) {
			this.panels.remove(this.panels.indexOf(this.top));
			this.panels.add(this.top);
		}
		for (final Panel panel : this.panels) {
			panel.draw(mouseX, mouseY);
		}
	}

	public void drawPinned() {
		for (final Panel panel : this.panels) {
			if (panel.isPinned()) {
				panel.draw(0, 0);
			}
		}
	}
}
