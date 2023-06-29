package splash.api.value.impl;

import org.apache.commons.lang3.ArrayUtils;
import splash.api.module.Module;
import splash.api.value.Value;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Ice
 * Created: 18:42, 12-Jun-20
 * Project: Client
 */
public class ModeValue<T extends Enum<T>> extends Value<T> {

    private T[] modes;

    public ModeValue(String valueName, T defaultValueObject, Module parent) {
        super(valueName, defaultValueObject, parent);
        modes = readModes(getValue());
    }

    public String getFixedValue() {
        return getValue().toString();
    }

    public T[] getModes() {
        return modes;
    }

    public T[] readModes(T value) {
        return value.getDeclaringClass().getEnumConstants();
    }

    public void increment() {
        T currentValue = getValue();

        for (T constant : getModes()) {
            if (constant != currentValue) {
                continue;
            }

            T newValue;

            int ordinal = constant.ordinal();
            if (ordinal == getModes().length - 1) {
                newValue = getModes()[0];
            } else {
                newValue = getModes()[ordinal + 1];
            }

            setValueObject(newValue);
            return;
        }
    }

    public void decrement() {
        T currentValue = getValue();

        for (T constant : getModes()) {
            if (constant != currentValue) {
                continue;
            }

            T newValue;

            int ordinal = constant.ordinal();
            if (ordinal == 0) {
                newValue = getModes()[getModes().length - 1];
            } else {
                newValue = getModes()[ordinal - 1];
            }

            setValueObject(newValue);
            return;
        }
    }
}

