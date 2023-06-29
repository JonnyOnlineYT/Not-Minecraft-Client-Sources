package org.spray.infinity.features.module.player;

import java.util.Optional;

import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.system.calculator.TermSolver;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

@ModuleInfo(category = Category.PLAYER, desc = "Automatically solves examples", key = -2, name = "ChatCalculator", visible = true)
public class ChatCalculator extends Module {

	private Setting globalChat = new Setting(this, "Global Chat", true);
	private Setting delay = new Setting(this, "Delay", 1D, 0D, 5D);

	private String[] contexts = new String[] { "Решите пример: " };

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType().equals(EventType.RECIEVE)) {
			if (event.getPacket() instanceof GameMessageS2CPacket) {
				GameMessageS2CPacket cms = (GameMessageS2CPacket) event.getPacket();

				String message = "";
				for (String context : contexts)
					message = cms.getMessage().getString().replace(context, "").replaceAll(" ", "");

				Optional<String> postfix = TermSolver.transformInfixToPostfix(message);

				if (postfix.isPresent()) {

					String result = String.valueOf((int) TermSolver.solvePostfix(postfix.get()));
					if (result == null)
						return;

					(new Thread() {
						@Override
						public void run() {
							try {
								Thread.sleep((long) (delay.getCurrentValueDouble() * 1000));

								Helper.getPlayer().sendChatMessage(globalChat.isToggle() ? "!" + result : result);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			}
		}
	}
}
