package splash.gui.huzini.components.components;

import net.minecraft.client.gui.Gui;
import splash.Splash;
import splash.api.value.impl.ModeValue;
import splash.gui.huzini.components.Component;
import splash.utilities.system.MouseLocation;

public class Spinner extends Component {
    private int x, y, width, height;
    private ModeValue host;
    private ModButton dad;

    public Spinner(ModButton dad, ComponentContainer parent, ModeValue plugin, int x, int y, int width, int height) {
        super(parent, plugin.getValueName());
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.host = plugin;
        this.dad = dad;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        super.draw(mouseX, mouseY, partialTicks);
        Gui.drawRect(getParent().getParent().getX() + getParent().getX() + dad.getX() + x,
                getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y,
                getParent().getParent().getX() + getParent().getX() + x + width,
                getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y + height,
                0xFF4f4f4f);
        Splash.getInstance().getFontRenderer().drawStringWithShadow(getLabel() + ": " + host.getValue().toString(), getParent().getParent().getX() + getParent().getX() + 2, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y + 2, 0xFFFFFFFF);
    }

    @Override
    public void mouseClicked(int x, int y, int mouseButton) {
        super.mouseClicked(x, y, mouseButton);
        if (MouseLocation.isOver(x, y, getParent().getParent().getX() + getParent().getX() + dad.getX() + this.x, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + this.y, getParent().getParent().getX() + getParent().getX() + dad.getX() + this.x + width, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + this.y + height)) {
            if (mouseButton == 0) {
                host.increment();
            } else if (mouseButton == 1) {
                host.decrement();
            }
        }
    }
}


