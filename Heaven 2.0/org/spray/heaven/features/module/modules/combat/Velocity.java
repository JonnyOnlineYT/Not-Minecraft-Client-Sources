package org.spray.heaven.features.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

import java.util.ArrayList;
import java.util.Arrays;

@ModuleInfo(name = "Velocity", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class Velocity extends Module {

	private Setting mode = register(
			new Setting("Mode", "Vanilla", new ArrayList<>(Arrays.asList("Vanilla", "Matrix 6.4"))));

	private Setting horizontal = register(new Setting("Horizontal", 0, 0, 1))
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Vanilla"));
	private Setting vertical = register(new Setting("Vertical", 0, 0, 1))
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Vanilla"));

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		setSuffix(mode.getCurrentMode());

		if (mode.getCurrentMode().equalsIgnoreCase("Matrix 6.4")) {
			if (mc.player.hurtTime > 8)
				event.setOnGround(true);;
		}
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType().equals(EventType.RECIEVE)) {
			if (event.getPacket() instanceof SPacketEntityVelocity) {
				SPacketEntityVelocity sv = (SPacketEntityVelocity) event.getPacket();
				if (mc.player.getEntityId() == sv.getEntityID()) {
					int[] motionDiff = getMotionDiff(sv.getMotionX(), sv.getMotionY(), sv.getMotionZ());

					sv.setMotionX(motionDiff[0]);
					sv.setMotionY(motionDiff[1]);
					sv.setMotionZ(motionDiff[2]);
				}
			}
		}
	}

	private int[] getMotionDiff(int motionX, int motionY, int motionZ) {
		double velX = (motionX / 8000d - mc.player.motionX) * horizontal.getValue();
		double velY = (motionY / 8000d - mc.player.motionY) * vertical.getValue();
		double velZ = (motionZ / 8000d - mc.player.motionZ) * horizontal.getValue();

		int x = (int) (velX * 8000 + mc.player.motionX * 8000);
		int y = (int) (velY * 8000 + mc.player.motionY * 8000);
		int z = (int) (velZ * 8000 + mc.player.motionZ * 8000);

		return new int[] { x, y, z };
	}

}
