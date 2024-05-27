package baguchan.enchantwithmob.mixins.client;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.api.IEnchantedTime;
import baguchan.enchantwithmob.client.ClientEventHandler;
import baguchan.enchantwithmob.client.render.layer.EnchantLayer;
import baguchan.enchantwithmob.platform.Services;
import baguchan.enchantwithmob.registry.EWMobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void init(EntityRendererProvider.Context $$0, EntityModel $$1, float $$2, CallbackInfo ci) {
        LivingEntityRenderer<T, M> livingEntityRenderer = ((LivingEntityRenderer) ((Object) this));
        this.addLayer(new EnchantLayer<>(livingEntityRenderer));
    }

    @Shadow
    public abstract M getModel();

    @Shadow
    protected abstract boolean addLayer(RenderLayer<T, M> $$0);

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;prepareMobModel(Lnet/minecraft/world/entity/Entity;FFF)V", shift = At.Shift.BEFORE))
    public void setRenderWithTime(T instance, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo callbackInfo) {
        if (this.getModel() instanceof IEnchantedTime enchantedTime) {
            if (instance instanceof IEnchantCap enchantCap) {
                //ajust time
                int fastTime = Mth.clamp(MobEnchantUtils.getMobEnchantLevelFromHandler(enchantCap.getEnchantCap().getMobEnchants(), EWMobEnchants.HASTE), 0, 2);
                int slowTime = Mth.clamp(MobEnchantUtils.getMobEnchantLevelFromHandler(enchantCap.getEnchantCap().getMobEnchants(), EWMobEnchants.SLOW), 0, 2);
                float different = 1 + fastTime * 0.125F - slowTime * 0.125F;

                enchantedTime.setDifferentTime(different);
            }
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V", shift = At.Shift.AFTER))
    public void setRenderScales(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo callbackInfo) {
        if (p_115308_ instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                if (MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.SMALL)) {
                    int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.SMALL);
                    float cappedScale = Mth.clamp(0.15F * level, 0.0F, 0.9F);
                    p_115311_.scale(1.0F - cappedScale, 1.0F - cappedScale, 1.0F - cappedScale);
                } else if (MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.HUGE)) {
                    int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.HUGE);
                    p_115311_.scale(1.0F + 0.15F * level, 1.0F + 0.15F * level, 1.0F + 0.15F * level);
                } else if (Services.CONFIG_HANDLER.getChangeSizeWhenEnchant()) {
                    p_115311_.scale(1.05F, 1.05F, 1.05F);
                }
            }
        }
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    public void render(T instance, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo callbackInfo) {

        PoseStack matrixStack = p_115311_;
        MultiBufferSource bufferBuilder = p_115312_;
        float particalTick = p_115310_;
        if (instance instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().getEnchantOwner() != null) {

                LivingEntity entity = cap.getEnchantCap().getEnchantOwner();
                if (entity != null) {
                    ClientEventHandler.renderBeam(cap.getEnchantCap(), instance, particalTick, matrixStack, bufferBuilder, entity);
                }
            }
        }


    }
}