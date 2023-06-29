package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.visual.anim.MathUtil;
import org.moonware.client.feature.impl.visual.anim.WorldRenderEvent;
import org.moonware.client.helpers.Utils.render.RenderUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class DamageParticles2 extends Feature {
    ArrayList<partical> particals = new ArrayList<>();
    public static ColorSetting color = new ColorSetting("Color",new Color(ClientHelper.getColor().getRGB()));
    public static NumberSetting deleteAfter = new NumberSetting("Delete After ? ",1500,100,10000,1);


    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.world != null && Minecraft.world != null) {
            for (Entity entity : Minecraft.world.loadedEntityList) {
                EntityLivingBase player = (EntityLivingBase) entity;
                String name = player.getName();
                if (((EntityLivingBase) entity).getHurtTime() > 0 && player != Minecraft.player) {
                    particals.add(new partical(player.posX + MathUtil.random(-0.05f, 0.05f), MathUtil.random((float) (player.posY + player.height), (float) player.posY), player.posZ + MathUtil.random(-0.05f, 0.05f)));
                    particals.add(new partical(MathUtil.random((float) (player.posX - player.width), (float) (player.posX + 0.1f)), MathUtil.random((float) (player.posY + player.height), (float) (player.posY + 0.1f)), MathUtil.random((float) (player.posZ - player.width), (float) (player.posZ + 0.1f))));
                    particals.add(new partical(MathUtil.random((float) (player.posX + player.width), (float) (player.posX + 0.1f)), MathUtil.random((float) (player.posY + player.height), (float) (player.posY + 0.1f)), MathUtil.random((float) (player.posZ + player.width), (float) (player.posZ + 0.1f))));
                    particals.add(new partical(player.posX + MathUtil.random(-0.05f, 0.05f), MathUtil.random((float) (player.posY + player.height), (float) player.posY), player.posZ + MathUtil.random(-0.05f, 0.05f)));
//                    particals.add(new partical(MathUtil.random((float) (player.posX - player.width * 2), (float) (player.posX + 0.1f)), MathUtil.random((float) (player.posY + player.height), (float) (player.posY + 0.1f)), MathUtil.random((float) (player.posZ - player.width / 2), (float) (player.posZ + 0.1f))));
//                    particals.add(new partical(MathUtil.random((float) (player.posX + player.width / 2), (float) (player.posX + 0.1f)), MathUtil.random((float) (player.posY + player.height), (float) (player.posY + 0.1f)), MathUtil.random((float) (player.posZ + player.width * 2), (float) (player.posZ + 0.1f))));

                }

                for (int i = 0; i < particals.size(); i++) {
                    if (System.currentTimeMillis() - particals.get(i).getTime() >= deleteAfter.getCurrentIntValue()) {
                        particals.remove(i);
                    }
                }
            }
        }
    }

    @EventTarget
    public void render(WorldRenderEvent event) {

        if (Minecraft.player != null && Minecraft.world != null) {
            for (partical partical : particals) {
                float step = (System.currentTimeMillis() - partical.time) / 10;
                Color colorc = new Color(color.getColor());
                partical.render(new Color(colorc.getRed(),colorc.getGreen(),colorc.getBlue(), Math.round(partical.alpha)).getRGB());
                //RenderUtils2.drawBlur(6,()-> partical.render(new Color(colorc.getRed(),colorc.getGreen(),colorc.getBlue(), (int) Math.round(partical.alpha)).getRGB()));
            }
        }
    }

    public class partical {
        double x;
        double y;
        double z;
        double motionX;
        double motionY;
        double motionZ;
        long time;
        public int alpha = 180;

        public partical(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            motionX = MathUtil.random(-0.04f, 0.06f);
            motionY = MathUtil.random(-0.02f, 0.04f);
            motionZ = MathUtil.random(-0.08f, 0.04f);
            time = System.currentTimeMillis();
        }


        public long getTime() {
            return time;
        }

        public void update() {
            double yEx = 0;

            double sp = Math.sqrt(motionX * motionX + motionZ * motionZ) * 1;
            x += motionX;

            y += motionY;

            if (posBlock(x, y, z)) {
                motionY = -motionY / 1.1;
            } else {
                if (
                        posBlock(x, y, z) ||
                                posBlock(x, y - yEx, z) ||
                                posBlock(x, y + yEx, z) ||

                                posBlock(x - sp, y, z - sp) ||
                                posBlock(x + sp, y, z + sp) ||
                                posBlock(x + sp, y, z - sp) ||
                                posBlock(x - sp, y, z + sp) ||
                                posBlock(x + sp, y, z) ||
                                posBlock(x - sp, y, z) ||
                                posBlock(x, y, z + sp) ||
                                posBlock(x, y, z - sp) ||

                                posBlock(x - sp, y - yEx, z - sp) ||
                                posBlock(x + sp, y - yEx, z + sp) ||
                                posBlock(x + sp, y - yEx, z - sp) ||
                                posBlock(x - sp, y - yEx, z + sp) ||
                                posBlock(x + sp, y - yEx, z) ||
                                posBlock(x - sp, y - yEx, z) ||
                                posBlock(x, y - yEx, z + sp) ||
                                posBlock(x, y - yEx, z - sp) ||

                                posBlock(x - sp, y + yEx, z - sp) ||
                                posBlock(x + sp, y + yEx, z + sp) ||
                                posBlock(x + sp, y + yEx, z - sp) ||
                                posBlock(x - sp, y + yEx, z + sp) ||
                                posBlock(x + sp, y + yEx, z) ||
                                posBlock(x - sp, y + yEx, z) ||
                                posBlock(x, y + yEx, z + sp) ||
                                posBlock(x, y + yEx, z - sp)

                ) {
                    motionX = -motionX + motionZ;
                    motionZ = -motionZ + motionX;
                }


            }

            z += motionZ;


            motionX /= 1.005;
            motionZ /= 1.005;
            motionY /= 1.005;
        }

        public void render(int color) {
            update();
            alpha-=0.1;
            float scale = 0.07f;
            GlStateManager.disableDepth();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            try {

                double posX = x - RenderManager.renderPosX;
                double posY = y - RenderManager.renderPosY;
                double posZ = z - RenderManager.renderPosZ;

                double distanceFromPlayer = Minecraft.player.getDistance(x, y - 1, z);
                int quality = (int) (distanceFromPlayer * 4 + 10);

                if (quality > 350)
                    quality = 350;

                GL11.glPushMatrix();
                GL11.glTranslated(posX, posY, posZ);


                GL11.glScalef(-scale, -scale, -scale);

                GL11.glRotated(-(Minecraft.getRenderManager()).playerViewY, 0.0D, 1.0D, 0.0D);
                GL11.glRotated((Minecraft.getRenderManager()).playerViewX, 1.0D, 0.0D, 0.0D);

                Color c = new Color(color);

                RenderUtil.drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);

                if (distanceFromPlayer < 4)
                    RenderUtil.drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);

                if (distanceFromPlayer < 20)
                    RenderUtil.drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);


                GL11.glScalef(0.8f, 0.8f, 0.8f);

                GL11.glPopMatrix();


            } catch (ConcurrentModificationException ignored) {
            }

            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GlStateManager.enableDepth();

            GL11.glColor3d(255, 255, 255);
        }

        private boolean posBlock(double x, double y, double z) {
            return (Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.AIR &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.WATER &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.LAVA &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.BED &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.CAKE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.TALLGRASS &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.GRASS &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.FLOWER_POT &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.RED_FLOWER &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.YELLOW_FLOWER &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.SAPLING &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.VINE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.ACACIA_FENCE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.ACACIA_FENCE_GATE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.BIRCH_FENCE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.BIRCH_FENCE_GATE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.DARK_OAK_FENCE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.DARK_OAK_FENCE_GATE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.JUNGLE_FENCE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.JUNGLE_FENCE_GATE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.NETHER_BRICK_FENCE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.OAK_FENCE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.OAK_FENCE_GATE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.SPRUCE_FENCE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.SPRUCE_FENCE_GATE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.ENCHANTING_TABLE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.END_PORTAL_FRAME &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.DOUBLE_PLANT &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.STANDING_SIGN &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.WALL_SIGN &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.SKULL &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.DAYLIGHT_DETECTOR &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.DAYLIGHT_DETECTOR_INVERTED &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.STONE_SLAB &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.WOODEN_SLAB &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.CARPET &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.DEADBUSH &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.VINE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.REDSTONE_WIRE &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.REEDS &&
                    Minecraft.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.SNOW_LAYER);
        }

    }

    public DamageParticles2() {
        super("FlyingParticles", "Летающие партиклы вокруг таргета", Type.Combat);
        addSettings(color, deleteAfter);
    }
}
