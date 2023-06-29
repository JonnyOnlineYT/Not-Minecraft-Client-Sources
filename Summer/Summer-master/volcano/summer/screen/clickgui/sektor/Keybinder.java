package volcano.summer.screen.clickgui.sektor;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import volcano.summer.base.manager.module.Module;

public class Keybinder extends Component {
	private final Module mod;
	private boolean binding;

	public Keybinder(Module mod, Component parent) {
		this.mod = mod;
		this.parent = parent;
		this.width = parent.width - 2;
		this.height = 14;
		this.renderWidth = this.width;
		this.renderHeight = this.height;
		this.isVisible = false;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks, int parX, int parY) {
		GL11.glPushMatrix();
		GL11.glTranslatef(this.parent.x, this.parent.y, 0.0f);
		this.absX = parX + this.x;
		this.absY = parY + this.y;
		Render.rectangleBorderedGradient(this.x, this.y, this.width, this.y + this.height,
				new int[] { -14606047, -15066598 }, new int[] { -15921907 }, 0.5);
		if (mouseX >= this.absX && mouseX <= this.absX + this.width && mouseY >= this.absY
				&& mouseY <= this.absY + this.height) {
			Render.drawRect(this.x, this.y, this.width, this.y + this.height, 1610612736);
		}
		int textColor = -657931;
		Keybinder.mc.fontRendererObj.func_175063_a("Keybind", this.x + 3,
				this.y + this.height / 2 - Keybinder.mc.fontRendererObj.FONT_HEIGHT / 2, textColor);
		String kb = this.binding ? "..." : Keyboard.getKeyName(this.mod.getBind());
		Keybinder.mc.fontRendererObj.func_175063_a(kb,
				this.x + this.width - 4 - Keybinder.mc.fontRendererObj.getStringWidth(kb),
				this.y + this.height / 2 - Keybinder.mc.fontRendererObj.FONT_HEIGHT / 2, textColor);
		GL11.glPopMatrix();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseX >= this.absX && mouseX <= this.absX + this.width && mouseY >= this.absY
				&& mouseY <= this.absY + this.height && mouseButton == 0) {
			this.binding = true;
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	}

	@Override
	public void keyPressed(int key) {
		if (this.binding) {
			if (key == 211 || key == 1 || key == 14) {
				this.mod.setBind(0);
			} else {
				this.mod.setBind(key);
			}
			this.binding = false;
		}
	}
}
