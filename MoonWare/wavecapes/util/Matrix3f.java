/*
 * Decompiled with CFR 0.150.
 */
package wavecapes.util;

import java.nio.FloatBuffer;

public final class Matrix3f {
    protected float m00;
    protected float m01;
    protected float m02;
    protected float m10;
    protected float m11;
    protected float m12;
    protected float m20;
    protected float m21;
    protected float m22;

    public Matrix3f() {
    }

    public Matrix3f(Quaternion quaternion) {
        float f = quaternion.i();
        float g = quaternion.j();
        float h = quaternion.k();
        float i = quaternion.r();
        float j = 2.0f * f * f;
        float k = 2.0f * g * g;
        float l = 2.0f * h * h;
        m00 = 1.0f - k - l;
        m11 = 1.0f - l - j;
        m22 = 1.0f - j - k;
        float m = f * g;
        float n = g * h;
        float o = h * f;
        float p = f * i;
        float q = g * i;
        float r = h * i;
        m10 = 2.0f * (m + r);
        m01 = 2.0f * (m - r);
        m20 = 2.0f * (o - q);
        m02 = 2.0f * (o + q);
        m21 = 2.0f * (n + p);
        m12 = 2.0f * (n - p);
    }

    public static Matrix3f createScaleMatrix(float f, float g, float h) {
        Matrix3f matrix3f = new Matrix3f();
        matrix3f.m00 = f;
        matrix3f.m11 = g;
        matrix3f.m22 = h;
        return matrix3f;
    }

    public Matrix3f(Matrix4f matrix4f) {
        m00 = matrix4f.m00;
        m01 = matrix4f.m01;
        m02 = matrix4f.m02;
        m10 = matrix4f.m10;
        m11 = matrix4f.m11;
        m12 = matrix4f.m12;
        m20 = matrix4f.m20;
        m21 = matrix4f.m21;
        m22 = matrix4f.m22;
    }

    public Matrix3f(Matrix3f matrix3f) {
        m00 = matrix3f.m00;
        m01 = matrix3f.m01;
        m02 = matrix3f.m02;
        m10 = matrix3f.m10;
        m11 = matrix3f.m11;
        m12 = matrix3f.m12;
        m20 = matrix3f.m20;
        m21 = matrix3f.m21;
        m22 = matrix3f.m22;
    }

    public void transpose() {
        float f = m01;
        m01 = m10;
        m10 = f;
        f = m02;
        m02 = m20;
        m20 = f;
        f = m12;
        m12 = m21;
        m21 = f;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Matrix3f matrix3f = (Matrix3f)object;
        return Float.compare(matrix3f.m00, m00) == 0 && Float.compare(matrix3f.m01, m01) == 0 && Float.compare(matrix3f.m02, m02) == 0 && Float.compare(matrix3f.m10, m10) == 0 && Float.compare(matrix3f.m11, m11) == 0 && Float.compare(matrix3f.m12, m12) == 0 && Float.compare(matrix3f.m20, m20) == 0 && Float.compare(matrix3f.m21, m21) == 0 && Float.compare(matrix3f.m22, m22) == 0;
    }

    public int hashCode() {
        int i = m00 != 0.0f ? Float.floatToIntBits(m00) : 0;
        i = 31 * i + (m01 != 0.0f ? Float.floatToIntBits(m01) : 0);
        i = 31 * i + (m02 != 0.0f ? Float.floatToIntBits(m02) : 0);
        i = 31 * i + (m10 != 0.0f ? Float.floatToIntBits(m10) : 0);
        i = 31 * i + (m11 != 0.0f ? Float.floatToIntBits(m11) : 0);
        i = 31 * i + (m12 != 0.0f ? Float.floatToIntBits(m12) : 0);
        i = 31 * i + (m20 != 0.0f ? Float.floatToIntBits(m20) : 0);
        i = 31 * i + (m21 != 0.0f ? Float.floatToIntBits(m21) : 0);
        i = 31 * i + (m22 != 0.0f ? Float.floatToIntBits(m22) : 0);
        return i;
    }

    private static int bufferIndex(int i, int j) {
        return j * 3 + i;
    }

