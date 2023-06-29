package splash.client.managers.value;

import splash.Splash;
import splash.api.manager.ClientManager;
import splash.api.module.Module;
import splash.api.value.Value;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.modules.combat.Aura;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Ice
 * Created: 18:34, 12-Jun-20
 * Project: Client
 */
public class ValueManager extends ClientManager<Value> {

    @Override
    public String managerName() {
        return "ValueManager";
    }

    public void loadValues(Module module) {
        List<Value> values = new ArrayList<>();
        for (final Field field : module.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object obj = field.get(module);

                if (obj instanceof NumberValue || obj instanceof BooleanValue || obj instanceof ModeValue) {
                    getContents().add((Value) obj);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Value> getValuesFrom(Module module) {
        ArrayList<Value> values = new ArrayList<>();

        for (Value value : getContents()) {
            if (value.getParent() == module) {
                values.add(value);
            }
        }
        return values;
    }

    public Value getValueByName(String valueName) {
        for(Value value : getContents()) {
            if(value.getValueName().equalsIgnoreCase(valueName)) {
                return value;
            }
        }
        return null;
    }


    public Value getValue(Module parent, String valueName) {
        for(Value value : getContents()) {
            if(value.getValueName().equalsIgnoreCase(valueName) && value.getParent() == parent) {
                    return value;
                }
        }
        return null;
    }


}
