package baguchan.enchantwithmob.client.render.layer;

import baguchan.enchantwithmob.api.IEnchantCap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

import static baguchan.enchantwithmob.client.render.EnchantRenderType.enchantSwirl;
import static baguchan.enchantwithmob.client.render.layer.EnchantLayer.ANCIENT_GLINT;

public class SlimeEnchantLayer<T extends LivingEntity> extends RenderLayer<T, SlimeModel<T>> {
	private final EntityModel<T> model;

	public SlimeEnchantLayer(RenderLayerParent<T, SlimeModel<T>> p_174536_, EntityModelSet p_174537_) {
		super(p_174536_);
		this.model = new SlimeModel<>(p_174537_.bakeLayer(ModelLayers.SLIME_OUTER));
	}

	public void render(PoseStack p_117470_, MultiBufferSource p_117471_, int p_117472_, T p_117473_, float p_117474_, float p_117475_, float p_117476_, float p_117477_, float p_117478_, float p_117479_) {
		Minecraft minecraft = Minecraft.getInstance();
		if (p_117473_ instanceof IEnchantCap cap) {
			if (cap.getEnchantCap().hasEnchant()) {
				if (!p_117473_.isInvisible()) {
					float intensity = cap.getEnchantCap().getMobEnchants().size() < 3 ? ((float) cap.getEnchantCap().getMobEnchants().size() / 3) : 3;

					VertexConsumer vertexconsumer = p_117471_.getBuffer(enchantSwirl(cap.getEnchantCap().isAncient() ? ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ENTITY));

					this.getParentModel().copyPropertiesTo(this.model);
					this.model.prepareMobModel(p_117473_, p_117474_, p_117475_, p_117476_);
					this.model.setupAnim(p_117473_, p_117474_, p_117475_, p_117477_, p_117478_, p_117479_);
					this.model.renderToBuffer(p_117470_, vertexconsumer, p_117472_, LivingEntityRenderer.getOverlayCoords(p_117473_, 0.0F), intensity, intensity, intensity, 1.0F);
				}
			}
		}
		;
	}
}