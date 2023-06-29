package Celestial.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    public String name();
    public ModuleCategory type();
}
