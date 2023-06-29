package splash.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldType;
import splash.Splash;
import splash.api.config.ClientConfig;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.Value;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.modules.visual.GUI;
import splash.client.modules.visual.GUI.Mode;
import splash.gui.huzini.Frame;
import splash.gui.huzini.components.components.ComponentContainer;
import splash.gui.huzini.components.components.ConfigButton;
import splash.gui.huzini.components.components.ModButton;
import splash.gui.huzini.components.components.Slider;
import splash.gui.huzini.components.components.Spinner;
import splash.gui.huzini.components.components.ValueButton;
import splash.gui.pannels.Panel;

/*
 * Auther: Seb
 * Seb is the best
 * buy sight lol
 */
public class ClickGui extends GuiScreen {
	
	public static ArrayList<Panel> panels = new ArrayList<Panel>();
	public static boolean dragging = false;
	public boolean createdPanels;//<- This is ghetto as fuck
	private Frame mainFrame;
	public Mode mode;
	
	public void reload(boolean reloadUserInterface) {
		switch (mode) {
			case HUZINI:
			break;
			case PANEL:
				for (Panel p : this.panels) {
					p.reload();
				}
			break;
		}
	}

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
	}

    @Override
    public void initGui() {
        super.initGui();
    }
	
	public ClickGui() {
		GUI cgui = (GUI)Splash.getInstance().getModuleManager().getModuleByClass(GUI.class);
		mode = cgui.mode.getValue();
		switch (mode) {
			case HUZINI: 
				build();
			break;
			case PANEL:
				int x = 3;
				int y = 5;
				int count = 0;
				if (!createdPanels && panels.size() != ModuleCategory.values().length) {
					for (ModuleCategory c : ModuleCategory.values()) {
						Panel p = new Panel(x, y, c);
						panels.add(p);
						x += p.getWidth() + 5;
						count++;
						if (count % 3 == 0) {
							y += 50;
							x = 3;
						}
						createdPanels = (panels.size() == ModuleCategory.values().length);
					}
				}
			break;
		}
	}
	
    public void build() {
        mainFrame = new Frame(Splash.getInstance().getClientName(), 2, 100, 200, 100);
        int y = mainFrame.getY() + 14;
        for (ModuleCategory category : ModuleCategory.values()) {
            ComponentContainer container = new ComponentContainer(mainFrame, WordUtils.capitalizeFully(category.name()), mainFrame.getX(), y, 196, 12);
            ComponentContainer configContainer = new ComponentContainer(mainFrame, WordUtils.capitalizeFully("configs"), mainFrame.getX(), y, 196, 12);
            int modY = 13;
            int configY = 13;

            if (category == ModuleCategory.CONFIGS) {
                for (ClientConfig clientConfig : Splash.getInstance().getConfigManager().getContents()) {
                    ConfigButton configButton = new ConfigButton(container, clientConfig, 0, configY, 196, 12);
                    container.getComponents().add(configButton);
                    configY += 13;

                    configContainer.getComponents().add(configButton);
                }
            }

            for (Module p : Splash.getInstance().getModuleManager().getContents()) {
                if (p.getModuleCategory() == category) {
                    ModButton button = new ModButton(container, p, 0, modY, 196, 12);
                    int propertyY = 1;
                    for (Value property : Splash.getInstance().getValueManager().getValuesFrom(p)) {
                        if (property instanceof BooleanValue) {
                            button.getComponents().add(new ValueButton(button, container, (BooleanValue) property, 0, propertyY, 196, 12));
                            propertyY += 13;
                        }
                        if (property instanceof NumberValue) {
                            button.getComponents().add(new Slider(button, container, (NumberValue) property, 0, propertyY, 196, 12));
                            propertyY += 13;
                        }
                        if (property instanceof ModeValue) {
                            button.getComponents().add(new Spinner(button, container, (ModeValue) property, 0, propertyY, 196, 12));
                            propertyY += 13;
                        }
                    }
                    container.getComponents().add(button);
                    modY += 13;
                }
            }
            y += 13;
            mainFrame.getContainers().add(container);
        }
        mainFrame.setHeight((mainFrame.getContainers().size() * 12) + 14);
    }

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GUI cgui = (GUI)Splash.getInstance().getModuleManager().getModuleByClass(GUI.class);
		if (mode != cgui.mode.getValue()) {
			this.onGuiClosed();
			Minecraft.getMinecraft().displayGuiScreen(new ClickGui());
		}
		switch (mode) {
			case HUZINI:
		        super.drawScreen(mouseX, mouseY, partialTicks);
		        int mainFrameHeight = 15;
		        for (ComponentContainer container : mainFrame.getContainers()) {
		            mainFrameHeight += container.getHeight() + 1;
		        }
		        mainFrame.draw(mouseX, mouseY, partialTicks);
		        mainFrame.setHeight(mainFrameHeight);
			break;
			case PANEL:
				ScaledResolution scalRes = new ScaledResolution(Minecraft.getMinecraft());
				drawGradientRect(0, 0, scalRes.getScaledWidth(), scalRes.getScaledHeight(), 0x00001215, Splash.getInstance().getClientColorNORGB().getRGB());
				panels.sort((a, b) -> Double.compare(a.lastClickedMs, b.lastClickedMs));
		    	for (int i = 0; i < panels.size(); i++) {
		    		panels.get(i).onTop = i == 0; 
		    		panels.get(i).drawScreen(mouseX, mouseY);
		    	}
			break;
		}
    }
	
	@Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		switch (mode) {
		case HUZINI:
	        super.mouseClicked(mouseX, mouseY, mouseButton);
	        mainFrame.mouseClicked(mouseX, mouseY, mouseButton);
		break;
		case PANEL:
	    	for (Panel p : this.panels) {
	    		p.mouseClicked(mouseX, mouseY, mouseButton);
	    	}
		break;
		}
    }
	
	@Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
		switch (mode) {
		case HUZINI:
	        super.mouseReleased(mouseX, mouseY, state);
	        mainFrame.mouseReleased(mouseX, mouseY, state);
		break;
		case PANEL:
			for (Panel p : this.panels) {
				p.mouseReleased(mouseX, mouseY, state);
			}
		break;
		}
    }
	
    public boolean doesGuiPauseGame() {
        return false;
    }
	
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
		switch (mode) {
		case HUZINI: 
	        super.keyTyped(typedChar, keyCode);
	        mainFrame.keyTyped(typedChar, keyCode);
		break;
		case PANEL:
	    	for (Panel p : this.panels) {
	    		p.keyTyped(typedChar, keyCode);
	    	}
	    	super.keyTyped(typedChar, keyCode);
		break;
		}
    }
	
	public static Color getSecondaryColor(boolean setting) {
		return setting ? new Color(0, 0, 0, 200) : new Color(25, 25, 25, 200);
	}
	
	public static Color getPrimaryColor() {
		return new Color(Splash.INSTANCE.getClientColor());
	}
}
