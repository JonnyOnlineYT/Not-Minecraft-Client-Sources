package de.strafe.gui.dropdown.component.impl.component.property.impl;


import de.strafe.font.Fonts;
import de.strafe.font.MCFontRenderer;
import de.strafe.gui.dropdown.component.impl.component.property.PropertyComponent;
import de.strafe.settings.impl.BooleanSetting;
import de.strafe.utils.OGLUtils;
import de.strafe.utils.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.*;
import de.strafe.gui.dropdown.component.Component;

import java.awt.*;

public final class BooleanPropertyComponent extends Component implements PropertyComponent
{
    private final BooleanSetting booleanProperty;
    private int buttonLeft;
    private int buttonTop;
    private int buttonRight;
    private int buttonBottom;

    MCFontRenderer font;
    
    public BooleanPropertyComponent(final Component parent, final BooleanSetting booleanProperty, final int x, final int y, final int width, final int height) {
        super(parent, booleanProperty.name, x, y, width, height);
        this.booleanProperty = booleanProperty;
        this.font = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Strafe/fonts/normal.ttf"), 18.0F, 0), true, true);
    }
    
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        final int x = this.getX();
        final int y = this.getY();
        final int middleHeight = this.getHeight() / 2;
        final int btnRight = x + 3 + middleHeight;
        final float maxWidth = font.getStringWidth(this.getName()) + middleHeight + 6.0f;
        final boolean hovered = this.isHovered(mouseX, mouseY);
        final boolean tooWide = maxWidth > this.getWidth();
        final boolean needScissorBox = tooWide && !hovered;
        Gui.drawRect((float)x, (float)y, x + ((tooWide && hovered) ? maxWidth : ((float)this.getWidth())), (float)(y + this.getHeight()), this.getSecondaryBackgroundColor(hovered));
        if (needScissorBox) {
            OGLUtils.startScissorBox(RenderUtil.getScaledResolution(), x, y, this.getWidth(), this.getHeight());
        }
        font.drawStringWithShadow(this.getName(), (float)(btnRight - 9), (float)(y + middleHeight - 3), -1);
        if (needScissorBox) {
            OGLUtils.endScissorBox();
        }
        final int buttonLeft = x + 2;
        this.buttonLeft = buttonLeft;
        final float n = (float)buttonLeft;
        final int buttonTop = y + middleHeight - middleHeight / 2;
        this.buttonTop = buttonTop;
        final float n2 = (float)buttonTop;
        final int buttonRight = btnRight;
        this.buttonRight = buttonRight;
        final float n3 = (float)buttonRight;
        final int buttonBottom = y + middleHeight + middleHeight / 2 + 2;
        this.buttonBottom = buttonBottom;
        Gui.drawRect(n + 95, n2, n3 + 66, (float)buttonBottom, booleanProperty.isEnabled() ? new Color(106,90,205).getRGB() :  new Color(12, 12, 12).getRGB());
        if (this.booleanProperty.isEnabled()) {
            Gui.drawRect(n + 93, n2 - 2.5, n3 + 66 + 11.7, (float)buttonBottom + 3,  new Color(110,95,240).getRGB());
        }else {
            Gui.drawRect(n + 82, n2 - 2.5, n3 + 66 + 1.7, (float)buttonBottom + 3, booleanProperty.isEnabled() ? new Color(106,90,205).getRGB() :  new Color(23, 23, 23).getRGB());
        }
    }
    
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (button == 0 && mouseX > this.buttonLeft - 2 && mouseY > this.buttonTop && mouseX < this.buttonRight + 120 && mouseY < this.buttonBottom) {
            this.booleanProperty.setEnabled(!this.booleanProperty.isEnabled());
        }
    }
    
    public BooleanSetting getProperty() {
        return this.booleanProperty;
    }
}
