package volcano.summer.client.modules.render;

import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.value.ModeValue;

public class Animations extends Module {
	public static ModeValue animation;

	//ItemRenderer
	
	public Animations() {
		super("Animations", 0, Category.RENDER);
		animation = new ModeValue("Mode", "Mode", "Summer", new String[] { "Summer", "1.7", "Push", "Geo", "Winter" },
				this);
		
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {

	}

}
