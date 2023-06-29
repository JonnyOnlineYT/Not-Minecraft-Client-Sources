package volcano.summer.screen.clickgui.exeter;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module.Category;
import volcano.summer.client.util.CustomFont;

public final class ClickGui extends GuiScreen {
	private static ClickGui clickGui;
	public final CustomFont guiFont;
	private final ArrayList<Panel> panels;

	public ClickGui() {
		this.guiFont = new CustomFont("Segoe UI", 18.0f);
		this.panels = new ArrayList<Panel>();
		if (this.getPanels().isEmpty()) {
			this.load();
		}
	}

	public static ClickGui getClickGui() {
		return (ClickGui.clickGui == null) ? (ClickGui.clickGui = new ClickGui()) : ClickGui.clickGui;
	}

	private void load() {
		int x = -84;
		for (final Category moduleType : Category.values()) {
			final ArrayList<Panel> panels = this.panels;
			final String label = moduleType.name();
			x += 90;
			panels.add(new Panel(label, x, 4, true) {
				@Override
				public void setupItems() {
					final Object val$moduleType = moduleType;
					Summer.moduleManager.getMods().forEach(module -> {
						if (!module.getName().equalsIgnoreCase("ClickGui")) {
							if (module.category.equals(val$moduleType)) {
								this.addButton(new ModuleButton(module));
							}
						}
					});
				}
			});
		}
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		this.panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int clickedButton) {
		this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, clickedButton));
	}

	@Override
	public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
		this.panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, releaseButton));
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public final ArrayList<Panel> getPanels() {
		return this.panels;
	}
}
