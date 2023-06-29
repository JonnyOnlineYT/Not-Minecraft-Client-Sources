package org.spray.heaven.features.module.modules.movement;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketPlayer;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.ClipEvent;
import org.spray.heaven.events.PushOutBlockEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

import com.darkmagician6.eventapi.EventTarget;

import static org.spray.heaven.util.MovementUtil.isMoving;

@ModuleInfo(name = "NoClip", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class NoClip extends Module {

	public final Setting mode = register(new Setting("Mode", "Advanced", Arrays.asList("Default", "Advanced", "BoatPhase","MatrixPacket")));
	private final Setting Boost = register(new Setting("Pulse", 0.4, 0, 1.0));

	private boolean falled = false;
	float TMSpeed = (float) Boost.getValue();
	@Override
	public void onDisable() {
		falled = false;
		mc.getTimer().reset();
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());

		if (mode.getCurrentMode().equalsIgnoreCase("Default")) {
			mc.player.motionY = 0.00001;

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.player.motionY = 0.4;
			}
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.motionY = -0.4;
			}
		}
		if (mode.getCurrentMode().equalsIgnoreCase("MatrixPacket")) {
			mc.player.motionY = 0.0D;
			mc.player.motionX = 0.0D;
			mc.player.motionZ = 0.0D;
			mc.player.onGround = false;
			mc.player.jumpMovementFactor = 0.0F;
			double in;
			if (mc.gameSettings.keyBindJump.pressed) {
				in = mc.player.posY + 20.0D;
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, in, mc.player.posZ, true));
			}

			if (mc.gameSettings.keyBindForward.pressed) {
				NetHandlerPlayClient dram = mc.player.connection;
				double clam = mc.player.posX;
				mc.getMinecraft();
				clam -= Math.sin(Math.toRadians((double) mc.player.rotationYaw)) * 15.0D;
				in = mc.player.posY;
				double var16 = mc.player.posZ;
				mc.getMinecraft();
				dram.sendPacket(new CPacketPlayer.Position(clam, in, var16 + Math.cos(Math.toRadians((double) mc.player.rotationYaw)) * 15.0D, true));
				in = mc.player.posY - 15.0D;
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, in, mc.player.posZ, true));
				if (mc.player.ticksExisted % 9 == 0) {
					in = mc.player.posY + 1.0D;
					mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, in, mc.player.posZ, true));
				}
			}

			if (mc.gameSettings.keyBindSneak.pressed) {
				in = mc.player.posY - 2.0D;
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, in, mc.player.posZ, true));
			}
			if (!mc.gameSettings.keyBindForward.isKeyDown() && !mc.gameSettings.keyBindBack.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
				if (!(isMoving())) {
					mc.getTimer().setSpeed(1.0F);
				}
			}
			mc.getTimer().setSpeed((float) (TMSpeed + 1.0F));
		}
		if (mode.getCurrentMode().equalsIgnoreCase("BoatPhase")) {
			mc.getTimer().setSpeed(4.4F);
			mc.player.motionX -= 5.0E-7;
			mc.player.motionZ += 5.0E-7;
			mc.player.motionY = -0.1;



		}

		if (mode.getCurrentMode().equalsIgnoreCase("Advanced")) {
//            if (mc.gameSettings.keyBindJump.isKeyDown()) {
//                mc.player.motionY = 0.4;
//            }
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
//				mc.player.motionY = -0.4;
				mc.player.onGround = false;
				mc.player.fallDistance = 0.4f;
				falled = true;
			} else if (falled) {
				mc.player.onGround = true;
				falled = false;
			}
		}

	}


	@EventTarget
	public void onPushBlock(PushOutBlockEvent event) {
		event.cancel();
	}

	@EventTarget
	public void onClip(ClipEvent event) {
		if (mode.getCurrentMode().equalsIgnoreCase("Default"))
			event.cancel();
		else if (mode.getCurrentMode().equalsIgnoreCase("Advanced")) {
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				event.cancel();
			}
		}
	}

	public boolean isAdvanced() {
		return isEnabled() && mode.getCurrentMode().equalsIgnoreCase("Advanced");
	}

}
