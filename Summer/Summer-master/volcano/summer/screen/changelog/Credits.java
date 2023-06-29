package volcano.summer.screen.changelog;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import volcano.summer.base.Summer;

public final class Credits extends GuiScreen {
	private final List<String> log;

	public Credits() {
		this.log = new ArrayList();
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		this.mc.displayGuiScreen(null);
	}

	@Override
	public void drawScreen(int x, int y, float z) {
		drawDefaultBackground();
		super.drawScreen(x, y, z);
		drawCenteredString(fontRendererObj, "§f§kf§eSummer " + Summer.version + " Credits!§f§k1", width / 2, 2, -1);
		int logY = 50;
		for (String text : this.log) {
			drawString(fontRendererObj, text, width / 6, logY, -1);
			logY += fontRendererObj.FONT_HEIGHT;
		}
		if (this.log.isEmpty()) {
			drawString(fontRendererObj, "§eLoading...", width / 6, logY, -1);
		}
	}

	@Override
	public void initGui() {
		this.log.add("§fWhat I Skidded:");
		this.log.add("");
		this.log.add("§aCowabunga - Cake");
		this.log.add("§aStep NCP - Zues");
		this.log.add("§aBowFly - Timeless");
		this.log.add("§aStep 2.5 - Nebula");
		this.log.add("§aScaffold - Zues(Xtasy)");
		this.log.add("§aInvCleaner - Zues(Xtasy)");
		this.buttonList.clear();
		this.buttonList.add(new GuiCustomButton(0, width / 2 - 100, this.height - 40, "Back"));
	}
}
