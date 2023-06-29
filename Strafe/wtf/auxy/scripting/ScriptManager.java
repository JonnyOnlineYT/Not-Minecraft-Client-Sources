package wtf.auxy.scripting;

import de.strafe.Strafe;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.utils.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.io.FileUtils;
import wtf.auxy.scripting.api.Api;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.*;

public class ScriptManager
{
    private ArrayList<Script> scripts;
    private ScriptEngine scriptEngine;
    private Api api;



    public ScriptManager() {
        this.scripts = new ArrayList<Script>();
        api = new Api();
    }
    
    public void onStart() throws IOException, ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        scriptEngine = manager.getEngineByName("JavaScript");
        for (final File f : Objects.requireNonNull(Strafe.INSTANCE.scriptingdir.listFiles())) {
            String scriptContent = FileUtil.readFile(f);
            try {scriptEngine.eval(scriptContent);} catch (ScriptException ignored) {}
            final Module mod = new Module((String) scriptEngine.get("name"), 0, Category.SCRIPTS);
            Strafe.INSTANCE.scriptManager.scripts.add(new Script(mod));
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(Strafe.INSTANCE.scriptingdir, f.getName())));
            String script = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                script += line + "\n";
            }
            //api.load(script);
            Strafe.INSTANCE.moduleManager.modules.add(mod);try {scriptEngine.eval(scriptContent);} catch (ScriptException e) {e.printStackTrace();
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Failed to load script" + f.getAbsolutePath()));
            }

        }
        this.loadScripts();
    }

    
    public Script getScript(final String str) {
        for (final Script s : this.scripts) {
            if (s.scriptName.equals(str)) {
                return s;
            }
        }
        return null;
    }


    /*
    events
     */


    public void loadScripts() {
        for (final Script c : Strafe.INSTANCE.scriptManager.scripts) {
            c.putVars();
            c.callEvent("onLoad");
            c.currentEvent = "onLoad";
        }

    }

    public void eventMotion() {
        for (final Script c : Strafe.INSTANCE.scriptManager.scripts) {
            c.putVars();
            c.callEvent("onMotion");
            c.currentEvent = "onMotion";
        }
    }

    public void eventUpdate() {
        for (final Script c : Strafe.INSTANCE.scriptManager.scripts) {
            c.putVars();
            c.callEvent("onUpdate");
            c.currentEvent = "onUpdate";
        }
    }

}
