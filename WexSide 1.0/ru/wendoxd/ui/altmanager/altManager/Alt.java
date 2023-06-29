package ru.wendoxd.ui.altmanager.altManager;

public class Alt {

	private String email, password, name;
	private boolean isMogang, unchecked;

	public Alt(String email, String password, String name) {
		this.email = email;

		if (password == null || password.isEmpty() || password.contains("empty")) {
			this.isMogang = false;
			this.unchecked = false;
			this.name = email;
			this.password = null;

		} else {
			this.isMogang = true;
			this.unchecked = name == null || name.isEmpty();
			this.name = name;
			this.password = password;
		}
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getNameOrEmail() {
		return unchecked ? email : name;
	}

	public String getPassword() {
		if (password == null || password.isEmpty() || password.contains("empty")) {
			isMogang = false;
			return "";
		} else {
			return password;
		}
	}

	public boolean isMojangAccount() {
		return isMogang;
	}

	public void setChecked(String name) {
		this.name = name;
		unchecked = false;
	}
}