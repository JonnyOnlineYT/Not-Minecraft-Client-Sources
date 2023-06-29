package splash.gui.pannels.components;

import java.awt.Color;
import java.text.DecimalFormat;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import splash.Splash;
import splash.api.value.impl.NumberValue;
import splash.gui.ClickGui;

public class Slider extends Component {

	private boolean dragging = false;
	private int x;
	private int y;
	private int height;
	private boolean hovered;
	
	private NumberValue set;
	
	public Slider(NumberValue s, Button b) {
		super(s, b);
		this.set = s;
	}
	
	@Override
	public int drawScreen(int mouseX, int mouseY, int x, int y) {
		this.hovered = this.isHovered(mouseX, mouseY);
		this.height = 15;
		this.x = x;
		this.y = y;
		
		
		float val = 0;
		double min = 0;
		double max = 0;
		if (this.set.getValue() instanceof Double) {
			min = (double) this.set.getMinimumValue();
			max = (double) this.set.getMaximumValue();
			double val1 = (double) this.set.getValue();
			val = (float) val1;
		} else if (this.set.getValue() instanceof Integer) {
			val = (int)this.set.getValue();
			min = (int) this.set.getMinimumValue();
			max = (int) this.set.getMaximumValue();
		} else if (this.set.getValue() instanceof Float) {
			val = (float)this.set.getValue();
			min = (float) this.set.getMinimumValue();
			max = (float) this.set.getMaximumValue();
		}
		if (this.dragging) {
			float toSet = (float)((float)mouseX - (float)this.x) / (float)this.parent.getWidth();
			if (toSet > 1) {
				toSet = 1;
			}
			if (toSet < 0) {
				toSet = 0;
			}
			double toSet2 = ((max - min) * toSet) + min;
			if (this.set.getValue() instanceof Double) {
				this.set.setValueObject((double)toSet2);
			} else if (this.set.getValue() instanceof Integer) {
				this.set.setValueObject((int)toSet2);
			} else if (this.set.getValue() instanceof Float) {
				this.set.setValueObject((float)toSet2);
			}
			
		}
		float distance = (float) ((val - min) / (max - min));
		Gui.drawRect(this.x, this.y, this.x + this.parent.getWidth(), this.y + this.height, ClickGui.getSecondaryColor(true).getRGB());
		String name = this.set.getValueName() + ": " + new DecimalFormat("#.##").format(this.set.getValue());
		Gui.drawRect(this.x, this.y + Splash.INSTANCE.getFontRenderer().getHeight() + 6, (int) (this.x + (this.parent.getWidth() * distance)), this.y + this.height - 3, ClickGui.getPrimaryColor().getRGB());
		Gui.drawRect((int) (this.x + (this.parent.getWidth() * distance)), this.y + Splash.INSTANCE.getFontRenderer().getHeight() + 6, (int) (this.x + (this.parent.getWidth() * distance)) + 5 > parent.getWidth() ? (int) (this.x + (this.parent.getWidth() * distance)) : (int) (this.x + (this.parent.getWidth() * distance)) + 5, this.y + this.height - 3, new Color(255,255,255).getRGB());
		GlStateManager.pushMatrix();
		float scale = 1;
		GlStateManager.scale(scale, scale, scale);
		Splash.INSTANCE.getFontRenderer().drawString(name, (this.x + 2) / scale, (y + 3) / scale, ClickGui.getPrimaryColor().getRGB());
		GlStateManager.popMatrix();
		return this.height;
	}
	
	@Override
	public void mouseClicked(int x, int y, int button) {
		if (button == 0 && this.hovered)  {
			this.dragging = true;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (state == 0) {
			this.dragging = false;
		}
	}
	
	private boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + this.parent.getWidth() && mouseY >= y && mouseY <= y + height;
	}
}
