package volcano.summer.screen.clickgui.original.parts;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;
import volcano.summer.base.manager.module.Module.Category;
import volcano.summer.screen.clickgui.original.R3DUtil;
import volcano.summer.screen.clickgui.original.settings.ModulesPart;

public class Frame {

	public ArrayList<Component> components;
	public Category category;
	public boolean open;
	public int width;
	public int y;
	public int x;
	public int barHeight;
	public boolean isDragging;
	public int dragX;
	public int dragY;

	public Frame(Category cat) {
		this.components = new ArrayList();
		this.category = cat;
		this.width = 88;
		this.x = 5;
		this.y = 5;
		this.barHeight = 18;
		this.dragX = 0;
		this.open = true;
		this.isDragging = false;
		int tY = this.barHeight;
		for (Module mod : Summer.moduleManager.getModsInCategory(this.category)) {
			ModulesPart modButton = new ModulesPart(mod, this, tY);
			this.components.add(modButton);
			tY += 24;
		}
	}

	public void setupItems() {
	}

	public ArrayList<Component> getComponents() {
		return this.components;
	}

	public void setX(double newX) {
		this.x = (int) newX;
	}

	public void setY(double newY) {
		this.y = (int) newY;
	}

	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}

	public boolean isOpen() {
		return this.open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public double pulse;
	public double pulsex;
	public double pulsey;

	public boolean dopulse;

	public void renderFrame(final FontRenderer fontRenderer) {
		this.barHeight = 18;
		this.width = 90;
		Gui.drawRect(this.x - 1, this.y - 3, this.x + this.width + 1, this.y + this.barHeight - 2,
				isDragging ? new Color(25, 181, 254, 100).getRGB() : Summer.getColor().getRGB());

		GL11.glPushMatrix();
		R3DUtil.INSTANCE.drawCentredStringWithShadow(this.category.name(), this.x + this.width / 2, y + 4, -1);

		GL11.glPopMatrix();

		if (this.open && !this.components.isEmpty()) {
			for (final Component component : this.components) {
				component.render();
			}
		}
		if (dopulse) {
			pulse += 1.5;
			if (pulse >= 80) {
				pulse = 0;
				dopulse = false;
			}
			GL11.glPushMatrix();
			prepareScissorBox(x + 1, y + barHeight - 2, x + width - 2,
					y + getComponents().size() * 12 + barHeight - 2 + getSize());

			GL11.glEnable(3089);
			R3DUtil.INSTANCE.drawFilledCircle(x + width / 2, pulsey, pulse, 0x20ffffff);
			GL11.glDisable(3089);
			GL11.glPopMatrix();
		}
	}

	public void prepareScissorBox(float x, float y, float x2, float y2) {
		ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);
		int factor = scale.getScaleFactor();
		GL11.glScissor((int) (x * factor), (int) ((scale.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor),
				(int) ((y2 - y) * factor));
	}

	public void refresh() {
		int off = this.barHeight;
		for (Component comp : this.components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}

	public int getSize() {
		int size = 0;
		for (Component comp : components) {
			if (((ModulesPart) comp).open) {
				if (((ModulesPart) comp).subcomponents != null) {
					size += ((ModulesPart) comp).subcomponents.size() * 12;
				}
			}
		}
		return size;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public void updatePosition(int mouseX, int mouseY) {
		if (this.isDragging) {
			this.setX(mouseX - this.dragX);
			this.setY(mouseY - this.dragY);
		}
	}

	public boolean isWithinHeader(int x, int y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
	}

}
