package splash.gui.pannels.components;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import splash.Splash;
import splash.api.config.ClientConfig;
import splash.api.module.Module;
import splash.api.value.Value;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.managers.cfont.CFontRenderer;
import splash.gui.ClickGui;
import splash.utilities.system.ClientLogger;
import splash.utilities.time.Stopwatch;

public class ConfigButton {

    private ClientConfig host;
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean hovered;
	public int animation;
	public boolean opened = false;
	private boolean clickable = false;
 
	
	public ConfigButton(ClientConfig host) {
		this.host = host;
	}
	
	public void keyTyped(char typedChar, int keyCode) {
 
	}
	
	public ClientConfig getMod() {
		return this.host;
	}
	
	private float lastRed = (float)ClickGui.getSecondaryColor(false).getRed() / 255F;
	private float lastGreen = (float)ClickGui.getSecondaryColor(false).getGreen() / 255F;
	private float lastBlue = (float)ClickGui.getSecondaryColor(false).getBlue() / 255F;
	
	public int drawScreen(int mouseX, int mouseY, int x, int y, int width, boolean open) {
		this.clickable = open;
		this.x = x;
		this.y = y;
		this.height = 15;
		this.width = width;
		this.hovered = this.isHovered(mouseX, mouseY);
		
		Color correctColor = ClickGui.getSecondaryColor(false);
		if (this.hovered) {
			int dark = 8;
			correctColor = new Color(Math.max(correctColor.getRed() - dark, 0), Math.max(correctColor.getGreen() - dark, 0), Math.max(correctColor.getBlue() - dark, 0));
		}
		
		float speed = 256F / (float)Minecraft.getMinecraft().getDebugFPS();
		lastRed += (((float)correctColor.getRed() / 255F) - lastRed) / speed;
		lastGreen += (((float)correctColor.getGreen() / 255F) - lastGreen) / speed;
		lastBlue += (((float)correctColor.getBlue() / 255F) - lastBlue) / speed;
		
		lastRed = Math.max(0, Math.min(1, lastRed));
		lastGreen = Math.max(0, Math.min(1, lastGreen));
		lastBlue = Math.max(0, Math.min(1, lastBlue));
		
		Gui.drawRect(x, y, x + width, y + height, new Color(lastRed, lastGreen, lastBlue, (float)ClickGui.getSecondaryColor(false).getAlpha() / 255F).getRGB());
		CFontRenderer font = Splash.getInstance().getFontRenderer();
		font.drawString(host.getConfigName(), this.x + 5, this.y + (this.height / 2) - (font.getHeight() / 2), new Color(175, 175, 175).getRGB());
		return this.height;
	}
	
	public void mouseClicked(int x, int y, int button) {
		if (!clickable) return;
		this.hovered = this.isHovered(x, y);

		if (this.hovered && button == 0) {
            ClientLogger.printToMinecraft("Loaded config " + host.getConfigName());
            Splash.getInstance().getConfigManager().loadConfig(host);
		//	Splash.getInstance().clickgui.reload();

		} 
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (!clickable) return;
 
	}
	
	private boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY < y + height;
	}
	
	public int getWidth() {
		return this.width;
	}
}
