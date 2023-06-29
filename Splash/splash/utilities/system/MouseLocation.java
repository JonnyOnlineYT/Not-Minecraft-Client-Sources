package splash.utilities.system;

public final class MouseLocation {

    public static boolean isOver(int mouseX, int mouseY, int x, int y, int x1, int y1) {
        return (mouseX >= x && mouseX <= x1) && (mouseY >= y && mouseY <= y1);
    }

}
