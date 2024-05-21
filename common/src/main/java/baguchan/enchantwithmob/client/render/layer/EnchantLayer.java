package baguchan.enchantwithmob.client.render.layer;

import baguchan.enchantwithmob.EWConstants;
import baguchan.enchantwithmob.EnchantWithMob;
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
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;

import static baguchan.enchantwithmob.client.render.EnchantRenderType.enchantSwirl;

public class EnchantLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public static final ResourceLocation ANCIENT_GLINT = new ResourceLocation(EWConstants.MOD_ID, "textures/entity/ancient_glint.png");

    public EnchantLayer(RenderLayerParent<T, M> p_i50947_1_) {
        super(p_i50947_1_);
    }

    public void render(PoseStack poseStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float tick = (float) entitylivingbaseIn.tickCount + partialTicks;
        if (entitylivingbaseIn instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant() && !entitylivingbaseIn.isInvisible()) {
                float intensity = cap.getEnchantCap().getMobEnchants().size() < 3 ? ((float) cap.getEnchantCap().getMobEnchants().size() / 3) : 3;

                float f = (float) entitylivingbaseIn.tickCount + partialTicks;
                EntityModel<T> entitymodel = this.getParentModel();
                entitymodel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
                this.getParentModel().copyPropertiesTo(entitymodel);
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(enchantSwirl(cap.getEnchantCap().isAncient() ? ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ENTITY));
                entitymodel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                entitymodel.renderToBuffer(poseStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, intensity, intensity, intensity, 1.0F);
            }
        }
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