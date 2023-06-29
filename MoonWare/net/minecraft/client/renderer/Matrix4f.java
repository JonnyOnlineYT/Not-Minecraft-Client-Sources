package net.minecraft.client.renderer;

public class Matrix4f extends org.lwjgl.util.vector.Matrix4f
{
    public Matrix4f(float[] matrix)
    {
        m00 = matrix[0];
        m01 = matrix[1];
        m02 = matrix[2];
        m03 = matrix[3];
        m10 = matrix[4];
        m11 = matrix[5];
        m12 = matrix[6];
        m13 = matrix[7];
        m20 = matrix[8];
        m21 = matrix[9];
        m22 = matrix[10];
        m23 = matrix[11];
        m30 = matrix[12];
        m31 = matrix[13];
        m32 = matrix[14];
        m33 = matrix[15];
    }

    public Matrix4f()
    {
        m00 = 0.0F;
        m01 = 0.0F;
        m02 = 0.0F;
        m03 = 0.0F;
        m10 = 0.0F;
        m11 = 0.0F;
        m12 = 0.0F;
        m13 = 0.0F;
        m20 = 0.0F;
        m21 = 0.0F;
        m22 = 0.0F;
        m23 = 0.0F;
        m30 = 0.0F;
        m31 = 0.0F;
        m32 = 0.0F;
        m33 = 0.0F;
    }

    public void translate(float x, float y, float v) {
    }
}
