package baguchan.enchantwithmob.mixins.client;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.client.render.EnchantRenderType;
import baguchan.enchantwithmob.client.render.layer.EnchantLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ArrowRenderer.class, remap = false)
public class ArrowRendererMixin<T extends AbstractArrow> {

	@Inject(method = "render", at = @At("TAIL"))
	public void render(T p_113839_, float p_113840_, float p_113841_, PoseStack p_113842_, MultiBufferSource p_113843_, int p_113844_, CallbackInfo callbackInfo) {
		if (p_113839_.getOwner() != null) {
            if (p_113839_.getOwner() instanceof IEnchantCap cap) {
                if (cap.getEnchantCap().hasEnchant()) {
                    p_113842_.pushPose();
                    p_113842_.mulPose(Axis.YP.rotationDegrees(Mth.lerp(p_113841_, p_113839_.yRotO, p_113839_.getYRot()) - 90.0F));
                    p_113842_.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(p_113841_, p_113839_.xRotO, p_113839_.getXRot())));
                    int i = 0;
                    float f = 0.0F;
                    float f1 = 0.5F;
                    float f2 = 0.0F;
                    float f3 = 0.15625F;
                    float f4 = 0.0F;
                    float f5 = 0.15625F;
					float f6 = 0.15625F;
					float f7 = 0.3125F;
					float f8 = 0.05625F;
					float f9 = (float) p_113839_.shakeTime - p_113841_;
					if (f9 > 0.0F) {
						float f10 = -Mth.sin(f9 * 3.0F) * f9;
						p_113842_.mulPose(Axis.ZP.rotationDegrees(f10));
					}

					p_113842_.mulPose(Axis.XP.rotationDegrees(45.0F));
					p_113842_.scale(0.05625F, 0.05625F, 0.05625F);
					p_113842_.translate(-4.0F, 0.0F, 0.0F);
                    VertexConsumer vertexconsumer = p_113843_.getBuffer(EnchantRenderType.enchantSwirl(cap.getEnchantCap().isAncient() ? EnchantLayer.ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ENTITY));
					PoseStack.Pose posestack$pose = p_113842_.last();
					Matrix4f matrix4f = posestack$pose.pose();
					Matrix3f matrix3f = posestack$pose.normal();
					this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, p_113844_);
					this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, p_113844_);
					this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, p_113844_);
					this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, p_113844_);
					this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, p_113844_);
					this.vertex(matrix4f, matrix3f, vertexconsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, p_113844_);
					this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, p_113844_);
					this.vertex(matrix4f, matrix3f, vertexconsumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, p_113844_);

                    for (int j = 0; j < 4; ++j) {
                        p_113842_.mulPose(Axis.XP.rotationDegrees(90.0F));
						this.vertex(matrix4f, matrix3f, vertexconsumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, p_113844_);
						this.vertex(matrix4f, matrix3f, vertexconsumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, p_113844_);
						this.vertex(matrix4f, matrix3f, vertexconsumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, p_113844_);
						this.vertex(matrix4f, matrix3f, vertexconsumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, p_113844_);
                    }

                    p_113842_.popPose();
                }
            }
            ;
        }
	}

	public void vertex(Matrix4f p_254392_, Matrix3f p_254011_, VertexConsumer p_253902_, int p_254058_, int p_254338_, int p_254196_, float p_254003_, float p_254165_, int p_253982_, int p_254037_, int p_254038_, int p_254271_) {
		p_253902_.vertex(p_254392_, (float) p_254058_, (float) p_254338_, (float) p_254196_).color(255, 255, 255, 255).uv(p_254003_, p_254165_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_254271_).normal(p_254011_, (float) p_253982_, (float) p_254038_, (float) p_254037_).endVertex();
	}
}
