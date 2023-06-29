package volcano.summer.base.manager.friend;

public class Friend {
	public String name;
	public String alias;

	public Friend(final String name, final String alias) {
		this.name = name;
		this.alias = alias;
	}

	public String getName() {
		return this.name;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setName(final String s) {
		this.name = s;
	}

	public void setAlias(final String s) {
		this.alias = s;
	}
}
