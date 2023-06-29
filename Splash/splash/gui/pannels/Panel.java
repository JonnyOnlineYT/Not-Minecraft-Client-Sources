package splash.gui.pannels;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import splash.Splash;
import splash.api.config.ClientConfig;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.managers.cfont.CFontRenderer;
import splash.gui.ClickGui;
import splash.gui.pannels.components.Button;
import splash.gui.pannels.components.ConfigButton;
import splash.utilities.system.ClientLogger;
import splash.utilities.time.Stopwatch;

public class Panel {
	private Stopwatch upTimer;
	private Stopwatch downTimer;
	private int x;
	private int y;
	private int width = 115;
	private int height = 15;
	private int animation = 0;
	public double lastClickedMs = 0.0;
	private ModuleCategory category;
	private boolean open;
	public boolean onTop;
	private boolean dragging;
	private boolean closingModule;
	
	private ArrayList<Button> buttons = new ArrayList<Button>();

	private ArrayList<ConfigButton> cnfgbuttons = new ArrayList<ConfigButton>();
	
	public Panel(int x, int y, ModuleCategory cat) {
		this.x = x;
		this.y = y;
		category = cat;
		if (category == cat.CONFIGS) {
            for (ClientConfig clientConfig : Splash.getInstance().getConfigManager().getContents()) {
            	 ConfigButton cnfgbutton = new ConfigButton(clientConfig);
            	 cnfgbuttons.add(cnfgbutton);
            }
		} else {
			for (Module m : Splash.INSTANCE.getModuleManager().getModulesInCategory(category)) {
				buttons.add(new Button(m));
			}
		}
		upTimer = new Stopwatch();
		downTimer = new Stopwatch();
	}
	
	public void reload() {
		buttons.clear();
		cnfgbuttons.clear();
		if (category == ModuleCategory.CONFIGS) {
            for (ClientConfig clientConfig : Splash.getInstance().getConfigManager().getContents()) {
            	 ConfigButton cnfgbutton = new ConfigButton(clientConfig);
            	 cnfgbuttons.add(cnfgbutton);
            }
		} else {
			for (Module m : Splash.INSTANCE.getModuleManager().getModulesInCategory(category)) {
				buttons.add(new Button(m));
			}
		}
	}
	
	public void drawScreen(int mouseX, int mouseY) {
		if (dragging) {
			x = mouseX - (width / 2);
			y = mouseY - (height / 2);
		}
        GL11.glPushMatrix();
		Gui.drawRect(x - 1, y, x + width + 1, y + height, ClickGui.getPrimaryColor().getRGB());
		CFontRenderer font = Splash.INSTANCE.getFontRenderer();
		font.drawString(category.name(), x + 5, y + (height / 2) - (font.getHeight() /2), -1);
        GL11.glPopMatrix();
		width = 115;
		height = 20;
		int offset = height;
		buttons.sort((a, b) -> Double.compare(b.lastInteract, a.lastInteract));
		if (open) {
			if (animation > 0) {
				if (downTimer.delay(15)) {
					animation --;
					downTimer.reset();
				}
			}

			if (category == ModuleCategory.CONFIGS) {
				for (int i = 0; i < (cnfgbuttons.size() - animation); i++) {
					offset += cnfgbuttons.get(i).drawScreen(mouseX, mouseY, x, y + offset, width, open);
				}
			} else {
				for (int i = 0; i < (buttons.size() - animation); i++) {
					offset += buttons.get(i).drawScreen(mouseX, mouseY, x, y + offset, width, open);
				}
			}
		} else {
			if (animation < 0) {
				for (int i = 0; i < Math.abs(animation); i++) { 
					if (i < buttons.size() && category != ModuleCategory.CONFIGS) {
						if (buttons.get(i) != null && buttons.get(i).opened == false && buttons.get(i).animation == 0 && i == buttons.size()) {
							closingModule = false;
						}
					}
				}
				if (upTimer.delay(10) && (!closingModule || category != ModuleCategory.CONFIGS)) {
					animation++;
					upTimer.reset();
				}
				for (int i = 0; i < Math.abs(animation); i++) {
					if (category != ModuleCategory.CONFIGS) {
						if (i < buttons.size()) {
							if (buttons.get(i).opened) {
								animation = -buttons.get(i).settings.size();
								buttons.get(i).opened = false;
								closingModule = true;
							}
							offset += buttons.get(i).drawScreen(mouseX, mouseY, x, y + offset, width, open);
						}
					} else {
						if (i < cnfgbuttons.size()) {
							offset += cnfgbuttons.get(i).drawScreen(mouseX, mouseY, x, y + offset, width, open);
						}
					}
				}
			}
		}
	}
	
	public void keyTyped(char typedChar, int keyCode) {
		for (Button b : buttons) {
			b.keyTyped(typedChar, keyCode);
		}
	}
	
	public void mouseClicked(int x, int y, int button) { 
		if (isHovered(x, y)) {
			if (button == 1) { 
				lastClickedMs = (double)System.currentTimeMillis();
				open = !open; 
				if (open) {
					animation = category.equals(ModuleCategory.CONFIGS) ? cnfgbuttons.size() : buttons.size();
				} else {
					animation = category.equals(ModuleCategory.CONFIGS) ? -cnfgbuttons.size() : -buttons.size();
				}
			} else if (button == 0 && !ClickGui.dragging) {
				dragging = true;
				ClickGui.dragging = true;
				lastClickedMs = (double)System.currentTimeMillis();
			}
		} else { 
			if (category.equals(ModuleCategory.CONFIGS)) {
				for (int i = 0; i < cnfgbuttons.size(); i++) {
					cnfgbuttons.get(i).mouseClicked(x, y, button);
				}
			} else {
				for (int i = 0; i < buttons.size(); i++) {
					buttons.get(i).mouseClicked(x, y, button);
				}
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (dragging && state == 0) {
			dragging = false;
			ClickGui.dragging = false;
			lastClickedMs = (double)System.currentTimeMillis();
		}

		for (Button b : buttons) {
			b.mouseReleased(mouseX, mouseY, state);
		}
	}
	
	public double getLastClickedMs() {
		return lastClickedMs;
	}
	
	private boolean isHovered(int mouseX, int mouseY) {
		return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
	}
}