    public void load(FloatBuffer floatBuffer) {
        m00 = floatBuffer.get(bufferIndex(0, 0));
        m01 = floatBuffer.get(bufferIndex(0, 1));
        m02 = floatBuffer.get(bufferIndex(0, 2));
        m10 = floatBuffer.get(bufferIndex(1, 0));
        m11 = floatBuffer.get(bufferIndex(1, 1));
        m12 = floatBuffer.get(bufferIndex(1, 2));
        m20 = floatBuffer.get(bufferIndex(2, 0));
        m21 = floatBuffer.get(bufferIndex(2, 1));
        m22 = floatBuffer.get(bufferIndex(2, 2));
    }

    public void loadTransposed(FloatBuffer floatBuffer) {
        m00 = floatBuffer.get(bufferIndex(0, 0));
        m01 = floatBuffer.get(bufferIndex(1, 0));
        m02 = floatBuffer.get(bufferIndex(2, 0));
        m10 = floatBuffer.get(bufferIndex(0, 1));
        m11 = floatBuffer.get(bufferIndex(1, 1));
        m12 = floatBuffer.get(bufferIndex(2, 1));
        m20 = floatBuffer.get(bufferIndex(0, 2));
        m21 = floatBuffer.get(bufferIndex(1, 2));
        m22 = floatBuffer.get(bufferIndex(2, 2));
    }

    public void load(FloatBuffer floatBuffer, boolean bl) {
        if (bl) {
            loadTransposed(floatBuffer);
        } else {
            load(floatBuffer);
        }
    }

