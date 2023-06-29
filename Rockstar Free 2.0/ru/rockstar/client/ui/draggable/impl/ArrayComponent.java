package ru.rockstar.client.ui.draggable.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;
import ru.rockstar.Main;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.Translate;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.display.ClientFont;
import ru.rockstar.client.features.impl.display.DamageFlyIndicator;
import ru.rockstar.client.features.impl.movement.DamageFly;
import ru.rockstar.client.ui.draggable.DraggableModule;

public class ArrayComponent extends DraggableModule {
	public static int x2;
	public static int y2;
    public ArrayComponent() {
        super("ArrayComponent", sr.getScaledWidth() - 300, sr.getScaledHeight() - 200);
    }
    public static int x;
    public static int y;

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public void render(int mouseX, int mouseY) {
    	x = getX();
    	y = getY();
    	 ScaledResolution sr = new ScaledResolution(this.mc);
         float yDist = 1;
         int yTotal = 0;
         for (int i = 0; i < Main.instance.featureDirector.getFeatureList().size(); ++i) {
             yTotal += ClientHelper.getFontRender().getFontHeight() + 3;
         }

        super.render(mouseX, mouseY);
    }
    
    private static Feature getNextEnabledModule(ArrayList<Feature> features, int startingIndex) {
        for (int i = startingIndex; i < features.size(); i++) {
            Feature feature = features.get(i);
            if (feature.isToggled()) {
                if (!feature.getModuleName().equals("ClickGui")) {
                    return feature;
                }
            }
        }
        return null;
    }

    @Override
    public void draw() {
    	
    	
        super.draw();
    }
}