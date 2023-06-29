package splash.gui.pannels.components;

import splash.api.value.Value;

public class SetComp {

	protected Button parent;
	protected Value set;
	
	public SetComp(Value s, Button parent) {
		this.set = s;
		this.parent = parent;
	}

	public int drawScreen(int mouseX, int mouseY, int x, int y) {
		return 0;
	}
	
	public void mouseClicked(int x, int y, int button) {}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {}
	
	public Value getSetting() {
		return this.set;
	}
	
	public void keyTyped(char typedChar, int keyCode) {
		
	}
}