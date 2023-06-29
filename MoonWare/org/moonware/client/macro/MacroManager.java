package org.moonware.client.macro;

import java.util.ArrayList;
import java.util.List;

public class MacroManager {

    public List<Macro> macros = new ArrayList<>();

    public List<Macro> getMacros() {
        return macros;
    }

    public void addMacro(Macro macro) {
        macros.add(macro);
    }

    public void deleteMacroByKey(int key) {
        macros.removeIf(macro -> macro.getKey() == key);
    }

}
