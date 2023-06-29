package volcano.summer.screen.particals;

import volcano.summer.client.util.Colors;
import volcano.summer.client.util.R2DUtils;

public class TopRightParticle extends Particle {
	TopRightParticle(final float posX, final float posY, final float size, final float speed, final float alpha) {
		super(posX, posY, size, speed, alpha);
	}

	@Override
	public void render(final ParticleManager p) {
		super.render(p);
		this.setPosY(this.getPosY() - this.getSpeed());
		this.setPosX(this.getPosX() - this.getSpeed());
		R2DUtils.drawFullCircle(this.getPosX(), this.getPosY(), this.getSize(),
				Colors.getColor(255, 255, 255, (int) this.getAlpha()));
	}
}
