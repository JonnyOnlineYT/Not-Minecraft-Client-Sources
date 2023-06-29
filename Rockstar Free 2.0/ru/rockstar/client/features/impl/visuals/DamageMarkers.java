package ru.rockstar.client.features.impl.visuals;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.icu.math.BigDecimal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.event.event.EventRespawn;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class DamageMarkers extends Feature{
	private static final Minecraft mc = Minecraft.getMinecraft();
	 private final Map<Integer, Float> hpData = Maps.newHashMap();
	 private final List<DamageMarkers.Particle> particles = Lists.newArrayList();
	 private final List<ru.rockstar.client.features.impl.visuals.Particle> particles1 = Lists.newArrayList();
	 private final TimerHelper timerHelper = new TimerHelper();
	 public static ListSetting dMode;
	 public static  NumberSetting deleteAfter;
	    private EntityLivingBase target;
	 
	public DamageMarkers() {
        super("DamageParticles", "Отображает регенирацию/дамаг всех энтити вокруг", 0, Category.VISUALS);
        dMode = new ListSetting("Particles Mode", "Number", () -> true, "Circles", "Number");
        deleteAfter = new NumberSetting("Delete After", 7.0F, 1.0F, 50.0F, 1.0F, () -> true);
        addSettings(deleteAfter);
    }

    @EventTarget
    public void onRespawn(EventRespawn event) {
        this.particles.clear();
    }
    

    @EventTarget
    public void onUpdate(EventUpdate e) {
        Iterator var2 = mc.world.loadedEntityList.iterator();
        target = KillAura.target;
        if (dMode.getCurrentMode().equalsIgnoreCase("Circles")) {
        	if (target != null && target.hurtTime >= 9 && mc.player.getDistance(target.posX, target.posY, target.posZ) < 10) {
                for (int i = 0; i < 10; i++)
                    particles1.add(new ru.rockstar.client.features.impl.visuals.Particle(new Vec3d(target.posX + (Math.random() - 0.5) * 0.5, target.posY + Math.random() * 1 + 0.5, target.posZ + (Math.random() - 0.5) * 0.5)));

                target = null;
            }
        } else {
        	while(var2.hasNext()) {
                Entity entity = (Entity)var2.next();
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase ent = (EntityLivingBase)entity;
                    double lastHp = (double)(Float)this.hpData.getOrDefault(ent.getEntityId(), ent.getMaxHealth());
                    this.hpData.remove(entity.getEntityId());
                    this.hpData.put(entity.getEntityId(), ent.getHealth());
                    if (lastHp != (double)ent.getHealth()) {
                        Color color;
                        if (lastHp > (double)ent.getHealth()) {
                            color = Color.red;
                        } else {
                            color = Color.GREEN;
                        }

                        Vec3d loc = new Vec3d(entity.posX + Math.random() * 0.5D * (double)(Math.random() > 0.5D ? -1 : 1), entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 0.5D, entity.posZ + Math.random() * 0.5D * (double)(Math.random() > 0.5D ? -1 : 1));
                        double str = (new BigDecimal(Math.abs(lastHp - (double)ent.getHealth()))).setScale(1, 4).doubleValue();
                        this.particles.add(new DamageMarkers.Particle("" + str, loc.xCoord, loc.yCoord, loc.zCoord, color));
                    }
                }
            }
        }
    }

    @EventTarget
    public void onRender3d(Event3D e) {
    	if (dMode.getCurrentMode().equalsIgnoreCase("Circles")) {
    	if (particles1.isEmpty())
            return;

        for (int i = 0; i <= timerHelper.getTime() / 1E+11; i++) {
            //if (physics.isEnabled())
                //particles.forEach(ru.rockstar.client.features.impl.visuals.Particle::update);
            //else
                particles1.forEach(ru.rockstar.client.features.impl.visuals.Particle :: updateWithoutPhysics);
        }

        particles1.removeIf(particle -> mc.player.getDistanceSq(particle.getPosition().xCoord, particle.getPosition().yCoord, particle.getPosition().zCoord) > 50 * 10);

        timerHelper.reset();

        RenderUtils.renderParticles(particles1);
    	} else {
    		
    	
        if (timerHelper.hasReached(this.deleteAfter.getNumberValue() * 300.0F)) {
            this.particles.clear();
            timerHelper.reset();
        }

        if (!this.particles.isEmpty()) {
            Iterator var2 = this.particles.iterator();

            while(var2.hasNext()) {
            	DamageMarkers.Particle p = (DamageMarkers.Particle)var2.next();
                if (p != null) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enablePolygonOffset();
                    GlStateManager.doPolygonOffset(1.0F, -1500000.0F);
                    GlStateManager.translate(p.posX - mc.getRenderManager().renderPosX, p.posY - mc.getRenderManager().renderPosY, p.posZ - mc.getRenderManager().renderPosZ);
                    GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                    float var10001 = mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
                    GlStateManager.rotate(mc.getRenderManager().playerViewX, var10001, 0.0F, 0.0F);
                    GlStateManager.scale(-0.03D, -0.03D, 0.03D);
                    GL11.glDepthMask(false);
                    	 DrawHelper.drawGlow(-mc.mntsb_17.getStringWidth(p.str) * 0.5D, -mc.fontRendererObj.FONT_HEIGHT + 1 - 5,-mc.mntsb_17.getStringWidth(p.str) * 0.5D + 10, -mc.fontRendererObj.FONT_HEIGHT + 1 + 10, p.color.getRGB() - new Color(0,0,0,100).getRGB());
                         mc.mntsb_17.drawStringWithShadow(p.str, ((float)((-mc.mntsb_17.getStringWidth(p.str)) * 0.5D)), (-mc.fontRendererObj.FONT_HEIGHT + 1), p.color.getRGB());
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDepthMask(true);
                    GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
                    GlStateManager.disablePolygonOffset();
                    GlStateManager.resetColor();
                    GlStateManager.popMatrix();
                }
            }
        }
    	}

    }

    class Particle {
        public String str;
        public double posX;
        public double posY;
        public double posZ;
        public Color color;
        public int ticks;

        public Particle(String str, double posX, double posY, double posZ, Color color) {
            this.str = str;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.color = color;
            this.ticks = 0;
        }
    }


}
