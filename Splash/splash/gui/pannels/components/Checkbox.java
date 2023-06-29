package splash.gui.pannels.components;

import java.awt.Color;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import splash.Splash;
import splash.api.value.impl.BooleanValue;
import splash.gui.ClickGui;
import splash.utilities.visual.RenderUtilities;

public class Checkbox extends Component {

	private boolean dragging = false;
	private int x;
	private int y;
	private int height;
	private boolean hovered;
	
	private BooleanValue set;
	
	public Checkbox(BooleanValue s, Button b) {
		super(s, b);
		this.set = s;
	}
	
	@Override
	public int drawScreen(int mouseX, int mouseY, int x, int y) {
		this.hovered = this.isHovered(mouseX, mouseY);
		this.height = 15;
		this.x = x;
		this.y = y;
		
		Gui.drawRect(this.x, this.y, this.x + this.parent.getWidth(), this.y + this.height, ClickGui.getSecondaryColor(true).getRGB());
		String name = this.set.getValueName();
		Splash.INSTANCE.getFontRenderer().drawString(name, (this.x + 16), (y + (Splash.INSTANCE.getFontRenderer().getHeight() / 2) + 0.5F), ClickGui.getPrimaryColor().getRGB());
		RenderUtilities.drawRoundedRect(this.x + 3, this.y + 5, this.x + 14, this.y + 14, new Color(0, 0, 0, 0).getRGB(), (boolean) this.set.getValue() ? ClickGui.getPrimaryColor().getRGB() : new Color(180, 180, 180).getRGB());
		GlStateManager.color(1, 1, 1);
		float x1 = (boolean) this.set.getValue() ? 5 : 1.5F;
		float x2 = (boolean) this.set.getValue() ? 3 : 0F;
		
		float x1Diff = x1 - this.lastX1;
		float x2Diff = x2 - this.lastX2;
		this.lastX1 += x1Diff / 4;
		this.lastX2 += x2Diff / 4;
		RenderUtilities.drawRoundedRect(this.x + lastX1 + 3, this.y + 5, this.x + 10 + lastX2, this.y + 13, new Color(0, 0, 0, 0).getRGB(), new Color(255, 255, 255).getRGB());
		
		return this.height;
	}
	
	private float lastX1 = 1.5F;
	private float lastX2 = -0.5F;
	
	private float red = 0.70588235294F;
	private float green = 0.70588235294F;
	private float blue = 0.70588235294F;
	
	@Override
	public void mouseClicked(int x, int y, int button) {
		if (button == 0 && this.hovered)  {
			this.set.setValueObject(!((boolean)this.set.getValue()));
		}
	}
	
	private boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + this.parent.getWidth() && mouseY > y && mouseY < y + height;
	}
}