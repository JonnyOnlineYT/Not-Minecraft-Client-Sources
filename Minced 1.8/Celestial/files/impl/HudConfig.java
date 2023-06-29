package Celestial.files.impl;

import Celestial.drag.comp.DragComp;
import Celestial.files.FileManager;
import Celestial.Smertnix;

import java.io.*;

public class HudConfig extends FileManager.CustomFile {

    public HudConfig(String name, boolean loadOnStart) {
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
                String x = curLine.split(":")[1];
                String y = curLine.split(":")[2];
                for (DragComp hudModule : Smertnix.instance.draggableHUD.getComponents()) {
                    if (hudModule.getName().equals(curLine.split(":")[0])) {
                        hudModule.setX(Integer.parseInt(x));
                        hudModule.setY(Integer.parseInt(y));
                    }
                }
            }
            br.close();
        } catch (Exception e) {

        }
    }

    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (DragComp draggableModule : Smertnix.instance.draggableHUD.getComponents()) {
                out.write(draggableModule.getName() + ":" + draggableModule.getX() + ":" + draggableModule.getY());
                out.write("\r\n");
            }
            out.close();
        } catch (Exception ignored) {

        }
    }
}
