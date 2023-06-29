/*
 * Decompiled with CFR 0.150.
 */
package wavecapes.util;

import com.google.common.collect.Queues;
import java.util.Deque;

public class PoseStack {
    private final Deque<Pose> poseStack = Queues.newArrayDeque();

    public PoseStack() {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        Matrix3f matrix3f = new Matrix3f();
        matrix3f.setIdentity();
        poseStack.add(new Pose(matrix4f, matrix3f));
    }

    public void translate(double d, double e, double f) {
        Pose pose = poseStack.getLast();
        pose.pose.multiplyWithTranslation((float)d, (float)e, (float)f);
    }

    public void scale(float f, float g, float h) {
        Pose pose = poseStack.getLast();
        pose.pose.multiply(Matrix4f.createScaleMatrix(f, g, h));
        if (f == g && g == h) {
            if (f > 0.0f) {
                return;
            }
            pose.normal.mul(-1.0f);
        }
        float i = 1.0f / f;
        float j = 1.0f / g;
        float k = 1.0f / h;
        float l = Mth.fastInvCubeRoot(i * j * k);
        pose.normal.mul(Matrix3f.createScaleMatrix(l * i, l * j, l * k));
    }

    public void mulPose(Quaternion quaternion) {
        Pose pose = poseStack.getLast();
        pose.pose.multiply(quaternion);
        pose.normal.mul(quaternion);
    }

    public void pushPose() {
        Pose pose = poseStack.getLast();
        poseStack.addLast(new Pose(pose.pose.copy(), pose.normal.copy()));
    }

    public void popPose() {
        poseStack.removeLast();
    }

    public Pose last() {
        return poseStack.getLast();
    }

    public boolean clear() {
        return poseStack.size() == 1;
    }

    public void setIdentity() {
        Pose pose = poseStack.getLast();
        pose.pose.setIdentity();
        pose.normal.setIdentity();
    }

    public void mulPoseMatrix(Matrix4f matrix4f) {
        poseStack.getLast().pose.multiply(matrix4f);
    }

    public static final class Pose {
        final Matrix4f pose;
        final Matrix3f normal;

        Pose(Matrix4f matrix4f, Matrix3f matrix3f) {
            pose = matrix4f;
            normal = matrix3f;
        }

        public Matrix4f pose() {
            return pose;
        }

        public Matrix3f normal() {
            return normal;
        }
    }
}

