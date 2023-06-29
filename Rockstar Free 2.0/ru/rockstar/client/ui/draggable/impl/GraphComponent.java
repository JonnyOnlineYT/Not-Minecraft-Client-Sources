/*
package ru.rockstar.client.ui.draggable.impl;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import ru.rockstar.Main;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.Counter;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.features.impl.display.DamageFlyIndicator;
import ru.rockstar.client.features.impl.display.Keystrokes;
import ru.rockstar.client.features.impl.display.MotionGraph;
import ru.rockstar.client.features.impl.display.TimerIndicator;
import ru.rockstar.client.features.impl.display.MotionGraph.MovementNode;
import ru.rockstar.client.features.impl.movement.TargetStrafe;
import ru.rockstar.client.features.impl.movement.Timer;
import ru.rockstar.client.features.impl.visuals.ArmorHUD;
import ru.rockstar.client.ui.draggable.DraggableModule;
import ru.rockstar.client.ui.settings.impl.NumberSetting;
public class GraphComponent extends DraggableModule {
	int x2,y2;
    public GraphComponent() {
        super("GraphComponent", sr.getScaledWidth() - 100, sr.getScaledHeight() - 20);
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
    Counter counter;
    public List<MovementNode> movementNodes;
    
    public float getAverageHeight() {
        float n = 0.0f;
        for (int i = this.movementNodes.size() - 1; i > 0; --i) {
            final MovementNode movementNode = this.movementNodes.get(i);
            if (this.movementNodes.size() > (0xAD ^ 0xA6) && movementNode != null && i > this.movementNodes.size() - (0x6 ^ 0xC)) {
                n += movementNode.speed;
            }
        }
        return n / 10.0f;
    }
    
    public static double map(double n, final double n2, final double n3, final double n4, final double n5) {
        n = (n - n2) / (n3 - n2);
        return n4 + n * (n5 - n4);
    }
    

    @Override
    public void render(int mouseX, int mouseY) {
    	if (!Main.instance.featureDirector.getFeatureByClass(MotionGraph.class).isToggled())
    		return;
    	this.counter = new Counter();
        final double n4 = 180.0;
        this.x = getX();
        this.y = getY();
        final double n5 = this.x;
        final double n6 = this.y;
        final double n7 = 20.0;
        if (this.mc.player != null && this.mc.world != null) {
            final DecimalFormat decimalFormat = new DecimalFormat("###.##");
            if (this.movementNodes.size() > n4 / 2.0) {
                this.movementNodes.clear();
            }
            if (this.counter.hasReached(15.0f)) {
                if (this.movementNodes.size() > n4 / 2.0 - 1.0) {
                    this.movementNodes.remove(0);
                }
                this.movementNodes.add(new MovementNode((float)MovementHelper.getSpeed()));
                this.counter.reset();
            }
            GL11.glPushMatrix();
            GL11.glAlphaFunc(197 + 17 + 153 + 149, 0.01f);
            RenderUtils.drawBlurredShadow((float)n5, (float)n6, 180.0f, 20.0f, 0x5F ^ 0x55, new Color(1, 1, 1, 0x77 ^ 0x13));
            RoundedUtil.drawRound((int)n5, (float)n6, (int)n4, (float)n7, 5.0f, new Color(0, 0, 0, 0xFA ^ 0xBC));
            GL11.glEnable(974 + 2143 - 1109 + 1081);
            MovementNode movementNode = null;
            for (int i = 0; i < this.movementNodes.size(); ++i) {
                final MovementNode movementNode2 = this.movementNodes.get(i);
                final float mappedX = (float)map(n4 / 2.0 - 1.0 - i, 0.0, n4 / 2.0 - 1.0, n5 + n4 - 1.0, n5 + 1.0);
                final float mappedY = (float)((float)map(movementNode2.speed, -2.0, this.getAverageHeight(), n6 + n7 - 1.0, n6 + 1.0) + n7 / 2.0);
                movementNode2.mappedX = mappedX;
                movementNode2.mappedY = mappedY;
                if (movementNode != null) {
                    final Color color = ClientHelper.getClientColor();
                    RenderUtils.drawBlurredShadow(movementNode2.mappedX - 3.0f, movementNode2.mappedY - 2.5f, 5.0f, 5.0f, 3, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0x2D ^ 0x1F));
                    RenderUtils.drawHLine(movementNode2.mappedX, movementNode2.mappedY, movementNode.mappedX, movementNode.mappedY, 2.0f, new Color(color.getRed(), color.getGreen(), color.getBlue(), 63 + 131 - 183 + 239).getRGB());
                }
                final float n8 = 0.0f;
                final float n9 = 0.0f;
                if (n8 >= movementNode2.mappedX && n8 <= movementNode2.mappedX + movementNode2.size && n9 >= n6 && n9 <= n6 + n7) {
                    final Color color2 = ClientHelper.getClientColor();
                    RenderUtils.drawRect(movementNode2.mappedX - movementNode2.size, n6, movementNode2.mappedX + movementNode2.size, n6 + n7, new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), 66 + 179 - 233 + 238).getRGB());
                    RenderUtils.drawRect(movementNode2.mappedX - movementNode2.size, movementNode2.mappedY - movementNode2.size, movementNode2.mappedX + movementNode2.size, movementNode2.mappedY + movementNode2.size, new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), 35 + 248 - 278 + 245).getRGB());
                    String.format("Speed: %s", decimalFormat.format(movementNode2.speed));
                }
                movementNode = movementNode2;
            }
            GL11.glDisable(632 + 2208 - 652 + 901);
            final String string = decimalFormat.format(this.movementNodes.get(this.movementNodes.size() - 1).speed) + "bps";
            mc.mntsb.drawString(string, (float)(n5 + n4 - this.mc.mntsb.getStringWidth(string)), (float)(n6 + n7 + 3.0), new Color(-1).getRGB());
            GL11.glPopMatrix();
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
    	if (!TimerIndicator.a) {
    		return;
    	}
    	GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        x2 = (int) getX();
        x = getX();
        y = getY();
    	y2 = (int) getY();
    	GlStateManager.pushMatrix();
    	GlStateManager.enableAlpha();
    	DrawHelper.drawRect(x2 - 1, y2, x2 + 46, y2 + 45,new Color(30,30,30,255).getRGB());
    	DrawHelper.drawGradientRect1(x2 - 3f, y2 - 2.3f, x2 + 48, y2 + 10, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
    	mc.mntsb_19.drawStringWithShadow("TIMER",x2 + 5,y2 + 1,-1);
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400, 11, 8, false, new Color(50,50,50,150));
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,400 - (float) (TimerIndicator.indicatorTimer * (400 / Timer.ticks)),400 - (float) (TimerIndicator.indicatorTimer * (400 / Timer.ticks)) + 10,11, 8, false, new Color(-1));
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400 - (float) (TimerIndicator.indicatorTimer * (400 / Timer.ticks)), 11, 8, false, ClientHelper.getClientColor());
    	GlStateManager.popMatrix();
    	GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        super.draw();
    }
    
    public static class MovementNode
    {
        public float mappedY;
        public float speed;
        public float mappedX;
        public float size;
        public Color color;
        
        public MovementNode(final float speed) {
            this.size = 0.5f;
            this.speed = 0.0f;
            this.speed = speed;
            this.color = new Color(43 + 166 - 46 + 92, 185 + 196 - 180 + 54, 209 + 249 - 395 + 192);
        }
    }
}*/