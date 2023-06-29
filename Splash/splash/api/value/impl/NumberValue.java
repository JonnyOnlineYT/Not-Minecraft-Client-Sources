package splash.api.value.impl;

import splash.api.module.Module;
import splash.api.value.Value;

/**
 * Author: Ice
 * Created: 18:33, 12-Jun-20
 * Project: Client
 */
public class NumberValue<T> extends Value<T> {

    private T minimumValue, maximumValue;

    public NumberValue(String valueName, T defaultValueObject, T minimumValue, T maximumValue, Module parent) {
        super(valueName, defaultValueObject, parent);
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    public T getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(T minimumValue) {
        this.minimumValue = minimumValue;
    }

    public T getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(T maximumValue) {
        this.maximumValue = maximumValue;
    }

}
