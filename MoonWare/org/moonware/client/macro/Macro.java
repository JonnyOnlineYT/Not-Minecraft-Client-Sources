package org.moonware.client.macro;

public class Macro {

    public String value;
    public int key;

    public Macro(int key, String macroValue) {
        this.key = key;
        value = macroValue;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
