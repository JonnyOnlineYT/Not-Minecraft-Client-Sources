package volcano.summer.screen.clickgui.original;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import volcano.summer.base.manager.module.Module.Category;
import volcano.summer.screen.clickgui.original.parts.Component;
import volcano.summer.screen.clickgui.original.parts.Frame;

public class ClickGUI extends GuiScreen {

	public ArrayList<Frame> getFrames() {
		return frames;
	}

	private ArrayList<Frame> frames;

	public ClickGUI() {
		this.frames = new ArrayList();
		int frameX = 5;
		Category[] values;
		for (int length = (values = Category.values()).length, i = 0; i < length; ++i) {
			Category category = values[i];
			Frame frame = new Frame(category);
			frame.setX(frameX);
			this.frames.add(frame);
			frameX += frame.getWidth() + 1;
		}
	}

	@Override
	public void initGui() {
		try {
			// if (Summer.fileManager.getFile(Gui.class).getFile().exists())
			// Summer.fileManager.getFile(Gui.class).loadFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		try {
			this.drawDefaultBackground();
			for (Frame frame : this.frames) {
				frame.renderFrame(this.fontRendererObj);
				frame.updatePosition(mouseX, mouseY);
				for (Component comp : frame.getComponents()) {
					comp.updateComponent(mouseX, mouseY);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		for (Frame frame : this.frames) {
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if (frame.isOpen() && !frame.getComponents().isEmpty()) {
				for (Component component : frame.getComponents()) {
					component.mouseClicked(mouseX, mouseY, mouseButton);
				}
			}

		}

	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		for (Frame frame : this.frames) {
			frame.isDragging = false;
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for (Frame frame : this.frames) {
			if (frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
				for (Component component : frame.getComponents()) {
					component.keyTyped(typedChar, keyCode);
				}
			}
		}
		if (keyCode == 1) {
			this.mc.displayGuiScreen(null);
		}
	}

	protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
		for (Frame frame : this.frames) {
			frame.setDrag(false);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		mc.entityRenderer.theShaderGroup = null;
		try {
			// Summer.fileManager.getFile(Gui.class).saveFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
