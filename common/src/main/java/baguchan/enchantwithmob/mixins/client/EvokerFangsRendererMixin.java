package baguchan.enchantwithmob.mixins.client;

import baguchan.enchantwithmob.api.IEnchantVisual;
import baguchan.enchantwithmob.client.render.layer.EnchantLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EvokerFangsRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.projectile.EvokerFangs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static baguchan.enchantwithmob.client.render.EnchantRenderType.enchantSwirl;

@Mixin(value = EvokerFangsRenderer.class, remap = false)
public class EvokerFangsRendererMixin {

	@Shadow
	@Final
	private EvokerFangsModel<EvokerFangs> model;

	@Inject(method = "render", at = @At("TAIL"))
	public void render(EvokerFangs p_114528_, float p_114529_, float p_114530_, PoseStack p_114531_, MultiBufferSource p_114532_, int p_114533_, CallbackInfo callbackInfo) {
		if (p_114528_ instanceof IEnchantVisual enchantVisual && enchantVisual.hasEnchantVisual()) {
			float f = p_114528_.getAnimationProgress(p_114530_);
			if (f != 0.0F) {
				float f1 = 2.0F;
				if (f > 0.9F) {
					f1 *= (1.0F - f) / 0.1F;
				}

				p_114531_.pushPose();
				p_114531_.mulPose(Axis.YP.rotationDegrees(90.0F - p_114528_.getYRot()));
				p_114531_.scale(-f1, -f1, f1);
				float f2 = 0.03125F;
				p_114531_.translate(0.0D, -0.626D, 0.0D);
				p_114531_.scale(0.5F, 0.5F, 0.5F);
				this.model.setupAnim(p_114528_, f, 0.0F, 0.0F, p_114528_.getYRot(), p_114528_.getXRot());
				VertexConsumer vertexconsumer = p_114532_.getBuffer(enchantSwirl(ItemRenderer.ENCHANTED_GLINT_ENTITY));
				this.model.renderToBuffer(p_114531_, vertexconsumer, p_114533_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
				p_114531_.popPose();
			}
		}
	}
}
