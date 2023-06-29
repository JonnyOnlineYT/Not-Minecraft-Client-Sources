package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.Display;

import ru.wendoxd.WexSide;
import ru.wendoxd.ui.altmanager.altManager.GuiAltManager;
import ru.wendoxd.utils.visual.shaders.Shader;
import ru.wendoxd.utils.visual.shaders.ShaderShell;

import java.io.IOException;

public class GuiMainMenu extends GuiScreen {

	public static ShaderShell textureCircle = ShaderShell.CIRCLE_TEXTURE_SHADER;
	public static Shader shader = new Shader();
	public static long time = System.currentTimeMillis();

	public GuiMainMenu() {
		shader.init();
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList
				.add(new GuiButton(0, width / 2 - 70, height / 2 - 25, 140, 15, I18n.format("menu.singleplayer")));
		this.buttonList.add(new GuiButton(1, width / 2 - 70, height / 2, 140, 15, I18n.format("menu.multiplayer")));
		this.buttonList
				.add(new GuiButton(2, width / 2 - 70, height / 2 + 25, 140, 15, "AltManager"));
		this.buttonList.add(new GuiButton(3, width / 2 - 70, height / 2 + 50, 140, 15, I18n.format("menu.options")));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0:
			this.mc.displayGuiScreen(new GuiWorldSelection(this));
			break;
		case 1:
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
			break;
		case 2:
			this.mc.displayGuiScreen(new GuiAltManager());
			break;
		case 3:
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
			break;
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (Display.isActive()) {
			ScaledResolution sr = new ScaledResolution(mc, 2);
			WexSide.drawBackground(width, height);
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
	}
}
