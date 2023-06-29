/*
 * Decompiled with CFR 0.150.
 */
package wavecapes.util;

import net.minecraft.util.math.MathHelper;

public final class Vector3f {
    public static Vector3f XN = new Vector3f(-1.0f, 0.0f, 0.0f);
    public static Vector3f XP = new Vector3f(1.0f, 0.0f, 0.0f);
    public static Vector3f YN = new Vector3f(0.0f, -1.0f, 0.0f);
    public static Vector3f YP = new Vector3f(0.0f, 1.0f, 0.0f);
    public static Vector3f ZN = new Vector3f(0.0f, 0.0f, -1.0f);
    public static Vector3f ZP = new Vector3f(0.0f, 0.0f, 1.0f);
    public static Vector3f ZERO = new Vector3f(0.0f, 0.0f, 0.0f);
    private float x;
    private float y;
    private float z;

    public Vector3f() {
    }

    public Vector3f(float f, float g, float h) {
        x = f;
        y = g;
        z = h;
    }

    public Vector3f(Vector4f vector4f) {
        this(vector4f.x(), vector4f.y(), vector4f.z());
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Vector3f vector3f = (Vector3f)object;
        if (Float.compare(vector3f.x, x) != 0) {
            return false;
        }
        if (Float.compare(vector3f.y, y) != 0) {
            return false;
        }
        return Float.compare(vector3f.z, z) == 0;
    }

    public int hashCode() {
        int i = Float.floatToIntBits(x);
        i = 31 * i + Float.floatToIntBits(y);
        i = 31 * i + Float.floatToIntBits(z);
        return i;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }

    public void mul(float f) {
        x *= f;
        y *= f;
        z *= f;
    }

    public void mul(float f, float g, float h) {
        x *= f;
        y *= g;
        z *= h;
    }

    public void clamp(Vector3f vector3f, Vector3f vector3f2) {
        x = MathHelper.clamp(x, vector3f.x(), vector3f2.x());
        y = MathHelper.clamp(y, vector3f.x(), vector3f2.y());
        z = MathHelper.clamp(z, vector3f.z(), vector3f2.z());
    }

    public void clamp(float f, float g) {
        x = MathHelper.clamp(x, f, g);
        y = MathHelper.clamp(y, f, g);
        z = MathHelper.clamp(z, f, g);
    }

    public void set(float f, float g, float h) {
        x = f;
        y = g;
        z = h;
    }

    public void load(Vector3f vector3f) {
        x = vector3f.x;
        y = vector3f.y;
        z = vector3f.z;
    }

    public void add(float f, float g, float h) {
        x += f;
        y += g;
        z += h;
    }

    public void add(Vector3f vector3f) {
        x += vector3f.x;
        y += vector3f.y;
        z += vector3f.z;
    }

    public void sub(Vector3f vector3f) {
        x -= vector3f.x;
        y -= vector3f.y;
        z -= vector3f.z;
    }

    public float dot(Vector3f vector3f) {
        return x * vector3f.x + y * vector3f.y + z * vector3f.z;
    }

    public boolean normalize() {
        float f = x * x + y * y + z * z;
        if ((double)f < 1.0E-5) {
            return false;
        }
        float g = Mth.fastInvSqrt(f);
        x *= g;
        y *= g;
        z *= g;
        return true;
    }

    public void cross(Vector3f vector3f) {
        float f = x;
        float g = y;
        float h = z;
        float i = vector3f.x();
        float j = vector3f.y();
        float k = vector3f.z();
        x = g * k - h * j;
        y = h * i - f * k;
        z = f * j - g * i;
    }

    public void transform(Quaternion quaternion) {
        Quaternion quaternion2 = new Quaternion(quaternion);
        quaternion2.mul(new Quaternion(x(), y(), z(), 0.0f));
        Quaternion quaternion3 = new Quaternion(quaternion);
        quaternion3.conj();
        quaternion2.mul(quaternion3);
        set(quaternion2.i(), quaternion2.j(), quaternion2.k());
    }

    public void lerp(Vector3f vector3f, float f) {
        float g = 1.0f - f;
        x = x * g + vector3f.x * f;
        y = y * g + vector3f.y * f;
        z = z * g + vector3f.z * f;
    }

    public Quaternion rotation(float f) {
        return new Quaternion(this, f, false);
    }

    public Quaternion rotationDegrees(float f) {
        return new Quaternion(this, f, true);
    }

    public Vector3f copy() {
        return new Vector3f(x, y, z);
    }

    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}

