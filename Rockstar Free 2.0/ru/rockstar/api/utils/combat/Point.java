package ru.rockstar.api.utils.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class Point {
	private double x, y, z;
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setZ(double z) {
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	
	public float getDistanceToEntity(Entity entityIn)
    {
        float f = (float)(this.x - entityIn.posX);
        float f1 = (float)(this.y - entityIn.posY);
        float f2 = (float)(this.z - entityIn.posZ);
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public double getDistance(double x, double y, double z)
    {
        double d0 = this.x - x;
        double d1 = this.y - y;
        double d2 = this.z - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }
}
