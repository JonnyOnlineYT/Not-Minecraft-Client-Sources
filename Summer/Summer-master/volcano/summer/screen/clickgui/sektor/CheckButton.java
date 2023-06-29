package volcano.summer.screen.clickgui.sektor;

import org.lwjgl.opengl.GL11;

public class CheckButton extends Component {
	protected boolean held;
	protected boolean selected;
	protected String title;

	public CheckButton(String title, Component parent) {
		this.title = title;
		this.selected = false;
		this.parent = parent;
		this.isVisible = false;
		this.height = 14;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks, int parX, int parY) {
		this.onUpdate();
		GL11.glPushMatrix();
		GL11.glTranslatef(this.parent.x, this.parent.y, 0.0f);
		this.absX = parX + this.x;
		this.absY = parY + this.y;
		this.renderWidth = this.width;
		this.renderHeight = this.height;
		if (this.selected) {
			Render.rectangleBorderedGradient(this.x + this.width - this.height - 3, this.y, this.x + this.width - 3,
					this.y + this.height, new int[] { -13369137, -16777053 }, new int[] { -15921907 }, 0.5);
			GL11.glPushMatrix();
			GL11.glDisable(3553);
			GL11.glEnable(2848);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			GL11.glLineWidth(2.2f);
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			GL11.glBegin(3);
			GL11.glVertex2d(this.x + this.width - 6.5, this.y + 3);
			GL11.glVertex2d(this.x + this.width - 11.5, this.y + 10);
			GL11.glVertex2d(this.x + this.width - 13.5, this.y + 8);
			GL11.glEnd();
			GL11.glDisable(3042);
			GL11.glEnable(3553);
			GL11.glPopMatrix();
		} else {
			Render.rectangleBorderedGradient(this.x + this.width - this.height - 3, this.y, this.x + this.width - 3,
					this.y + this.height, new int[] { -14606047, -15066598 }, new int[] { -15921907 }, 0.5);
		}
		CheckButton.mc.fontRendererObj.func_175063_a(this.title, this.x + 3,
				this.y + this.height / 2 - CheckButton.mc.fontRendererObj.FONT_HEIGHT / 2, -657931);
		GL11.glPopMatrix();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseX >= this.absX && mouseX <= this.absX + this.width && mouseY >= this.absY
				&& mouseY <= this.absY + this.height) {
			this.held = true;
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (mouseX >= this.absX && mouseX <= this.absX + this.width && mouseY >= this.absY
				&& mouseY <= this.absY + this.height && this.held) {
			this.held = false;
			this.onPressed();
		}
	}

	@Override
	public void keyPressed(int key) {
	}

	public void onPressed() {
	}

	public void onUpdate() {
	}
}
