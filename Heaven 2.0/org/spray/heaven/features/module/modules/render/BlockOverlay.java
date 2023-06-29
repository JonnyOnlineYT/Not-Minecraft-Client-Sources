package org.spray.heaven.features.module.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockAir;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.events.WorldRenderEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.font.FontRenderer;
import org.spray.heaven.font.IFont;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.RenderUtil;
import org.spray.heaven.util.render.WorldRender;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import java.awt.*;

@ModuleInfo(name = "BlockOverlay", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class BlockOverlay extends Module {
    public double x1, y1, z1;
    public int alpha;
    public double damage;

    @EventTarget
    public void render(WorldRenderEvent e) {
        FontRenderer font = IFont.POPPIN_LIST;
        BlockPos pos = mc.objectMouseOver.getBlockPos();
        x1 = RenderUtil.interpolate(pos.getX(), x1, 0.1);
        y1 = RenderUtil.interpolate(pos.getY(), y1, 0.1);
        z1 = RenderUtil.interpolate(pos.getZ(), z1, 0.1);
        damage = RenderUtil.interpolate(mc.playerController.curBlockDamageMP * 90, damage, 0.1);
        alpha = (int) RenderUtil.interpolate(mc.playerController.curBlockDamageMP > 0 ? (mc.world.getBlockState(pos).getBlock() instanceof BlockAir ? 0 : 255) : 0, alpha, 0.05f);
        double x = x1 - mc.getRenderManager().renderPosX;
        double y = y1 - mc.getRenderManager().renderPosY + 2;
        double z = z1 - mc.getRenderManager().renderPosZ;
        GL11.glPushMatrix();
        GlStateManager.disableDepth();
        GL11.glTranslated(x, y, z);
        float scale = (float) ((float) (1 / 100f) + mc.player.getDistance(x1, y1, z1) / 500);
        GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
        GL11.glRotated(mc.getRenderManager().playerViewX, 1.0D, 0.0D, 0.0D);
        GL11.glScalef(-scale, -scale, scale);
        Drawable.drawBlurredShadow(0,0, 100, 50, 15, new Color(0, 0, 0, alpha));
        RoundedShader.drawRound(0, 0, 100, 50, 2, new Color(21, 25, 23, alpha));
        if (!(mc.world.getBlockState(pos).getBlock() instanceof BlockAir) && mc.playerController.curBlockDamageMP > 0) {
            font.drawString(mc.world.getBlockState(pos).getBlock().getLocalizedName(), 5, 5, new Color(255, 255, 255, alpha).getRGB());
            font.drawString(String.valueOf((int) (mc.playerController.curBlockDamageMP * 100)) + "%", 5, 13, new Color(255, 255, 255, alpha).getRGB());
            RoundedShader.drawRound(5, 25, 90, 5, 2, new Color(20, 20, 23));
            Drawable.drawBlurredShadow(5, 25, (float) damage, 5, 15, new Color(50, 200, 200));
            RoundedShader.drawRound(5F, 25F, (float) damage, 5, 2, new Color(50, 200, 200));
        }
        GlStateManager.enableDepth();
        GL11.glPopMatrix();
    }

}