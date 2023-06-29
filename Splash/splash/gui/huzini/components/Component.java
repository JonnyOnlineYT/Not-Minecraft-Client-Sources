package splash.gui.huzini.components;


import splash.gui.huzini.components.components.ComponentContainer;

public class Component {

    private final String label;
    private int height, width, x, y;
    public final ComponentContainer parent;

    public Component(ComponentContainer parent, String label) {
        this.label = label;
        this.parent = parent;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {

    }

    public void mouseClicked(int x, int y, int mouseButton) {
    }

    public void mouseReleased(int x, int y, int mouseButton) {

    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public ComponentContainer getParent() {
        return parent;
    }

    public String getLabel() {
        return label;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
