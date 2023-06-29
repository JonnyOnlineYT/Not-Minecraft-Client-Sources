package volcano.summer.client.events;

import net.minecraft.client.Minecraft;
import volcano.summer.base.manager.event.Event;

public class RenderStringEvent extends Event {
	private final Minecraft mc = Minecraft.getMinecraft();
	private String string;
	private final State state;

	public RenderStringEvent(String string, State state) {
		this.string = string;
		this.state = state;
	}

	public String getString() {
		return this.string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public State getState() {
		return this.state;
	}

	public static enum State {
		TAB, SCOREBOARD, CHAT, NAMETAG;

		private State() {
		}
	}
}
