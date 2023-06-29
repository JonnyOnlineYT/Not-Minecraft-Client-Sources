package ru.rockstar.api.utils.combat;

import ru.rockstar.api.utils.Helper;

public class ChanceHelper implements Helper {
	boolean hitChance;
	int hitProcent;
	
	public void setChance(boolean chance) {
		hitChance = chance;
	}
	
	public boolean getChance() {
		return hitChance;
	}
	
	public int getProcent() {
		hitProcent = 50;
		
		if (mc.player.isOnLadder() || mc.player.isInLiquid() || mc.player.isInWeb) {
			hitProcent += 10;
		}
		
		if (!(mc.player.isOnLadder() || mc.player.isInLiquid() || mc.player.isInWeb)) {
			hitProcent -= 10;
		}
		
		
		
		return hitProcent;
	}
}
