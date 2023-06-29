package volcano.summer.screen.particals;

import net.minecraft.client.*;
import java.util.concurrent.*;
import java.util.*;
import net.minecraft.client.gui.*;

public class ParticleManager {
	private List<Particle> particles;
	public Minecraft mc;
	private boolean rightClicked;

	public ParticleManager() {
		this.particles = new CopyOnWriteArrayList<Particle>();
		this.mc = Minecraft.getMinecraft();
		this.particles.clear();
	}

	public void render(final int x, final int y) {
		if (this.particles.size() <= 10000) {
			for (int i = 0; i < 5; ++i) {
				final int rand = this.random(0, 6);
				if (rand == 1) {
					this.particles.add(new TopLeftParticle(
							this.centerWidth()
									+ this.random(-this.getRes().getScaledWidth(), this.getRes().getScaledWidth()),
							this.centerHeight()
									+ this.random(-this.getRes().getScaledHeight(), this.getRes().getScaledHeight()),
							this.random(1, 2), this.random(1, 2), this.random(40, 200)));
				}
				if (rand == 2) {
					this.particles.add(new GravityParticle(
							this.centerWidth()
									+ this.random(-this.getRes().getScaledWidth(), this.getRes().getScaledWidth()),
							this.centerHeight()
									+ this.random(-this.getRes().getScaledHeight(), this.getRes().getScaledHeight()),
							this.random(1, 3), this.random(1, 2), this.random(40, 220)));
				}
				if (rand == 3) {
					this.particles.add(new TopRightParticle(
							this.centerWidth()
									+ this.random(-this.getRes().getScaledWidth(), this.getRes().getScaledWidth()),
							this.centerHeight()
									+ this.random(-this.getRes().getScaledHeight(), this.getRes().getScaledHeight()),
							this.random(1, 3), this.random(1, 2), this.random(40, 220)));
				}
				if (rand == 4) {
					this.particles.add(new BottomLeftParticle(
							this.centerWidth()
									+ this.random(-this.getRes().getScaledWidth(), this.getRes().getScaledWidth()),
							this.centerHeight()
									+ this.random(-this.getRes().getScaledHeight(), this.getRes().getScaledHeight()),
							this.random(1, 3), this.random(1, 2), this.random(40, 220)));
				}
				if (rand == 5) {
					this.particles.add(new BottomRightParticle(
							this.centerWidth()
									+ this.random(-this.getRes().getScaledWidth(), this.getRes().getScaledWidth()),
							this.centerHeight()
									+ this.random(-this.getRes().getScaledHeight(), this.getRes().getScaledHeight()),
							this.random(1, 3), this.random(1, 2), this.random(40, 220)));
				}
			}
		}
		for (final Particle p : this.particles) {
			if (p.getAlpha() <= 0.0f) {
				this.particles.remove(p);
			}
			p.render(this);
		}
	}

	public int random(final int low, final int high) {
		final Random r = new Random();
		return r.nextInt(high - low) + low;
	}

	public ScaledResolution getRes() {
		return new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
	}

	public int centerWidth() {
		return this.getRes().getScaledWidth() / 2;
	}

	public int centerHeight() {
		return this.getRes().getScaledHeight() / 2;
	}

	public CopyOnWriteArrayList<Particle> getParticles() {
		return (CopyOnWriteArrayList<Particle>) (CopyOnWriteArrayList) this.particles;
	}

	public void setParticles(final CopyOnWriteArrayList<Particle> particles) {
		this.particles = particles;
	}
}