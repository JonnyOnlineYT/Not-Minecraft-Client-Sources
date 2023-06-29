package splash.gui.huzini.components.components;

import net.minecraft.client.gui.Gui;
import splash.Splash;
import splash.api.value.impl.NumberValue;
import splash.gui.huzini.components.Component;
import splash.utilities.system.MouseLocation;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Slider extends Component {

    private NumberValue property;
    private boolean moving;
    private int x, y, width, height;

    private ModButton dad;

    public Slider(ModButton dad, ComponentContainer parent, NumberValue plugin, int x, int y, int width, int height) {
        super(parent, plugin.getValueName());
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.property = plugin;
        this.dad = dad;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        super.draw(mouseX, mouseY, partialTicks);
        final int mainCol = Splash.getInstance().getClientColor();
        final int altCol = new Color(0x4f4f4f).darker().getRGB();
        int i = 0;
        int color = -1;

        if (property.getValue() instanceof Double) {
            double value = (double) property.getValue();
            double min = (double) property.getMinimumValue();
            double max = (double) property.getMaximumValue();
            Gui.drawRect(getParent().getParent().getX() + getParent().getX() + dad.getX() + x,
                    getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y,
                    getParent().getParent().getX() + getParent().getX() + x + width,
                    getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y + height, 0xFF4f4f4f);
            double sliderWidth = (width * (value - min) / (max - min));
            Gui.drawRect(getParent().getParent().getX() + getParent().getX() + dad.getX() + x, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y, getParent().getParent().getX() + getParent().getX() + dad.getX() + x + sliderWidth, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y + height, Splash.getInstance().getClientColor());
            Splash.getInstance().getFontRenderer().drawStringWithShadow(property.getValueName() + ": " + property.getValue(), getParent().getParent().getX() + getParent().getX() + dad.getX() + x + 2, (getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y) + 2, 0xFFFFFFFF);
            if (moving) {
                double diff = Math.min(width, Math.max(0, mouseX - (getParent().getParent().getX() + getParent().getX() + dad.getX() + x)));
                if (diff == 0) {
                    property.setValueObject(property.getMinimumValue());
                } else {
                    double newValue = roundToPlace(((diff / width) * (max - min) + min), 2);
                    property.setValueObject(newValue);
                }
            }
        }
        if (property.getValue() instanceof Integer) {
            int value = (int) property.getValue();
            int min = (int) property.getMinimumValue();
            int max = (int) property.getMaximumValue();
            Gui.drawRect(getParent().getParent().getX() + getParent().getX() + dad.getX() + x,
                    getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y,
                    getParent().getParent().getX() + getParent().getX() + x + width,
                    getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y + height, 0xFF4f4f4f);
            double sliderWidth = (width * (value - min) / (max - min));
            Gui.drawRect(getParent().getParent().getX() + getParent().getX() + dad.getX() + x, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y, getParent().getParent().getX() + getParent().getX() + dad.getX() + x + sliderWidth, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y + height, new Color(color).darker().getRGB());
            Splash.getInstance().getFontRenderer().drawStringWithShadow(property.getValueName() + " : " + property.getValue(), getParent().getParent().getX() + getParent().getX() + dad.getX() + x + 2, (getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y) + 2, 0xFFFFFFFF);
            if (moving) {
                double diff = Math.min(width, Math.max(0, mouseX - (getParent().getParent().getX() + getParent().getX() + dad.getX() + x)));
                if (diff == 0) {
                    property.setValueObject(property.getMinimumValue());
                } else {
                    int newValue = (int) Math.round(roundToPlace(((diff / width) * (max - min) + min), 1));
                    property.setValueObject(newValue);
                }
            }
        }
        if (property.getValue() instanceof Float) {
            float value = (float) property.getValue();
            float min = (float) property.getMinimumValue();
            float max = (float) property.getMaximumValue();
            Gui.drawRect(getParent().getParent().getX() + getParent().getX() + dad.getX() + x,
                    getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y,
                    getParent().getParent().getX() + getParent().getX() + x + width,
                    getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y + height, 0xFF4f4f4f);
            double sliderWidth = (width * (value - min) / (max - min));
            Gui.drawRect(getParent().getParent().getX() + getParent().getX() + dad.getX() + x, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y, getParent().getParent().getX() + getParent().getX() + dad.getX() + x + sliderWidth, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y + height, new Color(color).getRGB());
            Splash.getInstance().getFontRenderer().drawStringWithShadow(property.getValueName() + " : " + property.getValue(), getParent().getParent().getX() + getParent().getX() + dad.getX() + x + 2, (getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + y) + 2, 0xFFFFFFFF);
            if (moving) {
                double diff = Math.min(width, Math.max(0, mouseX - (getParent().getParent().getX() + getParent().getX() + dad.getX() + x)));
                if (diff == 0) {
                    property.setValueObject(property.getMinimumValue());
                } else {
                    float newValue = (float) roundToPlace(((diff / width) * (max - min) + min), 2);
                    property.setValueObject(newValue);
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseLocation.isOver(mouseX, mouseY, getParent().getParent().getX() + getParent().getX() + dad.getX() + this.x, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + this.y, getParent().getParent().getX() + getParent().getX() + dad.getX() + this.x + this.width, getParent().getParent().getY() + parent.getY() + dad.getY() + 12 + this.y + this.height) && mouseButton == 0) {
            this.moving = !moving;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.moving = false;
    }

    public static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.UP);
        return bd.doubleValue();
    }
}
