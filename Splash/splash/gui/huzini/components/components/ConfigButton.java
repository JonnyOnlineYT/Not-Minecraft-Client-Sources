package splash.gui.huzini.components.components;

import net.minecraft.client.gui.Gui;
import splash.Splash;
import splash.api.config.ClientConfig;
import splash.gui.huzini.components.Component;
import splash.utilities.system.ClientLogger;
import splash.utilities.system.MouseLocation;

import java.awt.*;
import java.util.ArrayList;

public class ConfigButton extends Component {

    private boolean extended;
    private ClientConfig host;
    private final ArrayList<Component> components = new ArrayList<>();

    public ConfigButton(ComponentContainer parent, ClientConfig plugin, int x, int y, int width, int height) {
        super(parent, plugin.getConfigName());
        setX(x);
        setY(y);
        setHeight(height);
        setWidth(width);
        this.host = plugin;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        super.draw(mouseX, mouseY, partialTicks);
        int color = new Color(16777215).getRGB();

        Gui.drawRect(getParent().getParent().getX() + getParent().getX() + getX(), getParent().getParent().getY() + getParent().getY() + getY(), getParent().getParent().getX() + getParent().getX() + getX() + getWidth(), getParent().getParent().getY() + getParent().getY() + getY() + 12, new Color(0x4f4f4f).darker().getRGB());
        Splash.getInstance().getFontRenderer().drawStringWithShadow(getLabel(), getParent().getParent().getX() + getParent().getX() + getX() + 4, getParent().getParent().getY() + getParent().getY() + getY() + 2, 0xFFFFFFFF);
        if (this.extended) {
            int newHeight = 12;
            for (Component component : getComponents()) {
                component.draw(mouseX, mouseY, partialTicks);
                newHeight += 13;
            }
            setHeight(newHeight);
        } else {
            setHeight(12);
        }
    }

    @Override
    public void mouseClicked(int x, int y, int mouseButton) {
        super.mouseClicked(x, y, mouseButton);
        if (MouseLocation.isOver(x, y, getParent().getParent().getX() + getParent().getX() + this.getX(), getParent().getParent().getY() + getParent().getY() + this.getY(), getParent().getParent().getX() + getParent().getX() + this.getX() + getWidth(), getParent().getParent().getY() + getParent().getY() + this.getY() + 12)) {
            if (mouseButton == 0) {
                ClientLogger.printToMinecraft("Loaded config " + host.getConfigName());
                Splash.getInstance().getConfigManager().loadConfig(host);
            } else if (mouseButton == 1) {
                extended = !extended;
            }
        }
        if (isExtended()) {
            for (Component component : getComponents()) {
                component.mouseClicked(x, y, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int x, int y, int mouseButton) {
        super.mouseReleased(x, y, mouseButton);
        this.getComponents().forEach(component -> component.mouseReleased(x, y, mouseButton));
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public boolean isExtended() {
        return extended;
    }

    public ClientConfig getHost() {
        return host;
    }

    public void setHost(ClientConfig host) {
        this.host = host;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }
}
