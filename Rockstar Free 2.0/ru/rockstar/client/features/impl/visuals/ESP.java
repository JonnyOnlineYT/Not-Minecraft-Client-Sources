package ru.rockstar.client.features.impl.visuals;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.api.event.event.EventNameTags;
import ru.rockstar.api.utils.font.FontRenderer;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.shader.FramebufferShader;
import ru.rockstar.api.utils.shader.shaders.EntityGlowShader;
import ru.rockstar.api.utils.shader.shaders.FlowShader;
import ru.rockstar.api.utils.shader.shaders.OutlineShader;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.espgble.impl.EspComponent;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import static ru.rockstar.api.utils.Helper.sr;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class ESP extends Feature {

    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
    private final int backgroundColor = (new Color(0, 0, 0, 120)).getRGB();
    private final int black = Color.BLACK.getRGB();
    public FramebufferShader framebuffer = null;

    public BooleanSetting showArmor;
    public BooleanSetting name;
    public BooleanSetting health;

    boolean nameTags;
    public static ColorSetting color = new ColorSetting("Color", new Color(0xFFFFFF).getRGB(), () -> true);
    public static ColorSetting colorfriend = new ColorSetting("FriendColor", new Color(0x00FFFF).getRGB(), () -> true);

    public ESP() {
        super("ESP","Показывает игроков, ник и их здоровье сквозь стены", 0,Category.VISUALS);
        ArrayList<String> options = new ArrayList<>();
        showArmor = new BooleanSetting("Show Armor", false, () -> true);
        name = new BooleanSetting("Name", false, () -> true);
        health = new BooleanSetting("Health", false, () -> true);
        addSettings(showArmor,name,health,color,colorfriend);


    }



    @EventTarget
    public void onRender2D(Event2D event) {
        GL11.glPushMatrix();
        float partialTicks = mc.timer.renderPartialTicks;
        int scaleFactor = ScaledResolution.getScaleFactor();
        double scaling = scaleFactor / Math.pow(scaleFactor, 2.0D);
        GL11.glScaled(scaling, scaling, scaling);
        int black = this.black;
        float scale = 1F;
        float upscale = 1.0F / scale;
        RenderManager renderMng = mc.getRenderManager();
        EntityRenderer entityRenderer = mc.entityRenderer;

        for (Entity entity : mc.world.loadedEntityList) {
            if (isValid(entity) && DrawHelper.isInViewFrustrum(entity)) {
                double x = DrawHelper.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
                double y = DrawHelper.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
                double z = DrawHelper.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
                double width = entity.width / 1.5D;
                double height = entity.height + ((entity.isSneaking() || (entity == mc.player && mc.player.isSneaking()) ? -0.3D : 0.2D));
                AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                Vector3d[] vectors = new Vector3d[]{new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ),
                        new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
                entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
                    if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
                        if (position == null)
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0D);
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                if (position != null && entity != mc.player) {
                    entityRenderer.setupOverlayRendering();
                    double posX;
                    double posY;
                    double endPosX;
                    double endPosY; 
                    posX = position.x;
                    posY = position.y;
                    endPosX = position.z;
                    endPosY = position.w;

                        //left

                    DrawHelper.drawRect(posX - 1, posY, posX - 0.5, endPosY- 0.5, color.getColorValue());

                    //Button
                    DrawHelper.drawRect(posX, endPosY - 1, endPosX- 0.5, endPosY- 0.5, color.getColorValue());

                    //Top
                    DrawHelper.drawRect(posX - 1, posY, endPosX- 0.5, posY - 0.5, color.getColorValue());

                    //Right
                    DrawHelper.drawRect(endPosX - 1, posY, endPosX- 0.5, endPosY- 0.5, color.getColorValue());
               
                    
                    boolean living = entity instanceof EntityLivingBase;

                    int healthColor2;
                    EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                    float hp2 = entityLivingBase.getHealth();
                    float maxHealth = entityLivingBase.getMaxHealth();
                    double hpPercentage = (hp2 / maxHealth);
                    double hpHeight2 = (endPosY - posY) * hpPercentage;
                    if (hp2 <= 4) {
                        healthColor2 = new Color(200, 0, 0).getRGB();
                    } else if (hp2 <= 8) {
                        healthColor2 = new Color(231, 143, 85).getRGB();
                    } else if (hp2 <= 12) {
                        healthColor2 = new Color(219, 201, 106).getRGB();
                    } else if (hp2 <= 16) {
                        healthColor2 = new Color(117, 231, 85).getRGB();
                    } else {
                        healthColor2 = new Color(44, 186, 19).getRGB();
                    }

                    if (entityLivingBase != null && hp2 > 0) {

                        if (living && health.getBoolValue()) {
                            MathHelper.clamp(hp2, 0, 20);
                            DrawHelper.drawRect(posX - 4.5, posY - 0.5, posX - 2.5, endPosY + 0.5, new Color(0, 0, 0, 125).getRGB());

                            DrawHelper.drawRect(posX - 4, endPosY, posX - 3, endPosY - hpHeight2,healthColor2);

                        }
                        if (living && name.getBoolValue()) {
                            if (!(Main.instance.featureDirector.getFeatureByClass(NameTags.class).isToggled())) {
                                float scaledHeight = 20.0F;
                                String name = entity.getName();
                                if (Main.instance.featureDirector.getFeatureByClass(NameProtect.class).isToggled() ) {
                                    name = "Protected";
                                }
                                double dif = (endPosX - posX) / 2.0D;
                                double textWidth = (mc.fontRendererObj.getStringWidth(name + " §7" + (int) mc.player.getDistanceToEntity(entity) + "m") * scale);
                                float tagX = (float) ((posX + dif - textWidth / 2.0D) * upscale);
                                float tagY = (float) (posY * upscale) - scaledHeight;
                                GL11.glPushMatrix();
                                GlStateManager.scale(scale, scale, scale);
                                mc.fontRendererObj.drawStringWithShadow(name, tagX, tagY, Color.WHITE.getRGB());
                                GL11.glPopMatrix();
                            }
                        }

                        if (living && showArmor.getBoolValue()) {
                            if (entity instanceof EntityPlayer) {
                                EntityPlayer player = (EntityPlayer) entity;
                                double ydiff = (endPosY - posY) / 4;

                                ItemStack stack = (player).getEquipmentInSlot(4);
                                if (mc.player.getDistanceToEntity(player) <= 15) {
                                    if (stack != null) {
                                        double diff1 = (posY + ydiff - 1) - (posY + 2);
                                        double percent = 1 - (double) stack.getItemDamage() / (double) stack.getMaxDamage();
                                        DrawHelper.renderItem(stack, (int) endPosX + 4, (int) posY + (int) ydiff - 1 - (int) (diff1 / 2) - 18);
                                        //mc.smallfontRenderer.drawStringWithShadow(stackname, (float) endPosX + 5, (float) (posY + ydiff - 1 - (diff1 / 2)) - (mc.clickguismall.getStringHeight(stack.getMaxDamage() - stack.getItemDamage() + "") / 2), color);
                                    }
                                    ItemStack stack2 = (player).getEquipmentInSlot(3);
                                    if (stack2 != null) {
                                        double diff1 = (posY + ydiff * 2) - (posY + ydiff + 2);
                                        String stackname = (stack.getDisplayName().equalsIgnoreCase("Air")) ? "0" : !(stack2.getItem() instanceof ItemArmor) ? stack2.getDisplayName() : stack2.getMaxDamage() - stack2.getItemDamage() + "";
                                        if (mc.player.getDistanceToEntity(player) < 10) {
                                            DrawHelper.renderItem(stack2, (int) endPosX + 4, (int) (posY + ydiff * 2) - (int) (diff1 / 2) - 18);
                                        }
                                    }
                                    ItemStack stack3 = (player).getEquipmentInSlot(2);
                                    if (stack3 != null) {

                                        double diff1 = (posY + ydiff * 3) - (posY + ydiff * 2 + 2);
                                        if (mc.player.getDistanceToEntity(player) < 10) {
                                            DrawHelper.renderItem(stack3, (int) endPosX + 4, (int) (posY + ydiff * 3) - (int) (diff1 / 2) - 18);
                                        }
                                    }
                                    ItemStack stack4 = (player).getEquipmentInSlot(1);
                                    double diff1 = (posY + ydiff * 4) - (posY + ydiff * 3 + 2);
                                    if (mc.player.getDistanceToEntity(player) < 10) {
                                        DrawHelper.renderItem(stack4, (int) endPosX + 4, (int) (posY + ydiff * 4) - (int) (diff1 / 2) - 18);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.enableBlend();
        entityRenderer.setupOverlayRendering();
    }

    @EventTarget
    public void onRenderName(EventNameTags eventRenderName) {
        if (!isToggled())
            return;
        eventRenderName.setCancelled(name.getBoolValue());
    }

    private boolean isValid(Entity entity) {
        if (mc.gameSettings.thirdPersonView == 0 && entity == mc.player)
            return false;
        if (entity.isDead)
            return false;
        if ((entity instanceof net.minecraft.entity.passive.EntityAnimal))
            return false;
        if ((entity instanceof EntityPlayer))
            return true;
        if ((entity instanceof EntityArmorStand))
            return false;
        if ((entity instanceof IAnimals))
            return false;
        if ((entity instanceof EntityItemFrame))
            return false;
        if ((entity instanceof EntityArrow || entity instanceof EntitySpectralArrow))
            return false;
        if ((entity instanceof EntityMinecart))
            return false;
        if ((entity instanceof EntityBoat))
            return false;
        if ((entity instanceof EntityDragonFireball))
            return false;
        if ((entity instanceof EntityXPOrb))
            return false;
        if ((entity instanceof EntityMinecartChest))
            return false;
        if ((entity instanceof EntityTNTPrimed))
            return false;
        if ((entity instanceof EntityMinecartTNT))
            return false;
        if ((entity instanceof EntityVillager))
            return false;
        if ((entity instanceof EntityExpBottle))
            return false;
        if ((entity instanceof EntityLightningBolt))
            return false;
        if ((entity instanceof EntityPotion))
            return false;
        if ((entity instanceof Entity))
            return false;
        if (((entity instanceof net.minecraft.entity.monster.EntityMob || entity instanceof net.minecraft.entity.monster.EntitySlime || entity instanceof net.minecraft.entity.boss.EntityDragon
                || entity instanceof net.minecraft.entity.monster.EntityGolem)))
            return false;
        return entity != mc.player;
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, this.modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, this.projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, this.viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.vector))
            return new Vector3d((this.vector.get(0) / scaleFactor), ((Display.getHeight() - this.vector.get(1)) / scaleFactor), this.vector.get(2));
        return null;
    }
}
