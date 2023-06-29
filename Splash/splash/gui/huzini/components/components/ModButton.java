package splash.gui.huzini.components.components;

import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import splash.Splash;
import splash.api.module.Module;
import splash.gui.huzini.components.Component;
import splash.utilities.system.ClientLogger;
import splash.utilities.system.MouseLocation;

import java.awt.*;
import java.util.ArrayList;

public class ModButton extends Component {

    private boolean extended;
    private Module host;
    private boolean middleClick;
    private final ArrayList<Component> components = new ArrayList<>();

    public ModButton(ComponentContainer parent, Module plugin, int x, int y, int width, int height) {
        super(parent, plugin.getModuleDisplayName());
        setX(x);
        setY(y);
        setHeight(height);
        setWidth(width);
        this.host = plugin;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        super.draw(mouseX, mouseY, partialTicks);
        int color = Splash.getInstance().getClientColor();
        Gui.drawRect(getParent().getParent().getX() + getParent().getX() + getX(), getParent().getParent().getY() + getParent().getY() + getY(), getParent().getParent().getX() + getParent().getX() + getX() + getWidth(), getParent().getParent().getY() + getParent().getY() + getY() + 12, host.isModuleActive() ? new Color(color).darker().getRGB() : new Color(0x4f4f4f).darker().getRGB());
        Splash.getInstance().getFontRenderer().drawStringWithShadow(getLabel() + getKey(), getParent().getParent().getX() + getParent().getX() + getX() + 2, getParent().getParent().getY() + getParent().getY() + getY() + 2, 0xFFFFFFFF);
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

    public String getKey() {
        if (isMiddleClick()) {
            return " [" + Keyboard.getKeyName(host.getModuleMacro()) + "]";
        } else {
            return "";
        }
    }

    @Override
    public void mouseClicked(int x, int y, int mouseButton) {
        super.mouseClicked(x, y, mouseButton);
        if (MouseLocation.isOver(x, y, getParent().getParent().getX() + getParent().getX() + this.getX(), getParent().getParent().getY() + getParent().getY() + this.getY(), getParent().getParent().getX() + getParent().getX() + this.getX() + getWidth(), getParent().getParent().getY() + getParent().getY() + this.getY() + 12)) {
            if (mouseButton == 0) {
                host.activateModule();
            } else if (mouseButton == 1) {
                extended = !extended;
            } else if (mouseButton == 2) {
                setMiddleClick(!isMiddleClick());
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

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (isMiddleClick()) {
            if (!Keyboard.getKeyName(keyCode).equalsIgnoreCase("ESCAPE")) {
                ClientLogger.printToMinecraft("Bound " + host.getModuleDisplayName() + " to " + Keyboard.getKeyName(keyCode));
                host.setModuleMacro(keyCode);
                setMiddleClick(false);
            }
        }
    }


    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public boolean isExtended() {
        return extended;
    }

    public Module getHost() {
        return host;
    }

    public void setHost(Module host) {
        this.host = host;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public boolean isMiddleClick() {
        return middleClick;
    }

    public void setMiddleClick(boolean middleClick) {
        this.middleClick = middleClick;
    }
}
