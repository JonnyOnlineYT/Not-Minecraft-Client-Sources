//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.clickgui;

import java.io.IOException;
import java.util.Iterator;
import black.nigger.wildclient.clickgui.component.Component;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.clickgui.component.Frame;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen
{
    public static ArrayList<Frame> frames;
    public static int color;
    
    public ClickGui() {
        ClickGui.frames = new ArrayList<Frame>();
        int frameX = 20;
        for (final Category category : Category.values()) {
            final Frame frame = new Frame(category);
            frame.setX(frameX);
            ClickGui.frames.add(frame);
            frameX += frame.getWidth() + 2;
        }
    }
    
    public void initGui() {
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        for (final Frame frame : ClickGui.frames) {
            frame.renderFrame(this.fontRenderer);
            frame.updatePosition(mouseX, mouseY);
            for (final Component comp : frame.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final Frame frame : ClickGui.frames) {
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frame.dragX = mouseX - frame.getX();
                frame.dragY = mouseY - frame.getY();
            }
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
        for (final Frame frame : ClickGui.frames) {
            if (frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        for (final Frame frame : ClickGui.frames) {
            frame.setDrag(false);
        }
        for (final Frame frame : ClickGui.frames) {
            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }
    
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    static {
        ClickGui.color = -1;
    }
}
