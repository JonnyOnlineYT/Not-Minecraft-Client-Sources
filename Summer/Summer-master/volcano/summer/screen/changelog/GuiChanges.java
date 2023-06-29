package volcano.summer.screen.changelog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import volcano.summer.base.Summer;
import volcano.summer.screen.mainmenu.GuiModdedMainMenu;

public final class GuiChanges extends GuiScreen {
	private final List<Change> changes = new ArrayList();
	private ChangeSlot changeSlot;

	public GuiChanges() {
		this.changes.add(new Change("New arraylist", Type.ADDED));
		this.changes.add(new Change("FastChat in HUD", Type.ADDED));
		this.changes.add(new Change("Fixed many many bugs and i'm extremely lazy lul", Type.FIXED));
		this.changes.add(new Change("Added clicks for modules", Type.ADDED));
		this.changes.add(new Change("Fixed up scaffold", Type.FIXED));
		this.changes.add(new Change("Rainbow for wings", Type.ADDED));
		this.changes.add(new Change("InvCleaner now toggles when finished", Type.FIXED));
		this.changes.add(new Change("Sword block animations", Type.ADDED));
		this.changes.add(new Change("Import alts button in alt manager", Type.ADDED));
		this.changes.add(new Change("Brought back Discord RPC", Type.ADDED));
		this.changes.add(new Change("Hypixel autoblock", Type.ADDED));
		this.changes.add(new Change("Hypixel crits", Type.ADDED));
		this.changes.add(new Change("Added Altening support for you nerds", Type.ADDED));
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.changeSlot.func_178039_p();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.changeSlot.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(this.fontRendererObj,
				String.format("§f§kf§eSummer b" + Summer.version + " ChangeLog!§f§k1§e§f",
						new Object[] { "Summer", Integer.valueOf(3) }),
				this.width / 2, 4, -1);
		this.fontRendererObj.func_175063_a(
				String.format("Changes: %s", new Object[] { Integer.valueOf(this.changes.size()) }), 2.0F, 2.0F, -1);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 76, this.height - 26, 149, 20, "Back"));
		this.changeSlot = new ChangeSlot(this);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		if (keyCode == 200) {
			if (this.changeSlot.selected == 0) {
				this.changeSlot.selected = this.changes.size();
			}
			this.changeSlot.selected -= 1;
		}
		if (keyCode == 208) {
			if (this.changeSlot.selected == this.changes.size()) {
				this.changeSlot.selected = 0;
			}
			this.changeSlot.selected += 1;
		}
	}

	@Override
	public void actionPerformed(GuiButton button) {
		try {
			super.actionPerformed(button);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiModdedMainMenu());
		}
	}

	public final List<Change> getChanges() {
		return this.changes;
	}

	public static enum Type {
		ADDED, REMOVED, FIXED;

		private Type() {
		}
	}
}