package ru.wendoxd.modules.impl.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketPlayer;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.events.impl.player.EventEntitySync;
import ru.wendoxd.events.impl.player.EventLivingUpdate;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;

public class FreeCam extends Module {

	private Frame freecam_frame = new Frame("FreeCam");
	public static final CheckBox freecam = new CheckBox("FreeCam").markArrayList("FreeCam");
	private static final Slider speed = new Slider("Speed", 2, 0.1, 2, 0.2, () -> freecam.isEnabled(true));
	private double oldPosX, oldPosY, oldPosZ;
	private float oldFlySpeed;

	@Override
	protected void initSettings() {
		freecam.markSetting("FreeCam");
		speed.modifyPath("Speed_5");
		freecam_frame.register(freecam, speed);
		MenuAPI.player.register(freecam_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventLivingUpdate) {
			if (freecam.isEnabled(false)) {
				mc.player.noClip = true;
				mc.player.onGround = false;
				mc.player.capabilities.setFlySpeed(speed.getFloatValue() / 5);
				mc.player.capabilities.isFlying = true;
			}
		}
		if (event instanceof EventEntitySync && freecam.isEnabled(false)) {
			if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null
					&& (mc.getCurrentServerData().serverIP.contains("reallyworld")
							|| mc.getCurrentServerData().serverIP.contains("mstnw"))) {
				if (mc.player.ticksExisted % 10 == 0) {
					mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
				}
			}
			((EventEntitySync) event).setCanceled();

		}
		if (event instanceof EventRender2D) {
			if (freecam.isEnabled(false)) {
				ScaledResolution sr = ((EventRender2D) event).getScaledResolution();
				String posX = "" + (int) -(oldPosX - mc.player.posX);
				String posY = "" + (int) -(oldPosY - mc.player.posY);
				String posZ = "" + (int) -(oldPosZ - mc.player.posZ);
				String plusOrMinusX = !posX.contains("-") && !posX.equals("0") ? "+" : "";
				String plusOrMinusY = !posY.contains("-") && !posY.equals("0") ? "+" : "";
				String plusOrMinusZ = !posZ.contains("-") && !posZ.equals("0") ? "+" : "";
				String clipValue = "X " + plusOrMinusX + posX + " Y " + plusOrMinusY + posY + " Z " + plusOrMinusZ
						+ posZ;
				Fonts.SEMI_BOLD_16.drawStringWithShadow(clipValue, sr.getScaledWidth() / 2F - 24,
						sr.getScaledHeight() / 2F + 7, -1);
			}
		}
		if (event instanceof EventSwapState) {
			if (((EventSwapState) event).getCheckBox() == freecam) {
				if (!((EventSwapState) event).getState()) {
					mc.player.capabilities.isFlying = false;
					mc.player.capabilities.setFlySpeed(oldFlySpeed);
					mc.player.setPositionAndRotation(oldPosX, oldPosY, oldPosZ, mc.player.rotationYaw,
							mc.player.rotationPitch);
					mc.world.removeEntityFromWorld(1337);
					mc.player.motionZ = 0;
					mc.player.motionX = 0;
				}
				if (((EventSwapState) event).getState()) {
					oldPosX = mc.player.posX;
					oldPosY = mc.player.posY;
					oldPosZ = mc.player.posZ;
					oldFlySpeed = mc.player.capabilities.getFlySpeed();
					EntityOtherPlayerMP player = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
					player.copyLocationAndAnglesFrom(mc.player);
					player.rotationYawHead = mc.player.rotationYawHead;
					mc.world.addEntityToWorld(1337, player);
				}
			}
		}
	}
}
