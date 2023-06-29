package de.strafe.gui.dropdown.component.impl.component.property.impl;


import de.strafe.font.Fonts;
import de.strafe.font.MCFontRenderer;
import de.strafe.gui.dropdown.component.impl.ExpandableComponent;
import de.strafe.gui.dropdown.component.impl.component.property.PropertyComponent;
import de.strafe.settings.impl.ModeSetting;
import de.strafe.utils.OGLUtils;
import de.strafe.utils.StringUtils;
import de.strafe.gui.dropdown.component.Component;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;


import java.awt.*;


public final class EnumBoxProperty extends ExpandableComponent implements PropertyComponent
{
    private final ModeSetting property;
    MCFontRenderer font;

    public EnumBoxProperty(final Component parent, final ModeSetting property, final int x, final int y, final int width, final int height) {
        super(parent, property.name, x, y, width, height);
        this.font = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Strafe/fonts/normal.ttf"), 18.0F, 0), true, true);
        this.property = property;
    }
    
    @Override
    public ModeSetting getProperty() {
        return this.property;
    }
    
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        final String selectedText = StringUtils.upperSnakeCaseToPascal(this.property.getMode());
        final int dropDownBoxY = y + 10;
        final boolean needScissor = font.getStringWidth(selectedText) > width - 4;
        final int textColor = 16777215;
        final int bgColor = this.getSecondaryBackgroundColor(this.isHovered(mouseX, mouseY));
        Gui.drawRect((float)x, (float)y, (float)(x + width), (float)(y + height), bgColor);

        Gui.drawRect(x + 2.5, dropDownBoxY + 1, x + this.getWidth() - 2.5, (double)(dropDownBoxY + 12), new Color(7, 7, 7).getRGB());
        if (needScissor) {
            OGLUtils.startScissorBox(scaledResolution, x + 2, dropDownBoxY + 2, width - 5, 10);
        }
        font.drawStringWithShadow(this.getName(), x + 3.5f, dropDownBoxY + 1.5f, -1);
        font.drawStringWithShadow(selectedText, x + 115 -  font.getStringWidth(selectedText), dropDownBoxY + 1.5f,  new Color(106,90,205).getRGB());
        if (needScissor) {
            OGLUtils.endScissorBox();
        }
        if (this.isExpanded()) {
            Gui.drawRect((float)(x + 1), (float)(y + height), (float)(x + width - 1), (float)(y + this.getHeightWithExpand()), new Color(21, 21, 21).getRGB());
            this.handleRender(x, y + this.getHeight() + 2, width, textColor);
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (this.isExpanded()) {
            this.handleClick(mouseX, mouseY, this.getX(), this.getY() + this.getHeight() + 2, this.getWidth());
        }
    }
    
    private <T extends Enum<T>> void handleRender(final int x, int y, final int width, final int textColor) {
        final int enabledColor =  new Color(106,90,205).getRGB();
        final ModeSetting property = (ModeSetting)this.property;
        for (final String e : property.getModes()) {

            font.drawStringWithShadow(StringUtils.upperSnakeCaseToPascal(e), (float)(x + 1 + 45 - (StringUtils.upperSnakeCaseToPascal(e).length()) - getLenghtX(StringUtils.upperSnakeCaseToPascal(e))), (float)y, property.is(e) ? new Color(106,90,205).getRGB() : -1);
            y += 12;
        }
    }

    private double getLenghtX(String e) {
        double x;
        if (e.length() > 5) {
            x = e.length() - 10;
        }else if (e.length() > 10) {
            x = e.length() - 11;
        }else if (e.length() > 4) {
            x = e.length() - 7;
        }else if (e.length() > 3) {
            x = e.length() - 9;
        }else {
            x = -3;
        }
        return x;
    }

    
    private <T extends Enum<T>> void handleClick(final int mouseX, final int mouseY, final int x, int y, final int width) {
        final ModeSetting property = (ModeSetting)this.property;
        for (final String e : property.getModes()) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + 15 - 3) {
                property.setSelected(e);
            }
            y += 12;
        }
    }
    
    @Override
    public int getHeightWithExpand() {
        return this.getHeight() + this.property.getModes().size() * 12;
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public boolean canExpand() {
        return this.property.getModes().size() > 1;
    }
}
