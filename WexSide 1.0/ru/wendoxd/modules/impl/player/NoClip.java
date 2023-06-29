package ru.wendoxd.modules.impl.player;

import net.minecraft.util.math.Vec3d;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;
import ru.wendoxd.events.impl.block.EventFullCube;
import ru.wendoxd.events.impl.block.EventPushBlock;
import ru.wendoxd.events.impl.player.EventMove;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class NoClip extends Module {
	public static Frame noclip_frame = new Frame("NoClip");
	public static CheckBox noclip = new CheckBox("NoClip").markArrayList("NoClip");
	private final SelectBox mode = new SelectBox("Type", new String[] { "Default", "Sunrise" },
			() -> noclip.isEnabled(true));
	public long violationTime, steps;
	private boolean needElytra;
	public static long nextMoveTime;

	@Override
	protected void initSettings() {
		noclip.markSetting("NoClip");
		noclip_frame.register(noclip, mode);
		MenuAPI.player.register(noclip_frame);
	}

	@Override
	public void onEvent(Event event) {
		if ((event instanceof EventFullCube) && noclip.isEnabled(false)) {
			((IEventCancelable) event).setCanceled();
		}
		if (event instanceof EventPushBlock) {
			((EventPushBlock) event).setCanceled();
		}
		if (event instanceof EventMove && noclip.isEnabled(false)) {
			EventMove move = (EventMove) event;
			if (!collisionPredict(move.to())) {
				if (move.isCollidedHorizontal())
					move.setIgnoreHorizontalCollision();
				if (move.motion().yCoord > 0 || mc.player.isSneaking()) {
					move.setIgnoreVerticalCollision();
				}
				move.motion().yCoord = Math.min(move.motion().yCoord, mode.get() == 0 ? 0.39 : 99999);
			}
		}
	}

	public boolean collisionPredict(Vec3d to) {
		boolean prevCollision = mc.world
				.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().contract(0.0625D)).isEmpty();
		Vec3d backUp = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);
		mc.player.setPosition(to.xCoord, to.yCoord, to.zCoord);
		boolean collision = mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().contract(0.0625D))
				.isEmpty() && prevCollision;
		mc.player.setPosition(backUp.xCoord, backUp.yCoord, backUp.zCoord);
		return collision;
	}
}