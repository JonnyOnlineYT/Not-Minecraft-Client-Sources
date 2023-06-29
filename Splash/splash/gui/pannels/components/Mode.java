package splash.gui.pannels.components;

import net.minecraft.client.gui.Gui;
import splash.Splash;
import splash.api.value.impl.ModeValue;
import splash.gui.ClickGui;

public class Mode extends SetComp {

	private boolean dragging = false;
	private int x;
	private int y;
	private int height;
	private boolean hovered;
	
	private ModeValue set;
	private int lastIndex = 0;
	
	public Mode(ModeValue s, Button b) {
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
		String name = this.set.getValueName() + ": " + this.set.getValue();
		Splash.INSTANCE.getFontRenderer().drawString(name, (this.x + 2), (y + (Splash.INSTANCE.getFontRenderer().getHeight() / 2) - 1), ClickGui.getPrimaryColor().getRGB());
		return this.height;
	}
	
	@Override
	public void mouseClicked(int x, int y, int button) {
		if ((button == 0 || button == 1) && this.hovered)  {
			if (button == 0) {
				lastIndex++;
			} else if (button == 1) {
				lastIndex--;
			}
			if (lastIndex >= this.set.getModes().length) {
				lastIndex = 0;
			} else if (lastIndex < 0) {
				lastIndex = this.set.getModes().length - 1;
			}
			this.set.setValueObject(this.set.getModes()[lastIndex]);
		}
	}
	
	private boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + this.parent.getWidth() && mouseY >= y && mouseY <= y + height;
	}
}