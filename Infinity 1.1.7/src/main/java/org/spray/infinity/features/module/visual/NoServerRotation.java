package org.spray.infinity.features.module.visual;

import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.utils.PacketUtil;

import com.darkmagician6.eventapi.EventTarget;

@ModuleInfo(category = Category.VISUAL, desc = "Removes packet rotations leaving only your movements", key = -2, name = "NoServerRotation", visible = true)
public class NoServerRotation extends Module {

	@EventTarget
	public void onPacket(PacketEvent event) {
		PacketUtil.cancelServerRotation(event);
	}

}
