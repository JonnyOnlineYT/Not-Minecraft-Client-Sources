package splash.api.theme;

import net.minecraft.client.Minecraft;

/**
 * Author: Ice
 * Created: 15:16, 31-May-20
 * Project: Client
 */
public abstract class Theme {

    private String themeName;
    public Minecraft mc = Minecraft.getMinecraft();

    public Theme(String themeName) {
        this.themeName = themeName;
    }

    public abstract void drawWatermark();

    public abstract void drawArraylist();

    public abstract void drawTabGUI();

    public abstract void onKey(int keyCode);

    public void drawAll() {
     drawWatermark();
     drawArraylist();
     drawTabGUI();
    }

}
