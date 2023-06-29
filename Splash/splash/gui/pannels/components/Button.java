package splash.gui.pannels.components;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import splash.Splash;
import splash.api.module.Module;
import splash.api.value.Value;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.managers.cfont.CFontRenderer;
import splash.gui.ClickGui;
import splash.utilities.system.ClientLogger;
import splash.utilities.system.MouseLocation;
import splash.utilities.time.Stopwatch;

public class Button {
	public long lastInteract;
	private Module mod;
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean hovered;
	public int animation;
	public boolean opened = false;
	private boolean clickable = false;
	private boolean isMiddleClick = false;
	private Stopwatch upTimer;
	private Stopwatch downTimer;
	
	public ArrayList<Component> settings = new ArrayList<Component>();
	
	public Button(Module mod) {
		this.mod = mod;
		
		for (Value s : Splash.INSTANCE.getValueManager().getValuesFrom(this.mod)) {
			if (s instanceof NumberValue) {
				this.settings.add(new Slider((NumberValue) s, this));
			}
			if (s instanceof BooleanValue) {
				this.settings.add(new Checkbox((BooleanValue) s, this));
			}
			if (s instanceof ModeValue) {
				this.settings.add(new ModeButton((ModeValue) s, this));
			}
		}
		upTimer = new Stopwatch();
		downTimer = new Stopwatch();
	}
	
	public void keyTyped(char typedChar, int keyCode) {
		for (Component s : this.settings) {
			s.keyTyped(typedChar, keyCode);
		}
        if (isMiddleClick()) {
            if (!Keyboard.getKeyName(keyCode).equalsIgnoreCase("ESCAPE")) {
                ClientLogger.printToMinecraft("Bound " + mod.getModuleDisplayName() + " to " + Keyboard.getKeyName(keyCode));
                mod.setModuleMacro(keyCode);
                setMiddleClick(false);
            } else {
                ClientLogger.printToMinecraft("Bound " + mod.getModuleDisplayName() + " to " + "NONE");
                mod.setModuleMacro(Keyboard.KEY_NONE);
                setMiddleClick(false);
            }
        }
	}
	
	public Module getMod() {
		return this.mod;
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
		font.drawString(this.mod.getModuleDisplayName() + getKey(), this.x + 5, this.y + (this.height / 2) - (font.getHeight() / 2), this.mod.isModuleActive() ? ClickGui.getPrimaryColor().getRGB() : new Color(175, 175, 175).getRGB());
		int addVal = 0;
		if (!settings.isEmpty()) {
			GL11.glPushMatrix(); 
			font.drawString(opened ? "+" : "-", x + width - 10, this.y + (this.height / 2) - (font.getHeight() / 2), new Color(175, 175, 175).getRGB());		
			GL11.glPopMatrix();
		}
		if (this.opened && !this.settings.isEmpty()) {
			
			addVal = this.height;
			if (this.animation > 0) {
				if (this.downTimer.delay(25)) {
					this.animation--;
					this.downTimer.reset();
				}
			}			 
			for (int i = 0; i < settings.size() - animation; i++) {
				addVal += settings.get(i).drawScreen(mouseX, mouseY, x, y + addVal);
			}
			addVal -= height;
			
		} else {
			if (this.animation < 0) {
				addVal = this.height;
				if (this.upTimer.delay(25)) {
					this.animation++;
					this.upTimer.reset();
				} 
				for (int i = 0; i < Math.abs(animation); i++) {
					addVal += settings.get(i).drawScreen(mouseX, mouseY, x, y + addVal);
				}
				addVal -= height;
			} else {

			}
		}
 
		return this.height + addVal;
	}
	
	public void mouseClicked(int x, int y, int button) {
		if (!clickable) return;
		this.hovered = this.isHovered(x, y);
		
		if (this.hovered && button == 0) {
			this.mod.activateModule();
		} else if (this.hovered && button == 1) {
			opened = !opened;
			if (opened) {
				lastInteract = System.currentTimeMillis();
				animation = settings.size();
			} else {
				animation = -settings.size();
			}
		} else if (hovered && button == 2) {
            if (opened) {
    			opened = !opened;
    			animation = -settings.size();
            }
            setMiddleClick(!isMiddleClick());
            if (!this.isMiddleClick()) {
                ClientLogger.printToMinecraft("Bound " + mod.getModuleDisplayName() + " to " + "NONE");
                mod.setModuleMacro(Keyboard.KEY_NONE);
                setMiddleClick(false);
            }
			
		} else if (this.opened) {
			for (Component sc : this.settings) {
				sc.mouseClicked(x, y, button);
			}
		}
	}
	
    public String getKey() {
        if (isMiddleClick()) {
            return " [" + Keyboard.getKeyName(mod.getModuleMacro()) + "]";
        } else {
            return "";
        }
    }
    
	private void setMiddleClick(boolean b) {
		isMiddleClick = b;
	}

	private boolean isMiddleClick() {
		return isMiddleClick;
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (!clickable) return;
		 if (this.opened) {
			for (Component sc : this.settings) {
				sc.mouseReleased(mouseX, mouseY, state);
			}
		 }
	}
	
	private boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY < y + height;
	}
	
	public int getWidth() {
		return this.width;
	}
}
