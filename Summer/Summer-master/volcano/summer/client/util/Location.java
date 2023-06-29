package volcano.summer.client.util;

public class Location {

	private double x, y, z;

	private float yaw, pitch;

	public static Location loc;

	public Location() {
		loc = this;
	}

	public Location(double x, double y, double z, float yaw, float pitch) {

		this.x = x;

		this.y = y;

		this.z = z;

		this.yaw = yaw;

		this.pitch = pitch;

	}

	public Location(float yaw, float pitch) {

		this.yaw = yaw;

		this.pitch = pitch;

	}

	public Location(double x, double y, double z) {

		this.x = x;

		this.y = y;

		this.z = z;

	}

	public Location setX(double x) {

		this.x = x;

		return this;

	}

	public Location setY(double y) {

		this.y = y;

		return this;

	}

	public Location setZ(double z) {

		this.z = z;

		return this;

	}

	public Location setYaw(float yaw) {

		this.yaw = yaw;

		return this;

	}

	public Location setPitch(float pitch) {

		this.pitch = pitch;

		return this;

	}

	public Location add(Location location) {

		this.x += location.getX();

		this.y += location.getY();

		this.z += location.getZ();

		return this;

	}

	public Location multiply(Location location) {

		this.x *= location.getX();

		this.y *= location.getY();

		this.z *= location.getZ();

		return this;

	}

	public Location divide(Location location) {

		this.x /= location.getX();

		this.y /= location.getY();

		this.z /= location.getZ();

		return this;

	}

	public Location subtract(Location location) {

		this.x -= location.getX();

		this.y -= location.getY();

		this.z -= location.getZ();

		return this;

	}

	public Location add(double x, double y, double z) {

		this.x += x;

		this.y += y;

		this.z += z;

		return this;

	}

	public Location multiply(double x, double y, double z) {

		this.x *= x;

		this.y *= y;

		this.z *= z;

		return this;

	}

	public Location divide(double x, double y, double z) {

		this.x /= x;

		this.y /= y;

		this.z /= z;

		return this;

	}

	public Location subtract(double x, double y, double z) {

		this.x -= x;

		this.y -= y;

		this.z -= z;

		return this;

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

	public float getYaw() {

		return yaw;

	}

	public float getPitch() {

		return pitch;

	}
}