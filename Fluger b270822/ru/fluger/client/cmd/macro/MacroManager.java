/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.cmd.macro;

import java.util.ArrayList;
import java.util.List;
import ru.fluger.client.cmd.macro.Macro;

public class MacroManager {
    public List<Macro> macros = new ArrayList<Macro>();

    public List<Macro> getMacros() {
        return this.macros;
    }

    public void addMacro(Macro macro) {
        this.macros.add(macro);
    }

    public void deleteMacroByKey(int key) {
        this.macros.removeIf(macro -> macro.getKey() == key);
    }
}

