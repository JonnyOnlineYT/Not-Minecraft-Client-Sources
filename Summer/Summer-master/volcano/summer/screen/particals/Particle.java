package volcano.summer.screen.particals;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Particle {
	private float posX;
	private float posY;
	private float alpha;
	private float size;
	private float speed;
	public Minecraft mc;

	public Particle(final float posX, final float posY, final float size, final float speed, final float alpha) {
		this.mc = Minecraft.getMinecraft();
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSize(size);
		this.setSpeed(speed);
		this.setAlpha(alpha);
	}

	public int random(final int low, final int high) {
		final Random r = new Random();
		return r.nextInt(high - low) + low;
	}

	public ScaledResolution getRes() {
		return new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
	}

	public void render(final ParticleManager p) {
		if (this.getAlpha() - 1.0f >= 0.0f) {
			this.setAlpha(this.getAlpha() - 1.0f);
		}
	}

	public float getPosX() {
		return this.posX;
	}

	public void setPosX(final float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return this.posY;
	}

	public void setPosY(final float posY) {
		this.posY = posY;
	}

	public float getAlpha() {
		return this.alpha;
	}

	public void setAlpha(final float alpha) {
		this.alpha = alpha;
	}

	public float getSize() {
		return this.size;
	}

	public void setSize(final float size) {
		this.size = size;
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setSpeed(final float speed) {
		this.speed = speed;
	}
}