/*
 * Decompiled with CFR 0.150.
 */
package wavecapes.util;

public final class Quaternion {
    public static final Quaternion ONE = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
    private float i;
    private float j;
    private float k;
    private float r;

    public Quaternion(float f, float g, float h, float i) {
        this.i = f;
        j = g;
        k = h;
        r = i;
    }

    public Quaternion(Vector3f vector3f, float f, boolean bl) {
        if (bl) {
            f *= (float)Math.PI / 180;
        }
        float g = sin(f / 2.0f);
        i = vector3f.x() * g;
        j = vector3f.y() * g;
        k = vector3f.z() * g;
        r = cos(f / 2.0f);
    }

    public Quaternion(float f, float g, float h, boolean bl) {
        if (bl) {
            f *= (float)Math.PI / 180;
            g *= (float)Math.PI / 180;
            h *= (float)Math.PI / 180;
        }
        float i = sin(0.5f * f);
        float j = cos(0.5f * f);
        float k = sin(0.5f * g);
        float l = cos(0.5f * g);
        float m = sin(0.5f * h);
        float n = cos(0.5f * h);
        this.i = i * l * n + j * k * m;
        this.j = j * k * n - i * l * m;
        this.k = i * k * n + j * l * m;
        r = j * l * n - i * k * m;
    }

    public Quaternion(Quaternion quaternion) {
        i = quaternion.i;
        j = quaternion.j;
        k = quaternion.k;
        r = quaternion.r;
    }

    public static Quaternion fromYXZ(float f, float g, float h) {
        Quaternion quaternion = ONE.copy();
        quaternion.mul(new Quaternion(0.0f, (float)Math.sin(f / 2.0f), 0.0f, (float)Math.cos(f / 2.0f)));
        quaternion.mul(new Quaternion((float)Math.sin(g / 2.0f), 0.0f, 0.0f, (float)Math.cos(g / 2.0f)));
        quaternion.mul(new Quaternion(0.0f, 0.0f, (float)Math.sin(h / 2.0f), (float)Math.cos(h / 2.0f)));
        return quaternion;
    }

    public static Quaternion fromXYZDegrees(Vector3f vector3f) {
        return fromXYZ((float)Math.toRadians(vector3f.x()), (float)Math.toRadians(vector3f.y()), (float)Math.toRadians(vector3f.z()));
    }

    public static Quaternion fromXYZ(Vector3f vector3f) {
        return fromXYZ(vector3f.x(), vector3f.y(), vector3f.z());
    }

    public static Quaternion fromXYZ(float f, float g, float h) {
        Quaternion quaternion = ONE.copy();
        quaternion.mul(new Quaternion((float)Math.sin(f / 2.0f), 0.0f, 0.0f, (float)Math.cos(f / 2.0f)));
        quaternion.mul(new Quaternion(0.0f, (float)Math.sin(g / 2.0f), 0.0f, (float)Math.cos(g / 2.0f)));
        quaternion.mul(new Quaternion(0.0f, 0.0f, (float)Math.sin(h / 2.0f), (float)Math.cos(h / 2.0f)));
        return quaternion;
    }

    public Vector3f toXYZ() {
        float f = r() * r();
        float g = i() * i();
        float h = j() * j();
        float i = k() * k();
        float j = f + g + h + i;
        float k = 2.0f * r() * i() - 2.0f * j() * k();
        float l = (float)Math.asin(k / j);
        if (Math.abs(k) > 0.999f * j) {
            return new Vector3f(2.0f * (float)Math.atan2(i(), r()), l, 0.0f);
        }
        return new Vector3f((float)Math.atan2(2.0f * j() * k() + 2.0f * i() * r(), f - g - h + i), l, (float)Math.atan2(2.0f * i() * j() + 2.0f * r() * k(), f + g - h - i));
    }

    public Vector3f toXYZDegrees() {
        Vector3f vector3f = toXYZ();
        return new Vector3f((float)Math.toDegrees(vector3f.x()), (float)Math.toDegrees(vector3f.y()), (float)Math.toDegrees(vector3f.z()));
    }

    public Vector3f toYXZ() {
        float f = r() * r();
        float g = i() * i();
        float h = j() * j();
        float i = k() * k();
        float j = f + g + h + i;
        float k = 2.0f * r() * i() - 2.0f * j() * k();
        float l = (float)Math.asin(k / j);
        if (Math.abs(k) > 0.999f * j) {
            return new Vector3f(l, 2.0f * (float)Math.atan2(j(), r()), 0.0f);
        }
        return new Vector3f(l, (float)Math.atan2(2.0f * i() * k() + 2.0f * j() * r(), f - g - h + i), (float)Math.atan2(2.0f * i() * j() + 2.0f * r() * k(), f - g + h - i));
    }

    public Vector3f toYXZDegrees() {
        Vector3f vector3f = toYXZ();
        return new Vector3f((float)Math.toDegrees(vector3f.x()), (float)Math.toDegrees(vector3f.y()), (float)Math.toDegrees(vector3f.z()));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Quaternion quaternion = (Quaternion)object;
        if (Float.compare(quaternion.i, i) != 0) {
            return false;
        }
        if (Float.compare(quaternion.j, j) != 0) {
            return false;
        }
        if (Float.compare(quaternion.k, k) != 0) {
            return false;
        }
        return Float.compare(quaternion.r, r) == 0;
    }

    public int hashCode() {
        int i = Float.floatToIntBits(this.i);
        i = 31 * i + Float.floatToIntBits(j);
        i = 31 * i + Float.floatToIntBits(k);
        i = 31 * i + Float.floatToIntBits(r);
        return i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Quaternion[").append(r()).append(" + ");
        stringBuilder.append(i()).append("i + ");
        stringBuilder.append(j()).append("j + ");
        stringBuilder.append(k()).append("k]");
        return stringBuilder.toString();
    }

    public float i() {
        return i;
    }

    public float j() {
        return j;
    }

    public float k() {
        return k;
    }

    public float r() {
        return r;
    }

    public void mul(Quaternion quaternion) {
        float f = i();
        float g = j();
        float h = k();
        float i = r();
        float j = quaternion.i();
        float k = quaternion.j();
        float l = quaternion.k();
        float m = quaternion.r();
        this.i = i * j + f * m + g * l - h * k;
        this.j = i * k - f * l + g * m + h * j;
        this.k = i * l + f * k - g * j + h * m;
        r = i * m - f * j - g * k - h * l;
    }

    public void mul(float f) {
        i *= f;
        j *= f;
        k *= f;
        r *= f;
    }

    public void conj() {
        i = -i;
        j = -j;
        k = -k;
    }

    public void set(float f, float g, float h, float i) {
        this.i = f;
        j = g;
        k = h;
        r = i;
    }

    private static float cos(float f) {
        return (float)Math.cos(f);
    }

    private static float sin(float f) {
        return (float)Math.sin(f);
    }

    public void normalize() {
        float f = i() * i() + j() * j() + k() * k() + r() * r();
        if (f > 1.0E-6f) {
            float g = Mth.fastInvSqrt(f);
            i *= g;
            j *= g;
            k *= g;
            r *= g;
        } else {
            i = 0.0f;
            j = 0.0f;
            k = 0.0f;
            r = 0.0f;
        }
    }

    public void slerp(Quaternion quaternion, float f) {
        throw new UnsupportedOperationException();
    }

    public Quaternion copy() {
        return new Quaternion(this);
    }
}

