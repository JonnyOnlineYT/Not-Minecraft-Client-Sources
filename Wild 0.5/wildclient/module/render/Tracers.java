//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.render;

import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import black.nigger.wildclient.module.EntityUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import black.nigger.wildclient.module.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import black.nigger.wildclient.module.Category;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import black.nigger.wildclient.module.Module;

public class Tracers extends Module
{
    private transient List<EntityPlayer> HITMEN;
    private transient List<String> CACHE;
    private transient int BOX;
    
    public Tracers() {
        super("Tracers", "Draw tracers", Category.RENDER);
        this.HITMEN = new ArrayList<EntityPlayer>();
        this.CACHE = new ArrayList<String>();
        this.BOX = 0;
    }
    
    @Override
    public void onEnable() {
        GL11.glNewList(this.BOX = GL11.glGenLists(1), 4864);
        RenderUtil.drawOutlinedBox(new AxisAlignedBB(-0.5, 0.0, -0.5, 0.5, 1.0, 0.5));
        GL11.glEndList();
        this.CACHE.clear();
    }
    
    @Override
    public void onDisable() {
        GL11.glDeleteLists(this.BOX, 1);
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        GL11.glPushAttrib(24581);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glPushMatrix();
        GL11.glTranslated(-Minecraft.getMinecraft().getRenderManager().viewerPosX, -Minecraft.getMinecraft().getRenderManager().viewerPosY, -Minecraft.getMinecraft().getRenderManager().viewerPosZ);
        this.drawTracers();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }
    
    private void drawBoxes(final float partialTicks) {
        GL11.glLineWidth(4.0f);
        for (final EntityPlayer entity : this.HITMEN) {
            GL11.glPushMatrix();
            final Vec3d interpolated = EntityUtil.getInterpolatedPos((Entity)entity, partialTicks);
            GL11.glTranslated(interpolated.x, interpolated.y, interpolated.z);
            GL11.glScaled(entity.width + 0.1, entity.height + 0.1, entity.width + 0.1);
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
            GL11.glCallList(this.BOX);
            GL11.glPopMatrix();
        }
    }
    
    private void drawTracers() {
        GL11.glLineWidth(4.0f);
        final Vec3d start = new Vec3d(Minecraft.getMinecraft().getRenderManager().viewerPosX, Minecraft.getMinecraft().getRenderManager().viewerPosY + Minecraft.getMinecraft().player.getEyeHeight(), Minecraft.getMinecraft().getRenderManager().viewerPosZ).add(Minecraft.getMinecraft().player.getLookVec());
        GL11.glBegin(1);
        for (final EntityPlayer entity : this.HITMEN) {
            final Vec3d target = entity.getEntityBoundingBox().getCenter();
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
            GL11.glVertex3d(start.x, start.y, start.z);
            GL11.glVertex3d(target.x, target.y, target.z);
        }
        GL11.glEnd();
    }
}
