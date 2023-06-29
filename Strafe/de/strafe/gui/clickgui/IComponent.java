package de.strafe.gui.clickgui;

public interface IComponent {

    void drawScreen(int mouseX, int mouseY);

    void keyTyped(char typedChar, int keyCode);

    void mouseClicked(int mouseX, int mouseY, int mouseButton);

    void mouseReleased(int mouseX, int mouseY, int state);

}
