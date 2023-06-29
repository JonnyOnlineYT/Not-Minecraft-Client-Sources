package org.spray.keyauth;

import org.spray.heaven.main.Heaven;
import org.spray.keyauth.api.KeyAuth;

import by.radioegor146.nativeobfuscator.Native;

@Native
public class KeyAuthProvider {

	private final String url = "https://keyauth.win/api/1.1/";

	private String secret;
	private String ownerid;
	private String appname;

	private KeyAuth api;

	public boolean relogin;
	public boolean relogincheck;

	public void preInit() {
		secret = "3fe9ee01b547c59ee94e639e80c410350bc3f5e707d88b0c84c50dcacd309bd3";
		ownerid = "VcKDWZd9tf";
		appname = "Heaven";
		relogin = true;
		relogincheck = true;
	}

	public void posInit() {
		api = new KeyAuth(appname, secret, ownerid, Heaven.VERSION, url);
		api.init();
	}

	public KeyAuth getApi() {
		return api;
	}

}
