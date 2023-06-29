package volcano.summer.screen.particals;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.util.MathHelper;
import volcano.summer.client.util.R2DUtils;
import volcano.summer.client.util.TimerUtil;

public class ParticleGenerator {
	private int count;
	private int width;
	private int height;
	private ArrayList<Particle> particles;
	private Random random;
	private TimerUtil timer;
	int state;
	int a;
	int r;
	int g;
	int b;

	public ParticleGenerator(final int count, final int width, final int height) {
		this.particles = new ArrayList<Particle>();
		this.random = new Random();
		this.timer = new TimerUtil();
		this.state = 0;
		this.a = 255;
		this.r = 255;
		this.g = 0;
		this.b = 0;
		this.count = count;
		this.width = width;
		this.height = height;
		for (int i = 0; i < count; ++i) {
			this.particles.add(new Particle(this.random.nextInt(width), this.random.nextInt(height)));
		}
	}

	public void drawParticles() {
		for (final Particle p : this.particles) {
			if (p.reset) {
				p.resetPosSize();
				p.access$1(p, false);
			}
			p.draw();
		}
	}

	public class Particle {
		private int x;
		private int y;
		private int k;
		private float size;
		private boolean reset;
		private Random random;
		private TimerUtil timer;

		public Particle(final int x, final int y) {
			this.random = new Random();
			this.timer = new TimerUtil();
			this.x = x;
			this.y = y;
			this.size = this.genRandom(2.0f, 3.0f);
			// pre particle size or speed again possibly
		}

		public void draw() {
			if (this.size <= 0.0f) {
				this.reset = true;
			}
			this.size -= 0.1f;
			++this.k;
			final int xx = (int) (MathHelper.cos(0.1f * (this.x + this.k)) * 55.0f);
			// don't know
			final int yy = (int) (MathHelper.cos(0.1f * (this.y + this.k)) * 55.0f);
			// Speed rate ig
			R2DUtils.drawBorderedCircle(this.x + xx, this.y + yy, this.size, 553648128, 553648127);
		}

		public void resetPosSize() {
			this.x = this.random.nextInt(ParticleGenerator.this.width);
			this.y = this.random.nextInt(ParticleGenerator.this.height);
			this.size = this.genRandom(8.0f, 12.0f);
			// post particle size
		}

		public float genRandom(final float min, final float max) {
			return (float) (min + Math.random() * (max - min + 1.0f));
			// haven't tested
		}

		public void access$1(final Particle particle, final boolean reset) {
			particle.reset = reset;
			// tf?
		}
	}
}