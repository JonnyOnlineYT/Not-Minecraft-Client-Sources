package splash.client.modules.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.omg.CosNaming._BindingIteratorImplBase;

import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.NumberValue;
import splash.client.events.render.EntityRenderEvent;
import splash.client.events.render.EventRenderPlayerName;
import splash.client.modules.combat.AntiBot;
import splash.utilities.math.MathUtils;
import splash.utilities.player.PlayerUtils;
import splash.utilities.visual.RenderUtilities;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Author: Ice
 * Created: 22:33, 13-Jun-20
 * Project: Client
 */
public class Nametags extends Module {

    private DecimalFormat decimalFormat = new DecimalFormat("#.#");
    float xSpeed = 133f / (mc.debugFPS * 1.05f);
    public float animated = 20;
    private NumberValue<Integer> opacity = new NumberValue<Integer>("Opacity", 70, 0, 255, this);
    public Nametags() {
        super("Nametags", "Renders tags above players head.", ModuleCategory.VISUALS);
    }

    @Collect
    public void cancelNormalTags(EventRenderPlayerName event) {
    	event.setCancelled(true);
    }
    
    @Collect
    public void on3D(EntityRenderEvent entityRenderEvent) {
        if (!mc.isGuiEnabled() || Splash.INSTANCE.getModuleManager().getModuleByClass(Freecam.class).isModuleActive())
            return;
        for (Object obj : mc.theWorld.playerEntities) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) obj;
                if (!isRenderingPossible(player))
                    continue;
                float partialTicks = entityRenderEvent.getPartialTicks();
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks - mc.getRenderManager().renderPosX;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks - mc.getRenderManager().renderPosY;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks - mc.getRenderManager().renderPosZ;
                drawNametags(player, x, y, z);
            }
        }
    }

    public void drawNametags(EntityLivingBase entity, double x, double y, double z) {

        String entityName = entity.getDisplayName().getUnformattedText();
        if (entity == mc.thePlayer)
            return;
        if (AntiBot.bots.contains(entity)) return;
        if (entity.isDead && !mc.getCurrentServerData().serverIP.contains("mineplex"))
            return;
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying)
            entityName = "" + entityName;
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)
            entityName = "\0248[C] " + entityName;
        if (entity.getDistanceToEntity((Entity) mc.thePlayer) >= 64.0F)
            entityName = "" + entityName;

        double health = entity.getHealth() / 2;
        double maxHealth = entity.getMaxHealth() / 2;
        double percentage = 100 * (health / maxHealth);
        String healthColor;


        if (percentage > 75) healthColor = "a";
        else if (percentage > 50) healthColor = "e";
        else if (percentage > 25) healthColor = "4";
        else healthColor = "4";

        String healthDisplay = decimalFormat.format(Math.floor((health + (double) 0.5F / 2) / 0.5F) * 0.5F);
        String healthDisplay1 = decimalFormat.format(Math.floor(((double) 0.5F / 2) / 0.5F) * 0.5F);


        String maxHealthDisplay = decimalFormat.format(Math.floor((entity.getMaxHealth() + (double) 0.5F / 2) / 0.5F) * 0.5F);
        if (!mc.getCurrentServerData().serverIP.contains("mineplex"))
            entityName = String.format(" %s ", entityName);
        if (Splash.getInstance().getFriendManager().isFriend(entity.getName()))
            entityName = String.format("[F]%s ", entityName);

        float distance = mc.thePlayer.getDistanceToEntity(entity);
        float var13 = (distance / 5 <= 2 ? 2.0F : distance / 5) * 0.7F;
        float var14 = 0.016666668F * var13;
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(x + 0.0F, y + entity.height + 0.4F, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        if (mc.gameSettings.thirdPersonView == 2) {
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, -1.0F, 0.0F, 0.0F);
        } else {
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        }
        GlStateManager.scale(-var14, -var14, var14);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        int var17 = -50;
        if (entity.isSneaking()) {
            var17 += 4;
        }
        var17 -= distance / 5;
        if (var17 < -8) {
            var17 = -8;
        }
        float var18 = mc.fontRendererObj.getStringWidth(entityName) / 2;
        float desiredWidth = ((mc.fontRendererObj.getStringWidth(entityName))/ entity.getMaxHealth()) * Math.min(entity.getHealth(), entity.getMaxHealth());
		if (desiredWidth < animated || desiredWidth > animated) {
			if (Math.abs(desiredWidth - animated) <= xSpeed) {
				animated = desiredWidth;
			} else {
				animated += (animated < desiredWidth ? xSpeed * 3 : -xSpeed);
			}
		}
		
		RenderUtilities.drawRectangle(-var18,  var17 + 10, animated + (var18 - mc.fontRendererObj.getStringWidth(entityName) / 2), 1F, PlayerUtils.getHealthColor(entity));
		RenderUtilities.drawRectangle(-var18,  var17  - 4, mc.fontRendererObj.getStringWidth(entityName), mc.fontRendererObj.FONT_HEIGHT + 5, new Color(0, 0, 0, opacity.getValue()).getRGB());
      
		mc.fontRendererObj.drawStringWithShadow(entityName, -var18, var17 - 1, Color.white.getRGB());
		GL11.glPushMatrix();
		GL11.glScaled(0.5, 0.5, 0.5);
		GL11.glPopMatrix();
		GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }


    public static double interpolate(double start, double end, double percent) {
        return start + (end - start) * percent;
    }

    public int getNametagColor(EntityLivingBase entity) {
        int i = 0;
        int color = 0xFFFFFFFF;
        if (entity instanceof EntityPlayer && Splash.getInstance().getFriendManager().isFriend(entity.getName())) {
            color = Color.CYAN.getRGB();
        } else if (entity.isInvisibleToPlayer(mc.thePlayer)) {
            color = 27753;
        } else if (entity.isSneaking()) {
            color = 7761001;
        }
//        } else if (entity.isEntityAlive() && !mc.getCurrentServerData().serverIP.contains("mineplex")) {
//
//        }


        return color;
    }

    private boolean isRenderingPossible(Entity entity) {
        return entity != null && entity != mc.thePlayer;
    }
}