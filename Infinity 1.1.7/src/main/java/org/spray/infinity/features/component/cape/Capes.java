package org.spray.infinity.features.component.cape;

import java.util.ArrayList;
import java.util.List;

public class Capes {

	public String CURRENT_NAME = "";

	private List<Cape> capes = new ArrayList<>();

	public void setCurrent(String name) {
		CURRENT_NAME = name;
	}

	public void initCapes() {
		capes.clear();
	}

	public List<Cape> getCapes() {
		return capes;
	}

	public class Cape {
		private String name;

		public Cape(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
