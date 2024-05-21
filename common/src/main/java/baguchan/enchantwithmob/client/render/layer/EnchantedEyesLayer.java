package baguchan.enchantwithmob.client.render.layer;

import baguchan.enchantwithmob.api.IEnchantCap;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;
import java.util.function.Predicate;

import static baguchan.enchantwithmob.client.render.EnchantRenderType.ENCHANTED_EYES;

public class EnchantedEyesLayer<T extends LivingEntity, M extends EntityModel<T>> extends EyesLayer<T, M> {


	public final RenderType render_types;
	public final Predicate<T> predicate;

	public EnchantedEyesLayer(RenderLayerParent<T, M> p_116964_, RenderType renderType, EntityType<?> entityType) {
		this(p_116964_, renderType, (entity) -> {
			return entity.getType() == entityType;
		});
	}

	public EnchantedEyesLayer(RenderLayerParent<T, M> p_116964_, RenderType renderType, Predicate<T> predicate) {
		super(p_116964_);

		this.render_types = renderType;
		this.predicate = predicate;
	}

	@Override
	public void render(PoseStack p_116983_, MultiBufferSource p_116984_, int p_116985_, T p_116986_, float p_116987_, float p_116988_, float p_116989_, float p_116990_, float p_116991_, float p_116992_) {
		if (p_116986_ instanceof IEnchantCap cap) {
			if (cap.getEnchantCap().hasEnchant() && predicate.test(p_116986_)) {
				VertexConsumer ivertexbuilder = p_116984_.getBuffer(this.renderType());
				this.getParentModel().renderToBuffer(p_116983_, ivertexbuilder, p_116985_, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
			}
		}
		;
	}

	public static RenderType enchantedEyes(ResourceLocation p_110455_) {
		return ENCHANTED_EYES.apply(p_110455_);
	}

	public RenderType renderType() {
		return render_types;
	}
}