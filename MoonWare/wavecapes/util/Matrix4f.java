/*
 * Decompiled with CFR 0.150.
 */
package wavecapes.util;

import java.nio.FloatBuffer;

public final class Matrix4f {
    protected float m00;
    protected float m01;
    protected float m02;
    protected float m03;
    protected float m10;
    protected float m11;
    protected float m12;
    protected float m13;
    protected float m20;
    protected float m21;
    protected float m22;
    protected float m23;
    protected float m30;
    protected float m31;
    protected float m32;
    protected float m33;

    public Matrix4f() {
    }

    public Matrix4f(Matrix4f matrix4f) {
        m00 = matrix4f.m00;
        m01 = matrix4f.m01;
        m02 = matrix4f.m02;
        m03 = matrix4f.m03;
        m10 = matrix4f.m10;
        m11 = matrix4f.m11;
        m12 = matrix4f.m12;
        m13 = matrix4f.m13;
        m20 = matrix4f.m20;
        m21 = matrix4f.m21;
        m22 = matrix4f.m22;
        m23 = matrix4f.m23;
        m30 = matrix4f.m30;
        m31 = matrix4f.m31;
        m32 = matrix4f.m32;
        m33 = matrix4f.m33;
    }

    public Matrix4f(Quaternion quaternion) {
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
        m33 = 1.0f;
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

    public boolean isInteger() {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.m30 = 1.0f;
        matrix4f.m31 = 1.0f;
        matrix4f.m32 = 1.0f;
        matrix4f.m33 = 0.0f;
        Matrix4f matrix4f2 = copy();
        matrix4f2.multiply(matrix4f);
        return isInteger(matrix4f2.m00 / matrix4f2.m03) && isInteger(matrix4f2.m10 / matrix4f2.m13) && isInteger(matrix4f2.m20 / matrix4f2.m23) && isInteger(matrix4f2.m01 / matrix4f2.m03) && isInteger(matrix4f2.m11 / matrix4f2.m13) && isInteger(matrix4f2.m21 / matrix4f2.m23) && isInteger(matrix4f2.m02 / matrix4f2.m03) && isInteger(matrix4f2.m12 / matrix4f2.m13) && isInteger(matrix4f2.m22 / matrix4f2.m23);
    }

    private static boolean isInteger(float f) {
        return (double)Math.abs(f - (float)Math.round(f)) <= 1.0E-5;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Matrix4f matrix4f = (Matrix4f)object;
        return Float.compare(matrix4f.m00, m00) == 0 && Float.compare(matrix4f.m01, m01) == 0 && Float.compare(matrix4f.m02, m02) == 0 && Float.compare(matrix4f.m03, m03) == 0 && Float.compare(matrix4f.m10, m10) == 0 && Float.compare(matrix4f.m11, m11) == 0 && Float.compare(matrix4f.m12, m12) == 0 && Float.compare(matrix4f.m13, m13) == 0 && Float.compare(matrix4f.m20, m20) == 0 && Float.compare(matrix4f.m21, m21) == 0 && Float.compare(matrix4f.m22, m22) == 0 && Float.compare(matrix4f.m23, m23) == 0 && Float.compare(matrix4f.m30, m30) == 0 && Float.compare(matrix4f.m31, m31) == 0 && Float.compare(matrix4f.m32, m32) == 0 && Float.compare(matrix4f.m33, m33) == 0;
    }

    public int hashCode() {
        int i = m00 != 0.0f ? Float.floatToIntBits(m00) : 0;
        i = 31 * i + (m01 != 0.0f ? Float.floatToIntBits(m01) : 0);
        i = 31 * i + (m02 != 0.0f ? Float.floatToIntBits(m02) : 0);
        i = 31 * i + (m03 != 0.0f ? Float.floatToIntBits(m03) : 0);
        i = 31 * i + (m10 != 0.0f ? Float.floatToIntBits(m10) : 0);
        i = 31 * i + (m11 != 0.0f ? Float.floatToIntBits(m11) : 0);
        i = 31 * i + (m12 != 0.0f ? Float.floatToIntBits(m12) : 0);
        i = 31 * i + (m13 != 0.0f ? Float.floatToIntBits(m13) : 0);
        i = 31 * i + (m20 != 0.0f ? Float.floatToIntBits(m20) : 0);
        i = 31 * i + (m21 != 0.0f ? Float.floatToIntBits(m21) : 0);
        i = 31 * i + (m22 != 0.0f ? Float.floatToIntBits(m22) : 0);
        i = 31 * i + (m23 != 0.0f ? Float.floatToIntBits(m23) : 0);
        i = 31 * i + (m30 != 0.0f ? Float.floatToIntBits(m30) : 0);
        i = 31 * i + (m31 != 0.0f ? Float.floatToIntBits(m31) : 0);
        i = 31 * i + (m32 != 0.0f ? Float.floatToIntBits(m32) : 0);
        i = 31 * i + (m33 != 0.0f ? Float.floatToIntBits(m33) : 0);
        return i;
    }

    private static int bufferIndex(int i, int j) {
        return j * 4 + i;
    }

    public void load(FloatBuffer floatBuffer) {
        m00 = floatBuffer.get(bufferIndex(0, 0));
        m01 = floatBuffer.get(bufferIndex(0, 1));
        m02 = floatBuffer.get(bufferIndex(0, 2));
        m03 = floatBuffer.get(bufferIndex(0, 3));
        m10 = floatBuffer.get(bufferIndex(1, 0));
        m11 = floatBuffer.get(bufferIndex(1, 1));
        m12 = floatBuffer.get(bufferIndex(1, 2));
        m13 = floatBuffer.get(bufferIndex(1, 3));
        m20 = floatBuffer.get(bufferIndex(2, 0));
        m21 = floatBuffer.get(bufferIndex(2, 1));
        m22 = floatBuffer.get(bufferIndex(2, 2));
        m23 = floatBuffer.get(bufferIndex(2, 3));
        m30 = floatBuffer.get(bufferIndex(3, 0));
        m31 = floatBuffer.get(bufferIndex(3, 1));
        m32 = floatBuffer.get(bufferIndex(3, 2));
        m33 = floatBuffer.get(bufferIndex(3, 3));
    }

    public void loadTransposed(FloatBuffer floatBuffer) {
        m00 = floatBuffer.get(bufferIndex(0, 0));
        m01 = floatBuffer.get(bufferIndex(1, 0));
        m02 = floatBuffer.get(bufferIndex(2, 0));
        m03 = floatBuffer.get(bufferIndex(3, 0));
        m10 = floatBuffer.get(bufferIndex(0, 1));
        m11 = floatBuffer.get(bufferIndex(1, 1));
        m12 = floatBuffer.get(bufferIndex(2, 1));
        m13 = floatBuffer.get(bufferIndex(3, 1));
        m20 = floatBuffer.get(bufferIndex(0, 2));
        m21 = floatBuffer.get(bufferIndex(1, 2));
        m22 = floatBuffer.get(bufferIndex(2, 2));
        m23 = floatBuffer.get(bufferIndex(3, 2));
        m30 = floatBuffer.get(bufferIndex(0, 3));
        m31 = floatBuffer.get(bufferIndex(1, 3));
        m32 = floatBuffer.get(bufferIndex(2, 3));
        m33 = floatBuffer.get(bufferIndex(3, 3));
    }

    public void load(FloatBuffer floatBuffer, boolean bl) {
        if (bl) {
            loadTransposed(floatBuffer);
        } else {
            load(floatBuffer);
        }
    }

    public void load(Matrix4f matrix4f) {
        m00 = matrix4f.m00;
        m01 = matrix4f.m01;
        m02 = matrix4f.m02;
        m03 = matrix4f.m03;
        m10 = matrix4f.m10;
        m11 = matrix4f.m11;
        m12 = matrix4f.m12;
        m13 = matrix4f.m13;
        m20 = matrix4f.m20;
        m21 = matrix4f.m21;
        m22 = matrix4f.m22;
        m23 = matrix4f.m23;
        m30 = matrix4f.m30;
        m31 = matrix4f.m31;
        m32 = matrix4f.m32;
        m33 = matrix4f.m33;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Matrix4f:\n");
        stringBuilder.append(m00);
        stringBuilder.append(" ");
        stringBuilder.append(m01);
        stringBuilder.append(" ");
        stringBuilder.append(m02);
        stringBuilder.append(" ");
        stringBuilder.append(m03);
        stringBuilder.append("\n");
        stringBuilder.append(m10);
        stringBuilder.append(" ");
        stringBuilder.append(m11);
        stringBuilder.append(" ");
        stringBuilder.append(m12);
        stringBuilder.append(" ");
        stringBuilder.append(m13);
        stringBuilder.append("\n");
        stringBuilder.append(m20);
        stringBuilder.append(" ");
        stringBuilder.append(m21);
        stringBuilder.append(" ");
        stringBuilder.append(m22);
        stringBuilder.append(" ");
        stringBuilder.append(m23);
        stringBuilder.append("\n");
        stringBuilder.append(m30);
        stringBuilder.append(" ");
        stringBuilder.append(m31);
        stringBuilder.append(" ");
        stringBuilder.append(m32);
        stringBuilder.append(" ");
        stringBuilder.append(m33);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public void store(FloatBuffer floatBuffer) {
        floatBuffer.put(bufferIndex(0, 0), m00);
        floatBuffer.put(bufferIndex(0, 1), m01);
        floatBuffer.put(bufferIndex(0, 2), m02);
        floatBuffer.put(bufferIndex(0, 3), m03);
        floatBuffer.put(bufferIndex(1, 0), m10);
        floatBuffer.put(bufferIndex(1, 1), m11);
        floatBuffer.put(bufferIndex(1, 2), m12);
        floatBuffer.put(bufferIndex(1, 3), m13);
        floatBuffer.put(bufferIndex(2, 0), m20);
        floatBuffer.put(bufferIndex(2, 1), m21);
        floatBuffer.put(bufferIndex(2, 2), m22);
        floatBuffer.put(bufferIndex(2, 3), m23);
        floatBuffer.put(bufferIndex(3, 0), m30);
        floatBuffer.put(bufferIndex(3, 1), m31);
        floatBuffer.put(bufferIndex(3, 2), m32);
        floatBuffer.put(bufferIndex(3, 3), m33);
    }

    public void storeTransposed(FloatBuffer floatBuffer) {
        floatBuffer.put(bufferIndex(0, 0), m00);
        floatBuffer.put(bufferIndex(1, 0), m01);
        floatBuffer.put(bufferIndex(2, 0), m02);
        floatBuffer.put(bufferIndex(3, 0), m03);
        floatBuffer.put(bufferIndex(0, 1), m10);
        floatBuffer.put(bufferIndex(1, 1), m11);
        floatBuffer.put(bufferIndex(2, 1), m12);
        floatBuffer.put(bufferIndex(3, 1), m13);
        floatBuffer.put(bufferIndex(0, 2), m20);
        floatBuffer.put(bufferIndex(1, 2), m21);
        floatBuffer.put(bufferIndex(2, 2), m22);
        floatBuffer.put(bufferIndex(3, 2), m23);
        floatBuffer.put(bufferIndex(0, 3), m30);
        floatBuffer.put(bufferIndex(1, 3), m31);
        floatBuffer.put(bufferIndex(2, 3), m32);
        floatBuffer.put(bufferIndex(3, 3), m33);
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
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 1.0f;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = 1.0f;
        m23 = 0.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 0.0f;
        m33 = 1.0f;
    }

    public float adjugateAndDet() {
        float f = m00 * m11 - m01 * m10;
        float g = m00 * m12 - m02 * m10;
        float h = m00 * m13 - m03 * m10;
        float i = m01 * m12 - m02 * m11;
        float j = m01 * m13 - m03 * m11;
        float k = m02 * m13 - m03 * m12;
        float l = m20 * m31 - m21 * m30;
        float m = m20 * m32 - m22 * m30;
        float n = m20 * m33 - m23 * m30;
        float o = m21 * m32 - m22 * m31;
        float p = m21 * m33 - m23 * m31;
        float q = m22 * m33 - m23 * m32;
        float r = m11 * q - m12 * p + m13 * o;
        float s = -m10 * q + m12 * n - m13 * m;
        float t = m10 * p - m11 * n + m13 * l;
        float u = -m10 * o + m11 * m - m12 * l;
        float v = -m01 * q + m02 * p - m03 * o;
        float w = m00 * q - m02 * n + m03 * m;
        float x = -m00 * p + m01 * n - m03 * l;
        float y = m00 * o - m01 * m + m02 * l;
        float z = m31 * k - m32 * j + m33 * i;
        float aa = -m30 * k + m32 * h - m33 * g;
        float ab = m30 * j - m31 * h + m33 * f;
        float ac = -m30 * i + m31 * g - m32 * f;
        float ad = -m21 * k + m22 * j - m23 * i;
        float ae = m20 * k - m22 * h + m23 * g;
        float af = -m20 * j + m21 * h - m23 * f;
        float ag = m20 * i - m21 * g + m22 * f;
        m00 = r;
        m10 = s;
        m20 = t;
        m30 = u;
        m01 = v;
        m11 = w;
        m21 = x;
        m31 = y;
        m02 = z;
        m12 = aa;
        m22 = ab;
        m32 = ac;
        m03 = ad;
        m13 = ae;
        m23 = af;
        m33 = ag;
        return f * q - g * p + h * o + i * n - j * m + k * l;
    }

    public float determinant() {
        float f = m00 * m11 - m01 * m10;
        float g = m00 * m12 - m02 * m10;
        float h = m00 * m13 - m03 * m10;
        float i = m01 * m12 - m02 * m11;
        float j = m01 * m13 - m03 * m11;
        float k = m02 * m13 - m03 * m12;
        float l = m20 * m31 - m21 * m30;
        float m = m20 * m32 - m22 * m30;
        float n = m20 * m33 - m23 * m30;
        float o = m21 * m32 - m22 * m31;
        float p = m21 * m33 - m23 * m31;
        float q = m22 * m33 - m23 * m32;
        return f * q - g * p + h * o + i * n - j * m + k * l;
    }

    public void transpose() {
        float f = m10;
        m10 = m01;
        m01 = f;
        f = m20;
        m20 = m02;
        m02 = f;
        f = m21;
        m21 = m12;
        m12 = f;
        f = m30;
        m30 = m03;
        m03 = f;
        f = m31;
        m31 = m13;
        m13 = f;
        f = m32;
        m32 = m23;
        m23 = f;
    }

    public boolean invert() {
        float f = adjugateAndDet();
        if (Math.abs(f) > 1.0E-6f) {
            multiply(f);
            return true;
        }
        return false;
    }

    public void multiply(Matrix4f matrix4f) {
        float f = m00 * matrix4f.m00 + m01 * matrix4f.m10 + m02 * matrix4f.m20 + m03 * matrix4f.m30;
        float g = m00 * matrix4f.m01 + m01 * matrix4f.m11 + m02 * matrix4f.m21 + m03 * matrix4f.m31;
        float h = m00 * matrix4f.m02 + m01 * matrix4f.m12 + m02 * matrix4f.m22 + m03 * matrix4f.m32;
        float i = m00 * matrix4f.m03 + m01 * matrix4f.m13 + m02 * matrix4f.m23 + m03 * matrix4f.m33;
        float j = m10 * matrix4f.m00 + m11 * matrix4f.m10 + m12 * matrix4f.m20 + m13 * matrix4f.m30;
        float k = m10 * matrix4f.m01 + m11 * matrix4f.m11 + m12 * matrix4f.m21 + m13 * matrix4f.m31;
        float l = m10 * matrix4f.m02 + m11 * matrix4f.m12 + m12 * matrix4f.m22 + m13 * matrix4f.m32;
        float m = m10 * matrix4f.m03 + m11 * matrix4f.m13 + m12 * matrix4f.m23 + m13 * matrix4f.m33;
        float n = m20 * matrix4f.m00 + m21 * matrix4f.m10 + m22 * matrix4f.m20 + m23 * matrix4f.m30;
        float o = m20 * matrix4f.m01 + m21 * matrix4f.m11 + m22 * matrix4f.m21 + m23 * matrix4f.m31;
        float p = m20 * matrix4f.m02 + m21 * matrix4f.m12 + m22 * matrix4f.m22 + m23 * matrix4f.m32;
        float q = m20 * matrix4f.m03 + m21 * matrix4f.m13 + m22 * matrix4f.m23 + m23 * matrix4f.m33;
        float r = m30 * matrix4f.m00 + m31 * matrix4f.m10 + m32 * matrix4f.m20 + m33 * matrix4f.m30;
        float s = m30 * matrix4f.m01 + m31 * matrix4f.m11 + m32 * matrix4f.m21 + m33 * matrix4f.m31;
        float t = m30 * matrix4f.m02 + m31 * matrix4f.m12 + m32 * matrix4f.m22 + m33 * matrix4f.m32;
        float u = m30 * matrix4f.m03 + m31 * matrix4f.m13 + m32 * matrix4f.m23 + m33 * matrix4f.m33;
        m00 = f;
        m01 = g;
        m02 = h;
        m03 = i;
        m10 = j;
        m11 = k;
        m12 = l;
        m13 = m;
        m20 = n;
        m21 = o;
        m22 = p;
        m23 = q;
        m30 = r;
        m31 = s;
        m32 = t;
        m33 = u;
    }

    public void multiply(Quaternion quaternion) {
        multiply(new Matrix4f(quaternion));
    }

    public void multiply(float f) {
        m00 *= f;
        m01 *= f;
        m02 *= f;
        m03 *= f;
        m10 *= f;
        m11 *= f;
        m12 *= f;
        m13 *= f;
        m20 *= f;
        m21 *= f;
        m22 *= f;
        m23 *= f;
        m30 *= f;
        m31 *= f;
        m32 *= f;
        m33 *= f;
    }

    public void add(Matrix4f matrix4f) {
        m00 += matrix4f.m00;
        m01 += matrix4f.m01;
        m02 += matrix4f.m02;
        m03 += matrix4f.m03;
        m10 += matrix4f.m10;
        m11 += matrix4f.m11;
        m12 += matrix4f.m12;
        m13 += matrix4f.m13;
        m20 += matrix4f.m20;
        m21 += matrix4f.m21;
        m22 += matrix4f.m22;
        m23 += matrix4f.m23;
        m30 += matrix4f.m30;
        m31 += matrix4f.m31;
        m32 += matrix4f.m32;
        m33 += matrix4f.m33;
    }

    public void subtract(Matrix4f matrix4f) {
        m00 -= matrix4f.m00;
        m01 -= matrix4f.m01;
        m02 -= matrix4f.m02;
        m03 -= matrix4f.m03;
        m10 -= matrix4f.m10;
        m11 -= matrix4f.m11;
        m12 -= matrix4f.m12;
        m13 -= matrix4f.m13;
        m20 -= matrix4f.m20;
        m21 -= matrix4f.m21;
        m22 -= matrix4f.m22;
        m23 -= matrix4f.m23;
        m30 -= matrix4f.m30;
        m31 -= matrix4f.m31;
        m32 -= matrix4f.m32;
        m33 -= matrix4f.m33;
    }

    public float trace() {
        return m00 + m11 + m22 + m33;
    }

    public static Matrix4f perspective(double d, float f, float g, float h) {
        float i = (float)(1.0 / Math.tan(d * 0.01745329238474369 / 2.0));
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.m00 = i / f;
        matrix4f.m11 = i;
        matrix4f.m22 = (h + g) / (g - h);
        matrix4f.m32 = -1.0f;
        matrix4f.m23 = 2.0f * h * g / (g - h);
        return matrix4f;
    }

    public static Matrix4f orthographic(float f, float g, float h, float i) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.m00 = 2.0f / f;
        matrix4f.m11 = 2.0f / g;
        float j = i - h;
        matrix4f.m22 = -2.0f / j;
        matrix4f.m33 = 1.0f;
        matrix4f.m03 = -1.0f;
        matrix4f.m13 = 1.0f;
        matrix4f.m23 = -(i + h) / j;
        return matrix4f;
    }

    public static Matrix4f orthographic(float f, float g, float h, float i, float j, float k) {
        Matrix4f matrix4f = new Matrix4f();
        float l = g - f;
        float m = h - i;
        float n = k - j;
        matrix4f.m00 = 2.0f / l;
        matrix4f.m11 = 2.0f / m;
        matrix4f.m22 = -2.0f / n;
        matrix4f.m03 = -(g + f) / l;
        matrix4f.m13 = -(h + i) / m;
        matrix4f.m23 = -(k + j) / n;
        matrix4f.m33 = 1.0f;
        return matrix4f;
    }

    public void translate(Vector3f vector3f) {
        m03 += vector3f.x();
        m13 += vector3f.y();
        m23 += vector3f.z();
    }

    public Matrix4f copy() {
        return new Matrix4f(this);
    }

    public void multiplyWithTranslation(float f, float g, float h) {
        m03 = m00 * f + m01 * g + m02 * h + m03;
        m13 = m10 * f + m11 * g + m12 * h + m13;
        m23 = m20 * f + m21 * g + m22 * h + m23;
        m33 = m30 * f + m31 * g + m32 * h + m33;
    }

    public static Matrix4f createScaleMatrix(float f, float g, float h) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.m00 = f;
        matrix4f.m11 = g;
        matrix4f.m22 = h;
        matrix4f.m33 = 1.0f;
        return matrix4f;
    }

    public static Matrix4f createTranslateMatrix(float f, float g, float h) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.m00 = 1.0f;
        matrix4f.m11 = 1.0f;
        matrix4f.m22 = 1.0f;
        matrix4f.m33 = 1.0f;
        matrix4f.m03 = f;
        matrix4f.m13 = g;
        matrix4f.m23 = h;
        return matrix4f;
    }
}

