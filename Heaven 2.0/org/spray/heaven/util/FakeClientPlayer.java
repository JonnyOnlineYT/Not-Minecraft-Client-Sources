package org.spray.heaven.util;

import org.spray.heaven.main.Wrapper;

import net.minecraft.client.entity.EntityOtherPlayerMP;

public class FakeClientPlayer {

	public static FakeClientPlayer INSTANCE = new FakeClientPlayer();

	EntityOtherPlayerMP visualPlayer;

	public void init() {
		visualPlayer = new EntityOtherPlayerMP(Wrapper.getWorld(), Wrapper.MC.getSession().getProfile());
	}

	public EntityOtherPlayerMP getPlayer() {
		return Wrapper.getWorld() != null ? visualPlayer : null;
	}

}
