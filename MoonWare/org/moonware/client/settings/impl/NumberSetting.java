package org.moonware.client.settings.impl;

import org.moonware.client.settings.Setting;

import java.util.function.Supplier;

public class NumberSetting extends Setting {

    private final NumberType type;
    private float current, minimum, maximum, increment;
    private String desc;

    public NumberSetting(String name, float current, float minimum, float maximum, float increment, Supplier<Boolean> visible) {
        this.name = name;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        type = NumberType.DEFAULT;
        setVisible(visible);
    }
    public NumberSetting(String name, float current, float minimum, float maximum, float increment) {
        this.name = name;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;

        if (name.contains("Cooldown"))  {
            type = NumberType.MS;
        }else if (name.contains("Size")) {

            type = NumberType.DEFAULT;
        }else{
            type = NumberType.DEFAULT;
        }
        setVisible(() -> true);
    }
    public NumberSetting(String name, double current, double minimum, double maximum, double increment) {
        this.name = name;
        this.minimum = (float) minimum;
        this.current = (float) current;
        this.maximum = (float) maximum;
        this.increment = (float) increment;
        type = NumberType.DEFAULT;
        setVisible(() -> true);
    }
    public NumberSetting(String name, float current, float minimum, float maximum) {
        this.name = name;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        increment = 0.01F;
        type = NumberType.DEFAULT;
        setVisible(() -> true);
    }
    public NumberSetting(String name, float current, float minimum, float maximum,Supplier<Boolean> visible) {
        this.name = name;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        increment = 0.01F;
        type = NumberType.DEFAULT;
        setVisible(visible);
    }
    public NumberSetting(String name, float current, float minimum, float maximum, float increment, Supplier<Boolean> visible, NumberType type) {
        this.name = name;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        this.type = type;
        setVisible(visible);
    }

    public NumberSetting(String name, String desc, float current, float minimum, float maximum, float increment, Supplier<Boolean> visible) {
        this.name = name;
        this.desc = desc;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        type = NumberType.DEFAULT;
        setVisible(visible);
    }

    public NumberSetting(String name, String desc, float current, float minimum, float maximum, float increment, Supplier<Boolean> visible, NumberType type) {
        this.name = name;
        this.desc = desc;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
        this.type = type;
        setVisible(visible);
    }

    public float getMinimum() {
        return minimum;
    }
    public float getMaximum() {
        return maximum;
    }
    public String getDesc() {
        return desc;
    }
    public double get() {
        return current;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getMinValue() {
        return minimum;
    }

    public void setMinValue(float minimum) {
        this.minimum = minimum;
    }

    public float getMaxValue() {
        return maximum;
    }

    public void setMaxValue(float maximum) {
        this.maximum = maximum;
    }

    public float getNumberValue() {
        return current;
    }
    public float getCurrentValue() {
        return current;
    }
    public float getValue() {
        return current;
    }
    public int getCurrentIntValue() {
        return (int) current;
    }
    public void setCurrentValue(float value) {
        current = value;
    }
    public void setValue(float value) {
        current = value;
    }
    public void setValue(double value) {
        current = (float) value;
    }

    public void setValueNumber(float current) {
        this.current = current;
    }

    public float getIncrement() {
        return increment;
    }

    public void setIncrement(float increment) {
        this.increment = increment;
    }

    public NumberType getType() {
        return type;
    }

    public enum NumberType {

        MS("Ms"), APS("Aps"), SIZE("Size"), PERCENTAGE("Percentage"), DISTANCE("Distance"), DEFAULT("");

        String name;

        NumberType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
