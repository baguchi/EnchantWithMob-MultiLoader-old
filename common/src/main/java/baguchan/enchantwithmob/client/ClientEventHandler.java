package baguchan.enchantwithmob.client;

import baguchan.enchantwithmob.capability.MobEnchantCapability;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static baguchan.enchantwithmob.client.render.EnchantRenderType.enchantBeamSwirl;
import static baguchan.enchantwithmob.client.render.layer.EnchantLayer.ANCIENT_GLINT;

/*
 * Base from Bumble Zone Lazer Layer
 * https://github.com/TelepathicGrunt/Bumblezone/blob/1.20-Arch/common/src/main/java/com/telepathicgrunt/the_bumblezone/client/rendering/cosmiccrystal/CosmicCrystalRenderer.java
 */
public class ClientEventHandler {


    public static Vec3 getPosition(Entity p_114803_, double p_114804_, float p_114805_) {
        double d0 = Mth.lerp(p_114805_, p_114803_.xOld, p_114803_.getX());
        double d1 = Mth.lerp(p_114805_, p_114803_.yOld, p_114803_.getY()) + p_114804_;
        double d2 = Mth.lerp(p_114805_, p_114803_.zOld, p_114803_.getZ());
        return new Vec3(d0, d1, d2);
    }

    public static float getXRotD(LivingEntity livingEntity, Entity target, float totalTick) {
        double d0 = target.getPosition(totalTick).x - livingEntity.getPosition(totalTick).x;
        double d1 = target.getPosition(totalTick).y - livingEntity.getPosition(totalTick).y;
        double d2 = target.getPosition(totalTick).z - livingEntity.getPosition(totalTick).z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return (float) (-(Mth.atan2(d1, d3) * (double) (180F / (float) Math.PI)));
    }

    public static float getYRotD(LivingEntity livingEntity, Entity target, float totalTick) {
        double d0 = target.getPosition(totalTick).x - livingEntity.getPosition(totalTick).x;
        double d1 = target.getPosition(totalTick).z - livingEntity.getPosition(totalTick).z;
        return (float) (Mth.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
    }

    public static <T extends LivingEntity, M extends EntityModel<T>> void renderBeam(@NotNull MobEnchantCapability cap, LivingEntity livingEntity, float totalTickTime, PoseStack poseStack, MultiBufferSource multiBufferSource, Entity target) {

        poseStack.pushPose();

        double d3 = Mth.lerp(totalTickTime, livingEntity.xo, livingEntity.getX());
        double d4 = Mth.lerp(totalTickTime, livingEntity.yo, livingEntity.getY()) + livingEntity.getBbHeight() * 0.5F;
        double d5 = Mth.lerp(totalTickTime, livingEntity.zo, livingEntity.getZ());
        Vec3 vector3d = target.getEyePosition(totalTickTime);
        Vec3 vec31 = new Vec3(d3, d4, d5);
        Vec3 vec32 = vector3d.subtract(vec31);
        float q = 0 * 0.05f * -1.5f;
        float v = 0.2f;
        float w2 = 0.5f;
        float length = (float) (vec32.length());
        float uv2 = -1.0f + (0 * -0.2f % 1.0f);
        float uv1 = length * 2.5f + uv2;
        float x1 = Mth.cos(q + (float) Math.PI) * v;
        float z1 = Mth.sin(q + (float) Math.PI) * v;
        float x2 = Mth.cos(q + 0.0f) * v;
        float z2 = Mth.sin(q + 0.0f) * v;
        float x3 = Mth.cos(q + 1.5707964f) * v;
        float z3 = Mth.sin(q + 1.5707964f) * v;
        float x4 = Mth.cos(q + 4.712389f) * v;
        float z4 = Mth.sin(q + 4.712389f) * v;
        float y1 = length;
        float y2 = 0.0f;
        float ux1 = 0.4999f;
        float ux2 = 0.0f;

        poseStack.translate(0, +livingEntity.getBbHeight() * 0.5F, 0);
        int j1;

        VertexConsumer consumer = multiBufferSource.getBuffer(enchantBeamSwirl(cap.isAncient() ? ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ENTITY));

        float xRot = getXRotD(livingEntity, target, totalTickTime);
        float yRot = getYRotD(livingEntity, target, totalTickTime);


        poseStack.mulPose(Axis.YP.rotationDegrees(-yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        float intensity = cap.getMobEnchants().size() < 3 ? ((float) cap.getMobEnchants().size() / 3) : 3;
        int intensityCacl = (int) (intensity / 3 * 255);
        vertex(consumer, matrix4f, matrix3f, x1, y1, z1, 255, 255, 255, intensityCacl, ux1, uv1);
        vertex(consumer, matrix4f, matrix3f, x1, y2, z1, 255, 255, 255, intensityCacl, ux1, uv2);
        vertex(consumer, matrix4f, matrix3f, x2, y2, z2, 255, 255, 255, intensityCacl, ux2, uv2);
        vertex(consumer, matrix4f, matrix3f, x2, y1, z2, 255, 255, 255, intensityCacl, ux2, uv1);
        vertex(consumer, matrix4f, matrix3f, x3, y1, z3, 255, 255, 255, intensityCacl, ux1, uv1);
        vertex(consumer, matrix4f, matrix3f, x3, y2, z3, 255, 255, 255, intensityCacl, ux1, uv2);
        vertex(consumer, matrix4f, matrix3f, x4, y2, z4, 255, 255, 255, intensityCacl, ux2, uv2);
        vertex(consumer, matrix4f, matrix3f, x4, y1, z4, 255, 255, 255, intensityCacl, ux2, uv1);

        poseStack.popPose();
    }


    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, int red, int green, int blue, int alpha, float ux, float uz) {
        vertexConsumer
                .vertex(matrix4f, x, y, z)
                .color(red, green, blue, 255)
                .uv(ux, uz)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(0xF000F0)
                .normal(matrix3f, 0.0f, 1.0f, 0.0f)
                .endVertex();
    }

    private static void setupGlintTexturing(float p_110187_) {
        long i = Util.getMillis() * 8L;
        float f = (float) (i % 110000L) / 110000.0F;
        float f1 = (float) (i % 30000L) / 30000.0F;
        Matrix4f matrix4f = (new Matrix4f()).translation(-f, f1, 0.0F);
        matrix4f.rotateZ(0.17453292F).scale(p_110187_);
        RenderSystem.setTextureMatrix(matrix4f);
    }
}
