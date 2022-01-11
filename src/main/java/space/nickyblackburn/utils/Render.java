package space.nickyblackburn.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

public class Render {
    

   public static void renderLineBox(PoseStack p_109622_, VertexConsumer p_109623_, double p_109624_, double p_109625_, double p_109626_, double p_109627_, double p_109628_, double p_109629_, float p_109630_, float p_109631_, float p_109632_, float p_109633_, float p_109634_, float p_109635_, float p_109636_) {
    Matrix4f matrix4f = p_109622_.last().pose();
    Matrix3f matrix3f = p_109622_.last().normal();
    float f = (float)p_109624_;
    float f1 = (float)p_109625_;
    float f2 = (float)p_109626_;
    float f3 = (float)p_109627_;
    float f4 = (float)p_109628_;
    float f5 = (float)p_109629_;
    p_109623_.vertex(matrix4f, f, f1, f2).color(p_109630_, p_109635_, p_109636_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f1, f2).color(p_109630_, p_109635_, p_109636_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f, f1, f2).color(p_109634_, p_109631_, p_109636_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f, f4, f2).color(p_109634_, p_109631_, p_109636_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f, f1, f2).color(p_109634_, p_109635_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
    p_109623_.vertex(matrix4f, f, f1, f5).color(p_109634_, p_109635_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f1, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
    p_109623_.vertex(matrix4f, f, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
    p_109623_.vertex(matrix4f, f, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f1, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
    p_109623_.vertex(matrix4f, f, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f1, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f4, f2).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
    p_109623_.vertex(matrix4f, f3, f4, f5).color(p_109630_, p_109631_, p_109632_, p_109633_).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
 }
}
