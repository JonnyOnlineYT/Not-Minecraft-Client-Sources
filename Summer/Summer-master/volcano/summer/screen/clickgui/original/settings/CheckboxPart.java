package volcano.summer.screen.clickgui.original.settings;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import volcano.summer.base.Summer;
import volcano.summer.client.value.Value;
import volcano.summer.screen.clickgui.original.R3DUtil;
import volcano.summer.screen.clickgui.original.parts.Component;

public class CheckboxPart extends Component {

	private boolean hovered;
	private Value op;
	private ModulesPart parent;
	private int offset;
	private int x;
	private int y;

	public CheckboxPart(Value option, ModulesPart modulesPart, int offset) {
		this.op = option;
		this.parent = modulesPart;
		this.x = modulesPart.parent.getX() + modulesPart.parent.getWidth();
		this.y = modulesPart.parent.getY() + modulesPart.offset;
		this.offset = offset;
	}

	@Override
	public void render() {

		Gui.drawRect(this.parent.parent.getX() + 1, this.parent.parent.getY() + 10 + this.offset,
				this.parent.parent.getX() - 2 + this.parent.parent.getWidth() * 1,
				this.parent.parent.getY() + this.offset - 2, 0xbb141414);

		int bgColor = 0xFF505050;
		R3DUtil.INSTANCE.drawFilledCircle(this.parent.parent.getX() + 10, this.parent.parent.getY() + 4 + this.offset,
				3.5f, bgColor);

		R3DUtil.INSTANCE.drawCircle(this.parent.parent.getX() + 10, this.parent.parent.getY() + 4 + this.offset, 3.5f,
				bgColor);

		if ((boolean) op.getValue()) {
			R3DUtil.INSTANCE.drawFilledCircle(this.parent.parent.getX() + 10,
					this.parent.parent.getY() + 4 + this.offset, 3f, Summer.getColor().getRGB());

			R3DUtil.INSTANCE.drawCircle(this.parent.parent.getX() + 10, this.parent.parent.getY() + 4 + this.offset, 3f,
					Summer.getColor().getRGB());
		}
		if (hovered) {
			R3DUtil.INSTANCE.drawFilledCircle(this.parent.parent.getX() + 10,
					this.parent.parent.getY() + 4 + this.offset, 3f, 0x55111111);

			R3DUtil.INSTANCE.drawCircle(this.parent.parent.getX() + 10, this.parent.parent.getY() + 4 + this.offset, 3f,
					0x55111111);
		}
		GL11.glPushMatrix();
		Summer.mc.fontRendererObj.func_175063_a(this.op.getName(), (x + 20), (y + 1), -1);
		GL11.glPopMatrix();
	}

	@Override
	public void setOff(int newOff) {
		this.offset = newOff;
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = this.isMouseOnButton(mouseX, mouseY);
		this.y = this.parent.parent.getY() + this.offset;
		this.x = this.parent.parent.getX();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			op.setValue(!(boolean) op.getValue());
		}
	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 105 && y > this.y && y < this.y + 12;
	}
}
