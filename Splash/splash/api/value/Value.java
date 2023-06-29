package splash.api.value;

import splash.Splash;
import splash.api.module.Module;
import splash.client.events.font.FontChangeEvent;

/**
 * Author: Ice
 * Created: 18:31, 12-Jun-20
 * Project: Client
 */
public class Value<T> {

    private String valueName;
    private T valueObject;
    private T defaultValueObject;
    private Module parent;

    public Value(String valueName, T defaultValueObject, Module parent) {
        this.valueName = valueName;
        this.defaultValueObject = defaultValueObject;
        this.parent = parent;
        if(valueObject == null) {
            valueObject = defaultValueObject;
        }
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public T getValue() {
        return valueObject;
    }

    public void setValueObject(T valueObject) {
        if(this.getValueName().equalsIgnoreCase("Font Mode")) {
            FontChangeEvent fontChangeEvent = new FontChangeEvent();
            Splash.getInstance().getEventBus().publish(fontChangeEvent);
        }
        this.valueObject = valueObject;
    }

    public T getDefaultValueObject() {
        return defaultValueObject;
    }

    public void setDefaultValueObject(T defaultValueObject) {
        this.defaultValueObject = defaultValueObject;
    }

    public Module getParent() {
        return parent;
    }

    public void setParent(Module parent) {
        this.parent = parent;
    }
}
