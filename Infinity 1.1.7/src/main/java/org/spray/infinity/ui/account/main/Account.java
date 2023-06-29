package org.spray.infinity.ui.account.main;

public class Account {

	private String mask;
	private final String username;
	private String password;
	private boolean mojang, nickname;

	public Account(final String username, final String password) {
		this(username, password, "");
	}

	public Account(final String username, final String password, final String mask) {
		if (password.isEmpty() || password == null || password.equalsIgnoreCase("")) {
			this.username = username;
			this.password = null;
			this.mask = "";
			setNickname(true);
		} else {
			this.mask = "";
			this.username = username;
			this.password = password;
			this.mask = mask;
			setMojang(true);
		}
	}

	public String getMask() {
		return this.mask;
	}

	public String getPassword() {
		return this.password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setMask(final String mask) {
		this.mask = mask;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public boolean isMojang() {
		return mojang;
	}

	public void setMojang(boolean mojang) {
		this.mojang = mojang;
	}

	public boolean isNickname() {
		return nickname;
	}

	public void setNickname(boolean nickname) {
		this.nickname = nickname;
	}
}