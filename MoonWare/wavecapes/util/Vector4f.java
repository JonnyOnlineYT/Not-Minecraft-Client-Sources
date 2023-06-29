/*
 * Decompiled with CFR 0.150.
 */
package wavecapes.util;

public class Vector4f {
    private float x;
    private float y;
    private float z;
    private float w;

    public Vector4f() {
    }

    public Vector4f(float f, float g, float h, float i) {
        x = f;
        y = g;
        z = h;
        w = i;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Vector4f vector4f = (Vector4f)object;
        if (Float.compare(vector4f.x, x) != 0) {
            return false;
        }
        if (Float.compare(vector4f.y, y) != 0) {
            return false;
        }
        if (Float.compare(vector4f.z, z) != 0) {
            return false;
        }
        return Float.compare(vector4f.w, w) == 0;
    }

    public int hashCode() {
        int i = Float.floatToIntBits(x);
        i = 31 * i + Float.floatToIntBits(y);
        i = 31 * i + Float.floatToIntBits(z);
        i = 31 * i + Float.floatToIntBits(w);
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

    public float w() {
        return w;
    }

    public void mul(float f) {
        x *= f;
        y *= f;
        z *= f;
        w *= f;
    }

    public void set(float f, float g, float h, float i) {
        x = f;
        y = g;
        z = h;
        w = i;
    }

    public void add(float f, float g, float h, float i) {
        x += f;
        y += g;
        z += h;
        w += i;
    }

    public float dot(Vector4f vector4f) {
        return x * vector4f.x + y * vector4f.y + z * vector4f.z + w * vector4f.w;
    }

    public boolean normalize() {
        float f = x * x + y * y + z * z + w * w;
        if ((double)f < 1.0E-5) {
            return false;
        }
        float g = Mth.fastInvSqrt(f);
        x *= g;
        y *= g;
        z *= g;
        w *= g;
        return true;
    }

    public void transform(Matrix4f matrix4f) {
        float f = x;
        float g = y;
        float h = z;
        float i = w;
        x = matrix4f.m00 * f + matrix4f.m01 * g + matrix4f.m02 * h + matrix4f.m03 * i;
        y = matrix4f.m10 * f + matrix4f.m11 * g + matrix4f.m12 * h + matrix4f.m13 * i;
        z = matrix4f.m20 * f + matrix4f.m21 * g + matrix4f.m22 * h + matrix4f.m23 * i;
        w = matrix4f.m30 * f + matrix4f.m31 * g + matrix4f.m32 * h + matrix4f.m33 * i;
    }

    public void transform(Quaternion quaternion) {
        Quaternion quaternion2 = new Quaternion(quaternion);
        quaternion2.mul(new Quaternion(x(), y(), z(), 0.0f));
        Quaternion quaternion3 = new Quaternion(quaternion);
        quaternion3.conj();
        quaternion2.mul(quaternion3);
        set(quaternion2.i(), quaternion2.j(), quaternion2.k(), w());
    }

    public void perspectiveDivide() {
        x /= w;
        y /= w;
        z /= w;
        w = 1.0f;
    }

    public void lerp(Vector4f vector4f, float f) {
        float g = 1.0f - f;
        x = x * g + vector4f.x * f;
        y = y * g + vector4f.y * f;
        z = z * g + vector4f.z * f;
        w = w * g + vector4f.w * f;
    }

    public String toString() {
        return "[" + x + ", " + y + ", " + z + ", " + w + "]";
    }
}

