package splash.api.value.impl;

import splash.Splash;
import splash.api.module.Module;
import splash.api.value.Value;

/**
 * Author: Ice
 * Created: 18:41, 12-Jun-20
 * Project: Client
 */
public class BooleanValue<T> extends Value<T> {
	//TODO: Module dependency! -> ()
    public BooleanValue(String valueName, T defaultValueObject, Module parent) {
        super(valueName, defaultValueObject, parent);
    }

}
