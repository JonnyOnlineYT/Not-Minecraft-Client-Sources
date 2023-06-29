package splash.api.notification;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.EnumChatFormatting;

import java.awt.Color;

import splash.Splash;
import splash.utilities.visual.ColorUtilities;
import splash.utilities.visual.RenderUtilities;

public class Notification {

    private String title;
    private String messsage;
    private long start;

    private long fadedIn;
    private long fadeOut;
    private long end;


    public Notification(String title,int timeShown) {
        this.title = title;
        fadedIn = 200 * timeShown;
        fadeOut = fadedIn + 500 * timeShown;
        end = fadeOut + fadedIn;
    }

    public Notification(String title, String message,int timeShown) {
        this.title = title;
        this.messsage = message;
        fadedIn = 200 * timeShown;
        fadeOut = fadedIn + 500 * timeShown;
        end = fadeOut + fadedIn;
    }

    public void show() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= end;
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public void render() {
        double offset = 0;
        int width = 120;
        int height = 30;
        long time = getTime();

        if (time < fadedIn) {
            offset = Math.tanh(time / (double) (fadedIn) * 3.0) * width;
        } else if (time > fadeOut) {
            offset = (Math.tanh(3.0 - (time - fadeOut) / (double) (end - fadeOut) * 3.0) * width);
        } else {
            offset = width;
        }

        Color color = new Color(0, 0, 0, 220);
        Color color1;
        int y = 0;
        int rainbow = ColorUtilities.getRainbow(-3000, y * -9).getRGB();
            color1 = new Color(204, 0, 18);
            int i = Math.max(0, Math.min(255, (int) (Math.sin(time / 100.0) * 255.0 / 2 + 127.5)));
            color = new Color(i, 0, 0, 220);

        int yCount = 0;
        GuiIngame.drawRect(GuiScreen.width - offset - 20 , GuiScreen.height - 15 - height, GuiScreen.width - offset - 20, GuiScreen.height - 6,-1);
        boolean check = messsage != title && messsage.length() > 1 && messsage != null;
        RenderUtilities.INSTANCE.drawRect((int) (GuiScreen.width - offset- 20), GuiScreen.height - 12 - height, (GuiScreen.width) - 3, GuiScreen.height - (check ? 7 : 18), new Color(0,0,0, 120).getRGB());
        RenderUtilities.INSTANCE.drawRect((int) (GuiScreen.width - offset - 22), GuiScreen.height - 12 - height, (GuiScreen.width - offset - 20), GuiScreen.height - (check ? 7 : 18), rainbow);
        
        yCount += 11;


        Splash.getInstance().getFontRenderer().drawStringWithShadow(title, (int) (GuiScreen.width - offset - 15), GuiScreen.height - 4 - height, new Color(255,255,255, 255).getRGB());
        if (check) {
        	Splash.getInstance().getFontRenderer().drawStringWithShadow(messsage, (int) (GuiScreen.width - offset - 15), GuiScreen.height + 13 - height, new Color(255,255,255, 255).getRGB());
        }
    }
}