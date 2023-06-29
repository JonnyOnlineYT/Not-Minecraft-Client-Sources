
package volcano.summer.screen.clickgui.original.settings;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import volcano.summer.base.Summer;
import volcano.summer.client.value.ModeValue;
import volcano.summer.screen.clickgui.original.parts.Component;

public class ModePart extends Component {

	private boolean hovered;
	private ModulesPart parent;
	private ModeValue option;
	private int offset;
	private int x;
	private int y;

	public ModePart(ModeValue option, ModulesPart modPart, int offset) {
		this.parent = modPart;
		this.option = option;
		this.x = modPart.parent.getX() + modPart.parent.getWidth();
		this.y = modPart.parent.getY() + modPart.offset;
		this.offset = offset;
	}

	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}

	@Override
	public void render() {
		Gui.drawRect(this.parent.parent.getX() + 1, this.parent.parent.getY() + this.offset - 2,
				this.parent.parent.getX() + this.parent.parent.getWidth() - 2,
				this.parent.parent.getY() + this.offset + 10, 0xbb141414);
		GL11.glPushMatrix();
		Summer.mc.fontRendererObj.func_175063_a("Mode: " + option.getStringValue(), (x + 4), (y + 1), -1);
		GL11.glPopMatrix();
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButton(mouseX, mouseY) && (button == 0 || button == 1) && this.parent.open) {
			if (button == 0) {
				option.next();
			} else if (button == 1) {
				option.next();
			}
		}
	}

	public boolean isMouseOnButton(int x, int y) {
		if (x > this.x && x < this.x + 88 && y > this.y - 3 && y < this.y + 10) {
			return true;
		}
		return false;
	}
}
