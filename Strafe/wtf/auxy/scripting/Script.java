package wtf.auxy.scripting;

import com.eventapi.events.Event;
import de.strafe.Strafe;
import de.strafe.modules.Module;
import de.strafe.settings.impl.BooleanSetting;
import de.strafe.settings.impl.ModeSetting;
import de.strafe.settings.impl.NumberSetting;

import javax.script.*;
import java.io.*;
import java.util.*;

public class Script
{
    public Invocable invocable;
    public ScriptEngineManager engineManager;
    public ScriptEngine scriptEngine;
    public String scriptName;
    public File scriptFile;
    public Module scriptModule;
    public String currentEvent;
    
    public Script(final Module m) {
        this.scriptModule = m;
        this.scriptName = m.getName();
        this.scriptFile = new File(Strafe.INSTANCE.scriptingdir, m.getName() + ".js");
        this.engineManager = new ScriptEngineManager();
        this.scriptEngine = this.engineManager.getEngineByName("JavaScript");
        this.invocable = (Invocable)this.scriptEngine;
    }
    
    public void callEvent(final String funcName) {
        try {
            this.scriptEngine.eval(new FileReader(this.scriptFile));
            this.invocable.invokeFunction(funcName, new Object[0]);
            System.out.print(funcName);

        }
        catch (Exception ex) {}
    }
    
    public void callEvent(final String funcName, final Event e) {
        try {
            this.scriptEngine.eval(new FileReader(this.scriptFile));
            this.invocable.invokeFunction(funcName, e);
        }
        catch (Exception ex) {}
    }
    
    public void putVars() {
        this.scriptEngine.put("scriptManager", Strafe.INSTANCE.scriptManager);
        this.scriptEngine.put("module", this.scriptModule);
        this.scriptEngine.put("script", this);
    }
    
    public void addSettings(final String mode, final Object... args) {
        switch (mode) {
            case "double": {
                this.scriptModule.addSettings(new NumberSetting((String)args[0], (double)args[1], (double)args[2], (double)args[3], (double)args[4]));
                break;
            }
            case "boolean": {
                this.scriptModule.addSettings(new BooleanSetting((String)args[0], (boolean)args[1]));
                break;
            }
            case "mode": {
                final List<String> strs = new ArrayList<String>();
                for (int a = 2; a < args.length; ++a) {
                    strs.add((String)args[a]);
                }
                final String[] strings = strs.toArray(new String[0]);
                this.scriptModule.addSettings(new ModeSetting((String)args[0], (String)args[1], strings));
                break;
            }
        }
    }

    public String getName() {
        return scriptName;
    }

}
