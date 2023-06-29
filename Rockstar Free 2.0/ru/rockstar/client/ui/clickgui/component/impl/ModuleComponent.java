package ru.rockstar.client.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.ui.clickgui.ClickGuiScreen;
import ru.rockstar.client.ui.clickgui.Panel;
import ru.rockstar.client.ui.clickgui.SorterHelper;
import ru.rockstar.client.ui.clickgui.component.AnimationState;
import ru.rockstar.client.ui.clickgui.component.Component;
import ru.rockstar.client.ui.clickgui.component.ExpandableComponent;
import ru.rockstar.client.ui.settings.Setting;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import org.lwjgl.input.Keyboard;

import org.lwjgl.opengl.GL11;

import javafx.animation.Interpolator;

import java.awt.*;


public final class ModuleComponent extends ExpandableComponent  {
    Minecraft mc = Minecraft.getMinecraft();
    private final Feature module;
    private final AnimationState state;
    private boolean binding;
    private int buttonLeft;
    private int buttonTop;
    private int buttonRight;
    private int buttonBottom;
    private int height;
    public float progress;
    private long lastMS;
    public float s1 = 0f;
	public float s2 = 0f;

    public ModuleComponent(Component parent, Feature module, int x, int y, int width, int height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.module = module;
        this.state = AnimationState.STATIC;
        int propertyX = Panel.X_ITEM_OFFSET;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanSettingComponent(this, (BooleanSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 6));
            } else if (setting instanceof ColorSetting) {
                components.add(new ColorPickerComponent(this, (ColorSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT));
            } else if (setting instanceof NumberSetting) {
                components.add(new NumberSettingComponent(this, (NumberSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 5));
            } else if (setting instanceof ListSetting) {
                components.add(new ListSettingComponent(this, (ListSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 7));
            }
        }
        this.progress = 0.0f;
    }
    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
         components.sort(new SorterHelper());
         GlStateManager.pushMatrix();
         
         
        float x = getX();
        boolean a = ClickGuiScreen.progress >= 0.9f;
        float y = a ? ((getY() - 2)) : (- 1000);
        int width = getWidth();
        int height = getHeight();
       

  //     if (!ClickGuiScreen.search.getText().isEmpty() && !this.module.getLabel().toLowerCase().contains(ClickGuiScreen.search.getText().toLowerCase())) {
   //         return;
 //       }

        int color = 0;

        Color onecolor = new Color(ClickGUI.color.getColorValue());
        Color twocolor = new Color(ClickGUI.colorTwo.getColorValue());
        double speed = ClickGUI.speed.getNumberValue();
        switch (ClickGUI.clickGuiColor.currentMode) {
            case "Client":
                color = DrawHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = DrawHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Color Two":
                color = DrawHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = DrawHelper.astolfo(true, (int) y).getRGB();
                break;
            case "Rainbow":
                color = DrawHelper.rainbow(300, 1, 1).getRGB();
                break;
            case "Category":
                Panel panel = (Panel) parent;
                color = panel.type.getColor();
                break;

        }
        String mode = ClickGUI.style.getOptions();
        boolean hovered = mode.equalsIgnoreCase("Rockstar New") ? false : false; //isHovered(mouseX, mouseY);
        ScaledResolution sr = new ScaledResolution(mc);   
        if (mode.equalsIgnoreCase("Rockstar New")) {
        	GlStateManager.pushMatrix();
        	GlStateManager.enable(GL11.GL_SCISSOR_TEST);
        	DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
            DrawHelper.scissorRect(0, getY2() + 47, getX() + 99 * ClickGuiScreen.progress2, getY2() + 253);
        }

        
        if (module.isToggled()) {
      		  
      		DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
      		if (ClickGUI.glow.getBoolValue()) {
        		 // RenderUtils.drawBlurredShadow(x - ClientHelper.getFontRender().getStringWidth(getName()) / 2 + 50 + 2, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F - 4 - ClickGUI.fontY.getNumberValue() - (hovered ? 1 : 0) + 5, x - ClientHelper.getFontRender().getStringWidth(getName()) / 2 + 50 + ClientHelper.getFontRender().getStringWidth(getName()) - 2 - (x - ClientHelper.getFontRender().getStringWidth(getName()) / 2 + 50 + 2) + 15 - 2 - 12, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F + 11 - ClickGUI.fontY.getNumberValue() - (hovered ? 1.5f : 0) - (y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F - 4 - ClickGUI.fontY.getNumberValue() - (hovered ? 1 : 0)) - 10, 10, new Color(255,255,255,100));
        	  }
      		DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
      		
          	  //DrawHelper.drawGradientRect1(x + 50 + 2 - 50.5f, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F - 4, x + 50 - 2 + 48.5f, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F + 9, color, new Color(color).darker().getRGB());
        }
        
    	this.s1 = (float)Interpolator.LINEAR.interpolate((double)this.s1, module.isToggled() ? 0 : 1, 0.1);

        
        DrawHelper.drawNewRect(x, y, x, y, new Color(30, 30, 30, 255).getRGB());
        if (hovered) {
            DrawHelper.drawNewRect(33 + 40, sr.getScaledHeight() - 23, 45 + mc.mntsb_20.getStringWidth(getName() + " - " +  module.getDesc())+ 16 + 20, sr.getScaledHeight() - 7, new Color(30, 30, 30, 200).getRGB());
            DrawHelper.drawGradientRect(51 + 20, sr.getScaledHeight() - 23, 53 + 20, sr.getScaledHeight() - 7, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 70);
            DrawHelper.drawGradientRect(51 + mc.mntsb_20.getStringWidth(getName() + " - " +  module.getDesc())+ 10 + 20, sr.getScaledHeight() - 23, 53 + mc.mntsb_20.getStringWidth(getName() + " - " +  module.getDesc())+ 10 + 20, sr.getScaledHeight() - 7, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 70);
            mc.mntsb_20.drawStringWithShadow(TextFormatting.GREEN + getName() + TextFormatting.RESET + " - " +  TextFormatting.GRAY + module.getDesc(), 56 + 20, sr.getScaledHeight() - 19, -1);
        }
        
        if (mode.equalsIgnoreCase("Rockstar") || mode.equalsIgnoreCase("Rockstar New")) {
        if (isExpanded()) {
        	DrawHelper.drawRect(x, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F - 4 + 13, x, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F + 9 + getHeightWithExpand() - 7, new Color(0,0,0, 55).getRGB());

        	DrawHelper.drawRect(x + 50 + 2 - 50.5f - 3, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F - 4 + 13, x + 3 + 50 - 2 + 48.5f, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F + 9 + getHeightWithExpand() - 7, new Color(0,0,0, 55).getRGB());
        	DrawHelper.drawGradientRect(x + 50 + 2 - 50.5f - 3, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F - 4 + 13, x + 3 + 50 - 2 + 48.5f, y + 10 + ClientHelper.getFontRender().getStringHeight(getName()) / 2F + 9, new Color(0,0,0, 55).getRGB(), new Color(0,0,0, 0).getRGB());
        	DrawHelper.drawGradientRect(x + 50 + 2 - 50.5f - 3, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F + 9 + getHeightWithExpand() - 7 - 7, x + 3 + 50 - 2 + 48.5f, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F + 9 + getHeightWithExpand() - 7, new Color(0,0,0, 0).getRGB(), new Color(0,0,0, 55).getRGB());
        }
        else {
        	if (isExpanded()) {
            	DrawHelper.drawRect(x + 50 + 2 - 50.5f, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F - 4 + 13, x + 50 - 2 + 48.5f, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F + 9 + getHeightWithExpand() - 7, new Color(0,0,0, 55).getRGB());
            	DrawHelper.drawGradientRect(x + 50 + 2 - 50.5f, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F - 4 + 13, x + 50 - 2 + 48.5f, y + 10 + ClientHelper.getFontRender().getStringHeight(getName()) / 2F + 9, new Color(0,0,0, 55).getRGB(), new Color(0,0,0, 0).getRGB());
            	DrawHelper.drawGradientRect(x + 50 + 2 - 50.5f, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F + 9 + getHeightWithExpand() - 7 - 7, x + 50 - 2 + 48.5f, y + ClientHelper.getFontRender().getStringHeight(getName()) / 2F + 9 + getHeightWithExpand() - 7, new Color(0,0,0, 0).getRGB(), new Color(0,0,0, 55).getRGB());
            }
        }
        
        Color l1 = ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor.brighter() : new Color(color).brighter();
        
        Color l2 = ClickGUI.clickGuiColor.currentMode.equalsIgnoreCase("Static") ? onecolor : new Color(color);

    	RoundedUtil.drawRound(x, y + height / 2F - 10 + 3, 98 * (ClickGuiScreen.progress2), getHeightWithExpand() - (isExpanded() ? 0 : 4), 3,  new Color(60,60,60, (int) (255 * ClickGuiScreen.progress2)));
		
    	RoundedUtil.drawGradientRound(x + 2, y + height / 2F - 8 + 3, 11, 11, 3, new Color(40,40,40), new Color(40,40,40), new Color(40,40,40), new Color(40,40,40));
		
    	if (ClickGUI.glow.getBoolValue()) {
        	RenderUtils.drawBlurredShadow(x + 2 + 5.f * s1, y + height / 2F - 8 + 5.f * s1 + 3, 11 - 5.f * (s1*2), 11 - 5.f * (s1*2), 10, l1);
    	}
    	
    	RoundedUtil.drawGradientRound(x + 2 + 5.f * s1, y + height / 2F - 8 + 5.f * s1 + 3, 11 - 5.f * (s1*2), 11 - 5.f * (s1*2), 3, l2, l1, l1, l2);
    		
    	
      components.sort(new SorterHelper());
  	  mc.mntsb_15.drawString(binding ? "Press a key.. " + Keyboard.getKeyName(module.getKey()) : getName(), x + 17, y + height / 2F - 3 - 1 + 3, -1);
  	  
  	  DrawHelper.drawGradientRect1(x - 30 + 50 * ClickGuiScreen.progress2, y + height / 2F - 3 - 1, x - 50 + 50 * ClickGuiScreen.progress2, y + height / 2F - 3 - 1 + 3 + 7, new Color(60,60,60, 100).getRGB(), new Color(60,60,60).getRGB());
  	  
  	 if (isExpanded()) {
     	
         int childY = Panel.ITEM_HEIGHT;
         for (Component child : components) {
             int cHeight = child.getHeight();
             if (child instanceof BooleanSettingComponent) {
                 BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent) child;
                 if (!booleanSettingComponent.booleanSetting.isVisible()) {
                     continue;
                 }
             }
             if (child instanceof NumberSettingComponent) {
                 NumberSettingComponent numberSettingComponent = (NumberSettingComponent) child;
                 if (!numberSettingComponent.numberSetting.isVisible()) {
                     continue;
                 }
             }
             if (child instanceof ColorPickerComponent) {
                 ColorPickerComponent colorPickerComponent = (ColorPickerComponent) child;
                 if (!colorPickerComponent.getSetting().isVisible()) {
                     continue;
                 }
             }
             if (child instanceof ListSettingComponent) {
                 ListSettingComponent listSettingComponent = (ListSettingComponent) child;
                 if (!listSettingComponent.getSetting().isVisible()) {
                     continue;
                 }
             }
             if (child instanceof ExpandableComponent) {
                 ExpandableComponent expandableComponent = (ExpandableComponent) child;
                 if (expandableComponent.isExpanded())
                     cHeight = expandableComponent.getHeightWithExpand();
             }
             child.setY(childY);
             child.drawComponent(scaledResolution, mouseX, mouseY);
             childY += cHeight;
         }
     } else {
     	this.lastMS = System.currentTimeMillis();
     	this.progress = 0;
     }
  	 
  	  if (mode.equalsIgnoreCase("Rockstar New")) {
  		
  		GlStateManager.disable(GL11.GL_SCISSOR_TEST);
  		GlStateManager.popMatrix();
  	  }
  	  
  	  
  	
  	  GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean canExpand() {
        return !components.isEmpty();
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
    	if (ClickGUI.style.currentMode.equalsIgnoreCase("Rockstar New")) {
    		if (mouseY > 47 && mouseY <  253) {
        		switch (button) {
                case 0:
                    module.toggle();
                    break;
                case 2:
                    binding = !binding;
                    break;
        		}
        	}
    	} else {
    		switch (button) {
            case 0:
                module.toggle();
                break;
            case 2:
                binding = !binding;
                break;
    		}
    	}
    }

    @Override
    public void onKeyPress(int keyCode) {
        if (binding) {
            ClickGuiScreen.escapeKeyInUse = true;
            module.setKey(keyCode == Keyboard.KEY_ESCAPE ? Keyboard.KEY_NONE : keyCode);
            binding = false;
        }
    }

    @Override
    public int getHeightWithExpand() {
    	height = getHeight();
        this.progress = (float)Interpolator.LINEAR.interpolate((double)this.progress, isExpanded() ? 1 : 0, 0.01 * ClickGUI.animateSpeed.getNumberValue());

        if (isExpanded()) {
            for (Component child : components) {
                int cHeight = child.getHeight();
                if (child instanceof BooleanSettingComponent) {
                    BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent) child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof NumberSettingComponent) {
                    NumberSettingComponent numberSettingComponent = (NumberSettingComponent) child;
                    if (!numberSettingComponent.numberSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ColorPickerComponent) {
                    ColorPickerComponent colorPickerComponent = (ColorPickerComponent) child;
                    if (!colorPickerComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ListSettingComponent) {
                    ListSettingComponent listSettingComponent = (ListSettingComponent) child;
                    if (!listSettingComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) child;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand();
                }
                    height += cHeight * progress;
                
            }
        }
        return height;
    }

}
