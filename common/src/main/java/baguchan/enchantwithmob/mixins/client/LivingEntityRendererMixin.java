package baguchan.enchantwithmob.mixins.client;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.api.IEnchantedTime;
import baguchan.enchantwithmob.client.render.layer.EnchantLayer;
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
                int fastTime = Mth.clamp(MobEnchantUtils.getMobEnchantLevelFromHandler(enchantCap.getEnchantCap().getMobEnchants(), EWMobEnchants.HASTE.get()), 0, 2);
                int slowTime = Mth.clamp(MobEnchantUtils.getMobEnchantLevelFromHandler(enchantCap.getEnchantCap().getMobEnchants(), EWMobEnchants.SLOW.get()), 0, 2);
                float different = 1 + fastTime * 0.125F - slowTime * 0.125F;

                enchantedTime.setDifferentTime(different);
            }
        }
    }
}