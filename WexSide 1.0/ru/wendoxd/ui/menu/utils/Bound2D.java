package ru.wendoxd.ui.menu.utils;

public class Bound2D {

    private final boolean interp;
    private int x, y, x2, y2;

    public Bound2D(int x, int y, int x2, int y2) {
        this(x, y, x2, y2, false);
    }

    public Bound2D(double x, double y, double x2, double y2) {
        this((int) x, (int) y, (int) x2, (int) y2);
    }

    public Bound2D(double x, double y, double x2, double y2, boolean interp) {
        this((int) x, (int) y, (int) x2, (int) y2, interp);
    }

    public Bound2D(int x, int y, int x2, int y2, boolean interp) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.interp = interp;
    }

    public void update(int x, int y, int x2, int y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void update(double x, double y, double x2, double y2) {
        this.update((int) x, (int) y, (int) x2, (int) y2);
    }

    public void offset(double x, double y) {
        this.update(x, y, x + (this.x2 - this.x), y + (this.y2 - this.y));
    }

    public boolean interp() {
        return this.interp;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getMaxX() {
        return this.x2;
    }

    public int getMaxY() {
        return this.y2;
    }

    @Override
    public String toString() {
        return "X = " + this.x + " Y = " + this.y + " X2 = " + this.x2 + " Y2 = " + this.y2 + " IN BOUND " + this.inBound() + " INTERP " + this.interp + " MX " + MenuAPI.mouseX + " MY " + MenuAPI.mouseY;
    }

    public boolean interpInBound() {
        return MenuAPI.mouseX > this.x && MenuAPI.mouseX < this.x2 && MenuAPI.mouseY > this.y && MenuAPI.mouseY < this.y2;
    }

    public boolean inBound() {
        return interp ? interpInBound() : MenuAPI.inBound(x, y, x2, y2);
    }
}
