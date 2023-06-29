package org.moonware.client.feature.impl.combat;

import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class ParticlesDamager extends Feature {
    public ParticlesDamager() {
        super("ZalupaAAA","", Type.Combat);
    }

//    private final NumberSetting amount = new NumberSetting("Amount", this, 10, 1, 20, 1);
//    private final BooleanSettingComponent physics = new BooleanSettingComponent("Physics", this, true);
//
//    private final List<Particle> particles = new EvictingList<>(100);
//    private final TimeUtil timer = new TimeUtil();
//    private EntityLivingBase target;
//
//    @EventTarget
//    public void onAttackEvent(final AttackEvent event) {
//        if (event.getTarget() instanceof EntityLivingBase)
//            target = (EntityLivingBase) event.getTarget();
//    }
//
//    @EventTarget
//    public void onPreMotion(EventPreMotion event) {
//        if (target != null && target.hurtTime >= 9 && mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) < 10) {
//            for (int i = 0; i < amount.getValue(); i++)
//                particles.add(new Particle(new Vec3(target.posX + (Math.random() - 0.5) * 0.5, target.posY + Math.random() * 1 + 0.5, target.posZ + (Math.random() - 0.5) * 0.5)));
//
//            target = null;
//        }
//    }
//
//    @EventTarget
//    public void onRender3DEvent(final Render3DEvent event) {
//        if (particles.isEmpty())
//            return;
//
//        for (int i = 0; i <= timer.getElapsedTime() / 1E+11; i++) {
//            if (physics.isEnabled())
//                particles.forEach(Particle::update);
//            else
//                particles.forEach(Particle::updateWithoutPhysics);
//        }
//
//        particles.removeIf(particle -> mc.thePlayer.getDistanceSq(particle.getPosition().xCoord, particle.getPosition().yCoord, particle.getPosition().zCoord) > 50 * 10);
//
//        timer.reset();
//
//        RenderUtil.renderParticles(particles);
//    }
}
