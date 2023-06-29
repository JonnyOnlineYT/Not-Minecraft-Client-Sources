package volcano.summer.client.modules.render;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.renderer.EntityRenderer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventTick;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class ClickGui extends Module {
	public static Value<Boolean> blur;
	public static ModeValue clickgui;
	volcano.summer.screen.clickgui.original.ClickGUI click;
	volcano.summer.screen.clickgui.one2.ClickGui click1;

	public ClickGui() {
		super("ClickGui", Keyboard.KEY_RSHIFT, Category.RENDER);
		blur = new Value<Boolean>("Blur", "blur", false, this);
		clickgui = new ModeValue("ClickMode", "Mode", "Old", new String[] { "Old", "New" }, this);
	}

	@Override
	public void onEnable() {
		if (this.click == null) {
			this.click = new volcano.summer.screen.clickgui.original.ClickGUI();
			this.click1 = new volcano.summer.screen.clickgui.one2.ClickGui();
		}

		if (!ClickGui.clickgui.getStringValue().equalsIgnoreCase("Old")) {
			mc.displayGuiScreen(this.click);
		}
		if (!ClickGui.clickgui.getStringValue().equalsIgnoreCase("New")) {
			mc.displayGuiScreen(this.click1);
		}
		setState(false);

	}

	@Override
	public void onDisable() {
		mc.displayGuiScreen(null);
		mc.entityRenderer.theShaderGroup = null;
	}

	@Override
	public void onEvent(Event e) {
		if (((e instanceof EventTick)) && (mc.currentScreen != this.click) && (mc.currentScreen != this.click1)) {
			toggleMod();
		}
	}
}
