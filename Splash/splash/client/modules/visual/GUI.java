package splash.client.modules.visual;

import org.lwjgl.input.Keyboard;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.ModeValue;
import splash.client.events.key.EventKey;
import splash.client.events.render.EventRender2D;
import splash.client.modules.visual.UI.ArrayColor;
import splash.gui.ClickGui;
public class GUI extends Module {
	
	public GUI() {
		super("ClickGui", "Graphical User Interface for setting module states and settings", ModuleCategory.VISUALS);
		this.setModuleMacro(Keyboard.KEY_RSHIFT);
	}

	public ModeValue<Mode> mode = new ModeValue<>("Mode", Mode.HUZINI, this);
	
	public enum Mode {
		PANEL, HUZINI;
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}

	
	@Collect
	public void onRender2D(EventRender2D e) {
        if (!(mc.currentScreen instanceof ClickGui)) return;
			activateModule();
	}
}
