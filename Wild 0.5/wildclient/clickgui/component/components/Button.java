//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.clickgui.component.components;

import black.nigger.wildclient.clickgui.ClickGui;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.util.Iterator;
import black.nigger.wildclient.clickgui.component.components.sub.VisibleButton;
import black.nigger.wildclient.clickgui.component.components.sub.Keybind;
import black.nigger.wildclient.clickgui.component.components.sub.Checkbox;
import black.nigger.wildclient.clickgui.component.components.sub.Slider;
import black.nigger.wildclient.clickgui.component.components.sub.ModeButton;
import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.Wild;
import java.util.ArrayList;
import black.nigger.wildclient.clickgui.component.Frame;
import black.nigger.wildclient.module.Module;
import black.nigger.wildclient.clickgui.component.Component;

public class Button extends Component
{
    public Module mod;
    public Frame parent;
    public int offset;
    private boolean isHovered;
    private ArrayList<Component> subcomponents;
    public boolean open;
    private int height;
    
    public Button(final Module mod, final Frame parent, final int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<Component>();
        this.open = false;
        this.height = 12;
        int opY = offset + 12;
        if (Wild.instance.settingsManager.getSettingsByMod(mod) != null) {
            for (final Setting s : Wild.instance.settingsManager.getSettingsByMod(mod)) {
                if (s.isCombo()) {
                    this.subcomponents.add(new ModeButton(s, this, mod, opY));
                    opY += 12;
                }
                if (s.isSlider()) {
                    this.subcomponents.add(new Slider(s, this, opY));
                    opY += 12;
                }
                if (s.isCheck()) {
                    this.subcomponents.add(new Checkbox(s, this, opY));
                    opY += 12;
                }
            }
        }
        this.subcomponents.add(new Keybind(this, opY));
        this.subcomponents.add(new VisibleButton(this, mod, opY));
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
        int opY = this.offset + 12;
        for (final Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 12;
        }
    }
    
    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, this.isHovered ? (this.mod.isToggled() ? new Color(-14540254).darker().getRGB() : -14540254) : (this.mod.isToggled() ? new Color(14, 14, 14).getRGB() : -15658735));
        GL11.glPushMatrix();
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.mod.getName(), (float)(this.parent.getX() + 2), (float)(this.parent.getY() + this.offset + 2), this.mod.isToggled() ? 9109504 : -1);
        if (this.subcomponents.size() > 2) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.open ? "V" : "<", (float)(this.parent.getX() + this.parent.getWidth() - 10), (float)(this.parent.getY() + this.offset + 3), -1);
        }
        GL11.glPopMatrix();
        if (this.open && !this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.renderComponent();
            }
            Gui.drawRect(this.parent.getX() + 2, this.parent.getY() + this.offset + 12, this.parent.getX() + 3, this.parent.getY() + this.offset + (this.subcomponents.size() + 1) * 12, ClickGui.color);
        }
    }
    
    @Override
    public int getHeight() {
        if (this.open) {
            return 12 * (this.subcomponents.size() + 1);
        }
        return 12;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (final Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int key) {
        for (final Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }
}
