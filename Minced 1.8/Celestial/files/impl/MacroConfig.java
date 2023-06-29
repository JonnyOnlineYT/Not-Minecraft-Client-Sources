package Celestial.files.impl;

import Celestial.cmd.macro.Macro;
import Celestial.Smertnix;
import Celestial.files.FileManager;
import org.lwjgl.input.Keyboard;

import java.io.*;

public class MacroConfig extends FileManager.CustomFile {

    public MacroConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    public void loadFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.getFile().getAbsolutePath());
            DataInputStream in = new DataInputStream(fileInputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String bind = curLine.split(":")[0];
                String value = curLine.split(":")[1];
                if (Smertnix.instance.macroManager != null) {
                    Smertnix.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(bind), value));
                }
            }
            br.close();
        } catch (Exception e) {

        }
    }

    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (Macro m : Smertnix.instance.macroManager.getMacros()) {
                if (m != null) {
                    out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue());
                    out.write("\r\n");
                }
            }
            out.close();
        } catch (Exception ignored) {

        }
    }
}
