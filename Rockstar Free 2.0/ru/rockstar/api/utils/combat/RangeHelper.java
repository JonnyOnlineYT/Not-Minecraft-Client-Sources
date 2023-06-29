package ru.rockstar.api.utils.combat;

import net.minecraft.client.Minecraft;
import ru.rockstar.Main;
import ru.rockstar.client.features.impl.combat.KillAura;

public class RangeHelper {
	static Minecraft mc = Minecraft.getMinecraft();
	public static float range;
	public static float auraRange;
	
	public static float calculateRange() {
		auraRange = KillAura.range.getNumberValue();
		if (KillAura.smartRange.getBoolValue()) {
			if (Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {
				if (KillAura.clickMode.getCurrentMode().equalsIgnoreCase("1.8")) {
					if (KillAura.hitCounter <= 2) {
						range = 3.2f;
					} else if (KillAura.hitCounter <= 5 && KillAura.hitCounter > 2) {
						range = 3.2f;
					} else if (KillAura.hitCounter <= 15 && KillAura.hitCounter > 5) {
						range = 3.3f;
					} else if (KillAura.hitCounter <= 20 && KillAura.hitCounter > 15) {
						range = 3.4f;
					} else if (KillAura.hitCounter > 20) {
						range = 3.5f;
					} else if (KillAura.hitCounter > 30) {
						range = 3.6f;
					} else {
						range = 3.2f;
					}
				} else {
					if (KillAura.hitCounter <= 2) {
						range = 4.5f;
					} else if (KillAura.hitCounter <= 5 && KillAura.hitCounter > 2) {
						range = 4.2f;
					} else if (KillAura.hitCounter <= 15 && KillAura.hitCounter > 5) {
						range = 3.8f;
					} else if (KillAura.hitCounter <= 20 && KillAura.hitCounter > 15) {
						range = 3.5f;
					} else if (KillAura.hitCounter > 20) {
						range = 3.4f;
					} else if (KillAura.hitCounter > 30) {
						range = 3.1f;
					} else {
						range = 3.1f;
					}
				}
				return range;
			} else {
				return 0;
			}
		} else {
			return auraRange;
		}
	}
	
	public static float getRange() {
		return calculateRange();
	}
}
