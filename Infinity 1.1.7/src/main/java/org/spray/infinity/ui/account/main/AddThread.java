package org.spray.infinity.ui.account.main;

import java.net.Proxy;

import org.spray.infinity.main.Infinity;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class AddThread extends Thread {
	private final String password;
	private final String username;

	public AddThread(final String username, final String password) {
		this.username = username;
		this.password = password;
	}

	private final void checkAndAddAlt(final String username, final String password) {
		final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
		final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
				.createUserAuthentication(Agent.MINECRAFT);
		auth.setUsername(username);
		auth.setPassword(password);
		try {
			auth.logIn();
			AccountManager.registry.add(new Account(username, password, auth.getSelectedProfile().getName()));
			Infinity.getAccountManager().save();
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}

	public Account fromName(String name) {
		for (Account config : Infinity.getAccountManager().getRegistry()) {
			if (config.getUsername().equals(name))
				return config;
		}
		return null;
	}

	@Override
	public void run() {
		if (this.password.equals("")) {
			AccountManager.add(new Account(this.username, ""));
			Infinity.getAccountManager().save();
			return;
		}
		this.checkAndAddAlt(this.username, this.password);
	}
}