package splash.gui.huzini.components.components;

import net.minecraft.client.gui.Gui;
import splash.Splash;
import splash.api.value.impl.BooleanValue;
import splash.gui.huzini.components.Component;
import splash.utilities.system.MouseLocation;

import java.awt.*;

public class ValueButton extends Component {
    private int x, y, width, height;
    private BooleanValue<Boolean> host;
    private ModButton dad;

    public ValueButton(ModButton dad, ComponentContainer parent, BooleanValue<Boolean> plugin, int x, int y, int width, int height) {
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
        int i = 0;
        int color = Splash.getInstance().getClientColor();
        final int mainCol = new Color(color).darker().getRGB();
        Gui.drawRect(getParent().getParent().getX() + getParent().getX() + dad.getX() + x,
                getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y,
                getParent().getParent().getX() + getParent().getX() + x + 12,
                getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y + height,
                new Color(0x4f4f4f).darker().getRGB());
        if (host.getValue()) {
            Gui.drawRect(getParent().getParent().getX() + getParent().getX() + dad.getX() + x + 2,
                    getParent().getParent().getY() + parent.getY() + dad.getY() + 14 + y,
                    getParent().getParent().getX() + getParent().getX() + x + 10,
                    getParent().getParent().getY() + parent.getY() + dad.getY() + 10 + y + height,
                    mainCol);
        }
        Splash.getInstance().getFontRenderer().drawStringWithShadow(getLabel(), getParent().getParent().getX() + getParent().getX() + 14, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y + 2, 0xFFFFFFFF);
    }

    @Override
    public void mouseClicked(int x, int y, int mouseButton) {
        super.mouseClicked(x, y, mouseButton);
        if (MouseLocation.isOver(x, y, getParent().getParent().getX() + getParent().getX() + dad.getX() + this.x, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + this.y, getParent().getParent().getX() + getParent().getX() + dad.getX() + this.x + 12, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + this.y + height)) {
            if (mouseButton == 0) {
                host.setValueObject(!host.getValue());
            }
        }
    }
}
