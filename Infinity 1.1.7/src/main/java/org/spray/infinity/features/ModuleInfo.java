package org.spray.infinity.features;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface ModuleInfo {

	public String name();

	public int key();

	public boolean visible();

	public Category category();

	public String desc();

}
