package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.Event;
import org.moonware.client.event.events.impl.motion.EventMove;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.visual.anim.CastHelper;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.settings.impl.BooleanSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JumpCircle extends Feature {
    static List<Circle> circles = new ArrayList<>();
    public static BooleanSetting Players = new BooleanSetting("Players",true);
    public static BooleanSetting Mobs = new BooleanSetting("Mobs",true);
    public static BooleanSetting Animals = new BooleanSetting("Animals",true);
    public static BooleanSetting Villagers= new BooleanSetting("Villagers",true);
    public static BooleanSetting Invisibles = new BooleanSetting("Invisibles",true);
    public JumpCircle() {
        super("JumpCircleZALUPA", "отрисовывает вашь прыжок под вами", Type.Visuals);
    }

    @EventTarget
    public void onRender3D(EventRender3D event ) {
        Minecraft.gameRenderer.setupCameraTransform(Helper.mc.getRenderPartialTicks(), 2);
        for (EntityPlayer player : Minecraft.world.playerEntities) {
            CastHelper castHelper = new CastHelper();
            castHelper.apply(CastHelper.EntityType.PLAYERS);
            castHelper.apply(CastHelper.EntityType.FRIENDS);
            castHelper.apply(CastHelper.EntityType.SELF);
            CastHelper.EntityType type;
            if (player != null) {
                boolean jumpcircles = true;
                Color color = ColorUtil.skyRainbow(2,4);
                if (jumpcircles) {
                    GL11.glPushMatrix();
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glShadeModel(GL11.GL_SMOOTH);

                    for (Circle circle : circles) {
                        GL11.glBegin(GL11.GL_QUAD_STRIP);
                        for (int i = 0; i <= 360; i += 5) {
                            float red = color.getRed() / 255f, green = color.getGreen() / 255f, blue = color.getBlue() / 255f;
                            if (jumpcircles) {
                                int rainbow = PaletteHelper.rainbow(i / 360F);
                                red = ((rainbow >> 16) & 255) / 255F;
                                green = ((rainbow >> 8) & 255) / 255F;
                                blue = (rainbow & 255) / 255F;
                            }
                            Vec3d pos = circle.pos();
                            double x = Math.cos(Math.toRadians(i)) * MoonWare.createAnimation((1 - circle.getAnimation(Helper.mc.getRenderPartialTicks()))) * 0.6;
                            double z = Math.sin(Math.toRadians(i)) * MoonWare.createAnimation((1 - circle.getAnimation(Helper.mc.getRenderPartialTicks()))) * 0.6;
                            GL11.glColor4d(red, green, blue, (color.getAlpha() / 255f) * circle.getAnimation(Helper.mc.getRenderPartialTicks()));
                            GL11.glVertex3d(pos.xCoord + x, pos.yCoord + 0.2f, pos.zCoord + z);
                            GL11.glColor4d(red, green, blue, (color.getAlpha() / 255f) * 0.2 * circle.getAnimation(Helper.mc.getRenderPartialTicks()));
                            GL11.glVertex3d(pos.xCoord + x * 1.4, pos.yCoord + 0.2f, pos.zCoord + z * 1.4);
                        }
                        GL11.glEnd();
                    }
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                    GL11.glShadeModel(GL11.GL_FLAT);
                    GL11.glEnable(GL11.GL_CULL_FACE);
                    GL11.glPopMatrix();
                    System.out.println("Залупа");
                }
            }

        }
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (getState())  {
            for (EntityPlayer player : Minecraft.world.playerEntities) {
                //System.out.println("Залупа");
                //circles.removeIf(Circle::update);
            }
        }
    }
    @EventTarget
    public void onEvent(Event event) {
        if (event instanceof EventRender3D) {
            Minecraft.gameRenderer.setupCameraTransform(Helper.mc.getRenderPartialTicks(), 2);
            for (EntityPlayer player : Minecraft.world.playerEntities) {
                CastHelper castHelper = new CastHelper();
                castHelper.apply(CastHelper.EntityType.PLAYERS);
                castHelper.apply(CastHelper.EntityType.FRIENDS);
                castHelper.apply(CastHelper.EntityType.SELF);
                CastHelper.EntityType type;
                if ((type = CastHelper.isInstanceof(player, castHelper.build())) != null) {
                    boolean jumpcircles = true;
                    Color color = ColorUtil.skyRainbow(2,4);
                    if (jumpcircles) {
                        GL11.glPushMatrix();
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glDisable(GL11.GL_ALPHA_TEST);
                        GL11.glDisable(GL11.GL_CULL_FACE);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glShadeModel(GL11.GL_SMOOTH);

                        for (Circle circle : circles) {
                            GL11.glBegin(GL11.GL_QUAD_STRIP);
                            for (int i = 0; i <= 360; i += 5) {
                                float red = color.getRed() / 255f, green = color.getGreen() / 255f, blue = color.getBlue() / 255f;
                                if (jumpcircles) {
                                    int rainbow = PaletteHelper.rainbow(i / 360F);
                                    red = ((rainbow >> 16) & 255) / 255F;
                                    green = ((rainbow >> 8) & 255) / 255F;
                                    blue = (rainbow & 255) / 255F;
                                }
                                Vec3d pos = circle.pos();
                                double x = Math.cos(Math.toRadians(i)) * MoonWare.createAnimation((1 - circle.getAnimation(Helper.mc.getRenderPartialTicks()))) * 0.6;
                                double z = Math.sin(Math.toRadians(i)) * MoonWare.createAnimation((1 - circle.getAnimation(Helper.mc.getRenderPartialTicks()))) * 0.6;
                                GL11.glColor4d(red, green, blue, (color.getAlpha() / 255f) * circle.getAnimation(Helper.mc.getRenderPartialTicks()));
                                GL11.glVertex3d(pos.xCoord + x, pos.yCoord + 0.2f, pos.zCoord + z);
                                GL11.glColor4d(red, green, blue, (color.getAlpha() / 255f) * 0.2 * circle.getAnimation(Helper.mc.getRenderPartialTicks()));
                                GL11.glVertex3d(pos.xCoord + x * 1.4, pos.yCoord + 0.2f, pos.zCoord + z * 1.4);
                            }
                            GL11.glEnd();
                        }
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glShadeModel(GL11.GL_FLAT);
                        GL11.glEnable(GL11.GL_CULL_FACE);
                        GL11.glPopMatrix();
                    }
                }
            }
            System.out.println("Залупка");
        }
        if (event instanceof EventUpdate) {
            for (EntityPlayer player : Minecraft.world.playerEntities) {
                circles.removeIf(Circle::update);
            }
        }
        if (event instanceof EventMove) {
            EventMove eem = (EventMove) event;
//            if (eem.get instanceof EntityPlayer) {
//                EntityPlayer player = (EntityPlayer) eem.ctx();
//                double motionY = eem.ctx().posY - eem.from().yCoord;
//                if (!player.onGround && player.onGround != player.raycastGround && motionY > 0) {
//                    player.circles.add(new Circle(eem.from()));
//                }
//                player.raycastGround = player.onGround;
//            }
        }
    }

    public static class Circle {
        private Vec3d vector;
        private int tick, prevTick;

        public Circle(Vec3d vector) {
            this.vector = vector;
            tick = 20;
            prevTick = tick;
        }

        public double getAnimation(float pt) {
            return (prevTick + (tick - prevTick) * pt) / 20F;
        }

        public boolean update() {
            prevTick = tick;
            return tick-- <= 0;
        }

        public Vec3d pos() {
            return new Vec3d(vector.xCoord - RenderManager.renderPosX, vector.yCoord - RenderManager.renderPosY, vector.zCoord - RenderManager.renderPosZ);
        }
    }
}
