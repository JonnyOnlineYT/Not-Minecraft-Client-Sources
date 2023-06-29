package volcano.summer.screen.clickgui.sektor;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Frame extends Component {
	public String title;
	private boolean isOpen;
	private boolean isDrag;
	private int offsetX;
	private int offsetY;
	private float targetScrollAmount = 0.0f;
	private float scrollAmount = 0.0f;
	public ArrayList<Component> items = new ArrayList();

	public Frame(String title) {
		this(title, 115, 15);
	}

	public Frame(String title, int width, int height) {
		this.title = title;
		this.x = 0;
		this.y = 0;
		this.width = width;
		this.height = height;
		this.isOpen = false;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks, int parX, int parY) {
		this.drag(mouseX, mouseY);
		float speedRatio = 144.0f / Minecraft.debugFPS;
		int maxHeight = Frame.mc.displayHeight / 4;
		this.absX = parX + this.x;
		this.absY = parY + this.y;
		int rHeight = this.height;
		if (this.isOpen) {
			for (Component item : this.items) {
				rHeight += item.renderHeight + 2;
			}
			Render.drawBorderedRect(this.x, this.y, this.x + this.width, this.y + Math.min(rHeight += 2, maxHeight),
					-13421773, new Color(0xFFFFFFFF).getRGB());
		}
		// Render.drawBorderedRect(this.x, this.y, this.x + this.width,
		// this.y + Math.min(rHeight += 2, maxHeight), -16777216, -16777216);
		Render.drawBorderedRect(this.x, this.y, this.x + this.width, this.y + Math.min(this.height, maxHeight),
				-13421773, new Color(0xFFFFFFFF).getRGB());
		Frame.mc.fontRendererObj.func_175063_a(Click.capitalize(this.title.toLowerCase()),
				this.x + this.width / 2 - Frame.mc.fontRendererObj.getStringWidth(this.title) / 2,
				this.y + this.height / 2 - Frame.mc.fontRendererObj.FONT_HEIGHT / 2, new Color(0xFFFFFFFF).getRGB());
		if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + rHeight
				&& Mouse.hasWheel()) {
			int dw = Mouse.getDWheel();
			int amt = 20;
			if (dw <= -120) {
				this.targetScrollAmount -= amt;
			} else if (dw >= 120) {
				this.targetScrollAmount += amt;
			}
		}
		this.targetScrollAmount = Math.max(maxHeight - rHeight, this.targetScrollAmount);
		this.targetScrollAmount = Math.min(0.0f, this.targetScrollAmount);
		if (rHeight < maxHeight) {
			this.targetScrollAmount = 0.0f;
		}
		float targetDist = Math.abs(this.targetScrollAmount - this.scrollAmount);
		float targetChangeAmount = targetDist / 10.0f * speedRatio;
		if (this.scrollAmount < this.targetScrollAmount) {
			this.scrollAmount += targetChangeAmount;
			if (this.scrollAmount > this.targetScrollAmount) {
				this.scrollAmount = this.targetScrollAmount;
			}
		} else if (this.scrollAmount > this.targetScrollAmount) {
			this.scrollAmount -= targetChangeAmount;
			if (this.scrollAmount < this.targetScrollAmount) {
				this.scrollAmount = this.targetScrollAmount;
			}
		}
		if (this.isOpen) {
			GL11.glEnable(3089);
			scissor2(this.x, this.y + this.height + 1, 100000, this.y + maxHeight - 1);
			for (Component item : this.items) {
				item.draw(mouseX, mouseY, partialTicks, this.absX, this.absY);
				item.isVisible = true;
			}
			GL11.glDisable(3089);
		} else {
			for (Component item : this.items) {
				item.isVisible = false;
			}
		}
		this.resizeComponenets();
	}

	public static void scissor2(int x, int y, int xend, int yend) {
		int width = xend - x;
		int height = yend - y;
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);
		int factor = sr.getScaleFactor();
		int bottomY = Minecraft.getMinecraft().currentScreen.height - yend;
		GL11.glScissor(x * factor, bottomY * factor, width * factor, height * factor);
	}

	private void resizeComponenets() {
		int compY = this.height;
		for (Component comp : this.items) {
			comp.x = 2;
			comp.y = compY + 2 + (int) this.scrollAmount;
			comp.width = this.width - 2;
			compY += comp.renderHeight + 2;
		}
	}

	public void addItem(Component item) {
		item.parent = this;
		this.items.add(item);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height) {
			if (mouseButton == 0) {
				this.isDrag = true;
				this.offsetX = mouseX - this.x;
				this.offsetY = mouseY - this.y;
			} else {
				this.isOpen = !this.isOpen;
			}
		}
		for (Component child : this.items) {
			child.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void mouseReleased(int x, int y, int mouseButton) {
		this.isDrag = false;
		for (Component child : this.items) {
			child.mouseReleased(x, y, mouseButton);
		}
	}

	@Override
	public void keyPressed(int key) {
		for (Component comp : this.items) {
			comp.keyPressed(key);
		}
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setOpen(boolean open) {
		this.isOpen = open;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public boolean isOpen() {
		return this.isOpen;
	}

	public void drag(int mouseX, int mouseY) {
		if (this.isDrag) {
			this.x = mouseX - this.offsetX;
			this.y = mouseY - this.offsetY;
		}
	}
}
