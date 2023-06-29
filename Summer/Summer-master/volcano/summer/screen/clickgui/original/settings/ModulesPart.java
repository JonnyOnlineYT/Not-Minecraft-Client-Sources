package volcano.summer.screen.clickgui.original.settings;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;
import volcano.summer.screen.clickgui.original.R3DUtil;
import volcano.summer.screen.clickgui.original.parts.Component;
import volcano.summer.screen.clickgui.original.parts.Frame;

public class ModulesPart extends Component {

	private Module mod;
	public Frame parent;
	public int offset;
	private boolean binding;
	private boolean isHovered;
	public ArrayList<Component> subcomponents;
	public boolean open;

	public ModulesPart(Module mod, Frame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList();
		this.open = false;
		int opY = offset + 24;
		if (Summer.valueManager.getModValues(mod) != null) {
			for (Value value : Summer.valueManager.getModValues(mod)) {
				if (value instanceof ModeValue) {
					ModePart mode = new ModePart((ModeValue) value, this, opY);
					this.subcomponents.add(mode);
					opY += 12;
				} else if (value instanceof ClampedValue) {
					SliderPart sliderPart = new SliderPart((ClampedValue) value, this, opY);
					this.subcomponents.add(sliderPart);
					opY += 12;
				} else if (value.getValue() instanceof Boolean) {
					CheckboxPart check = new CheckboxPart(value, this, opY);
					this.subcomponents.add(check);
					opY += 12;
				}
			}
		}
	}

	@Override
	public void setOff(int newOff) {
		this.offset = newOff;
		int opY = this.offset + 12;
		for (Component comp : this.subcomponents) {
			comp.setOff(opY);
			opY += 12;
		}
	}

	@Override
	public void render() {
		if (this.isHovered && Mouse.isButtonDown(2)) {
			this.binding = true;
		}
		Gui.drawRect(this.parent.getX() + 1, this.parent.getY() - 2 + this.offset,
				this.parent.getX() + this.parent.getWidth() - 2, this.parent.getY() + 11 + this.offset,
				this.isHovered ? new Color(100, 100, 100, 150).getRGB()
						: parent.isDragging ? 0xbb0F0F0F : new Color(15, 15, 15, 255).getRGB());

		R3DUtil.INSTANCE.drawCentredStringWithShadow(this.binding ? "" : this.mod.getName(),
				this.parent.getX() + (parent.getWidth() / 2) - 2, this.parent.getY() + this.offset + 0,
				this.mod.getState() ? Summer.getColor().getRGB() : new Color(150, 150, 150).getRGB());

		GL11.glPushMatrix();
		String keyName = "null";
		try {
			keyName = Keyboard.getKeyName(mod.getBind());
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		Summer.mc.fontRendererObj.func_175063_a(this.binding ? "Press... [" + keyName + "]" : "",
				this.parent.getX() + 5, ((this.parent.getY() + this.offset) + 1), new Color(150, 150, 150).getRGB());
		GL11.glPopMatrix();
		for (Value value : Summer.valueManager.getModValues(mod)) {
			if (value.getValue() != null && !binding) {
				R3DUtil.INSTANCE.drawArrow(this.parent.getX() + 65 + 13, this.parent.getY() + this.offset + 2,
						this.open, new Color(150, 150, 150).getRGB());
			}
		}

		if (this.open && !this.subcomponents.isEmpty()) {
			for (Component comp : this.subcomponents) {
				comp.render();
			}
		}
	}

	@Override
	public int getHeight() {
		if (this.open) {
			return (int) (12 * (this.subcomponents.size() + 1.04));
		}
		return 12;
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.parent.refresh();
		this.isHovered = this.isMouseOnButton(mouseX, mouseY);
		if (!this.subcomponents.isEmpty()) {
			for (Component comp : this.subcomponents) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (this.isMouseOnButton(mouseX, mouseY) && button == 2 && this.parent.open) {
			this.binding = !this.binding;
		}
		if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && !this.mod.getName().equalsIgnoreCase("ClickGui")) {
			this.mod.toggleMod();
			parent.dopulse = true;
			parent.pulsey = parent.y + offset;
		}
		if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
		}
		for (Component comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void keyTyped(char typedChar, int key) {
		if (this.binding) {
			if (key == Keyboard.KEY_ESCAPE || key == Keyboard.KEY_DELETE) {
				this.mod.setBind(0);
				Summer.tellPlayer(mod.getName() + " has been unbound.");
				this.binding = false;
			} else {
				this.mod.setBind(key);
				Summer.tellPlayer(mod.getName() + " has been bound to " + Keyboard.getKeyName(key) + ".");
				this.binding = false;
			}
		}
	}

	private boolean isMouseOnButton(int x, int y) {
		return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth()
				&& y > this.parent.getY() + this.offset - 3 && y < this.parent.getY() + 10 + this.offset;
	}
}
