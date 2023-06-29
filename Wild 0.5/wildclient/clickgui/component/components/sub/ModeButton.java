//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.clickgui.component.components.sub;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import black.nigger.wildclient.module.Module;
import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.clickgui.component.components.Button;
import black.nigger.wildclient.clickgui.component.Component;

public class ModeButton extends Component
{
    private boolean hovered;
    private Button parent;
    private Setting set;
    private int offset;
    private int x;
    private int y;
    private Module mod;
    private int modeIndex;
    
    public ModeButton(final Setting set, final Button button, final Module mod, final int offset) {
        this.set = set;
        this.parent = button;
        this.mod = mod;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
        this.modeIndex = 0;
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() * 1, this.parent.parent.getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, -15658735);
        GL11.glPushMatrix();
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Mode: " + this.set.getValString(), (float)(this.parent.parent.getX() + 7), (float)(this.parent.parent.getY() + this.offset), 11393254);
        GL11.glPopMatrix();
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            final int maxIndex = this.set.getOptions().size();
            if (this.modeIndex + 1 > maxIndex) {
                this.modeIndex = 0;
            }
            else {
                ++this.modeIndex;
            }
            this.set.setValString(this.set.getOptions().get(this.modeIndex));
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