    public void load(Matrix3f matrix3f) {
        m00 = matrix3f.m00;
        m01 = matrix3f.m01;
        m02 = matrix3f.m02;
        m10 = matrix3f.m10;
        m11 = matrix3f.m11;
        m12 = matrix3f.m12;
        m20 = matrix3f.m20;
        m21 = matrix3f.m21;
        m22 = matrix3f.m22;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Matrix3f:\n");
        stringBuilder.append(m00);
        stringBuilder.append(" ");
        stringBuilder.append(m01);
        stringBuilder.append(" ");
        stringBuilder.append(m02);
        stringBuilder.append("\n");
        stringBuilder.append(m10);
        stringBuilder.append(" ");
        stringBuilder.append(m11);
        stringBuilder.append(" ");
        stringBuilder.append(m12);
        stringBuilder.append("\n");
        stringBuilder.append(m20);
        stringBuilder.append(" ");
        stringBuilder.append(m21);
        stringBuilder.append(" ");
        stringBuilder.append(m22);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public void store(FloatBuffer floatBuffer) {
        floatBuffer.put(bufferIndex(0, 0), m00);
        floatBuffer.put(bufferIndex(0, 1), m01);
        floatBuffer.put(bufferIndex(0, 2), m02);
        floatBuffer.put(bufferIndex(1, 0), m10);
        floatBuffer.put(bufferIndex(1, 1), m11);
        floatBuffer.put(bufferIndex(1, 2), m12);
        floatBuffer.put(bufferIndex(2, 0), m20);
        floatBuffer.put(bufferIndex(2, 1), m21);
        floatBuffer.put(bufferIndex(2, 2), m22);
    }

    public void storeTransposed(FloatBuffer floatBuffer) {
        floatBuffer.put(bufferIndex(0, 0), m00);
        floatBuffer.put(bufferIndex(1, 0), m01);
        floatBuffer.put(bufferIndex(2, 0), m02);
        floatBuffer.put(bufferIndex(0, 1), m10);
        floatBuffer.put(bufferIndex(1, 1), m11);
        floatBuffer.put(bufferIndex(2, 1), m12);
        floatBuffer.put(bufferIndex(0, 2), m20);
        floatBuffer.put(bufferIndex(1, 2), m21);
        floatBuffer.put(bufferIndex(2, 2), m22);
    }

    public void store(FloatBuffer floatBuffer, boolean bl) {
        if (bl) {
            storeTransposed(floatBuffer);
        } else {
            store(floatBuffer);
        }
    }

    public void setIdentity() {
        m00 = 1.0f;
        m01 = 0.0f;
        m02 = 0.0f;
        m10 = 0.0f;
        m11 = 1.0f;
        m12 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = 1.0f;
    }

    public float adjugateAndDet() {
        float f = m11 * m22 - m12 * m21;
        float g = -(m10 * m22 - m12 * m20);
        float h = m10 * m21 - m11 * m20;
        float i = -(m01 * m22 - m02 * m21);
        float j = m00 * m22 - m02 * m20;
        float k = -(m00 * m21 - m01 * m20);
        float l = m01 * m12 - m02 * m11;
        float m = -(m00 * m12 - m02 * m10);
        float n = m00 * m11 - m01 * m10;
        float o = m00 * f + m01 * g + m02 * h;
        m00 = f;
        m10 = g;
        m20 = h;
        m01 = i;
        m11 = j;
        m21 = k;
        m02 = l;
        m12 = m;
        m22 = n;
        return o;
    }

    public float determinant() {
        float f = m11 * m22 - m12 * m21;
        float g = -(m10 * m22 - m12 * m20);
        float h = m10 * m21 - m11 * m20;
        return m00 * f + m01 * g + m02 * h;
    }

    public boolean invert() {
        float f = adjugateAndDet();
        if (Math.abs(f) > 1.0E-6f) {
            mul(f);
            return true;
        }
        return false;
    }

    public void set(int i, int j, float f) {
        if (i == 0) {
            if (j == 0) {
                m00 = f;
            } else if (j == 1) {
                m01 = f;
            } else {
                m02 = f;
            }
        } else if (i == 1) {
            if (j == 0) {
                m10 = f;
            } else if (j == 1) {
                m11 = f;
            } else {
                m12 = f;
            }
        } else if (j == 0) {
            m20 = f;
        } else if (j == 1) {
            m21 = f;
        } else {
            m22 = f;
        }
    }

    public void mul(Matrix3f matrix3f) {
        float f = m00 * matrix3f.m00 + m01 * matrix3f.m10 + m02 * matrix3f.m20;
        float g = m00 * matrix3f.m01 + m01 * matrix3f.m11 + m02 * matrix3f.m21;
        float h = m00 * matrix3f.m02 + m01 * matrix3f.m12 + m02 * matrix3f.m22;
        float i = m10 * matrix3f.m00 + m11 * matrix3f.m10 + m12 * matrix3f.m20;
        float j = m10 * matrix3f.m01 + m11 * matrix3f.m11 + m12 * matrix3f.m21;
        float k = m10 * matrix3f.m02 + m11 * matrix3f.m12 + m12 * matrix3f.m22;
        float l = m20 * matrix3f.m00 + m21 * matrix3f.m10 + m22 * matrix3f.m20;
        float m = m20 * matrix3f.m01 + m21 * matrix3f.m11 + m22 * matrix3f.m21;
        float n = m20 * matrix3f.m02 + m21 * matrix3f.m12 + m22 * matrix3f.m22;
        m00 = f;
        m01 = g;
        m02 = h;
        m10 = i;
        m11 = j;
        m12 = k;
        m20 = l;
        m21 = m;
        m22 = n;
    }

    public void mul(Quaternion quaternion) {
        mul(new Matrix3f(quaternion));
    }

    public void mul(float f) {
        m00 *= f;
        m01 *= f;
        m02 *= f;
        m10 *= f;
        m11 *= f;
        m12 *= f;
        m20 *= f;
        m21 *= f;
        m22 *= f;
    }

    public void add(Matrix3f matrix3f) {
        m00 += matrix3f.m00;
        m01 += matrix3f.m01;
        m02 += matrix3f.m02;
        m10 += matrix3f.m10;
        m11 += matrix3f.m11;
        m12 += matrix3f.m12;
        m20 += matrix3f.m20;
        m21 += matrix3f.m21;
        m22 += matrix3f.m22;
    }

    public void sub(Matrix3f matrix3f) {
        m00 -= matrix3f.m00;
        m01 -= matrix3f.m01;
        m02 -= matrix3f.m02;
        m10 -= matrix3f.m10;
        m11 -= matrix3f.m11;
        m12 -= matrix3f.m12;
        m20 -= matrix3f.m20;
        m21 -= matrix3f.m21;
        m22 -= matrix3f.m22;
    }

    public float trace() {
        return m00 + m11 + m22;
    }

    public Matrix3f copy() {
        return new Matrix3f(this);
    }
}

