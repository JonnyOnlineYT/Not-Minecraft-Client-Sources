package org.spray.infinity.utils.render;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class Vertexer {

	public static void vertexBoxQuads(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, double alpha,
			int color) {
		float x1 = (float) box.minX;
		float y1 = (float) box.minY;
		float z1 = (float) box.minZ;
		float x2 = (float) box.maxX;
		float y2 = (float) box.maxY;
		float z2 = (float) box.maxZ;

		vertexQuad(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, alpha, color);
		vertexQuad(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, x1, y2, z1, x1, y1, z1, alpha, color);
		vertexQuad(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2, alpha, color);
		vertexQuad(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, alpha, color);
		vertexQuad(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, x1, y2, z2, x1, y1, z2, alpha, color);
		vertexQuad(matrices, vertexConsumer, x1, y2, z2, x2, y2, z2, x2, y2, z1, x1, y2, z1, alpha, color);
	}

	public static void vertexQuad(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1,
			float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, double alpha,
			int color) {
		float g = (float) (color >> 16 & 255) / 255.0F;
		float h = (float) (color >> 8 & 255) / 255.0F;
		float k = (float) (color & 255) / 255.0F;

		vertexConsumer.vertex(matrices.peek().getModel(), x1, y1, z1).color(g, h, k, (float) alpha).next();
		vertexConsumer.vertex(matrices.peek().getModel(), x2, y2, z2).color(g, h, k, (float) alpha).next();
		vertexConsumer.vertex(matrices.peek().getModel(), x3, y3, z3).color(g, h, k, (float) alpha).next();
		vertexConsumer.vertex(matrices.peek().getModel(), x4, y4, z4).color(g, h, k, (float) alpha).next();

	}

	public static void vertexBoxLines(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, int color) {
		float x1 = (float) box.minX;
		float y1 = (float) box.minY;
		float z1 = (float) box.minZ;
		float x2 = (float) box.maxX;
		float y2 = (float) box.maxY;
		float z2 = (float) box.maxZ;

		vertexLine(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, color);
		vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y1, z2, color);
		vertexLine(matrices, vertexConsumer, x2, y1, z2, x1, y1, z2, color);
		vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y1, z1, color);

		vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, color);
		vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, color);

		vertexLine(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, color);
		vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, color);

		vertexLine(matrices, vertexConsumer, x1, y2, z1, x2, y2, z1, color);
		vertexLine(matrices, vertexConsumer, x2, y2, z1, x2, y2, z2, color);
		vertexLine(matrices, vertexConsumer, x2, y2, z2, x1, y2, z2, color);
		vertexLine(matrices, vertexConsumer, x1, y2, z2, x1, y2, z1, color);
	}

	public static void vertexLine(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1,
			float x2, float y2, float z2, int color) {
		float f = (float) (color >> 24 & 255) / 255.0F;
		float g = (float) (color >> 16 & 255) / 255.0F;
		float h = (float) (color >> 8 & 255) / 255.0F;
		float k = (float) (color & 255) / 255.0F;

		Matrix4f model = matrices.peek().getModel();
		Matrix3f normal = matrices.peek().getNormal();

		Vec3f normalVec = getNormal(normal, x1, y1, z1, x2, y2, z2);

		vertexConsumer.vertex(model, x1, y1, z1).color(g, h, k, f)
				.normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
		vertexConsumer.vertex(model, x2, y2, z2).color(g, h, k, f)
				.normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
	}

	public static Vec3f getNormal(Matrix3f normal, float x1, float y1, float z1, float x2, float y2, float z2) {
		float xNormal = x2 - x1;
		float yNormal = y2 - y1;
		float zNormal = z2 - z1;
		float normalSqrt = MathHelper.sqrt(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal);

		return new Vec3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
	}

}
