package volcano.summer.screen.clickgui.sektor;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class Button extends Component {
	protected String title;
	protected boolean enabled = false;
	private String description = null;
	private final String buttonID;
	protected boolean held = false;
	private boolean isExtended = false;
	public boolean hasValue = false;
	private long hoverTime = 0;
	private long lastTime = 0;
	private ArrayList<Component> extendedComponents = new ArrayList();

	public Button(String title, String description, String buttonID, Component parent) {
		this(title, buttonID, parent);
		this.description = description;
	}

	public Button(String title, String buttonID, Component parent) {
		this.title = title;
		this.buttonID = buttonID;
		this.parent = parent;
		this.width = parent.width - 2;
		this.height = 14;
		this.renderWidth = this.width;
		this.renderHeight = this.height;
		this.isVisible = false;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks, int parX, int parY) {
		this.onUpdate();
		GL11.glPushMatrix();
		GL11.glTranslatef(this.parent.x, this.parent.y, 0.0f);
		this.absX = parX + this.x;
		this.absY = parY + this.y;
		Render.rectangleBorderedGradient(this.x, this.y, this.width, this.y + this.height, this.enabled ? -1 : -7829368,
				-1, (int) 0.5);
		int textColor = -657931;
		if (mouseX >= this.absX && mouseX <= this.absX + this.width && mouseY >= this.absY
				&& mouseY <= this.absY + this.height) {
			Render.drawRect(this.x, this.y, this.width, this.y + this.height, 0xff232323);
		}
		if (this.hasExtendedComponenets()) {
			if (this.isExtended) {
				this.renderHeight = this.height;
				for (Component c : this.extendedComponents) {
					c.draw(mouseX, mouseY, partialTicks, this.absX, this.absY);
					c.isVisible = true;
					this.renderHeight += c.renderHeight;
				}
			} else {
				this.renderHeight = this.height;
				for (Component c : this.extendedComponents) {
					c.isVisible = false;
				}
			}
		} else {
			this.renderHeight = this.height;
		}
		if (!this.hasValue || this.title.split(":").length != 2) {
			Button.mc.fontRendererObj.func_175063_a(this.title, this.x + 3,
					this.y + this.height / 2 - Button.mc.fontRendererObj.FONT_HEIGHT / 2, textColor);
		} else {
			String string1 = this.title.split(":")[0];
			String string2 = this.title.split(":")[1].replaceAll("_", " ");
			Button.mc.fontRendererObj.func_175063_a(string1, this.x + 3,
					this.y + this.height / 2 - Button.mc.fontRendererObj.FONT_HEIGHT / 2, textColor);
			Button.mc.fontRendererObj.func_175063_a(string2,
					this.x + this.width - 3 - Button.mc.fontRendererObj.getStringWidth(string2),
					this.y + this.height / 2 - Button.mc.fontRendererObj.FONT_HEIGHT / 2, textColor);
		}
		if (this.description != null) {
			if (mouseX >= this.absX && mouseX <= this.absX + this.width) {
				if (mouseY >= this.absY && mouseY <= this.absY + this.height) {
					this.hoverTime += System.currentTimeMillis() - this.lastTime;
					if (this.hoverTime > 500) {
						GL11.glPushMatrix();
						Render.rectangleBorderedGradient(this.x + mouseX - this.absX + 1,
								this.y + mouseY - this.absY - Button.mc.fontRendererObj.FONT_HEIGHT - 1,
								this.x + mouseX - this.absX + Button.mc.fontRendererObj.getStringWidth(this.description)
										+ 7,
								this.y + mouseY - this.absY + 2, new int[] { -14606047, -15066598 },
								new int[] { -15921907, -15921907 }, 0.5);
						GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
						Button.mc.fontRendererObj.func_175063_a(this.description, this.x + mouseX + 4 - this.absX,
								this.y + mouseY + 1 - this.absY - Button.mc.fontRendererObj.FONT_HEIGHT, textColor);
						GL11.glPopMatrix();
					}
				} else {
					this.hoverTime = 0;
				}
			} else {
				this.hoverTime = 0;
			}
		}
		this.resizeChildren();
		GL11.glPopMatrix();
		this.lastTime = System.currentTimeMillis();
	}

	private void resizeChildren() {
		int compY = this.height;
		boolean compX = false;
		int spacer = 2;
		for (Component c : this.extendedComponents) {
			c.y = compY + spacer;
			c.x = 2;
			c.width = this.width - 4;
			compY += c.renderHeight + spacer;
		}
		if (this.isExtended) {
			this.renderHeight = compY + spacer - 2;
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (!this.isVisible) {
			return;
		}
		if (this.hasExtendedComponenets() && this.isExtended) {
			for (Component c : this.extendedComponents) {
				c.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		if (mouseX >= this.absX && mouseX <= this.absX + this.width && mouseY >= this.absY
				&& mouseY <= this.absY + this.height) {
			if (mouseButton == 0) {
				this.held = true;
			} else if (this.hasExtendedComponenets()) {
				this.isExtended = !this.isExtended;
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (!this.isVisible) {
			return;
		}
		if (this.hasExtendedComponenets() && this.isExtended) {
			for (Component c : this.extendedComponents) {
				c.mouseReleased(mouseX, mouseY, mouseButton);
			}
		}
		if (mouseX >= this.absX && mouseX <= this.absX + this.width && mouseY >= this.absY
				&& mouseY <= this.absY + this.height && this.held) {
			this.held = false;
			this.onPressed();
		}
	}

	@Override
	public void keyPressed(int key) {
		for (Component comp : this.extendedComponents) {
			comp.keyPressed(key);
		}
	}

	public final void addExtendedComponent(Component c) {
		this.extendedComponents.add(c);
	}

	public final boolean hasExtendedComponenets() {
		if (this.extendedComponents.size() > 0) {
			return true;
		}
		return false;
	}

	public final String getTitle() {
		return this.title;
	}

	public final boolean isExtended() {
		return this.isExtended;
	}

	public final String getButtonID() {
		return this.buttonID;
	}

	public void onPressed() {
	}

	public void onUpdate() {
	}
}