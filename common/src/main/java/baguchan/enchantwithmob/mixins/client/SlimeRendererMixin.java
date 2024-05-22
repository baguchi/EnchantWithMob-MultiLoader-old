package baguchan.enchantwithmob.mixins.client;

import baguchan.enchantwithmob.client.render.layer.SlimeEnchantLayer;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SlimeRenderer.class)
public abstract class SlimeRendererMixin extends MobRenderer<Slime, SlimeModel<Slime>> {
    public SlimeRendererMixin(EntityRendererProvider.Context $$0, SlimeModel<Slime> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void init(EntityRendererProvider.Context $$0, CallbackInfo ci) {
        SlimeRenderer slimeRenderer = ((SlimeRenderer) ((Object) this));

        this.addLayer(new SlimeEnchantLayer<>(slimeRenderer, $$0.getModelSet()));
    }
}