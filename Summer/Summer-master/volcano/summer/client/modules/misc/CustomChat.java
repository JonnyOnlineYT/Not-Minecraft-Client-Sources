package volcano.summer.client.modules.misc;

import net.minecraft.client.gui.GuiNewChat;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.screen.changelog.GuiSummerChat;

public class CustomChat extends Module {

	public CustomChat() {
		super("CustomChat", 0, Category.MISC);
	}

	@Override
	public void onEnable() {
		GuiNewChat chat = new GuiSummerChat(this.mc);
		chat = copyLines(this.mc.ingameGUI.getChatGUI(), chat);
		this.mc.ingameGUI.setChatGUI(chat);
	}

	@Override
	public void onDisable() {
		GuiNewChat chat = new GuiNewChat(this.mc);
		chat = copyLines(this.mc.ingameGUI.getChatGUI(), chat);
		this.mc.ingameGUI.setChatGUI(chat);
	}

	private GuiNewChat copyLines(GuiNewChat oldChat, GuiNewChat newChat) {
		for (Object o : oldChat.getChatLines()) {
			newChat.getChatLines().add(o);
		}
		for (Object o : oldChat.getField_146253_i()) {
			newChat.getField_146253_i().add(o);
		}
		for (Object o : oldChat.getSentMessages()) {
			newChat.getSentMessages().add(o);
		}
		return newChat;
	}

	@Override
	public void onEvent(Event event) {
	}
}